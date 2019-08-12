package info.androidhive.bottomnavigation.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import info.androidhive.bottomnavigation.BottomNavigation;
import info.androidhive.bottomnavigation.MainActivity;
import info.androidhive.bottomnavigation.PostActivity;
import info.androidhive.bottomnavigation.R;
import info.androidhive.bottomnavigation.SocialPostActivity;
import info.androidhive.bottomnavigation.complaint;


public class StoreFragment extends Fragment {

    private CardView mComp;
    private RecyclerView mCompList;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase_votes;
    private DatabaseReference mDatabase_votes_child;
    private Query mQuery;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;

    private Boolean mProcessVote = false;
    public static int count;

    public StoreFragment() {
        // Required empty public constructor
    }

    public static StoreFragment newInstance(String param1, String param2) {
        StoreFragment fragment = new StoreFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_store, container, false);
        //mCompList = (CardView) view.findViewById(R.id.card1);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        String userid=mFirebaseUser.getUid();


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Social");
        mDatabase_votes = FirebaseDatabase.getInstance().getReference().child("Votes");
       // mDatabase_votes_child = FirebaseDatabase.getInstance().getReference().child("votes").child()
       // mQuery = mDatabase.orderByChild("uid").equalTo(userid);
        mCompList = (RecyclerView) view.findViewById(R.id.recycler2);
        //mCompList.hasFixedSize(true);
        mCompList.setLayoutManager(new LinearLayoutManager(getActivity()));



        mDatabase_votes.keepSynced(true);


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.menu2);

        fab.setOnClickListener(new OnClickListener()
        {

            public void onClick(View v)
            {
                //String ids = String.valueOf(v.getId()))
                //Button testbutton = (Button)findViewById(R.id.button1);
                Intent intent;
                intent = new Intent(StoreFragment.this.getActivity(), SocialPostActivity.class);
                startActivity(intent);
            }
        });
        return view;


    }

    @Override
    public void onStart()
    {
        super.onStart();

        FirebaseRecyclerAdapter<complaint,Complaint_view_holder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<complaint, Complaint_view_holder>(
                complaint.class,
                R.layout.activity_social_card,
                Complaint_view_holder.class,
                mDatabase

        ) {
            @Override
            protected void populateViewHolder(final Complaint_view_holder viewHolder, final complaint model, int position) {

                //if ((model.getUid()).equals(mFirebaseUser.getUid())) {

                final String Postkey = getRef(position).getKey();
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getContext(), model.getImage());
                viewHolder.setlikebtn(Postkey);



                mDatabase_votes_child = FirebaseDatabase.getInstance().getReference().child("votes").child(Postkey);



                viewHolder.mbutton2.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        mProcessVote = true;

                            mDatabase_votes.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {




                                    if(mProcessVote) {

                                        if (dataSnapshot.child(Postkey).hasChild(mAuth.getCurrentUser().getUid())) {

                                            mDatabase_votes.child(Postkey).child(mAuth.getCurrentUser().getUid()).removeValue();
                                            mProcessVote = false;
                                        } else {
                                            mDatabase_votes.child(Postkey).child(mAuth.getCurrentUser().getUid()).setValue("RandomValue");
                                            mProcessVote = false;


                                        }



                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                    }
                });

                mDatabase_votes.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //count = (int) dataSnapshot.child(Postkey).getChildrenCount();
                        count = (int) dataSnapshot.child(Postkey).getChildrenCount();

                        Toast.makeText
                                (getActivity().getApplicationContext(), "Dept--- : "+  dataSnapshot.getChildrenCount(), Toast.LENGTH_SHORT)
                                .show();

                        viewHolder.setVotes(String.valueOf(count));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




            }



        };

        mCompList.setAdapter(firebaseRecyclerAdapter);
    }
    public static class Complaint_view_holder extends RecyclerView.ViewHolder
    {

        View mview;
        Button mbutton;
        TextView mvotes;
        ImageButton mbutton2;
        DatabaseReference mDatabasevote;
        FirebaseAuth mAuth;
        public Complaint_view_holder(View itemView) {
            super(itemView);

            mview = itemView;
            //mbutton = (Button) mview.findViewById(R.id.vote);
            mbutton2 = (ImageButton) mview.findViewById(R.id.votebut);
            //mvotes = (TextView) mview.findViewById(R.id.total_votes);
            mDatabasevote = FirebaseDatabase.getInstance().getReference().child("Votes");
            mAuth = FirebaseAuth.getInstance();

            mDatabasevote.keepSynced(true);
        }
        public void setlikebtn(final String postkey)
        {
mDatabasevote.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if(dataSnapshot.child(postkey).hasChild(mAuth.getCurrentUser().getUid()))
        {
            mbutton2.setImageResource(R.drawable.baseline_thumb_up_black);
        }
        else
        {
            mbutton2.setImageResource(R.drawable.outline_thumb_up_alt_black);
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
});

        }
        public void setTitle(String title)
        {
            TextView Complaint_title = (TextView) mview.findViewById(R.id.post_title);
            Complaint_title.setText(title);
        }

        public void setDesc(String desc)
        {
            TextView Complaint_desc = (TextView) mview.findViewById(R.id.post_text);
            Complaint_desc.setText(desc);
        }

        public void setImage(Context ctx, String image)
        {
            ImageView post_image = (ImageView) mview.findViewById(R.id.post_image);
            Picasso.get().load(image).into(post_image);

        }

        public void setVotes(String votes)
        {
            TextView set_votes = (TextView) mview.findViewById(R.id.total_votes);
            set_votes.setText(votes);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

}


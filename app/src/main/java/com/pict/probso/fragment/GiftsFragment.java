package info.androidhive.bottomnavigation.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import info.androidhive.bottomnavigation.complaint;


public class GiftsFragment extends Fragment {

    private CardView mComp;
    private TextView check_comp;
    private RecyclerView mCompList;
    private DatabaseReference mDatabase;
    private Query mQuery;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;

    public GiftsFragment() {
        // Required empty public constructor
    }

    public static GiftsFragment newInstance(String param1, String param2) {
        GiftsFragment fragment = new GiftsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gifts, container, false);
        //mCompList = (CardView) view.findViewById(R.id.card1);

        check_comp = (TextView) view.findViewById(R.id.check_complaint);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        String userid=mFirebaseUser.getUid();


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Complaint");
        mQuery = mDatabase.orderByChild("uid").equalTo(userid);
        mQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(getActivity().getApplicationContext(),"data exists",Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(),"No data exists",Toast.LENGTH_SHORT).show();
                    check_comp.setVisibility(View.VISIBLE);
                    check_comp.setText("No Complaints Registered Yet.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mCompList = (RecyclerView) view.findViewById(R.id.recycler1);
        //mCompList.hasFixedSize(true);
        mCompList.setLayoutManager(new LinearLayoutManager(getActivity()));





        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.menu);

        fab.setOnClickListener(new OnClickListener()
        {

            public void onClick(View v)
            {
                //String ids = String.valueOf(v.getId()))
                //Button testbutton = (Button)findViewById(R.id.button1);
                Intent intent;
                intent = new Intent(GiftsFragment.this.getActivity(), PostActivity.class);
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
                R.layout.activity_card,
                Complaint_view_holder.class,
                mQuery

        ) {
            @Override
            protected void populateViewHolder(Complaint_view_holder viewHolder, complaint model, int position) {

                //if ((model.getUid()).equals(mFirebaseUser.getUid())) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getContext(), model.getImage());

            }
        };

        mCompList.setAdapter(firebaseRecyclerAdapter);
    }
    public static class Complaint_view_holder extends RecyclerView.ViewHolder
    {

        View mview;
        public Complaint_view_holder(View itemView) {
            super(itemView);

            mview = itemView;
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
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

}


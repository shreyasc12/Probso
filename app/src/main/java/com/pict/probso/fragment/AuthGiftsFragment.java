package info.androidhive.bottomnavigation.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import info.androidhive.bottomnavigation.AuthRecycler;
import info.androidhive.bottomnavigation.BottomNavigation;
import info.androidhive.bottomnavigation.MainActivity;
import info.androidhive.bottomnavigation.PostActivity;
import info.androidhive.bottomnavigation.R;
import info.androidhive.bottomnavigation.RecycleronClick;
import info.androidhive.bottomnavigation.UserData;
import info.androidhive.bottomnavigation.complaint;

import static com.android.volley.VolleyLog.TAG;


public class AuthGiftsFragment extends Fragment {

    private CardView mComp;
    private TextView check_authcomp;
    private RecyclerView mCompList;
    private DatabaseReference mDatabase;
    private Query mQuery;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    UserData user;


    public AuthGiftsFragment() {
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

        View view = inflater.inflate(R.layout.fragment_auth_gifts, container, false);
        //mCompList = (CardView) view.findViewById(R.id.card1);

        check_authcomp = (TextView) view.findViewById(R.id.check_authcomp);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        String userId=mFirebaseUser.getUid();
        String email = mFirebaseUser.getEmail();
        String[] arrOfStr = email.split("\\.");
        String dept_name = arrOfStr[0];


        /*DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child("Users").child(userId);


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 user = dataSnapshot.getValue(ProfileFragment.UserData.class);
                 AuthGiftsFragment.dept_name = user.FirstName;

                /*Toast.makeText
                        (getActivity().getApplicationContext(), "Dept--- : "+dept_name  , Toast.LENGTH_SHORT)
                        .show();*/

        // Check for null
        if (user == null) {
            Log.e("++++++++++******++++++", "******************************User data is null!");

        }

                /*Toast.makeText
                        (getActivity().getApplicationContext(), "Selected : " + user.FirstName + user.LastName + "**" + user.Address + user.Age + "*" +user.Mobno , Toast.LENGTH_SHORT)
                        .show();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.e(TAG, "Failed to read user", databaseError.toException());
            }
        });*/

        //String dept_name = user.FirstName;


       /* Toast.makeText
                (getActivity().getApplicationContext(), "Dept--- : "+AuthGiftsFragment.dept_name  , Toast.LENGTH_SHORT)
                .show();*/





        mDatabase = FirebaseDatabase.getInstance().getReference().child("Complaint");
        mQuery = mDatabase.orderByChild("Dept").equalTo(dept_name);

        mQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(getActivity().getApplicationContext(),"data exists",Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(),"No data exists",Toast.LENGTH_SHORT).show();
                    check_authcomp.setVisibility(View.VISIBLE);
                    check_authcomp.setText("No Complaints Concerning Your Department have been Registered.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        mCompList = (RecyclerView) view.findViewById(R.id.recycler2);
        //mCompList.hasFixedSize(true);
        mCompList.setLayoutManager(new LinearLayoutManager(getActivity()));





      /*  FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.menu);

        fab.setOnClickListener(new OnClickListener()
        {

            public void onClick(View v)
            {
                //String ids = String.valueOf(v.getId()))
                //Button testbutton = (Button)findViewById(R.id.button1);
                Intent intent;
                intent = new Intent(AuthGiftsFragment.this.getActivity(), PostActivity.class);
                startActivity(intent);
            }
        });*/
        return view;


    }

    @Override
    public void onStart()
    {
        super.onStart();

        FirebaseRecyclerAdapter<AuthRecycler,Complaint_view_holder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<AuthRecycler, Complaint_view_holder>(
                AuthRecycler.class,
                R.layout.activity_authcard,
                Complaint_view_holder.class,
                mQuery

        ) {
            @Override
            protected void populateViewHolder(Complaint_view_holder viewHolder, AuthRecycler model, int position) {

                final String Complaint_key = getRef(position).getKey();
                viewHolder.setTitle(model.getTitle());
                //viewHolder.setDesc(model.getDesc());
                //viewHolder.setImage(getContext(),model.getImage());
                viewHolder.mview.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(AuthGiftsFragment.this.getActivity(), RecycleronClick.class);
                        intent.putExtra("key",Complaint_key);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);

                    }
                });
            }
        };

        mCompList.setAdapter(firebaseRecyclerAdapter);
    }
    //VIEW HOLDER
    public static class Complaint_view_holder extends RecyclerView.ViewHolder
    {

        View mview;
        public Complaint_view_holder(View itemView) {
            super(itemView);

            mview = itemView;
        }
        public void setTitle(String title)
        {
            TextView Complaint_title = (TextView) mview.findViewById(R.id.title);
            Complaint_title.setText(title);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

}


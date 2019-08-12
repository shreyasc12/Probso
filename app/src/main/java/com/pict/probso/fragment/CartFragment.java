package info.androidhive.bottomnavigation.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import info.androidhive.bottomnavigation.AuthRecycler;
import info.androidhive.bottomnavigation.MessageUseronClick;
import info.androidhive.bottomnavigation.R;
import info.androidhive.bottomnavigation.RecycleronClick;
import info.androidhive.bottomnavigation.messages;

public class CartFragment extends Fragment {

    private RecyclerView mCompList;
    private TextView check_msg;
    private DatabaseReference mDatabase;
    private Query mQuery;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;

    public CartFragment() {
        // Required empty public constructor
    }

    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_cart, container, false);

        check_msg = (TextView) view.findViewById(R.id.check_msg) ;
        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        String userid=mFirebaseUser.getUid();


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Messages");
        mQuery = mDatabase.orderByChild("To").equalTo(userid);
        mQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(getActivity().getApplicationContext(),"data exists",Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(),"No data exists",Toast.LENGTH_SHORT).show();
                    check_msg.setVisibility(View.VISIBLE);
                    check_msg.setText("No Acknowledgements Sent.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mCompList = (RecyclerView) view.findViewById(R.id.recycler5);
        //mCompList.hasFixedSize(true);
        mCompList.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;

    }

    @Override
    public void onStart()
    {
        super.onStart();

        FirebaseRecyclerAdapter<messages,CartFragment.Messsage_view_holder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<messages,Messsage_view_holder>(
                messages.class,
                R.layout.activity_message_view,
                CartFragment.Messsage_view_holder.class,
                mQuery

        ) {

            protected void populateViewHolder(CartFragment.Messsage_view_holder viewHolder, messages model, int position) {

                final String Message_key = getRef(position).getKey();
                viewHolder.setTitle(model.getTitle());
                viewHolder.setTimestamp(model.getTimestamp());
                //viewHolder.setDesc(model.getDesc());
                //viewHolder.setImage(getContext(),model.getImage());
                viewHolder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(CartFragment.this.getActivity(), MessageUseronClick.class);
                        intent.putExtra("msg",Message_key);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.slide_in_right);
                        Toast.makeText
                                (getActivity().getApplicationContext(), "Msg--- : "+ Message_key  , Toast.LENGTH_SHORT)
                                .show();

                    }
                });
            }
        };

        mCompList.setAdapter(firebaseRecyclerAdapter);
    }
    //VIEW HOLDER
    public static class Messsage_view_holder extends RecyclerView.ViewHolder
    {

        View mview;
        public Messsage_view_holder(View itemView) {
            super(itemView);

            mview = itemView;
        }
        public void setTitle(String title)
        {
            TextView Message_title = (TextView) mview.findViewById(R.id.title);
            Message_title.setText(title);
        }

        public void setTimestamp(String timestamp)
        {
            TextView Message_timestamp = (TextView) mview.findViewById(R.id.timestamp);
            Message_timestamp.setText(timestamp);
        }
    }
}

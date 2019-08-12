package info.androidhive.bottomnavigation.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import info.androidhive.bottomnavigation.Edit_Profile;
import info.androidhive.bottomnavigation.ForgetAndChangePasswordActivity;
import info.androidhive.bottomnavigation.LoginActivity;
import info.androidhive.bottomnavigation.MainActivity;
import info.androidhive.bottomnavigation.R;
import info.androidhive.bottomnavigation.RegisterActivity;
import info.androidhive.bottomnavigation.SendReplyActivity;
import info.androidhive.bottomnavigation.UserData;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.UUID;

//import static android.databinding.DataBindingUtil.setContentView;
import static com.android.volley.VolleyLog.TAG;


public class ProfileFragment extends Fragment {
    Button btnSignOut;
    ImageView btnprofile;
    FirebaseAuth auth;
    FirebaseUser user;
    TextView Name,Location,Email,Age,Gender,Mobno;



    public ProfileFragment() {
        // Required empty public constructor
    }


    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();


    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_profile, container, false);

        btnSignOut = (Button) root.findViewById(R.id.SignOut);//getting error here in //findViewById
        btnprofile = (ImageView) root.findViewById(R.id.edit);
        Drawable woman_image= getResources().getDrawable(R.drawable.woman,null);
        Drawable man_image= getResources().getDrawable(R.drawable.man,null);
        Name = (TextView)root.findViewById(R.id.name);
        Location = (TextView)root.findViewById(R.id.location);
        Email = (TextView)root.findViewById(R.id.email);
        Age = (TextView)root.findViewById(R.id.age);
        Gender = (TextView)root.findViewById(R.id.gender);
        Mobno = (TextView)root.findViewById(R.id.mobileNumber);


        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child("Users");
        FirebaseUser user =  auth.getInstance().getCurrentUser();
        String email = user.getEmail();
        Email.setText(email);
        String userId = user.getUid();


        ref.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserData user = dataSnapshot.getValue(UserData.class);
                String Add = user.getAddress().toString();
                String edge = user.getAge().toString();
                String mob = user.getMobno().toString();
                String name = user.getFirstName().toString() + " " + user.getLastName().toString();

                // Check for null
                if (user == null) {
                    Log.e(TAG, "User data is null!");
                    return;
                }

                /*Toast.makeText
                        (getActivity().getApplicationContext(), "Selected : " + user.FirstName + user.LastName + "**" + user.Address + user.Age + "*" +user.Mobno , Toast.LENGTH_SHORT)
                        .show();*/

                Name.setText(name);
                Location.setText(user.getAddress());
                Age.setText(user.getAge());
                Mobno.setText(user.getMobno());
                Gender.setText(user.getGender());

                if(user.getGender() == "Male")
                {
                    btnprofile.setImageResource(R.drawable.man);
                }
                else if(user.getGender() == "Female")
                {
                    btnprofile.setImageResource(R.drawable.woman);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.e(TAG, "Failed to read user", databaseError.toException());
            }
        });






        btnSignOut.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                auth.signOut();
                Intent intent;
                intent = new Intent(ProfileFragment.this.getActivity(), LoginActivity.class);
                startActivity(intent);
            }

        });

        btnprofile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;

                intent = new Intent(ProfileFragment.this.getActivity(),Edit_Profile.class);
                startActivity(intent);
            }
        });



        return root;
    }




    /*public void addListenerOnButton(View v) {

        btnSignOut = (Button) v.findViewById(R.id.SignOut); //getting error here in //findViewById

        btnSignOut.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.i("","shre****************************");
                auth.signOut();
                Intent intent;
                intent = new Intent(ProfileFragment.this.getActivity(), LoginActivity.class);
                startActivity(intent);
            }

        });




    }*/

}


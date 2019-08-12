package info.androidhive.bottomnavigation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Button btnSignOut;
    FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    FirebaseUser user;
    ProgressDialog PD,PD1;
    boolean authority;



    @Override    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        /*PD1 = new ProgressDialog(this);
        PD1.setMessage("Wait a Sec...");
        PD1.setCancelable(true);
        PD1.setCanceledOnTouchOutside(false);*/


        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            // user auth state is changed - user is null
            // launch login activity
            startActivity(new Intent(MainActivity.this, AuthLoginActivity.class));
            finish();
        }
        else if( user != null)
        {

            String userid = user.getUid();
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            DatabaseReference ref = database.child("Authorities").child(userid);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){

                        authority=true;
                        Intent intent = new Intent(MainActivity.this, AuthBottomNavigation.class);
                        startActivity(intent);
                        finish();

                    }
                    else
                    {
                        authority =false;
                        Intent intent = new Intent(MainActivity.this, BottomNavigation.class);
                        startActivity(intent);
                        finish();
                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            //startActivity(new Intent(MainActivity.this, BottomNavigation.class));
        }











        /*authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                Log.i("123","**************************hey!!!!!!!!!!!!!!!!!!!!!!!!!**********************");
                Log.i("123","**************************hey!!!!!!!!!!!!!!!!!!!!!!!!!**********************");
                Log.i("123","**************************hey!!!!!!!!!!!!!!!!!!!!!!!!!**********************");
                Log.i("123","**************************hey!!!!!!!!!!!!!!!!!!!!!!!!!**********************");
                Log.i("123","**************************hey!!!!!!!!!!!!!!!!!!!!!!!!!**********************");

                Toast.makeText(
                        MainActivity.this,
                        "//////////////////////YESSS////////////////////////",
                        Toast.LENGTH_LONG).show();

                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
                else if( user != null)
                {
                    String userid = user.getUid();
                    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference ref = database.child("Authorities").child(userid);

                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){

                                authority=true;
                                Intent intent = new Intent(MainActivity.this, AuthBottomNavigation.class);
                                startActivity(intent);
                                finish();

                            }
                            else
                            {
                                authority =false;
                                Intent intent = new Intent(MainActivity.this, BottomNavigation.class);
                                startActivity(intent);
                                finish();
                            }

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    startActivity(new Intent(MainActivity.this, BottomNavigation.class));
                }
            }
        };*/



        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);

        btnSignOut = (Button) findViewById(R.id.sign_out_button);

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override            public void onClick(View view) {
                auth.signOut();
                FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user == null) {
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finish();
                        }
                    }
                };
            }
        });

        findViewById(R.id.change_password_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ForgetAndChangePasswordActivity.class).putExtra("Mode", 1));
            }
        });

        findViewById(R.id.change_email_button).setOnClickListener(new View.OnClickListener() {
            @Override            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ForgetAndChangePasswordActivity.class).putExtra("Mode", 2));
            }
        });

        findViewById(R.id.delete_user_button).setOnClickListener(new View.OnClickListener() {
            @Override            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ForgetAndChangePasswordActivity.class).putExtra("Mode", 3));
            }
        });
    }

    @Override    protected void onResume() {
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
        else
        {
            if (authority == false)
            {
                startActivity(new Intent(MainActivity.this, BottomNavigation.class));
            }
            else if(authority == true)
            {
                startActivity(new Intent(MainActivity.this, AuthBottomNavigation.class));
            }
        }
        super.onResume();
    }
}


package info.androidhive.bottomnavigation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AuthLoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private TextView Authlog;
    private FirebaseAuth auth;
    private FirebaseUser Fireuser;
    private Button  btnLogin;
    private ProgressDialog PD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_login_activity);


        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);
        auth = FirebaseAuth.getInstance();

        inputEmail = (EditText) findViewById(R.id.authemail);
        inputPassword = (EditText) findViewById(R.id.authpassword);
        btnLogin = (Button) findViewById(R.id.authsign_in_button);
        Authlog = (TextView) findViewById(R.id.Userlogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                try {

                    if (password.length() > 0 && email.length() > 0) {
                        PD.show();
                        auth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(AuthLoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Fireuser = auth.getCurrentUser();
                                            String curr_uid = Fireuser.getUid();


                                            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                                            DatabaseReference ref = database.child("Authorities").child(curr_uid);

                                            ref.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()){

                                                        Intent intent = new Intent(AuthLoginActivity.this, AuthBottomNavigation.class);
                                                        startActivity(intent);
                                                        finish();

                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(
                                                                AuthLoginActivity.this,
                                                                "Either You are not authority or check your crentials!",
                                                                Toast.LENGTH_LONG).show();
                                                        auth.signOut();
                                                        Intent intent = new Intent(AuthLoginActivity.this, LoginActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }

                                                }
                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });


                                        } else {
                                            Toast.makeText(
                                                    AuthLoginActivity.this,
                                                    "Authentication Failed",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                        PD.dismiss();
                                    }
                                });
                    } else {
                        Toast.makeText(
                                AuthLoginActivity.this,
                                "Fill All Fields",
                                Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.Userlogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });



    }
}

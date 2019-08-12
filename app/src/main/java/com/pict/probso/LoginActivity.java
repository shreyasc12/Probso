package info.androidhive.bottomnavigation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private Button btnSignUp, btnLogin;
    private android.widget.TextView sign_up;
    private ProgressDialog PD;
    private FirebaseUser Fireuser;

    @Override    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);
        auth = FirebaseAuth.getInstance();

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        //btnSignUp = (Button) findViewById(R.id.sign_up_button);
        btnLogin = (Button) findViewById(R.id.sign_in_button);
        sign_up = (android.widget.TextView) findViewById(R.id.sign_up_button);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override            public void onClick(View view) {
                final String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                try {

                    if (password.length() > 0 && email.length() > 0) {
                        PD.show();
                        auth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
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

                                                        Toast.makeText(
                                                                LoginActivity.this,
                                                                "Please Login through Authority Login.",
                                                                Toast.LENGTH_LONG).show();
                                                        auth.signOut();
                                                        Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                                                        startActivity(intent);
                                                        finish();

                                                    }
                                                    else
                                                    {

                                                        Intent intent = new Intent(LoginActivity.this, BottomNavigation.class);
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
                                                    LoginActivity.this,
                                                    "Authentication Failed",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                        PD.dismiss();
                                    }
                                });
                    } else {
                        Toast.makeText(
                                LoginActivity.this,
                                "Fill All Fields",
                                Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.forget_password_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ForgetAndChangePasswordActivity.class).putExtra("Mode", 0));
            }
        });

        findViewById(R.id.Authlogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AuthLoginActivity.class));
            }
        });

    }

    @Override    protected void onResume() {
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        super.onResume();
    }
}
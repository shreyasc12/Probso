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
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private EditText inputFirstName,inputLastName,inputAddress,inputAge,inputMobno,inputConfirm;
    private FirebaseAuth auth;
    private Button btnSignUp, btnLogin;
    private ProgressDialog PD;
    public String selectedItemText;



    @Override    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final android.widget.Spinner spinner = (android.widget.Spinner) findViewById(R.id.spinner);

        // Initializing a String Array
        String[] plants = new String[]{
                "Gender",
                "Male",
                "Female",
                "Rather Not Say"
        };

        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        }

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
         inputFirstName = (EditText) findViewById(R.id.FirstName);
        inputLastName = (EditText) findViewById(R.id.LastName);
        inputConfirm = (EditText) findViewById(R.id.confirmpassword);
        inputAddress = (EditText) findViewById(R.id.Address);
        inputAge = (EditText) findViewById(R.id.Age);
        inputMobno = (EditText) findViewById(R.id.Mobno);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        btnLogin = (Button) findViewById(R.id.sign_in_button);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override            public void onClick(View view) {
                final String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();
                final String confirmpassword = inputConfirm.getText().toString();
                final String Firstname = inputFirstName.getText().toString();
                final String Lastname = inputLastName.getText().toString();
                final String Address = inputAddress.getText().toString();
                final String Age = inputAge.getText().toString();
                final String Mobno = inputMobno.getText().toString();

                if(password == confirmpassword) {
                    Toast.makeText(
                            RegisterActivity.this,
                            "->" + email + "*" + password + "*" + confirmpassword + Firstname + Lastname + "++" + Address + Age + Mobno,
                            Toast.LENGTH_LONG).show();
                }

                try {
                    if (password.length() > 5 && email.length() > 0  ) {
                        PD.show();
                        auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(
                                                    RegisterActivity.this,
                                                    "Authentication Failed",
                                                    Toast.LENGTH_LONG).show();
                                            Log.v("error", task.getResult().toString());
                                        } else {

                                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                                           FirebaseUser user =  auth.getInstance().getCurrentUser();
                                           String userId = user.getUid();
                                           String email = user.getEmail();
                                           com.google.firebase.database.DatabaseReference mRef =  database.getReference().child("Users").child(userId);
                                           mRef.child("FirstName").setValue(Firstname);
                                           mRef.child("LastName").setValue(Lastname);
                                           mRef.child("Address").setValue(Address);
                                           mRef.child("Age").setValue(Age);
                                           mRef.child("Mobno").setValue(Mobno);
                                           mRef.child("Type").setValue("Citizen");
                                            mRef.child("Email").setValue(email);
                                            mRef.child("Gender").setValue(selectedItemText);


                                            Toast.makeText(
                                                    RegisterActivity.this,
                                                    "Successfully Signed Up!!!",
                                                    Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(RegisterActivity.this, BottomNavigation.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        PD.dismiss();
                                    }
                                });
                    } else {
                        Toast.makeText(
                                RegisterActivity.this,
                                "Fill All Fields",
                                Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override            public void onClick(View view) {
                finish();
            }
        });

        final java.util.List<String> plantsList = new ArrayList<>(Arrays.asList(plants));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item,plantsList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0){
                    // Notify the selected item text

                            Toast.makeText
                            (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                            .show();
                  /*  FirebaseDatabase database = FirebaseDatabase.getInstance();
                    FirebaseUser user =  auth.getInstance().getCurrentUser();
                    String userId = user.getUid();
                    DatabaseReference mRef =  database.getReference().child("Users").child(userId);
                    mRef.child("Gender").setValue(selectedItemText);*/
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
}
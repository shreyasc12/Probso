package info.androidhive.bottomnavigation;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PostSignupActivity extends AppCompatActivity {

    private EditText inputFirstName,inputLastName,inputAddress,inputAge,inputMobno;
    private Button btnGetStarted;
    FirebaseAuth auth;
    FirebaseUser user;
    public String selectedItemText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_signup_activity);

        // Get reference of widgets from XML layout
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Initializing a String Array
        String[] plants = new String[]{
                "Gender",
                "Male",
                "Female",
                "Rather Not Say"
        };

        inputFirstName = (TextInputEditText) findViewById(R.id.firstName);
        inputLastName = (TextInputEditText) findViewById(R.id.LastName);
        inputAddress = (TextInputEditText) findViewById(R.id.Address);
        inputAge = (TextInputEditText) findViewById(R.id.Age);
        inputMobno = (TextInputEditText) findViewById(R.id.Mobno);

        btnGetStarted = (Button) findViewById(R.id.GetStarted);

        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Firstname = inputFirstName.getText().toString();
                final String Lastname = inputLastName.getText().toString();
                final String Address = inputAddress.getText().toString();
                final String Age = inputAge.getText().toString();
                final String Mobno = inputMobno.getText().toString();



                FirebaseDatabase database = FirebaseDatabase.getInstance();
                FirebaseUser user =  auth.getInstance().getCurrentUser();
                String userId = user.getUid();
                String email = user.getEmail();
                DatabaseReference mRef =  database.getReference().child("Users").child(userId);
                mRef.child("FirstName").setValue(Firstname);
                mRef.child("LastName").setValue(Lastname);
                mRef.child("Address").setValue(Address);
                mRef.child("Age").setValue(Age);
                mRef.child("Mobno").setValue(Mobno);
                mRef.child("Type").setValue("Citizen");
                mRef.child("Email").setValue(email);
                mRef.child("Gender").setValue(selectedItemText);

                Toast.makeText
                        (getApplicationContext(), "Selected : " + Firstname + Lastname + "**" + Address + Age + "*" +Mobno, Toast.LENGTH_LONG)
                        .show();

                startActivity(new Intent(PostSignupActivity.this, BottomNavigation.class));



            }
        });



        final List<String> plantsList = new ArrayList<>(Arrays.asList(plants));

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
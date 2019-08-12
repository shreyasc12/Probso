package info.androidhive.bottomnavigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import info.androidhive.bottomnavigation.fragment.ProfileFragment;

import static com.android.volley.VolleyLog.TAG;


public class SendReplyActivity extends AppCompatActivity {


    /*FileInputStream serviceAccount = new FileInputStream("/home/vindeep/Androidapp/final/BottomNavigation/app/google-services.json");

    FirebaseOptions options = new FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setDatabaseUrl("https://Probso.firebaseio.com/")
            .build();

    FirebaseApp.initializeApp(this);*/



    FirebaseAuth auth;
    FirebaseUser user;
    TextInputEditText Textto,Texttitle,Textmessage;
    Button SendReply;



    public SendReplyActivity() throws IOException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_reply);

        Textto = (TextInputEditText)findViewById(R.id.txtTo);
        Texttitle = (TextInputEditText)findViewById(R.id.txtTitle);
        SendReply = (Button)findViewById(R.id.btnSend);

        Textmessage = (TextInputEditText)findViewById(R.id.txtMessage);



        /*Title = (TextView) findViewById(R.id.title);
        Description = (TextView) findViewById(R.id.description);
        comp_img = (ImageView) findViewById(R.id.complaint_img);
        btnSend = (Button) findViewById(R.id.Reply);*/

        Intent intent = getIntent();
        final String uid = intent.getStringExtra("cust_uid");
        final String cid = intent.getStringExtra("complaint_key");
        final String complaint_title = intent.getStringExtra("cust_title");
        Texttitle.setText(complaint_title);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child("Users");
        FirebaseUser user =  auth.getInstance().getCurrentUser();


        ref.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               UserData user = dataSnapshot.getValue(UserData.class);
               String To = user.getFirstName()+" " +user.getLastName();
               Textto.setText(To);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });


        SendReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy hh:mm aaa");
                String format = s.format(new Date());

                final String To = Textto.getText().toString();
                final String Title = Texttitle.getText().toString();
                final String Message = Textmessage.getText().toString();

                FirebaseDatabase data = FirebaseDatabase.getInstance();
                FirebaseUser user1 =  auth.getInstance().getCurrentUser();
                String AuthId = user1.getUid();
                String email = user1.getEmail();
                DatabaseReference mRef =  data.getReference().child("Messages").child(String.valueOf(UUID.randomUUID()));
                mRef.child("From").setValue(AuthId);
                mRef.child("ComplaintUid").setValue(cid);
                mRef.child("To").setValue(uid);
                mRef.child("Timestamp").setValue(format);
                mRef.child("Title").setValue(Title);
                mRef.child("Message").setValue(Message);

                Toast.makeText
                        (getApplicationContext(), "Message Sent " , Toast.LENGTH_SHORT)
                        .show();

                startActivity(new Intent(SendReplyActivity.this, AuthBottomNavigation.class));
                overridePendingTransition(R.anim.slide_in_up,R.anim.slide_out_up);


            }
        });








        //Toast.makeText(getApplicationContext(), userRecord.getEmail() + "***" + complaint_title, Toast.LENGTH_SHORT).show();

    }



}

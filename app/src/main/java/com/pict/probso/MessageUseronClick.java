package info.androidhive.bottomnavigation;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.EventLogTags;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MessageUseronClick extends AppCompatActivity {

    TextView msgtitle,msgdesc,msgfrom,msgtimestamp;
    android.widget.ImageButton delete;
    String message_key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_useron_click);

        msgdesc = (TextView) findViewById(R.id.msgdesc);
        msgtitle = (TextView) findViewById(R.id.msgtitle) ;
        msgtimestamp = (TextView) findViewById(R.id.msgtimesstamp);
        msgfrom = (TextView) findViewById(R.id.msgfrom);
        delete = (android.widget.ImageButton) findViewById(R.id.delete);

        Intent intent = getIntent();
        message_key = intent.getStringExtra("msg");

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference ref = database.child("Messages");
        final DatabaseReference ref1 = database.child("Users");


        ref.child(message_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    messages msg = dataSnapshot.getValue(messages.class);

                /*Toast.makeText
                        (getApplicationContext(), "Selected : " + msg.getTitle()+ " "+msg.getMessage()+"**"+msg.getFrom()+"**" + msg.getTimestamp() , Toast.LENGTH_SHORT)
                        .show();*/
                    msgtitle.setText(msg.getTitle());
                    msgdesc.setText(msg.getMessage());
                    msgtimestamp.setText(msg.getTimestamp());

                    String from = msg.getFrom();

                    ref1.child(from).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            UserData from = dataSnapshot.getValue(UserData.class);

                            String name = from.getFirstName().toString() + " " + from.getLastName().toString();

                            msgfrom.setText(name);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog diaBox = AskOption();
                diaBox.show();
            }
        });


    }

    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete Message?")
                .setIcon(R.drawable.baseline_delete_black_24dp)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code

                        DatabaseReference del = FirebaseDatabase.getInstance().getReference().child("Messages").child(message_key);
                        del.setValue(null);
                        Intent intent = new Intent(MessageUseronClick.this,MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_in_right);
                        dialog.dismiss();
                    }

                })



                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;


    }

}

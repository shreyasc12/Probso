package info.androidhive.bottomnavigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import info.androidhive.bottomnavigation.fragment.AuthGiftsFragment;

public class RecycleronClick extends AppCompatActivity{

    TextView Title,Description;
    ImageView comp_img;
    Button btnSend;

    public static class Complaint_info {

        String desc,image;
        String title,uid,dept;

        public Complaint_info(){
            //constructor
        }

        public Complaint_info(String desc, String image, String title, String uid, String dept)
        {
            this.desc=desc;
            this.dept=dept;
            this.title=title;
            this.uid=uid;
            this.image=image;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_complaint);

        Title = (TextView) findViewById(R.id.title);
        Description = (TextView) findViewById(R.id.description);
        comp_img = (ImageView) findViewById(R.id.complaint_img);
        btnSend = (Button) findViewById(R.id.reply);

        Intent intent = getIntent();
        final String complaint_key = intent.getStringExtra("key");
        //Toast.makeText(getApplicationContext(),complaint_key,Toast.LENGTH_SHORT).show();
        //final complaint comp;
        // final Complaint_info[] complaint_obj = new Complaint_info[1];
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child("Complaint");

        ref.child(complaint_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 //complaint_obj[0] = dataSnapshot.getValue(Complaint_info.class);
            final complaint comp = dataSnapshot.getValue(complaint.class);

                Toast.makeText
                        (getApplicationContext(), "Selected : " + comp.getTitle() + "**" + comp.getDesc() + "**"+ comp.getDept()  , Toast.LENGTH_SHORT)
                        .show();
                Title.setText(comp.getTitle());
                Description.setText(comp.getDesc());

                /*try {
                    Picasso.with(RecycleronClick.this).load(complaint_obj.image).placeholder(R.id.complaint_img).into(Photo);
                } catch (Exception e) {
                    e.printStackTrace();
                }*/

                Glide.with(RecycleronClick.this).asBitmap().load(comp.getImage()).into(comp_img);

                btnSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String Customer_uid = comp.getUid();
                        String Comp_title = comp.getTitle();
                        Intent intent = new Intent(RecycleronClick.this, SendReplyActivity.class);
                        intent.putExtra("cust_uid",Customer_uid);
                        intent.putExtra("cust_title",Comp_title);
                        intent.putExtra("complaint_key",complaint_key);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_up,R.anim.slide_out_up);
                    }
                });


            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

}

package info.androidhive.bottomnavigation;

//import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import info.androidhive.bottomnavigation.fragment.GiftsFragment;

public class PostActivity extends AppCompatActivity {

    private ImageButton mSelectImage;
    private EditText mPostTitle;
    private EditText mPostDesc;
    private Button mSubmitbtn;
    private Uri mImageUri = null;
    private ProgressDialog mProgress;

    public String selectedItemText;

    //private StorageReference mStorage;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    //storing into database
    private DatabaseReference mDatabase;

    private static final int GALLERY_REQUEST = 1;

    //@SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        // Get reference of widgets from XML layout
        final Spinner spinner = (Spinner) findViewById(R.id.spinner_post);

        // Initializing a String Array
        String[] plants = new String[]{
                "waterdept",
                "roaddept",
                "solidwastedept",
                "electricaldept"
        };


        //mStorage = FirebaseStorage.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Complaint");

        mSelectImage = (ImageButton) findViewById(R.id.selctimg);
        mPostTitle = (EditText) findViewById(R.id.complaint);
        mPostDesc = (EditText) findViewById(R.id.desc);
        mSubmitbtn = (Button) findViewById(R.id.SUBMIT);

        mProgress = new ProgressDialog(this);

        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);

            }
        });

        mSubmitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();

            }
        });



        final List<String> plantsList = new ArrayList<>(Arrays.asList(plants));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item,plantsList){
            @Override
            public boolean isEnabled(int position){

                    return true;

            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                    tv.setTextColor(Color.BLACK);

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

    private void startPosting() {
        if (mImageUri != null) {
            final String title_val = mPostTitle.getText().toString().trim();
            final String desc_val = mPostDesc.getText().toString().trim();
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            ref.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            //adding everything to database
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();

                            DatabaseReference newComplaint = mDatabase.push();

                            newComplaint.child("title").setValue(title_val);
                            newComplaint.child("desc").setValue(desc_val);
                            newComplaint.child("image").setValue(downloadUrl.toString());
                            newComplaint.child("uid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            newComplaint.child("Dept").setValue(selectedItemText);


                            progressDialog.dismiss();
                            Toast.makeText(PostActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(PostActivity.this, BottomNavigation.class));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(PostActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());

                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK)
        {
            mImageUri =data.getData();
            mSelectImage.setImageURI(mImageUri);
        }

    }








}


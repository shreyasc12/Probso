package info.androidhive.bottomnavigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.EventLogTags;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import info.androidhive.bottomnavigation.fragment.AuthCartFragment;
import info.androidhive.bottomnavigation.fragment.AuthGiftsFragment;
import info.androidhive.bottomnavigation.fragment.CartFragment;
import info.androidhive.bottomnavigation.fragment.GiftsFragment;
import info.androidhive.bottomnavigation.fragment.ProfileFragment;
import info.androidhive.bottomnavigation.fragment.StoreFragment;
import info.androidhive.bottomnavigation.helper.BottomNavigationBehavior;

import static com.android.volley.VolleyLog.TAG;

public class BottomNavigation extends AppCompatActivity {

    private ActionBar toolbar;
    private DatabaseReference mDatabase,ref;
    private Query mQuery;
    public String type;
    //public FirebaseAuth mAuth;
    //private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottomnav);

        toolbar = getSupportActionBar();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // attaching bottom sheet behaviour - hide / show on scroll
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        // load the store fragment by default
        toolbar.setTitle("Shop");
        loadFragment(new StoreFragment());


    }

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
    String userId=mFirebaseUser.getUid();


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;

            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            DatabaseReference ref = database.child("Users");
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
            String userId=mFirebaseUser.getUid();



            ref.child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserData usertype = dataSnapshot.getValue(UserData.class);
                     type = usertype.getType();

                    // Check for null
                    if (usertype == null) {
                        Log.e(TAG, "User data is null!");
                        return;
                    }

                Toast.makeText
                        (getApplicationContext(), "Type- : " + type, Toast.LENGTH_SHORT)
                        .show();


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Log.e(TAG, "Failed to read user", databaseError.toException());
                }
            });

            Toast.makeText
                    (getApplicationContext(), "Type- : " + type, Toast.LENGTH_SHORT)
                    .show();


            switch (item.getItemId()) {

                case R.id.navigation_shop:
                    toolbar.setTitle("Home");
                    fragment = new StoreFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_gifts:
                    toolbar.setTitle("My Complaints");
                    fragment = new GiftsFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_cart:
                    toolbar.setTitle("Notifications");
                    fragment = new CartFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_profile:
                    toolbar.setTitle("Profile");
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    return true;
            }

            return false;
        }
    };

    /**
     * loading fragment into FrameLayout
     *
     * @param fragment
     */
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}

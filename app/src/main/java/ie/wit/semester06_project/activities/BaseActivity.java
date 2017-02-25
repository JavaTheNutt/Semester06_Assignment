package ie.wit.semester06_project.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ie.wit.semester06_project.main.FinanceApp;
import ie.wit.semester06_project.model.User;

public class BaseActivity extends AppCompatActivity
{
    public FinanceApp app;
    protected static final String TAG = "Finance";
    protected FirebaseAnalytics mFirebaseAnalytics;
    /*protected FirebaseAuth mAuth;
    protected FirebaseAuth.AuthStateListener mAuthListener;*/

    protected DatabaseReference databaseReference;
    protected DatabaseReference userDatabaseReference;

    protected String[] usernames;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        app = (FinanceApp) getApplication();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        userDatabaseReference = FirebaseDatabase.getInstance().getReference("users");
        userDatabaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                HashMap<String, Map> users = (HashMap<String, Map>) dataSnapshot.getValue();
                usernames = new String[users.size()];
                int i = 0;
                for(Map.Entry<String, Map> user : users.entrySet()){
                    String tempUser = (String) user.getValue().get("emailAddress");
                    Log.v(TAG, tempUser);
                    usernames[i] = tempUser;
                    i++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.e(TAG, databaseError.toString());
            }
        });
        /*mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.v(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.v(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };*/
        //setContentView(R.layout.activity_login);
    }

    /*@Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }*/
}

package ie.wit.semester06_project.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ie.wit.semester06_project.main.FinanceApp;

/**
 * This will be the Activity that all other activities will inherit from.
 */
public class BaseActivity extends AppCompatActivity
{
    public FinanceApp app; //Reference to the application object
    public static final String TAG = "Finance"; //the tag for the logger
    protected FirebaseAnalytics mFirebaseAnalytics;
    protected FirebaseAuth firebaseAuth;
    protected FirebaseAuth.AuthStateListener stateListener;

    protected DatabaseReference databaseReference;


    /**
     * {@inheritDoc}
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        app = (FinanceApp) getApplication();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        setUpAuth();
        firebaseAuth.addAuthStateListener(stateListener);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        if(stateListener != null){
            firebaseAuth.removeAuthStateListener(stateListener);
        }
    }

    private void setUpAuth(){
        firebaseAuth = FirebaseAuth.getInstance();
        stateListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Log.d(TAG, "onAuthStateChanged: user signed in" + user.getDisplayName());
                    FinanceApp.setCurrentUser(user);
                } else{
                    Log.d(TAG, "onAuthStateChanged: user signed out");
                    FinanceApp.setCurrentUser(null);
                }
            }
        };
    }
}

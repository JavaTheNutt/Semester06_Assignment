package ie.wit.semester06_project.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ie.wit.semester06_project.main.FinanceApp;
import ie.wit.semester06_project.service.AuthService;

/**
 * This will be the Activity that all other activities will inherit from.
 */
public class BaseActivity extends AppCompatActivity
{
    public FinanceApp app; //Reference to the application object
    public static final String TAG = "Finance"; //the tag for the logger
    protected FirebaseAnalytics mFirebaseAnalytics;
    protected AuthService authService;
    private FirebaseAuth mAuth;
    /*protected FirebaseAuth.AuthStateListener mAuthListener;*/

    protected DatabaseReference databaseReference;
    //protected DatabaseReference detailsDatabaseReference;


    /**
     * {@inheritDoc}
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        app = (FinanceApp) getApplication();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //authService = new AuthService(mAuth);
    }

   /* @Override
   todo uncomment when ready
    protected void onStart()
    {
        super.onStart();
        authService.start();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        authService.stop();
    }*/
}

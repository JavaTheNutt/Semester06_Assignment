package ie.wit.semester06_project.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ie.wit.semester06_project.main.FinanceApp;
import ie.wit.semester06_project.service.AuthService;
import ie.wit.semester06_project.service.data.UserDataService;

/**
 * This will be the Activity that all other activities will inherit from.
 */
public class BaseActivity extends AppCompatActivity
{
    /**
     * The App.
     */
    public FinanceApp app; //Reference to the application object
    /**
     * The constant TAG.
     */
    public static final String TAG = "Finance"; //the tag for the logger
    /**
     * The M firebase analytics.
     */
    protected FirebaseAnalytics mFirebaseAnalytics;
    /**
     * The Auth service.
     */
    protected AuthService authService;

    /**
     * The Database reference.
     */
    protected DatabaseReference databaseReference;


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
        authService = new AuthService(FirebaseAuth.getInstance(), new UserDataService(databaseReference.child("users")));
    }

    /**
     * {{@inheritDoc}}
     */
    @Override
    protected void onStart()
    {
        super.onStart();
        authService.start();
    }

    /**
     * {{@inheritDoc}}
     */
    @Override
    protected void onStop()
    {
        super.onStop();
        authService.stop();
    }
}

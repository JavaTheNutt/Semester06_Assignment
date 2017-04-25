package ie.wit.application.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ie.wit.application.main.FinanceApp;
import ie.wit.application.service.AuthService;

/**
 * This will be the Activity that all other activities will inherit from.
 */
public class BaseActivity extends AppCompatActivity
{
    /**
     * The constant TAG.
     */
    public static final String TAG = "Finance"; //the tag for the logger
    /**
     * The App.
     */
    public FinanceApp app; //Reference to the application object
    /**
     * reference to firebase analytics
     */
    protected FirebaseAnalytics mFirebaseAnalytics;
    /**
     * reference to auth service
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
        /* BaseActivity.java */
        super.onCreate(savedInstanceState);
        app = (FinanceApp) getApplication();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        authService = new AuthService();
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

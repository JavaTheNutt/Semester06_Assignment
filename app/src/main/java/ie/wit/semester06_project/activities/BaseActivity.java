package ie.wit.semester06_project.activities;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import ie.wit.semester06_project.R;
import ie.wit.semester06_project.main.FinanceApp;
import ie.wit.semester06_project.model.User;

public class BaseActivity extends AppCompatActivity
{
    public FinanceApp app;
    public static final String TAG = "Finance";
    protected FirebaseAnalytics mFirebaseAnalytics;
    /*protected FirebaseAuth mAuth;
    protected FirebaseAuth.AuthStateListener mAuthListener;*/

    protected DatabaseReference databaseReference;
    //protected DatabaseReference detailsDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        app = (FinanceApp) getApplication();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }
}

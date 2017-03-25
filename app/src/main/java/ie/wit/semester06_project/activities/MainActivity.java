package ie.wit.semester06_project.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import ie.wit.semester06_project.R;

public final class MainActivity extends BaseActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed()
    {
        finish();
    }

    public void loginClicked(View view)
    {
        Log.v(TAG, "Button to open application clicked");
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void signUpClicked(View view)
    {
        Log.v(TAG, "Sign up button clicked");
        startActivity(new Intent(this, SignUpActivity.class));
    }
}

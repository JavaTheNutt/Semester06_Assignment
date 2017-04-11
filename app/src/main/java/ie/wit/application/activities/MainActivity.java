package ie.wit.application.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import ie.wit.application.R;

/**
 * This will be the main Activity that contains the login/signup buttons
 */
public final class MainActivity extends BaseActivity
{

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.mainCheckAuthLoading);
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * {{@inheritDoc}}
     */
    @Override
    protected void onStart()
    {
        super.onStart();
        registerCallbacks(true);
    }

    /**
     * {{@inheritDoc}}
     */
    @Override
    protected void onStop()
    {
        super.onStop();
        registerCallbacks(false);
    }

    private void registerCallbacks(boolean register)
    {
        if (register) {
            authService.registerUserLoggedInCallback(result -> showDashboard());
            authService.registerUserNotLoggedInCallback(result -> hideLoading());
        } else {
            authService.registerUserLoggedInCallback(null);
            authService.registerUserNotLoggedInCallback(null);
        }

    }

    private void showDashboard()
    {
        startActivity(new Intent(this, DashboardActivity.class));
    }

    private void hideLoading()
    {
        progressBar.setVisibility(View.GONE);
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

    /**
     * Login clicked.
     *
     * @param view the view
     */
    public void loginClicked(View view)
    {
        Log.v(TAG, "Button to open application clicked");
        startActivity(new Intent(this, LoginActivity.class));
    }

    /**
     * Sign up clicked.
     *
     * @param view the view
     */
    public void signUpClicked(View view)
    {
        Log.v(TAG, "Sign up button clicked");
        startActivity(new Intent(this, SignUpActivity.class));
    }
}

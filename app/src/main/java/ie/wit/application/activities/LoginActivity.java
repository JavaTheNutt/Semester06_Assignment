package ie.wit.application.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import ie.wit.application.R;
import ie.wit.application.main.FinanceApp;

/**
 * The type Login activity.
 */
public class LoginActivity extends EntryActivity
{
    private EditText emailField;
    private EditText passwordField;
    private LinearLayout formContainer;
    private RelativeLayout progressContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailField = (EditText) findViewById(R.id.logInEmailField);
        passwordField = (EditText) findViewById(R.id.logInPasswordField);
        formContainer = (LinearLayout) findViewById(R.id.loginFormContainer);
        progressContainer = (RelativeLayout) findViewById(R.id.progressContainer);
    }

    /**
     * Login clicked.
     *
     * @param view the view
     */
    public void loginClicked(View view)
    {
        progressContainer.setVisibility(View.VISIBLE);
        formContainer.setAlpha(0.3f);
        progressContainer.setOnClickListener(this::consumePageClick);
        validation();
    }

    private void signIn(String email, String password)
    {
        validateUser(email, password);
    }

    private void validation()
    {
        String res = entryService.validateEmailAndPassword(emailField, passwordField);
        if (res.equals("")) {
            Log.v(TAG, "Email and password are in the correct format");
            signIn(emailField.getText().toString(), passwordField.getText().toString());
        } else {
            Log.v(TAG, res);
            FinanceApp.serviceFactory.getUtil().makeAToast(this, res);
        }
    }

    private void validateUser(String email, String password)
    {
        //third param is anonymous method
        authService.login(email, password, result -> {
            if (result.isEmpty()) { //if login is successfull
                startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
            } else { //if login fails
                progressContainer.setVisibility(View.GONE);
                formContainer.setAlpha(1f);
                progressContainer.setOnClickListener(null);
                makeToast(result);
            }
        });
    }

    /**
     * Consume page click.
     *
     * @param v the v
     */
    public void consumePageClick(View v)
    {
        Log.d(TAG, "consumePageClick: attempting to click while spinner is active");
    }

    private void makeToast(String msg)
    {
        FinanceApp.serviceFactory.getUtil().makeAToast(this, msg);
    }
}

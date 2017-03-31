package ie.wit.application.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import ie.wit.application.R;
import ie.wit.application.main.FinanceApp;

/**
 * The type Login activity.
 */
public class LoginActivity extends EntryActivity
{
    private EditText emailField;
    private EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailField = (EditText) findViewById(R.id.logInEmailField);
        passwordField = (EditText) findViewById(R.id.logInPasswordField);
    }

    /**
     * Login clicked.
     *
     * @param view the view
     */
    public void loginClicked(View view)
    {
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
       authService.login(email, password, (result) -> {
           startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
       });
    }

    private void makeToast(String msg)
    {
        FinanceApp.serviceFactory.getUtil().makeAToast(this, msg);
    }
}

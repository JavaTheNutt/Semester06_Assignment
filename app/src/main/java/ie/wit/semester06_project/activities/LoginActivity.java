package ie.wit.semester06_project.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import ie.wit.semester06_project.R;
import ie.wit.semester06_project.main.FinanceApp;
import ie.wit.semester06_project.model.User;

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

    public void loginClicked(View view)
    {
        validation();
    }

    private void signIn(String email, String password)
    {
        if (validateUser(email, password)) {
            makeToast("Login successful");
            startActivity(new Intent(this, DashboardActivity.class));
        }
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

    private boolean validateUser(String email, String password)
    {
        if (userDataService.getUsernames().indexOf(email) == -1) {
            makeToast("There is no user with that email address");
            return false;
        }
        User requestedUser;
        try {
            requestedUser = userDataService.getOne(email);
            authService.login(requestedUser.getEmail(), passwordField.getText().toString()); // TODO: 25/03/2017 uncomment when ready
        } catch (Exception e) {
            e.printStackTrace();
            Log.w(TAG, "validateUser: user not found");
            Log.e(TAG, "validateUser: user not found", e);
            makeToast(e.getMessage());
            return false;
        }
        FinanceApp.setCurrentUser(requestedUser);
        return true;
    }

    private void makeToast(String msg)
    {
        FinanceApp.serviceFactory.getUtil().makeAToast(this, msg);
    }
}

package ie.wit.semester06_project.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

import ie.wit.semester06_project.R;
import ie.wit.semester06_project.main.FinanceApp;
import ie.wit.semester06_project.model.User;

/**
 * The type Sign up activity.
 */
public class SignUpActivity extends EntryActivity
{
    private Map<String, EditText> namedFields;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setUpReferences();
    }

    /**
     * Register clicked.
     *
     * @param view the view
     */
    public void registerClicked(View view)
    {
        Log.v(TAG, "Register User button clicked");
        boolean detailsValid = entryService.validateSignUp(this, namedFields);
        String validationResult = detailsValid ? "All fields are valid" : "Some fields are invalid";
        Log.w(TAG, validationResult);
        if (detailsValid) {
            registerUser();
        }
    }

    private void setUpReferences()
    {
        namedFields = new HashMap<>(5);
        namedFields.put("firstName", (EditText) findViewById(R.id.signUpFirstNameField));
        namedFields.put("surname", (EditText) findViewById(R.id.signUpSurnameField));
        namedFields.put("password", (EditText) findViewById(R.id.signUpPasswordField));
        namedFields.put("confPassword", (EditText) findViewById(R.id.signUpConfirmPassword));
        namedFields.put("email", (EditText) findViewById(R.id.signUpEmailField));
    }

    private void makeToast(String msg)
    {
        FinanceApp.serviceFactory.getUtil().makeAToast(this, msg);
    }

    private void registerUser()
    {
        /*SignUpActivity.java*/
        authService.createAccount(entryService.extractText(namedFields), result -> {
            if (result){
                startActivity(new Intent(this, DashboardActivity.class));
            }else{
                makeToast("Login details are incorrect");
            }
        });
    }

}

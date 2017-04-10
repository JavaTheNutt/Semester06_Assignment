package ie.wit.application.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.util.HashMap;
import java.util.Map;

import ie.wit.application.R;
import ie.wit.application.main.FinanceApp;

/**
 * The type Sign up activity.
 */
public class SignUpActivity extends EntryActivity
{
    private Map<String, EditText> namedFields;
    private ScrollView formContainer;
    private RelativeLayout progressContainer;

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
        formContainer.setAlpha(0.3f);
        progressContainer.setVisibility(View.VISIBLE);
        progressContainer.setOnClickListener(this::consumeClick);
        boolean detailsValid = entryService.validateSignUp(this, namedFields);
        String validationResult = detailsValid ? "All fields are valid" : "Some fields are invalid";
        Log.w(TAG, validationResult);
        if (detailsValid) {
            registerUser();
        }
    }

    /**
     * This will put all of the text fields into a map so that they can be validated and the text extracted
     */
    private void setUpReferences()
    {
        namedFields = new HashMap<>(5);
        namedFields.put("firstName", (EditText) findViewById(R.id.signUpFirstNameField));
        namedFields.put("surname", (EditText) findViewById(R.id.signUpSurnameField));
        namedFields.put("password", (EditText) findViewById(R.id.signUpPasswordField));
        namedFields.put("confPassword", (EditText) findViewById(R.id.signUpConfirmPassword));
        namedFields.put("email", (EditText) findViewById(R.id.signUpEmailField));
        formContainer = (ScrollView) findViewById(R.id.signUpFormContainer);
        progressContainer = (RelativeLayout) findViewById(R.id.signUpProgressContainer);
    }
    private void consumeClick(View v){
        Log.d(TAG, "consumeClick: attempting to click while spinner is active");
    }

    /**
     * Helper method to generate toast
     *
     * @param msg the message to be shown
     */
    private void makeToast(String msg)
    {
        FinanceApp.serviceFactory.getUtil().makeAToast(this, msg);
    }

    /**
     * This method will register a user
     */
    private void registerUser()
    {
        /*AuthService.createAccount takes a Consumer<Boolean> where the result will be true if the user successfully logged in, or false otherwise*/
        authService.createAccount(entryService.extractText(namedFields), result -> {
            if (result) {
                startActivity(new Intent(this, DashboardActivity.class));
            } else {
                makeToast("Sign up failed");
                formContainer.setAlpha(1f);
                progressContainer.setVisibility(View.GONE);
                progressContainer.setOnClickListener(null);
            }
        });
    }

}

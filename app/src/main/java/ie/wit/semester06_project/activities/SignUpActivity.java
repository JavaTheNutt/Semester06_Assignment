package ie.wit.semester06_project.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

import ie.wit.semester06_project.R;
import ie.wit.semester06_project.main.FinanceApp;
import ie.wit.semester06_project.model.User;
import ie.wit.semester06_project.service.LoginValidationServiceImpl;

public class SignUpActivity extends BaseActivity
{

    private LoginValidationServiceImpl loginValidationService;

    private EditText firstNameField;
    private EditText surnameField;
    private EditText emailAddressField;
    private EditText passwordField;
    private EditText confirmPasswordField;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        loginValidationService = FinanceApp.serviceFactory.getLoginValidationService();

        firstNameField = (EditText) findViewById(R.id.signUpFirstNameField);
        surnameField = (EditText) findViewById(R.id.signUpSurnameField);
        emailAddressField = (EditText) findViewById(R.id.signUpEmailField);
        passwordField = (EditText) findViewById(R.id.signUpPasswordField);
        confirmPasswordField = (EditText) findViewById(R.id.signUpConfirmPassword);

    }
    public void registerClicked(View view){
        Log.v(TAG, "Register User button clicked");
        boolean detailsValid = validate();
        String validationResult = detailsValid ? "All fields are valid" : "Some fields are invalid";
        Log.v(TAG, validationResult);
        if(detailsValid){
            User user = new User();
            user.setFirstName(firstNameField.getText().toString());
            user.setSurname(surnameField.getText().toString());
            user.setEmailAddress(emailAddressField.getText().toString());
            user.setPassword(passwordField.getText().toString());
            registerUser(user);
        }

    }
    private String checkFields(Map<String, Boolean> values){
        for (Map.Entry<String, Boolean> entry: values.entrySet()) {
            Log.v(TAG, "Field:\t" + entry.getKey() + "\tval:\t" + entry.getValue());
            if(!entry.getValue()){
                return entry.getKey() + " needs to be at least three characters long";
            }
        }
        return "";
    }
    private void makeToast(String msg){
        FinanceApp.serviceFactory.getUtil().makeAToast(this, msg);
    }
    private boolean validate(){
        Map<String, String> values = new HashMap<>(5);
        values.put("First name", firstNameField.getText().toString());
        values.put("Surname", surnameField.getText().toString());
        Map<String, Boolean> response = loginValidationService.validateFieldsLength(values, 3);
        String err = checkFields(response);
        if(!err.equals("")){
            Log.w(TAG, err);
            makeToast(err);
            return false;
        }
        if(!loginValidationService.isValidEmail(emailAddressField.getText().toString())){
            err = "The email address supplied is invalid";
            Log.w(TAG, err);
            makeToast(err);
            return false;
        }
        if(!loginValidationService.validateFieldLength(passwordField.getText().toString(), 6)){
            err = "The password must be at least 6 characters long";
            Log.w(TAG, err);
            makeToast(err);
            return false;
        }
        if(!passwordField.getText().toString().equals(confirmPasswordField.getText().toString())){
            err = "Passwords must match!";
            Log.w(TAG, err);
            makeToast(err);
            return false;
        }
        return true;
    }

    // FIXME: 25/02/2017 check that username is not already in use
    // FIXME: 25/02/2017 hash password client side 
    private void registerUser(User user){
        for (String usedEmail :  usernames){
            if(usedEmail.equalsIgnoreCase(user.getEmailAddress())){
                makeToast("This email address is already in use");
                return;
            }
        }
        String key = generateKey(user.getEmailAddress());
        databaseReference.child("users").child(key).setValue(user);
    }
    private String generateKey(String emailAddress){
        String[] splitAddress = emailAddress.split("");
        String newKey = "";
        for (String character : splitAddress) {
            if(!character.equals(".")){
                newKey += character;
            }else{
                newKey += "_";
            }
        }
        Log.v(TAG, "New key:\t" + newKey);
        return newKey;
    }
}

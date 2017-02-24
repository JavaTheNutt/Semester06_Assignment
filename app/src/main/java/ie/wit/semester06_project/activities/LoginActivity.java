package ie.wit.semester06_project.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;

import ie.wit.semester06_project.R;
import ie.wit.semester06_project.main.FinanceApp;
import ie.wit.semester06_project.service.IValidationService;

public class LoginActivity extends BaseActivity
{
    private EditText emailField;
    private EditText passwordField;
    private DatabaseReference userDatabaseReference = database.getReference("user");
    private IValidationService loginValidationService;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailField = (EditText) findViewById(R.id.logInEmailField);
        passwordField = (EditText) findViewById(R.id.logInPasswordField);
        loginValidationService = FinanceApp.serviceFactory.getLoginValidationService();
    }

    private void signIn(String email, String password){

    }

    public void loginClicked(View view){
        Log.v(TAG, "LoginClicked called from within loginActivity");
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        Log.v(TAG, "User attempting to log in with the details: \n{\n\tusername:\t\'" + email + "\'\n\tpassword:\t\'" + password + "\'\n}");
        //String res = validateDetails(email, password);
        String res = loginValidationService.validateEmailAndPassword(email, password);
        if(res.equals("")){
            Log.v(TAG, "Email and password are in the correct format");
            signIn(email, password);
        }else{
            Log.v(TAG, res);
            FinanceApp.serviceFactory.getUtil().makeAToast(this, res);
        }

    }


}
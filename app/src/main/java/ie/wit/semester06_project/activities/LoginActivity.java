package ie.wit.semester06_project.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import ie.wit.semester06_project.R;
import ie.wit.semester06_project.main.FinanceApp;
import ie.wit.semester06_project.model.User;
import ie.wit.semester06_project.service.IValidationService;

public class LoginActivity extends EntryActivity
{
    private EditText emailField;
    private EditText passwordField;
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

    private OnCompleteListener<AuthResult> setUpOnCompleteListener(){
        return new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                Log.d(TAG, "onComplete: " + task.isSuccessful());
                if(!task.isSuccessful()){
                    Log.w(TAG, "onComplete: auth failed: ", task.getException());
                    FinanceApp.serviceFactory.getUtil().makeAToast(LoginActivity.this, "Incorrect login details");
                }else{
                    startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                }
            }
        };
    }
    private void signIn(String email, String password){

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(setUpOnCompleteListener());
        startActivity(new Intent(this, DashboardActivity.class));
    }

    public void loginClicked(View view){
        Log.v(TAG, "LoginClicked called from within loginActivity");
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        Log.v(TAG, "User attempting to log in with the details: \n{\n\tusername:\t\'" + email + "\'\n\tpassword:\t\'" + password + "\'\n}");
        //String res = validateDetails(email, password);
        String res = loginValidationService.validateEmailAndPassword(email, password);
        if(res.equals("")){
            Log.v(TAG, "Email and password are in the correct format");
            //FinanceApp.setCurrentUser(new User(email, password, "Joe", "Bloggs"));
            signIn(email, password);
        }else{
            Log.v(TAG, res);
            FinanceApp.serviceFactory.getUtil().makeAToast(this, res);
        }
    }

    private void makeToast(String msg){
        FinanceApp.serviceFactory.getUtil().makeAToast(this, msg);
    }
}

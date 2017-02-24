package ie.wit.semester06_project.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;

import ie.wit.semester06_project.R;

public class LoginActivity extends BaseActivity
{
    private EditText emailField;
    private EditText passwordField;
    private DatabaseReference userDatabaseReference = database.getReference("user");
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailField = (EditText) findViewById(R.id.logInEmailField);
        passwordField = (EditText) findViewById(R.id.logInPasswordField);
    }

    private void signIn(String email, String password){

    }

    public void loginClicked(View view){
        Log.v(TAG, "LoginClicked called from within loginActivity");
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        Log.v(TAG, "User attempting to log in with the details: \n{\n\tusername:\t\'" + email + "\'\n\tpassword:\t\'" + password + "\'\n}");
        signIn(email, password);
    }


}

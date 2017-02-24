package ie.wit.semester06_project.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import ie.wit.semester06_project.R;

public class SignUpActivity extends BaseActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }
    public void registerClicked(View view){
        Log.v(TAG, "Register User button clicked");
    }
}

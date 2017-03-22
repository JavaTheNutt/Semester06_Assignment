package ie.wit.semester06_project.service;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import org.jetbrains.annotations.Contract;

import java.util.HashMap;
import java.util.Map;

import ie.wit.semester06_project.activities.BaseActivity;
import ie.wit.semester06_project.main.FinanceApp;
import ie.wit.semester06_project.model.User;

/**
 * Created by joewe on 22/03/2017.
 */

public class EntryService
{
    public Map<String, Boolean> validateFieldsLength(Map<String, String> fields, int requiredMin)
    {
        Map<String, Boolean> returnedMap = new HashMap<>(fields.size());
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            returnedMap.put(entry.getKey(), validateFieldLength(entry.getValue(), requiredMin));
        }
        return returnedMap;
    }

    public boolean validateSignUp(Context src, Map<String, EditText> fields)
    {
        Map<String, String> values = new HashMap<>();
        values.put("firstName", fields.get("firstName").getText().toString());
        values.put("surname", fields.get("surname").getText().toString());
        String err = checkFields(validateFieldsLength(values, 3));
        if (!err.equals("")) {
            Log.w(BaseActivity.TAG, err);
            FinanceApp.serviceFactory.getUtil().makeAToast(src, err);
            return false;
        }
        err = validateData(fields);
        if(!err.equals("")){
            Log.w(BaseActivity.TAG, err);
            FinanceApp.serviceFactory.getUtil().makeAToast(src, err);
            return false;
        }
        return true;
    }

    public String validateEmailAndPassword(String email, String password)
    {
        String errorMsg = "";
        if (!isValidEmail(email)) {
            errorMsg += "Email address supplied is invalid!";
        }
        if (password.length() < 6) {
            if (!errorMsg.equals("")) {
                errorMsg += "\n";
            }
            errorMsg += "Password supplied is too short!";
        }
        return errorMsg;
    }

    public String validateEmailAndPassword(EditText emailField, EditText password)
    {
        String errorMsg = "";
        if (!isValidEmail(emailField.getText().toString())) {
            errorMsg += "Email address supplied is invalid!";
        }
        if (password.getText().toString().length() < 6) {
            if (!errorMsg.equals("")) {
                errorMsg += "\n";
            }
            errorMsg += "Password supplied is too short!";
        }
        return errorMsg;
    }

    public boolean isValidEmail(CharSequence target)
    {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public boolean validateFieldLength(String str, int requiredMin)
    {
        requiredMin = requiredMin == 0 ? 3 : requiredMin;
        return str.length() >= requiredMin;
    }

    public User mapUser(EditText[] fields)
    {
        String[] values = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            values[i] = fields[i].getText().toString();
        }
        return mapUser(values);
    }
    public User mapUser(Map<String, EditText> fields){
        String[] values = new String[fields.size()];
        values[0] = fields.get("email").getText().toString().trim();
        values[1] = fields.get("password").getText().toString().trim();
        values[2] = fields.get("firstName").getText().toString().trim();
        values[3] = fields.get("surname").getText().toString().trim();
        return mapUser(values);
    }

    @Contract("_ -> !null")
    private User mapUser(String[] values)
    {
        return new User(values[0], values[1], values[2], values[3]);
    }

    @NonNull
    private String validateData(Map<String, EditText> fields)
    {
        if (!isValidEmail(fields.get("email").getText().toString())) {
            Log.w(BaseActivity.TAG, "Email is invalid");
            return "Email is invalid";
        }
        if(!validateFieldLength(fields.get("password").getText().toString(), 6)){
            Log.w(BaseActivity.TAG, "validateData: Password must be at least 6 characters");
            return "Password must be at least 6 characters";
        }
        if(!fields.get("password").getText().toString().equals(fields.get("confPassword").getText().toString())){
            Log.w(BaseActivity.TAG, "validateData: Passwords must match");
            return "Passwords must match";
        }
        return "";
    }

    private String checkFields(Map<String, Boolean> values)
    {
        for (Map.Entry<String, Boolean> entry : values.entrySet()) {
            Log.v(BaseActivity.TAG, "Field:\t" + entry.getKey() + "\tval:\t" + entry.getValue());
            if (!entry.getValue()) {
                return entry.getKey() + " needs to be at least three characters long";
            }
        }
        return "";
    }
}

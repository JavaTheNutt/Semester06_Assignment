package ie.wit.semester06_project.service;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ie.wit.semester06_project.activities.BaseActivity;
import ie.wit.semester06_project.main.FinanceApp;
import ie.wit.semester06_project.model.User;

/**
 * Created by joewe on 22/03/2017.
 */

public class EntryService
{
    private static final String[] FIELD_NAMES = {"emailAddress", "password", "firstName", "surname", "confPassword"};
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
        if (!err.equals("")) {
            Log.w(BaseActivity.TAG, err);
            FinanceApp.serviceFactory.getUtil().makeAToast(src, err);
            return false;
        }
        return true;
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

    public List<User> mapUsers(Map<String, Map> users)
    {
        List<User> userList = new ArrayList<>(users.size());
        for (Map.Entry<String, Map> user : users.entrySet()) {
            userList.add(mapUser(user));
        }
        return userList;
    }

    public User mapUser(Map<String, EditText> fields)
    {
        String[] values = new String[fields.size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = fields.get(FIELD_NAMES[i]).getText().toString().trim();
        }
        return mapUser(values);
    }

    public boolean checkIfUserExists(String username, List<User> users)
    {
        for (User user : users) {
            if (user.getEmailAddress().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public String[] getUsernames(List<User> users)
    {
        String[] usernames = new String[users.size()];
        for (int i = 0; i < users.size(); i++) {
            usernames[i] = users.get(i).getEmailAddress();
        }
        return usernames;
    }

    private Map<String, Boolean> validateFieldsLength(Map<String, String> fields, int requiredMin)
    {
        Map<String, Boolean> returnedMap = new HashMap<>(fields.size());
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            returnedMap.put(entry.getKey(), validateFieldLength(entry.getValue(), requiredMin));
        }
        return returnedMap;
    }
    
    @Contract("_ -> !null")
    private User mapUser(String[] values)
    {
        return new User(values[0], values[1], values[2], values[3]);
    }

    private boolean isValidEmail(CharSequence target)
    {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private boolean validateFieldLength(String str, int requiredMin)
    {
        requiredMin = requiredMin == 0 ? 3 : requiredMin;
        return str.length() >= requiredMin;
    }

    // FIXME: 22/03/2017 refactor to use the keys instead of the constant array
    private User mapUser(Map.Entry<String, Map> user)
    {
        String[] values = new String[user.getValue().size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = user.getValue().get(FIELD_NAMES[i]).toString().trim();
        }
        return mapUser(values);
    }

    @NonNull
    private String validateData(Map<String, EditText> fields)
    {
        if (!isValidEmail(fields.get("emailAddress").getText().toString())) {
            Log.w(BaseActivity.TAG, "Email is invalid");
            return "Email is invalid";
        }
        if (!validateFieldLength(fields.get("password").getText().toString(), 6)) {
            Log.w(BaseActivity.TAG, "validateData: Password must be at least 6 characters");
            return "Password must be at least 6 characters";
        }
        if (!fields.get("password").getText().toString().equals(fields.get("confPassword").getText().toString())) {
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

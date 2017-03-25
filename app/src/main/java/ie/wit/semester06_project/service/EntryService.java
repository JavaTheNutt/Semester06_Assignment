package ie.wit.semester06_project.service;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

import ie.wit.semester06_project.main.FinanceApp;

import static android.content.ContentValues.TAG;

/**
 * This class will perform ui-related services, such as validation, for both the LoginActivity and SignUpActivity.
 */
public class EntryService
{
    /**
     * Validate sign up boolean.
     *
     * @param src    the src
     * @param fields the fields
     * @return the boolean
     */
    public boolean validateSignUp(Context src, Map<String, EditText> fields)
    {
        Map<String, String> values = new HashMap<>();
        values.put("firstName", fields.get("firstName").getText().toString());
        values.put("surname", fields.get("surname").getText().toString());
        String err = checkFields(validateFieldsLength(values, 3));
        if (!err.equals("")) {
            Log.w(TAG, err);
            FinanceApp.serviceFactory.getUtil().makeAToast(src, err);
            return false;
        }
        err = validateData(fields);
        if (!err.equals("")) {
            Log.w(TAG, err);
            FinanceApp.serviceFactory.getUtil().makeAToast(src, err);
            return false;
        }
        return true;
    }

    /**
     * Validate email and password string.
     *
     * @param emailField the email field
     * @param password   the password
     * @return the string
     */
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

    private Map<String, Boolean> validateFieldsLength(Map<String, String> fields, int requiredMin)
    {
        Map<String, Boolean> returnedMap = new HashMap<>(fields.size());
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            returnedMap.put(entry.getKey(), validateFieldLength(entry.getValue(), requiredMin));
        }
        return returnedMap;
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

    @NonNull
    private String validateData(Map<String, EditText> fields)
    {
        if (!isValidEmail(fields.get("email").getText().toString())) {
            Log.w(TAG, "Email is invalid");
            return "Email is invalid";
        }
        if (!validateFieldLength(fields.get("password").getText().toString(), 6)) {
            Log.w(TAG, "validateData: Password must be at least 6 characters");
            return "Password must be at least 6 characters";
        }
        if (!fields.get("password").getText().toString().equals(fields.get("confPassword").getText().toString())) {
            Log.w(TAG, "validateData: Passwords must match");
            return "Passwords must match";
        }
        return "";
    }

    private String checkFields(Map<String, Boolean> values)
    {
        for (Map.Entry<String, Boolean> entry : values.entrySet()) {
            Log.v(TAG, "Field:\t" + entry.getKey() + "\tval:\t" + entry.getValue());
            if (!entry.getValue()) {
                return entry.getKey() + " needs to be at least three characters long";
            }
        }
        return "";
    }

    /**
     * Extract text map.
     *
     * @param fields the fields
     * @return the map
     */
    public Map<String, String> extractText(Map<String, EditText> fields)
    {
        Map<String, String> user = new HashMap<>(4);
        for (Map.Entry field : fields.entrySet()) {
            user.put(field.getKey().toString(), ((EditText) field.getValue()).getText().toString().trim());
        }
        return user;
    }
}

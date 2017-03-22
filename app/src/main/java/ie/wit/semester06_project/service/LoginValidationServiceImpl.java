package ie.wit.semester06_project.service;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import ie.wit.semester06_project.model.User;

/**
 * Created by joewe on 24/02/2017.
 */

public class LoginValidationServiceImpl implements IValidationService
{
    public boolean isValidEmail(CharSequence target)
    {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    public String validateEmailAndPassword(String email, String password){
        String errorMsg = "";
        if(!isValidEmail(email)){
            errorMsg += "Email address supplied is invalid!";
        }
        if(password.length() < 6){
            if(!errorMsg.equals("")){
                errorMsg += "\n";
            }
            errorMsg += "Password supplied is too short!";
        }
        return errorMsg;
    }
    public boolean validateFieldLength(String str, int requiredMin){
        requiredMin = requiredMin == 0 ? 3 : requiredMin;
        return str.length() >= requiredMin;
    }
    public Map<String, Boolean> validateFieldsLength(Map<String, String> fields, int requiredMin){
        Map<String, Boolean> returnedMap = new HashMap<>(fields.size());
        for (Map.Entry<String, String> entry: fields.entrySet()) {
            returnedMap.put(entry.getKey(), validateFieldLength(entry.getValue(), requiredMin));
        }
        return returnedMap;
    }
    public boolean vaildatePasswordMatch(String pass01, String pass02){
        return pass01.equals(pass02);
    }

}

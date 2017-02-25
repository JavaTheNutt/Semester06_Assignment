package ie.wit.semester06_project.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import ie.wit.semester06_project.activities.BaseActivity;

/**
 * Created by joewe on 24/02/2017.
 */

public class Util
{
    public void makeAToast(Context src, String msg){
        Toast.makeText(src, msg, Toast.LENGTH_SHORT).show();
    }
    public String formatEmailAsKey(String emailAddress){
        Log.v(BaseActivity.TAG, "Formatting " + emailAddress + " so it can be used as a key");
        String[] splitAddress = emailAddress.split("");
        String newKey = "";
        for (String character : splitAddress) {
            if(!character.equals(".")){
                newKey += character;
            }else{
                newKey += "_";
            }
        }
        Log.v(BaseActivity.TAG, "New key:\t" + newKey);
        return newKey;
    }
}

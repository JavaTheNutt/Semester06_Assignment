package ie.wit.semester06_project.model;

import android.util.Log;

import com.google.firebase.database.Exclude;

import java.util.Map;

import ie.wit.semester06_project.activities.BaseActivity;

/**
 * Created by joewe on 24/02/2017.
 */

public class User
{

    private String email;
    private String uuid;
    private String firstName;
    private String surname;

    public User()
    {
    }

    public User (String email, String uuid, String firstName, String surname)
    {

        this.email = email;
        this.uuid = uuid;
        this.firstName = firstName;
        this.surname = surname;
    }
    public User(Map<String, String> userDetails){
        this.email = userDetails.get("email");
        this.firstName = userDetails.get("firstName");
        this.surname = userDetails.get("surname");
        this.uuid = userDetails.get("uuid");
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getUuid()
    {
        return uuid;
    }

    public void setUuid(String password)
    {
        this.uuid = password;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getSurname()
    {
        return surname;
    }

    public void setSurname(String surname)
    {
        this.surname = surname;
    }
    @Exclude
    public String getKey(){
        Log.v(BaseActivity.TAG, "Formatting " + this.email + " so it can be used as a key");
        String[] splitAddress = this.email.split("");
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

    @Override
    public String toString()
    {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + uuid + '\'' +
                ", firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}

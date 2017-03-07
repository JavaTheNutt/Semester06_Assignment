package ie.wit.semester06_project.model;

import android.util.Log;

import com.google.firebase.database.Exclude;

import ie.wit.semester06_project.activities.BaseActivity;

/**
 * Created by joewe on 24/02/2017.
 */

public class User
{

    private String emailAddress;
    private String password;
    private String firstName;
    private String surname;

    public User()
    {
    }

    public User (String emailAddress, String password, String firstName, String surname)
    {

        this.emailAddress = emailAddress;
        this.password = password;
        this.firstName = firstName;
        this.surname = surname;
    }

    public String getEmailAddress()
    {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
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
        Log.v(BaseActivity.TAG, "Formatting " + this.emailAddress + " so it can be used as a key");
        String[] splitAddress = this.emailAddress.split("");
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
                "emailAddress='" + emailAddress + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}

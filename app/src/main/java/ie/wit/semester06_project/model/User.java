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
    private String firebaseUid;

    /**
     * Instantiates a new User.
     */
    public User()
    {
    }

    /**
     * Instantiates a new User.
     *
     * @param email     the email
     * @param uuid      the uuid
     * @param firstName the first name
     * @param surname   the surname
     */
    public User (String email, String uuid, String firstName, String surname)
    {

        this.email = email;
        this.uuid = uuid;
        this.firstName = firstName;
        this.surname = surname;
    }

    /**
     * Instantiates a new User.
     *
     * @param userDetails the user details
     */
    public User(Map<String, String> userDetails){
        this.email = userDetails.get("email");
        this.firstName = userDetails.get("firstName");
        this.surname = userDetails.get("surname");
        this.uuid = userDetails.get("uuid");
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail()
    {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email)
    {
        this.email = email;
    }

    /**
     * Gets uuid.
     *
     * @return the uuid
     */
    public String getUuid()
    {
        return uuid;
    }

    /**
     * Sets uuid.
     *
     * @param uuid the uuid
     */
    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    /**
     * Gets first name.
     *
     * @return the first name
     */
    public String getFirstName()
    {
        return firstName;
    }

    /**
     * Sets first name.
     *
     * @param firstName the first name
     */
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    /**
     * Gets surname.
     *
     * @return the surname
     */
    public String getSurname()
    {
        return surname;
    }

    /**
     * Sets surname.
     *
     * @param surname the surname
     */
    public void setSurname(String surname)
    {
        this.surname = surname;
    }

    /**
     * Get key string.
     *
     * @return the string
     */
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
                ", firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}

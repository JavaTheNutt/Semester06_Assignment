package ie.wit.semester06_project.model;

/**
 * Created by joewe on 24/02/2017.
 */

public class User
{
    private Integer userId;
    private String emailAddress;
    private String password;
    private String firstName;
    private String surname;

    public User()
    {
    }

    public User(Integer userId, String emailAddress, String password, String firstName, String surname)
    {
        this.userId = userId;
        this.emailAddress = emailAddress;
        this.password = password;
        this.firstName = firstName;
        this.surname = surname;
    }

    public Integer getUserId()
    {
        return userId;
    }

    public void setUserId(Integer userId)
    {
        this.userId = userId;
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

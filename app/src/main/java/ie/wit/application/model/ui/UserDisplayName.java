package ie.wit.application.model.ui;

import java.util.Observable;

/**
 * Created by joewe on 09/04/2017.
 */
public class UserDisplayName extends Observable
{
    private String firstName = "John";
    private String surname = "Doe";

    /**
     * Sets name.
     *
     * @param firstName the first name
     * @param surname   the surname
     */
    public void setName(String firstName, String surname)
    {
        synchronized (this) {
            this.firstName = firstName;
            this.surname = surname;
        }
        setChanged();
        notifyObservers();
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    synchronized String getName()
    {
        return this.firstName + " " + this.surname;
    }
}

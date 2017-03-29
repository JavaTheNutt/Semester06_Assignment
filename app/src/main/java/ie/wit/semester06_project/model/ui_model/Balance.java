package ie.wit.semester06_project.model.ui_model;

import java.util.Observable;

/**
 * Found: http://stackoverflow.com/a/15434375/4108556
 */
public class Balance extends Observable
{
    private float balance = 0;

    /**
     * This will set the balance to a specified amount and notify observers
     *
     * @param balance the new balance
     */
    public void setBalance(float balance){
        synchronized (this){
            this.balance = balance;
        }
        setChanged();
        notifyObservers();
    }


    /**
     * Increment balance.
     *
     * @param amount the amount
     */
    public void incrementBalance(float amount){
        synchronized (this){
            this.balance += amount;
        }
        setChanged();
        notifyObservers();
    }

    /**
     * Decrement balance.
     *
     * @param amount the amount
     */
    public void decrementBalance(float amount){
        synchronized (this){
            this.balance -= amount;
        }
        setChanged();
        notifyObservers();
    }

    /**
     * Get balance float.
     *
     * @return the float
     */
    synchronized float getBalance(){
        return balance;
    }
}

package ie.wit.application.model.ui_model;

import java.util.Observable;

/**
 * Found: http://stackoverflow.com/a/15434375/4108556
 */
public class Balance extends Observable
{
    private float currentBalance = 0;

    /**
     * This will set the currentBalance to a specified amount and notify observers.
     * This method is not synchronised because only actual value change needs to be safe.
     * The two method calls below trigger synchronized reads of the value.
     *
     * @param currentBalance the new currentBalance
     */
    public void setCurrentBalance(float currentBalance){
        synchronized (this){
            this.currentBalance = currentBalance;
        }
        setChanged();
        notifyObservers();
    }


    /**
     * Increment currentBalance.
     *
     * @param amount the amount
     */
    public void incrementBalance(float amount){
        synchronized (this){
            this.currentBalance += amount;
        }
        setChanged();
        notifyObservers();
    }

    /**
     * Decrement currentBalance.
     *
     * @param amount the amount
     */
    public void decrementBalance(float amount){
        synchronized (this){
            this.currentBalance -= amount;
        }
        setChanged();
        notifyObservers();
    }

    /**
     * Get currentBalance float.
     *
     * @return the float
     */
    synchronized float getCurrentBalance(){
        return currentBalance;
    }
}

package ie.wit.semester06_project.model;

import java.util.Observable;

/**
 * Found: http://stackoverflow.com/a/15434375/4108556
 */

public class Balance extends Observable
{
    private float balance = 0;
    public void setBalance(float balance){
        synchronized (this){
            this.balance = balance;
        }
        setChanged();
        notifyObservers();
    }
    public void incrementBalance(float amount){
        synchronized (this){
            this.balance += amount;
        }
        setChanged();
        notifyObservers();
    }
    public void decrementBalance(float amount){
        synchronized (this){
            this.balance -= amount;
        }
        setChanged();
        notifyObservers();
    }
    public synchronized float getBalance(){
        return balance;
    }
}

package ie.wit.application.model.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

/**
 * Found: http://stackoverflow.com/a/15434375/4108556
 */
public class Balance extends Observable
{
    private float currentBalance = 0;
    private float totalIncome = 0;
    private float totalExpenditure = 0;

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

    public void setAll(float totalIncome, float totalExpenditure){
        synchronized (this){
            this.totalIncome = totalIncome;
            this.totalExpenditure = totalExpenditure;
            this.currentBalance = totalIncome - totalIncome;
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
    synchronized Map<String, Float> getAll(){
        Map<String, Float> values = new HashMap<>(3);
        values.put("income", totalIncome);
        values.put("expenditure", totalExpenditure);
        values.put("balance", currentBalance);
        return values;
    }
}

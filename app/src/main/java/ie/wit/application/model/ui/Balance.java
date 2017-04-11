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
     * Sets all.
     *
     * @param totalIncome      the total income
     * @param totalExpenditure the total expenditure
     */
    public void setAll(float totalIncome, float totalExpenditure)
    {
        synchronized (this) {
            this.totalIncome = totalIncome;
            this.totalExpenditure = totalExpenditure;
            this.currentBalance = totalIncome - totalExpenditure;
        }
        setChanged();
        notifyObservers();
    }

    /**
     * Gets all.
     *
     * @return the all
     */
    synchronized Map<String, Float> getAll()
    {
        Map<String, Float> values = new HashMap<>(3);
        values.put("income", totalIncome);
        values.put("expenditure", totalExpenditure);
        values.put("balance", currentBalance);
        return values;
    }
}

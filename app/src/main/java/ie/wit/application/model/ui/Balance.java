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
    private float totalPendingIncome = 0;
    private float totalPendingExpenditure = 0;
    private float totalCompletedIncome = 0;
    private float totalCompletedExpenditure = 0;
    private float totalPendingBalance = 0;
    private float totalCompletedBalance = 0;

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
    public void setAll(Map<String, Float> values){
        synchronized (this){
            this.totalPendingIncome = values.get("pendingIncome");
            this.totalCompletedIncome = values.get("completedIncome");
            this.totalIncome = this.totalCompletedIncome + this.totalPendingIncome;
            this.totalCompletedExpenditure = values.get("completedExpenditure");
            this.totalPendingExpenditure = values.get("pendingExpenditure");
            this.totalExpenditure = this.totalCompletedExpenditure + totalPendingExpenditure;
            this.currentBalance = this.totalIncome - this.totalExpenditure;
            this.totalPendingBalance = this.totalPendingIncome - this.totalPendingExpenditure;
            this.totalCompletedBalance = this.totalCompletedIncome - this.totalCompletedExpenditure;
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
        values.put("pendingIncome", totalPendingIncome);
        values.put("completedIncome", totalCompletedIncome);
        values.put("pendingExpenditure", totalPendingExpenditure);
        values.put("completedExpenditure", totalCompletedExpenditure);
        values.put("pendingBalance", totalPendingBalance);
        values.put("completedBalance", totalCompletedBalance);
        return values;
    }
}

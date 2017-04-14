package ie.wit.application.model;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by joewe on 01/03/2017.
 */
@IgnoreExtraProperties
public class Transaction implements Serializable
{
    private Long timestamp;
    private String title;
    private Float amount;
    private boolean isIncome;
    private Long dueDate;
    @Exclude
    private String firebaseId;

    /**
     * Instantiates a new Transaction.
     */
    public Transaction()
    {
        //timestamp = new Date().getTime();
    }

    /**
     * Instantiates a new Transaction.
     *
     * @param timestamp the timestamp
     */
    public Transaction(Long timestamp)
    {
        this.timestamp = timestamp;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title)
    {
        this.title = title;
    }

    /**
     * Gets amount.
     *
     * @return the amount
     */
    public Float getAmount()
    {
        return amount;
    }

    /**
     * Sets amount.
     *
     * @param amount the amount
     */
    public void setAmount(Float amount)
    {
        this.amount = amount;
    }

    /**
     * Gets timestamp.
     *
     * @return the timestamp
     */
    public Long getTimestamp()
    {
        return timestamp;
    }

    /**
     * Set timestamp.
     *
     * @param timestamp the timestamp
     */
    public void setTimestamp(Long timestamp)
    {
        if (timestamp == null) {
            timestamp = new Date().getTime();
        }
        this.timestamp = timestamp;
    }

    /**
     * Is income boolean.
     *
     * @return the boolean
     */
    public boolean isIncome()
    {
        return isIncome;
    }

    /**
     * Sets income.
     *
     * @param income the income
     */
    public void setIncome(boolean income)
    {
        isIncome = income;
    }

    /**
     * Gets due date.
     *
     * @return the due date
     */
    public Long getDueDate()
    {
        return dueDate;
    }

    /**
     * Sets due date.
     *
     * @param dueDate the due date
     */
    public void setDueDate(Long dueDate)
    {
        this.dueDate = dueDate;
    }

    /**
     * Gets firebase id.
     *
     * @return the firebase id
     */
    @Exclude
    public String getFirebaseId()
    {
        return firebaseId;
    }

    /**
     * Sets firebase id.
     *
     * @param firebaseId the firebase id
     */
    @Exclude
    public void setFirebaseId(String firebaseId)
    {
        this.firebaseId = firebaseId;
    }


    @Override
    public String toString()
    {
        return "Transaction{" +
                "timestamp=" + timestamp +
                ", due on=" + dueDate +
                ", title='" + title + '\'' +
                ", amount=" + amount +
                '}';
    }
    public static Comparator<Transaction> transactionDueAsc = (o1, o2) -> o1.getDueDate().compareTo(o2.getDueDate());
    public static Comparator<Transaction> transactionDueDesc = (o1, o2) -> o2.getDueDate().compareTo(o1.getDueDate());
    public static Comparator<Transaction> transactionEnteredAsc = (o1, o2) -> o1.getTimestamp().compareTo(o2.getTimestamp());
    public static Comparator<Transaction> transactionEnteredDesc = (o1, o2) -> o2.getTimestamp().compareTo(o1.getTimestamp());
    public static Comparator<Transaction> transactionTitleAsc = (o1, o2) -> o1.getTitle().toLowerCase().compareTo(o2.getTitle().toLowerCase());
    public static Comparator<Transaction> transactionTitleDesc = (o1, o2) -> o2.getTitle().toLowerCase().compareTo(o1.getTitle().toLowerCase());
    public static Comparator<Transaction> transactionAmountAsc = (o1, o2) -> o1.getAmount() >= o2.getAmount() ? 1:0;
    public static Comparator<Transaction> transactionAmountDesc = (o1, o2) -> o1.getAmount() >= o2.getAmount() ? 0:1;


}

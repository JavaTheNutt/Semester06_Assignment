package ie.wit.application.model;

import java.util.Date;

/**
 * Created by joewe on 01/03/2017.
 */
public class Transaction
{
    private Long timestamp;
    private String title;
    private Float amount;
    private boolean isIncome;

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
    public void setTimestamp(Long timestamp){
        if(timestamp == null){
            timestamp = new Date().getTime();
        }
        this.timestamp = timestamp;
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

    @Override
    public String toString()
    {
        return "IncomeDTOIn{" +
                "timestamp=" + timestamp +
                ", title='" + title + '\'' +
                ", amount=" + amount +
                '}';
    }

}

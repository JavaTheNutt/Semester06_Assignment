package ie.wit.semester06_project.model;

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

    public Transaction()
    {
        //timestamp = new Date().getTime();
    }

    public Transaction(Long timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public Float getAmount()
    {
        return amount;
    }

    public Long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Long timestamp){
        if(timestamp == null){
            timestamp = new Date().getTime();
        }
        this.timestamp = timestamp;
    }

    public void setAmount(Float amount)
    {
        this.amount = amount;
    }

    public boolean isIncome()
    {
        return isIncome;
    }

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

package ie.wit.semester06_project.model;

import java.util.Date;

/**
 * Created by joewe on 25/02/2017.
 */

public class Income
{
    private Long timestamp;
    private String title;
    private Float amount;

    public Income()
    {
        timestamp = new Date().getTime();
    }

    public Income(Long timestamp)
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

    public void setAmount(Float amount)
    {
        this.amount = amount;
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

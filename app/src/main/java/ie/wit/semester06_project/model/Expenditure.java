package ie.wit.semester06_project.model;

/**
 * Created by joewe on 01/03/2017.
 */

public class Expenditure implements ITransaction
{
    private Long timestamp;
    private String title;
    private Float amount;

    public Expenditure()
    {
    }

    public Expenditure(Long timestamp)
    {
        this.timestamp = timestamp;
    }

    public Long getTimestamp()
    {
        return timestamp;
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

    public void setAmount(Float amount)
    {
        this.amount = amount;
    }

    @Override
    public String toString()
    {
        return "Expenditure{" +
                "timestamp=" + timestamp +
                ", title='" + title + '\'' +
                ", amount=" + amount +
                '}';
    }
}

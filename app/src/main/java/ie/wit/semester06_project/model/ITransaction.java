package ie.wit.semester06_project.model;

/**
 * Created by joewe on 26/02/2017.
 */

public interface ITransaction
{
    String getTitle();
    Long getTimestamp();
    Float getAmount();
    void setTitle(String title);
    void setAmount(Float amount);
}

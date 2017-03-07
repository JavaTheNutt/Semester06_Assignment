package ie.wit.semester06_project.repo;

import java.util.List;

import ie.wit.semester06_project.model.ITransaction;

/**
 * Created by joewe on 26/02/2017.
 */

public interface IRepo<T extends ITransaction>
{
    List<T> getAllItems();

    void addItem(T item);

    void removeItem(T item) throws Exception;

    void removeItem(Long timestamp) throws Exception;

}

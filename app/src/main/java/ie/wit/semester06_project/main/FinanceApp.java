package ie.wit.semester06_project.main;

import android.app.Application;
import android.util.Log;

import ie.wit.semester06_project.factory.ServiceFactory;
import ie.wit.semester06_project.model.User;

/**
 * Main Application Class
 */
public class FinanceApp extends Application
{
    /**
     * The constant serviceFactory.
     */
    public static ServiceFactory serviceFactory;
    private static User currentUser;

    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.v("Finance", "Application started");
        serviceFactory = new ServiceFactory();
    }

    /**
     * Gets current user.
     *
     * @return the current user
     */
    public static User getCurrentUser()
    {
        return currentUser;
    }

    /**
     * Sets current user.
     *
     * @param currentUser the current user
     */
    public static void setCurrentUser(User currentUser)
    {
        FinanceApp.currentUser = currentUser;
    }
}

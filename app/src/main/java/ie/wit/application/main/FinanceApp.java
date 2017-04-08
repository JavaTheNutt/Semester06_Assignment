package ie.wit.application.main;

import android.app.Application;
import android.util.Log;

import ie.wit.application.factory.ServiceFactory;
import ie.wit.application.model.User;

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
    private static String currentUserId;

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

    public static String getCurrentUserId()
    {
        return currentUserId;
    }

    public static void setCurrentUserId(String currentUserId)
    {
        FinanceApp.currentUserId = currentUserId;
    }
}

package ie.wit.semester06_project.main;

import android.app.Application;
import android.util.Log;

import com.google.firebase.auth.FirebaseUser;

import ie.wit.semester06_project.factory.ServiceFactory;
import ie.wit.semester06_project.model.User;
import ie.wit.semester06_project.service.IValidationService;
import ie.wit.semester06_project.service.LoginValidationServiceImpl;

/**
 * Main Application Class
 */

public class FinanceApp extends Application
{
    public static ServiceFactory serviceFactory;
    private static User currentUser;
    private static FirebaseUser firebaseUser;

    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.v("Finance", "Application started");
        serviceFactory = new ServiceFactory();
    }

    public static FirebaseUser getCurrentUser()
    {
        return firebaseUser;
    }

    public static void setCurrentUser(FirebaseUser firebaseUser)
    {
        FinanceApp.firebaseUser = firebaseUser;
    }

}

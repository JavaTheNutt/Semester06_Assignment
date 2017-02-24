package ie.wit.semester06_project.main;

import android.app.Application;
import android.util.Log;

import ie.wit.semester06_project.factory.ServiceFactory;
import ie.wit.semester06_project.service.IValidationService;
import ie.wit.semester06_project.service.LoginValidationServiceImpl;

/**
 * Main Application Class
 */

public class FinanceApp extends Application
{
    public static ServiceFactory serviceFactory;
    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.v("Finance", "Application started");
        serviceFactory = new ServiceFactory();
    }

}

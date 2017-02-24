package ie.wit.semester06_project.main;

import android.app.Application;
import android.util.Log;

/**
 * Main Application Class
 */

public class FinanceApp extends Application
{

    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.v("Finance", "Application started");

    }

}

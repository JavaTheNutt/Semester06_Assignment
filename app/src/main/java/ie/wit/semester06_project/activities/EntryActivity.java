package ie.wit.semester06_project.activities;

import android.os.Bundle;

import com.google.firebase.database.FirebaseDatabase;

import ie.wit.semester06_project.main.FinanceApp;
import ie.wit.semester06_project.service.EntryService;
import ie.wit.semester06_project.service.data.UserDataService;

/**
 * This will be the class that will any activities that relate to login/logout wil inherit from.
 */

public class EntryActivity extends BaseActivity
{
    protected EntryService entryService;
    //protected UserDataService userDataService;


    /**
     * {@inheritDoc}
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //userDataService = new UserDataService(FirebaseDatabase.getInstance().getReference("users"));
        entryService = FinanceApp.serviceFactory.getEntryService();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        //userDataService.start();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        //userDataService.stop();
    }

}

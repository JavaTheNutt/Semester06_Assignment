package ie.wit.application.activities;

import android.os.Bundle;

import ie.wit.application.main.FinanceApp;
import ie.wit.application.service.EntryService;

/**
 * This will be the class that will any activities that relate to login/logout wil inherit from.
 */
public class EntryActivity extends BaseActivity
{
    /**
     * The Entry service.
     */
    protected EntryService entryService;


    /**
     * {@inheritDoc}
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        entryService = FinanceApp.serviceFactory.getEntryService();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }

}

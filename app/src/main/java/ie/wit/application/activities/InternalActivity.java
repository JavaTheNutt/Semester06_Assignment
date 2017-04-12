package ie.wit.application.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ie.wit.application.R;
import ie.wit.application.exceptions.NoUserLoggedInException;
import ie.wit.application.main.FinanceApp;
import ie.wit.application.service.data.TransactionDataService;

/**
 * The type Internal activity.
 */
public class InternalActivity extends BaseActivity
{
    /**
     * The Details database reference.
     */
    protected DatabaseReference detailsDatabaseReference;
    /**
     * The Transaction data service.
     */
    protected TransactionDataService transactionDataService;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        detailsDatabaseReference = FirebaseDatabase.getInstance().getReference(FinanceApp.getCurrentUserId() + "/transactions");
        transactionDataService = new TransactionDataService();
    }

    /**
     * {{@inheritDoc}}
     */
    @Override
    protected void onStart()
    {
        super.onStart();
        authService.registerUserNotLoggedInCallback(result -> showMainScreen());
        try {
            transactionDataService.start();
        } catch (NoUserLoggedInException e) {
            Log.e(TAG, "onStart: there is no user logged in", e);
        }
    }

    /**
     * {{@inheritDoc}}
     */
    @Override
    protected void onStop()
    {
        super.onStop();
        authService.registerUserNotLoggedInCallback(null);
        transactionDataService.stop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_finance, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        super.onPrepareOptionsMenu(menu);
        MenuItem dashboard = menu.findItem(R.id.menuDashboard);
        MenuItem addIncome = menu.findItem(R.id.menuAddIncome);
        MenuItem transactionList = menu.findItem(R.id.menuList);
        if (this instanceof DashboardActivity) {
            dashboard.setEnabled(false);
            addIncome.setEnabled(true);
            transactionList.setEnabled(true);
        } else if (this instanceof AddTransactionActivity) {
            addIncome.setEnabled(false);
            dashboard.setEnabled(true);
            transactionList.setEnabled(true);
        } else if(this instanceof TransactionListActivity){
            dashboard.setEnabled(true);
            addIncome.setEnabled(true);
            transactionList.setEnabled(false);
        }
        return true;
    }

    /**
     * Sign out.
     *
     * @param item the item
     */
    public void signOut(MenuItem item)
    {
        transactionDataService.stop();
        authService.signOut();
    }

    private void showMainScreen()
    {
        startActivity(new Intent(this, MainActivity.class));
    }

    /**
     * Add income.
     *
     * @param item the item
     */
    public void addIncome(MenuItem item)
    {
        startActivity(new Intent(this, AddTransactionActivity.class));
    }

    /**
     * Dashboard.
     *
     * @param item the item
     */
    public void dashboard(MenuItem item)
    {
        startActivity(new Intent(this, DashboardActivity.class));
    }
    public void transactionList(MenuItem item){
        startActivity(new Intent(this, TransactionListActivity.class));
    }
}

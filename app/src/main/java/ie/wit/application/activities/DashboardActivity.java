package ie.wit.application.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ie.wit.application.R;
import ie.wit.application.exceptions.NoUserLoggedInException;
import ie.wit.application.model.Transaction;
import ie.wit.application.model.ui.BalanceObserver;
import ie.wit.application.model.ui.TransactionAdapter;
import ie.wit.application.model.ui.UserDisplayNameObserver;

/**
 * This class represents the activity that will control the dashboard.
 */
public class DashboardActivity extends InternalActivity
{
    private BalanceObserver totalBalanceObserver;

    private UserDisplayNameObserver userObserver;

    private boolean totalsShown = true;

    /**
     * {@inheritDoc}
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        setUpReferences();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onStart()
    {
        super.onStart();
        try {
            transactionDataService.start();
        } catch (NoUserLoggedInException e) {
            Log.e(TAG, "onStart: no user logged in", e);
        }
        transactionDataService.registerBalanceObserver(totalBalanceObserver);
        authService.registerUserObserver(userObserver);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void onStop()
    {
        super.onStop();
        transactionDataService.registerTransactionCallback(null);
        transactionDataService.stop();
    }



    /**
     * Toggle totals.
     *
     * @param v the v
     */
    public void toggleTotals(View v)
    {
        totalsShown = !totalsShown;
        int visibilityId = totalsShown ? View.VISIBLE : View.GONE;
        int buttonTextId = totalsShown ? R.string.hideTotals : R.string.showTotals;
        ((Button) v).setText(getString(buttonTextId));
        findViewById(R.id.totalsTable).setVisibility(visibilityId);
    }

    private void setUpReferences()
    {
        Map<String, TextView> totals = new HashMap<>(3);
        totals.put("balance", (TextView) findViewById(R.id.dashboardCurrentBalance));
        totals.put("income", (TextView) findViewById(R.id.totalIncomeView));
        totals.put("expenditure", (TextView) findViewById(R.id.totalExpenditureView));
        totalBalanceObserver = new BalanceObserver(this, totals, "total");
        userObserver = new UserDisplayNameObserver(this, ((TextView) findViewById(R.id.userNameLabel)));
    }


}

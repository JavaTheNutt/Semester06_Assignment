package ie.wit.application.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import ie.wit.application.R;
import ie.wit.application.exceptions.NoUserLoggedInException;
import ie.wit.application.model.ui.Balance;
import ie.wit.application.model.ui.BalanceObserver;
import ie.wit.application.model.ui.UserDisplayNameObserver;

/**
 * This class represents the activity that will control the dashboard.
 */
public class DashboardActivity extends InternalActivity
{
    private BalanceObserver totalBalanceObserver;
    private BalanceObserver pendingBalanceObserver;
    private BalanceObserver completedBalanceObserver;

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
        //transactionDataService.registerBalanceObserver(totalBalanceObserver);
        BalanceObserver[] balanceObservers = {totalBalanceObserver, pendingBalanceObserver, completedBalanceObserver};
        transactionDataService.registerBalanceObservers(balanceObservers);
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
        Map<String, Map> views = getBalanceViews();
        totalBalanceObserver = new BalanceObserver(this, views.get("total"), "total");
        pendingBalanceObserver = new BalanceObserver(this, views.get("pending"), "pending");
        completedBalanceObserver = new BalanceObserver(this, views.get("completed"), "completed");
        userObserver = new UserDisplayNameObserver(this, ((TextView) findViewById(R.id.userNameLabel)));
    }
    private Map<String, Map> getBalanceViews(){
        Map<String, Map> views = new HashMap<>(3);
        Map<String, TextView> totals = new HashMap<>(3);
        Map<String, TextView> pending = new HashMap<>(3);
        Map<String, TextView> completed = new HashMap<>(3);
        totals.put("balance", (TextView) findViewById(R.id.currentBalanceView));
        totals.put("income", (TextView) findViewById(R.id.totalIncomeView));
        totals.put("expenditure", (TextView) findViewById(R.id.totalExpenditureView));
        pending.put("balance", (TextView) findViewById(R.id.pendingBalanceView));
        pending.put("income", (TextView) findViewById(R.id.pendingIncomeView));
        pending.put("expenditure", (TextView) findViewById(R.id.pendingExpenditureView));
        completed.put("balance", (TextView) findViewById(R.id.completedBalanceView));
        completed.put("income", (TextView) findViewById(R.id.completedIncomeView));
        completed.put("expenditure", (TextView) findViewById(R.id.completedExpenditureView));
        views.put("total", totals);
        views.put("pending", pending);
        views.put("completed", completed);
        return views;
    }


}

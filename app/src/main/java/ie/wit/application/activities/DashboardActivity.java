package ie.wit.application.activities;

import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ie.wit.application.R;
import ie.wit.application.exceptions.NoUserLoggedInException;
import ie.wit.application.main.FinanceApp;
import ie.wit.application.model.Transaction;
import ie.wit.application.model.ui_model.BalanceObserver;
import ie.wit.application.model.ui_model.TransactionAdapter;
import ie.wit.application.service.DashboardService;

/**
 * This class represents the activity that will control the dashboard.
 */
public class DashboardActivity extends InternalActivity
{
    private TextView currentBalance;
    private ListView listView;
    private TextView noDataFoundLabel;
    private TextView usernameLabel;
    private Button toggleListButton;
    private RadioGroup toggleDataRadio;

    private DashboardService dashboardService;
    private ArrayAdapter<Transaction> transactionAdapter;

    private BalanceObserver observer;
    private List<Transaction> transactions = new ArrayList<>();

    private boolean listShown = true;

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
        transactionDataService.registerBalanceObserver(observer);
        transactionDataService.registerTransactionCallback(result -> updateView());
        transactionAdapter = new TransactionAdapter(this, transactionDataService.getTransactions());
        listView.setAdapter(transactionAdapter);
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

    public void toggleList(View v)
    {
        listShown = !listShown;
        Button btn = (Button) v;
        int visibilityId = listShown ? View.VISIBLE : View.GONE;
        String buttonText = listShown ? "Hide Transactions" : "Show Transactions";
        int btnColor = listShown ? R.color.negativeBalance : R.color.positiveBalance;
        listView.setVisibility(visibilityId);
        toggleDataRadio.setVisibility(visibilityId);
        btn.setText(buttonText);
        btn.setBackgroundColor(ResourcesCompat.getColor(getResources(), btnColor, null));
    }

    private void setUpReferences()
    {
        currentBalance = (TextView) findViewById(R.id.dashboardCurrentBalance);
        listView = (ListView) findViewById(R.id.transactionList);
        noDataFoundLabel = (TextView) findViewById(R.id.noDataFoundLabel);
        dashboardService = FinanceApp.serviceFactory.getDashboardService();
        usernameLabel = (TextView) findViewById(R.id.userNameLabel);
        toggleListButton = (Button) findViewById(R.id.toggleShowList);
        toggleDataRadio = (RadioGroup) findViewById(R.id.toggleDataRadio);
        observer = new BalanceObserver(this, currentBalance);
    }

    private void updateView()
    {
        List<Transaction> transactionList = transactionDataService.getTransactions();
        transactionAdapter = new TransactionAdapter(this, transactionList);
        listView.setAdapter(transactionAdapter);
        toggleUi(!(transactionList == null || transactionList.isEmpty()));
    }

    private void toggleUi(boolean hasData)
    {
        View[] shownHasData = {
                listView,
                currentBalance,
                usernameLabel,
                toggleListButton,
                toggleDataRadio
        };
        if (hasData) {
            noDataFoundLabel.setVisibility(View.GONE);
            for (View view : shownHasData) {
                view.setVisibility(View.VISIBLE);
            }
            return;
        }
        noDataFoundLabel.setVisibility(View.VISIBLE);
        for (View view : shownHasData) {
            view.setVisibility(View.GONE);
        }
    }
}

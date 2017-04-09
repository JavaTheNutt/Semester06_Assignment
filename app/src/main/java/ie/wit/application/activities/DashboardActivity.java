package ie.wit.application.activities;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ie.wit.application.R;
import ie.wit.application.exceptions.NoUserLoggedInException;
import ie.wit.application.model.Transaction;
import ie.wit.application.model.ui_model.BalanceObserver;
import ie.wit.application.model.ui_model.TransactionAdapter;

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
    private RadioButton allDataButton;
    private RadioButton allIncomeButton;
    private RadioButton allExpenditureButton;

    private ArrayAdapter<Transaction> transactionAdapter;

    private BalanceObserver observer;

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
        int visibilityId = listShown ? View.VISIBLE : View.GONE;
        int buttonTextId = listShown ? R.string.dashboardShowList : R.string.dashboardHideList;
        listView.setVisibility(visibilityId);
        toggleDataRadio.setVisibility(visibilityId);
        ((Button) v).setText(getString(buttonTextId));
    }

    private void setUpReferences()
    {
        currentBalance = (TextView) findViewById(R.id.dashboardCurrentBalance);
        listView = (ListView) findViewById(R.id.transactionList);
        noDataFoundLabel = (TextView) findViewById(R.id.noDataFoundLabel);
        usernameLabel = (TextView) findViewById(R.id.userNameLabel);
        toggleListButton = (Button) findViewById(R.id.toggleShowList);
        toggleDataRadio = (RadioGroup) findViewById(R.id.toggleDataRadio);
        allDataButton = (RadioButton) findViewById(R.id.showAllRadioButton);
        allIncomeButton = (RadioButton) findViewById(R.id.showIncomeRadioButton);
        allExpenditureButton = (RadioButton) findViewById(R.id.showExpenditureRadioButton);
        toggleDataRadio.setOnCheckedChangeListener((group, checkedId) -> updateView());
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Transaction transaction  = (Transaction) listView.getItemAtPosition(position);
            Toast.makeText(DashboardActivity.this, transaction.toString(), Toast.LENGTH_SHORT).show();
        });
        observer = new BalanceObserver(this, currentBalance);
    }

    private void updateView()
    {
        List<Transaction> transactionList;
        if (allDataButton.isChecked()) {
            transactionList = transactionDataService.getTransactions();
        }else if(allIncomeButton.isChecked()){
            transactionList = transactionDataService.getIncomes();
        }else {
            transactionList = transactionDataService.getExpenditures();
        }
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

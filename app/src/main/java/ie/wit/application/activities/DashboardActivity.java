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

import java.util.List;

import ie.wit.application.R;
import ie.wit.application.exceptions.NoUserLoggedInException;
import ie.wit.application.model.Transaction;
import ie.wit.application.model.ui.BalanceObserver;
import ie.wit.application.model.ui.TransactionAdapter;

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
        registerForContextMenu(listView);
    }

    /**
     * Called when a context menu for the {@code view} is about to be shown.
     * Unlike onCreateOptionsMenu(Menu), this will be called every
     * time the context menu is about to be shown and should be populated for
     * the view (or item inside the view for {@link AdapterView} subclasses,
     * this can be found in the {@code menuInfo})).
     * <p>
     * Use {@link #onContextItemSelected(MenuItem)} to know when an
     * item has been selected.
     * <p>
     * It is not safe to hold onto the context menu after this method returns.
     *
     * @param menu     the context menu
     * @param v        the view the user selected
     * @param menuInfo info about the item selected
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard_list_context_menu, menu);
    }

    /**
     * This hook is called whenever an item in a context menu is selected. The
     * default implementation simply returns false to have the normal processing
     * happen (calling the item's Runnable or sending a message to its Handler
     * as appropriate). You can use this method for any items for which you
     * would like to do processing without those other facilities.
     * <p>
     * Use {@link MenuItem#getMenuInfo()} to get extra information set by the
     * View that added this menu item.
     * <p>
     * Derived classes should call through to the base class for it to perform
     * the default menu handling.
     *
     * @param item The context menu item that was selected.
     * @return boolean Return false to allow normal context menu processing to
     * proceed, true to consume it here.
     */
    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.contextEditItem:
                Log.d(TAG, "onContextItemSelected: edit selected");
                manipulateItem(info.position, true);
                return true;
            case R.id.contextDeleteItem:
                Log.d(TAG, "onContextItemSelected: delete selected");
                manipulateItem(info.position, false);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void manipulateItem(int position, boolean isEdit)
    {
        String operation = isEdit ? "editing" : "deleting";
        Log.d(TAG, "manipulateItem: " + operation + " item at position" + position);
        Transaction transaction = (Transaction) listView.getItemAtPosition(position);
        if (isEdit) {
            editItem(transaction);
            return;
        }
        deleteItem(transaction);
    }

    private void deleteItem(Transaction transaction)
    {
        Log.d(TAG, "deleteItem: item to be deleted: " + transaction.toString());
        transactionDataService.removeTransaction(transaction.getTimestamp());
    }

    private void editItem(Transaction transaction)
    {
        Log.d(TAG, "editItem: editing transaction: " + transaction.toString());
        Intent intent = new Intent(this, AddTransactionActivity.class);
        intent.putExtra("transaction", transaction);
        startActivity(intent);
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
            Transaction transaction = (Transaction) listView.getItemAtPosition(position);
            Toast.makeText(DashboardActivity.this, transaction.toString(), Toast.LENGTH_SHORT).show();
        });
        observer = new BalanceObserver(this, currentBalance);
    }

    private void updateView()
    {
        List<Transaction> transactionList;
        if (allDataButton.isChecked()) {
            transactionList = transactionDataService.getTransactions();
        } else if (allIncomeButton.isChecked()) {
            transactionList = transactionDataService.getIncomes();
        } else {
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

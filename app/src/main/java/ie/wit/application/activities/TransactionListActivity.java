package ie.wit.application.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.List;

import ie.wit.application.R;
import ie.wit.application.exceptions.NoUserLoggedInException;
import ie.wit.application.model.Transaction;
import ie.wit.application.model.ui.TransactionAdapter;

public class TransactionListActivity extends InternalActivity
{
    private ListView transactionList;
    private RadioButton showALlTypes;
    private RadioButton showIncome;
    private RadioButton showExpenditure;
    private RadioButton showAllDates;
    private RadioButton showPending;
    private RadioButton showCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list);
        setUpReferences();
    }

    /**
     * {{@inheritDoc}}
     */
    @Override
    protected void onStart()
    {
        super.onStart();
        try {
            transactionDataService.start();
            Log.d(TAG, "TransactionListActivity#onStart: transaction data service started");
        } catch (NoUserLoggedInException e) {
            Log.e(TAG, "TransactionListActivity#onStart: no user logged in", e);
        }
        transactionDataService.registerTransactionCallback(this::updateView);
        registerForContextMenu(transactionList);
    }

    /**
     * {{@inheritDoc}}
     */
    @Override
    protected void onStop()
    {
        super.onStop();
        transactionDataService.stop();
    }

    /**
     * Called when a context menu for the {@code view} is about to be shown.
     * Unlike onCreateOptionsMenu(Menu), this will be called every
     * time the context menu is about to be shown and should be populated for
     * the view (or item inside the view for AdapterView subclasses,
     * this can be found in the {@code menuInfo})).
     * <p>
     * Use {@link #onContextItemSelected(MenuItem)} to know when an
     * item has been selected.
     * <p>
     * It is not safe to hold onto the context menu after this method returns.
     *
     * @param menu
     * @param v
     * @param menuInfo
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

    private void setUpReferences()
    {
        transactionList = (ListView) findViewById(R.id.transactionListView);
        showALlTypes = (RadioButton) findViewById(R.id.transactionListShowAllTypes);
        showIncome = (RadioButton) findViewById(R.id.transactionListShowIncome);
        showExpenditure = (RadioButton) findViewById(R.id.transactionListShowExpenditure);
        showAllDates = (RadioButton) findViewById(R.id.transactionListShowAllDates);
        showPending = (RadioButton) findViewById(R.id.transactionListShowPending);
        showCompleted = (RadioButton) findViewById(R.id.transactionListShowCompleted);
        ((RadioGroup)findViewById(R.id.transactionListSelectDate)).setOnCheckedChangeListener((a, b) -> updateView(null));
        ((RadioGroup)findViewById(R.id.transactionListSelectType)).setOnCheckedChangeListener((a, b) -> updateView(null));
    }

    private void updateView(String data)
    {
        TransactionAdapter adapter = new TransactionAdapter(this, getTransactions());
        ((ListView) findViewById(R.id.transactionListView)).setAdapter(adapter);
    }

    private List<Transaction> getTransactions()
    {
        if (showALlTypes.isChecked()) {
            if (showAllDates.isChecked()) {
                return transactionDataService.getTransactions();
            } else if (showPending.isChecked()) {
                return transactionDataService.getAllPending();
            } else {
                return transactionDataService.getAllCompleted();
            }
        } else if (showIncome.isChecked()) {
            if (showAllDates.isChecked()) {
                return transactionDataService.getIncomes();
            } else if (showPending.isChecked()) {
                return transactionDataService.getIncomePending();
            } else {
                return transactionDataService.getIncomeCompleted();
            }
        } else {
            if (showAllDates.isChecked()) {
                return transactionDataService.getExpenditures();
            } else if (showPending.isChecked()) {
                return transactionDataService.getExpenditurePending();
            } else {
                return transactionDataService.getExpenditureCompleted();
            }
        }
    }

    private void manipulateItem(int position, boolean isEdit)
    {
        Log.d(TAG, "manipulateItem: manipulating item at position" + position);
        Transaction transaction = (Transaction) transactionList.getItemAtPosition(position);
        if (transaction != null) {
            if (isEdit) {
                editItem(transaction);
                return;
            }
            deleteItem(transaction);
        }
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

}

package ie.wit.application.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.List;

import ie.wit.application.R;
import ie.wit.application.exceptions.NoUserLoggedInException;
import ie.wit.application.model.Transaction;
import ie.wit.application.model.ui.TransactionAdapter;
import ie.wit.application.model.ui.UserDisplayNameObserver;
import ie.wit.application.service.TransactionListService;
import ie.wit.application.service.enums.TransactionSortType;

public class TransactionListActivity extends InternalActivity
{
    private ListView transactionList;
    private RadioButton showAllTypes;
    private RadioButton showIncome;
    private RadioButton showExpenditure;
    private RadioButton showAllDates;
    private RadioButton showPending;
    private RadioButton showCompleted;
    private RadioButton sortAsc;
    private RadioButton sortDue;
    private RadioButton sortTitle;
    private RadioButton sortAmount;

    private boolean filterShown = false;
    private TransactionListService transactionListService;


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
        authService.registerUserObserver(new UserDisplayNameObserver(this, ((TextView) findViewById(R.id.listViewUsername))));
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

    public void toggleFilter(View v)
    {
        int toBeSet = findViewById(R.id.filterSortTab).getVisibility() == View.GONE ? View.VISIBLE : View.GONE;
        String btnTxt = toBeSet == View.GONE ? "Show Filter/Sort" : "Hide Filter/Sort";
        findViewById(R.id.filterSortTab).setVisibility(toBeSet);
        ((Button) findViewById(R.id.toggleFilter)).setText(btnTxt);
    }

    private void setUpTab()
    {
        TabHost host = (TabHost) findViewById(R.id.filterSortTab);
        host.setup();

        TabHost.TabSpec spec = host.newTabSpec("Filter");
        spec.setContent(R.id.filterTab);
        spec.setIndicator("Filter");
        host.addTab(spec);

        spec = host.newTabSpec("Sort");
        spec.setContent(R.id.sortTab);
        spec.setIndicator("Sort");
        host.addTab(spec);
    }

    private void setUpReferences()
    {
        transactionList = (ListView) findViewById(R.id.transactionListView);
        showAllTypes = (RadioButton) findViewById(R.id.transactionListShowAllTypes);
        showIncome = (RadioButton) findViewById(R.id.transactionListShowIncome);
        showExpenditure = (RadioButton) findViewById(R.id.transactionListShowExpenditure);
        showAllDates = (RadioButton) findViewById(R.id.transactionListShowAllDates);
        showPending = (RadioButton) findViewById(R.id.transactionListShowPending);
        showCompleted = (RadioButton) findViewById(R.id.transactionListShowCompleted);
        sortDue = (RadioButton) findViewById(R.id.sortDue);
        sortAsc = (RadioButton) findViewById(R.id.sortAscending);
        sortAmount = (RadioButton) findViewById(R.id.sortAmount);
        sortTitle = (RadioButton) findViewById(R.id.sortTitle);

        ((RadioGroup) findViewById(R.id.transactionListSelectDate)).setOnCheckedChangeListener((a, b) -> updateView(null));
        ((RadioGroup) findViewById(R.id.transactionListSelectType)).setOnCheckedChangeListener((a, b) -> updateView(null));
        ((RadioGroup)findViewById(R.id.sortDir)).setOnCheckedChangeListener((a, b) -> updateView(null));
        ((RadioGroup)findViewById(R.id.sortField)).setOnCheckedChangeListener((a,b) ->updateView(null));
        transactionListService = new TransactionListService();
        setUpTab();
        toggleFilter(null);
    }

    private void updateView(String data)
    {
        TransactionAdapter adapter = new TransactionAdapter(this, sortTransactions(getTransactions()));
        ((ListView) findViewById(R.id.transactionListView)).setAdapter(adapter);
    }

    private List<Transaction> getTransactions()
    {
        if (showAllTypes.isChecked()) {
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

    private List<Transaction> sortTransactions(List<Transaction> transactions)
    {
        TransactionSortType currentType = getCurrentType();
        if (sortAsc.isChecked()) {
            return transactionListService.sortAscending(transactions, currentType);
        }
        return transactionListService.sortDescending(transactions, currentType);

    }

    private TransactionSortType getCurrentType(){
        if(sortAmount.isChecked()){
            return TransactionSortType.AMOUNT;
        }else if (sortDue.isChecked()){
            return TransactionSortType.DUE;
        } else if(sortTitle.isChecked()){
            return TransactionSortType.TITLE;
        } else {
            return TransactionSortType.ENTERED;
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
        transactionDataService.removeTransaction(transaction.getFirebaseId());
    }

    private void editItem(Transaction transaction)
    {
        Log.d(TAG, "editItem: editing transaction: " + transaction.toString());
        Intent intent = new Intent(this, AddTransactionActivity.class);
        intent.putExtra("transaction", transaction);
        startActivity(intent);
    }

}

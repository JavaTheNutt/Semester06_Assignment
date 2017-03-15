package ie.wit.semester06_project.activities;

import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ie.wit.semester06_project.R;
import ie.wit.semester06_project.main.FinanceApp;
import ie.wit.semester06_project.model.Balance;
import ie.wit.semester06_project.model.BalanceObserver;
import ie.wit.semester06_project.model.ITransaction;
import ie.wit.semester06_project.model.Income;
import ie.wit.semester06_project.model.Transaction;
import ie.wit.semester06_project.service.CalculationService;
import ie.wit.semester06_project.service.DashboardService;

/**
 * This class represents the activity that will control the dashboard.
 */
public class DashboardActivity extends InternalActivity
{
    private TextView currentBalance;
    private ListView listView;
    private TextView noDataFoundLabel;
    private TextView usernameLabel;

    private CalculationService calculationService;
    private DashboardService dashboardService;
    private FirebaseListAdapter<Transaction> transactionAdapter;

    private float currentTotal = 0;
    private Balance balance = new Balance();
    private BalanceObserver observer;
    private List<Long> usedTimeStamps = new ArrayList<>();
    private List<Transaction> transactions = new ArrayList<>();

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
        currentBalance = (TextView) findViewById(R.id.dashboardCurrentBalance);
        listView = (ListView) findViewById(R.id.transactionList);
        noDataFoundLabel = (TextView) findViewById(R.id.noDataFoundLabel);
        calculationService = FinanceApp.serviceFactory.getCalculationService();
        dashboardService = FinanceApp.serviceFactory.getDashboardService();
        usernameLabel = (TextView)findViewById(R.id.userNameLabel);
        observer = new BalanceObserver(this, currentBalance);
        observer.observe(balance);
        usernameLabel.setText("Welcome, " + FinanceApp.getCurrentUser().getFirstName() + " " + FinanceApp.getCurrentUser().getSurname() + "!");

        detailsDatabaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Map<String, Map> transactionMap = (HashMap<String, Map>) dataSnapshot.getValue();
                Log.v(TAG, Integer.toString(transactions.size()));
                transactions = dashboardService.createTransactions(transactionMap);
                if(transactions == null | transactions.size() == 0){
                    noDataFoundLabel.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                    currentBalance.setVisibility(View.GONE);
                    usernameLabel.setVisibility(View.GONE);
                    return;
                }
                noDataFoundLabel.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                currentBalance.setVisibility(View.VISIBLE);
                usernameLabel.setVisibility(View.VISIBLE);
                setBalance(transactions);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.e(TAG, "Unable to retrieve transaction data", databaseError.toException());
            }

            private void setBalance(List<Transaction> transactions){
                balance.setBalance(0);
                for (Transaction transaction : transactions) {
                    if (transaction.isIncome())
                       balance.incrementBalance(transaction.getAmount());
                    else
                        balance.decrementBalance(transaction.getAmount());
                }
            }
        });
        transactionAdapter = new FirebaseListAdapter<Transaction>(this, Transaction.class, R.layout.row_transaction, detailsDatabaseReference)
        {
            @Override
            protected void populateView(View v, Transaction model, int position)
            {
                ((TextView) v.findViewById(R.id.rowDate)).setText( new SimpleDateFormat("dd-MM-yyyy", Locale.UK).format(new Date(model.getTimestamp())));
                ((TextView)v.findViewById(R.id.rowAmount)).setText(model.getAmount().toString());
                ((TextView) v.findViewById(R.id.transactionTitle)).setText(dashboardService.titleCase(model.getTitle()));
                int colorId = model.isIncome() ? R.color.positiveBalance : R.color.negativeBalance;
                ((TextView)v.findViewById(R.id.transactionTitle)).setTextColor(ResourcesCompat.getColor(getResources(), colorId, null));
            }
        };
        listView.setAdapter(transactionAdapter);
    }

}

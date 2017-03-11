package ie.wit.semester06_project.activities;

import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ie.wit.semester06_project.R;
import ie.wit.semester06_project.activities.ui.TransactionHolder;
import ie.wit.semester06_project.main.FinanceApp;
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
    private RecyclerView listView;
    private LinearLayoutManager layoutManager;

    private CalculationService calculationService;
    private DashboardService dashboardService;
    private FirebaseListAdapter<Transaction> transactionAdapter;
    private FirebaseRecyclerAdapter<Transaction, TransactionHolder> transactionHolderFirebaseRecyclerAdapter;

    private float currentTotal = 0;
    private List<Long> usedTimeStamps = new ArrayList<>();

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
        listView = (RecyclerView) findViewById(R.id.transactionList);
        calculationService = FinanceApp.serviceFactory.getCalculationService();
        dashboardService = FinanceApp.serviceFactory.getDashboardService();
        ((TextView)findViewById(R.id.userNameLabel)).setText("Welcome, " + FinanceApp.getCurrentUser().getFirstName() + " " + FinanceApp.getCurrentUser().getSurname() + "!");
        transactionHolderFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Transaction, TransactionHolder>(Transaction.class, R.layout.row_transaction, TransactionHolder.class, detailsDatabaseReference)
        {
            @Override
            protected void populateViewHolder(TransactionHolder viewHolder, Transaction model, int position)
            {
                Log.v(TAG, model.toString());

                viewHolder.setTransactionTitle(model.getTitle());
                viewHolder.setTransactionAmount(model.getAmount());
                viewHolder.setTransactionDate(model.getTimestamp());
            }
        };
        // Found at: https://codelabs.developers.google.com/codelabs/firebase-android/#6
        transactionHolderFirebaseRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver(){
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount)
            {
                super.onItemRangeInserted(positionStart, itemCount);
                Log.v(TAG, "onItemRangeInserted called for: " + this.getClass().getSimpleName());
                int transactionCount = transactionHolderFirebaseRecyclerAdapter.getItemCount();

            }
        });
        listView.setAdapter(transactionHolderFirebaseRecyclerAdapter);
        refreshView();
    }


    /**
     * This method will update the
     */
    public void refreshView()
    {
        //currentTotal = calculationService.getTotal();
        String strTotal = "€" + Float.toString(currentTotal);
        int colorId = currentTotal <= 0 ? R.color.negativeBalance : R.color.positiveBalance;
        currentBalance.setTextColor(ResourcesCompat.getColor(getResources(), colorId, null));
        currentBalance.setText(strTotal);
    }
}

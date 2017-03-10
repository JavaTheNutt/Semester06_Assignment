package ie.wit.semester06_project.activities;

import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ie.wit.semester06_project.R;
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

    private CalculationService calculationService;
    private DashboardService dashboardService;
    private FirebaseListAdapter<Transaction> transactionAdapter;

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
        transactionAdapter = new FirebaseListAdapter<Transaction>(this, Transaction.class, R.layout.row_transaction, detailsDatabaseReference)
        {
            @Override
            protected void populateView(View v, Transaction model, int position)
            {
                ((TextView) v.findViewById(R.id.rowDate)).setText( new SimpleDateFormat("dd-MM-yyyy", Locale.UK).format(new Date(model.getTimestamp())));
                ((TextView)v.findViewById(R.id.rowAmount)).setText(model.getAmount().toString());
                ((TextView) v.findViewById(R.id.transactionTitle)).setText(model.getTitle());
                int colorId = model.isIncome() ? R.color.positiveBalance : R.color.negativeBalance;
                v.setBackgroundColor(ResourcesCompat.getColor(getResources(), colorId, null));
                if(!usedTimeStamps.contains(model.getTimestamp())){
                    usedTimeStamps.add(model.getTimestamp());
                    if(model.isIncome()){
                        currentTotal += model.getAmount();
                    }else {
                        currentTotal -= model.getAmount();
                    }
                }
                refreshView();
            }
        };
        //listView.setAdapter(transactionAdapter);
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

package ie.wit.application.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import ie.wit.application.R;
import ie.wit.application.fragments.DatePickerFragment;
import ie.wit.application.main.FinanceApp;
import ie.wit.application.model.Transaction;
import ie.wit.application.service.AddTransactionService;

/**
 * This class will be the activity for adding a transaction to the associated transactions for a specific user
 */
public class AddTransactionActivity extends InternalActivity
{

    private static final Map<String, EditText> fields = new HashMap<>(2);
    private static final Map<String, String> transactionDetails = new HashMap<>(4);

    private AddTransactionService addTransactionService;

    private String dueDateText;
    private Long dueDateTimestamp;

    /**
     * {{@inheritDoc}}
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);
        addTransactionService = new AddTransactionService(this);
        setUpReferences();
        handleExtras(getIntent().getExtras());
        changeDueDateLabel();
    }

    private void handleExtras(Bundle extras)
    {
        if (extras == null) {
            return;
        }
        Transaction transaction = (Transaction) extras.getSerializable("transaction");
        if (transaction == null) {
            return;
        }
        String isIncome = transaction.isIncome() ? "true" : "false";
        transactionDetails.put("title", transaction.getTitle());
        transactionDetails.put("amount", transaction.getAmount().toString());
        transactionDetails.put("isIncome", isIncome);
        transactionDetails.put("timestamp", transaction.getTimestamp().toString());
        transactionDetails.put("dueDate", transaction.getDueDate().toString());
        transactionDetails.put("firebaseId", transaction.getFirebaseId());
        dueDateTimestamp = transaction.getDueDate();
        dueDateText = FinanceApp.serviceFactory.getUtil().convertFromTimestamp(dueDateTimestamp);
        addDetailsToView();
        changeDueDateLabel();
    }

    /**
     * Show date picker.
     *
     * @param v the v
     */
    public void showDatePicker(View v)
    {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.registerDateSelectedCallback(this::dateSelected);
        datePickerFragment.show(getFragmentManager(), "addTransactionDatePicker");
    }

    private void dateSelected(String date)
    {
        Log.d(TAG, "dateSelected: " + date + " has been selected by the datepicker.");
        try {
            dueDateTimestamp = FinanceApp.serviceFactory.getUtil().convertToTimestamp(date);
            Log.d(TAG, "dateSelected: converted " + date + " to " + dueDateTimestamp);
        } catch (ParseException e) {
            Log.e(TAG, "dateSelected: date is not in correct format", e);
            Toast.makeText(this, "date selection failed", Toast.LENGTH_SHORT).show();
        }
        dueDateText = date;
        changeDueDateLabel();
    }

    private void addDetailsToView()
    {
        fields.get("title").setText(transactionDetails.get("title"));
        fields.get("amount").setText(transactionDetails.get("amount"));
        int checkedId = transactionDetails.get("isIncome").equalsIgnoreCase("true") ? R.id.isIncome : R.id.isExpenditure;
        ((RadioButton) findViewById(checkedId)).setChecked(true);

    }

    private void setUpReferences()
    {
        fields.put("title", ((EditText) findViewById(R.id.addIncomeTitle)));
        fields.put("amount", ((EditText) findViewById(R.id.addIncomeAmount)));
        ((RadioGroup) findViewById(R.id.transactionType)).setOnCheckedChangeListener((a, b) -> changeDueDateLabel());
    }

    private void changeDueDateLabel()
    {
        if (dueDateTimestamp == null) {
            Calendar calendar = Calendar.getInstance();
            dueDateTimestamp = calendar.getTimeInMillis();
            Log.d(TAG, "changeDueDateLabel: due timestamp: " + dueDateTimestamp);
            dueDateText = calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
        }
        String transactionType = ((RadioButton) findViewById(R.id.isIncome)).isChecked() ? "Income" : "Expenditure";
        int textColorId = transactionType.equalsIgnoreCase("income") ? R.color.positiveBalance : R.color.negativeBalance;
        String dateLabelText = getString(R.string.transactionDueDate, transactionType, dueDateText);
        ((TextView) findViewById(R.id.dueDateLabel)).setText(dateLabelText);
        ((TextView) findViewById(R.id.dueDateLabel)).setTextColor(ResourcesCompat.getColor(getResources(), textColorId, null));
    }

    private boolean validateAndCreate()
    {
        Map<String, String> result = addTransactionService.extractTransactionDetails(fields);
        if (result == null) {
            return false;
        }
        for (Map.Entry<String, String> value : result.entrySet()) {
            transactionDetails.put(value.getKey(), value.getValue());
        }
        return true;
    }

    /**
     * Submit transaction.
     *
     * @param view the view
     */
    public void submitTransaction(View view)
    {
        if (!validateAndCreate()) {
            return;
        }
        String isIncome = ((RadioButton) findViewById(R.id.isIncome)).isChecked() ? "true" : "false";
        transactionDetails.put("isIncome", isIncome);
        transactionDetails.put("dueDate", Long.toString(dueDateTimestamp));
        Transaction transaction = addTransactionService.createTransaction(transactionDetails);
        Log.d(TAG, "submitTransaction: creating transaction for " + transaction.toString());
        transactionDataService.addTransaction(transaction);
        startActivity(new Intent(this, DashboardActivity.class));
    }
}

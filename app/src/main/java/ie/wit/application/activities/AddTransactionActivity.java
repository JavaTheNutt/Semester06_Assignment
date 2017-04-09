package ie.wit.application.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import java.util.HashMap;
import java.util.Map;

import ie.wit.application.R;
import ie.wit.application.model.Transaction;
import ie.wit.application.service.AddTransactionService;

/**
 * This class will be the activity for adding a transaction to the associated transactions for a specific user
 */
public class AddTransactionActivity extends InternalActivity
{

    private EditText title;
    private EditText amount;

    private Long currentTimestamp;

    private static final Map<String, EditText> fields = new HashMap<>(2);
    private static final Map<String, String> transactionDetails = new HashMap<>(4);

    private AddTransactionService addTransactionService;

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
        title = (EditText) findViewById(R.id.addIncomeTitle);
        amount = (EditText) findViewById(R.id.addIncomeAmount);
        handleExtras(getIntent().getExtras());
    }

    private void handleExtras(Bundle extras){
        if (extras == null) {
            return;
        }
        Transaction transaction = (Transaction) extras.getSerializable("transaction");
        if (transaction == null) {
            return;
        }
        String isIncome = transaction.isIncome() ? "true": "false";
        transactionDetails.put("title", transaction.getTitle());
        transactionDetails.put("amount", transaction.getAmount().toString());
        transactionDetails.put("isIncome", isIncome);
        transactionDetails.put("timestamp", transaction.getTimestamp().toString());
        addDetailsToView();
    }
    private void addDetailsToView(){
        fields.get("title").setText(transactionDetails.get("title"));
        fields.get("amount").setText(transactionDetails.get("amount"));
        int checkedId = transactionDetails.get("isIncome").equalsIgnoreCase("true")? R.id.isIncome: R.id.isExpenditure;
        ((RadioButton)findViewById(checkedId)).setChecked(true);
    }
    private void setUpReferences(){
        fields.put("title", ((EditText)findViewById(R.id.addIncomeTitle)));
        fields.put("amount", ((EditText)findViewById(R.id.addIncomeAmount)));
    }
    private boolean validateAndCreate(){
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
     * Create the transaction and add it to the list.
     *
     * @param view the view that was clicked
     */
    public void submitClicked(View view)
    {
        if (title.getText().toString().length() > 3 && Float.parseFloat(amount.getText().toString()) > 0) {
            Transaction tempIncome = new Transaction();
            tempIncome.setTitle(title.getText().toString());
            tempIncome.setAmount(Float.parseFloat(amount.getText().toString()));
            tempIncome.setTimestamp(currentTimestamp);
            tempIncome.setIncome(((RadioButton) findViewById(R.id.isIncome)).isChecked());
            Log.v(TAG, tempIncome.toString());
            transactionDataService.addTransaction(tempIncome);
            //detailsDatabaseReference.child(tempIncome.getTimestamp().toString()).setValue(tempIncome);
            startActivity(new Intent(this, DashboardActivity.class));
        }
    }
    public void submitTransaction(View view){
        if(!validateAndCreate()){
            return;
        }
        Transaction transaction = addTransactionService.createTransaction(transactionDetails);
        String isIncome = ((RadioButton)findViewById(R.id.isIncome)).isChecked() ? "true": "false";
        transactionDetails.put("isIncome", isIncome);
        Log.d(TAG, "submitTransaction: creating transaction for " + transaction.toString());
        transactionDataService.addTransaction(transaction);
        startActivity(new Intent(this, DashboardActivity.class));
    }
}

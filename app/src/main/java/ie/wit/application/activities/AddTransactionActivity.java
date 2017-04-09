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
        setUpReferences();
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
    
    public void submitTransaction(View view){
        if(!validateAndCreate()){
            return;
        }
        String isIncome = ((RadioButton)findViewById(R.id.isIncome)).isChecked() ? "true": "false";
        transactionDetails.put("isIncome", isIncome);
        Transaction transaction = addTransactionService.createTransaction(transactionDetails);
        Log.d(TAG, "submitTransaction: creating transaction for " + transaction.toString());
        transactionDataService.addTransaction(transaction);
        startActivity(new Intent(this, DashboardActivity.class));
    }
}

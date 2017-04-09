package ie.wit.application.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import ie.wit.application.R;
import ie.wit.application.model.Transaction;

/**
 * This class will be the activity for adding a transaction to the associated transactions for a specific user
 */
public class AddTransactionActivity extends InternalActivity
{

    private EditText title;
    private EditText amount;

    private Long currentTimestamp;

    /**
     * {{@inheritDoc}}
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        setContentView(R.layout.activity_add_income);
        title = (EditText) findViewById(R.id.addIncomeTitle);
        amount = (EditText) findViewById(R.id.addIncomeAmount);
        if (extras != null) {
            Transaction transaction = (Transaction) extras.getSerializable("transaction");
            if (transaction != null) {
                title.setText(transaction.getTitle());
                amount.setText(transaction.getAmount().toString());
                currentTimestamp = transaction.getTimestamp();
                if (transaction.isIncome()) {
                    ((RadioButton) findViewById(R.id.isIncome)).setChecked(true);
                } else {
                    ((RadioButton) findViewById(R.id.isExpenditure)).setChecked(true);
                }
            }
        }
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
}

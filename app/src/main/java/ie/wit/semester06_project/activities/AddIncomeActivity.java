package ie.wit.semester06_project.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import ie.wit.semester06_project.R;
import ie.wit.semester06_project.model.Transaction;

/**
 * This class will be the activity for adding an income to the
 */
public class AddIncomeActivity extends InternalActivity
{

    private EditText title;
    private EditText amount;

    private Long currentTimestamp;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        setContentView(R.layout.activity_add_income);
        title = (EditText) findViewById(R.id.addIncomeTitle);
        amount = (EditText) findViewById(R.id.addIncomeAmount);
        if(extras != null){
            Transaction transaction = (Transaction) extras.getSerializable("transaction");
            if (transaction != null) {
                title.setText(transaction.getTitle());
                amount.setText(transaction.getAmount().toString());
                currentTimestamp = transaction.getTimestamp();
                if(transaction.isIncome()){
                    ((RadioButton)findViewById(R.id.isIncome)).setChecked(true);
                }else{
                    ((RadioButton)findViewById(R.id.isExpenditure)).setChecked(true);
                }
            }
        }
    }
    public void submitClicked(View view){
        if(title.getText().toString().length() > 3 && Float.parseFloat(amount.getText().toString()) > 0){
            Transaction tempIncome = new Transaction();
            tempIncome.setTitle(title.getText().toString());
            tempIncome.setAmount(Float.parseFloat(amount.getText().toString()));
            tempIncome.setTimestamp(currentTimestamp);
            tempIncome.setIncome(((RadioButton)findViewById(R.id.isIncome)).isChecked());
            Log.v(TAG, tempIncome.toString());
            detailsDatabaseReference.child(tempIncome.getTimestamp().toString()).setValue(tempIncome);
            startActivity(new Intent(this, DashboardActivity.class));
        }
    }
}

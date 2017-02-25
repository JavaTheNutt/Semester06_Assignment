package ie.wit.semester06_project.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import ie.wit.semester06_project.R;
import ie.wit.semester06_project.main.FinanceApp;
import ie.wit.semester06_project.model.Income;

public class AddIncomeActivity extends InternalActivity
{

    private EditText title;
    private EditText amount;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);
        title = (EditText) findViewById(R.id.addIncomeTitle);
        amount = (EditText) findViewById(R.id.addIncomeAmount);
    }
    public void submitClicked(View view){
        if(title.getText().toString().length() > 3 && Float.parseFloat(amount.getText().toString()) > 0){
            Income tempIncome = new Income();
            tempIncome.setTitle(title.getText().toString());
            tempIncome.setAmount(Float.parseFloat(amount.getText().toString()));
            Log.v(TAG, tempIncome.toString());
            detailsDatabaseReference.child("income").child(tempIncome.getTimestamp().toString()).setValue(tempIncome);
        }
    }
}

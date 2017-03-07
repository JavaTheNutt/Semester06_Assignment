package ie.wit.semester06_project.repo;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ie.wit.semester06_project.activities.BaseActivity;
import ie.wit.semester06_project.main.FinanceApp;
import ie.wit.semester06_project.model.ITransaction;
import ie.wit.semester06_project.model.Income;
import ie.wit.semester06_project.model.Transaction;


/**
 * Created by joewe on 25/02/2017.
 */

public class IncomeRepo /*implements IRepo<Transaction>*/
{
    private DatabaseReference databaseReference;
    private List<Transaction> allIncomes;

    private List<String> keys;

    public IncomeRepo()
    {
        databaseReference = FirebaseDatabase.getInstance().getReference("details/" + FinanceApp.getCurrentUser().getKey() + "/income");
        allIncomes = new ArrayList<>();
        keys = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener (){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                HashMap<String, Map> incomes = (HashMap<String, Map>) dataSnapshot.getValue();
                Log.v(BaseActivity.TAG, "Data changed for user incomes");
                if(incomes == null || incomes.size() == 0){
                    return;
                }
                for(Map.Entry<String, Map> currentIncome : incomes.entrySet()){
                    Log.v(BaseActivity.TAG, "current income:\t" + currentIncome);
                    Transaction income = new Transaction(Long.parseLong(currentIncome.getKey()));
                    income.setTitle((String) currentIncome.getValue().get("title"));
                    Object amount = currentIncome.getValue().get("amount");
                    if(amount instanceof Double){
                        income.setAmount(((Double) amount).floatValue());
                    }
                    if(amount instanceof Long){
                        income.setAmount(((Long) amount).floatValue());
                    }
                    if (amount instanceof Float){
                        income.setAmount((Float) amount);
                    }
                    Log.v(BaseActivity.TAG, "The current amount:\t" + amount);
                    allIncomes.add(income);
                    Long tempKey = (Long) currentIncome.getValue().get("timestamp");
                    keys.add(tempKey.toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.e(BaseActivity.TAG, databaseError.toString());
            }
        });

    }
    public List<Transaction> getAllItems(){
        return allIncomes;
    }
    public Transaction getOneItem(Long timestamp) throws Exception
    {
        for (Transaction income : allIncomes) {
            if (income.getTimestamp().equals(timestamp)){
                return income;
            }
        }
        throw new Exception("The specfied Income record was not found");
    }
    public void addItem(Transaction income){
        databaseReference.child(income.getTimestamp().toString()).setValue(income);
    }
    public void removeItem(Transaction income) throws Exception{
        if(!keys.contains(income.getTimestamp().toString())){
            throw new Exception("There is no  income with that timestamp");
        }
        // FIXME: 25/02/2017 TEST!!!!!
        removeItem(income.getTimestamp());
        databaseReference.child(income.getTimestamp().toString()).setValue(null);
    }
    public void removeItem(Long timestamp) throws Exception{
        if(!keys.contains(timestamp.toString())){
            throw new Exception("There is no  income with that timestamp");
        }
        // FIXME: 25/02/2017 TEST!!!!!
        databaseReference.child(timestamp.toString()).setValue(null);
    }

    
}

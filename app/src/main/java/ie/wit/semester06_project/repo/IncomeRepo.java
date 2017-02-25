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
import ie.wit.semester06_project.model.Income;


/**
 * Created by joewe on 25/02/2017.
 */

public class IncomeRepo
{
    private DatabaseReference databaseReference;
    private List<Income> allIncomes;
    List<String> keys;

    public IncomeRepo()
    {
        databaseReference = FirebaseDatabase.getInstance().getReference("details/" + FinanceApp.getCurrentUser().getKey() + "/income");
        databaseReference.addValueEventListener(getValueEventListener());

    }
    public List<Income> getAllIncomes(){
        return allIncomes;
    }
    public void addIncome(Income income){
        databaseReference.child(income.getTimestamp().toString()).setValue(income);
    }
    public void removeIncome(Income income) throws Exception{
        if(!keys.contains(income.getTimestamp().toString())){
            throw new Exception("There is no  income with that timestamp");
        }
        // FIXME: 25/02/2017 TEST!!!!!
        databaseReference.child(income.getTimestamp().toString()).setValue(null);
    }
    private ValueEventListener getValueEventListener(){

       return new ValueEventListener (){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                HashMap<String, Map> incomes = (HashMap<String, Map>) dataSnapshot.getValue();
                allIncomes = new ArrayList<>(incomes.size() * 3);
                for(Map.Entry<String, Map> currentIncome : incomes.entrySet()){
                    Income income = new Income(Long.parseLong(currentIncome.getKey()));
                    income.setTitle((String) currentIncome.getValue().get("title"));
                    income.setAmount((Float) currentIncome.getValue().get("amount"));
                    allIncomes.add(income);
                    keys.add(currentIncome.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.e(BaseActivity.TAG, databaseError.toString());
            }
        };
    }
    
}

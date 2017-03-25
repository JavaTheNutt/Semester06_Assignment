package ie.wit.semester06_project.service;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ie.wit.semester06_project.activities.BaseActivity;
import ie.wit.semester06_project.model.Transaction;

/**
 * Created by joewe on 25/02/2017.
 */

public class DashboardService
{
    public List<Transaction> createTransactions(Map<String, Map> transactions)
    {
        List<Transaction> transactionList = new ArrayList<Transaction>();
        if (transactions == null) {
            return transactionList;
        }
        for (Map.Entry<String, Map> transactionMap : transactions.entrySet()) {
            Log.v(BaseActivity.TAG, transactionMap.toString());

            Transaction transaction = convertToPojo(transactionMap);
            transactionList.add(transaction);
        }
        return transactionList;
    }

    public String titleCase(String str)
    {
        Log.d(BaseActivity.TAG, "titleCase: fixing string: " + str);
        String[] splitStr = str.split("\\s+");
        String[] fixedStr = new String[splitStr.length];
        Log.d(BaseActivity.TAG, "titleCase: numbers of words: " + splitStr.length);
        int i = 0;
        for (String s : splitStr) {
            Log.d(BaseActivity.TAG, "titleCase: current string: " + s);
            String tempStr = s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
            fixedStr[i] = tempStr;
            i++;
        }

        return TextUtils.join(" ", fixedStr);
    }

    private Transaction convertToPojo(Map.Entry<String, Map> transactionMap)
    {
        Log.v(BaseActivity.TAG, transactionMap.toString());
        Transaction transaction = new Transaction();
        transaction.setTimestamp((Long) transactionMap.getValue().get("timestamp"));
        Number amount = (Number) transactionMap.getValue().get("amount");
        transaction.setAmount(Float.parseFloat(amount.toString()));
        transaction.setIncome((boolean) transactionMap.getValue().get("income"));
        transaction.setTitle((String) transactionMap.getValue().get("title"));
        Log.d(BaseActivity.TAG, "convertToPojo() returned: " + transaction.toString());
        return transaction;
    }
}

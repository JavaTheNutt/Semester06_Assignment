package ie.wit.application.service;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

import ie.wit.application.exceptions.InvalidTransactionException;
import ie.wit.application.main.FinanceApp;
import ie.wit.application.model.Transaction;

import static ie.wit.application.activities.BaseActivity.TAG;

/**
 * Created by joewe on 09/04/2017.
 */
public class AddTransactionService
{
    private Context activity;

    /**
     * Instantiates a new Add transaction service.
     *
     * @param activity the activity
     */
    public AddTransactionService(Context activity)
    {
        this.activity = activity;
    }

    /**
     * Extract transaction details map.
     *
     * @param fields the fields
     * @return the map
     */
    public Map<String, String> extractTransactionDetails(Map<String, EditText> fields)
    {
        String title = fields.get("title").getText().toString();
        String amount = fields.get("amount").getText().toString();
        try {
            validateFields(title, amount);
        } catch (InvalidTransactionException e) {
            Log.e(TAG, "extractTransactionDetails: invalid transaction", e);
            FinanceApp.serviceFactory.getUtil().makeAToast(this.activity, e.getMessage());
            return null;
        }
        Map<String, String> details = new HashMap<>();
        details.put("title", title);
        details.put("amount", amount);
        return details;
    }

    /**
     * Create transaction transaction.
     *
     * @param values the values
     * @return the transaction
     */
    public Transaction createTransaction(Map<String, String> values)
    {
        Transaction transaction = new Transaction();
        boolean isIncome = values.get("isIncome").equalsIgnoreCase("true");
        transaction.setTitle(values.get("title"));
        transaction.setIncome(isIncome);
        transaction.setAmount(Float.parseFloat(values.get("amount")));
        String timestamp = values.get("timestamp");
        transaction.setDueDate(Long.parseLong(values.get("dueDate")));
        if (values.get("firebaseId") != null) {
            transaction.setFirebaseId(values.get("firebaseId"));
        }
        if (timestamp == null) {
            transaction.setTimestamp(null);
            return transaction;
        }
        transaction.setTimestamp(Long.parseLong(timestamp));
        return transaction;
    }

    private boolean validateFields(String titleValue, String amountValue) throws InvalidTransactionException
    {
        if (titleValue.length() == 0) {
            throw new InvalidTransactionException("Title field is required");
        }

        if (titleValue.length() < 3) {
            throw new InvalidTransactionException("Title field must be at least three characters");
        }

        try {
            Float.parseFloat(amountValue);
        } catch (NumberFormatException e) {
            throw new InvalidTransactionException("Amount is not properly fomatted");
        }
        return true;
    }
}

package ie.wit.application.model.ui_model;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ie.wit.application.R;
import ie.wit.application.model.Transaction;

import static ie.wit.application.activities.BaseActivity.TAG;

/**
 * Created by joewe on 08/04/2017.
 */

public class TransactionAdapter extends ArrayAdapter<Transaction>
{
    private final Context context;
    private List<Transaction> transactions;

    /**
     * Constructor
     *
     * @param context The current context.
     */
    public TransactionAdapter(@NonNull Context context, List<Transaction> transactions)
    {
        super(context, R.layout.row_transaction, transactions);
        this.context = context;
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.row_transaction, parent, false);
        Transaction transaction = transactions.get(position);
        Log.d(TAG, "getView: called for"  + transaction.getTitle());
        ((TextView) view.findViewById(R.id.transactionTitle)).setText(transaction.getTitle());
        ((TextView) view.findViewById(R.id.rowDate)).setText(new SimpleDateFormat("dd-MM-yyyy", Locale.UK).format(new Date(transaction.getTimestamp())));
        ((TextView) view.findViewById(R.id.rowAmount)).setText(transaction.getAmount().toString());
        int colorId = transaction.isIncome() ? R.color.positiveBalance : R.color.negativeBalance;
        ((TextView) view.findViewById(R.id.transactionTitle)).setTextColor(ResourcesCompat.getColor(context.getResources(), colorId, null));
        return view;
    }
}

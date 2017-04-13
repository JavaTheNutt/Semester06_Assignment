package ie.wit.application.model.ui;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
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
import ie.wit.application.main.FinanceApp;
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
     * @param context      The current context.
     * @param transactions the transactions
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
        Log.d(TAG, "getView: called for" + transaction.getTitle());
        Resources resources =  getContext().getResources();
        String transactionDueDateLabelText = resources.getString(R.string.transactionRowDueLabel, FinanceApp.serviceFactory.getUtil().convertFromTimestamp(transaction.getDueDate()));
        String transactionEnteredDateText = resources.getString(R.string.transactionRowEnteredLabel, FinanceApp.serviceFactory.getUtil().convertFromTimestamp(transaction.getTimestamp()));
        String transactionAmountText = resources.getString(R.string.transactionAmountLabel, transaction.getAmount().toString());
        ((TextView) view.findViewById(R.id.transactionTitle)).setText(FinanceApp.serviceFactory.getUtil().titleCase(transaction.getTitle()));
        ((TextView) view.findViewById(R.id.rowDate)).setText(transactionEnteredDateText);
        ((TextView) view.findViewById(R.id.rowAmount)).setText(transactionAmountText);
        ((TextView) view.findViewById(R.id.dueDateLabel)).setText(transactionDueDateLabelText);
        int colorId = transaction.isIncome() ? R.color.positiveBalance : R.color.negativeBalance;
        ((TextView) view.findViewById(R.id.transactionTitle)).setTextColor(ResourcesCompat.getColor(context.getResources(), colorId, null));
        return view;
    }
}

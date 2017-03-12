package ie.wit.semester06_project.activities.ui;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ie.wit.semester06_project.R;
import ie.wit.semester06_project.activities.BaseActivity;

/**
 * This class will act as a holder for each transaction that will be displayed in the list of transactions
 */

public class TransactionHolder extends RecyclerView.ViewHolder
{
    private final TextView transactionTitle;
    private final TextView transactionDate;
    private final TextView transactionAmount;

    public TransactionHolder(View itemView)
    {
        super(itemView);
        this.transactionTitle = (TextView) itemView.findViewById(R.id.transactionTitle);
        this.transactionDate = (TextView) itemView.findViewById(R.id.rowDate);
        this.transactionAmount = (TextView) itemView.findViewById(R.id.rowAmount);
    }
    public void setTransactionTitle(String title){
        Log.v(BaseActivity.TAG, "title\t'" + title + "'\tset for ViewHolder");
        transactionTitle.setText(title);
    }
    public void setTransactionDate(Long date){
        transactionDate.setText(new SimpleDateFormat("dd-MM-yyyy", Locale.UK).format(new Date(date)));
    }
    public void setTransactionAmount(Float amount){
        transactionAmount.setText(Float.toString(amount));
    }

}

package ie.wit.application.service.data;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

import ie.wit.application.exceptions.NoUserLoggedInException;
import ie.wit.application.main.FinanceApp;
import ie.wit.application.model.Transaction;
import static ie.wit.application.activities.BaseActivity.TAG;


/**
 * Abstract the details of the firebase database from the rest of the application
 */
public class TransactionDataService
{
    private final DatabaseReference DATABASE_REF;
    private final String COLLECTION_NAME = "transactions";

    private String currentUserId;
    private List<Transaction> transactions;
    private ValueEventListener valueEventListener;
    private DatabaseReference transactionReference;

    /**
     * Instantiates a new Transaction data service.
     */
    public TransactionDataService()
    {
        DATABASE_REF = FirebaseDatabase.getInstance().getReference();
        transactions = new ArrayList<>();
    }

    /**
     * Start.
     *
     * @throws NoUserLoggedInException the no user logged in exception
     */
    public void start() throws NoUserLoggedInException
    {
        currentUserId = FinanceApp.getCurrentUserId();
        if (currentUserId == null) {
            throw new NoUserLoggedInException("Attempted to attach listener to transaction collection, but there is no user logged in");
        }
        if (valueEventListener == null) {
            valueEventListener = setupValueEventListener();
        }
        if (transactionReference == null) {
            transactionReference = DATABASE_REF.child(currentUserId).child(COLLECTION_NAME);
        }
        transactionReference.addValueEventListener(valueEventListener);
    }

    /**
     * Stop.
     */
    public void stop(){
        transactionReference.removeEventListener(valueEventListener);
    }

    /**
     * Get transactions list.
     *
     * @return the list
     */
    public List<Transaction> getTransactions(){
        if (transactions == null) {
            transactions = new ArrayList<>();
        }
        return transactions;
    }

    /**
     * Add transaction.
     *
     * @param transaction the transaction
     */
    public void addTransaction(Transaction transaction){
        transactionReference.child(transaction.getTimestamp().toString()).setValue(transaction);
    }
    @Contract(" -> !null")
    private ValueEventListener setupValueEventListener(){
        return new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Log.d(TAG, "onDataChange: Transaction");
                for (DataSnapshot transactionSnapshot: dataSnapshot.getChildren()){
                    Transaction transaction = transactionSnapshot.getValue(Transaction.class);
                    transactions.add(transaction);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.e(TAG, "onCancelled: Transactions", databaseError.toException());
            }
        };
    }

}

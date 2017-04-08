package ie.wit.application.service.data;

import android.util.Log;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Consumer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.stream.DoubleStream;


import ie.wit.application.exceptions.NoUserLoggedInException;
import ie.wit.application.main.FinanceApp;
import ie.wit.application.model.Transaction;
import ie.wit.application.model.ui_model.Balance;
import ie.wit.application.model.ui_model.BalanceObserver;

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
    private Balance currentBalance;
    private Consumer<String> updateTransactionListCallback;

    /**
     * Instantiates a new Transaction data service.
     */
    public TransactionDataService()
    {
        DATABASE_REF = FirebaseDatabase.getInstance().getReference();
        transactions = new ArrayList<>();
        currentBalance = new Balance();
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
    public void stop()
    {
        transactionReference.removeEventListener(valueEventListener);
    }

    /**
     * Get transactions list.
     *
     * @return the list
     */
    public List<Transaction> getTransactions()
    {
        if (transactions == null) {
            transactions = new ArrayList<>();
        }
        return transactions;
    }
    public List<Transaction> getIncomes(){
        return Stream.of(transactions).filter(Transaction::isIncome).collect(Collectors.toList());
    }
    public List<Transaction> getExpenditures(){
        return Stream.of(transactions).filter(transaction -> !transaction.isIncome()).collect(Collectors.toList());
    }

    public void registerBalanceObserver(BalanceObserver observer)
    {
        observer.observe(currentBalance);
    }

    public void registerTransactionCallback(Consumer<String> callback){
        this.updateTransactionListCallback = callback;
    }
    /**
     * Add transaction.
     *
     * @param transaction the transaction
     */
    public void addTransaction(Transaction transaction)
    {
        transactionReference.child(transaction.getTimestamp().toString()).setValue(transaction);
    }

    private float getBalance()
    {
        float tmpBalance = 0;
        for (Transaction transaction : transactions) {
            float tmpAmount = transaction.getAmount();
            if (!transaction.isIncome()) {
                tmpAmount = tmpAmount * -1;
            }
            tmpBalance += tmpAmount;
        }
        return tmpBalance;
    }

    @Contract(" -> !null")
    private ValueEventListener setupValueEventListener()
    {
        return new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                transactions.clear();
                Log.d(TAG, "onDataChange: Transaction");
                for (DataSnapshot transactionSnapshot : dataSnapshot.getChildren()) {
                    Transaction transaction = transactionSnapshot.getValue(Transaction.class);
                    transactions.add(transaction);
                }
                float tmpBalance = getBalance();
                currentBalance.setCurrentBalance(tmpBalance);
                if (updateTransactionListCallback != null) {
                    updateTransactionListCallback.accept("data changed");
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

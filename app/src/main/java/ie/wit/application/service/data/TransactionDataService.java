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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ie.wit.application.exceptions.NoUserLoggedInException;
import ie.wit.application.main.FinanceApp;
import ie.wit.application.model.Transaction;
import ie.wit.application.model.ui.Balance;
import ie.wit.application.model.ui.BalanceObserver;

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

    /**
     * Gets incomes.
     *
     * @return the incomes
     */
    public List<Transaction> getIncomes()
    {
        return Stream.of(transactions).filter(Transaction::isIncome).collect(Collectors.toList());
    }

    /**
     * Gets expenditures.
     *
     * @return the expenditures
     */
    public List<Transaction> getExpenditures()
    {
        return Stream.of(transactions).filter(transaction -> !transaction.isIncome()).collect(Collectors.toList());
    }

    public List<Transaction> getAllPending()
    {
        return Stream.of(transactions).filter(this::isPending).collect(Collectors.toList());
    }

    public List<Transaction> getAllCompleted()
    {
        return Stream.of(transactions).filter((transaction) -> !isPending(transaction)).collect(Collectors.toList());
    }

    public List<Transaction> getIncomePending()
    {
        return Stream.of(getIncomes()).filter(this::isPending).collect(Collectors.toList());
    }

    public List<Transaction> getIncomeCompleted()
    {
        return Stream.of(getIncomes()).filter((transaction) -> !isPending(transaction)).collect(Collectors.toList());
    }

    public List<Transaction> getExpenditurePending()
    {
        return Stream.of(getExpenditures()).filter(this::isPending).collect(Collectors.toList());
    }

    public List<Transaction> getExpenditureCompleted()
    {
        return Stream.of(getExpenditures()).filter((transaction) -> !isPending(transaction)).collect(Collectors.toList());
    }


    private boolean isPending(Transaction transaction)
    {
        return FinanceApp.serviceFactory.getUtil().checkTimestampPending(transaction.getDueDate());
    }

    /**
     * Register balance observer.
     *
     * @param observer the observer
     */
    public void registerBalanceObserver(BalanceObserver observer)
    {
        observer.observe(currentBalance);
    }
    public void registerBalanceObservers(BalanceObserver[] observers){
        for (BalanceObserver observer : observers) {
            observer.observe(currentBalance);
        }
    }

    /**
     * Register transaction callback.
     *
     * @param callback the callback
     */
    public void registerTransactionCallback(Consumer<String> callback)
    {
        this.updateTransactionListCallback = callback;
    }

    /**
     * Add transaction.
     *
     * @param transaction the transaction
     */
    public void addTransaction(Transaction transaction)
    {
        if (transaction.getFirebaseId() == null) {
            transactionReference.push().setValue(transaction);
            return;
        }
        transactionReference.child(transaction.getFirebaseId()).setValue(transaction);
    }

    /**
     * Remove transaction.
     *
     * @param timestamp the timestamp
     */
    public void removeTransaction(Long timestamp)
    {
        transactionReference.child(timestamp.toString()).setValue(null);
    }
    public void removeTransaction(String id){
        transactionReference.child(id).setValue(null);
    }


    private float getTotalIncome()
    {
        return getTotal(getIncomes());
    }

    private float getTotalExpenditure()
    {
        return getTotal(getExpenditures());
    }
    private float getPendingIncomeTotal(){
        return getTotal(getIncomePending());
    }
    private float getPendingExpenditureTotal(){
        return getTotal(getExpenditurePending());
    }
    private float getCompletedExpenditureTotal(){
        return getTotal(getExpenditureCompleted());
    }
    private float getCompletedIncomeTotal(){
        return getTotal(getIncomeCompleted());
    }
    private Map<String, Float> getTotals(){
        Map<String, Float> totals = new HashMap<>();
        totals.put("pendingIncome", getPendingIncomeTotal());
        totals.put("completedIncome", getCompletedIncomeTotal());
        totals.put("pendingExpenditure", getPendingExpenditureTotal());
        totals.put("completedExpenditure", getCompletedExpenditureTotal());
        return totals;
    }
    private float getTotal(List<Transaction> transactions){
        float tmpTotal = 0;
        for (Transaction transaction : transactions) {
            tmpTotal += transaction.getAmount();
        }
        return tmpTotal;
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
                    transaction.setFirebaseId(transactionSnapshot.getKey());
                    transactions.add(transaction);
                }
                //currentBalance.setAll(getTotalIncome(), getTotalExpenditure());// this will update the totals and trigger relevant view updates
                currentBalance.setAll(getTotals());
                if (updateTransactionListCallback != null) {
                    updateTransactionListCallback.accept("data changed"); //this will let the dashboard know that the data has been changed so it can refresh the list
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

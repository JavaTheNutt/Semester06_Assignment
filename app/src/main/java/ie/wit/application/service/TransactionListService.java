package ie.wit.application.service;

import com.annimon.stream.Collectors;

import java.util.Collections;
import java.util.List;

import ie.wit.application.model.Transaction;
import ie.wit.application.service.enums.TransactionSortType;

/**
 * Created by joewe on 14/04/2017.
 */

public class TransactionListService
{
    public List<Transaction> sortAscending(List<Transaction> transactions, TransactionSortType type){
        if (type == TransactionSortType.DUE){
            return sortDue(transactions, true);
        }
        return sortEntered(transactions, true);
    }
    public List<Transaction> sortDescending(List<Transaction> transactions, TransactionSortType type){
        if (type == TransactionSortType.DUE){
            return sortDue(transactions, false);
        }
        return sortDue(transactions, false);
    }
    private List<Transaction> sortEntered(List<Transaction> transactions, boolean isAscending){
        if (isAscending){
            Collections.sort(transactions, Transaction.transactionEnteredAsc);
            return transactions;
        }
        Collections.sort(transactions, Transaction.transactionEnteredDesc);
        return transactions;
    }
    private List<Transaction> sortDue(List<Transaction> transactions, boolean isAscending){
        if (isAscending){
            Collections.sort(transactions, Transaction.transactionDueAsc);
            return transactions;
        }
        Collections.sort(transactions, Transaction.transactionDueDesc);
        return transactions;
    }
}


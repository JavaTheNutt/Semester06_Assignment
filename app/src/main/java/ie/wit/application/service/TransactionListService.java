package ie.wit.application.service;

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
        return sort(transactions, type, true);
    }
    public List<Transaction> sortDescending(List<Transaction> transactions, TransactionSortType type){
        return sort(transactions,type, false);
    }
    private List<Transaction> sort(List<Transaction> transactions,TransactionSortType type, boolean isAscending){
        switch (type){
            case TITLE:
                sortTitle(transactions, isAscending);
                break;
            case AMOUNT:
                sortAmount(transactions, isAscending);
                break;
            case DUE:
                sortDue(transactions, isAscending);
                break;
            case ENTERED:
                sortEntered(transactions, isAscending);
                break;
            default:
                sortEntered(transactions, isAscending);
                break;
        }
        return transactions;
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
    private List<Transaction> sortTitle(List<Transaction> transactions, boolean isAscending){
        if(isAscending){
            Collections.sort(transactions, Transaction.transactionTitleAsc);
            return transactions;
        }
        Collections.sort(transactions, Transaction.transactionTitleDesc);
        return transactions;
    }
    private List<Transaction> sortAmount(List<Transaction> transactions, boolean isAscending){
        if(isAscending){
            Collections.sort(transactions, Transaction.transactionAmountAsc);
            return transactions;
        }
        Collections.sort(transactions, Transaction.transactionAmountDesc);
        return transactions;
    }
}


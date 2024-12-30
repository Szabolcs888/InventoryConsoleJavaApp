package com.myinventoryapp.ui.menu;

import com.myinventoryapp.repository.SalesTransactionRepository;
import com.myinventoryapp.entities.SalesTransaction;
import com.myinventoryapp.util.displayhelpers.TransactionDisplayHelper;

import java.util.List;

public class MenuOption5DisplayTransactions {

    public void displayTransactionList(String text) {
        List<SalesTransaction> transactionList = SalesTransactionRepository.getSalesTransactionList();
        TransactionDisplayHelper.displayTransactionList(transactionList, text);
    }
}
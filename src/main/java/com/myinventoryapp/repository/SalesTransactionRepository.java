package com.myinventoryapp.repository;

import com.myinventoryapp.entities.SalesTransaction;

import java.util.ArrayList;
import java.util.List;

public final class SalesTransactionRepository {
    private static final List<SalesTransaction> transactionList = new ArrayList<>();

    private SalesTransactionRepository() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static List<SalesTransaction> getSalesTransactionList() {
        return new ArrayList<>(transactionList);
    }

    public static void addSalesTransaction(SalesTransaction salesTransaction) {
        transactionList.add(salesTransaction);
    }

    public static void clearSalesTransactionList() {
        transactionList.clear();
    }
}
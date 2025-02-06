package com.myinventoryapp.repository;

import com.myinventoryapp.entities.SalesTransaction;

import java.util.ArrayList;
import java.util.List;

public final class SalesTransactionRepository {
    private static final List<SalesTransaction> TRANSACTION_LIST = new ArrayList<>();

    private SalesTransactionRepository() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static List<SalesTransaction> getSalesTransactionList() {
        return new ArrayList<>(TRANSACTION_LIST);
    }

    public static void addSalesTransaction(SalesTransaction salesTransaction) {
        TRANSACTION_LIST.add(salesTransaction);
    }

    public static void clearSalesTransactionList() {
        TRANSACTION_LIST.clear();
    }
}
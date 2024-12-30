package com.myinventoryapp.services;

import com.myinventoryapp.repository.SalesTransactionRepository;
import com.myinventoryapp.entities.Product;
import com.myinventoryapp.entities.SalesTransaction;
import com.myinventoryapp.util.DateUtils;
import com.myinventoryapp.util.IdUtils;
import com.myinventoryapp.util.displayhelpers.TransactionDisplayHelper;

public class TransactionService {

    public void transactionRegistration(
            boolean isRegisteredCustomer, String customerName, String customerId, Product product, int quantitySold) {
        String productName = product.getProductName();
        int unitPrice = product.getUnitPrice();
        String transactionId = "trId" + IdUtils.generateId();
        String transactionDate = DateUtils.getCurrentFormattedDate();
        SalesTransaction salesTransaction = new SalesTransaction(
                transactionId, customerName, customerId, productName, quantitySold, unitPrice, transactionDate);
        System.out.println();
        TransactionDisplayHelper.displayTransactionInfo(
                product, quantitySold, customerName, customerId, isRegisteredCustomer, transactionDate);
        SalesTransactionRepository.addSalesTransaction(salesTransaction);
    }
}
package com.myinventoryapp.entities;

import java.util.Objects;

public class SalesTransaction {
    private final String transactionId;
    private final String customerName;
    private final String customerId;
    private final String productName;
    private final int quantitySold;
    private final int unitPrice;
    private final String transactionDate;

    public SalesTransaction(
            String transactionId, String customerName, String customerId, String productName,
            int quantitySold, int unitPrice, String transactionDate) {
        this.transactionId = transactionId;
        this.customerName = customerName;
        this.customerId = customerId;
        this.productName = productName;
        this.quantitySold = quantitySold;
        this.unitPrice = unitPrice;
        this.transactionDate = transactionDate;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SalesTransaction salesTransaction = (SalesTransaction) o;
        return Objects.equals(transactionId, salesTransaction.transactionId) &&
                Objects.equals(customerName, salesTransaction.customerName) &&
                Objects.equals(customerId, salesTransaction.customerId) &&
                Objects.equals(productName, salesTransaction.productName) &&
                quantitySold == salesTransaction.quantitySold &&
                unitPrice == salesTransaction.unitPrice &&
                Objects.equals(transactionDate, salesTransaction.transactionDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                transactionId, customerName, customerId, productName, quantitySold, unitPrice, transactionDate);
    }
}
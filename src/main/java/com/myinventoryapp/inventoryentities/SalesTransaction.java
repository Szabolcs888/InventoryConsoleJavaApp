package com.myinventoryapp.inventoryentities;

public class SalesTransaction {
    private String transactionId;
    private String customerName;
    private String customerId;
    private String productName;
    private int quantitySold;
    private int unitPrice;
    private String transactionDate;

    public SalesTransaction(String transactionId, String customerName, String customerId, String productName, int quantitySold, int unitPrice, String transactionDate) {
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
}

package com.myinventoryapp.util.displayhelpers;

import com.myinventoryapp.entities.Product;
import com.myinventoryapp.entities.SalesTransaction;

import java.util.List;

import static com.myinventoryapp.util.Colors.*;

public final class TransactionDisplayHelper {

    private TransactionDisplayHelper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static void displayTransactionList(List<SalesTransaction> transactionList, String text) {
        System.out.println(text);
        if (transactionList.isEmpty()) {
            System.out.println("There are currently no transactions in the inventory!");
        } else {
            System.out.println(GREEN.getColorCode() + "There are a total of " + transactionList.size() +
                    " transactions in the inventory:" + RESET.getColorCode());
            for (SalesTransaction transaction : transactionList) {
                System.out.println(transaction.getTransactionId() +
                        ", Date: " + transaction.getTransactionDate() +
                        ", Product: " + transaction.getProductName() +
                        ", Quantity: " + transaction.getQuantitySold() +
                        ", Amount paid: " + transaction.getQuantitySold() +
                        " * " + transaction.getUnitPrice() +
                        " = " + (transaction.getQuantitySold() * transaction.getUnitPrice()) +
                        " HUF, Customer: " + transaction.getCustomerName() +
                        " (" + transaction.getCustomerId() + ")");
            }
        }
        System.out.println();
    }

    public static void displayTransactionInfo(
            Product product, int quantitySold, String customerName,
            String customerId, boolean isRegisteredCustomer, String transactionDate) {
        System.out.println(GREEN.getColorCode() + "TRANSACTION DETAILS: " + RESET.getColorCode() +
                "\nProduct name: " + product.getProductName() + " (" + product.getProductId() + ")");
        if (isRegisteredCustomer) {
            System.out.println("Customer name: " + customerName + " (" + customerId + " / returning customer)");
        } else {
            System.out.println("Customer name: " + customerName + " (" + customerId + " / newly registered)");
        }
        System.out.println("Quantity sold: " + quantitySold +
                "\nAmount paid: " + quantitySold +
                " * " + product.getUnitPrice() +
                " = " + product.getUnitPrice() * quantitySold +
                " HUF" +
                "\nTransaction date: " + transactionDate);
    }
}
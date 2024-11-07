package iventory.util.displayHelpers;

import iventory.dataStorage.ProductRepository;
import iventory.iventoryEntities.SalesTransaction;

import java.util.List;

import static iventory.util.Colors.*;

public class TransactionDisplayHelper {

    public static void displayTransactionList(List<SalesTransaction> transactionList, String text) {
        System.out.println(text);
        if (transactionList.isEmpty()) {
            System.out.println("There are currently no transactions in the inventory!");
        } else {
            System.out.println(GREEN.getColorCode() + "There are a total of " + transactionList.size() +
                    " transactions in the inventory:" + RESET.getColorCode());
            for (SalesTransaction item : transactionList) {
                System.out.println(item.getTransactionId() + ", Date: " + item.getTransactionDate() + ", Product: " + item.getProductName() +
                        ", Quantity: " + item.getQuantitySold() + ", Amount paid: " + item.getQuantitySold() + " * " + item.getUnitPrice() +
                        " = " + (item.getQuantitySold() * item.getUnitPrice()) + " HUF, Customer: " +
                        item.getCustomerName() + " (" + item.getCustomerId() + ")");
            }
        }
        System.out.println();
    }

    public static void displayTransactionInfo(
            int productIndex, String productName, int quantitySold, String customerName, String customerId, boolean isRegisteredCustomer, String transactionDate) {
        System.out.println(GREEN.getColorCode() + "TRANSACTION DETAILS: " + RESET.getColorCode() +
                "\nProduct name: " + productName + " (" + ProductRepository.productList.get(productIndex).getProductId() + ")");
        if (isRegisteredCustomer)
            System.out.println("\"Customer name: " + customerName + " (" + customerId + " / returning customer)");
        else
            System.out.println("Customer name: " + customerName + " (" + customerId + " / newly registered)");
        System.out.println("Quantity sold: " + quantitySold +
                "\nAmount paid: " + quantitySold + " * " + ProductRepository.productList.get(productIndex).unitPrice +
                " = " + ProductRepository.productList.get(productIndex).unitPrice * quantitySold + " HUF" +
                "\nTransaction date: " + transactionDate);
    }
}

package com.myinventoryapp.dataio;

import com.myinventoryapp.repository.CustomerRepository;
import com.myinventoryapp.repository.SalesTransactionRepository;
import com.myinventoryapp.entities.Customer;
import com.myinventoryapp.entities.Product;
import com.myinventoryapp.util.Colors;
import com.myinventoryapp.util.FileUtils;
import com.myinventoryapp.repository.ProductRepository;
import com.myinventoryapp.entities.SalesTransaction;

import java.util.List;

public class DataLoader {

    public void loadAllData() {
        String productFilePath = FilePaths.getProductsFilePath();
        String customersFilePath = FilePaths.getCustomersFilePath();
        String transactionsFilePath = FilePaths.getTransactionsFilePath();

        loadProductsFromFile(productFilePath);
        loadCustomersFromFile(customersFilePath);
        loadTransactionsFromFile(transactionsFilePath);
    }

    void loadProductsFromFile(String productFilePath) {
        System.out.print("\nChecking the database of products: ");
        try {
            List<String> productListFromFile = FileUtils.readFromFile(productFilePath);
            for (String line : productListFromFile) {
                String[] lineData = line.split(",");
                String productName = lineData[0];
                String productId = lineData[1];
                int unitPrice = Integer.parseInt(lineData[2]);
                int quantity = Integer.parseInt(lineData[3]);
                Product product = new Product(productName, productId, unitPrice, quantity);
                ProductRepository.addProduct(product);
            }
        } catch (Exception e) {
            System.out.println(Colors.RED.getColorCode() + " (Error loading products from file: "
                    + e.getMessage() + ")" + Colors.RESET.getColorCode());
        }
    }

    void loadCustomersFromFile(String customersFilePath) {
        System.out.print("Checking the database of customers: ");
        try {
            List<String> customerListFromFile = FileUtils.readFromFile(customersFilePath);
            for (String line : customerListFromFile) {
                String[] rowData = line.split(",");
                String customerName = rowData[0];
                int totalPurchases = Integer.parseInt(rowData[1]);
                String customerID = rowData[2];
                Customer customer = new Customer(customerName, customerID, totalPurchases);
                CustomerRepository.addCustomer(customer);
            }
        } catch (Exception e) {
            System.out.println(Colors.RED.getColorCode() + " (Error loading customers from file: "
                    + e.getMessage() + ")" + Colors.RESET.getColorCode());
        }
    }

    void loadTransactionsFromFile(String transactionsFilePath) {
        System.out.print("Checking the database of transactions: ");
        try {
            List<String> transactionListFromFile = FileUtils.readFromFile(transactionsFilePath);
            for (String line : transactionListFromFile) {
                String[] rowData = line.split(",");
                String transactionId = rowData[0];
                String transactionDate = rowData[1];
                String productName = rowData[2];
                int quantitySold = Integer.parseInt(rowData[3]);
                int unitPrice = Integer.parseInt(rowData[4]);
                String customerName = rowData[5];
                String customerId = rowData[6];
                SalesTransaction salesTransaction = new SalesTransaction(
                        transactionId, customerName, customerId, productName, quantitySold, unitPrice, transactionDate);
                SalesTransactionRepository.addSalesTransaction(salesTransaction);
            }
        } catch (Exception e) {
            System.out.println(Colors.RED.getColorCode() + " (Error loading transactions from file: "
                    + e.getMessage() + ")" + Colors.RESET.getColorCode());
        }
    }
}
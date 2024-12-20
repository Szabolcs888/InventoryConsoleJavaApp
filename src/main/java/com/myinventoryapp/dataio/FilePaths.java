package com.myinventoryapp.dataio;

public class FilePaths {
    private static final String PRODUCTS_FILE = "src/main/resources/inventorydata/productList.txt";
    private static final String CUSTOMERS_FILE = "src/main/resources/inventorydata/customerList.txt";
    private static final String TRANSACTIONS_FILE = "src/main/resources/inventorydata/transactionList.txt";

    public static String getProductsFilePath() {
        return PRODUCTS_FILE;
    }

    public static String getCustomersFilePath() {
        return CUSTOMERS_FILE;
    }

    public static String getTransactionsFilePath() {
        return TRANSACTIONS_FILE;
    }
}

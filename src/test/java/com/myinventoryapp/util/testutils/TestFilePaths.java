package com.myinventoryapp.util.testutils;

import java.nio.file.Paths;

public final class TestFilePaths {
    public static final String TEST_PRODUCTS_FILE = "src/test/resources/inventorytestdata/testProductList.txt";
    public static final String TEST_CUSTOMERS_FILE = "src/test/resources/inventorytestdata/testCustomerList.txt";
    public static final String TEST_TRANSACTIONS_FILE = "src/test/resources/inventorytestdata/testTransactionList.txt";

    private TestFilePaths() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static String getTestProductsFilePath() {
        return Paths.get(TEST_PRODUCTS_FILE).toString();
    }

    public static String getTestCustomersFilePath() {
        return Paths.get(TEST_CUSTOMERS_FILE).toString();
    }

    public static String getTestTransactionsFilePath() {
        return Paths.get(TEST_TRANSACTIONS_FILE).toString();
    }
}
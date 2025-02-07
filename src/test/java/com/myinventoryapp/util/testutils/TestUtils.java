package com.myinventoryapp.util.testutils;

import com.myinventoryapp.repository.CustomerRepository;
import com.myinventoryapp.repository.ProductRepository;
import com.myinventoryapp.repository.SalesTransactionRepository;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public final class TestUtils {

    private TestUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static ByteArrayOutputStream redirectSystemOut() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream, true, StandardCharsets.UTF_8));
        return outputStream;
    }

    public static void restoreSystemOut() {
        System.setOut(System.out);
    }

    public static void reset() {
        ProductRepository.clearProductList();
        CustomerRepository.clearCustomerList();
        SalesTransactionRepository.clearSalesTransactionList();
    }
}
package com.myinventoryapp.util.testutils;

import com.myinventoryapp.repository.CustomerRepository;
import com.myinventoryapp.repository.ProductRepository;
import com.myinventoryapp.repository.SalesTransactionRepository;

public final class TestUtils {

    private TestUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static void reset() {
        ProductRepository.clearProductList();
        CustomerRepository.clearCustomerList();
        SalesTransactionRepository.clearSalesTransactionList();
    }
}
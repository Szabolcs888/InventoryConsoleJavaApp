package com.myinventoryapp.util.testutils;

import com.myinventoryapp.repository.CustomerRepository;
import com.myinventoryapp.repository.ProductRepository;
import com.myinventoryapp.repository.SalesTransactionRepository;

public class TestUtils {

    public static void reset() {
        ProductRepository.clearProductList();
        CustomerRepository.clearCustomerList();
        SalesTransactionRepository.clearSalesTransactionList();
    }
}
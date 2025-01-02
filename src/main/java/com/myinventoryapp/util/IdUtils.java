package com.myinventoryapp.util;

import com.myinventoryapp.entities.Product;
import com.myinventoryapp.repository.CustomerRepository;
import com.myinventoryapp.repository.ProductRepository;
import com.myinventoryapp.repository.SalesTransactionRepository;
import com.myinventoryapp.entities.Customer;
import com.myinventoryapp.entities.SalesTransaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class IdUtils {

    private IdUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static int generateId() {
        Random random = new Random();
        int id;
        do {
            id = random.nextInt(1_000_000, 9_999_999) + 1;
        } while (isIdExisting(id));
        return id;
    }

    private static boolean isIdExisting(int id) {
        List<Integer> alreadyExistingIds = readExistingIdsFromLists();
        return alreadyExistingIds.contains(id);
    }

    private static List<Integer> readExistingIdsFromLists() {
        List<Integer> alreadyExistingIds = new ArrayList<>();

        List<Product> productList = ProductRepository.getProductList();
        for (Product product : productList) {
            alreadyExistingIds.add(Integer.parseInt(product.getProductId().split("r")[1]));
        }

        List<Customer> customerList = CustomerRepository.getCustomerList();
        for (Customer customer : customerList) {
            alreadyExistingIds.add(Integer.parseInt(customer.getCustomerId().split("D")[1]));
        }

        List<SalesTransaction> transactionList = SalesTransactionRepository.getSalesTransactionList();
        for (SalesTransaction transaction : transactionList) {
            alreadyExistingIds.add(Integer.parseInt(transaction.getTransactionId().split("d")[1]));
        }
        return alreadyExistingIds;
    }
}
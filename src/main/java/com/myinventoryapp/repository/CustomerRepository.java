package com.myinventoryapp.repository;

import com.myinventoryapp.entities.Customer;

import java.util.ArrayList;
import java.util.List;

public final class CustomerRepository {
    private static final List<Customer> CUSTOMER_LIST = new ArrayList<>();

    private CustomerRepository() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static List<Customer> getCustomerList() {
        return new ArrayList<>(CUSTOMER_LIST);
    }

    public static void addCustomer(Customer customer) {
        CUSTOMER_LIST.add(customer);
    }

    public static Customer findCustomerByName(String customerName) {
        Customer foundCustomerByName = null;
        for (Customer customer : CUSTOMER_LIST) {
            if (customer.getCustomerName().equalsIgnoreCase(customerName)) {
                foundCustomerByName = customer;
                break;
            }
        }
        return foundCustomerByName;
    }

    public static void clearCustomerList() {
        CUSTOMER_LIST.clear();
    }
}
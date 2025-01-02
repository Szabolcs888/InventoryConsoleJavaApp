package com.myinventoryapp.repository;

import com.myinventoryapp.entities.Customer;

import java.util.ArrayList;
import java.util.List;

public final class CustomerRepository {
    private static final List<Customer> customerList = new ArrayList<>();

    private CustomerRepository() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static List<Customer> getCustomerList() {
        return new ArrayList<>(customerList);
    }

    public static void addCustomer(Customer customer) {
        customerList.add(customer);
    }

    public static Customer findCustomerByName(String customerName) {
        Customer foundCustomerByName = null;
        for (Customer customer : customerList) {
            if (customer.getCustomerName().equalsIgnoreCase(customerName)) {
                foundCustomerByName = customer;
                break;
            }
        }
        return foundCustomerByName;
    }

    public static void clearCustomerList() {
        customerList.clear();
    }
}
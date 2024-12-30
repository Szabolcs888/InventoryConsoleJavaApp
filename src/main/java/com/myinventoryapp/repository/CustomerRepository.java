package com.myinventoryapp.repository;

import com.myinventoryapp.entities.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerRepository {
    private static List<Customer> customerList = new ArrayList<>();

    public static List<Customer> getCustomerList() {
        return customerList;
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
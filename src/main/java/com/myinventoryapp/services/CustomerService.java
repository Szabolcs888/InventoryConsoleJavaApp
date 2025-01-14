package com.myinventoryapp.services;

import com.myinventoryapp.repository.CustomerRepository;
import com.myinventoryapp.entities.Customer;
import com.myinventoryapp.entities.Product;
import com.myinventoryapp.util.IdUtils;

public class CustomerService {

    public String handleCustomerTransaction(
            boolean isRegisteredCustomer, String customerName, Product product, int quantitySold) {
        if (isRegisteredCustomer) {
            return updateRegisteredCustomerPurchases(customerName, product, quantitySold);
        } else {
            return registerNewCustomer(customerName, product, quantitySold);
        }
    }

    String updateRegisteredCustomerPurchases(String customerName, Product product, int quantitySold) {
        Customer customer = CustomerRepository.findCustomerByName(customerName);
        int currentPurchase = product.getUnitPrice() * quantitySold;
        customer.setTotalPurchases(customer.getTotalPurchases() + currentPurchase);
        return customer.getCustomerId();
    }

    String registerNewCustomer(String customerName, Product product, int quantitySold) {
        String customerId = "cID" + IdUtils.generateId();
        int totalPurchases = product.getUnitPrice() * quantitySold;
        Customer newCustomer = new Customer(customerName, customerId, totalPurchases);
        CustomerRepository.addCustomer(newCustomer);
        return customerId;
    }
}
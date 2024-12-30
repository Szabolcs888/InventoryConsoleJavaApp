package com.myinventoryapp.util.displayhelpers;

import com.myinventoryapp.repository.CustomerRepository;
import com.myinventoryapp.entities.Customer;

import java.util.List;

import static com.myinventoryapp.util.Colors.*;

public class CustomerDisplayHelper {

    public static void displayCustomerList(List<Customer> customerList, String text) {
        System.out.println(text);
        if (CustomerRepository.getCustomerList().isEmpty()) {
            System.out.println("There are currently no registered customers in the inventory!");
        } else {
            System.out.println(GREEN.getColorCode() + "There are a total of " + CustomerRepository.getCustomerList().size() +
                    " registered customers in the inventory:" + RESET.getColorCode());
            for (Customer customer : customerList) {
                System.out.println("Name: " + customer.getCustomerName() + " (" + customer.getCustomerId() +
                        "), Total purchases: " + customer.getTotalPurchases() + " HUF");
            }
        }
        System.out.println();
    }
}
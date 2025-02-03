package com.myinventoryapp.util.displayhelpers;

import com.myinventoryapp.entities.Customer;

import java.util.List;

import static com.myinventoryapp.util.Colors.*;

public final class CustomerDisplayHelper {

    private CustomerDisplayHelper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static void displayCustomerList(List<Customer> customerList, String menuMessage) {
        System.out.println(menuMessage);
        if (customerList.isEmpty()) {
            System.out.println("There are currently no registered customers in the inventory!");
        } else {
            System.out.println(GREEN.getColorCode() + "There are a total of " + customerList.size() +
                    " registered customers in the inventory:" + RESET.getColorCode());
            for (Customer customer : customerList) {
                System.out.println("Name: " + customer.getCustomerName() + " (" + customer.getCustomerId() +
                        "), Total purchases: " + customer.getTotalPurchases() + " HUF");
            }
        }
        System.out.println();
    }
}
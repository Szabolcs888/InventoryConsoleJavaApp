package com.myinventoryapp.ui.menu;

import com.myinventoryapp.util.displayhelpers.CustomerDisplayHelper;
import com.myinventoryapp.repository.CustomerRepository;
import com.myinventoryapp.entities.Customer;

import java.util.List;

public class MenuOption4DisplayCustomers {

    public void displayCustomerList(String menuMessage) {
        List<Customer> customerList = CustomerRepository.getCustomerList();
        CustomerDisplayHelper.displayCustomerList(customerList, menuMessage);
    }
}
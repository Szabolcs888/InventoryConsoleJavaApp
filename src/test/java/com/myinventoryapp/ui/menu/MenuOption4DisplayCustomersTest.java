package com.myinventoryapp.ui.menu;

import com.myinventoryapp.entities.Customer;
import com.myinventoryapp.repository.CustomerRepository;
import com.myinventoryapp.util.displayhelpers.CustomerDisplayHelper;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

class MenuOption4DisplayCustomersTest {

    @Test
    void testDisplayCustomerList_CallsRepositoryAndHelper() {
        String menuMessage = "\n-DISPLAY CUSTOMERS MENU-\n";
        List<Customer> customerList = Arrays.asList(
                new Customer("Egerszegi  Krisztina", "cID5794138", 1356),
                new Customer("Darnyi Tamás", "cID9742746", 560),
                new Customer("Thomas Mann", "cID2633111", 5400)
        );

        try (MockedStatic<CustomerRepository> mockedCustomerRepository = Mockito.mockStatic(CustomerRepository.class);
             MockedStatic<CustomerDisplayHelper> mockedCustomerDisplayHelper = Mockito.mockStatic(CustomerDisplayHelper.class)) {

            mockedCustomerRepository.when(CustomerRepository::getCustomerList).thenReturn(customerList);
            mockedCustomerDisplayHelper.when(() -> CustomerDisplayHelper.displayCustomerList(anyList(), anyString())).
                    thenAnswer(invocation -> null);

            MenuOption4DisplayCustomers menuOption4DisplayCustomers = new MenuOption4DisplayCustomers();
            menuOption4DisplayCustomers.displayCustomerList(menuMessage);

            mockedCustomerRepository.verify(CustomerRepository::getCustomerList, times(1));
            mockedCustomerDisplayHelper.verify(() -> CustomerDisplayHelper.displayCustomerList(
                    customerList, menuMessage), times(1));
        }
    }

    @Test
    void testDisplayCustomerList_CallsRepositoryAndHelper_EmptyList() {
        String menuMessage = "\n-DISPLAY CUSTOMERS MENU-\n";

        try (MockedStatic<CustomerRepository> mockedCustomerRepository = Mockito.mockStatic(CustomerRepository.class);
             MockedStatic<CustomerDisplayHelper> mockedCustomerDisplayHelper = Mockito.mockStatic(CustomerDisplayHelper.class)) {

            mockedCustomerRepository.when(CustomerRepository::getCustomerList).thenReturn(Collections.emptyList());
            mockedCustomerDisplayHelper.when(() -> CustomerDisplayHelper.displayCustomerList(anyList(), anyString())).
                    thenAnswer(invocation -> null);

            MenuOption4DisplayCustomers menuOption4DisplayCustomers = new MenuOption4DisplayCustomers();
            menuOption4DisplayCustomers.displayCustomerList(menuMessage);

            mockedCustomerRepository.verify(CustomerRepository::getCustomerList, times(1));
            mockedCustomerDisplayHelper.verify(() -> CustomerDisplayHelper.displayCustomerList(
                    Collections.emptyList(), menuMessage), times(1));
        }
    }
}
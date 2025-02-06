package com.myinventoryapp.util.displayhelpers;

import com.myinventoryapp.entities.Customer;
import com.myinventoryapp.util.testutils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerDisplayHelperTest {
    private final static String MENU_MESSAGE = "\n-DISPLAY CUSTOMERS MENU-\n";

    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        outputStream = TestUtils.redirectSystemOut();
    }

    @AfterEach
    void tearDown() {
        TestUtils.restoreSystemOut();
    }

    @Test
    void testDisplayCustomerList_PrintsMenuMessage() {
        List<Customer> customerList = Collections.emptyList();

        CustomerDisplayHelper.displayCustomerList(customerList, MENU_MESSAGE);

        String expectedMessage = "-DISPLAY CUSTOMERS MENU-";
        String output = outputStream.toString();
        assertTrue(output.contains(expectedMessage),
                "Expected message '" + expectedMessage + "' was not found in the output.");
    }

    @Test
    void testDisplayCustomerList_PrintsEmptyListMessage() {
        List<Customer> customerList = Collections.emptyList();

        CustomerDisplayHelper.displayCustomerList(customerList, MENU_MESSAGE);

        String expectedMessage = "There are currently no registered customers in the inventory!";
        String output = outputStream.toString();
        assertTrue(output.contains(expectedMessage),
                "Expected message '" + expectedMessage + "' was not found in the output.");
    }

    @Test
    void testDisplayCustomerList_PrintsNotEmptyListMessages() {
        List<Customer> customerList = Arrays.asList(
                new Customer("Töröcsik András", "cID2422151", 560),
                new Customer("Freddie Mercury", "cID6925498", 2640),
                new Customer("Szabó Tibor", "cID6067122", 12608)
        );

        CustomerDisplayHelper.displayCustomerList(customerList, MENU_MESSAGE);

        String expectedMessage = "There are a total of " + customerList.size() + " registered customers in the inventory:";
        String output = outputStream.toString();
        assertTrue(output.contains(expectedMessage),
                "Expected message '" + expectedMessage + "' was not found in the output.");
        for (Customer customer : customerList) {
            assertTrue(output.contains("Name: " + customer.getCustomerName() + " (" + customer.getCustomerId() +
                    "), Total purchases: " + customer.getTotalPurchases() + " HUF"));
        }
    }
}
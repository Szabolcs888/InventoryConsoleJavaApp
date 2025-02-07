package com.myinventoryapp.util.displayhelpers;

import com.myinventoryapp.entities.Product;
import com.myinventoryapp.entities.SalesTransaction;
import com.myinventoryapp.util.testutils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionDisplayHelperTest {
    private static final String MENU_MESSAGE = "\n-DISPLAY TRANSACTIONS MENU-\n";

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
    void testDisplayTransactionList_WithTransactions() {
        List<SalesTransaction> transactionList = Arrays.asList(
                new SalesTransaction("trId2301928", "Lakatos Regina", "cID1506683",
                        "pear", 5, 675, "2024.02.01. 15:09:19"),
                new SalesTransaction("trId1430909", "Nagy Anna", "cID5916556",
                        "banana", 3, 720, "2024.02.01. 15:03:58"),
                new SalesTransaction("trId4844949", "Egerszegi Krisztina", "cID5794138",
                        "cherry", 3, 452, "2024.11.03. 23:42:05")
        );

        TransactionDisplayHelper.displayTransactionList(transactionList, MENU_MESSAGE);

        String expectedMenuMessage = "-DISPLAY TRANSACTIONS MENU-";
        String output = outputStream.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains(expectedMenuMessage),
                "Expected message '" + expectedMenuMessage + "' was not found in the output.");
        String expectedTransactionsMessage = "There are a total of 3 transactions in the inventory:";
        assertTrue(output.contains(expectedTransactionsMessage),
                "Expected message '" + expectedTransactionsMessage + "' was not found in the output.");
        String expectedTransactionItem1 = "trId2301928, Date: 2024.02.01. 15:09:19, Product: pear, Quantity: " +
                "5, Amount paid: 5 * 675 = 3375 HUF, Customer: Lakatos Regina (cID1506683)";
        assertTrue(output.contains(expectedTransactionItem1),
                "Expected message '" + expectedTransactionItem1 + "' was not found in the output.");
        String expectedTransactionItem2 = "trId1430909, Date: 2024.02.01. 15:03:58, Product: banana, Quantity: 3, " +
                "Amount paid: 3 * 720 = 2160 HUF, Customer: Nagy Anna (cID5916556)";
        assertTrue(output.contains(expectedTransactionItem2),
                "Expected message '" + expectedTransactionItem2 + "' was not found in the output.");
        String expectedTransactionItem3 = "trId4844949, Date: 2024.11.03. 23:42:05, Product: cherry, Quantity: 3, " +
                "Amount paid: 3 * 452 = 1356 HUF, Customer: Egerszegi Krisztina (cID5794138)";
        assertTrue(output.contains(expectedTransactionItem3),
                "Expected message '" + expectedTransactionItem3 + "' was not found in the output.");
    }

    @Test
    void testDisplayTransactionList_EmptyList() {
        List<SalesTransaction> transactionList = Collections.emptyList();

        TransactionDisplayHelper.displayTransactionList(transactionList, MENU_MESSAGE);

        String expectedMenuMessage = "-DISPLAY TRANSACTIONS MENU-";
        String output = outputStream.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains(expectedMenuMessage),
                "Expected message '" + expectedMenuMessage + "' was not found in the output.");
        String expectedNoTransactionsMessage = "There are currently no transactions in the inventory!";
        assertTrue(output.contains(expectedNoTransactionsMessage),
                "Expected message '" + expectedNoTransactionsMessage + "' was not found in the output.");
    }

    @Test
    void testDisplayTransactionInfo_RegisteredCustomer() {
        Product product = new Product("carrot", "pr2110710", 245, 31);

        TransactionDisplayHelper.displayTransactionInfo(
                product, 8, "Kovács Ágnes", "cID5513060",
                true, "2024.02.12. 20:06:14");

        String expectedTransactionDetailsMessage1 = "TRANSACTION DETAILS: ";
        String output = outputStream.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains(expectedTransactionDetailsMessage1),
                "Expected message '" + expectedTransactionDetailsMessage1 + "' was not found in the output.");
        String expectedTransactionDetails1 = "Customer name: Kovács Ágnes (cID5513060 / returning customer)";
        assertTrue(output.contains(expectedTransactionDetails1),
                "Expected message '" + expectedTransactionDetails1 + "' was not found in the output.");
        String expectedTransactionDetails2 = "Quantity sold: 8\n" +
                "Amount paid: 8 * 245 = 1960 HUF\n" +
                "Transaction date: 2024.02.12. 20:06:14";
        assertTrue(output.contains(expectedTransactionDetails2),
                "Expected message '" + expectedTransactionDetails2 + "' was not found in the output.");
    }

    @Test
    void testDisplayTransactionInfo_NewCustomer() {
        Product product = new Product("lemon", "pr6634365", 880, 45);

        TransactionDisplayHelper.displayTransactionInfo(
                product, 10, "Szeleczky Zita", "cID5882118",
                false, "2024.11.03. 23:38:23");

        String expectedTransactionDetailsMessage1 = "TRANSACTION DETAILS: ";
        String output = outputStream.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains(expectedTransactionDetailsMessage1),
                "Expected message '" + expectedTransactionDetailsMessage1 + "' was not found in the output.");
        String expectedTransactionDetails1 = "Customer name: Szeleczky Zita (cID5882118 / newly registered)";
        assertTrue(output.contains(expectedTransactionDetails1),
                "Expected message '" + expectedTransactionDetails1 + "' was not found in the output.");
        String expectedTransactionDetails2 = "Quantity sold: 10\n" +
                "Amount paid: 10 * 880 = 8800 HUF\n" +
                "Transaction date: 2024.11.03. 23:38:23";
        assertTrue(output.contains(expectedTransactionDetails2),
                "Expected message '" + expectedTransactionDetails2 + "' was not found in the output.");
    }
}
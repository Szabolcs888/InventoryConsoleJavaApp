package com.myinventoryapp;

import com.myinventoryapp.ui.menu.*;
import com.myinventoryapp.util.ErrorHandler;

import static org.mockito.Mockito.*;

import com.myinventoryapp.util.testutils.TestUtils;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.*;

class InventoryAppTest {

    @Test
    void testMenuSelection_PrintsMessage() {
        ByteArrayOutputStream outputStream = TestUtils.redirectSystemOut();
        String welcomeMessage = "Welcome to the inventory system!" +
                "You can choose from the following menu items:";

        try (MockedStatic<ErrorHandler> mockedErrorHandler = Mockito.mockStatic(ErrorHandler.class)) {
            mockedErrorHandler.when(() -> ErrorHandler.getValidNumber(anyString())).thenReturn(3);

            InventoryApp inventoryApp = new InventoryApp();
            inventoryApp.menuSelection(welcomeMessage);

            String expectedMessage = "Welcome to the inventory system!" +
                    "You can choose from the following menu items:";
            String output = outputStream.toString();
            assertTrue(output.contains(expectedMessage),
                    "Expected message '" + expectedMessage + "' was not found in the output.");
        } finally {
            TestUtils.restoreSystemOut();
        }
    }

    @Test
    void testMenuSelection_ValidInput() {
        TestUtils.redirectSystemOut();
        String welcomeMessage = "Welcome to the inventory system!";

        try (MockedStatic<ErrorHandler> mockedErrorHandler = Mockito.mockStatic(ErrorHandler.class)) {
            mockedErrorHandler.when(() -> ErrorHandler.getValidNumber(anyString())).thenReturn(4);

            InventoryApp inventoryApp = new InventoryApp();
            int result = inventoryApp.menuSelection(welcomeMessage);

            mockedErrorHandler.verify(() -> ErrorHandler.getValidNumber(anyString()), times(1));
            assertEquals(4, result, "Expected menu selection to be '4', but got: " + result);
        } finally {
            TestUtils.restoreSystemOut();
        }
    }

    @Test
    void testMenuSelection_InvalidInput() {
        ByteArrayOutputStream outputStream = TestUtils.redirectSystemOut();
        String welcomeMessage = "Welcome to the inventory system!" +
                "You can choose from the following menu items:";

        try (MockedStatic<ErrorHandler> mockedErrorHandler = Mockito.mockStatic(ErrorHandler.class)) {
            mockedErrorHandler.when(() -> ErrorHandler.getValidNumber(anyString())).thenReturn(0).thenReturn(3);

            InventoryApp inventoryApp = new InventoryApp();
            inventoryApp.menuSelection(welcomeMessage);

            String expectedMessage = "You can choose from 1 to 6!";
            String output = outputStream.toString();
            assertTrue(output.contains(expectedMessage),
                    "Expected message '" + expectedMessage + "' was not found in the output.");
        } finally {
            TestUtils.restoreSystemOut();
        }
    }

    @Test
    void testTransactionSelector_ExecutesMenuOption1Sell() {
        InventoryApp spyInventoryApp = Mockito.spy(new InventoryApp());

        MenuOption1Sell mockSellMenu = Mockito.mock(MenuOption1Sell.class);
        MenuOption6SaveData mockSaveData = Mockito.mock(MenuOption6SaveData.class);
        spyInventoryApp.setMenuOption1Sell(mockSellMenu);
        spyInventoryApp.setMenuOption6SaveData(mockSaveData);
        Mockito.doReturn(6).when(spyInventoryApp).menuSelection(anyString());

        spyInventoryApp.transactionSelector(1);

        Mockito.verify(mockSellMenu, times(1)).sellProduct(anyString());
        Mockito.verify(mockSaveData, times(1)).saveData();
    }

    @Test
    void testTransactionSelector_ExecutesMenuOption2GoodsReceipt() {
        InventoryApp spyInventoryApp = Mockito.spy(new InventoryApp());

        MenuOption2GoodsReceipt mockGoodsReceipt = Mockito.mock(MenuOption2GoodsReceipt.class);
        MenuOption6SaveData mockSaveData = Mockito.mock(MenuOption6SaveData.class);
        spyInventoryApp.setMenuOption2GoodsReceipt(mockGoodsReceipt);
        spyInventoryApp.setMenuOption6SaveData(mockSaveData);
        Mockito.doReturn(6).when(spyInventoryApp).menuSelection(anyString());

        spyInventoryApp.transactionSelector(2);

        Mockito.verify(mockGoodsReceipt, times(1)).goodsReceipt(anyString());
        Mockito.verify(mockSaveData, times(1)).saveData();
    }

    @Test
    void testTransactionSelector_ExecutesMenuOption3DisplayProducts() {
        InventoryApp spyInventoryApp = Mockito.spy(new InventoryApp());

        MenuOption3DisplayProducts mockDisplayProducts = Mockito.mock(MenuOption3DisplayProducts.class);
        MenuOption6SaveData mockSaveData = Mockito.mock(MenuOption6SaveData.class);
        spyInventoryApp.setMenuOption3DisplayProducts(mockDisplayProducts);
        spyInventoryApp.setMenuOption6SaveData(mockSaveData);
        Mockito.doReturn(6).when(spyInventoryApp).menuSelection(anyString());

        spyInventoryApp.transactionSelector(3);

        Mockito.verify(mockDisplayProducts, times(1)).displayProductList(anyString());
        Mockito.verify(mockSaveData, times(1)).saveData();
    }

    @Test
    void testTransactionSelector_ExecutesMenuOption4DisplayCustomers() {
        InventoryApp spyInventoryApp = Mockito.spy(new InventoryApp());

        MenuOption4DisplayCustomers mockDisplayCustomers = Mockito.mock(MenuOption4DisplayCustomers.class);
        MenuOption6SaveData mockSaveData = Mockito.mock(MenuOption6SaveData.class);
        spyInventoryApp.setMenuOption4DisplayCustomers(mockDisplayCustomers);
        spyInventoryApp.setMenuOption6SaveData(mockSaveData);
        Mockito.doReturn(6).when(spyInventoryApp).menuSelection(anyString());

        spyInventoryApp.transactionSelector(4);

        Mockito.verify(mockDisplayCustomers, times(1)).displayCustomerList(anyString());
        Mockito.verify(mockSaveData, times(1)).saveData();
    }

    @Test
    void testTransactionSelector_ExecutesMenuOption5DisplayTransactions() {
        InventoryApp spyInventoryApp = Mockito.spy(new InventoryApp());

        MenuOption5DisplayTransactions mockDisplayTransactions = Mockito.mock(MenuOption5DisplayTransactions.class);
        MenuOption6SaveData mockSaveData = Mockito.mock(MenuOption6SaveData.class);
        spyInventoryApp.setMenuOption5DisplayTransactions(mockDisplayTransactions);
        spyInventoryApp.setMenuOption6SaveData(mockSaveData);
        Mockito.doReturn(6).when(spyInventoryApp).menuSelection(anyString());

        spyInventoryApp.transactionSelector(5);

        Mockito.verify(mockDisplayTransactions, times(1)).displayTransactionList(anyString());
        Mockito.verify(mockSaveData, times(1)).saveData();
    }

    @Test
    void testTransactionSelector_ExecutesMenuOption6SaveData() {
        InventoryApp inventoryApp = new InventoryApp();

        MenuOption6SaveData mockSaveData = Mockito.mock(MenuOption6SaveData.class);
        inventoryApp.setMenuOption6SaveData(mockSaveData);

        inventoryApp.transactionSelector(6);

        Mockito.verify(mockSaveData, times(1)).saveData();
    }

    @Test
    void testTransactionSelector_WithMenuSelection() {
        InventoryApp spyInventoryApp = Mockito.spy(new InventoryApp());

        MenuOption1Sell mockSellMenu = Mockito.mock(MenuOption1Sell.class);
        MenuOption6SaveData mockSaveData = Mockito.mock(MenuOption6SaveData.class);
        spyInventoryApp.setMenuOption1Sell(mockSellMenu);
        spyInventoryApp.setMenuOption6SaveData(mockSaveData);
        Mockito.doReturn(6).when(spyInventoryApp).menuSelection(anyString());

        spyInventoryApp.transactionSelector(1);

        Mockito.verify(spyInventoryApp, times(1)).menuSelection(anyString());
        Mockito.verify(mockSellMenu, times(1)).sellProduct(anyString());
        Mockito.verify(mockSaveData, times(1)).saveData();
    }
}
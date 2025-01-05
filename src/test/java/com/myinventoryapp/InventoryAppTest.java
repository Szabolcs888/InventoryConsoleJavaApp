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
            mockedErrorHandler.when(() -> ErrorHandler.getValidNumber(anyString()))
                    .thenReturn(0)
                    .thenReturn(3);

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
        InventoryApp inventoryApp = Mockito.spy(new InventoryApp());

        MenuOption1Sell mockSellMenu = mock(MenuOption1Sell.class);
        MenuOption6SaveData mockSaveData = mock(MenuOption6SaveData.class);
        inventoryApp.setMenuOption1Sell(mockSellMenu);
        inventoryApp.setMenuOption6SaveData(mockSaveData);
        doReturn(6).when(inventoryApp).menuSelection(anyString());

        inventoryApp.transactionSelector(1);

        verify(mockSellMenu, times(1)).sellProduct(anyString());
        verify(mockSaveData, times(1)).saveData();
    }

    @Test
    void testTransactionSelector_ExecutesMenuOption2GoodsReceipt() {
        InventoryApp inventoryApp = Mockito.spy(new InventoryApp());

        MenuOption2GoodsReceipt mockGoodsReceipt = mock(MenuOption2GoodsReceipt.class);
        MenuOption6SaveData mockSaveData = mock(MenuOption6SaveData.class);
        inventoryApp.setMenuOption2GoodsReceipt(mockGoodsReceipt);
        inventoryApp.setMenuOption6SaveData(mockSaveData);
        doReturn(6).when(inventoryApp).menuSelection(anyString());

        inventoryApp.transactionSelector(2);

        verify(mockGoodsReceipt, times(1)).goodsReceipt(anyString());
        verify(mockSaveData, times(1)).saveData();
    }

    @Test
    void testTransactionSelector_ExecutesMenuOption3DisplayProducts() {
        InventoryApp inventoryApp = Mockito.spy(new InventoryApp());

        MenuOption3DisplayProducts mockDisplayProducts = mock(MenuOption3DisplayProducts.class);
        MenuOption6SaveData mockSaveData = mock(MenuOption6SaveData.class);
        inventoryApp.setMenuOption3DisplayProducts(mockDisplayProducts);
        inventoryApp.setMenuOption6SaveData(mockSaveData);
        doReturn(6).when(inventoryApp).menuSelection(anyString());

        inventoryApp.transactionSelector(3);

        verify(mockDisplayProducts, times(1)).displayProductList(anyString());
        verify(mockSaveData, times(1)).saveData();
    }

    @Test
    void testTransactionSelector_ExecutesMenuOption4DisplayCustomers() {
        InventoryApp inventoryApp = Mockito.spy(new InventoryApp());

        MenuOption4DisplayCustomers mockDisplayCustomers = mock(MenuOption4DisplayCustomers.class);
        MenuOption6SaveData mockSaveData = mock(MenuOption6SaveData.class);
        inventoryApp.setMenuOption4DisplayCustomers(mockDisplayCustomers);
        inventoryApp.setMenuOption6SaveData(mockSaveData);
        doReturn(6).when(inventoryApp).menuSelection(anyString());

        inventoryApp.transactionSelector(4);

        verify(mockDisplayCustomers, times(1)).displayCustomerList(anyString());
        verify(mockSaveData, times(1)).saveData();
    }

    @Test
    void testTransactionSelector_ExecutesMenuOption5DisplayTransactions() {
        InventoryApp inventoryApp = Mockito.spy(new InventoryApp());

        MenuOption5DisplayTransactions mockDisplayTransactions = mock(MenuOption5DisplayTransactions.class);
        MenuOption6SaveData mockSaveData = mock(MenuOption6SaveData.class);
        inventoryApp.setMenuOption5DisplayTransactions(mockDisplayTransactions);
        inventoryApp.setMenuOption6SaveData(mockSaveData);
        doReturn(6).when(inventoryApp).menuSelection(anyString());

        inventoryApp.transactionSelector(5);

        verify(mockDisplayTransactions, times(1)).displayTransactionList(anyString());
        verify(mockSaveData, times(1)).saveData();
    }

    @Test
    void testTransactionSelector_ExecutesMenuOption6SaveData() {
        InventoryApp inventoryApp = new InventoryApp();

        MenuOption6SaveData mockSaveData = mock(MenuOption6SaveData.class);
        inventoryApp.setMenuOption6SaveData(mockSaveData);

        inventoryApp.transactionSelector(6);

        verify(mockSaveData, times(1)).saveData();
    }

    @Test
    void testTransactionSelector_WithMenuSelection() {
        InventoryApp inventoryApp = Mockito.spy(new InventoryApp());

        MenuOption1Sell mockSellMenu = mock(MenuOption1Sell.class);
        MenuOption6SaveData mockSaveData = mock(MenuOption6SaveData.class);
        inventoryApp.setMenuOption1Sell(mockSellMenu);
        inventoryApp.setMenuOption6SaveData(mockSaveData);
        doReturn(6).when(inventoryApp).menuSelection(anyString());

        inventoryApp.transactionSelector(1);

        verify(inventoryApp, times(1)).menuSelection(anyString());
        verify(mockSellMenu, times(1)).sellProduct(anyString());
        verify(mockSaveData, times(1)).saveData();
    }
}
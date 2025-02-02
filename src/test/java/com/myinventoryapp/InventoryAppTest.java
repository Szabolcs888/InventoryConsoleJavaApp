package com.myinventoryapp;

import com.myinventoryapp.dataio.DataLoader;
import com.myinventoryapp.ui.menu.*;
import com.myinventoryapp.util.ErrorHandler;

import static org.mockito.Mockito.*;

import com.myinventoryapp.util.testutils.MenuOptionFactory;
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

        MenuOption1Sell menuOption1Sell = MenuOptionFactory.createMenuOption1Sell();
        MenuOption2GoodsReceipt menuOption2GoodsReceipt = MenuOptionFactory.createMenuOption2GoodsReceipt();
        MenuOption6SaveData menuOption6SaveData = MenuOptionFactory.createMenuOption6SaveData();

        try (MockedStatic<ErrorHandler> mockedErrorHandler = Mockito.mockStatic(ErrorHandler.class)) {
            mockedErrorHandler.when(() -> ErrorHandler.getValidNumber(anyString())).thenReturn(3);

            InventoryApp inventoryApp = new InventoryApp(menuOption1Sell, menuOption2GoodsReceipt,
                    new MenuOption3DisplayProducts(), new MenuOption4DisplayCustomers(),
                    new MenuOption5DisplayTransactions(), menuOption6SaveData, new DataLoader());

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
        int userChoice = 4;

        MenuOption1Sell menuOption1Sell = MenuOptionFactory.createMenuOption1Sell();
        MenuOption2GoodsReceipt menuOption2GoodsReceipt = MenuOptionFactory.createMenuOption2GoodsReceipt();
        MenuOption6SaveData menuOption6SaveData = MenuOptionFactory.createMenuOption6SaveData();

        try (MockedStatic<ErrorHandler> mockedErrorHandler = Mockito.mockStatic(ErrorHandler.class)) {
            mockedErrorHandler.when(() -> ErrorHandler.getValidNumber(anyString())).thenReturn(userChoice);

            InventoryApp inventoryApp = new InventoryApp(menuOption1Sell, menuOption2GoodsReceipt,
                    new MenuOption3DisplayProducts(), new MenuOption4DisplayCustomers(),
                    new MenuOption5DisplayTransactions(), menuOption6SaveData, new DataLoader());

            int userChoiceResult = inventoryApp.menuSelection(welcomeMessage);

            assertEquals(userChoice, userChoiceResult, "Expected menu selection to be '4', but got: " + userChoiceResult);
            mockedErrorHandler.verify(() -> ErrorHandler.getValidNumber(anyString()), times(1));
        } finally {
            TestUtils.restoreSystemOut();
        }
    }

    @Test
    void testMenuSelection_InvalidInput() {
        ByteArrayOutputStream outputStream = TestUtils.redirectSystemOut();
        String welcomeMessage = "Welcome to the inventory system!" +
                "You can choose from the following menu items:";

        MenuOption1Sell menuOption1Sell = MenuOptionFactory.createMenuOption1Sell();
        MenuOption2GoodsReceipt menuOption2GoodsReceipt = MenuOptionFactory.createMenuOption2GoodsReceipt();
        MenuOption6SaveData menuOption6SaveData = MenuOptionFactory.createMenuOption6SaveData();

        try (MockedStatic<ErrorHandler> mockedErrorHandler = Mockito.mockStatic(ErrorHandler.class)) {
            mockedErrorHandler.when(() -> ErrorHandler.getValidNumber(anyString())).thenReturn(0).thenReturn(3);

            InventoryApp inventoryApp = new InventoryApp(menuOption1Sell, menuOption2GoodsReceipt,
                    new MenuOption3DisplayProducts(), new MenuOption4DisplayCustomers(),
                    new MenuOption5DisplayTransactions(), menuOption6SaveData, new DataLoader());

            inventoryApp.menuSelection(welcomeMessage);

            String expectedMessage = "You can choose from 1 to 6!";
            String output = outputStream.toString();
            assertTrue(output.contains(expectedMessage),
                    "Expected message '" + expectedMessage + "' was not found in the output.");
            mockedErrorHandler.verify(() -> ErrorHandler.getValidNumber(anyString()), times(2));
        } finally {
            TestUtils.restoreSystemOut();
        }
    }

    @Test
    void testTransactionSelector_ExecutesMenuOption1Sell() {
        MenuOption2GoodsReceipt menuOption2GoodsReceipt = MenuOptionFactory.createMenuOption2GoodsReceipt();

        MenuOption1Sell mockMenuOption1Sell = Mockito.mock(MenuOption1Sell.class);
        MenuOption6SaveData mockMenuOption6SaveData = Mockito.mock(MenuOption6SaveData.class);

        InventoryApp spyInventoryApp = Mockito.spy(new InventoryApp(mockMenuOption1Sell, menuOption2GoodsReceipt,
                new MenuOption3DisplayProducts(), new MenuOption4DisplayCustomers(),
                new MenuOption5DisplayTransactions(), mockMenuOption6SaveData, new DataLoader()));

        Mockito.doNothing().when(mockMenuOption1Sell).sellProduct(anyString());
        Mockito.doReturn(6).when(spyInventoryApp).menuSelection(anyString());
        Mockito.doNothing().when(mockMenuOption6SaveData).saveData();

        spyInventoryApp.transactionSelector(1);

        Mockito.verify(mockMenuOption1Sell, times(1)).
                sellProduct("\n-SELL PRODUCT MENU-\n");
        Mockito.verify(mockMenuOption6SaveData, times(1)).saveData();
    }

    @Test
    void testTransactionSelector_ExecutesMenuOption2GoodsReceipt() {
        MenuOption1Sell menuOption1Sell = MenuOptionFactory.createMenuOption1Sell();

        MenuOption2GoodsReceipt mockMenuOption2GoodsReceipt = Mockito.mock(MenuOption2GoodsReceipt.class);
        MenuOption6SaveData mockMenuOption6SaveData = Mockito.mock(MenuOption6SaveData.class);

        InventoryApp spyInventoryApp = Mockito.spy(new InventoryApp(menuOption1Sell, mockMenuOption2GoodsReceipt,
                new MenuOption3DisplayProducts(), new MenuOption4DisplayCustomers(),
                new MenuOption5DisplayTransactions(), mockMenuOption6SaveData, new DataLoader()));

        Mockito.doNothing().when(mockMenuOption2GoodsReceipt).goodsReceipt(anyString());
        Mockito.doReturn(6).when(spyInventoryApp).menuSelection(anyString());
        Mockito.doNothing().when(mockMenuOption6SaveData).saveData();

        spyInventoryApp.transactionSelector(2);

        Mockito.verify(mockMenuOption2GoodsReceipt, times(1)).
                goodsReceipt("\n-RECEIVE PRODUCT MENU-\n");
        Mockito.verify(mockMenuOption6SaveData, times(1)).saveData();
    }

    @Test
    void testTransactionSelector_ExecutesMenuOption3DisplayProducts() {
        MenuOption1Sell menuOption1Sell = MenuOptionFactory.createMenuOption1Sell();
        MenuOption2GoodsReceipt menuOption2GoodsReceipt = MenuOptionFactory.createMenuOption2GoodsReceipt();

        MenuOption3DisplayProducts mockMenuOption3DisplayProducts = Mockito.mock(MenuOption3DisplayProducts.class);
        MenuOption6SaveData mockMenuOption6SaveData = Mockito.mock(MenuOption6SaveData.class);

        InventoryApp spyInventoryApp = Mockito.spy(new InventoryApp(menuOption1Sell, menuOption2GoodsReceipt,
                mockMenuOption3DisplayProducts, new MenuOption4DisplayCustomers(),
                new MenuOption5DisplayTransactions(), mockMenuOption6SaveData, new DataLoader()));

        Mockito.doNothing().when(mockMenuOption3DisplayProducts).displayProductList(anyString());
        Mockito.doReturn(6).when(spyInventoryApp).menuSelection(anyString());
        Mockito.doNothing().when(mockMenuOption6SaveData).saveData();

        spyInventoryApp.transactionSelector(3);

        Mockito.verify(mockMenuOption3DisplayProducts, times(1)).
                displayProductList("\n-DISPLAY AVAILABLE PRODUCTS MENU-\n");
        Mockito.verify(mockMenuOption6SaveData, times(1)).saveData();
    }

    @Test
    void testTransactionSelector_ExecutesMenuOption4DisplayCustomers() {
        MenuOption1Sell menuOption1Sell = MenuOptionFactory.createMenuOption1Sell();
        MenuOption2GoodsReceipt menuOption2GoodsReceipt = MenuOptionFactory.createMenuOption2GoodsReceipt();

        MenuOption4DisplayCustomers mockMenuOption4DisplayCustomers = Mockito.mock(MenuOption4DisplayCustomers.class);
        MenuOption6SaveData mockMenuOption6SaveData = Mockito.mock(MenuOption6SaveData.class);

        InventoryApp spyInventoryApp = Mockito.spy(new InventoryApp(menuOption1Sell, menuOption2GoodsReceipt,
                new MenuOption3DisplayProducts(), mockMenuOption4DisplayCustomers,
                new MenuOption5DisplayTransactions(), mockMenuOption6SaveData, new DataLoader()));

        Mockito.doNothing().when(mockMenuOption4DisplayCustomers).displayCustomerList(anyString());
        Mockito.doReturn(6).when(spyInventoryApp).menuSelection(anyString());
        Mockito.doNothing().when(mockMenuOption6SaveData).saveData();

        spyInventoryApp.transactionSelector(4);

        Mockito.verify(mockMenuOption4DisplayCustomers, times(1)).
                displayCustomerList("\n-DISPLAY CUSTOMERS MENU-\n");
        Mockito.verify(mockMenuOption6SaveData, times(1)).saveData();
    }

    @Test
    void testTransactionSelector_ExecutesMenuOption5DisplayTransactions() {
        MenuOption1Sell menuOption1Sell = MenuOptionFactory.createMenuOption1Sell();
        MenuOption2GoodsReceipt menuOption2GoodsReceipt = MenuOptionFactory.createMenuOption2GoodsReceipt();

        MenuOption5DisplayTransactions mockMenuOption5DisplayTransactions = Mockito.mock(MenuOption5DisplayTransactions.class);
        MenuOption6SaveData mockMenuOption6SaveData = Mockito.mock(MenuOption6SaveData.class);

        InventoryApp spyInventoryApp = Mockito.spy(new InventoryApp(menuOption1Sell, menuOption2GoodsReceipt,
                new MenuOption3DisplayProducts(), new MenuOption4DisplayCustomers(),
                mockMenuOption5DisplayTransactions, mockMenuOption6SaveData, new DataLoader()));

        Mockito.doNothing().when(mockMenuOption5DisplayTransactions).displayTransactionList(anyString());
        Mockito.doReturn(6).when(spyInventoryApp).menuSelection(anyString());
        Mockito.doNothing().when(mockMenuOption6SaveData).saveData();

        spyInventoryApp.transactionSelector(5);

        Mockito.verify(mockMenuOption5DisplayTransactions, times(1)).
                displayTransactionList("\n-DISPLAY TRANSACTIONS MENU-\n");
        Mockito.verify(mockMenuOption6SaveData, times(1)).saveData();
    }

    @Test
    void testTransactionSelector_ExecutesMenuOption6SaveData() {
        MenuOption1Sell menuOption1Sell = MenuOptionFactory.createMenuOption1Sell();
        MenuOption2GoodsReceipt menuOption2GoodsReceipt = MenuOptionFactory.createMenuOption2GoodsReceipt();

        MenuOption6SaveData mockMenuOption6SaveData = Mockito.mock(MenuOption6SaveData.class);
        InventoryApp inventoryApp = new InventoryApp(menuOption1Sell, menuOption2GoodsReceipt,
                new MenuOption3DisplayProducts(), new MenuOption4DisplayCustomers(),
                new MenuOption5DisplayTransactions(), mockMenuOption6SaveData, new DataLoader());

        Mockito.doNothing().when(mockMenuOption6SaveData).saveData();

        inventoryApp.transactionSelector(6);

        Mockito.verify(mockMenuOption6SaveData, times(1)).saveData();
    }

    @Test
    void testTransactionSelector_MenuSelection() {
        MenuOption1Sell menuOption1Sell = MenuOptionFactory.createMenuOption1Sell();

        MenuOption2GoodsReceipt mockMenuOption2GoodsReceipt = Mockito.mock(MenuOption2GoodsReceipt.class);
        MenuOption3DisplayProducts mockMenuOption3DisplayProducts = Mockito.mock(MenuOption3DisplayProducts.class);
        MenuOption4DisplayCustomers mockMenuOption4DisplayCustomers = Mockito.mock(MenuOption4DisplayCustomers.class);
        MenuOption6SaveData mockMenuOption6SaveData = Mockito.mock(MenuOption6SaveData.class);

        InventoryApp spyInventoryApp = Mockito.spy(new InventoryApp(menuOption1Sell, mockMenuOption2GoodsReceipt,
                mockMenuOption3DisplayProducts, mockMenuOption4DisplayCustomers,
                new MenuOption5DisplayTransactions(), mockMenuOption6SaveData, new DataLoader()));

        Mockito.doNothing().when(mockMenuOption4DisplayCustomers).displayCustomerList(anyString());
        Mockito.doNothing().when(mockMenuOption3DisplayProducts).displayProductList(anyString());
        Mockito.doNothing().when(mockMenuOption2GoodsReceipt).goodsReceipt(anyString());
        Mockito.doReturn(3, 2, 6).when(spyInventoryApp).menuSelection(anyString());
        Mockito.doNothing().when(mockMenuOption6SaveData).saveData();

        spyInventoryApp.transactionSelector(4);

        Mockito.verify(spyInventoryApp, times(3)).
                menuSelection("\n-MAIN MENU-\n");
        Mockito.verify(mockMenuOption4DisplayCustomers, times(1)).
                displayCustomerList("\n-DISPLAY CUSTOMERS MENU-\n");
        Mockito.verify(mockMenuOption3DisplayProducts, times(1)).
                displayProductList("\n-DISPLAY AVAILABLE PRODUCTS MENU-\n");
        Mockito.verify(mockMenuOption2GoodsReceipt, times(1)).
                goodsReceipt("\n-RECEIVE PRODUCT MENU-\n");
        Mockito.verify(mockMenuOption6SaveData, times(1)).saveData();
    }
}
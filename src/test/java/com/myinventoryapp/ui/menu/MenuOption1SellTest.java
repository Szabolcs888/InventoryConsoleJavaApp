package com.myinventoryapp.ui.menu;

import com.myinventoryapp.entities.Product;
import com.myinventoryapp.repository.ProductRepository;
import com.myinventoryapp.services.CustomerService;
import com.myinventoryapp.services.ProductService;
import com.myinventoryapp.services.TransactionService;
import com.myinventoryapp.util.ErrorHandler;
import com.myinventoryapp.util.displayhelpers.ProductDisplayHelper;
import com.myinventoryapp.util.testutils.MenuOptionFactory;
import com.myinventoryapp.util.testutils.TestUtils;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

class MenuOption1SellTest {

    @Test
    void testSellProduct_PrintsSellProductMenuMessage() {
        ByteArrayOutputStream outputStream = TestUtils.redirectSystemOut();

        try (MockedStatic<ProductRepository> mockedProductRepository = Mockito.mockStatic(ProductRepository.class);
             MockedStatic<ProductDisplayHelper> mockedProductDisplayHelper = Mockito.mockStatic(ProductDisplayHelper.class)) {
            mockedProductRepository.when(ProductRepository::getProductList).thenReturn(Collections.emptyList());
            mockedProductDisplayHelper.when(ProductDisplayHelper::displayNoProductsAvailableMessage).
                    thenAnswer(invocation -> null);

            MenuOption1Sell menuOption1Sell = MenuOptionFactory.createMenuOption1Sell();
            menuOption1Sell.sellProduct("\n-SELL PRODUCT MENU-\n");

            String expectedMessage = "-SELL PRODUCT MENU-";
            String output = outputStream.toString();
            assertTrue(output.contains(expectedMessage),
                    "Expected message '" + expectedMessage + "' was not found in the output.");
        } finally {
            TestUtils.restoreSystemOut();
        }
    }

    @Test
    void testSellProduct_EmptyProductList() {
        try (MockedStatic<ProductRepository> mockedProductRepository = Mockito.mockStatic(ProductRepository.class);
             MockedStatic<ProductDisplayHelper> mockedProductDisplayHelper = Mockito.mockStatic(ProductDisplayHelper.class)) {

            mockedProductRepository.when(ProductRepository::getProductList).thenReturn(Collections.emptyList());
            mockedProductDisplayHelper.when(ProductDisplayHelper::displayNoProductsAvailableMessage).
                    thenAnswer(invocation -> null);

            MenuOption1Sell spyMenuOption1Sell = Mockito.spy(MenuOptionFactory.createMenuOption1Sell());
            spyMenuOption1Sell.sellProduct(anyString());

            mockedProductRepository.verify(ProductRepository::getProductList, times(1));
            mockedProductDisplayHelper.verify(ProductDisplayHelper::displayNoProductsAvailableMessage, times(1));
            Mockito.verify(spyMenuOption1Sell, never()).processSale();
        }
    }

    @Test
    void testSellProduct_WithProducts() {
        String productName = "mango";
        String productId = "pr4531265";
        int unitPrice = 1350;
        int quantity = 46;
        Product product = new Product(productName, productId, unitPrice, quantity);

        try (MockedStatic<ProductRepository> mockedProductRepository = Mockito.mockStatic(ProductRepository.class);
             MockedStatic<ProductDisplayHelper> mockedProductDisplayHelper = Mockito.mockStatic(ProductDisplayHelper.class);
             MockedStatic<ErrorHandler> mockedErrorHandler = Mockito.mockStatic(ErrorHandler.class)) {
            MenuOption1Sell spyMenuOption1Sell = Mockito.spy(MenuOptionFactory.createMenuOption1Sell());

            mockedProductRepository.when(ProductRepository::getProductList).thenReturn(Collections.singletonList(product));
            mockedErrorHandler.when(() -> ErrorHandler.getYesOrNoAnswer(anyString())).thenReturn("N");
            Mockito.doNothing().when(spyMenuOption1Sell).processSale();

            spyMenuOption1Sell.sellProduct(anyString());

            mockedProductRepository.verify(ProductRepository::getProductList, times(1));
            mockedProductDisplayHelper.verify(ProductDisplayHelper::displayNoProductsAvailableMessage, times(0));
            Mockito.verify(spyMenuOption1Sell, times(1)).processSale();
            mockedErrorHandler.verify(() -> ErrorHandler.getYesOrNoAnswer(
                    "Would you like to register another sale? (Y/N)"), times(1));
        }
    }

    @Test
    void testSellProduct_WithProducts_UserRegistersAnotherSale() {
        String productName = "mango";
        String productId = "pr4531265";
        int unitPrice = 1350;
        int quantity = 46;
        Product product = new Product(productName, productId, unitPrice, quantity);

        try (MockedStatic<ProductRepository> mockedProductRepository = Mockito.mockStatic(ProductRepository.class);
             MockedStatic<ProductDisplayHelper> mockedProductDisplayHelper = Mockito.mockStatic(ProductDisplayHelper.class);
             MockedStatic<ErrorHandler> mockedErrorHandler = Mockito.mockStatic(ErrorHandler.class)) {
            MenuOption1Sell spyMenuOption1Sell = Mockito.spy(MenuOptionFactory.createMenuOption1Sell());

            mockedProductRepository.when(ProductRepository::getProductList).thenReturn(Collections.singletonList(product), Collections.singletonList(product));
            mockedErrorHandler.when(() -> ErrorHandler.getYesOrNoAnswer(anyString())).thenReturn("Y", "N");
            Mockito.doNothing().when(spyMenuOption1Sell).processSale();

            spyMenuOption1Sell.sellProduct(anyString());

            mockedProductRepository.verify(ProductRepository::getProductList, times(2));
            mockedProductDisplayHelper.verify(ProductDisplayHelper::displayNoProductsAvailableMessage, times(0));
            Mockito.verify(spyMenuOption1Sell, times(2)).processSale();
            mockedErrorHandler.verify(() -> ErrorHandler.getYesOrNoAnswer(
                    "Would you like to register another sale? (Y/N)"), times(2));
        }
    }

    @Test
    void testProcessSale_WithRegisteredCustomer() {
        String customerName = "Thomas Mann";
        String customerId = "cID2633111";
        boolean isRegisteredCustomer = true;
        String productName = "mango";
        String productId = "pr4531265";
        int unitPrice = 1350;
        int quantity = 46;
        int quantitySold = 3;
        Product productSold = new Product(productName, productId, unitPrice, quantity);

        try (MockedStatic<ProductDisplayHelper> mockedProductDisplayHelper = Mockito.mockStatic(ProductDisplayHelper.class)){
            CustomerService mockCustomerService = Mockito.mock(CustomerService.class);
            ProductService mockProductService = Mockito.mock(ProductService.class);
            TransactionService mockTransactionService = Mockito.mock(TransactionService.class);
            MenuOption1Sell spyMenuOption1Sell = Mockito.spy(new MenuOption1Sell(
                    mockCustomerService, mockProductService, mockTransactionService));

            Mockito.doReturn(customerName).when(spyMenuOption1Sell).getCustomerName();
            Mockito.doReturn(productSold).when(spyMenuOption1Sell).getProductData();
            Mockito.doReturn(quantitySold).when(spyMenuOption1Sell).getQuantitySold(productSold);
            Mockito.doReturn(isRegisteredCustomer).when(spyMenuOption1Sell).isCustomerRegistered(customerName);
            Mockito.when(mockCustomerService.handleCustomerTransaction(
                    isRegisteredCustomer, customerName, productSold, quantitySold)).thenReturn(customerId);
            Mockito.doNothing().when(mockTransactionService).transactionRegistration(
                    isRegisteredCustomer, customerName, customerId, productSold, quantitySold);
            Mockito.doNothing().when(mockProductService).updateProductQuantity(productSold, quantitySold);
            mockedProductDisplayHelper.when(() -> ProductDisplayHelper.displayProductInfoAfterSellAndUpdateGoodsReceipt(
                    productSold, "PRODUCT DATA AFTER THE TRANSACTION:")).thenAnswer(invocation -> null);

            spyMenuOption1Sell.processSale();

            Mockito.verify(spyMenuOption1Sell, times(1)).getCustomerName();
            Mockito.verify(spyMenuOption1Sell, times(1)).getProductData();
            Mockito.verify(spyMenuOption1Sell, times(1)).getQuantitySold(productSold);
            Mockito.verify(spyMenuOption1Sell, times(1)).isCustomerRegistered(customerName);
            Mockito.verify(mockCustomerService, times(1)).handleCustomerTransaction(
                    isRegisteredCustomer, customerName, productSold, quantitySold);
            Mockito.verify(mockTransactionService, times(1)).transactionRegistration(
                    isRegisteredCustomer, customerName, customerId, productSold, quantitySold);
            Mockito.verify(mockProductService, times(1)).updateProductQuantity(productSold, quantitySold);
            mockedProductDisplayHelper.verify(()-> ProductDisplayHelper.displayProductInfoAfterSellAndUpdateGoodsReceipt(
                    productSold, "PRODUCT DATA AFTER THE TRANSACTION:"), times(1));
        }
    }

    @Test
    void testProcessSale_WithNewCustomer() {
        String customerName = "Daniel Keyes";
        String customerId = "cID6729105";
        boolean isRegisteredCustomer = false;
        String productName = "tea";
        String productId = "pr7236284";
        int unitPrice = 1200;
        int quantity = 52;
        int quantitySold = 14;
        Product productSold = new Product(productName, productId, unitPrice, quantity);

        try (MockedStatic<ProductDisplayHelper> mockedProductDisplayHelper = Mockito.mockStatic(ProductDisplayHelper.class)){
            CustomerService mockCustomerService = Mockito.mock(CustomerService.class);
            ProductService mockProductService = Mockito.mock(ProductService.class);
            TransactionService mockTransactionService = Mockito.mock(TransactionService.class);
            MenuOption1Sell spyMenuOption1Sell = Mockito.spy(new MenuOption1Sell(
                    mockCustomerService, mockProductService, mockTransactionService));

            Mockito.doReturn(customerName).when(spyMenuOption1Sell).getCustomerName();
            Mockito.doReturn(productSold).when(spyMenuOption1Sell).getProductData();
            Mockito.doReturn(quantitySold).when(spyMenuOption1Sell).getQuantitySold(productSold);
            Mockito.doReturn(isRegisteredCustomer).when(spyMenuOption1Sell).isCustomerRegistered(customerName);
            Mockito.when(mockCustomerService.handleCustomerTransaction(
                    isRegisteredCustomer, customerName, productSold, quantitySold)).thenReturn(customerId);
            Mockito.doNothing().when(mockTransactionService).transactionRegistration(
                    isRegisteredCustomer, customerName, customerId, productSold, quantitySold);
            Mockito.doNothing().when(mockProductService).updateProductQuantity(productSold, quantitySold);
            mockedProductDisplayHelper.when(() -> ProductDisplayHelper.displayProductInfoAfterSellAndUpdateGoodsReceipt(
                    productSold, "PRODUCT DATA AFTER THE TRANSACTION:")).thenAnswer(invocation -> null);

            spyMenuOption1Sell.processSale();

            Mockito.verify(spyMenuOption1Sell, times(1)).getCustomerName();
            Mockito.verify(spyMenuOption1Sell, times(1)).getProductData();
            Mockito.verify(spyMenuOption1Sell, times(1)).getQuantitySold(productSold);
            Mockito.verify(spyMenuOption1Sell, times(1)).isCustomerRegistered(customerName);
            Mockito.verify(mockCustomerService, times(1)).handleCustomerTransaction(
                    isRegisteredCustomer, customerName, productSold, quantitySold);
            Mockito.verify(mockTransactionService, times(1)).transactionRegistration(
                    isRegisteredCustomer, customerName, customerId, productSold, quantitySold);
            Mockito.verify(mockProductService, times(1)).updateProductQuantity(productSold, quantitySold);
            mockedProductDisplayHelper.verify(()-> ProductDisplayHelper.displayProductInfoAfterSellAndUpdateGoodsReceipt(
                    productSold, "PRODUCT DATA AFTER THE TRANSACTION:"), times(1));
        }
    }
}
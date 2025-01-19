package com.myinventoryapp.ui.menu;

import com.myinventoryapp.entities.Product;
import com.myinventoryapp.repository.ProductRepository;
import com.myinventoryapp.util.ErrorHandler;
import com.myinventoryapp.util.displayhelpers.ProductDisplayHelper;
import com.myinventoryapp.util.testutils.TestUtils;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
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

            MenuOption1Sell menuOption1Sell = new MenuOption1Sell();
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

            MenuOption1Sell spyMenuOption1Sell = Mockito.spy(new MenuOption1Sell());
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
            MenuOption1Sell spyMenuOption1Sell = Mockito.spy(new MenuOption1Sell());

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
            MenuOption1Sell spyMenuOption1Sell = Mockito.spy(new MenuOption1Sell());

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
}
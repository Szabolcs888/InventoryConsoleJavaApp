package com.myinventoryapp.ui.menu;

import com.myinventoryapp.entities.Customer;
import com.myinventoryapp.entities.Product;
import com.myinventoryapp.repository.CustomerRepository;
import com.myinventoryapp.repository.ProductRepository;
import com.myinventoryapp.services.CustomerService;
import com.myinventoryapp.services.ProductService;
import com.myinventoryapp.services.TransactionService;
import com.myinventoryapp.util.*;
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
        Product product = new Product("pear", "pr4270613", 675, 19);

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
        Product product = new Product("coffee", "pr5204875", 1800, 44);

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
        int quantitySold = 3;
        Product productSold = new Product("pineapple", "pr5711807", 894, 1);

        try (MockedStatic<ProductDisplayHelper> mockedProductDisplayHelper = Mockito.mockStatic(ProductDisplayHelper.class)) {
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
            mockedProductDisplayHelper.verify(() -> ProductDisplayHelper.displayProductInfoAfterSellAndUpdateGoodsReceipt(
                    productSold, "PRODUCT DATA AFTER THE TRANSACTION:"), times(1));
        }
    }

    @Test
    void testProcessSale_WithNewCustomer() {
        String customerName = "Daniel Keyes";
        String customerId = "cID6729105";
        boolean isRegisteredCustomer = false;
        int quantitySold = 14;
        Product productSold = new Product("banana", "pr5223508", 720, 102);

        try (MockedStatic<ProductDisplayHelper> mockedProductDisplayHelper = Mockito.mockStatic(ProductDisplayHelper.class)) {
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
            mockedProductDisplayHelper.verify(() -> ProductDisplayHelper.displayProductInfoAfterSellAndUpdateGoodsReceipt(
                    productSold, "PRODUCT DATA AFTER THE TRANSACTION:"), times(1));
        }
    }

    @Test
    void testGetCustomerName_ValidInputFirstTry() {
        String customerName = "Kölcsey Ferenc";
        boolean isValidName = true;

        try (MockedStatic<UserInputUtils> mockedUserInputUtils = Mockito.mockStatic(UserInputUtils.class);
             MockedStatic<ErrorHandler> mockedErrorHandler = Mockito.mockStatic(ErrorHandler.class);
             MockedStatic<ValidationUtils> mockedValidationUtils = Mockito.mockStatic(ValidationUtils.class)) {

            mockedUserInputUtils.when(() -> UserInputUtils.readFromUser(anyString())).thenReturn(customerName);
            mockedErrorHandler.when(() -> ErrorHandler.validateName(customerName)).thenAnswer(invocation -> null);
            mockedValidationUtils.when(() -> ValidationUtils.isValidName(customerName)).thenReturn(isValidName);


            MenuOption1Sell menuOption1Sell = MenuOptionFactory.createMenuOption1Sell();
            String result = menuOption1Sell.getCustomerName();

            assertEquals(customerName, result);
            mockedUserInputUtils.verify(() -> UserInputUtils.readFromUser("Please enter the customer's name:"), times(1));
            mockedErrorHandler.verify(() -> ErrorHandler.validateName(customerName), times(1));
            mockedValidationUtils.verify(() -> ValidationUtils.isValidName(customerName), times(1));
        }
    }

    @Test
    void testGetCustomerName_MultipleTries() {
        String customerName = "Erkel Ferenc";
        String invalidCustomerName = "InvalidName";
        boolean isValidName = false;

        try (MockedStatic<UserInputUtils> mockedUserInputUtils = Mockito.mockStatic(UserInputUtils.class);
             MockedStatic<ErrorHandler> mockedErrorHandler = Mockito.mockStatic(ErrorHandler.class);
             MockedStatic<ValidationUtils> mockedValidationUtils = Mockito.mockStatic(ValidationUtils.class)) {

            mockedUserInputUtils.when(() -> UserInputUtils.readFromUser(anyString())).thenReturn(invalidCustomerName).thenReturn(customerName);
            mockedErrorHandler.when(() -> ErrorHandler.validateName(anyString())).thenAnswer(invocation -> null);
            mockedValidationUtils.when(() -> ValidationUtils.isValidName(invalidCustomerName)).thenReturn(isValidName);
            mockedValidationUtils.when(() -> ValidationUtils.isValidName(customerName)).thenReturn(true);

            MenuOption1Sell menuOption1Sell = MenuOptionFactory.createMenuOption1Sell();
            String result = menuOption1Sell.getCustomerName();

            assertEquals(customerName, result);
            mockedUserInputUtils.verify(() -> UserInputUtils.readFromUser("Please enter the customer's name:"), times(2));
            mockedErrorHandler.verify(() -> ErrorHandler.validateName(invalidCustomerName), times(1));
            mockedValidationUtils.verify(() -> ValidationUtils.isValidName(invalidCustomerName), times(1));
            mockedErrorHandler.verify(() -> ErrorHandler.validateName(customerName), times(1));
            mockedValidationUtils.verify(() -> ValidationUtils.isValidName(customerName), times(1));
        }
    }

    @Test
    void testGetProductData_ValidProductAndSufficientQuantity() {
        String inputProductName = "mango";
        Product foundProduct = new Product("mango", "pr4531265", 1350, 46);

        try (MockedStatic<UserInputUtils> mockedUserInputUtils = Mockito.mockStatic(UserInputUtils.class);
             MockedStatic<ProductRepository> mockedProductRepository = Mockito.mockStatic(ProductRepository.class);
             MockedStatic<ProductDisplayHelper> mockedProductDisplayHelper = Mockito.mockStatic(ProductDisplayHelper.class)) {

            mockedUserInputUtils.when(() -> UserInputUtils.readFromUser(anyString())).thenReturn(inputProductName);
            mockedProductRepository.when(() -> ProductRepository.findProductByName(inputProductName)).thenReturn(foundProduct);
            mockedProductDisplayHelper.when(() -> ProductDisplayHelper.displayProductInfoIfProductFound(foundProduct)).thenAnswer(invocation -> null);
            mockedProductDisplayHelper.when(ProductDisplayHelper::displayOutOfStockMessage).thenAnswer(invocation -> null);
            mockedProductDisplayHelper.when(() -> ProductDisplayHelper.displayProductNotFoundMessage(anyString())).thenAnswer(invocation -> null);

            MenuOption1Sell menuOption1Sell = MenuOptionFactory.createMenuOption1Sell();
            Product resultProduct = menuOption1Sell.getProductData();

            assertEquals(foundProduct, resultProduct);
            mockedUserInputUtils.verify(() -> UserInputUtils.readFromUser("\nPlease enter the name of the product to be sold:"), times(1));
            mockedProductRepository.verify(() -> ProductRepository.findProductByName(inputProductName), times(1));
            mockedProductDisplayHelper.verify(() -> ProductDisplayHelper.displayProductInfoIfProductFound(foundProduct), times(1));
            mockedProductDisplayHelper.verify(ProductDisplayHelper::displayOutOfStockMessage, never());
            mockedProductDisplayHelper.verify(() -> ProductDisplayHelper.displayProductNotFoundMessage(anyString()), never());
        }
    }

    @Test
    void testGetProductData_ValidProductAndZeroQuantity() {
        String firstProductName = "coffee";
        String secondProductName = "cherry";
        Product firstProduct = new Product("coffee", "pr5204875", 1800, 0);
        Product secondProduct = new Product("cherry", "pr7860912", 452, 115);

        try (MockedStatic<UserInputUtils> mockedUserInputUtils = Mockito.mockStatic(UserInputUtils.class);
             MockedStatic<ProductRepository> mockedProductRepository = Mockito.mockStatic(ProductRepository.class);
             MockedStatic<ProductDisplayHelper> mockedProductDisplayHelper = Mockito.mockStatic(ProductDisplayHelper.class)) {

            mockedUserInputUtils.when(() -> UserInputUtils.readFromUser(anyString())).thenReturn(firstProductName).thenReturn(secondProductName);
            mockedProductRepository.when(() -> ProductRepository.findProductByName(firstProductName)).thenReturn(firstProduct);
            mockedProductDisplayHelper.when(() -> ProductDisplayHelper.displayProductInfoIfProductFound(firstProduct)).thenAnswer(invocation -> null);
            mockedProductDisplayHelper.when(ProductDisplayHelper::displayOutOfStockMessage).thenAnswer(invocation -> null);
            mockedProductRepository.when(() -> ProductRepository.findProductByName(secondProductName)).thenReturn(secondProduct);
            mockedProductDisplayHelper.when(() -> ProductDisplayHelper.displayProductInfoIfProductFound(secondProduct)).thenAnswer(invocation -> null);
            mockedProductDisplayHelper.when(() -> ProductDisplayHelper.displayProductNotFoundMessage(anyString())).thenAnswer(invocation -> null);

            MenuOption1Sell menuOption1Sell = MenuOptionFactory.createMenuOption1Sell();
            Product resultProduct = menuOption1Sell.getProductData();

            assertEquals(secondProduct, resultProduct);
            mockedUserInputUtils.verify(() -> UserInputUtils.readFromUser("\nPlease enter the name of the product to be sold:"), times(2));
            mockedProductRepository.verify(() -> ProductRepository.findProductByName(firstProductName), times(1));
            mockedProductDisplayHelper.verify(() -> ProductDisplayHelper.displayProductInfoIfProductFound(firstProduct), times(1));
            mockedProductDisplayHelper.verify(ProductDisplayHelper::displayOutOfStockMessage, times(1));
            mockedProductRepository.verify(() -> ProductRepository.findProductByName(secondProductName), times(1));
            mockedProductDisplayHelper.verify(() -> ProductDisplayHelper.displayProductInfoIfProductFound(secondProduct), times(1));
            mockedProductDisplayHelper.verify(() -> ProductDisplayHelper.displayProductNotFoundMessage(anyString()), never());
        }
    }

    @Test
    void testGetProductData_ProductNotFoundThenValidProduct() {
        String notFoundProductName = "beer";
        String foundProductName = "tea";
        Product foundProduct = new Product("tea", "pr7236284", 1200, 52);

        try (MockedStatic<UserInputUtils> mockedUserInputUtils = Mockito.mockStatic(UserInputUtils.class);
             MockedStatic<ProductRepository> mockedProductRepository = Mockito.mockStatic(ProductRepository.class);
             MockedStatic<ProductDisplayHelper> mockedProductDisplayHelper = Mockito.mockStatic(ProductDisplayHelper.class)) {

            mockedUserInputUtils.when(() -> UserInputUtils.readFromUser(anyString())).thenReturn(notFoundProductName).thenReturn(foundProductName);
            mockedProductRepository.when(() -> ProductRepository.findProductByName(notFoundProductName)).thenReturn(null);
            mockedProductDisplayHelper.when(() -> ProductDisplayHelper.displayProductInfoIfProductFound(any(Product.class))).thenAnswer(invocation -> null);
            mockedProductDisplayHelper.when(ProductDisplayHelper::displayOutOfStockMessage).thenAnswer(invocation -> null);
            mockedProductRepository.when(() -> ProductRepository.findProductByName(foundProductName)).thenReturn(foundProduct);
            mockedProductDisplayHelper.when(() -> ProductDisplayHelper.displayProductInfoIfProductFound(foundProduct)).thenAnswer(invocation -> null);
            mockedProductDisplayHelper.when(() -> ProductDisplayHelper.displayProductNotFoundMessage(anyString())).thenAnswer(invocation -> null);

            MenuOption1Sell menuOption1Sell = MenuOptionFactory.createMenuOption1Sell();
            Product resultProduct = menuOption1Sell.getProductData();

            assertEquals(foundProduct, resultProduct);
            mockedUserInputUtils.verify(() -> UserInputUtils.readFromUser("\nPlease enter the name of the product to be sold:"), times(2));
            mockedProductRepository.verify(() -> ProductRepository.findProductByName(notFoundProductName), times(1));
            mockedProductDisplayHelper.verify(() -> ProductDisplayHelper.displayProductNotFoundMessage(notFoundProductName), times(1));
            mockedProductRepository.verify(() -> ProductRepository.findProductByName(foundProductName), times(1));
            mockedProductDisplayHelper.verify(() -> ProductDisplayHelper.displayProductInfoIfProductFound(foundProduct), times(1));
            mockedProductDisplayHelper.verify(ProductDisplayHelper::displayOutOfStockMessage, never());
        }
    }

    @Test
    void testGetQuantitySold_ValidQuantityFirstAttempt() {
        int quantitySold = 4;
        Product product = new Product("cocoa", "pr7553549", 235, 35);

        try (MockedStatic<ErrorHandler> mockedErrorHandler = Mockito.mockStatic(ErrorHandler.class);
             MockedStatic<ProductDisplayHelper> mockedProductDisplayHelper = Mockito.mockStatic(ProductDisplayHelper.class)) {

            mockedErrorHandler.when(() -> ErrorHandler.getValidNumber(anyString())).thenReturn(quantitySold);
            mockedProductDisplayHelper.when(() -> ProductDisplayHelper.displayProductQuantityErrorMessage(any(Product.class), anyInt(), anyInt())).thenAnswer(invocation -> null);

            MenuOption1Sell menuOption1Sell = MenuOptionFactory.createMenuOption1Sell();
            int resultQuantitySold = menuOption1Sell.getQuantitySold(product);

            assertEquals(quantitySold, resultQuantitySold);
            mockedErrorHandler.verify(() -> ErrorHandler.getValidNumber("\nPlease enter the quantity to be sold:"), times(1));
            mockedProductDisplayHelper.verify(() -> ProductDisplayHelper.displayProductQuantityErrorMessage(product, 35, quantitySold), times(1));
        }
    }

    @Test
    void testGetQuantitySold_InvalidThenValidQuantity() {
        int invalidQuantitySold = 52;
        int validQuantitySold = 5;
        Product product = new Product("lemon", "pr6634365", 880, 35);

        try (MockedStatic<ErrorHandler> mockedErrorHandler = Mockito.mockStatic(ErrorHandler.class);
             MockedStatic<ProductDisplayHelper> mockedProductDisplayHelper = Mockito.mockStatic(ProductDisplayHelper.class)) {

            mockedErrorHandler.when(() -> ErrorHandler.getValidNumber(anyString())).thenReturn(invalidQuantitySold).thenReturn(validQuantitySold);
            mockedProductDisplayHelper.when(() -> ProductDisplayHelper.displayProductQuantityErrorMessage(any(Product.class), anyInt(), anyInt())).thenAnswer(invocation -> null);

            MenuOption1Sell menuOption1Sell = MenuOptionFactory.createMenuOption1Sell();
            int resultQuantitySold = menuOption1Sell.getQuantitySold(product);

            assertEquals(validQuantitySold, resultQuantitySold);
            mockedErrorHandler.verify(() -> ErrorHandler.getValidNumber("\nPlease enter the quantity to be sold:"), times(2));
            mockedProductDisplayHelper.verify(() -> ProductDisplayHelper.displayProductQuantityErrorMessage(product, 35, invalidQuantitySold), times(1));
            mockedProductDisplayHelper.verify(() -> ProductDisplayHelper.displayProductQuantityErrorMessage(product, 35, validQuantitySold), times(1));
        }
    }

    @Test
    void testIsCustomerRegistered_CustomerExists() {
        String customerName = "Tamasi Tamara";
        Customer foundCustomer = new Customer("Tamasi Tamara", "cID8448077", 4480);

        try (MockedStatic<CustomerRepository> mockedCustomerRepository = Mockito.mockStatic(CustomerRepository.class)) {
            mockedCustomerRepository.when(() -> CustomerRepository.findCustomerByName(anyString())).thenReturn(foundCustomer);

            MenuOption1Sell menuOption1Sell = MenuOptionFactory.createMenuOption1Sell();
            boolean isCustomerRegistered = menuOption1Sell.isCustomerRegistered(customerName);

            assertTrue(isCustomerRegistered);
            mockedCustomerRepository.verify(() -> CustomerRepository.findCustomerByName(customerName), times(1));
        }
    }

    @Test
    void testIsCustomerRegistered_CustomerDoesNotExists() {
        String customerName = "Kiss Emese";

        try (MockedStatic<CustomerRepository> mockedCustomerRepository = Mockito.mockStatic(CustomerRepository.class)) {
            mockedCustomerRepository.when(() -> CustomerRepository.findCustomerByName(anyString())).thenReturn(null);

            MenuOption1Sell menuOption1Sell = MenuOptionFactory.createMenuOption1Sell();
            boolean isCustomerRegistered = menuOption1Sell.isCustomerRegistered(customerName);

            assertFalse(isCustomerRegistered);
            mockedCustomerRepository.verify(() -> CustomerRepository.findCustomerByName(customerName), times(1));
        }
    }
}
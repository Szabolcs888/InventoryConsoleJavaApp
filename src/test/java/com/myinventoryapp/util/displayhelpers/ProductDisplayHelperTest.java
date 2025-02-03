package com.myinventoryapp.util.displayhelpers;

import com.myinventoryapp.entities.Product;
import com.myinventoryapp.repository.ProductRepository;
import com.myinventoryapp.util.testutils.TestUtils;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;

class ProductDisplayHelperTest {
    private final String menuMessage = "\n-DISPLAY AVAILABLE PRODUCTS MENU-\n";

    @Test
    void testDisplayProductList_PrintsMenuMessage() {
        ByteArrayOutputStream outputStream = TestUtils.redirectSystemOut();
        List<Product> productList = Collections.emptyList();

        ProductDisplayHelper.displayProductList(productList, menuMessage);

        String expectedMessage = "-DISPLAY AVAILABLE PRODUCTS MENU-";
        String output = outputStream.toString();
        assertTrue(output.contains(expectedMessage),
                "Expected message '" + expectedMessage + "' was not found in the output.");
        TestUtils.restoreSystemOut();
    }

    @Test
    void testDisplayProductList_PrintsEmptyListMessage() {
        ByteArrayOutputStream outputStream = TestUtils.redirectSystemOut();
        List<Product> productList = Collections.emptyList();

        ProductDisplayHelper.displayProductList(productList, menuMessage);

        String expectedMessage = "There are currently no products in the inventory!";
        String output = outputStream.toString();
        assertTrue(output.contains(expectedMessage),
                "Expected message '" + expectedMessage + "' was not found in the output.");
        TestUtils.restoreSystemOut();
    }

    @Test
    void testDisplayProductList_PrintsNotEmptyListMessages() {
        ByteArrayOutputStream outputStream = TestUtils.redirectSystemOut();
        List<Product> productList = Arrays.asList(
                new Product("apple", "pr5197140", 560, 0),
                new Product("pear", "pr4270613", 675, 17),
                new Product("banana", "pr5223508", 720, 99)
        );

        ProductDisplayHelper.displayProductList(productList, menuMessage);

        String expectedMessage = "There are a total of " + productList.size() + " items in the inventory:";
        String output = outputStream.toString();
        assertTrue(output.contains(expectedMessage),
                "Expected message '" + expectedMessage + "' was not found in the output.");
        for (Product product : productList) {
            assertTrue(output.contains("Name: " + product.getProductName() + ", Price: " + product.getUnitPrice() +
                    " HUF, Quantity: " + product.getQuantity() + ", Product ID: " + product.getProductId()));
        }
        TestUtils.restoreSystemOut();
    }

    @Test
    void testDisplayProductInfo_PrintsMessages() {
        ByteArrayOutputStream outputStream = TestUtils.redirectSystemOut();

        ProductDisplayHelper.displayProductInfo(
                "DETAILS OF THE PRODUCT NAMED ", "tea",
                "pr7236284", 1200, 52);

        String expectedProductInfoText = "DETAILS OF THE PRODUCT NAMED ";
        String output = outputStream.toString();
        assertTrue(output.contains(expectedProductInfoText),
                "Expected message '" + expectedProductInfoText + "' was not found in the output.");
        String expectedProductDataMessage = "Product name: tea, Product ID: pr7236284, Unit price: 1200 HUF, Available quantity: 52";
        assertTrue(output.contains(expectedProductDataMessage),
                "Expected message '" + expectedProductDataMessage + "' was not found in the output.");
        TestUtils.restoreSystemOut();
    }

    @Test
    void testDisplayProductInfoAfterGoodsReceipt_PrintsMessages() {
        ByteArrayOutputStream outputStream = TestUtils.redirectSystemOut();
        Product newProduct = new Product("coffee", "pr5204875", 1800, 44);

        ProductDisplayHelper.displayProductInfoAfterGoodsReceipt(newProduct);

        String expectedProductInfoText = "THE PRODUCT HAS BEEN ADDED TO THE INVENTORY:";
        String output = outputStream.toString();
        assertTrue(output.contains(expectedProductInfoText),
                "Expected message '" + expectedProductInfoText + "' was not found in the output.");
        String expectedProductDataMessage = "Product name: coffee\n" +
                "Product ID: pr5204875\n" +
                "Unit price: 1800 HUF\n" +
                "Available quantity: 44";
        assertTrue(output.contains(expectedProductDataMessage),
                "Expected message '" + expectedProductDataMessage + "' was not found in the output.");
        TestUtils.restoreSystemOut();
    }

    @Test
    void testDisplayProductNotFoundMessage_PrintsMessages() {
        ByteArrayOutputStream outputStream = TestUtils.redirectSystemOut();
        List<Product> productList = Arrays.asList(
                new Product("orange juice", "pr7987615", 870, 89),
                new Product("pineapple", "pr5711807", 894, 1),
                new Product("cherry", "pr7860912", 452, 115)
        );

        try (MockedStatic<ProductRepository> mockedProductRepository = Mockito.mockStatic(ProductRepository.class)) {
            mockedProductRepository.when(ProductRepository::getProductList).thenReturn(productList);

            ProductDisplayHelper.displayProductNotFoundMessage("red wine");

            String expectedWarningText =
                    "The product named \"red wine\" is not in the inventory, please choose another product:";
            String output = outputStream.toString();
            assertTrue(output.contains(expectedWarningText),
                    "Expected message '" + expectedWarningText + "' was not found in the output.");
            String expectedListedAvailableProducts = "orange juice, pineapple, cherry";
            assertTrue(output.contains(expectedListedAvailableProducts),
                    "Expected message '" + expectedListedAvailableProducts + "' was not found in the output.");
        } finally {
            TestUtils.restoreSystemOut();
        }
    }

    @Test
    void testDisplayProductQuantityErrorMessage_PrintsMessage_QuantitySoldMoreThanAvailableQuantity() {
        ByteArrayOutputStream outputStream = TestUtils.redirectSystemOut();
        int quantity = 44;
        int quantitySold = 45;
        Product product = new Product("coffee", "pr5204875", 1800, quantity);

        ProductDisplayHelper.displayProductQuantityErrorMessage(product, quantitySold);

        String expectedWarningText =
                "There are a total of " + quantity + " coffee in stock, you cannot sell more than that!";
        String output = outputStream.toString();
        assertTrue(output.contains(expectedWarningText),
                "Expected message '" + expectedWarningText + "' was not found in the output.");
        TestUtils.restoreSystemOut();
    }

    @Test
    void testDisplayProductQuantityErrorMessage_PrintsMessage_ZeroQuantitySold() {
        ByteArrayOutputStream outputStream = TestUtils.redirectSystemOut();
        int quantity = 89;
        int quantitySold = 0;
        Product product = new Product("orange juice", "pr7987615", 870, quantity);

        ProductDisplayHelper.displayProductQuantityErrorMessage(product, quantitySold);

        String expectedWarningText =
                "The quantity sold must be at least 1!";
        String output = outputStream.toString();
        assertTrue(output.contains(expectedWarningText),
                "Expected message '" + expectedWarningText + "' was not found in the output.");
        TestUtils.restoreSystemOut();
    }

    @Test
    void testDisplayProductInfoIfProductFound() {
        Product product = new Product("pineapple", "pr5711807", 894, 35);

        try (MockedStatic<ProductDisplayHelper> mockedProductDisplayHelper = Mockito.mockStatic(ProductDisplayHelper.class)) {

            mockedProductDisplayHelper.when(() ->
                    ProductDisplayHelper.displayProductInfoIfProductFound(any(Product.class))).thenCallRealMethod();
            mockedProductDisplayHelper.when(() ->
                            ProductDisplayHelper.displayProductInfo(anyString(), anyString(), anyString(), anyInt(), anyInt()))
                    .thenAnswer(invocation -> null);

            ProductDisplayHelper.displayProductInfoIfProductFound(product);

            mockedProductDisplayHelper.verify(() ->
                            ProductDisplayHelper.displayProductInfo(
                                    "DETAILS OF THE PRODUCT NAMED " + product.getProductName() + ":",
                                    product.getProductName(), product.getProductId(), product.getUnitPrice(), product.getQuantity()
                            ),
                    times(1)
            );
        }
    }

    @Test
    void testDisplayProductInfoAfterSellAndUpdateGoodsReceipt() {
        Product product = new Product("cherry", "pr7860912", 452, 115);
        String productInfoText = "PRODUCT DATA AFTER THE TRANSACTION:";

        try (MockedStatic<ProductDisplayHelper> mockedProductDisplayHelper = Mockito.mockStatic(ProductDisplayHelper.class)) {
            mockedProductDisplayHelper.when(() ->
                    ProductDisplayHelper.displayProductInfoAfterSellAndUpdateGoodsReceipt(any(Product.class), anyString())).thenCallRealMethod();
            mockedProductDisplayHelper.when(() ->
                            ProductDisplayHelper.displayProductInfo(anyString(), anyString(), anyString(), anyInt(), anyInt()))
                    .thenAnswer(invocation -> null);

            ProductDisplayHelper.displayProductInfoAfterSellAndUpdateGoodsReceipt(product, productInfoText);

            mockedProductDisplayHelper.verify(() ->
                            ProductDisplayHelper.displayProductInfo(
                                    productInfoText, product.getProductName(), product.getProductId(),
                                    product.getUnitPrice(), product.getQuantity()
                            ),
                    times(1)
            );
        }
    }

    @Test
    void testDisplayExistingProductInfo() {
        Product product = new Product("mango", "pr4531265", 1350, 46);

        try (MockedStatic<ProductDisplayHelper> mockedProductDisplayHelper = Mockito.mockStatic(ProductDisplayHelper.class)) {
            mockedProductDisplayHelper.when(() ->
                    ProductDisplayHelper.displayExistingProductInfo(any(Product.class))).thenCallRealMethod();
            mockedProductDisplayHelper.when(() ->
                            ProductDisplayHelper.displayProductInfo(anyString(), anyString(), anyString(), anyInt(), anyInt()))
                    .thenAnswer(invocation -> null);

            ProductDisplayHelper.displayExistingProductInfo(product);

            mockedProductDisplayHelper.verify(() ->
                            ProductDisplayHelper.displayProductInfo(
                                    "THE PRODUCT NAMED " + product.getProductName() + " IS ALREADY IN THE INVENTORY. DETAILS:",
                                    product.getProductName(), product.getProductId(), product.getUnitPrice(), product.getQuantity()
                            ),
                    times(1)
            );
        }
    }

    @Test
    void testDisplayOutOfStockMessage_PrintsMessage() {
        ByteArrayOutputStream outputStream = TestUtils.redirectSystemOut();

        ProductDisplayHelper.displayOutOfStockMessage();

        String expectedMessage = "The product is in the inventory but currently out of stock. Please choose another!";
        String output = outputStream.toString();
        assertTrue(output.contains(expectedMessage),
                "Expected message '" + expectedMessage + "' was not found in the output.");

        TestUtils.restoreSystemOut();
    }

    @Test
    void testDisplayNoProductsAvailableMessage_PrintsMessage() {
        ByteArrayOutputStream outputStream = TestUtils.redirectSystemOut();

        ProductDisplayHelper.displayNoProductsAvailableMessage();

        String expectedMessage = "There are currently no products available for sale!";
        String output = outputStream.toString();
        assertTrue(output.contains(expectedMessage),
                "Expected message '" + expectedMessage + "' was not found in the output.");

        TestUtils.restoreSystemOut();
    }
}
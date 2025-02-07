package com.myinventoryapp.util.displayhelpers;

import com.myinventoryapp.entities.Product;
import com.myinventoryapp.repository.ProductRepository;
import com.myinventoryapp.util.testutils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductDisplayHelperTest {
    private static final String MENU_MESSAGE = "\n-DISPLAY AVAILABLE PRODUCTS MENU-\n";

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
    void testDisplayProductList_PrintsMenuMessage() {
        List<Product> productList = Collections.emptyList();

        ProductDisplayHelper.displayProductList(productList, MENU_MESSAGE);

        String expectedMessage = "-DISPLAY AVAILABLE PRODUCTS MENU-";
        String output = outputStream.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains(expectedMessage),
                "Expected message '" + expectedMessage + "' was not found in the output.");
    }

    @Test
    void testDisplayProductList_PrintsEmptyListMessage() {
        List<Product> productList = Collections.emptyList();

        ProductDisplayHelper.displayProductList(productList, MENU_MESSAGE);

        String expectedMessage = "There are currently no products in the inventory!";
        String output = outputStream.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains(expectedMessage),
                "Expected message '" + expectedMessage + "' was not found in the output.");
    }

    @Test
    void testDisplayProductList_PrintsNotEmptyListMessages() {
        List<Product> productList = Arrays.asList(
                new Product("apple", "pr5197140", 560, 0),
                new Product("pear", "pr4270613", 675, 17),
                new Product("banana", "pr5223508", 720, 99)
        );

        ProductDisplayHelper.displayProductList(productList, MENU_MESSAGE);

        String expectedMessage = "There are a total of " + productList.size() + " items in the inventory:";
        String output = outputStream.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains(expectedMessage),
                "Expected message '" + expectedMessage + "' was not found in the output.");
        for (Product product : productList) {
            assertTrue(output.contains("Name: " + product.getProductName() + ", Price: " + product.getUnitPrice() +
                    " HUF, Quantity: " + product.getQuantity() + ", Product ID: " + product.getProductId()));
        }
    }

    @Test
    void testDisplayProductInfoAfterGoodsReceipt_PrintsMessages() {
        Product newProduct = new Product("coffee", "pr5204875", 1800, 44);

        ProductDisplayHelper.displayProductInfoAfterGoodsReceipt(newProduct);

        String expectedProductInfoMessage = "THE PRODUCT HAS BEEN ADDED TO THE INVENTORY:";
        String output = outputStream.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains(expectedProductInfoMessage),
                "Expected message '" + expectedProductInfoMessage + "' was not found in the output.");
        String expectedProductDataMessage = "Product name: coffee\n" +
                "Product ID: pr5204875\n" +
                "Unit price: 1800 HUF\n" +
                "Available quantity: 44";
        assertTrue(output.contains(expectedProductDataMessage),
                "Expected message '" + expectedProductDataMessage + "' was not found in the output.");
    }

    @Test
    void testDisplayProductNotFoundMessage_PrintsMessages() {
        List<Product> productList = Arrays.asList(
                new Product("orange juice", "pr7987615", 870, 89),
                new Product("pineapple", "pr5711807", 894, 1),
                new Product("cherry", "pr7860912", 452, 115)
        );

        try (MockedStatic<ProductRepository> mockedProductRepository = Mockito.mockStatic(ProductRepository.class)) {
            mockedProductRepository.when(ProductRepository::getProductList).thenReturn(productList);

            ProductDisplayHelper.displayProductNotFoundMessage("red wine");

            String expectedWarningMessage =
                    "The product named \"red wine\" is not in the inventory, please choose another product:";
            String output = outputStream.toString(StandardCharsets.UTF_8);
            assertTrue(output.contains(expectedWarningMessage),
                    "Expected message '" + expectedWarningMessage + "' was not found in the output.");
            String expectedListedAvailableProducts = "orange juice, pineapple, cherry";
            assertTrue(output.contains(expectedListedAvailableProducts),
                    "Expected message '" + expectedListedAvailableProducts + "' was not found in the output.");
        }
    }

    @Test
    void testDisplayProductQuantityErrorMessage_PrintsMessage_QuantitySoldMoreThanAvailableQuantity() {
        int quantity = 44;
        int quantitySold = 45;
        Product product = new Product("coffee", "pr5204875", 1800, quantity);

        ProductDisplayHelper.displayProductQuantityErrorMessage(product, quantitySold);

        String expectedWarningMessage =
                "There are a total of " + quantity + " coffee in stock, you cannot sell more than that!";
        String output = outputStream.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains(expectedWarningMessage),
                "Expected message '" + expectedWarningMessage + "' was not found in the output.");
    }

    @Test
    void testDisplayProductQuantityErrorMessage_PrintsMessage_ZeroQuantitySold() {
        int quantity = 89;
        int quantitySold = 0;
        Product product = new Product("orange juice", "pr7987615", 870, quantity);

        ProductDisplayHelper.displayProductQuantityErrorMessage(product, quantitySold);

        String expectedWarningMessage =
                "The quantity sold must be at least 1!";
        String output = outputStream.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains(expectedWarningMessage),
                "Expected message '" + expectedWarningMessage + "' was not found in the output.");
    }

    @Test
    void testDisplayProductInfoIfProductFound_AndDisplayProductInfo() {
        Product product = new Product("pineapple", "pr5711807", 894, 35);

        ProductDisplayHelper.displayProductInfoIfProductFound(product);

        String expectedProductInfoMessage = "DETAILS OF THE PRODUCT NAMED ";
        String output = outputStream.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains(expectedProductInfoMessage),
                "Expected message '" + expectedProductInfoMessage + "' was not found in the output.");
        String expectedProductDataMessage = "Product name: pineapple, Product ID: pr5711807, Unit price: 894 HUF, Available quantity: 35";
        assertTrue(output.contains(expectedProductDataMessage),
                "Expected message '" + expectedProductDataMessage + "' was not found in the output.");
    }

    @Test
    void testDisplayProductInfoAfterSellAndUpdateGoodsReceipt_AndDisplayProductInfo() {
        Product product = new Product("cherry", "pr7860912", 452, 115);
        String productInfoMessage = "PRODUCT DATA AFTER THE TRANSACTION:";

        ProductDisplayHelper.displayProductInfoAfterSellAndUpdateGoodsReceipt(product, productInfoMessage);

        String expectedProductInfoMessage = "PRODUCT DATA AFTER THE TRANSACTION:";
        String output = outputStream.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains(expectedProductInfoMessage),
                "Expected message '" + expectedProductInfoMessage + "' was not found in the output.");
        String expectedProductDataMessage = "Product name: cherry, Product ID: pr7860912, Unit price: 452 HUF, Available quantity: 115";
        assertTrue(output.contains(expectedProductDataMessage),
                "Expected message '" + expectedProductDataMessage + "' was not found in the output.");
    }

    @Test
    void testDisplayExistingProductInfo_AndDisplayProductInfo() {
        Product product = new Product("mango", "pr4531265", 1350, 46);

        ProductDisplayHelper.displayExistingProductInfo(product);

        String expectedProductInfoMessage = "THE PRODUCT NAMED mango IS ALREADY IN THE INVENTORY. DETAILS:";
        String output = outputStream.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains(expectedProductInfoMessage),
                "Expected message '" + expectedProductInfoMessage + "' was not found in the output.");
        String expectedProductDataMessage = "Product name: mango, Product ID: pr4531265, Unit price: 1350 HUF, Available quantity: 46";
        assertTrue(output.contains(expectedProductDataMessage),
                "Expected message '" + expectedProductDataMessage + "' was not found in the output.");
    }

    @Test
    void testDisplayOutOfStockMessage_PrintsMessage() {

        ProductDisplayHelper.displayOutOfStockMessage();

        String expectedMessage = "The product is in the inventory but currently out of stock. Please choose another!";
        String output = outputStream.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains(expectedMessage),
                "Expected message '" + expectedMessage + "' was not found in the output.");
    }

    @Test
    void testDisplayNoProductsAvailableMessage_PrintsMessage() {

        ProductDisplayHelper.displayNoProductsAvailableMessage();

        String expectedMessage = "There are currently no products available for sale!";
        String output = outputStream.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains(expectedMessage),
                "Expected message '" + expectedMessage + "' was not found in the output.");
    }
}
package com.myinventoryapp.services;

import com.myinventoryapp.entities.Product;
import com.myinventoryapp.repository.ProductRepository;
import com.myinventoryapp.util.ErrorHandler;
import com.myinventoryapp.util.IdUtils;
import com.myinventoryapp.util.displayhelpers.ProductDisplayHelper;
import com.myinventoryapp.util.testutils.TestUtils;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

class ProductServiceTest {

    @Test
    void testAddNewProduct_CallsGenerateIdOnce() {
        String productName = "apple";
        int unitPrice = 560;
        int quantity = 25;

        try (MockedStatic<IdUtils> mockedIdUtils = Mockito.mockStatic(IdUtils.class);
             MockedStatic<ProductRepository> mockedProductRepository = Mockito.mockStatic(ProductRepository.class);
             MockedStatic<ProductDisplayHelper> mockedProductDisplayHelper = Mockito.mockStatic(ProductDisplayHelper.class)) {

            mockedIdUtils.when(IdUtils::generateId).thenReturn(5204875);
            mockedProductRepository.when(() -> ProductRepository.addProduct(any(Product.class))).
                    thenAnswer(invocation -> null);
            mockedProductDisplayHelper.when(() ->
                    ProductDisplayHelper.displayProductInfoAfterGoodsReceipt(any(Product.class))).thenAnswer(invocation -> null);

            ProductService productService = new ProductService();
            productService.addNewProduct(productName, unitPrice, quantity);

            mockedIdUtils.verify(IdUtils::generateId, times(1));
        }
    }

    @Test
    void testAddNewProduct_AddsProductToRepository() {
        String productName = "pear";
        int unitPrice = 675;
        int quantity = 19;

        try (MockedStatic<IdUtils> mockedIdUtils = Mockito.mockStatic(IdUtils.class);
             MockedStatic<ProductRepository> mockedProductRepository = Mockito.mockStatic(ProductRepository.class);
             MockedStatic<ProductDisplayHelper> mockedProductDisplayHelper = Mockito.mockStatic(ProductDisplayHelper.class)) {

            mockedIdUtils.when(IdUtils::generateId).thenReturn(7825102);
            mockedProductRepository.when(() ->
                    ProductRepository.addProduct(any(Product.class))).thenAnswer(invocation -> null);
            mockedProductDisplayHelper.when(() ->
                    ProductDisplayHelper.displayProductInfoAfterGoodsReceipt(any(Product.class))).thenAnswer(invocation -> null);

            ProductService productService = new ProductService();
            productService.addNewProduct(productName, unitPrice, quantity);

            String expectedProductId = "pr7825102";
            mockedProductRepository.verify(() -> ProductRepository.addProduct(
                    argThat(product ->
                            product.getProductName().equals(productName) &&
                                    product.getProductId().equals(expectedProductId) &&
                                    product.getUnitPrice() == unitPrice &&
                                    product.getQuantity() == quantity
                    )
            ), times(1));
        }
    }

    @Test
    void testAddNewProduct_DisplaysProductInfo() {
        String productName = "banana";
        int unitPrice = 720;
        int quantity = 102;

        try (MockedStatic<IdUtils> mockedIdUtils = Mockito.mockStatic(IdUtils.class);
             MockedStatic<ProductRepository> mockedProductRepository = Mockito.mockStatic(ProductRepository.class);
             MockedStatic<ProductDisplayHelper> mockedProductDisplayHelper = Mockito.mockStatic(ProductDisplayHelper.class)) {

            mockedIdUtils.when(IdUtils::generateId).thenReturn(5223508);
            mockedProductRepository.when(() ->
                    ProductRepository.addProduct(any(Product.class))).thenAnswer(invocation -> null);
            mockedProductDisplayHelper.when(() ->
                    ProductDisplayHelper.displayProductInfoAfterGoodsReceipt(any(Product.class))).thenAnswer(invocation -> null);

            ProductService productService = new ProductService();
            productService.addNewProduct(productName, unitPrice, quantity);

            String expectedProductId = "pr5223508";
            mockedProductDisplayHelper.verify(() -> ProductDisplayHelper.displayProductInfoAfterGoodsReceipt(
                    argThat(product ->
                            product.getProductName().equals(productName) &&
                                    product.getProductId().equals(expectedProductId) &&
                                    product.getUnitPrice() == unitPrice &&
                                    product.getQuantity() == quantity
                    )
            ), times(1));
        }
    }

    @Test
    void testDeleteProduct_ConfirmsDeletion() {
        TestUtils.redirectSystemOut();
        String productName = "tea";
        Product product = new Product(productName, "pr7236284", 1200, 52);

        try (MockedStatic<ErrorHandler> mockedErrorHandler = Mockito.mockStatic(ErrorHandler.class);
             MockedStatic<ProductRepository> mockedProductRepository = Mockito.mockStatic(ProductRepository.class)) {

            mockedErrorHandler.when(() -> ErrorHandler.getYesOrNoAnswer(anyString())).thenReturn("Y");
            mockedProductRepository.when(() ->
                    ProductRepository.deleteProduct(product)).thenAnswer(invocation -> null);

            ProductService productService = new ProductService();
            productService.deleteProduct(product);

            mockedErrorHandler.verify(() -> ErrorHandler.getYesOrNoAnswer(
                            contains("Are you sure you want to DELETE the product named " + productName)),
                    times(1));

            mockedProductRepository.verify(() -> ProductRepository.deleteProduct(product), times(1));
        } finally {
            TestUtils.restoreSystemOut();
        }
    }

    @Test
    void testDeleteProduct_DoesNotDeleteWhenNotConfirmed() {
        TestUtils.redirectSystemOut();
        String productName = "coffee";
        Product product = new Product(productName, "pr5204875", 1800, 120);

        try (MockedStatic<ErrorHandler> mockedErrorHandler = Mockito.mockStatic(ErrorHandler.class);
             MockedStatic<ProductRepository> mockedProductRepository = Mockito.mockStatic(ProductRepository.class)) {

            mockedErrorHandler.when(() -> ErrorHandler.getYesOrNoAnswer(anyString())).thenReturn("N");
            mockedProductRepository.when(() ->
                    ProductRepository.deleteProduct(product)).thenAnswer(invocation -> null);

            ProductService productService = new ProductService();
            productService.deleteProduct(product);

            mockedErrorHandler.verify(() -> ErrorHandler.getYesOrNoAnswer(
                            contains("Are you sure you want to DELETE the product named " + productName)),
                    times(1));
            mockedProductRepository.verify(() -> ProductRepository.deleteProduct(product), never());
        } finally {
            TestUtils.restoreSystemOut();
        }
    }

    @Test
    void testDeleteProduct_DeletesAndDisplaysConfirmationMessage() {
        ByteArrayOutputStream outputStream = TestUtils.redirectSystemOut();
        Product product = new Product("orange juice", "pr7987615", 870, 89);

        try (MockedStatic<ErrorHandler> mockedErrorHandler = Mockito.mockStatic(ErrorHandler.class);
             MockedStatic<ProductRepository> mockedProductRepository = Mockito.mockStatic(ProductRepository.class)) {

            mockedErrorHandler.when(() -> ErrorHandler.getYesOrNoAnswer(anyString())).thenReturn("Y");
            mockedProductRepository.when(() ->
                    ProductRepository.deleteProduct(product)).thenAnswer(invocation -> null);

            ProductService productService = new ProductService();
            productService.deleteProduct(product);

            String expectedAfterDeletingMessage = "\nThe item has been deleted!";
            String output = outputStream.toString(StandardCharsets.UTF_8);
            assertTrue(output.contains(expectedAfterDeletingMessage),
                    "Expected message '" + expectedAfterDeletingMessage + "' was not found in the output.");
        } finally {
            TestUtils.restoreSystemOut();
        }
    }

    @Test
    void testDeleteProduct_AbortsAndDisplaysNoDeletionMessage() {
        ByteArrayOutputStream outputStream = TestUtils.redirectSystemOut();
        Product product = new Product("pineapple", "pr5711807", 894, 1);

        try (MockedStatic<ErrorHandler> mockedErrorHandler = Mockito.mockStatic(ErrorHandler.class);
             MockedStatic<ProductRepository> mockedProductRepository = Mockito.mockStatic(ProductRepository.class)) {

            mockedErrorHandler.when(() -> ErrorHandler.getYesOrNoAnswer(anyString())).thenReturn("N");
            mockedProductRepository.when(() ->
                    ProductRepository.deleteProduct(product)).thenAnswer(invocation -> null);

            ProductService productService = new ProductService();
            productService.deleteProduct(product);

            String unexpectedAfterDeletingMessage = "\nThe item has been deleted!";
            String output = outputStream.toString(StandardCharsets.UTF_8);
            assertFalse(output.contains(unexpectedAfterDeletingMessage),
                    "Expected message '" + unexpectedAfterDeletingMessage + "' found in the output.");
        } finally {
            TestUtils.restoreSystemOut();
        }
    }

    @Test
    void testUpdateProductQuantity_Product_int() {
        int quantity = 115;
        int quantitySold = 5;
        Product product = new Product("cherry", "pr7860912", 452, quantity);

        ProductService productService = new ProductService();
        productService.updateProductQuantity(product, quantitySold);

        int expectedQuantity = quantity - quantitySold;
        assertEquals(expectedQuantity, product.getQuantity(), "Product quantity was not updated correctly.");
    }

    @Test
    void testUpdateProductQuantity_Product_SuccessfulUpdate() {
        int quantity = 46;
        Product product = new Product("mango", "pr4531265", 1350, quantity);

        int quantityModification = 15;
        int newQuantity = quantity + quantityModification;

        try (MockedStatic<ErrorHandler> mockedErrorHandler = Mockito.mockStatic(ErrorHandler.class);
             MockedStatic<ProductDisplayHelper> mockedProductDisplayHelper = Mockito.mockStatic(ProductDisplayHelper.class)) {
            ProductService spyProductService = Mockito.spy(new ProductService());

            Mockito.doReturn(quantityModification).when(spyProductService).getQuantityModification(product);
            Mockito.doReturn(newQuantity).when(spyProductService).calculateNewQuantity(product, quantityModification);
            mockedErrorHandler.when(() -> ErrorHandler.validateNonNegativeQuantity(newQuantity)).thenAnswer(invocation -> null);
            Mockito.doNothing().when(spyProductService).setNewQuantity(product, newQuantity);
            mockedProductDisplayHelper.when(() -> ProductDisplayHelper.displayProductInfoAfterSellAndUpdateGoodsReceipt(
                    any(Product.class), anyString())).thenAnswer(invocation -> null);

            spyProductService.updateProductQuantity(product);

            Mockito.verify(spyProductService, times(1)).getQuantityModification(product);
            Mockito.verify(spyProductService, times(1)).calculateNewQuantity(product, quantityModification);
            mockedErrorHandler.verify(() -> ErrorHandler.validateNonNegativeQuantity(newQuantity), times(1));
            Mockito.verify(spyProductService, times(1)).setNewQuantity(product, newQuantity);
            mockedProductDisplayHelper.verify(() -> ProductDisplayHelper.displayProductInfoAfterSellAndUpdateGoodsReceipt(
                    product, "PRODUCT INFORMATION AFTER RECEIPT TRANSACTION:"), times(1));
        }
    }

    @Test
    void testUpdateProductQuantity_Product_RetryOnNegativeQuantity() {
        int quantity = 31;
        Product product = new Product("carrot", "pr2110710", 245, quantity);

        int negativeQuantityModification = -47;
        int positiveQuantityModification = 35;
        int newQuantity1 = quantity + negativeQuantityModification;
        int newQuantity2 = quantity + positiveQuantityModification;

        try (MockedStatic<ErrorHandler> mockedErrorHandler = Mockito.mockStatic(ErrorHandler.class);
             MockedStatic<ProductDisplayHelper> mockedProductDisplayHelper = Mockito.mockStatic(ProductDisplayHelper.class)) {
            ProductService spyProductService = Mockito.spy(new ProductService());

            Mockito.doReturn(negativeQuantityModification, positiveQuantityModification)
                    .when(spyProductService).getQuantityModification(product);
            Mockito.doReturn(newQuantity1).when(spyProductService).calculateNewQuantity(product, negativeQuantityModification);
            mockedErrorHandler.when(() -> ErrorHandler.validateNonNegativeQuantity(anyInt())).thenAnswer(invocation -> null);
            Mockito.doNothing().when(spyProductService).setNewQuantity(product, newQuantity1);
            Mockito.doReturn(newQuantity2).when(spyProductService).calculateNewQuantity(product, positiveQuantityModification);
            Mockito.doNothing().when(spyProductService).setNewQuantity(product, newQuantity2);
            mockedProductDisplayHelper.when(() -> ProductDisplayHelper.displayProductInfoAfterSellAndUpdateGoodsReceipt(
                    any(Product.class), anyString())).thenAnswer(invocation -> null);

            spyProductService.updateProductQuantity(product);

            // There are so many verifications because the test runs two iterations.
            Mockito.verify(spyProductService, times(2)).getQuantityModification(product);
            Mockito.verify(spyProductService, times(1)).calculateNewQuantity(product, negativeQuantityModification);
            mockedErrorHandler.verify(() -> ErrorHandler.validateNonNegativeQuantity(newQuantity1), times(1));
            Mockito.verify(spyProductService, times(0)).setNewQuantity(product, newQuantity1);
            Mockito.verify(spyProductService, times(1)).calculateNewQuantity(product, positiveQuantityModification);
            mockedErrorHandler.verify(() -> ErrorHandler.validateNonNegativeQuantity(newQuantity2), times(1));
            Mockito.verify(spyProductService, times(1)).setNewQuantity(product, newQuantity2);
            mockedProductDisplayHelper.verify(() -> ProductDisplayHelper.displayProductInfoAfterSellAndUpdateGoodsReceipt(
                    product, "PRODUCT INFORMATION AFTER RECEIPT TRANSACTION:"), times(1));
        }
    }

    @Test
    void testGetQuantityModification_ProductQuantityZero() {
        String productName = "lemon";
        Product product = new Product(productName, "pr6634365", 880, 0);

        int quantityModification = 19;

        try (MockedStatic<ErrorHandler> mockedErrorHandler = Mockito.mockStatic(ErrorHandler.class)) {
            mockedErrorHandler.when(() -> ErrorHandler.getValidNumber(anyString())).thenReturn(quantityModification);

            ProductService productService = new ProductService();
            int quantityToBeModifiedResult = productService.getQuantityModification(product);

            mockedErrorHandler.verify(() -> ErrorHandler.getValidNumber(
                            "\nBy how much should we increase the " + productName + " quantity?"),
                    times(1));
            mockedErrorHandler.verify(() -> ErrorHandler.getValidNumber(
                            "\nBy how much should we increase or decrease the " + productName + " quantity?"),
                    times(0));
            assertEquals(quantityModification, quantityToBeModifiedResult,
                    "The returned quantity modification should match the mocked value.");
        }
    }

    @Test
    void testGetQuantityModification_ProductQuantityNonZero() {
        String productName = "cocoa";
        Product product = new Product(productName, "pr7553549", 235, 35);

        int quantityModification = 5;

        try (MockedStatic<ErrorHandler> mockedErrorHandler = Mockito.mockStatic(ErrorHandler.class)) {
            mockedErrorHandler.when(() -> ErrorHandler.getValidNumber(anyString())).thenReturn(quantityModification);

            ProductService productService = new ProductService();
            int quantityToBeModifiedResult = productService.getQuantityModification(product);

            mockedErrorHandler.verify(() -> ErrorHandler.getValidNumber(
                            "\nBy how much should we increase or decrease the " + productName + " quantity?"),
                    times(1));
            mockedErrorHandler.verify(() -> ErrorHandler.getValidNumber(
                            "\nBy how much should we increase the " + productName + " quantity?"),
                    times(0));
            assertEquals(quantityModification, quantityToBeModifiedResult,
                    "The returned quantity modification should match the mocked value.");
        }
    }

    @Test
    void testCalculateNewQuantity_PositiveModification() {
        int quantityModification = 15;
        Product product = new Product("apple", "pr5197140", 560, 25);

        ProductService productService = new ProductService();
        int newQuantityResult = productService.calculateNewQuantity(product, quantityModification);

        int expectedNewQuantity = 40;
        assertEquals(expectedNewQuantity, newQuantityResult,
                "The new quantity should be " + expectedNewQuantity + " when modification is " + quantityModification + ".");
    }

    @Test
    void testCalculateNewQuantity_NegativeModification() {
        int quantityModification = -32;
        Product product = new Product("pear", "pr4270613", 675, 92);

        ProductService productService = new ProductService();
        int newQuantityResult = productService.calculateNewQuantity(product, quantityModification);

        int expectedNewQuantity = 60;
        assertEquals(expectedNewQuantity, newQuantityResult,
                "The new quantity should be " + expectedNewQuantity + " when modification is " + quantityModification + ".");
    }

    @Test
    void testCalculateNewQuantity_ZeroModification() {
        int quantityModification = 0;
        Product product = new Product("banana", "pr5223508", 720, 102);

        ProductService productService = new ProductService();
        int newQuantityResult = productService.calculateNewQuantity(product, quantityModification);

        int expectedNewQuantity = 102;
        assertEquals(expectedNewQuantity, newQuantityResult,
                "The new quantity should be " + expectedNewQuantity + " when modification is " + quantityModification + ".");
    }

    @Test
    void testCalculateNewQuantity_NegativeResult() {
        int quantityModification = -157;
        Product product = new Product("coffee", "pr5204875", 1800, 92);

        ProductService productService = new ProductService();
        int newQuantityResult = productService.calculateNewQuantity(product, quantityModification);

        int expectedNewQuantity = -65;
        assertEquals(expectedNewQuantity, newQuantityResult,
                "The new quantity should be " + expectedNewQuantity + " when modification is " + quantityModification + ".");
    }

    @Test
    void testCalculateNewQuantity_LargeNumbers() {
        int quantityModification = 1;
        Product product = new Product("apple", "pr5197140", 1800, Integer.MAX_VALUE - 1);

        ProductService productService = new ProductService();
        int newQuantityResult = productService.calculateNewQuantity(product, quantityModification);

        int expectedNewQuantity = Integer.MAX_VALUE;
        assertEquals(expectedNewQuantity, newQuantityResult,
                "The new quantity should be " + expectedNewQuantity + " when modification is " + quantityModification + ".");
    }

    @Test
    void testSetNewQuantity_UpdatesQuantity() {
        TestUtils.redirectSystemOut();
        int newQuantity = 102;
        Product product = new Product("tea", "pr7236284", 1200, 52);

        ProductService productService = new ProductService();
        productService.setNewQuantity(product, newQuantity);

        assertEquals(newQuantity, product.getQuantity());

        TestUtils.restoreSystemOut();
    }

    @Test
    void testSetNewQuantity_PrintsMessage() {
        ByteArrayOutputStream outputStream = TestUtils.redirectSystemOut();
        int newQuantity = 93;
        Product product = new Product("coffee", "pr5204875", 1800, 125);

        ProductService productService = new ProductService();
        productService.setNewQuantity(product, newQuantity);

        String expectedMessage = "\nThe modification has been made!";
        String output = outputStream.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains(expectedMessage),
                "Expected message '" + expectedMessage + "' was not found in the output.");

        TestUtils.restoreSystemOut();
    }
}
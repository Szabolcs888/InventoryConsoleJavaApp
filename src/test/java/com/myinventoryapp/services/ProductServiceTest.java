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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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

            mockedIdUtils.when(IdUtils::generateId).thenReturn(5204875);
            mockedProductRepository.when(() ->
                    ProductRepository.addProduct(any(Product.class))).thenAnswer(invocation -> null);
            mockedProductDisplayHelper.when(() ->
                    ProductDisplayHelper.displayProductInfoAfterGoodsReceipt(any(Product.class))).thenAnswer(invocation -> null);

            ProductService productService = new ProductService();
            productService.addNewProduct(productName, unitPrice, quantity);

            String expectedProductId = "pr5204875";
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

            mockedIdUtils.when(IdUtils::generateId).thenReturn(5204875);
            mockedProductRepository.when(() ->
                    ProductRepository.addProduct(any(Product.class))).thenAnswer(invocation -> null);
            mockedProductDisplayHelper.when(() ->
                    ProductDisplayHelper.displayProductInfoAfterGoodsReceipt(any(Product.class))).thenAnswer(invocation -> null);

            ProductService productService = new ProductService();
            productService.addNewProduct(productName, unitPrice, quantity);

            String expectedProductId = "pr5204875";
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
        String productId = "pr7236284";
        int unitPrice = 1200;
        int quantity = 52;
        Product product = new Product(productName, productId, unitPrice, quantity);

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
        String productId = "pr5204875";
        int unitPrice = 1800;
        int quantity = 120;
        Product product = new Product(productName, productId, unitPrice, quantity);

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
            mockedProductRepository.verify(() -> ProductRepository.deleteProduct(product), times(0));
        } finally {
            TestUtils.restoreSystemOut();
        }
    }

    @Test
    void testDeleteProduct_DeletesAndDisplaysConfirmationMessage() {
        ByteArrayOutputStream outputStream = TestUtils.redirectSystemOut();

        String productName = "orange juice";
        String productId = "pr7987615";
        int unitPrice = 870;
        int quantity = 89;
        Product product = new Product(productName, productId, unitPrice, quantity);

        try (MockedStatic<ErrorHandler> mockedErrorHandler = Mockito.mockStatic(ErrorHandler.class);
             MockedStatic<ProductRepository> mockedProductRepository = Mockito.mockStatic(ProductRepository.class)) {

            mockedErrorHandler.when(() -> ErrorHandler.getYesOrNoAnswer(anyString())).thenReturn("Y");
            mockedProductRepository.when(() ->
                    ProductRepository.deleteProduct(product)).thenAnswer(invocation -> null);

            ProductService productService = new ProductService();
            productService.deleteProduct(product);

            String expectedAfterDeletingMessage = "\nThe item has been deleted!";
            String output = outputStream.toString();
            assertTrue(output.contains(expectedAfterDeletingMessage),
                    "Expected message '" + expectedAfterDeletingMessage + "' was not found in the output.");
        } finally {
            TestUtils.restoreSystemOut();
        }
    }

    @Test
    void testDeleteProduct_AbortsAndDisplaysNoDeletionMessage() {
        ByteArrayOutputStream outputStream = TestUtils.redirectSystemOut();

        String productName = "pineapple";
        String productId = "pr5711807";
        int unitPrice = 894;
        int quantity = 1;
        Product product = new Product(productName, productId, unitPrice, quantity);

        try (MockedStatic<ErrorHandler> mockedErrorHandler = Mockito.mockStatic(ErrorHandler.class);
             MockedStatic<ProductRepository> mockedProductRepository = Mockito.mockStatic(ProductRepository.class)) {

            mockedErrorHandler.when(() -> ErrorHandler.getYesOrNoAnswer(anyString())).thenReturn("N");
            mockedProductRepository.when(() ->
                    ProductRepository.deleteProduct(product)).thenAnswer(invocation -> null);

            ProductService productService = new ProductService();
            productService.deleteProduct(product);

            String unexpectedAfterDeletingMessage = "\nThe item has been deleted!";
            String output = outputStream.toString();
            assertFalse(output.contains(unexpectedAfterDeletingMessage),
                    "Expected message '" + unexpectedAfterDeletingMessage + "' found in the output.");
        } finally {
            TestUtils.restoreSystemOut();
        }
    }

    @Test
    void testUpdateProductQuantity_Product_int() {
        String productName = "cherry";
        String productId = "pr7860912";
        int unitPrice = 452;
        int quantity = 115;
        int quantitySold = 5;
        Product product = new Product(productName, productId, unitPrice, quantity);

        ProductService productService = new ProductService();
        productService.updateProductQuantity(product, quantitySold);

        int expectedQuantity = quantity - quantitySold;
        assertEquals(expectedQuantity, product.getQuantity(), "Product quantity was not updated correctly.");
    }

    @Test
    void testUpdateProductQuantity_Product_SuccessfulUpdate() {
        String productName = "mango";
        String productId = "pr4531265";
        int unitPrice = 1350;
        int quantity = 46;
        Product product = new Product(productName, productId, unitPrice, quantity);

        int quantityModification = 15;
        int newQuantity = quantity + quantityModification;

        try (MockedStatic<ErrorHandler> mockedErrorHandler = Mockito.mockStatic(ErrorHandler.class);
             MockedStatic<ProductDisplayHelper> mockedProductDisplayHelper = Mockito.mockStatic(ProductDisplayHelper.class)) {
            ProductService spyProductService = Mockito.spy(new ProductService());

            Mockito.doReturn(quantityModification).when(spyProductService).getQuantityModification(product);
            Mockito.doReturn(newQuantity).when(spyProductService).calculateNewQuantity(product, quantityModification);
            Mockito.doNothing().when(spyProductService).setNewQuantity(product, newQuantity);
            mockedErrorHandler.when(() -> ErrorHandler.validateNonNegativeQuantity(newQuantity)).thenAnswer(invocation -> null);
            mockedProductDisplayHelper.when(() -> ProductDisplayHelper.displayProductInfoAfterSellAndUpdateGoodsReceipt(
                    any(Product.class), anyString())).thenAnswer(invocation -> null);

            spyProductService.updateProductQuantity(product);

            Mockito.verify(spyProductService, times(1)).getQuantityModification(product);
            Mockito.verify(spyProductService, times(1)).calculateNewQuantity(product, quantityModification);
            Mockito.verify(spyProductService, times(1)).calculateNewQuantity(product, quantityModification);
            Mockito.verify(spyProductService, times(1)).setNewQuantity(product, newQuantity);
            mockedErrorHandler.verify(() -> ErrorHandler.validateNonNegativeQuantity(newQuantity), times(1));
            mockedProductDisplayHelper.verify(() -> ProductDisplayHelper.displayProductInfoAfterSellAndUpdateGoodsReceipt(
                    product, "PRODUCT INFORMATION AFTER RECEIPT TRANSACTION:"), times(1));
        }
    }

    @Test
    void testUpdateProductQuantity_Product_RetryOnNegativeQuantity() {
        String productName = "carrot";
        String productId = "pr2110710";
        int unitPrice = 245;
        int quantity = 31;
        Product product = new Product(productName, productId, unitPrice, quantity);

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
            Mockito.doReturn(newQuantity2).when(spyProductService).calculateNewQuantity(product, positiveQuantityModification);
            Mockito.doNothing().when(spyProductService).setNewQuantity(product, newQuantity1);
            Mockito.doNothing().when(spyProductService).setNewQuantity(product, newQuantity2);
            mockedErrorHandler.when(() -> ErrorHandler.validateNonNegativeQuantity(anyInt())).thenAnswer(invocation -> null);
            mockedProductDisplayHelper.when(() -> ProductDisplayHelper.displayProductInfoAfterSellAndUpdateGoodsReceipt(
                    any(Product.class), anyString())).thenAnswer(invocation -> null);

            spyProductService.updateProductQuantity(product);

            // There are so many verifications because the test runs two iterations.
            Mockito.verify(spyProductService, times(0)).setNewQuantity(product, newQuantity1);
            Mockito.verify(spyProductService, times(1)).setNewQuantity(product, newQuantity2);
            mockedErrorHandler.verify(() -> ErrorHandler.validateNonNegativeQuantity(newQuantity1), times(1));
            mockedErrorHandler.verify(() -> ErrorHandler.validateNonNegativeQuantity(newQuantity2), times(1));
            Mockito.verify(spyProductService, times(2)).getQuantityModification(product);
            Mockito.verify(spyProductService, times(1)).calculateNewQuantity(product, negativeQuantityModification);
            Mockito.verify(spyProductService, times(1)).calculateNewQuantity(product, positiveQuantityModification);
            mockedProductDisplayHelper.verify(() -> ProductDisplayHelper.displayProductInfoAfterSellAndUpdateGoodsReceipt(
                    product, "PRODUCT INFORMATION AFTER RECEIPT TRANSACTION:"), times(1));
        }
    }

    @Test
    void testGetQuantityModification_ProductQuantityZero() {
        String productName = "lemon";
        String productId = "pr6634365";
        int unitPrice = 880;
        int quantity = 0;
        Product product = new Product(productName, productId, unitPrice, quantity);

        int expectedQuantityModification = 19;
        try (MockedStatic<ErrorHandler> mockedErrorHandler = Mockito.mockStatic(ErrorHandler.class)) {
            mockedErrorHandler.when(() -> ErrorHandler.getValidNumber(anyString())).thenReturn(expectedQuantityModification);

            ProductService productService = new ProductService();
            int result = productService.getQuantityModification(product);

            mockedErrorHandler.verify(() -> ErrorHandler.getValidNumber(
                            "\nBy how much should we increase or decrease the " + productName + " quantity?"),
                    times(0));
            mockedErrorHandler.verify(() -> ErrorHandler.getValidNumber(
                            "\nBy how much should we increase the " + productName + " quantity?"),
                    times(1));
            assertEquals(expectedQuantityModification, result,
                    "The returned quantity modification should match the mocked value.");
        }
    }

    @Test
    void testGetQuantityModification_ProductQuantityNonZero() {
        String productName = "cocoa";
        String productId = "pr7553549";
        int unitPrice = 235;
        int quantity = 35;
        Product product = new Product(productName, productId, unitPrice, quantity);

        int expectedQuantityModification = 5;
        try (MockedStatic<ErrorHandler> mockedErrorHandler = Mockito.mockStatic(ErrorHandler.class)) {
            mockedErrorHandler.when(() -> ErrorHandler.getValidNumber(anyString())).thenReturn(expectedQuantityModification);

            ProductService productService = new ProductService();
            int result = productService.getQuantityModification(product);

            mockedErrorHandler.verify(() -> ErrorHandler.getValidNumber(
                            "\nBy how much should we increase or decrease the " + productName + " quantity?"),
                    times(1));
            mockedErrorHandler.verify(() -> ErrorHandler.getValidNumber(
                            "\nBy how much should we increase the " + productName + " quantity?"),
                    times(0));
            assertEquals(expectedQuantityModification, result,
                    "The returned quantity modification should match the mocked value.");
        }
    }

    @Test
    void testCalculateNewQuantity_PositiveModification() {
        String productName = "apple";
        String productId = "pr5197140";
        int unitPrice = 560;
        int quantity = 25;
        int quantityModification = 15;
        Product product = new Product(productName, productId, unitPrice, quantity);

        ProductService productService = new ProductService();
        int result = productService.calculateNewQuantity(product, quantityModification);

        int expectedResult = 40;
        assertEquals(expectedResult, result,
                "The new quantity should be " + expectedResult + " when modification is " + quantityModification + ".");
    }

    @Test
    void testCalculateNewQuantity_NegativeModification() {
        String productName = "pear";
        String productId = "pr4270613";
        int unitPrice = 675;
        int quantity = 92;
        int quantityModification = -32;
        Product product = new Product(productName, productId, unitPrice, quantity);

        ProductService productService = new ProductService();
        int result = productService.calculateNewQuantity(product, quantityModification);

        int expectedResult = 60;
        assertEquals(expectedResult, result,
                "The new quantity should be " + expectedResult + " when modification is " + quantityModification + ".");
    }

    @Test
    void testCalculateNewQuantity_ZeroModification() {
        String productName = "banana";
        String productId = "pr5223508";
        int unitPrice = 720;
        int quantity = 102;
        int quantityModification = 0;
        Product product = new Product(productName, productId, unitPrice, quantity);

        ProductService productService = new ProductService();
        int result = productService.calculateNewQuantity(product, quantityModification);

        int expectedResult = 102;
        assertEquals(expectedResult, result,
                "The new quantity should be " + expectedResult + " when modification is " + quantityModification + ".");
    }

    @Test
    void testCalculateNewQuantity_NegativeResult() {
        String productName = "coffee";
        String productId = "pr5204875";
        int unitPrice = 1800;
        int quantity = 92;
        int quantityModification = -157;
        Product product = new Product(productName, productId, unitPrice, quantity);

        ProductService productService = new ProductService();
        int result = productService.calculateNewQuantity(product, quantityModification);

        int expectedResult = -65;
        assertEquals(expectedResult, result,
                "The new quantity should be " + expectedResult + " when modification is " + quantityModification + ".");
    }

    @Test
    void testCalculateNewQuantity_LargeNumbers() {
        String productName = "coffee";
        String productId = "pr5204875";
        int unitPrice = 1800;
        int quantity = Integer.MAX_VALUE - 1;
        int quantityModification = 1;
        Product product = new Product(productName, productId, unitPrice, quantity);

        ProductService productService = new ProductService();
        int result = productService.calculateNewQuantity(product, quantityModification);

        int expectedResult = Integer.MAX_VALUE;
        assertEquals(expectedResult, result,
                "The new quantity should be " + expectedResult + " when modification is " + quantityModification + ".");
    }

    @Test
    void testSetNewQuantity_UpdatesQuantity() {
        TestUtils.redirectSystemOut();

        String productName = "tea";
        String productId = "pr7236284";
        int unitPrice = 1200;
        int quantity = 52;
        int newQuantity = 102;
        Product product = new Product(productName, productId, unitPrice, quantity);

        ProductService productService = new ProductService();
        productService.setNewQuantity(product, newQuantity);

        assertEquals(newQuantity, product.getQuantity());

        TestUtils.restoreSystemOut();
    }

    @Test
    void testSetNewQuantity_PrintsMessage() {
        ByteArrayOutputStream outputStream = TestUtils.redirectSystemOut();

        String productName = "coffee";
        String productId = "pr5204875";
        int unitPrice = 1800;
        int quantity = 125;
        int newQuantity = 93;
        Product product = new Product(productName, productId, unitPrice, quantity);

        ProductService productService = new ProductService();
        productService.setNewQuantity(product, newQuantity);

        String expectedMessage = "\nThe modification has been made!";
        String output = outputStream.toString();
        assertTrue(output.contains(expectedMessage),
                "Expected message '" + expectedMessage + "' was not found in the output.");

        TestUtils.restoreSystemOut();
    }
}
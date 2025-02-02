package com.myinventoryapp.ui.menu;

import com.myinventoryapp.entities.Product;
import com.myinventoryapp.repository.ProductRepository;
import com.myinventoryapp.services.ProductService;
import com.myinventoryapp.util.*;
import com.myinventoryapp.util.displayhelpers.ProductDisplayHelper;
import com.myinventoryapp.util.testutils.MenuOptionFactory;
import com.myinventoryapp.util.testutils.TestUtils;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

class MenuOption2GoodsReceiptTest {

    @Test
    void testGoodsReceipt_PrintsReceiveProductsMessage() {
        ByteArrayOutputStream outputStream = TestUtils.redirectSystemOut();
        String inputProductName = "mango";

        try (MockedStatic<ErrorHandler> mockedErrorHandler = Mockito.mockStatic(ErrorHandler.class)) {
            MenuOption2GoodsReceipt spyMenuOption2GoodsReceipt = Mockito.spy(MenuOptionFactory.createMenuOption2GoodsReceipt());

            Mockito.doReturn(inputProductName).when(spyMenuOption2GoodsReceipt).getProductName();
            Mockito.doReturn(true).when(spyMenuOption2GoodsReceipt).isProductInList(inputProductName);
            Mockito.doNothing().when(spyMenuOption2GoodsReceipt).handleProductIfInList(inputProductName);
            mockedErrorHandler.when(() -> ErrorHandler.getYesOrNoAnswer(anyString())).thenReturn("N");

            spyMenuOption2GoodsReceipt.goodsReceipt("\n-RECEIVE PRODUCT MENU-\n");

            String expectedMessage = "-RECEIVE PRODUCT MENU-";
            String output = outputStream.toString();
            assertTrue(output.contains(expectedMessage),
                    "Expected message '" + expectedMessage + "' was not found in the output.");
        } finally {
            TestUtils.restoreSystemOut();
        }
    }

    @Test
    void testGoodsReceipt_ProductInListThenNoNewProductAdded() {
        TestUtils.redirectSystemOut();
        String inputProductName = "cocoa";

        try (MockedStatic<ErrorHandler> mockedErrorHandler = Mockito.mockStatic(ErrorHandler.class)) {
            MenuOption2GoodsReceipt spyMenuOption2GoodsReceipt = Mockito.spy(MenuOptionFactory.createMenuOption2GoodsReceipt());

            Mockito.doReturn(inputProductName).when(spyMenuOption2GoodsReceipt).getProductName();
            Mockito.doReturn(true).when(spyMenuOption2GoodsReceipt).isProductInList(inputProductName);
            Mockito.doNothing().when(spyMenuOption2GoodsReceipt).handleProductIfInList(inputProductName);
            mockedErrorHandler.when(() -> ErrorHandler.getYesOrNoAnswer(anyString())).thenReturn("N");

            spyMenuOption2GoodsReceipt.goodsReceipt("\n-RECEIVE PRODUCT MENU-\n");

            Mockito.verify(spyMenuOption2GoodsReceipt, times(1)).getProductName();
            Mockito.verify(spyMenuOption2GoodsReceipt, times(1)).isProductInList(inputProductName);
            Mockito.verify(spyMenuOption2GoodsReceipt, times(1)).handleProductIfInList(inputProductName);
            Mockito.verify(spyMenuOption2GoodsReceipt, never()).handleNewProductAddition(inputProductName);
            mockedErrorHandler.verify(() -> ErrorHandler.getYesOrNoAnswer(
                            "Would you like to add a new product or modify the quantity of an existing one? (Y/N)"),
                    times(1));
        } finally {
            TestUtils.restoreSystemOut();
        }
    }

    @Test
    void testGoodsReceipt_ProductInListThenAddingNewProduct() {
        TestUtils.redirectSystemOut();
        String firstInputProductName = "apple";
        String secondInputProductName = "jam";

        try (MockedStatic<ErrorHandler> mockedErrorHandler = Mockito.mockStatic(ErrorHandler.class)) {
            MenuOption2GoodsReceipt spyMenuOption2GoodsReceipt = Mockito.spy(MenuOptionFactory.createMenuOption2GoodsReceipt());

            Mockito.doReturn(firstInputProductName, secondInputProductName).when(spyMenuOption2GoodsReceipt).getProductName();
            Mockito.doReturn(true).when(spyMenuOption2GoodsReceipt).isProductInList(firstInputProductName);
            Mockito.doNothing().when(spyMenuOption2GoodsReceipt).handleProductIfInList(firstInputProductName);
            mockedErrorHandler.when(() -> ErrorHandler.getYesOrNoAnswer(anyString())).thenReturn("Y").thenReturn("N");
            Mockito.doReturn(false).when(spyMenuOption2GoodsReceipt).isProductInList(secondInputProductName);
            Mockito.doNothing().when(spyMenuOption2GoodsReceipt).handleNewProductAddition(secondInputProductName);

            spyMenuOption2GoodsReceipt.goodsReceipt("\n-RECEIVE PRODUCT MENU-\n");

            Mockito.verify(spyMenuOption2GoodsReceipt, times(2)).getProductName();
            Mockito.verify(spyMenuOption2GoodsReceipt, times(1)).isProductInList(firstInputProductName);
            Mockito.verify(spyMenuOption2GoodsReceipt, times(1)).handleProductIfInList(firstInputProductName);
            Mockito.verify(spyMenuOption2GoodsReceipt, times(1)).isProductInList(secondInputProductName);
            Mockito.verify(spyMenuOption2GoodsReceipt, times(1)).handleNewProductAddition(secondInputProductName);
            mockedErrorHandler.verify(() -> ErrorHandler.getYesOrNoAnswer(
                            "Would you like to add a new product or modify the quantity of an existing one? (Y/N)"),
                    times(2));
        } finally {
            TestUtils.restoreSystemOut();
        }
    }

    @Test
    void testGoodsReceipt_ProductIsNotInListThenNoNewProductAdded() {
        TestUtils.redirectSystemOut();
        String inputProductName = "bean";

        try (MockedStatic<ErrorHandler> mockedErrorHandler = Mockito.mockStatic(ErrorHandler.class)) {
            MenuOption2GoodsReceipt spyMenuOption2GoodsReceipt = Mockito.spy(MenuOptionFactory.createMenuOption2GoodsReceipt());

            Mockito.doReturn(inputProductName).when(spyMenuOption2GoodsReceipt).getProductName();
            Mockito.doReturn(false).when(spyMenuOption2GoodsReceipt).isProductInList(inputProductName);
            Mockito.doNothing().when(spyMenuOption2GoodsReceipt).handleNewProductAddition(inputProductName);
            mockedErrorHandler.when(() -> ErrorHandler.getYesOrNoAnswer(anyString())).thenReturn("N");

            spyMenuOption2GoodsReceipt.goodsReceipt("\n-RECEIVE PRODUCT MENU-\n");

            Mockito.verify(spyMenuOption2GoodsReceipt, times(1)).getProductName();
            Mockito.verify(spyMenuOption2GoodsReceipt, times(1)).isProductInList(inputProductName);
            Mockito.verify(spyMenuOption2GoodsReceipt, never()).handleProductIfInList(inputProductName);
            Mockito.verify(spyMenuOption2GoodsReceipt, times(1)).handleNewProductAddition(inputProductName);
            mockedErrorHandler.verify(() -> ErrorHandler.getYesOrNoAnswer(
                            "Would you like to add a new product or modify the quantity of an existing one? (Y/N)"),
                    times(1));
        } finally {
            TestUtils.restoreSystemOut();
        }
    }

    @Test
    void testGoodsReceipt_ProductIsNotInListThenAddingNewProduct() {
        TestUtils.redirectSystemOut();
        String firstInputProductName = "biscuit";
        String secondInputProductName = "honey";

        try (MockedStatic<ErrorHandler> mockedErrorHandler = Mockito.mockStatic(ErrorHandler.class)) {
            MenuOption2GoodsReceipt spyMenuOption2GoodsReceipt = Mockito.spy(MenuOptionFactory.createMenuOption2GoodsReceipt());

            Mockito.doReturn(firstInputProductName, secondInputProductName).when(spyMenuOption2GoodsReceipt).getProductName();
            Mockito.doReturn(false).when(spyMenuOption2GoodsReceipt).isProductInList(firstInputProductName);
            Mockito.doNothing().when(spyMenuOption2GoodsReceipt).handleNewProductAddition(firstInputProductName);
            mockedErrorHandler.when(() -> ErrorHandler.getYesOrNoAnswer(anyString())).thenReturn("Y").thenReturn("N");
            Mockito.doReturn(false).when(spyMenuOption2GoodsReceipt).isProductInList(secondInputProductName);
            Mockito.doNothing().when(spyMenuOption2GoodsReceipt).handleNewProductAddition(secondInputProductName);

            spyMenuOption2GoodsReceipt.goodsReceipt("\n-RECEIVE PRODUCT MENU-\n");

            Mockito.verify(spyMenuOption2GoodsReceipt, times(2)).getProductName();
            Mockito.verify(spyMenuOption2GoodsReceipt, times(1)).isProductInList(firstInputProductName);
            Mockito.verify(spyMenuOption2GoodsReceipt, never()).handleProductIfInList(firstInputProductName);
            Mockito.verify(spyMenuOption2GoodsReceipt, times(1)).handleNewProductAddition(firstInputProductName);
            Mockito.verify(spyMenuOption2GoodsReceipt, times(1)).isProductInList(secondInputProductName);
            Mockito.verify(spyMenuOption2GoodsReceipt, never()).handleProductIfInList(secondInputProductName);
            Mockito.verify(spyMenuOption2GoodsReceipt, times(1)).handleNewProductAddition(secondInputProductName);
            mockedErrorHandler.verify(() -> ErrorHandler.getYesOrNoAnswer(
                            "Would you like to add a new product or modify the quantity of an existing one? (Y/N)"),
                    times(2));
        } finally {
            TestUtils.restoreSystemOut();
        }
    }

    @Test
    void testGetProductName_WithValidName() {
        String validInputProductName = "lemon";

        try (MockedStatic<UserInputUtils> mockedUserInputUtils = Mockito.mockStatic(UserInputUtils.class);
             MockedStatic<ErrorHandler> mockedErrorHandler = Mockito.mockStatic(ErrorHandler.class);
             MockedStatic<ValidationUtils> mockedValidationUtils = Mockito.mockStatic(ValidationUtils.class)) {

            mockedUserInputUtils.when(() -> UserInputUtils.readFromUser(anyString())).thenReturn(validInputProductName);
            mockedErrorHandler.when(() -> ErrorHandler.validateName(validInputProductName)).thenAnswer(invocation -> null);
            mockedValidationUtils.when(() -> ValidationUtils.isValidName(validInputProductName)).thenReturn(true);

            MenuOption2GoodsReceipt menuOption2GoodsReceipt = MenuOptionFactory.createMenuOption2GoodsReceipt();
            String inputProductNameResult = menuOption2GoodsReceipt.getProductName();

            assertEquals(validInputProductName, inputProductNameResult);
            mockedUserInputUtils.verify(() -> UserInputUtils.readFromUser("Please enter the product name:"),
                    times(1));
            mockedErrorHandler.verify(() -> ErrorHandler.validateName(validInputProductName),
                    times(1));
            mockedValidationUtils.verify(() -> ValidationUtils.isValidName(validInputProductName),
                    times(1));
        }
    }

    @Test
    void testGetProductName_WithInvalidNameThenValidName() {
        String invalidInputProductName = "aI";
        String validInputProductName = "honey";

        try (MockedStatic<UserInputUtils> mockedUserInputUtils = Mockito.mockStatic(UserInputUtils.class);
             MockedStatic<ErrorHandler> mockedErrorHandler = Mockito.mockStatic(ErrorHandler.class);
             MockedStatic<ValidationUtils> mockedValidationUtils = Mockito.mockStatic(ValidationUtils.class)) {

            mockedUserInputUtils.when(() -> UserInputUtils.readFromUser(anyString())).
                    thenReturn(invalidInputProductName).thenReturn(validInputProductName);
            mockedErrorHandler.when(() -> ErrorHandler.validateName(invalidInputProductName)).thenAnswer(invocation -> null);
            mockedValidationUtils.when(() -> ValidationUtils.isValidName(invalidInputProductName)).thenReturn(false);
            mockedErrorHandler.when(() -> ErrorHandler.validateName(validInputProductName)).thenAnswer(invocation -> null);
            mockedValidationUtils.when(() -> ValidationUtils.isValidName(validInputProductName)).thenReturn(true);

            MenuOption2GoodsReceipt menuOption2GoodsReceipt = MenuOptionFactory.createMenuOption2GoodsReceipt();
            String inputProductNameResult = menuOption2GoodsReceipt.getProductName();

            assertEquals(validInputProductName, inputProductNameResult);
            mockedUserInputUtils.verify(() -> UserInputUtils.readFromUser("Please enter the product name:"),
                    times(2));
            mockedErrorHandler.verify(() -> ErrorHandler.validateName(invalidInputProductName),
                    times(1));
            mockedValidationUtils.verify(() -> ValidationUtils.isValidName(invalidInputProductName),
                    times(1));
            mockedErrorHandler.verify(() -> ErrorHandler.validateName(validInputProductName),
                    times(1));
            mockedValidationUtils.verify(() -> ValidationUtils.isValidName(validInputProductName),
                    times(1));
        }
    }

    @Test
    void testIsProductInList_ProductIsInList() {
        String inputProductName = "carrot";
        Product product = new Product("carrot", "pr2110710", 245, 31);

        try (MockedStatic<ProductRepository> mockedProductRepository = Mockito.mockStatic(ProductRepository.class)) {
            mockedProductRepository.when(() -> ProductRepository.findProductByName(anyString())).thenReturn(product);

            MenuOption2GoodsReceipt menuOption2GoodsReceipt = MenuOptionFactory.createMenuOption2GoodsReceipt();
            boolean isProductInListResult = menuOption2GoodsReceipt.isProductInList(inputProductName);

            assertTrue(isProductInListResult);
            mockedProductRepository.verify(() -> ProductRepository.findProductByName(inputProductName),
                    times(1));
        }
    }

    @Test
    void testIsProductInList_ProductIsNotInList() {
        String inputProductName = "coke";

        try (MockedStatic<ProductRepository> mockedProductRepository = Mockito.mockStatic(ProductRepository.class)) {
            mockedProductRepository.when(() -> ProductRepository.findProductByName(anyString())).thenReturn(null);

            MenuOption2GoodsReceipt menuOption2GoodsReceipt = MenuOptionFactory.createMenuOption2GoodsReceipt();
            boolean isProductInListResult = menuOption2GoodsReceipt.isProductInList(inputProductName);

            assertFalse(isProductInListResult);
            mockedProductRepository.verify(() -> ProductRepository.findProductByName(inputProductName),
                    times(1));
        }
    }

    @Test
    void testHandleNewProductAddition() {
        String productName = "tea";
        int unitPrice = 450;
        int quantity = 82;

        ProductService mockProductService = Mockito.mock(ProductService.class);
        MenuOption2GoodsReceipt spyMenuOption2GoodsReceipt = Mockito.spy(new MenuOption2GoodsReceipt(mockProductService));

        Mockito.doReturn(unitPrice).when(spyMenuOption2GoodsReceipt).getProductPrice();
        Mockito.doReturn(quantity).when(spyMenuOption2GoodsReceipt).getProductQuantity();
        Mockito.doNothing().when(mockProductService).addNewProduct(anyString(), anyInt(), anyInt());

        spyMenuOption2GoodsReceipt.handleNewProductAddition(productName);

        Mockito.verify(spyMenuOption2GoodsReceipt, times(1)).getProductPrice();
        Mockito.verify(spyMenuOption2GoodsReceipt, times(1)).getProductQuantity();
        Mockito.verify(mockProductService, times(1)).addNewProduct(productName, unitPrice, quantity);
    }

    @Test
    void testGetProductPrice_ValidPrice() {
        int unitPrice = 725;

        try (MockedStatic<ErrorHandler> mockedErrorHandler = Mockito.mockStatic(ErrorHandler.class)) {

            mockedErrorHandler.when(() -> ErrorHandler.getValidNumber(anyString())).thenReturn(unitPrice);
            mockedErrorHandler.when(() -> ErrorHandler.validatePrice(unitPrice)).thenAnswer(invocation -> null);

            MenuOption2GoodsReceipt menuOption2GoodsReceipt = MenuOptionFactory.createMenuOption2GoodsReceipt();
            int unitPriceResult = menuOption2GoodsReceipt.getProductPrice();

            assertEquals(unitPrice, unitPriceResult);
            mockedErrorHandler.verify(() -> ErrorHandler.getValidNumber("\nPlease enter the product price:"),
                    times(1));
            mockedErrorHandler.verify(() -> ErrorHandler.validatePrice(unitPrice), times(1));
        }
    }

    @Test
    void testGetProductPrice_InvalidPriceThenValidPrice() {
        int invalidUnitPrice = -115;
        int validUnitPrice = 570;

        try (MockedStatic<ErrorHandler> mockedErrorHandler = Mockito.mockStatic(ErrorHandler.class)) {

            mockedErrorHandler.when(() -> ErrorHandler.getValidNumber(anyString())).
                    thenReturn(invalidUnitPrice).thenReturn(validUnitPrice);
            mockedErrorHandler.when(() -> ErrorHandler.validatePrice(invalidUnitPrice)).thenAnswer(invocation -> null);
            mockedErrorHandler.when(() -> ErrorHandler.validatePrice(validUnitPrice)).thenAnswer(invocation -> null);

            MenuOption2GoodsReceipt menuOption2GoodsReceipt = MenuOptionFactory.createMenuOption2GoodsReceipt();
            int unitPriceResult = menuOption2GoodsReceipt.getProductPrice();

            assertEquals(validUnitPrice, unitPriceResult);
            mockedErrorHandler.verify(() -> ErrorHandler.getValidNumber("\nPlease enter the product price:"),
                    times(2));
            mockedErrorHandler.verify(() -> ErrorHandler.validatePrice(invalidUnitPrice), times(1));
            mockedErrorHandler.verify(() -> ErrorHandler.validatePrice(validUnitPrice), times(1));
        }
    }

    @Test
    void testGetProductQuantity_ValidQuantity() {
        int quantity = 72;

        try (MockedStatic<ErrorHandler> mockedErrorHandler = Mockito.mockStatic(ErrorHandler.class)) {

            mockedErrorHandler.when(() -> ErrorHandler.getValidNumber(anyString())).thenReturn(quantity);
            mockedErrorHandler.when(() -> ErrorHandler.validateQuantity(quantity)).thenAnswer(invocation -> null);

            MenuOption2GoodsReceipt menuOption2GoodsReceipt = MenuOptionFactory.createMenuOption2GoodsReceipt();
            int quantityResult = menuOption2GoodsReceipt.getProductQuantity();

            assertEquals(quantity, quantityResult);
            mockedErrorHandler.verify(() -> ErrorHandler.getValidNumber("\nPlease enter the product quantity:"),
                    times(1));
            mockedErrorHandler.verify(() -> ErrorHandler.validateQuantity(quantity), times(1));
        }
    }

    @Test
    void testGetProductQuantity_InvalidQuantityThenValidQuantity() {
        int invalidQuantity = 0;
        int validQuantity = 88;

        try (MockedStatic<ErrorHandler> mockedErrorHandler = Mockito.mockStatic(ErrorHandler.class)) {

            mockedErrorHandler.when(() -> ErrorHandler.getValidNumber(anyString())).
                    thenReturn(invalidQuantity).thenReturn(validQuantity);
            mockedErrorHandler.when(() -> ErrorHandler.validateQuantity(invalidQuantity)).thenAnswer(invocation -> null);
            mockedErrorHandler.when(() -> ErrorHandler.validateQuantity(validQuantity)).thenAnswer(invocation -> null);

            MenuOption2GoodsReceipt menuOption2GoodsReceipt = MenuOptionFactory.createMenuOption2GoodsReceipt();
            int quantityResult = menuOption2GoodsReceipt.getProductQuantity();

            assertEquals(validQuantity, quantityResult);
            mockedErrorHandler.verify(() -> ErrorHandler.getValidNumber("\nPlease enter the product quantity:"),
                    times(2));
            mockedErrorHandler.verify(() -> ErrorHandler.validateQuantity(invalidQuantity), times(1));
            mockedErrorHandler.verify(() -> ErrorHandler.validateQuantity(validQuantity), times(1));
        }
    }

    @Test
    void testHandleProductIfInList_ProductInList() {
        String inputProductName = "cherry";
        Product product = new Product("cherry", "pr7860912", 452, 115);

        try (MockedStatic<ProductRepository> mockedProductRepository = Mockito.mockStatic(ProductRepository.class);
             MockedStatic<ProductDisplayHelper> mockedProductDisplayHelper = Mockito.mockStatic(ProductDisplayHelper.class)) {
            MenuOption2GoodsReceipt spyMenuOption2GoodsReceipt = Mockito.spy(MenuOptionFactory.createMenuOption2GoodsReceipt());

            mockedProductRepository.when(() -> ProductRepository.findProductByName(anyString())).thenReturn(product);
            mockedProductDisplayHelper.when(() -> ProductDisplayHelper.displayExistingProductInfo(product)).
                    thenAnswer(invocation -> null);
            Mockito.doNothing().when(spyMenuOption2GoodsReceipt).askUserForProductAction(product);

            spyMenuOption2GoodsReceipt.handleProductIfInList(inputProductName);

            mockedProductRepository.verify(() -> ProductRepository.findProductByName(inputProductName),
                    times(1));
            mockedProductDisplayHelper.verify(() -> ProductDisplayHelper.displayExistingProductInfo(product),
                    times(1));
            Mockito.verify(spyMenuOption2GoodsReceipt, times(1)).askUserForProductAction(product);
            mockedProductDisplayHelper.verify(() -> ProductDisplayHelper.displayProductNotFoundMessage(inputProductName), never());
        }
    }

    @Test
    void testHandleProductIfInList_ProductIsNotInList() {
        String inputProductName = "lollipop";

        try (MockedStatic<ProductRepository> mockedProductRepository = Mockito.mockStatic(ProductRepository.class);
             MockedStatic<ProductDisplayHelper> mockedProductDisplayHelper = Mockito.mockStatic(ProductDisplayHelper.class)) {
            MenuOption2GoodsReceipt spyMenuOption2GoodsReceipt = Mockito.spy(MenuOptionFactory.createMenuOption2GoodsReceipt());

            mockedProductRepository.when(() -> ProductRepository.findProductByName(anyString())).thenReturn(null);
            mockedProductDisplayHelper.when(() -> ProductDisplayHelper.displayProductNotFoundMessage(anyString())).
                    thenAnswer(invocation -> null);

            spyMenuOption2GoodsReceipt.handleProductIfInList(inputProductName);

            mockedProductRepository.verify(() -> ProductRepository.findProductByName(inputProductName),
                    times(1));
            mockedProductDisplayHelper.verify(() -> ProductDisplayHelper.displayExistingProductInfo(any(Product.class)), never());
            Mockito.verify(spyMenuOption2GoodsReceipt, never()).askUserForProductAction(any(Product.class));
            mockedProductDisplayHelper.verify(() -> ProductDisplayHelper.displayProductNotFoundMessage(inputProductName),
                    times(1));
        }
    }

    @Test
    void testAskUserForProductAction_YesOption() {
        String yesModificationOption = "Y";
        Product product = new Product("pear", "pr4270613", 675, 19);

        try (MockedStatic<ErrorHandler> mockedErrorHandler = Mockito.mockStatic(ErrorHandler.class)) {
            ProductService mockProductService = Mockito.mock(ProductService.class);
            MenuOption2GoodsReceipt spyMenuOption2GoodsReceipt = Mockito.spy(new MenuOption2GoodsReceipt(mockProductService));

            mockedErrorHandler.when(() -> ErrorHandler.getYesOrNoOrDeleteAnswer(anyString())).thenReturn(yesModificationOption);
            Mockito.doNothing().when(spyMenuOption2GoodsReceipt).askAndUpdateProductQuantity(any(Product.class));

            spyMenuOption2GoodsReceipt.askUserForProductAction(product);

            mockedErrorHandler.verify(() -> ErrorHandler.getYesOrNoOrDeleteAnswer(
                            "Would you like to add to or subtract from the product quantity? (Y/N) " +
                                    "To delete from the inventory, press \"D\"!"),
                    times(1));
            Mockito.verify(spyMenuOption2GoodsReceipt, times(1)).askAndUpdateProductQuantity(product);
            Mockito.verify(mockProductService, never()).deleteProduct(product);
        }
    }

    @Test
    void testAskUserForProductAction_NoOption() {
        String noModificationOption = "N";
        Product product = new Product("pineapple", "pr5711807", 894, 18);

        try (MockedStatic<ErrorHandler> mockedErrorHandler = Mockito.mockStatic(ErrorHandler.class)) {
            ProductService mockProductService = Mockito.mock(ProductService.class);
            MenuOption2GoodsReceipt spyMenuOption2GoodsReceipt = Mockito.spy(new MenuOption2GoodsReceipt(mockProductService));

            mockedErrorHandler.when(() -> ErrorHandler.getYesOrNoOrDeleteAnswer(anyString())).thenReturn(noModificationOption);

            spyMenuOption2GoodsReceipt.askUserForProductAction(product);

            mockedErrorHandler.verify(() -> ErrorHandler.getYesOrNoOrDeleteAnswer(
                            "Would you like to add to or subtract from the product quantity? (Y/N) " +
                                    "To delete from the inventory, press \"D\"!"),
                    times(1));
            Mockito.verify(spyMenuOption2GoodsReceipt, never()).askAndUpdateProductQuantity(product);
            Mockito.verify(mockProductService, never()).deleteProduct(product);
        }
    }

    @Test
    void testAskUserForProductAction_DeleteOption() {
        String removeProductOption = "D";
        Product product = new Product("banana", "pr5223508", 720, 102);

        try (MockedStatic<ErrorHandler> mockedErrorHandler = Mockito.mockStatic(ErrorHandler.class)) {
            ProductService mockProductService = Mockito.mock(ProductService.class);
            MenuOption2GoodsReceipt spyMenuOption2GoodsReceipt = Mockito.spy(new MenuOption2GoodsReceipt(mockProductService));

            mockedErrorHandler.when(() -> ErrorHandler.getYesOrNoOrDeleteAnswer(anyString())).thenReturn(removeProductOption);
            Mockito.doNothing().when(mockProductService).deleteProduct(any(Product.class));

            spyMenuOption2GoodsReceipt.askUserForProductAction(product);

            mockedErrorHandler.verify(() -> ErrorHandler.getYesOrNoOrDeleteAnswer(
                            "Would you like to add to or subtract from the product quantity? (Y/N) " +
                                    "To delete from the inventory, press \"D\"!"),
                    times(1));
            Mockito.verify(spyMenuOption2GoodsReceipt, never()).askAndUpdateProductQuantity(product);
            Mockito.verify(mockProductService, times(1)).deleteProduct(product);
        }
    }

    @Test
    void testAskAndUpdateProductQuantity_YesOptionThenNoOption() {
        String yesModificationOption = "Y";
        String noModificationOption = "N";
        Product product = new Product("coffee", "pr5204875", 1800, 44);

        try (MockedStatic<ErrorHandler> mockedErrorHandler = Mockito.mockStatic(ErrorHandler.class)) {
            ProductService mockProductService = Mockito.mock(ProductService.class);

            Mockito.doNothing().when(mockProductService).updateProductQuantity(any(Product.class));
            mockedErrorHandler.when(() -> ErrorHandler.getYesOrNoAnswer(anyString())).
                    thenReturn(yesModificationOption).thenReturn(noModificationOption);

            MenuOption2GoodsReceipt menuOption2GoodsReceipt = new MenuOption2GoodsReceipt(mockProductService);
            menuOption2GoodsReceipt.askAndUpdateProductQuantity(product);

            Mockito.verify(mockProductService, times(2)).updateProductQuantity(product);
            mockedErrorHandler.verify(() -> ErrorHandler.getYesOrNoAnswer(
                            "Would you like to modify the " + product.getProductName() + " quantity further? (Y/N)"),
                    times(2));
        }
    }

    @Test
    void testAskAndUpdateProductQuantity_NoOption() {
        String noModificationOption = "N";
        Product product = new Product("orange juice", "pr7987615", 870, 89);

        try (MockedStatic<ErrorHandler> mockedErrorHandler = Mockito.mockStatic(ErrorHandler.class)) {
            ProductService mockProductService = Mockito.mock(ProductService.class);

            Mockito.doNothing().when(mockProductService).updateProductQuantity(any(Product.class));
            mockedErrorHandler.when(() -> ErrorHandler.getYesOrNoAnswer(anyString())).thenReturn(noModificationOption);

            MenuOption2GoodsReceipt menuOption2GoodsReceipt = new MenuOption2GoodsReceipt(mockProductService);
            menuOption2GoodsReceipt.askAndUpdateProductQuantity(product);

            Mockito.verify(mockProductService, times(1)).updateProductQuantity(product);
            mockedErrorHandler.verify(() -> ErrorHandler.getYesOrNoAnswer(
                            "Would you like to modify the " + product.getProductName() + " quantity further? (Y/N)"),
                    times(1));
        }
    }
}
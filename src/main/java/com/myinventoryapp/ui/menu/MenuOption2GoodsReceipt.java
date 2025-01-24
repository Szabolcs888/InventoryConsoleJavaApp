package com.myinventoryapp.ui.menu;

import com.myinventoryapp.entities.Product;
import com.myinventoryapp.services.ProductService;
import com.myinventoryapp.util.*;
import com.myinventoryapp.util.displayhelpers.ProductDisplayHelper;
import com.myinventoryapp.repository.ProductRepository;

public class MenuOption2GoodsReceipt {
    private static final String YES_OPTION = "Y";
    private static final String DELETE_OPTION = "D";

    private final ProductService productService;

    public MenuOption2GoodsReceipt(ProductService productService) {
        this.productService = productService;
    }

    public void goodsReceipt(String text) {
        System.out.println(text);
        String inputProductName;
        String askAddOrModifyProduct;
        do {
            inputProductName = getProductName();
            if (isProductInList(inputProductName)) {
                handleProductIfInList(inputProductName);
            } else {
                handleNewProductAddition(inputProductName);
            }
            askAddOrModifyProduct = ErrorHandler.getYesOrNoAnswer(
                    "Would you like to add a new product or modify the quantity of an existing one? (Y/N)");
            System.out.println();
        } while (YES_OPTION.equalsIgnoreCase(askAddOrModifyProduct));
    }

    private String getProductName() {
        String inputProductName;
        do {
            inputProductName = UserInputUtils.readFromUser("Please enter the product name:");
            ErrorHandler.validateName(inputProductName);
        } while (!ValidationUtils.isValidName(inputProductName));
        return inputProductName;
    }

    public boolean isProductInList(String inputProductName) {
        return ProductRepository.findProductByName(inputProductName) != null;
    }

    private void handleNewProductAddition(String productName) {
        int unitPrice = getProductPrice();
        int quantity = getProductQuantity();
        productService.addNewProduct(productName, unitPrice, quantity);
    }

    private int getProductPrice() {
        int unitPrice;
        do {
            unitPrice = ErrorHandler.getValidNumber("\nPlease enter the product price:");
            ErrorHandler.validatePrice(unitPrice);
        } while (unitPrice < 0);
        return unitPrice;
    }

    private int getProductQuantity() {
        int quantity;
        do {
            quantity = ErrorHandler.getValidNumber("\nPlease enter the product quantity:");
            ErrorHandler.validateQuantity(quantity);
        } while (quantity < 1);
        return quantity;
    }

    private void handleProductIfInList(String inputProductName) {
        Product product = ProductRepository.findProductByName(inputProductName);
        if (product != null) {
            ProductDisplayHelper.displayExistingProductInfo(product);
            askUserForProductAction(product);
        } else {
            ProductDisplayHelper.displayProductNotFoundMessage(inputProductName);
        }
    }

    private void askUserForProductAction(Product product) {
        String askAddOrRemoveProduct = ErrorHandler.getYesOrNoOrDeleteAnswer(
                "Would you like to add to or subtract from the product quantity? (Y/N) " +
                        "To delete from the inventory, press \"D\"!");
        if (YES_OPTION.equalsIgnoreCase(askAddOrRemoveProduct)) {
            askAndUpdateProductQuantity(product);
        } else if (DELETE_OPTION.equalsIgnoreCase(askAddOrRemoveProduct)) {
            productService.deleteProduct(product);
        }
    }

    private void askAndUpdateProductQuantity(Product product) {
        String modifyProductAnswerAgain;
        do {
            productService.updateProductQuantity(product);
            modifyProductAnswerAgain = ErrorHandler.getYesOrNoAnswer(
                    "Would you like to modify the " + product.getProductName() + " quantity further? (Y/N)");
        } while (YES_OPTION.equalsIgnoreCase(modifyProductAnswerAgain));
    }
}
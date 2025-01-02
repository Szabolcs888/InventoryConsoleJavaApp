package com.myinventoryapp.services;

import com.myinventoryapp.repository.ProductRepository;
import com.myinventoryapp.entities.Product;
import com.myinventoryapp.util.Colors;
import com.myinventoryapp.util.ErrorHandler;
import com.myinventoryapp.util.IdUtils;
import com.myinventoryapp.util.displayhelpers.ProductDisplayHelper;

public class ProductService {

    public void addNewProduct(String productName, int unitPrice, int quantity) {
        String productId = "pr" + IdUtils.generateId();
        Product newProduct = new Product(productName, productId, unitPrice, quantity);
        ProductRepository.addProduct(newProduct);
        ProductDisplayHelper.displayProductInfoAfterGoodsReceipt(newProduct);
    }

    public void deleteProduct(Product product) {
        String yesOption = "Y";
        String productNameForDeletion = product.getProductName();
        String deleteConfirmation = ErrorHandler.getYesOrNoAnswer(
                Colors.RED.getColorCode() + "Are you sure you want to DELETE the product named " +
                        productNameForDeletion + " from the inventory? (Y/N)" + Colors.RESET.getColorCode());
        if (yesOption.equalsIgnoreCase(deleteConfirmation)) {
            ProductRepository.deleteProduct(product);
            System.out.println("\nThe item has been deleted!");
        }
    }

    public void updateProductQuantity(Product product, int quantitySold) {
        int productNewQuantity = product.getQuantity() - quantitySold;
        product.setQuantity(productNewQuantity);
    }

    public void updateProductQuantity(Product product) {
        int newQuantity;
        do {
            int quantityModification = getQuantityModification(product);
            newQuantity = calculateNewQuantity(product, quantityModification);
            ErrorHandler.validateNonNegativeQuantity(newQuantity);
        } while (newQuantity < 0);
        setNewQuantity(product, newQuantity);
        ProductDisplayHelper.displayProductInfoAfterSellAndUpdateGoodsReceipt(
                product, "PRODUCT INFORMATION AFTER RECEIPT TRANSACTION:");
    }

    private int getQuantityModification(Product product) {
        int quantityToBeModified;
        String productName = product.getProductName();
        if (product.getQuantity() == 0) {
            quantityToBeModified = ErrorHandler.getValidNumber(
                    "\nBy how much should we increase the " + productName + " quantity?");
        } else {
            quantityToBeModified = ErrorHandler.getValidNumber(
                    "\nBy how much should we increase or decrease the " + productName + " quantity?");
        }
        return quantityToBeModified;
    }

    private int calculateNewQuantity(Product product, int quantityModification) {
        int quantity = product.getQuantity();
        return quantity + quantityModification;
    }

    private void setNewQuantity(Product product, int newQuantity) {
        product.setQuantity(newQuantity);
        System.out.println("\nThe modification has been made!");
    }
}
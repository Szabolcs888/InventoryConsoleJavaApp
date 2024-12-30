package com.myinventoryapp.ui.menu;

import com.myinventoryapp.entities.Customer;
import com.myinventoryapp.entities.Product;
import com.myinventoryapp.repository.CustomerRepository;
import com.myinventoryapp.services.CustomerService;
import com.myinventoryapp.services.ProductService;
import com.myinventoryapp.services.TransactionService;
import com.myinventoryapp.util.*;
import com.myinventoryapp.util.displayhelpers.ProductDisplayHelper;
import com.myinventoryapp.repository.ProductRepository;

public class MenuOption1Sell {

    public void sellProduct(String text) {
        System.out.println(text);
        String askAnotherSell;
        do {
            if (ProductRepository.getProductList().isEmpty()) {
                ProductDisplayHelper.displayNoProductsAvailableMessage();
                askAnotherSell = "N";
            } else {
                processSale();
                askAnotherSell = ErrorHandler.getYesOrNoAnswer("Would you like to register another sale? (Y/N)");
                System.out.println();
            }
        } while ("Y".equalsIgnoreCase(askAnotherSell));
    }

    private void processSale() {
        CustomerService customerService = new CustomerService();
        ProductService productService = new ProductService();
        TransactionService transactionService = new TransactionService();
        String customerName = getCustomerName();
        Product productSold = getProductData();
        int quantitySold = getQuantitySold(productSold);
        boolean isRegisteredCustomer = isCustomerRegistered(customerName);
        String customerId = customerService.handleCustomerTransaction(
                isRegisteredCustomer, customerName, productSold, quantitySold);
        transactionService.transactionRegistration(
                isRegisteredCustomer, customerName, customerId, productSold, quantitySold);
        productService.updateProductQuantity(productSold, quantitySold);
        ProductDisplayHelper.displayProductInfoAfterSellAndUpdateGoodsReceipt(productSold,
                "PRODUCT DATA AFTER THE TRANSACTION:");
    }

    public String getCustomerName() {
        String customerName;
        do {
            customerName = UserInputUtils.readFromUser("Please enter the customer's name:");
            ErrorHandler.validateName(customerName);
        } while (!ValidationUtils.isValidName(customerName));
        return customerName;
    }

    public Product getProductData() {
        String inputProductName;
        boolean isProductInList;
        boolean isAvailableQuantityZero;
        Product foundProduct;
        do {
            isAvailableQuantityZero = false;
            inputProductName = UserInputUtils.readFromUser("\nPlease enter the name of the product to be sold:");
            foundProduct = ProductRepository.findProductByName(inputProductName);
            if (foundProduct != null) {
                ProductDisplayHelper.displayProductInfoIfProductFound(foundProduct);
                isProductInList = true;
                if (foundProduct.getQuantity() == 0) {
                    ProductDisplayHelper.displayOutOfStockMessage();
                    isAvailableQuantityZero = true;
                }
            } else {
                isProductInList = false;
                ProductDisplayHelper.displayProductNotFoundMessage(inputProductName);
            }
        } while (!isProductInList || isAvailableQuantityZero);
        return foundProduct;
    }

    public int getQuantitySold(Product product) {
        int quantitySold = 0;
        int availableQuantity = product.getQuantity();
        while (availableQuantity - quantitySold < 0 || quantitySold < 1) {
            quantitySold = ErrorHandler.getValidNumber("\nPlease enter the quantity to be sold:");
            ProductDisplayHelper.displayProductQuantityErrorMessage(product, availableQuantity, quantitySold);
        }
        return quantitySold;
    }

    public boolean isCustomerRegistered(String customerName) {
        Customer foundCustomer = CustomerRepository.findCustomerByName(customerName);
        return foundCustomer != null;
    }
}
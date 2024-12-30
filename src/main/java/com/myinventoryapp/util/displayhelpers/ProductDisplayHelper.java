package com.myinventoryapp.util.displayhelpers;

import com.myinventoryapp.entities.Product;
import com.myinventoryapp.util.Colors;
import com.myinventoryapp.repository.ProductRepository;

import java.util.List;

public class ProductDisplayHelper {

    public static void displayProductList(List<Product> productList, String text) {
        System.out.println(text);
        if (productList.isEmpty()) {
            System.out.println("There are currently no products in the inventory!");
        } else {
            System.out.println(Colors.GREEN.getColorCode() + "There are a total of " + productList.size() +
                    " items in the inventory:" + Colors.RESET.getColorCode());
            for (Product product : productList) {
                System.out.println("Name: " + product.getProductName() + ", Price: " + product.getUnitPrice() +
                        " HUF, Quantity: " + product.getQuantity() + ", Product ID: " + product.getProductId());
            }
        }
        System.out.println();
    }

    private static void displayProductInfo(
            String text, String productName, String productId, int unitPrice, int quantity) {
        System.out.println(Colors.GREEN.getColorCode() + "\n" + text + Colors.RESET.getColorCode());
        System.out.println("Product name: " + productName +
                ", Product ID: " + productId +
                ", Unit price: " + unitPrice + " HUF" +
                ", Available quantity: " + quantity);
    }

    public static void displayProductInfoAfterGoodsReceipt(Product newProduct) {
        System.out.println(Colors.GREEN.getColorCode() +
                "\nTHE PRODUCT HAS BEEN ADDED TO THE INVENTORY:" + Colors.RESET.getColorCode());
        System.out.println("Product name: " + newProduct.getProductName() +
                "\nProduct ID: " + newProduct.getProductId() +
                "\nUnit price: " + newProduct.getUnitPrice() + " HUF" +
                "\nAvailable quantity: " + newProduct.getQuantity());
    }

    public static void displayProductNotFoundMessage(String productName) {
        System.out.print(Colors.RED.getColorCode() + "\nThe product named \"" + productName +
                "\" is not in the inventory, please choose another product:" + Colors.RESET.getColorCode() + " ");
        for (Product product : ProductRepository.getProductList()) {
            System.out.print(product.getProductName() + ", ");
        }
        System.out.println();
    }

    public static void displayProductQuantityErrorMessage(Product product, int availableQuantity, int quantitySold) {
        if (availableQuantity - quantitySold < 0) {
            System.out.println(Colors.RED.getColorCode() + "\nThere are a total of " + product.getQuantity() + " " +
                    product.getProductName() + " in stock, you cannot sell more than that!" +
                    Colors.RESET.getColorCode());
        }
        if (quantitySold < 1) {
            System.out.println(
                    Colors.RED.getColorCode() + "\nThe quantity sold must be at least 1!" + Colors.RESET.getColorCode());
        }
    }

    public static void displayProductInfoIfProductFound(Product product) {
        displayProductInfo("DETAILS OF THE PRODUCT NAMED " + product.getProductName() + ":",
                product.getProductName(), product.getProductId(), product.getUnitPrice(), product.getQuantity());
    }

    public static void displayProductInfoAfterSellAndUpdateGoodsReceipt(Product product, String text) {
        displayProductInfo(
                text, product.getProductName(), product.getProductId(), product.getUnitPrice(), product.getQuantity());
    }

    public static void displayExistingProductInfo(Product product) {
        displayProductInfo("THE PRODUCT NAMED " + product.getProductName() + " IS ALREADY IN THE INVENTORY. DETAILS:",
                product.getProductName(), product.getProductId(), product.getUnitPrice(), product.getQuantity());
    }

    public static void displayOutOfStockMessage() {
        System.out.println(Colors.RED.getColorCode() + "\nThe product is in the inventory but currently out of stock." +
                " Please choose another!" + Colors.RESET.getColorCode());
    }

    public static void displayNoProductsAvailableMessage() {
        System.out.println("There are currently no products available for sale!\n");
    }
}
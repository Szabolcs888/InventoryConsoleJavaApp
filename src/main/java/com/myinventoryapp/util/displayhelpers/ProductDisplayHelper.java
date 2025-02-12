package com.myinventoryapp.util.displayhelpers;

import com.myinventoryapp.entities.Product;
import com.myinventoryapp.util.Colors;
import com.myinventoryapp.repository.ProductRepository;

import java.util.List;

public final class ProductDisplayHelper {

    private ProductDisplayHelper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static void displayProductList(List<Product> productList, String menuMessage) {
        System.out.println(menuMessage);
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

    static void displayProductInfo(
            String productInfoText, String productName, String productId, int unitPrice, int quantity) {
        System.out.println(Colors.GREEN.getColorCode() + "\n" + productInfoText + Colors.RESET.getColorCode());
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

    public static void displayProductQuantityErrorMessage(Product product, int quantitySold) {
        int zeroValue = 0;
        int minQuantityValue = 1;
        int availableQuantity = product.getQuantity();
        if (availableQuantity - quantitySold < zeroValue) {
            System.out.println(Colors.RED.getColorCode() + "\nThere are a total of " + availableQuantity + " " +
                    product.getProductName() + " in stock, you cannot sell more than that!" +
                    Colors.RESET.getColorCode());
        }
        if (quantitySold < minQuantityValue) {
            System.out.println(
                    Colors.RED.getColorCode() + "\nThe quantity sold must be at least 1!" + Colors.RESET.getColorCode());
        }
    }

    public static void displayProductInfoIfProductFound(Product product) {
        displayProductInfo("DETAILS OF THE PRODUCT NAMED " + product.getProductName() + ":",
                product.getProductName(), product.getProductId(), product.getUnitPrice(), product.getQuantity());
    }

    public static void displayProductInfoAfterSellAndUpdateGoodsReceipt(Product product, String productInfoText) {
        displayProductInfo(
                productInfoText, product.getProductName(), product.getProductId(), product.getUnitPrice(), product.getQuantity());
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
package com.myinventoryapp.util.testutils;

import com.myinventoryapp.services.CustomerService;
import com.myinventoryapp.services.ProductService;
import com.myinventoryapp.services.TransactionService;
import com.myinventoryapp.ui.menu.MenuOption1Sell;
import com.myinventoryapp.ui.menu.MenuOption2GoodsReceipt;

public class MenuOptionFactory {

    public static MenuOption1Sell createMenuOption1Sell() {
        CustomerService customerService = new CustomerService();
        ProductService productService = new ProductService();
        TransactionService transactionService = new TransactionService();
        return new MenuOption1Sell(customerService, productService, transactionService);
    }

    public static MenuOption2GoodsReceipt createMenuOption2GoodsReceipt() {
        ProductService productService = new ProductService();
        return new MenuOption2GoodsReceipt(productService);
    }
}
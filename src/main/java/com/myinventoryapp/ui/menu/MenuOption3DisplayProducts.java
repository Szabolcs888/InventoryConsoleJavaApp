package com.myinventoryapp.ui.menu;

import com.myinventoryapp.entities.Product;
import com.myinventoryapp.util.displayhelpers.ProductDisplayHelper;
import com.myinventoryapp.repository.ProductRepository;

import java.util.List;

public class MenuOption3DisplayProducts {

    public void displayProductList(String text) {
        List<Product> productList = ProductRepository.getProductList();
        ProductDisplayHelper.displayProductList(productList, text);
    }
}
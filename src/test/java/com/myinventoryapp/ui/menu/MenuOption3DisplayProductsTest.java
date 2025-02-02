package com.myinventoryapp.ui.menu;

import com.myinventoryapp.entities.Product;
import com.myinventoryapp.repository.ProductRepository;
import com.myinventoryapp.util.displayhelpers.ProductDisplayHelper;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

class MenuOption3DisplayProductsTest {

    @Test
    void testDisplayProductList_CallsRepositoryAndHelper() {
        String menuMessage = "\n-DISPLAY AVAILABLE PRODUCTS MENU-\n";
        List<Product> productList = Arrays.asList(
                new Product("banana", "pr5223508", 720, 102),
                new Product("tea", "pr7236284", 1200, 52),
                new Product("coffee", "pr5204875", 1800, 44)
        );

        try (MockedStatic<ProductRepository> mockedProductRepository = Mockito.mockStatic(ProductRepository.class);
             MockedStatic<ProductDisplayHelper> mockedProductDisplayHelper = Mockito.mockStatic(ProductDisplayHelper.class)) {

            mockedProductRepository.when(ProductRepository::getProductList).thenReturn(productList);
            mockedProductDisplayHelper.when(() -> ProductDisplayHelper.displayProductList(anyList(), anyString())).
                    thenAnswer(invocation -> null);

            MenuOption3DisplayProducts menuOption3DisplayProducts = new MenuOption3DisplayProducts();
            menuOption3DisplayProducts.displayProductList(menuMessage);

            mockedProductRepository.verify(ProductRepository::getProductList, times(1));
            mockedProductDisplayHelper.verify(() -> ProductDisplayHelper.displayProductList(
                    productList, menuMessage), times(1));
        }
    }

    @Test
    void testDisplayProductList_CallsRepositoryAndHelper_EmptyList() {
        String menuMessage = "\n-DISPLAY AVAILABLE PRODUCTS MENU-\n";

        try (MockedStatic<ProductRepository> mockedProductRepository = Mockito.mockStatic(ProductRepository.class);
             MockedStatic<ProductDisplayHelper> mockedProductDisplayHelper = Mockito.mockStatic(ProductDisplayHelper.class)) {

            mockedProductRepository.when(ProductRepository::getProductList).thenReturn(Collections.emptyList());
            mockedProductDisplayHelper.when(() -> ProductDisplayHelper.displayProductList(anyList(), anyString())).
                    thenAnswer(invocation -> null);

            MenuOption3DisplayProducts menuOption3DisplayProducts = new MenuOption3DisplayProducts();
            menuOption3DisplayProducts.displayProductList(menuMessage);

            mockedProductRepository.verify(ProductRepository::getProductList, times(1));
            mockedProductDisplayHelper.verify(() -> ProductDisplayHelper.displayProductList(
                    Collections.emptyList(), menuMessage), times(1));
        }
    }
}
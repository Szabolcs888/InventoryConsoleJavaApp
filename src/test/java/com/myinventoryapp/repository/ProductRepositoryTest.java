package com.myinventoryapp.repository;

import com.myinventoryapp.entities.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductRepositoryTest {

    @BeforeEach
    void setup() {
        ProductRepository.clearProductList();
    }

    @Test
    void testGetProductList_EmptyList() {
        List<Product> products = ProductRepository.getProductList();

        assertTrue(products.isEmpty(), "The products list should be empty initially.");
        assertNotSame(products, ProductRepository.getProductList(),
                "The returned list should be a new instance.");
    }

    @Test
    void testGetProductList_ModificationDoesNotAffectOriginal() {
        List<Product> products = ProductRepository.getProductList();
        products.add(new Product("apple", "pr5197140", 560, 25));

        assertTrue(ProductRepository.getProductList().isEmpty(),
                "The original product list should not be affected by modifications to the returned list.");
    }

    @Test
    void testGetProductList_AfterAddingProducts() {
        Product product1 = new Product("pear", "pr4270613", 675, 19);
        Product product2 = new Product("banana", "pr5223508", 720, 102);
        ProductRepository.addProduct(product1);
        ProductRepository.addProduct(product2);

        List<Product> products = ProductRepository.getProductList();

        assertEquals(2, products.size(), "The products list should contain two products.");
        assertTrue(products.contains(product1),
                "The products list should contain 'pear'.");
        assertTrue(products.contains(product2),
                "The products list should contain 'banana'.");
    }

    @Test
    void testFindProductByName_EmptyList() {
        assertNull(ProductRepository.findProductByName("coconut"),
                "Expected null when searching in an empty list.");
    }

    @Test
    void testFindProductByName() {
        Product product1 = new Product("tea", "pr7236284", 1200, 52);
        Product product2 = new Product("coffee", "pr5204875", 1800, 44);
        ProductRepository.addProduct(product1);
        ProductRepository.addProduct(product2);

        Product result = ProductRepository.findProductByName("coffee");

        assertNotNull(result, "Expected to find the product 'coffee'.");
        assertEquals(product2, result);
    }

    @Test
    void testFindProductByName_CaseInsensitiveSearch() {
        Product product = new Product("orange juice", "pr7987615", 870, 89);
        ProductRepository.addProduct(product);

        Product result = ProductRepository.findProductByName("oRAngE JUiCe");

        assertNotNull(result, "Expected to find the product 'orange juice' with case-insensitive search.");
        assertEquals(product, result, "The returned product does not match the expected one.");
    }

    @Test
    void testFindProductByName_ReturnsNullForNonExistentName() {
        Product product = new Product("orange juice", "pr7987615", 870, 89);
        ProductRepository.addProduct(product);

        Product result = ProductRepository.findProductByName("fruit juice");

        assertNull(result, "Expected null when the product name does not exist.");
    }

    @Test
    void testFindProductByName_ReturnsFirstMatch() {
        Product product1 = new Product("pineapple", "pr5711807", 894, 36);
        Product product2 = new Product("pineapple", "pr5205725", 1800, 44);
        ProductRepository.addProduct(product1);
        ProductRepository.addProduct(product2);

        Product result = ProductRepository.findProductByName("pineapple");

        assertNotNull(result, "Expected to find the first product named 'pineapple'.");
        assertEquals(product1, result, "The returned product does not match the first match.");
    }

    @Test
    void testDeleteProduct() {
        Product product1 = new Product("cherry", "pr7860912", 452, 115);
        Product product2 = new Product("mango", "pr4531265", 1350, 46);
        Product product3 = new Product("carrot", "pr2110710", 245, 31);
        ProductRepository.addProduct(product1);
        ProductRepository.addProduct(product2);
        ProductRepository.addProduct(product3);

        ProductRepository.deleteProduct(product2);

        List<Product> products = ProductRepository.getProductList();
        assertEquals(2, ProductRepository.getProductList().size(),
                "The products list should contain two products after deletion.");
        assertTrue(products.contains(product1), "The product list should still contain 'cherry'.");
        assertTrue(products.contains(product3), "The product list should still contain 'carrot'.");
        assertFalse(products.contains(product2), "The product list should not contain 'mango'.");
    }
}
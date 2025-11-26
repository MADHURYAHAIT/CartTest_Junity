package com.example.cart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Cart Backward Compatibility Tests")
class CartBackwardCompatibilityTest {

    private Cart cart;

    @BeforeEach
    void setUp() {
        cart = new Cart();
    }

    @Nested
    @DisplayName("String-Based Add Tests")
    class StringBasedAddTests {

        @Test
        @DisplayName("Should add product by string name")
        void testAddProductByStringValid() {
            assertTrue(cart.addProduct("Laptop"));
            assertTrue(cart.containsProduct("Laptop"));
        }

        @Test
        @DisplayName("Should return false when adding duplicate string")
        void testAddProductByStringDuplicate() {
            assertTrue(cart.addProduct("Mouse"));
            assertFalse(cart.addProduct("Mouse"));
            assertEquals(1, cart.getUniqueProductCount());
        }

        @Test
        @DisplayName("Should throw exception for null string")
        void testAddProductByStringNull() {
            assertThrows(IllegalArgumentException.class,
                () -> cart.addProduct((String) null));
        }

        @Test
        @DisplayName("Should throw exception for empty string")
        void testAddProductByStringEmpty() {
            assertThrows(IllegalArgumentException.class,
                () -> cart.addProduct(""));
        }

        @Test
        @DisplayName("Should create product with zero price and General category")
        void testAddProductByStringCreatesZeroPriceProduct() {
            cart.addProduct("TestProduct");
            assertEquals(0.0, cart.getSubtotal(), 0.001);
        }
    }

    @Nested
    @DisplayName("String-Based Remove Tests")
    class StringBasedRemoveTests {

        @Test
        @DisplayName("Should remove product by string name")
        void testRemoveProductByStringExists() {
            cart.addProduct("Keyboard");
            assertTrue(cart.removeProduct("Keyboard"));
            assertFalse(cart.containsProduct("Keyboard"));
        }

        @Test
        @DisplayName("Should return false when removing nonexistent product")
        void testRemoveProductByStringNotExists() {
            assertFalse(cart.removeProduct("Nonexistent"));
        }

        @Test
        @DisplayName("Should handle null string gracefully")
        void testRemoveProductByStringNull() {
            assertFalse(cart.removeProduct((String) null));
        }
    }

    @Nested
    @DisplayName("String-Based Query Tests")
    class StringBasedQueryTests {

        @Test
        @DisplayName("Should find product by string name")
        void testContainsProductByStringExists() {
            cart.addProduct("Monitor");
            assertTrue(cart.containsProduct("Monitor"));
        }

        @Test
        @DisplayName("Should return false for nonexistent product")
        void testContainsProductByStringNotExists() {
            assertFalse(cart.containsProduct("Nonexistent"));
        }

        @Test
        @DisplayName("Should handle null string gracefully")
        void testContainsProductByStringNull() {
            assertFalse(cart.containsProduct((String) null));
        }

        @Test
        @DisplayName("Should be case-sensitive")
        void testContainsProductByStringCaseSensitive() {
            cart.addProduct("Laptop");
            assertFalse(cart.containsProduct("laptop"));
            assertFalse(cart.containsProduct("LAPTOP"));
        }
    }

    @Nested
    @DisplayName("GetItems Tests")
    class GetItemsTests {

        @Test
        @DisplayName("Should return list of product names")
        void testGetItemsReturnsProductNames() {
            cart.addProduct("Laptop");
            cart.addProduct("Mouse");
            cart.addProduct("Keyboard");

            List<String> items = cart.getItems();
            assertEquals(3, items.size());
            assertTrue(items.contains("Laptop"));
            assertTrue(items.contains("Mouse"));
            assertTrue(items.contains("Keyboard"));
        }

        @Test
        @DisplayName("Should return empty list for empty cart")
        void testGetItemsEmptyCart() {
            List<String> items = cart.getItems();
            assertNotNull(items);
            assertTrue(items.isEmpty());
        }

        @Test
        @DisplayName("Should not include duplicates even with quantity > 1")
        void testGetItemsDoesNotIncludeDuplicates() {
            Product laptop = TestDataFactory.createLaptop();
            cart.addProduct(laptop, 5);

            List<String> items = cart.getItems();
            assertEquals(1, items.size());
            assertEquals(1, items.stream().filter(item -> item.equals("Laptop")).count());
        }
    }

    @Nested
    @DisplayName("Mixed API Tests")
    class MixedAPITests {

        @Test
        @DisplayName("Should remove by string after adding Product object")
        void testAddProductObjectAndRemoveByString() {
            Product laptop = TestDataFactory.createLaptop();
            cart.addProduct(laptop);
            assertTrue(cart.removeProduct("Laptop"));
            assertFalse(cart.containsProduct(laptop));
        }

        @Test
        @DisplayName("Should query by Product after adding by string")
        void testAddByStringAndQueryByProduct() {
            cart.addProduct("Mouse");
            Product mouse = new Product("Mouse", 0.0, "General");
            assertTrue(cart.containsProduct(mouse));
        }

        @Test
        @DisplayName("Should handle quantity properly with mixed API")
        void testMixedAPIQuantityHandling() {
            cart.addProduct("Keyboard");
            assertEquals(1, cart.getItemCount());

            Product keyboard = new Product("Keyboard", 0.0, "General");
            cart.addProduct(keyboard, 3);
            assertEquals(4, cart.getItemCount());
        }
    }
}

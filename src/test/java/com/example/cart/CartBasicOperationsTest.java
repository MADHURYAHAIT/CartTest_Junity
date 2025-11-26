package com.example.cart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Cart Basic Operations")
class CartBasicOperationsTest {

    private Cart cart;

    @BeforeEach
    void setUp() {
        cart = new Cart();
    }

    @Nested
    @DisplayName("Initialization Tests")
    class InitializationTests {

        @Test
        @DisplayName("Should create empty cart")
        void testNewCartIsEmpty() {
            assertTrue(cart.isEmpty());
            assertEquals(0, cart.getItemCount());
        }

        @Test
        @DisplayName("Should have zero subtotal")
        void testNewCartHasZeroSubtotal() {
            assertEquals(0.0, cart.getSubtotal(), 0.001);
        }

        @Test
        @DisplayName("Should have no promotions")
        void testNewCartHasNoPromotions() {
            assertTrue(cart.getActivePromotions().isEmpty());
        }

        @Test
        @DisplayName("Should have zero unique products")
        void testNewCartHasZeroUniqueProducts() {
            assertEquals(0, cart.getUniqueProductCount());
        }
    }

    @Nested
    @DisplayName("Add Product Operations")
    class AddProductTests {

        @Test
        @DisplayName("Should add single product")
        void testAddSingleProduct() {
            Product laptop = TestDataFactory.createLaptop();
            assertTrue(cart.addProduct(laptop));
            assertTrue(cart.containsProduct(laptop));
            assertEquals(1, cart.getQuantity(laptop));
        }

        @Test
        @DisplayName("Should add product with specified quantity")
        void testAddProductWithQuantity() {
            Product mouse = TestDataFactory.createMouse();
            assertTrue(cart.addProduct(mouse, 5));
            assertEquals(5, cart.getQuantity(mouse));
        }

        @Test
        @DisplayName("Should accumulate quantity when adding same product twice")
        void testAddSameProductTwiceIncrementsQuantity() {
            Product keyboard = TestDataFactory.createKeyboard();
            cart.addProduct(keyboard, 3);
            cart.addProduct(keyboard, 2);
            assertEquals(5, cart.getQuantity(keyboard));
        }

        @Test
        @DisplayName("Should throw exception for null product")
        void testAddNullProduct() {
            assertThrows(IllegalArgumentException.class,
                () -> cart.addProduct(null));
        }

        @Test
        @DisplayName("Should throw exception for zero quantity")
        void testAddProductWithZeroQuantity() {
            Product book = TestDataFactory.createBook();
            assertThrows(IllegalArgumentException.class,
                () -> cart.addProduct(book, 0));
        }

        @Test
        @DisplayName("Should throw exception for negative quantity")
        void testAddProductWithNegativeQuantity() {
            Product shirt = TestDataFactory.createShirt();
            assertThrows(IllegalArgumentException.class,
                () -> cart.addProduct(shirt, -5));
        }

        @Test
        @DisplayName("Should add multiple different products")
        void testAddMultipleDifferentProducts() {
            cart.addProduct(TestDataFactory.createLaptop());
            cart.addProduct(TestDataFactory.createMouse());
            cart.addProduct(TestDataFactory.createKeyboard());
            assertEquals(3, cart.getUniqueProductCount());
        }

        @Test
        @DisplayName("Should handle large quantities")
        void testAddProductWithLargeQuantity() {
            Product book = TestDataFactory.createBook();
            assertTrue(cart.addProduct(book, 10000));
            assertEquals(10000, cart.getQuantity(book));
        }

        @Test
        @DisplayName("Should accumulate quantity over multiple additions")
        void testAddSameProductMultipleTimes() {
            Product mouse = TestDataFactory.createMouse();
            cart.addProduct(mouse, 2);
            cart.addProduct(mouse, 3);
            cart.addProduct(mouse, 5);
            assertEquals(10, cart.getQuantity(mouse));
        }
    }

    @Nested
    @DisplayName("Remove Product Operations")
    class RemoveProductTests {

        @Test
        @DisplayName("Should remove existing product")
        void testRemoveExistingProduct() {
            Product laptop = TestDataFactory.createLaptop();
            cart.addProduct(laptop);
            assertTrue(cart.removeProduct(laptop));
            assertFalse(cart.containsProduct(laptop));
        }

        @Test
        @DisplayName("Should return false when removing nonexistent product")
        void testRemoveNonexistentProduct() {
            Product mouse = TestDataFactory.createMouse();
            assertFalse(cart.removeProduct(mouse));
        }

        @Test
        @DisplayName("Should return false when removing null product")
        void testRemoveNullProduct() {
            assertFalse(cart.removeProduct((Product) null));
        }

        @Test
        @DisplayName("Should return false when removing from empty cart")
        void testRemoveProductFromEmptyCart() {
            Product keyboard = TestDataFactory.createKeyboard();
            assertFalse(cart.removeProduct(keyboard));
        }

        @Test
        @DisplayName("Should decrement quantity when removing one unit")
        void testRemoveProductUnit() {
            Product book = TestDataFactory.createBook();
            cart.addProduct(book, 5);
            assertTrue(cart.removeProductUnit(book));
            assertEquals(4, cart.getQuantity(book));
        }

        @Test
        @DisplayName("Should remove product when removing last unit")
        void testRemoveProductUnitLastItem() {
            Product shirt = TestDataFactory.createShirt();
            cart.addProduct(shirt);
            assertTrue(cart.removeProductUnit(shirt));
            assertFalse(cart.containsProduct(shirt));
        }

        @Test
        @DisplayName("Should return false when removing unit of nonexistent product")
        void testRemoveProductUnitNonexistent() {
            Product laptop = TestDataFactory.createLaptop();
            assertFalse(cart.removeProductUnit(laptop));
        }

        @Test
        @DisplayName("Should return false when removing unit with null product")
        void testRemoveProductUnitNull() {
            assertFalse(cart.removeProductUnit(null));
        }

        @Test
        @DisplayName("Should remove all units of product")
        void testRemoveProductCompletely() {
            Product mouse = TestDataFactory.createMouse();
            cart.addProduct(mouse, 5);
            assertTrue(cart.removeProduct(mouse));
            assertEquals(0, cart.getQuantity(mouse));
        }
    }

    @Nested
    @DisplayName("Update Quantity Operations")
    class UpdateQuantityTests {

        @Test
        @DisplayName("Should increase quantity")
        void testUpdateQuantityIncrease() {
            Product keyboard = TestDataFactory.createKeyboard();
            cart.addProduct(keyboard, 2);
            assertTrue(cart.updateQuantity(keyboard, 5));
            assertEquals(5, cart.getQuantity(keyboard));
        }

        @Test
        @DisplayName("Should decrease quantity")
        void testUpdateQuantityDecrease() {
            Product laptop = TestDataFactory.createLaptop();
            cart.addProduct(laptop, 5);
            assertTrue(cart.updateQuantity(laptop, 2));
            assertEquals(2, cart.getQuantity(laptop));
        }

        @Test
        @DisplayName("Should remove product when setting quantity to zero")
        void testUpdateQuantityToZero() {
            Product mouse = TestDataFactory.createMouse();
            cart.addProduct(mouse, 3);
            assertTrue(cart.updateQuantity(mouse, 0));
            assertFalse(cart.containsProduct(mouse));
        }

        @Test
        @DisplayName("Should return false for nonexistent product")
        void testUpdateQuantityNonexistentProduct() {
            Product book = TestDataFactory.createBook();
            assertFalse(cart.updateQuantity(book, 5));
        }

        @Test
        @DisplayName("Should throw exception for null product")
        void testUpdateQuantityWithNull() {
            assertThrows(IllegalArgumentException.class,
                () -> cart.updateQuantity(null, 5));
        }

        @Test
        @DisplayName("Should throw exception for negative quantity")
        void testUpdateQuantityNegative() {
            Product shirt = TestDataFactory.createShirt();
            cart.addProduct(shirt);
            assertThrows(IllegalArgumentException.class,
                () -> cart.updateQuantity(shirt, -1));
        }

        @Test
        @DisplayName("Should handle updating to same quantity")
        void testUpdateQuantityToSameValue() {
            Product keyboard = TestDataFactory.createKeyboard();
            cart.addProduct(keyboard, 3);
            assertTrue(cart.updateQuantity(keyboard, 3));
            assertEquals(3, cart.getQuantity(keyboard));
        }
    }

    @Nested
    @DisplayName("Query Operations")
    class QueryOperationsTests {

        @Test
        @DisplayName("Should return correct quantity for existing product")
        void testGetQuantityExistingProduct() {
            Product laptop = TestDataFactory.createLaptop();
            cart.addProduct(laptop, 7);
            assertEquals(7, cart.getQuantity(laptop));
        }

        @Test
        @DisplayName("Should return zero for nonexistent product")
        void testGetQuantityNonexistentProduct() {
            Product mouse = TestDataFactory.createMouse();
            assertEquals(0, cart.getQuantity(mouse));
        }

        @Test
        @DisplayName("Should return true for existing product")
        void testContainsProductExists() {
            Product keyboard = TestDataFactory.createKeyboard();
            cart.addProduct(keyboard);
            assertTrue(cart.containsProduct(keyboard));
        }

        @Test
        @DisplayName("Should return false for nonexistent product")
        void testContainsProductNotExists() {
            Product book = TestDataFactory.createBook();
            assertFalse(cart.containsProduct(book));
        }

        @Test
        @DisplayName("Should return correct item count for single product")
        void testGetItemCountWithSingleProduct() {
            Product shirt = TestDataFactory.createShirt();
            cart.addProduct(shirt, 3);
            assertEquals(3, cart.getItemCount());
        }

        @Test
        @DisplayName("Should return sum of all quantities")
        void testGetItemCountWithMultipleProducts() {
            cart.addProduct(TestDataFactory.createLaptop(), 2);
            cart.addProduct(TestDataFactory.createMouse(), 5);
            cart.addProduct(TestDataFactory.createKeyboard(), 3);
            assertEquals(10, cart.getItemCount());
        }

        @Test
        @DisplayName("Should return correct unique product count")
        void testGetUniqueProductCountMultiple() {
            cart.addProduct(TestDataFactory.createLaptop(), 5);
            cart.addProduct(TestDataFactory.createMouse(), 3);
            cart.addProduct(TestDataFactory.createKeyboard(), 1);
            assertEquals(3, cart.getUniqueProductCount());
        }

        @Test
        @DisplayName("Should return map with all products and quantities")
        void testGetProductsWithQuantities() {
            Product laptop = TestDataFactory.createLaptop();
            Product mouse = TestDataFactory.createMouse();
            cart.addProduct(laptop, 2);
            cart.addProduct(mouse, 3);

            Map<Product, Integer> products = cart.getProductsWithQuantities();
            assertEquals(2, products.size());
            assertEquals(2, products.get(laptop));
            assertEquals(3, products.get(mouse));
        }

        @Test
        @DisplayName("Should return defensive copy of products map")
        void testGetProductsWithQuantitiesReturnsDefensiveCopy() {
            Product keyboard = TestDataFactory.createKeyboard();
            cart.addProduct(keyboard, 5);

            Map<Product, Integer> products = cart.getProductsWithQuantities();
            products.clear();

            assertEquals(5, cart.getQuantity(keyboard));
        }
    }

    @Nested
    @DisplayName("Clear Cart Operations")
    class ClearCartTests {

        @Test
        @DisplayName("Should clear already empty cart")
        void testClearEmptyCart() {
            cart.clearCart();
            assertTrue(cart.isEmpty());
        }

        @Test
        @DisplayName("Should clear cart with items")
        void testClearCartWithItems() {
            cart.addProduct(TestDataFactory.createLaptop(), 2);
            cart.addProduct(TestDataFactory.createMouse(), 3);
            cart.clearCart();
            assertTrue(cart.isEmpty());
            assertEquals(0, cart.getItemCount());
        }

        @Test
        @DisplayName("Should allow adding products after clearing")
        void testClearCartDoesNotAffectPromotions() {
            cart.addProduct(TestDataFactory.createLaptop());
            cart.applyDiscount(10.0);
            cart.clearCart();

            cart.addProduct(TestDataFactory.createMouse());
            assertEquals(1, cart.getUniqueProductCount());
        }
    }
}

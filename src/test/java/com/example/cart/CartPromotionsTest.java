package com.example.cart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Cart Promotions Tests")
class CartPromotionsTest {

    private Cart cart;

    @BeforeEach
    void setUp() {
        cart = new Cart();
    }

    @Nested
    @DisplayName("Add Promotion Tests")
    class AddPromotionTests {

        @Test
        @DisplayName("Should add valid promotion")
        void testAddValidPromotion() {
            cart.addPromotion("Laptop", 50.0);
            assertTrue(cart.getActivePromotions().containsKey("Laptop"));
            assertEquals(50.0, cart.getActivePromotions().get("Laptop"), 0.001);
        }

        @Test
        @DisplayName("Should throw exception for null product name")
        void testAddPromotionWithNullProductName() {
            assertThrows(IllegalArgumentException.class,
                () -> cart.addPromotion(null, 10.0));
        }

        @Test
        @DisplayName("Should throw exception for empty product name")
        void testAddPromotionWithEmptyProductName() {
            assertThrows(IllegalArgumentException.class,
                () -> cart.addPromotion("", 10.0));
        }

        @Test
        @DisplayName("Should throw exception for negative discount")
        void testAddPromotionWithNegativeDiscount() {
            assertThrows(IllegalArgumentException.class,
                () -> cart.addPromotion("Mouse", -5.0));
        }

        @Test
        @DisplayName("Should accept zero discount")
        void testAddPromotionWithZeroDiscount() {
            cart.addPromotion("Keyboard", 0.0);
            assertTrue(cart.getActivePromotions().containsKey("Keyboard"));
        }

        @Test
        @DisplayName("Should add multiple promotions for different products")
        void testAddMultiplePromotionsDifferentProducts() {
            cart.addPromotion("Laptop", 100.0);
            cart.addPromotion("Mouse", 5.0);
            cart.addPromotion("Keyboard", 15.0);
            assertEquals(3, cart.getActivePromotions().size());
        }

        @Test
        @DisplayName("Should overwrite existing promotion")
        void testAddPromotionOverwritesExisting() {
            cart.addPromotion("Laptop", 50.0);
            cart.addPromotion("Laptop", 100.0);
            assertEquals(100.0, cart.getActivePromotions().get("Laptop"), 0.001);
        }
    }

    @Nested
    @DisplayName("Remove Promotion Tests")
    class RemovePromotionTests {

        @Test
        @DisplayName("Should remove existing promotion")
        void testRemoveExistingPromotion() {
            cart.addPromotion("Mouse", 5.0);
            cart.removePromotion("Mouse");
            assertFalse(cart.getActivePromotions().containsKey("Mouse"));
        }

        @Test
        @DisplayName("Should handle removing nonexistent promotion")
        void testRemoveNonexistentPromotion() {
            cart.removePromotion("Laptop");
            assertTrue(cart.getActivePromotions().isEmpty());
        }

        @Test
        @DisplayName("Should handle removing null promotion")
        void testRemoveNullPromotion() {
            cart.addPromotion("Mouse", 5.0);
            cart.removePromotion(null);
            assertEquals(1, cart.getActivePromotions().size());
        }
    }

    @Nested
    @DisplayName("Clear Promotions Tests")
    class ClearPromotionsTests {

        @Test
        @DisplayName("Should clear empty promotions")
        void testClearPromotionsEmpty() {
            cart.clearPromotions();
            assertTrue(cart.getActivePromotions().isEmpty());
        }

        @Test
        @DisplayName("Should clear all promotions")
        void testClearPromotionsWithActivePromotions() {
            cart.addPromotion("Laptop", 50.0);
            cart.addPromotion("Mouse", 5.0);
            cart.clearPromotions();
            assertTrue(cart.getActivePromotions().isEmpty());
        }

        @Test
        @DisplayName("Should reset percentage discount")
        void testClearPromotionsResetsPercentageDiscount() {
            cart.applyDiscount(20.0);
            cart.addPromotion("Laptop", 50.0);
            cart.clearPromotions();
            assertEquals(0.0, cart.getDiscountPercentage(), 0.001);
        }
    }

    @Nested
    @DisplayName("Get Active Promotions Tests")
    class GetActivePromotionsTests {

        @Test
        @DisplayName("Should return empty map when no promotions")
        void testGetActivePromotionsEmpty() {
            assertTrue(cart.getActivePromotions().isEmpty());
        }

        @Test
        @DisplayName("Should return all active promotions")
        void testGetActivePromotions() {
            cart.addPromotion("Laptop", 100.0);
            cart.addPromotion("Mouse", 5.0);
            Map<String, Double> promotions = cart.getActivePromotions();
            assertEquals(2, promotions.size());
            assertEquals(100.0, promotions.get("Laptop"), 0.001);
            assertEquals(5.0, promotions.get("Mouse"), 0.001);
        }

        @Test
        @DisplayName("Should return defensive copy")
        void testGetActivePromotionsReturnsDefensiveCopy() {
            cart.addPromotion("Laptop", 50.0);
            Map<String, Double> promotions = cart.getActivePromotions();
            promotions.clear();
            assertEquals(1, cart.getActivePromotions().size());
        }
    }

    @Nested
    @DisplayName("Promotion Discount Calculations")
    class PromotionDiscountCalculationsTests {

        @Test
        @DisplayName("Should apply promotion to single product")
        void testPromotionDiscountSingleProduct() {
            Product laptop = TestDataFactory.createLaptop();
            cart.addProduct(laptop, 1);
            cart.addPromotion("Laptop", 50.0);
            assertEquals(50.0, cart.getDiscountAmount(), 0.001);
        }

        @Test
        @DisplayName("Should multiply promotion by quantity")
        void testPromotionDiscountWithQuantity() {
            Product mouse = TestDataFactory.createMouse();
            cart.addProduct(mouse, 3);
            cart.addPromotion("Mouse", 5.0);
            assertEquals(15.0, cart.getDiscountAmount(), 0.001);
        }

        @Test
        @DisplayName("Should sum multiple promotions")
        void testPromotionDiscountMultipleProducts() {
            Product laptop = TestDataFactory.createLaptop();
            Product mouse = TestDataFactory.createMouse();
            cart.addProduct(laptop, 1);
            cart.addProduct(mouse, 2);
            cart.addPromotion("Laptop", 100.0);
            cart.addPromotion("Mouse", 5.0);
            assertEquals(110.0, cart.getDiscountAmount(), 0.001);
        }

        @Test
        @DisplayName("Should return zero for promotion on product not in cart")
        void testPromotionDiscountForNonexistentProduct() {
            cart.addPromotion("Laptop", 50.0);
            assertEquals(0.0, cart.getDiscountAmount(), 0.001);
        }
    }

    @Nested
    @DisplayName("Combined Discount Tests")
    class CombinedDiscountTests {

        @Test
        @DisplayName("Should combine percentage and promotion discounts")
        void testPercentageAndPromotionCombined() {
            Product laptop = TestDataFactory.createLaptop();
            cart.addProduct(laptop);
            cart.applyDiscount(10.0);
            cart.addPromotion("Laptop", 50.0);

            double percentageDiscount = 999.99 * 0.10;
            double promotionDiscount = 50.0;
            double expectedTotal = 999.99 - percentageDiscount - promotionDiscount;

            assertEquals(expectedTotal, cart.getTotal(), 0.01);
        }

        @Test
        @DisplayName("Should handle multiple promotions with percentage")
        void testMultiplePromotionsAndPercentage() {
            Product laptop = TestDataFactory.createLaptop();
            Product mouse = TestDataFactory.createMouse();
            cart.addProduct(laptop);
            cart.addProduct(mouse, 2);

            cart.applyDiscount(10.0);
            cart.addPromotion("Laptop", 100.0);
            cart.addPromotion("Mouse", 5.0);

            double subtotal = 999.99 + (29.99 * 2);
            double percentageDiscount = subtotal * 0.10;
            double promotionDiscount = 100.0 + (5.0 * 2);
            double expectedTotal = subtotal - percentageDiscount - promotionDiscount;

            assertEquals(expectedTotal, cart.getTotal(), 0.01);
        }

        @Test
        @DisplayName("Should not go negative when discounts exceed subtotal")
        void testCombinedDiscountsExceedSubtotal() {
            Product mouse = TestDataFactory.createMouse();
            cart.addProduct(mouse);
            cart.applyDiscount(100.0);
            cart.addPromotion("Mouse", 100.0);

            assertEquals(0.0, cart.getTotal(), 0.001);
        }

        @Test
        @DisplayName("Should apply discounts in consistent order")
        void testDiscountCalculationOrder() {
            Product laptop = TestDataFactory.createLaptop();
            cart.addProduct(laptop);

            cart.applyDiscount(10.0);
            cart.addPromotion("Laptop", 50.0);
            double total1 = cart.getTotal();

            Cart cart2 = new Cart();
            cart2.addProduct(laptop);
            cart2.addPromotion("Laptop", 50.0);
            cart2.applyDiscount(10.0);
            double total2 = cart2.getTotal();

            assertEquals(total1, total2, 0.001);
        }

        @Test
        @DisplayName("Should apply promotion only to matching product")
        void testPromotionAppliedToCorrectProduct() {
            Product laptop = TestDataFactory.createLaptop();
            Product mouse = TestDataFactory.createMouse();
            cart.addProduct(laptop);
            cart.addProduct(mouse);

            cart.addPromotion("Laptop", 100.0);

            double expected = (999.99 + 29.99) - 100.0;
            assertEquals(expected, cart.getTotal(), 0.01);
        }
    }
}

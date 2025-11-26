package com.example.cart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Cart Integration Tests")
class CartIntegrationTest {

    private Cart cart;

    @BeforeEach
    void setUp() {
        cart = new Cart();
    }

    @Nested
    @DisplayName("Complex Shopping Scenarios")
    class ComplexShoppingScenarios {

        @Test
        @DisplayName("Should handle complete shopping journey")
        void testCompleteShoppingJourney() {
            cart.addProduct(TestDataFactory.createLaptop(), 2);
            cart.addProduct(TestDataFactory.createMouse(), 3);
            cart.addProduct(TestDataFactory.createKeyboard());
            cart.addProduct(TestDataFactory.createBook(), 2);
            cart.addProduct(TestDataFactory.createShirt(), 5);

            cart.applyDiscount(15.0);
            cart.addPromotion("Laptop", 100.0);
            cart.addPromotion("Mouse", 5.0);

            cart.updateQuantity(TestDataFactory.createShirt(), 3);
            cart.removeProduct(TestDataFactory.createBook());

            assertEquals(4, cart.getUniqueProductCount());
            assertTrue(cart.getTotal() > 0);
        }

        @Test
        @DisplayName("Should handle products from multiple categories")
        void testMultipleCategoriesAndPricing() {
            cart.addProduct(TestDataFactory.createLaptop());
            cart.addProduct(TestDataFactory.createBook(), 3);
            cart.addProduct(TestDataFactory.createShirt(), 2);

            double expectedSubtotal = 999.99 + (49.99 * 3) + (19.99 * 2);
            assertEquals(expectedSubtotal, cart.getSubtotal(), 0.01);
        }

        @Test
        @DisplayName("Should handle bulk add and remove operations")
        void testBulkAddAndRemove() {
            for (int i = 0; i < 20; i++) {
                Product product = new Product("Product" + i, i * 10.0, "Category");
                cart.addProduct(product, i + 1);
            }
            assertEquals(20, cart.getUniqueProductCount());

            for (int i = 0; i < 10; i++) {
                Product product = new Product("Product" + i, i * 10.0, "Category");
                cart.removeProduct(product);
            }
            assertEquals(10, cart.getUniqueProductCount());
        }

        @Test
        @DisplayName("Should handle clearing and refilling cart")
        void testEmptyCartMultipleTimes() {
            cart.addProduct(TestDataFactory.createLaptop());
            cart.clearCart();
            assertTrue(cart.isEmpty());

            cart.addProduct(TestDataFactory.createMouse());
            cart.clearCart();
            assertTrue(cart.isEmpty());

            cart.addProduct(TestDataFactory.createKeyboard());
            assertEquals(1, cart.getUniqueProductCount());
        }
    }

    @Nested
    @DisplayName("Promotion Scenarios")
    class PromotionScenarios {

        @Test
        @DisplayName("Should handle seasonal sale scenario")
        void testSeasonalSaleScenario() {
            cart.addProduct(TestDataFactory.createLaptop());
            cart.addProduct(TestDataFactory.createMouse(), 2);

            cart.applyDiscount(20.0);
            cart.addPromotion("Laptop", 100.0);
            cart.addPromotion("Mouse", 5.0);

            double subtotal = 999.99 + (29.99 * 2);
            double percentDiscount = subtotal * 0.20;
            double promoDiscount = 100.0 + (5.0 * 2);
            double expected = subtotal - percentDiscount - promoDiscount;

            assertEquals(expected, cart.getTotal(), 0.01);
        }

        @Test
        @DisplayName("Should scale discount with quantity")
        void testBuyMoreSaveMoreScenario() {
            Product mouse = TestDataFactory.createMouse();
            cart.addProduct(mouse, 2);
            cart.addPromotion("Mouse", 5.0);

            double discount1 = cart.getDiscountAmount();

            cart.updateQuantity(mouse, 5);
            double discount2 = cart.getDiscountAmount();

            assertTrue(discount2 > discount1);
            assertEquals(25.0, discount2, 0.001);
        }

        @Test
        @DisplayName("Should handle promotion lifecycle")
        void testPromotionLifecycle() {
            Product laptop = TestDataFactory.createLaptop();
            cart.addProduct(laptop);

            cart.addPromotion("Laptop", 50.0);
            double total1 = cart.getTotal();

            cart.removePromotion("Laptop");
            double total2 = cart.getTotal();

            cart.addPromotion("Laptop", 100.0);
            double total3 = cart.getTotal();

            assertTrue(total1 > total3);
            assertTrue(total2 > total1);
        }

        @Test
        @DisplayName("Should use latest promotion value")
        void testConflictingPromotions() {
            Product laptop = TestDataFactory.createLaptop();
            cart.addProduct(laptop);

            cart.addPromotion("Laptop", 50.0);
            cart.addPromotion("Laptop", 100.0);

            assertEquals(100.0, cart.getDiscountAmount(), 0.001);
        }
    }

    @Nested
    @DisplayName("Edge Case Scenarios")
    class EdgeCaseScenarios {

        @Test
        @DisplayName("Should handle cart with only free products")
        void testAllFreeProducts() {
            Product freeProduct1 = TestDataFactory.createFreeProduct();
            Product freeProduct2 = new Product("Free Item 2", 0.0, "Promotional");
            cart.addProduct(freeProduct1, 5);
            cart.addProduct(freeProduct2, 3);

            assertEquals(0.0, cart.getSubtotal(), 0.001);
            assertEquals(0.0, cart.getTotal(), 0.001);
        }

        @Test
        @DisplayName("Should handle mix of free and paid products")
        void testMixedFreeAndPaidProducts() {
            cart.addProduct(TestDataFactory.createFreeProduct(), 3);
            cart.addProduct(TestDataFactory.createLaptop());

            assertEquals(999.99, cart.getSubtotal(), 0.001);
            assertEquals(4, cart.getItemCount());
        }

        @Test
        @DisplayName("Should handle very large cart")
        void testVeryLargeCart() {
            for (int i = 0; i < 100; i++) {
                Product product = new Product("Product" + i, 10.0 + i, "Category");
                cart.addProduct(product, (i % 5) + 1);
            }

            assertEquals(100, cart.getUniqueProductCount());
            assertTrue(cart.getItemCount() > 100);
            assertTrue(cart.getSubtotal() > 0);
        }

        @Test
        @DisplayName("Should maintain floating point precision")
        void testFloatingPointPrecision() {
            Product product1 = new Product("Item1", 0.10, "Test");
            Product product2 = new Product("Item2", 0.20, "Test");
            Product product3 = new Product("Item3", 0.30, "Test");

            cart.addProduct(product1);
            cart.addProduct(product2);
            cart.addProduct(product3);

            assertEquals(0.60, cart.getSubtotal(), 0.001);
        }
    }

    @Nested
    @DisplayName("Pricing Calculation Scenarios")
    class PricingCalculationScenarios {

        @Test
        @DisplayName("Should handle complex discount combinations")
        void testComplexDiscountCombinations() {
            cart.addProduct(TestDataFactory.createLaptop(), 2);
            cart.addProduct(TestDataFactory.createMouse(), 3);
            cart.addProduct(TestDataFactory.createKeyboard());

            cart.applyDiscount(30.0);
            cart.addPromotion("Laptop", 150.0);
            cart.addPromotion("Mouse", 3.0);
            cart.addPromotion("Keyboard", 10.0);

            assertTrue(cart.getTotal() > 0);
            assertTrue(cart.getDiscountAmount() > 0);
        }

        @Test
        @DisplayName("Should handle discount greater than subtotal")
        void testDiscountGreaterThanSubtotalScenario() {
            Product mouse = TestDataFactory.createMouse();
            cart.addProduct(mouse);

            cart.applyDiscount(100.0);
            cart.addPromotion("Mouse", 100.0);

            assertEquals(0.0, cart.getTotal(), 0.001);
        }

        @Test
        @DisplayName("Should achieve zero total multiple ways")
        void testZeroTotalAchievedMultipleWays() {
            Product laptop = TestDataFactory.createLaptop();

            Cart cart1 = new Cart();
            cart1.addProduct(laptop);
            cart1.applyDiscount(100.0);
            assertEquals(0.0, cart1.getTotal(), 0.001);

            Cart cart2 = new Cart();
            cart2.addProduct(laptop);
            cart2.addPromotion("Laptop", 999.99);
            assertEquals(0.0, cart2.getTotal(), 0.01);
        }
    }

    @Nested
    @DisplayName("Cart Summary Tests")
    class CartSummaryTests {

        @Test
        @DisplayName("Should return empty message for empty cart")
        void testCartSummaryEmpty() {
            String summary = cart.getCartSummary();
            assertTrue(summary.contains("empty") || summary.contains("Empty"));
        }

        @Test
        @DisplayName("Should show all items in summary")
        void testCartSummaryWithItems() {
            cart.addProduct(TestDataFactory.createLaptop());
            cart.addProduct(TestDataFactory.createMouse(), 2);

            String summary = cart.getCartSummary();
            assertTrue(summary.contains("Laptop"));
            assertTrue(summary.contains("Mouse"));
            assertTrue(summary.contains("x2"));
        }

        @Test
        @DisplayName("Should show discount in summary")
        void testCartSummaryWithDiscounts() {
            cart.addProduct(TestDataFactory.createLaptop());
            cart.applyDiscount(10.0);

            String summary = cart.getCartSummary();
            assertTrue(summary.contains("Discount") || summary.contains("discount"));
        }

        @Test
        @DisplayName("Should have consistent formatting")
        void testCartSummaryFormatting() {
            cart.addProduct(TestDataFactory.createLaptop());
            cart.addProduct(TestDataFactory.createMouse());

            String summary = cart.getCartSummary();
            assertTrue(summary.contains("="));
            assertTrue(summary.contains("Total"));
        }
    }

    @Nested
    @DisplayName("State Management Tests")
    class StateManagementTests {

        @Test
        @DisplayName("Should treat equal products as same key")
        void testProductEqualityAsMapKey() {
            Product product1 = new Product("Laptop", 999.99, "Electronics");
            Product product2 = new Product("Laptop", 999.99, "Electronics");

            cart.addProduct(product1, 2);
            cart.addProduct(product2, 3);

            assertEquals(5, cart.getQuantity(product1));
        }

        @Test
        @DisplayName("Should maintain valid state after exception")
        void testCartStateAfterException() {
            cart.addProduct(TestDataFactory.createLaptop());

            try {
                cart.addProduct(null);
            } catch (IllegalArgumentException e) {
                // Expected
            }

            assertEquals(1, cart.getUniqueProductCount());
        }

        @Test
        @DisplayName("Should match promotion by exact product name")
        void testPromotionNameMatchingWithProductName() {
            cart.addProduct(TestDataFactory.createLaptop());
            cart.addPromotion("Laptop", 50.0);

            assertTrue(cart.getDiscountAmount() > 0);

            cart.addPromotion("laptop", 100.0);
            assertEquals(2, cart.getActivePromotions().size());
        }
    }
}

package com.example.cart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Cart Pricing Tests")
class CartPricingTest {

    private Cart cart;

    @BeforeEach
    void setUp() {
        cart = new Cart();
    }

    @Nested
    @DisplayName("Subtotal Calculations")
    class SubtotalTests {

        @Test
        @DisplayName("Should return zero for empty cart")
        void testSubtotalEmptyCart() {
            assertEquals(0.0, cart.getSubtotal(), 0.001);
        }

        @Test
        @DisplayName("Should calculate subtotal for single product")
        void testSubtotalSingleProduct() {
            Product laptop = TestDataFactory.createLaptop();
            cart.addProduct(laptop, 2);
            assertEquals(1999.98, cart.getSubtotal(), 0.001);
        }

        @Test
        @DisplayName("Should calculate subtotal for multiple products")
        void testSubtotalMultipleProducts() {
            cart.addProduct(TestDataFactory.createLaptop(), 1);
            cart.addProduct(TestDataFactory.createMouse(), 2);
            cart.addProduct(TestDataFactory.createKeyboard(), 1);
            double expected = 999.99 + (29.99 * 2) + 79.99;
            assertEquals(expected, cart.getSubtotal(), 0.001);
        }

        @Test
        @DisplayName("Should handle zero price products")
        void testSubtotalWithZeroPriceProduct() {
            Product freeProduct = TestDataFactory.createFreeProduct();
            Product laptop = TestDataFactory.createLaptop();
            cart.addProduct(freeProduct, 3);
            cart.addProduct(laptop, 1);
            assertEquals(999.99, cart.getSubtotal(), 0.001);
        }

        @Test
        @DisplayName("Should maintain precision with decimal prices")
        void testSubtotalPrecisionWithDecimals() {
            Product mouse = TestDataFactory.createMouse();
            Product book = TestDataFactory.createBook();
            cart.addProduct(mouse, 3);
            cart.addProduct(book, 2);
            double expected = (29.99 * 3) + (49.99 * 2);
            assertEquals(expected, cart.getSubtotal(), 0.001);
        }

        @Test
        @DisplayName("Should handle large quantities")
        void testSubtotalWithLargeQuantities() {
            Product shirt = TestDataFactory.createShirt();
            cart.addProduct(shirt, 1000);
            assertEquals(19990.0, cart.getSubtotal(), 0.001);
        }
    }

    @Nested
    @DisplayName("Discount Percentage Tests")
    class DiscountPercentageTests {

        @Test
        @DisplayName("Should have zero discount by default")
        void testNoDiscountByDefault() {
            assertEquals(0.0, cart.getDiscountPercentage(), 0.001);
        }

        @Test
        @DisplayName("Should accept valid percentage discounts")
        void testApplyValidPercentageDiscount() {
            cart.applyDiscount(10.0);
            assertEquals(10.0, cart.getDiscountPercentage(), 0.001);

            cart.applyDiscount(25.0);
            assertEquals(25.0, cart.getDiscountPercentage(), 0.001);

            cart.applyDiscount(50.0);
            assertEquals(50.0, cart.getDiscountPercentage(), 0.001);
        }

        @Test
        @DisplayName("Should accept zero percent discount")
        void testApplyZeroPercentageDiscount() {
            cart.applyDiscount(0.0);
            assertEquals(0.0, cart.getDiscountPercentage(), 0.001);
        }

        @Test
        @DisplayName("Should accept 100 percent discount")
        void testApplyFullPercentageDiscount() {
            cart.applyDiscount(100.0);
            assertEquals(100.0, cart.getDiscountPercentage(), 0.001);
        }

        @Test
        @DisplayName("Should throw exception for negative discount")
        void testApplyNegativePercentageDiscount() {
            assertThrows(IllegalArgumentException.class,
                () -> cart.applyDiscount(-10.0));
        }

        @Test
        @DisplayName("Should throw exception for discount over 100")
        void testApplyPercentageOver100() {
            assertThrows(IllegalArgumentException.class,
                () -> cart.applyDiscount(101.0));
        }

        @Test
        @DisplayName("Should accept exactly 0.0 and 100.0")
        void testApplyPercentageBoundary() {
            cart.applyDiscount(0.0);
            assertEquals(0.0, cart.getDiscountPercentage(), 0.001);

            cart.applyDiscount(100.0);
            assertEquals(100.0, cart.getDiscountPercentage(), 0.001);
        }
    }

    @Nested
    @DisplayName("Discount Amount Calculations")
    class DiscountAmountTests {

        @Test
        @DisplayName("Should return zero with no discount")
        void testDiscountAmountWithNoDiscount() {
            cart.addProduct(TestDataFactory.createLaptop());
            assertEquals(0.0, cart.getDiscountAmount(), 0.001);
        }

        @Test
        @DisplayName("Should calculate percentage discount")
        void testDiscountAmountWithPercentage() {
            cart.addProduct(TestDataFactory.createLaptop());
            cart.applyDiscount(10.0);
            double expected = 999.99 * 0.10;
            assertEquals(expected, cart.getDiscountAmount(), 0.01);
        }

        @Test
        @DisplayName("Should return zero for empty cart with discount")
        void testDiscountAmountEmptyCartWithPercentage() {
            cart.applyDiscount(50.0);
            assertEquals(0.0, cart.getDiscountAmount(), 0.001);
        }

        @Test
        @DisplayName("Should maintain precision in discount calculations")
        void testDiscountAmountPrecision() {
            Product mouse = TestDataFactory.createMouse();
            cart.addProduct(mouse, 3);
            cart.applyDiscount(33.33);
            double subtotal = 29.99 * 3;
            double expected = subtotal * 0.3333;
            assertEquals(expected, cart.getDiscountAmount(), 0.01);
        }
    }

    @Nested
    @DisplayName("Total Calculations")
    class TotalTests {

        @Test
        @DisplayName("Should equal subtotal with no discount")
        void testTotalWithNoDiscount() {
            cart.addProduct(TestDataFactory.createLaptop());
            assertEquals(cart.getSubtotal(), cart.getTotal(), 0.001);
        }

        @Test
        @DisplayName("Should calculate total with percentage discount")
        void testTotalWithPercentageDiscount() {
            cart.addProduct(TestDataFactory.createLaptop());
            cart.applyDiscount(20.0);
            double expected = 999.99 * 0.80;
            assertEquals(expected, cart.getTotal(), 0.01);
        }

        @Test
        @DisplayName("Should return zero with 100% discount")
        void testTotalWithFullDiscount() {
            cart.addProduct(TestDataFactory.createLaptop());
            cart.applyDiscount(100.0);
            assertEquals(0.0, cart.getTotal(), 0.001);
        }

        @Test
        @DisplayName("Should never return negative total")
        void testTotalNeverNegative() {
            Product laptop = TestDataFactory.createLaptop();
            cart.addProduct(laptop);
            cart.applyDiscount(100.0);
            cart.addPromotion("Laptop", 1000.0);
            assertTrue(cart.getTotal() >= 0.0);
        }

        @Test
        @DisplayName("Should return zero for empty cart")
        void testTotalEmptyCart() {
            assertEquals(0.0, cart.getTotal(), 0.001);
        }
    }

    @Nested
    @DisplayName("Dynamic Pricing Tests")
    class DynamicPricingTests {

        @Test
        @DisplayName("Should update total after adding product")
        void testPricingAfterAddingProduct() {
            cart.addProduct(TestDataFactory.createLaptop());
            double total1 = cart.getTotal();

            cart.addProduct(TestDataFactory.createMouse());
            double total2 = cart.getTotal();

            assertTrue(total2 > total1);
        }

        @Test
        @DisplayName("Should update total after removing product")
        void testPricingAfterRemovingProduct() {
            Product laptop = TestDataFactory.createLaptop();
            Product mouse = TestDataFactory.createMouse();
            cart.addProduct(laptop);
            cart.addProduct(mouse);
            double total1 = cart.getTotal();

            cart.removeProduct(mouse);
            double total2 = cart.getTotal();

            assertTrue(total2 < total1);
        }

        @Test
        @DisplayName("Should update total after updating quantity")
        void testPricingAfterUpdatingQuantity() {
            Product keyboard = TestDataFactory.createKeyboard();
            cart.addProduct(keyboard, 2);
            double total1 = cart.getTotal();

            cart.updateQuantity(keyboard, 5);
            double total2 = cart.getTotal();

            assertTrue(total2 > total1);
        }

        @Test
        @DisplayName("Should reset totals after clearing cart")
        void testPricingAfterClearingCart() {
            cart.addProduct(TestDataFactory.createLaptop(), 3);
            cart.applyDiscount(10.0);
            cart.clearCart();

            assertEquals(0.0, cart.getSubtotal(), 0.001);
            assertEquals(0.0, cart.getTotal(), 0.001);
        }

        @Test
        @DisplayName("Should update total after changing discount")
        void testPricingAfterChangingDiscount() {
            cart.addProduct(TestDataFactory.createLaptop());
            cart.applyDiscount(10.0);
            double total1 = cart.getTotal();

            cart.applyDiscount(20.0);
            double total2 = cart.getTotal();

            assertTrue(total2 < total1);
        }
    }
}

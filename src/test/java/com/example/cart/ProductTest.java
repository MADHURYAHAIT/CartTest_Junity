package com.example.cart;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Product Tests")
class ProductTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create valid product with three arguments")
        void testValidProductCreationThreeArgs() {
            Product product = new Product("Laptop", 999.99, "Electronics");
            assertEquals("Laptop", product.getName());
            assertEquals(999.99, product.getPrice(), 0.001);
            assertEquals("Electronics", product.getCategory());
        }

        @Test
        @DisplayName("Should create valid product with two arguments and default category")
        void testValidProductCreationTwoArgs() {
            Product product = new Product("Mouse", 29.99);
            assertEquals("Mouse", product.getName());
            assertEquals(29.99, product.getPrice(), 0.001);
            assertEquals("General", product.getCategory());
        }

        @Test
        @DisplayName("Should throw exception for null name")
        void testProductWithNullName() {
            assertThrows(IllegalArgumentException.class,
                () -> new Product(null, 50.0, "Books"));
        }

        @Test
        @DisplayName("Should throw exception for empty name")
        void testProductWithEmptyName() {
            assertThrows(IllegalArgumentException.class,
                () -> new Product("", 50.0, "Books"));
        }

        @Test
        @DisplayName("Should allow whitespace-only name")
        void testProductWithWhitespaceOnlyName() {
            assertThrows(IllegalArgumentException.class,
                () -> new Product("   ", 50.0, "Books"));
        }

        @Test
        @DisplayName("Should throw exception for negative price")
        void testProductWithNegativePrice() {
            assertThrows(IllegalArgumentException.class,
                () -> new Product("Book", -10.0, "Books"));
        }

        @Test
        @DisplayName("Should accept zero price")
        void testProductWithZeroPrice() {
            Product product = new Product("Free Sample", 0.0, "Promotional");
            assertEquals(0.0, product.getPrice(), 0.001);
        }

        @Test
        @DisplayName("Should accept very large price")
        void testProductWithVeryLargePrice() {
            Product product = new Product("Expensive", Double.MAX_VALUE, "Luxury");
            assertEquals(Double.MAX_VALUE, product.getPrice(), 0.001);
        }

        @Test
        @DisplayName("Should accept very small positive price")
        void testProductWithVerySmallPrice() {
            Product product = new Product("Cheap", 0.01, "Bargain");
            assertEquals(0.01, product.getPrice(), 0.001);
        }

        @Test
        @DisplayName("Should allow null category")
        void testProductWithNullCategory() {
            Product product = new Product("Item", 10.0, null);
            assertNull(product.getCategory());
        }

        @Test
        @DisplayName("Should allow empty category")
        void testProductWithEmptyCategory() {
            Product product = new Product("Item", 10.0, "");
            assertEquals("", product.getCategory());
        }
    }

    @Nested
    @DisplayName("Getter Tests")
    class GetterTests {

        @Test
        @DisplayName("Should return correct name")
        void testGetName() {
            Product product = TestDataFactory.createLaptop();
            assertEquals("Laptop", product.getName());
        }

        @Test
        @DisplayName("Should return correct price with proper precision")
        void testGetPrice() {
            Product product = TestDataFactory.createMouse();
            assertEquals(29.99, product.getPrice(), 0.001);
        }

        @Test
        @DisplayName("Should return correct category")
        void testGetCategory() {
            Product product = TestDataFactory.createBook();
            assertEquals("Books", product.getCategory());
        }
    }

    @Nested
    @DisplayName("Equals Contract Tests")
    class EqualsTests {

        @Test
        @DisplayName("Should be equal to itself (reflexive)")
        void testEqualsReflexive() {
            Product product = TestDataFactory.createLaptop();
            assertEquals(product, product);
        }

        @Test
        @DisplayName("Should be symmetric")
        void testEqualsSymmetric() {
            Product product1 = new Product("Mouse", 29.99, "Electronics");
            Product product2 = new Product("Mouse", 29.99, "Electronics");
            assertEquals(product1, product2);
            assertEquals(product2, product1);
        }

        @Test
        @DisplayName("Should be transitive")
        void testEqualsTransitive() {
            Product product1 = new Product("Keyboard", 79.99, "Electronics");
            Product product2 = new Product("Keyboard", 79.99, "Electronics");
            Product product3 = new Product("Keyboard", 79.99, "Electronics");
            assertEquals(product1, product2);
            assertEquals(product2, product3);
            assertEquals(product1, product3);
        }

        @Test
        @DisplayName("Should be equal when all fields match")
        void testEqualsWithIdenticalProduct() {
            Product product1 = TestDataFactory.createLaptop();
            Product product2 = new Product("Laptop", 999.99, "Electronics");
            assertEquals(product1, product2);
        }

        @Test
        @DisplayName("Should not be equal when names differ")
        void testEqualsWithDifferentName() {
            Product product1 = new Product("Laptop", 999.99, "Electronics");
            Product product2 = new Product("Desktop", 999.99, "Electronics");
            assertNotEquals(product1, product2);
        }

        @Test
        @DisplayName("Should not be equal when prices differ")
        void testEqualsWithDifferentPrice() {
            Product product1 = new Product("Laptop", 999.99, "Electronics");
            Product product2 = new Product("Laptop", 899.99, "Electronics");
            assertNotEquals(product1, product2);
        }

        @Test
        @DisplayName("Should not be equal when categories differ")
        void testEqualsWithDifferentCategory() {
            Product product1 = new Product("Item", 50.0, "Electronics");
            Product product2 = new Product("Item", 50.0, "Books");
            assertNotEquals(product1, product2);
        }

        @Test
        @DisplayName("Should return false when compared to null")
        void testEqualsWithNull() {
            Product product = TestDataFactory.createLaptop();
            assertNotEquals(null, product);
        }

        @Test
        @DisplayName("Should return false when compared to different class")
        void testEqualsWithDifferentClass() {
            Product product = TestDataFactory.createLaptop();
            String notAProduct = "Not a product";
            assertNotEquals(product, notAProduct);
        }
    }

    @Nested
    @DisplayName("HashCode Contract Tests")
    class HashCodeTests {

        @Test
        @DisplayName("Should return consistent hashCode on multiple calls")
        void testHashCodeConsistency() {
            Product product = TestDataFactory.createLaptop();
            int hash1 = product.hashCode();
            int hash2 = product.hashCode();
            assertEquals(hash1, hash2);
        }

        @Test
        @DisplayName("Should have equal hashCodes for equal objects")
        void testHashCodeEqualObjects() {
            Product product1 = new Product("Mouse", 29.99, "Electronics");
            Product product2 = new Product("Mouse", 29.99, "Electronics");
            assertEquals(product1.hashCode(), product2.hashCode());
        }

        @Test
        @DisplayName("Should likely have different hashCodes for different objects")
        void testHashCodeDifferentObjects() {
            Product product1 = TestDataFactory.createLaptop();
            Product product2 = TestDataFactory.createMouse();
            assertNotEquals(product1.hashCode(), product2.hashCode());
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("Should format toString as 'Name ($Price) [Category]'")
        void testToStringFormat() {
            Product product = TestDataFactory.createLaptop();
            String result = product.toString();
            assertTrue(result.contains("Laptop"));
            assertTrue(result.contains("999.99"));
            assertTrue(result.contains("Electronics"));
            assertTrue(result.matches(".*\\$.*\\[.*\\]"));
        }

        @Test
        @DisplayName("Should handle null category in toString")
        void testToStringWithNullCategory() {
            Product product = new Product("Item", 10.0, null);
            String result = product.toString();
            assertNotNull(result);
            assertTrue(result.contains("Item"));
        }
    }
}

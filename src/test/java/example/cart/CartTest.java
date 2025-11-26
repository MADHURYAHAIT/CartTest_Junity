package com.example.cart;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    @Test
    void testAddNewProduct() {
        Cart cart = new Cart();
        boolean added = cart.addProduct("Laptop");
        assertTrue(added);
        assertTrue(cart.getItems().contains("Laptop"));
    }

    @Test
    void testAddExistingProduct() {
        Cart cart = new Cart();
        cart.addProduct("Laptop");
        boolean addedAgain = cart.addProduct("Laptop");
        assertFalse(addedAgain);
        assertEquals(1, cart.getItems().size());
    }

    @Test
    void testAddInvalidProduct() {
        Cart cart = new Cart();
        assertThrows(IllegalArgumentException.class, () -> cart.addProduct(""));
        assertThrows(IllegalArgumentException.class, () -> cart.addProduct(null));
    }

    @Test
    void testRemoveProduct() {
        Cart cart = new Cart();
        cart.addProduct("Phone");
        boolean removed = cart.removeProduct("Phone");
        assertTrue(removed);
        assertFalse(cart.getItems().contains("Phone"));
    }

    @Test
    void testRemoveNonexistentProduct() {
        Cart cart = new Cart();
        boolean removed = cart.removeProduct("Tablet");
        assertFalse(removed);
    }
}


package com.example.cart;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<String> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public boolean addProduct(String product) {
        if (product == null || product.isEmpty()) {
            throw new IllegalArgumentException("Invalid product");
        }
        if (items.contains(product)) {
            return false;
        }
        items.add(product);
        return true;
    }

    public boolean removeProduct(String product) {
        return items.remove(product);
    }

    public List<String> getItems() {
        return items;
    }
}


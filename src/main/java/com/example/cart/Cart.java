package com.example.cart;

import java.util.*;

public class Cart {
    private Map<Product, Integer> items;
    private Map<String, Double> activePromotions;
    private double discountPercentage;

    public Cart() {
        this.items = new HashMap<>();
        this.activePromotions = new HashMap<>();
        this.discountPercentage = 0.0;
    }

    // Backward compatibility: add product by name (creates a Product with $0 price)
    public boolean addProduct(String productName) {
        if (productName == null || productName.isEmpty()) {
            throw new IllegalArgumentException("Invalid product");
        }

        // Check if product with this name already exists (for backward compatibility)
        if (containsProduct(productName)) {
            return false;
        }

        Product product = new Product(productName, 0.0);
        return addProduct(product, 1);
    }

    // Add product with quantity
    public boolean addProduct(Product product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        if (items.containsKey(product)) {
            items.put(product, items.get(product) + quantity);
        } else {
            items.put(product, quantity);
        }
        return true;
    }

    // Add single product
    public boolean addProduct(Product product) {
        return addProduct(product, 1);
    }

    // Update quantity of a product
    public boolean updateQuantity(Product product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        if (!items.containsKey(product)) {
            return false;
        }

        if (quantity == 0) {
            items.remove(product);
        } else {
            items.put(product, quantity);
        }
        return true;
    }

    // Backward compatibility: remove product by name
    public boolean removeProduct(String productName) {
        Product productToRemove = null;
        for (Product product : items.keySet()) {
            if (product.getName().equals(productName)) {
                productToRemove = product;
                break;
            }
        }

        if (productToRemove != null) {
            items.remove(productToRemove);
            return true;
        }
        return false;
    }

    // Remove product completely
    public boolean removeProduct(Product product) {
        if (product == null) {
            return false;
        }
        return items.remove(product) != null;
    }

    // Remove one unit of a product
    public boolean removeProductUnit(Product product) {
        if (product == null || !items.containsKey(product)) {
            return false;
        }

        int currentQuantity = items.get(product);
        if (currentQuantity > 1) {
            items.put(product, currentQuantity - 1);
        } else {
            items.remove(product);
        }
        return true;
    }

    // Get quantity of a specific product
    public int getQuantity(Product product) {
        return items.getOrDefault(product, 0);
    }

    // Backward compatibility: return list of product names
    public List<String> getItems() {
        List<String> productNames = new ArrayList<>();
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            productNames.add(entry.getKey().getName());
        }
        return productNames;
    }

    // Get all products with their quantities
    public Map<Product, Integer> getProductsWithQuantities() {
        return new HashMap<>(items);
    }

    // Get total number of items (sum of all quantities)
    public int getItemCount() {
        return items.values().stream().mapToInt(Integer::intValue).sum();
    }

    // Get number of unique products
    public int getUniqueProductCount() {
        return items.size();
    }

    // Check if cart is empty
    public boolean isEmpty() {
        return items.isEmpty();
    }

    // Clear all items from cart
    public void clearCart() {
        items.clear();
    }

    // Calculate subtotal (before discounts)
    public double getSubtotal() {
        double subtotal = 0.0;
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            subtotal += entry.getKey().getPrice() * entry.getValue();
        }
        return subtotal;
    }

    // Calculate discount amount
    public double getDiscountAmount() {
        double subtotal = getSubtotal();
        double percentageDiscount = subtotal * (discountPercentage / 100.0);

        double promotionDiscount = 0.0;
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            String productName = entry.getKey().getName();
            if (activePromotions.containsKey(productName)) {
                promotionDiscount += activePromotions.get(productName) * entry.getValue();
            }
        }

        return percentageDiscount + promotionDiscount;
    }

    // Calculate total (after discounts)
    public double getTotal() {
        return Math.max(0, getSubtotal() - getDiscountAmount());
    }

    // Apply percentage discount to entire cart
    public void applyDiscount(double percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
        }
        this.discountPercentage = percentage;
    }

    // Get current discount percentage
    public double getDiscountPercentage() {
        return discountPercentage;
    }

    // Add promotion for specific product (fixed amount off)
    public void addPromotion(String productName, double discountAmount) {
        if (productName == null || productName.isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        if (discountAmount < 0) {
            throw new IllegalArgumentException("Discount amount cannot be negative");
        }
        activePromotions.put(productName, discountAmount);
    }

    // Remove promotion for specific product
    public void removePromotion(String productName) {
        activePromotions.remove(productName);
    }

    // Clear all promotions
    public void clearPromotions() {
        activePromotions.clear();
        discountPercentage = 0.0;
    }

    // Get all active promotions
    public Map<String, Double> getActivePromotions() {
        return new HashMap<>(activePromotions);
    }

    // Check if cart contains a product
    public boolean containsProduct(Product product) {
        return items.containsKey(product);
    }

    // Check if cart contains product by name
    public boolean containsProduct(String productName) {
        for (Product product : items.keySet()) {
            if (product.getName().equals(productName)) {
                return true;
            }
        }
        return false;
    }

    // Get cart summary as string
    public String getCartSummary() {
        if (isEmpty()) {
            return "Cart is empty";
        }

        StringBuilder summary = new StringBuilder();
        summary.append("Cart Summary:\n");
        summary.append("=".repeat(50)).append("\n");

        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            double itemTotal = product.getPrice() * quantity;
            summary.append(String.format("%s x%d = $%.2f\n",
                product.getName(), quantity, itemTotal));
        }

        summary.append("=".repeat(50)).append("\n");
        summary.append(String.format("Subtotal: $%.2f\n", getSubtotal()));

        if (getDiscountAmount() > 0) {
            summary.append(String.format("Discount: -$%.2f\n", getDiscountAmount()));
        }

        summary.append(String.format("Total: $%.2f\n", getTotal()));
        summary.append(String.format("Total Items: %d", getItemCount()));

        return summary.toString();
    }
}


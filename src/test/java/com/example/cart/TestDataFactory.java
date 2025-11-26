package com.example.cart;

public class TestDataFactory {

    // Standard Products
    public static Product createLaptop() {
        return new Product("Laptop", 999.99, "Electronics");
    }

    public static Product createMouse() {
        return new Product("Mouse", 29.99, "Electronics");
    }

    public static Product createKeyboard() {
        return new Product("Keyboard", 79.99, "Electronics");
    }

    public static Product createBook() {
        return new Product("Programming Book", 49.99, "Books");
    }

    public static Product createShirt() {
        return new Product("T-Shirt", 19.99, "Clothing");
    }

    // Edge Case Products
    public static Product createFreeProduct() {
        return new Product("Free Sample", 0.0, "Promotional");
    }

    public static Product createExpensiveProduct() {
        return new Product("Diamond Ring", 99999.99, "Luxury");
    }

    public static Product createProductWithLongName() {
        String longName = "A".repeat(200);
        return new Product(longName, 10.0, "Test");
    }

    public static Product createProductWithSpecialCharacters() {
        return new Product("Product™ with €urö & spëciàl çhars", 25.50, "Special");
    }

    public static Product createProductWithUnicode() {
        return new Product("笔记本电脑 Laptop", 1200.00, "Electronics");
    }

    // Pre-configured Carts
    public static Cart createEmptyCart() {
        return new Cart();
    }

    public static Cart createCartWithSingleProduct() {
        Cart cart = new Cart();
        cart.addProduct(createLaptop());
        return cart;
    }

    public static Cart createCartWithMultipleProducts() {
        Cart cart = new Cart();
        cart.addProduct(createLaptop());
        cart.addProduct(createMouse(), 2);
        cart.addProduct(createKeyboard());
        return cart;
    }

    public static Cart createCartWithPromotions() {
        Cart cart = createCartWithMultipleProducts();
        cart.applyDiscount(10.0);
        cart.addPromotion("Laptop", 50.0);
        cart.addPromotion("Mouse", 5.0);
        return cart;
    }

    public static Cart createCartWithLargeInventory() {
        Cart cart = new Cart();
        for (int i = 1; i <= 50; i++) {
            Product product = new Product("Product" + i, i * 10.0, "Category" + (i % 5));
            cart.addProduct(product, i % 10 + 1);
        }
        return cart;
    }
}

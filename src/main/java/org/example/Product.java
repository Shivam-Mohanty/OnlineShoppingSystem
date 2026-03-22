package org.example;

public class Product {
    private int id;
    private String name;
    private double price;
    private int stockCount;

    // Constructor for creating products from database rows
    public Product(int id, String name, double price, int stockCount) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stockCount = stockCount;
    }

    // Getters - Required for JavaFX and logic
    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStockCount() { return stockCount; }

    // This determines how the product looks in the ListView
    @Override
    public String toString() {
        // This ensures the stock is visible in the list immediately
        return String.format("%s - $%.2f (In Stock: %d)", name, price, stockCount);
    }
}
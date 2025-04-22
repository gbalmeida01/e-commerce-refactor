package com.auth;

import java.util.Objects;

public class Product {
    private final int id;
    private String name;
    private String description;
    private double price;
    private final Inventory stock;

    public Product(int id, String name, String description, double price, int initialQuantity) {
        if (id <= 0) {
            throw new IllegalArgumentException("Product ID must be positive!");
        }
        this.id = id;
        this.stock = new Inventory(initialQuantity);

        setName(name);
        setDescription(description);
        setPrice(price);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty!");
        }
        this.name = name.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description != null ? description : "";
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be positive!");
        }
        this.price = price;
    }

    public Inventory getStock() {
        return stock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%s [ID: %d] - $%.2f - %s",
                name, id, price, stock);
    }

    public double calculateTotalForQuantity(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative!");
        }
        return price * amount;
    }
}
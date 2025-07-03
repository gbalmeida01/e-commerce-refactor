package com.auth.service;

import com.auth.model.Product;

import java.util.List;
import java.util.Scanner;

public class ProductService {
    private static List<Product> products;
    private static Scanner scanner;

    public ProductService(List<Product> products, Scanner scanner) {
        this.products = products;
        this.scanner = scanner;
    }

    public static void addProduct() {
        System.out.println("\n=== Add New Product ===");
        System.out.print("Enter product ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter product name: ");
        String name = scanner.nextLine();

        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        System.out.print("Enter price: ");
        double price = scanner.nextDouble();

        System.out.print("Enter initial stock: ");
        int stock = scanner.nextInt();

        products.add(new Product(id, name, description, price, stock));
        System.out.println("Product added successfully!");
    }

    public static void updateStock() {
        if (products.isEmpty()) {
            System.out.println("No products available!");
            return;
        }

        System.out.println("\n=== Update Stock ===");
        listProducts();

        System.out.print("Select product ID: ");
        int id = scanner.nextInt();

        Product product = findProduct(id);
        if (product == null) {
            System.out.println("Product not found!");
            return;
        }

        System.out.print("Enter quantity to add (use negative to remove): ");
        int quantity = scanner.nextInt();

        try {
            if (quantity > 0) {
                product.getStock().add(quantity);
            } else {
                product.getStock().remove(-quantity);
            }
            System.out.println("Stock updated successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void listProducts() {
        System.out.println("\nAvailable Products:");
        products.forEach(p -> System.out.printf("ID: %d | %-20s | Price: $%-8.2f | Stock: %s%n",
                p.getId(), p.getName(), p.getPrice(), p.getStock()));
    }

    public static Product findProduct(int id) {
        return products.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }
}


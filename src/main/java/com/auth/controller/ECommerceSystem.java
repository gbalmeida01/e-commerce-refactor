package com.auth.controller;

import com.auth.service.ProductService;
import com.auth.service.UserService;
import com.auth.util.ReportGenerator;
import com.auth.model.Product;
import com.auth.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.auth.service.ProductService.*;

public class ECommerceSystem {
    private static List<Product> products = new ArrayList<>();
    private static List<User> users = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static int userIdCounter = 1;

    public static void main(String[] args) {
        ProductService productService = new ProductService(products, scanner);
        UserService userService = new UserService(users, scanner);
        int choice;

        do {
            System.out.println("\n=== Welcome to Inventory System ===");
            System.out.println("1. Create Account");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> userService.createAccount();
                case 2 -> {
                    User loggedInUser = userService.login();
                    if (loggedInUser != null) {
                        runAdminPanel(loggedInUser, productService);
                    }
                }
                case 3 -> System.out.println("Exiting system...");
                default -> System.out.println("Invalid choice!");
            }

        } while (choice != 3);
    }

    private static void runAdminPanel(User user, ProductService productService) {
        int choice;
        do {
            System.out.println("\n=== Admin Panel ===");
            System.out.println("1. Add Product");
            System.out.println("2. Update Stock");
            System.out.println("3. Process Sale");
            System.out.println("4. Generate Reports");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> productService.addProduct();
                case 2 -> productService.updateStock();
                case 3 -> processSale(); // ainda está no ECommerceSystem
                case 4 -> new ReportGenerator(products).showMenu();
                case 5 -> System.out.println("Logging out...");
                default -> System.out.println("Invalid choice!");
            }
        } while (choice != 5);
    }


    private static void processSale() {
        if (products.isEmpty()) {
            System.out.println("No products available!");
            return;
        }

        System.out.println("\n=== Process Sale ===");
        listProducts();

        System.out.print("Select product ID: ");
        int id = scanner.nextInt();

        Product product = findProduct(id);
        if (product == null) {
            System.out.println("Product not found!");
            return;
        }

        System.out.print("Enter quantity to sell: ");
        int quantity = scanner.nextInt();

        try {
            product.getStock().reserve(quantity);
            product.getStock().confirmSale(quantity);
            System.out.printf("Sold %d of %s. Total: $%.2f%n",
                    quantity, product.getName(), product.calculateTotalForQuantity(quantity));
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

package com.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ECommerceSystem {
    private static List<Product> products = new ArrayList<>();
    private static List<User> users = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static int userIdCounter = 1;

    public static void main(String[] args) {
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
                case 1 -> createAccount();
                case 2 -> login();
                case 3 -> System.out.println("Exiting system...");
                default -> System.out.println("Invalid choice!");
            }

        } while (choice != 3);
    }

    private static void createAccount() {
        System.out.println("\n=== Create Admin Account ===");

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User newUser = new User(userIdCounter++, username, email, password, "admin");
        users.add(newUser);
        System.out.println("Admin account created successfully!");
    }

    private static void login() {
        System.out.println("\n=== Admin Login ===");

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password) && user.getRole().equals("admin")) {
                System.out.println("Login successful. Welcome, " + user.getUserName() + "!");
                runAdminPanel(user);
                return;
            }
        }

        System.out.println("Invalid credentials or not an admin!");
    }

    private static void runAdminPanel(User user) {
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
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1 -> addProduct();
                case 2 -> updateStock();
                case 3 -> processSale();
                case 4 -> new ReportGenerator(products).showMenu();
                case 5 -> System.out.println("Logging out...");
                default -> System.out.println("Invalid choice!");
            }
        } while (choice != 5);
    }

    private static void addProduct() {
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

    private static void updateStock() {
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

    private static void listProducts() {
        System.out.println("\nAvailable Products:");
        products.forEach(p -> System.out.printf("ID: %d | %-20s | Price: $%-8.2f | Stock: %s%n",
                p.getId(), p.getName(), p.getPrice(), p.getStock()));
    }

    private static Product findProduct(int id) {
        return products.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }
}

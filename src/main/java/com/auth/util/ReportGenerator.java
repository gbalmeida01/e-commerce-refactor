package com.auth.util;

import com.auth.model.Inventory;
import com.auth.model.Product;

import java.util.List;
import java.util.Scanner;

public class ReportGenerator {
    private final List<Product> products;

    public ReportGenerator(List<Product> products) {
        this.products = products;
    }

    public void showMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.println("\n=== Reports Menu ===");
            System.out.println("1. Inventory Summary");
            System.out.println("2. Low Stock Alert");
            System.out.println("3. Product Valuation");
            System.out.println("4. Back to Main Menu");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1 -> generateInventorySummary();
                case 2 -> generateLowStockReport();
                case 3 -> generateValuationReport();
                case 4 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid choice!");
            }
        } while (choice != 4);
    }

    private void generateInventorySummary() {
        System.out.println("\n=== Inventory Summary ===");
        System.out.printf("%-5s %-20s %-10s %-15s %-15s%n",
                "ID", "Name", "Price", "Available", "Reserved");

        products.forEach(p -> {
            Inventory stock = p.getStock();
            System.out.printf("%-5d %-20s $%-9.2f %-15d %-15d%n",
                    p.getId(), p.getName(), p.getPrice(),
                    stock.getAvailableQuantity(), stock.getReservedQuantity());
        });
    }

    private void generateLowStockReport() {
        System.out.println("\n=== Low Stock Alert ===");
        System.out.print("Enter threshold quantity: ");
        int threshold = new Scanner(System.in).nextInt();

        System.out.printf("%nProducts with stock below %d:%n", threshold);
        System.out.printf("%-5s %-20s %-10s %-15s%n",
                "ID", "Name", "Price", "Available");

        products.stream()
                .filter(p -> p.getStock().getAvailableQuantity() < threshold)
                .forEach(p -> System.out.printf("%-5d %-20s $%-9.2f %-15d%n",
                        p.getId(), p.getName(), p.getPrice(),
                        p.getStock().getAvailableQuantity()));
    }

    private void generateValuationReport() {
        System.out.println("\n=== Product Valuation ===");
        double totalValue = products.stream()
                .mapToDouble(p -> p.getPrice() * p.getStock().getTotalQuantity())
                .sum();

        System.out.printf("%n%-5s %-20s %-10s %-15s %-15s%n",
                "ID", "Name", "Price", "Quantity", "Value");

        products.forEach(p -> {
            double value = p.getPrice() * p.getStock().getTotalQuantity();
            System.out.printf("%-5d %-20s $%-9.2f %-15d $%-14.2f%n",
                    p.getId(), p.getName(), p.getPrice(),
                    p.getStock().getTotalQuantity(), value);
        });

        System.out.printf("%nTotal Inventory Value: $%.2f%n", totalValue);
    }
}
package com.auth.service;

import com.auth.model.User;

import java.util.List;
import java.util.Scanner;

public class UserService {
    private static List<User> users;
    private static Scanner scanner;
    private static int userIdCounter = 1;

    public UserService(List<User> users, Scanner scanner) {
        this.users = users;
        this.scanner = scanner;
    }

    public static void createAccount() {
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

    public static User login() {
        System.out.println("\n=== Admin Login ===");

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password) && user.getRole().equals("admin")) {
                System.out.println("Login successful. Welcome, " + user.getUserName() + "!");
                return user;
            }
        }

        System.out.println("Invalid credentials or not an admin!");
        return null;
    }
}

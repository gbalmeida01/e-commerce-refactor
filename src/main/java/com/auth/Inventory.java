package com.auth;

public class Inventory {
    private int amount;
    private int reserved;

    public Inventory(int initialQuantity) {
        if (initialQuantity < 0) {
            throw new IllegalArgumentException("Initial inventory cannot be negative");
        }
        this.amount = initialQuantity;
        this.reserved = 0;
    }

    public int getAvailableQuantity() {
        return amount - reserved;
    }

    public int getTotalQuantity() {
        return amount;
    }

    public int getReservedQuantity() {
        return reserved;
    }

    public void add(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.amount += quantity;
    }

    public void remove(int quantity) {
        if (quantity > getAvailableQuantity()) {
            throw new IllegalStateException("Insufficient inventory");
        }
        this.amount -= quantity;
    }

    public void reserve(int quantity) {
        if (quantity > getAvailableQuantity()) {
            throw new IllegalStateException("Insufficient inventory for reservation");
        }
        this.reserved += quantity;
    }

    public void releaseReservation(int quantity) {
        if (quantity > reserved) {
            throw new IllegalStateException("Release quantity exceeds reserved");
        }
        this.reserved -= quantity;
    }

    public void confirmSale(int quantity) {
        if (quantity > reserved) {
            throw new IllegalStateException("Sale quantity exceeds reserved");
        }
        this.reserved -= quantity;
    }

    public boolean isAvailable() {
        return getAvailableQuantity() > 0;
    }

    public boolean hasReservedItems() {
        return reserved > 0;
    }

    @Override
    public String toString() {
        return String.format("Inventory: %d available (%d reserved)",
                getAvailableQuantity(), reserved);
    }
}
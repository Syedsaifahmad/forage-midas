package com.jpmc.midascore;

public class Transaction {
    private double amount;
    private String id; // or other fields depending on your project

    // Empty constructor (needed for Kafka deserialization)
    public Transaction() {}

    public Transaction(double amount, String id) {
        this.amount = amount;
        this.id = id;
    }

    // Getters and setters
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Transaction{id='" + id + "', amount=" + amount + "}";
    }
}

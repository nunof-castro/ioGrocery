package com.example.iogrocery.models;

public class newUser {
    private String name;
    private String email;
    private Double amount;
    private String role;

    public newUser(String name, String email, Double amount, String role) {
        this.name = name;
        this.email = email;
        this.amount = amount;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

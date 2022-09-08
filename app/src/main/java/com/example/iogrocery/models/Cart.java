package com.example.iogrocery.models;

import java.util.ArrayList;

public class Cart {
    public String id;
    public String name;
    public String img;
    public Double totalPrice;
    public Double unitPrice;
    public int available;
    public int quantity;


    public Cart(String id, String name, String img, Double totalPrice, Double unitPrice, int available, int quantity) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.totalPrice = totalPrice;
        this.unitPrice = unitPrice;
        this.available = available;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

package com.example.iogrocery.models;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.DiffUtil;


import java.util.Objects;

public class Product {
    private String id;
    private Double packPrice;
    private int packUnits;
    private String name;
    private String buyer;
    private int available;
    private String img;

    public Product(String id, Double packPrice, int packUnits, String name, String buyer, int available, String img) {
        this.id = id;
        this.packPrice = packPrice;
        this.packUnits = packUnits;
        this.name = name;
        this.buyer = buyer;
        this.available = available;
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getPackPrice() {
        return packPrice;
    }

    public void setPackPrice(Double packPrice) {
        this.packPrice = packPrice;
    }

    public int getPackUnits() {
        return packUnits;
    }

    public void setPackUnits(int packUnits) {
        this.packUnits = packUnits;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}

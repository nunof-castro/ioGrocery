package com.example.iogrocery.models;

import org.json.JSONArray;

import java.util.ArrayList;

public class Purchase {
    private String purchaseid;
    private String purchaseCard;
    private String balance;
    private String product_id;
    private String productId;
    private int productQuantity;
    private String purchaseDate;
    private JSONArray products;





    public Purchase(String purchaseid, String purchaseCard, String balance, String purchaseDate, JSONArray products) {
        this.purchaseid = purchaseid;
        this.purchaseCard = purchaseCard;
        this.balance = balance;
        this.product_id = product_id;
        this.productId = productId;
        this.productQuantity = productQuantity;
        this.purchaseDate = purchaseDate;
        this.products = products;
    }

    public String getPurchaseCard() {
        return purchaseCard;
    }

    public void setPurchaseCard(String purchaseCard) {
        this.purchaseCard = purchaseCard;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getPurchaseid() {
        return purchaseid;
    }

    public void setPurchaseid(String purchaseid) {
        this.purchaseid = purchaseid;
    }

    public JSONArray getProducts() {
        return products;
    }

    public void setProducts(JSONArray products) {
        this.products = products;
    }
}

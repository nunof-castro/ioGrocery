package com.example.iogrocery.models;

import org.json.JSONArray;

public class Water {

    public Double quantity;

    public Water(Double quantity) {
        this.quantity = quantity;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
}

package com.example.iogrocery.models;

public class Cupon {
    private String id;
    private int discount;

    public Cupon(String id,  int discount) {
        this.id = id;

        this.discount = discount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}

package com.example.iogrocery.models;

import android.net.Uri;

public class User {
    private Double email;
    private String user_name;
    private String cardId;


    public Double getEmail() {
        return email;
    }

    public void setEmail(Double email) {
        this.email = email;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public User(Double email, String user_name, String cardId) {
        this.email = email;
        this.user_name = user_name;
        this.cardId = cardId;
    }
}

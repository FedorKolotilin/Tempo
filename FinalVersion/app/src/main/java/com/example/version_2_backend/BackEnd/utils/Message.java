package com.example.version_2_backend.BackEnd.utils;

public class Message {
    String text;
    String email;

    public void setText(String text) {
        this.text = text;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getText() {
        return text;
    }

    public String getEmail() {
        return email;
    }

    public Message(){

    }
}

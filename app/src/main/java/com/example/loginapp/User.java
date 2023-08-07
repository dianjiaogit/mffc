package com.example.loginapp;

public class User {
    private String username;
    private String email;
    private String hashedPassword;

    private Boolean isOnline;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.hashedPassword = password;
    }
}

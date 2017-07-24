package com.example.sheldon.instagramclone.models;

/**
 * Created by sheldon on 7/19/2017.
 */

public class User {
    private String email;
    private String username;
    private String user_id;
    private long phone_number;

    public User(String email, String username, String user_id, long phone_number) {
        this.email = email;
        this.username = username;
        this.user_id = user_id;
        this.phone_number = phone_number;
    }

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public long getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(long phone_number) {
        this.phone_number = phone_number;
    }
}

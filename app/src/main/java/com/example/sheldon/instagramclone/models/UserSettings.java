package com.example.sheldon.instagramclone.models;

/**
 * Created by sheldon on 7/24/2017.
 */

public class UserSettings {
    private User user;
    private UserAccountSettings settings;

    public UserSettings() {
    }

    public UserSettings(User user, UserAccountSettings settings) {
        this.user = user;
        this.settings = settings;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserAccountSettings getSettings() {
        return settings;
    }

    public void setSettings(UserAccountSettings settings) {
        this.settings = settings;
    }
}

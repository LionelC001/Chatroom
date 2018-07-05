package com.lionel.chatroom.signup.model;

public class UserDataModel {

    private String name, email;
    private Boolean isOnline;

    public UserDataModel() {
    }

    public UserDataModel(String name, String email) {
        this.name = name;
        this.email = email;
        this.isOnline = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIsOnline(boolean is) {
        isOnline = is;
    }

    public boolean getIsOnline() {
        return isOnline;
    }
}

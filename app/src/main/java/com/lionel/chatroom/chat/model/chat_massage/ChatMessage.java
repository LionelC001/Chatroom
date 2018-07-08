package com.lionel.chatroom.chat.model.chat_massage;

import android.net.Uri;

import java.util.Calendar;

public class ChatMessage {
    private String name, message, imageUri, email;
    private long time;
    private int userColor;

    public ChatMessage() {
    }

    public ChatMessage(String name, String message, String imageUri, String email, int userColor) {
        this.name = name;
        this.message = message;
        this.email = email;
        this.userColor = userColor;
        this.time = Calendar.getInstance().getTimeInMillis();
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getEmail() {
        return email;
    }

    public long getTime() {
        return time;
    }

    public int getUserColor() {
        return userColor;
    }

}

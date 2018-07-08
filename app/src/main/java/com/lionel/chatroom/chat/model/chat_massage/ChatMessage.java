package com.lionel.chatroom.chat.model.chat_massage;

import java.util.Calendar;

public class ChatMessage {
    private String name, message, imageUrl, email;
    private long time;
    private int userColor;

    public ChatMessage() {
    }

    public ChatMessage(String name, String message, String imageUrl, String email, int userColor) {
        this.name = name;
        this.message = message;
        this.imageUrl = imageUrl;
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

    public String getImageUrl() {
        return imageUrl;
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

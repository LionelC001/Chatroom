package com.lionel.chatroom.chat.model;

import java.util.Calendar;

public class ChatMessage {
    private String name, message, email;
    private long time;
    private int userColor;

    ChatMessage() {
    }

    ChatMessage(String name, String message, String email, int userColor) {
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

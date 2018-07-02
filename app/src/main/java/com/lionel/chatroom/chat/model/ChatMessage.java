package com.lionel.chatroom.chat.model;

import java.util.Calendar;
import java.util.Date;

public class ChatMessage {
    private String name, message, email;
    private long time;

    ChatMessage() {
    }

    ChatMessage(String name, String message, String email) {
        this.name = name;
        this.message = message;
        this.email = email;
        this.time = Calendar.getInstance().getTimeInMillis();
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public String getEmail() { return email;}

    public long getTime() {
        return time;
    }
}

package com.lionel.chatroom.chat.model;

import java.util.Calendar;
import java.util.Date;

public class ChatMessage {
    private String name, message;
    private long time;

    ChatMessage() {
    }

    ChatMessage(String name, String message) {
        this.name = name;
        this.message = message;
        this.time = Calendar.getInstance().getTimeInMillis();
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public long getTime() {
        return time;
    }
}

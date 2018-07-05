package com.lionel.chatroom.chat.model;

public class ChatMessageBoxColor {
    private static String[] color = new String[]{
            "#D98880", "#F1948A", "#C39BD3", "#7FB3D5", "#85C1E9",
            "#76D7C4", "#52BE80", "#F4D03F", "#F8C471", "#F0B27A"};

    public ChatMessageBoxColor() {

    }

    public static String getColor(int colorIndex) {
        return color[colorIndex];
    }
}

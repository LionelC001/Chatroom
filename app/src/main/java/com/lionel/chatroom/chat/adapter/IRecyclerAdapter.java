package com.lionel.chatroom.chat.adapter;

public interface IRecyclerAdapter {
    //回傳發送訊息時的日期
    String fetchDate();

    //更改使用者名稱
    void setUserName(String name);
}

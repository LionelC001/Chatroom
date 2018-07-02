package com.lionel.chatroom.chat.model;

import com.firebase.ui.database.FirebaseRecyclerOptions;

public interface IChatModel {
    //從Database中確認使用者名稱
    void needUserName();

    //更改使用者名稱
    void changeUserName(String newName);

    //回傳使用者名稱
    String getUserName();

    //開始製作AdapterOptions物件
    void needAdapterOptions();

    // 回傳FirebaseRecyclerAdapter建構所需參數
    FirebaseRecyclerOptions getAdapterOptions();

    //發送訊息
    void sendMessage(String msg);
}

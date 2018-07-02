package com.lionel.chatroom.chat.model;

import com.firebase.ui.database.FirebaseRecyclerOptions;

public interface IChatModel {
    //從Database中確認使用者名稱
    void checkUserName();
    //回傳使用者名稱
    String getUserName();


    //發送訊息
    void sendMessage(String msg);

    // 回傳FirebaseRecyclerAdapter建構所需參數
    FirebaseRecyclerOptions getAdapterOptions();
}

package com.lionel.chatroom.chat.model;

import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.List;

public interface IChatModel {
    //從Firebase Database 取得使用者帳號與名稱
    void needUserData();

    //更改使用者名稱
    void changeUserName(String newName);

    //回傳使用者帳號
    String getUserEmail();

    //開始製作AdapterOptions物件
    void needAdapterOptions();

    // 回傳FirebaseRecyclerAdapter建構所需參數
    FirebaseRecyclerOptions getAdapterOptions();

    //發送訊息
    void sendMessage(String msg);

    //登出帳號
    void logout();

   /* //更新正在線上的使用者狀態
    void updateOnlineUser();

    //取得正在線上的使用者名字
    List<String> getOnlineUser();*/
}

package com.lionel.chatroom.chat.model;

import android.net.Uri;

import com.firebase.ui.database.FirebaseRecyclerOptions;

public interface IChatModelFirebase {
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

    //發送圖片
    void sendImage(Uri localImageUri);

    //取得正在線上的成員名字
    void needOnlineUserList();

    //更新成員是否在線上
    void updateOnlineUserState(Boolean isOnline);

    //登出帳號
    void logout();
}

package com.lionel.chatroom.chat.presenter;

import com.lionel.chatroom.chat.adapter.RecyclerAdapter;

public interface IChatPresenter {
    //通知Model發送訊息
    void sendMessage(String msg);
    void onSendMessageFailure();

    //回傳FirebaseRecyclerAdapter物件
    RecyclerAdapter getAdapter();

    //向Adapter取得發送訊息時的日期,並回傳
    String fetchDate();
}

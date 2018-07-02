package com.lionel.chatroom.chat.presenter;

import com.lionel.chatroom.chat.adapter.RecyclerAdapter;

public interface IChatPresenter {
    //通知Model發送訊息
    void sendMessage(String msg);

    //發送訊息失敗
    void onSendMessageFailure();

    // 準備RecyclerAdapter需要的參數
    void initAdapterParams();

    //RecyclerAdapter所需參數準備就緒
    void onAdapterParamsInitDone();

    //回傳FirebaseRecyclerAdapter物件
    RecyclerAdapter getAdapter();

    //向Adapter取得發送訊息時的日期,並回傳
    String fetchMessageDate();

    //變更使用者名稱
    void changeUserName(String name);

    //變更名稱成功
    void onChangeUserNameSuccess();
}

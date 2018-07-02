package com.lionel.chatroom.chat.view;

import com.lionel.chatroom.chat.adapter.RecyclerAdapter;

public interface IChatView {
    //通知presenter需要一個Adapter, presenter會開始準備工作
    void needRecyclerAdapter();

    //確認Adapter物件可以開始運作
    void recyclerAdapterIsReady();

    //訊息傳送失敗
    void onSendMessageFailure(String msg);

    //RecyclerView捲動時,在上方顯示日期
    void showMessageDate();

    void hideMessageDate();
}

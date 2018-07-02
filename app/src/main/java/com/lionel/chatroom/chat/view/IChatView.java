package com.lionel.chatroom.chat.view;

public interface IChatView {
    //訊息傳送失敗
    void onSendMessageFailure(String msg);
    //RecyclerView捲動時,在上方顯示日期
    void showDate();
    void hideDate();
}

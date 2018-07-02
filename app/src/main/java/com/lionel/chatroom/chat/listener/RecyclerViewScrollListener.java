package com.lionel.chatroom.chat.listener;

import android.support.v7.widget.RecyclerView;

import com.lionel.chatroom.chat.view.IChatView;

//此類負責監聽RecyclerView的捲動狀態
public class RecyclerViewScrollListener extends RecyclerView.OnScrollListener{
    private IChatView chatView;

    public RecyclerViewScrollListener(IChatView view) {
        chatView = view;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState==0) {
            chatView.hideMessageDate();
        }else {
            chatView.showMessageDate();
        }
    }
}

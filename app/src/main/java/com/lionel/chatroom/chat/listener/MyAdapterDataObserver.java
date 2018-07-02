package com.lionel.chatroom.chat.listener;

import android.support.v7.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

//此類為觀察者,負責監聽RecyclerAdapter的狀態變化
public class MyAdapterDataObserver extends RecyclerView.AdapterDataObserver {
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter recyclerAdapter;

    public MyAdapterDataObserver(RecyclerView view, FirebaseRecyclerAdapter adapter) {
        recyclerView = view;
        recyclerAdapter = adapter;
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        //當Adapter有新數據時,就發出通知
        //讓RecyclerView保持在最新消息的位置
        recyclerView.smoothScrollToPosition(recyclerAdapter.getItemCount());
    }
}

package com.lionel.chatroom.chat.presenter;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.lionel.chatroom.chat.adapter.RecyclerAdapter;
import com.lionel.chatroom.chat.model.ChatModel;
import com.lionel.chatroom.chat.model.IChatModel;
import com.lionel.chatroom.chat.view.IChatView;

public class ChatPresenterImpl implements IChatPresenter {
    private IChatView chatView;
    private IChatModel chatModel;
    private RecyclerAdapter adapter;

    public ChatPresenterImpl(IChatView view) {
        chatView = view;
        chatModel = new ChatModel(ChatPresenterImpl.this);
    }


    @Override
    public void sendMessage(String msg) {
        chatModel.sendMessage(msg);
    }

    @Override
    public void onSendMessageFailure() {
        String msg = "訊息發送失敗";
        chatView.onSendMessageFailure(msg);
    }

    @Override
    public RecyclerAdapter getAdapter() {
        FirebaseRecyclerOptions options = chatModel.getAdapterOptions();
        adapter = new RecyclerAdapter(options, chatModel.getUserName());
        return (adapter);
    }

    @Override
    public String fetchDate() {
        return adapter.fetchDate();
    }
}

package com.lionel.chatroom.chat.presenter;

import android.util.Log;

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
    public void initAdapterParams() {
        chatModel.needUserName();
        chatModel.needAdapterOptions();
    }

    @Override
    public void onAdapterParamsInitDone() {
        chatView.recyclerAdapterIsReady();
    }

    @Override
    public RecyclerAdapter getAdapter() {
        adapter = new RecyclerAdapter(chatModel.getAdapterOptions(), chatModel.getUserName());
        return adapter;
    }

    @Override
    public String fetchMessageDate() {
        return adapter.fetchDate();
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
    public void changeUserName(String name) {
        chatModel.changeUserName(name);
    }

    @Override
    public void onChangeUserNameSuccess(String newName) {
        adapter.setUserName(newName);
    }
}

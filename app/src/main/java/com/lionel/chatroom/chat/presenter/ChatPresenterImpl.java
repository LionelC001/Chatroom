package com.lionel.chatroom.chat.presenter;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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
        if (isNetworkAvailable()) {
            chatModel.needUserData();
            chatModel.needAdapterOptions();
            chatView.showProgress();
        } else {
            chatView.showNeedNetwork();
        }
    }

    @Override
    public void onAdapterParamsInitDone() {
        chatView.recyclerAdapterIsReady();
        chatView.hideProgress();
    }

    @Override
    public RecyclerAdapter getAdapter() {
        adapter = new RecyclerAdapter(chatModel.getAdapterOptions(), chatModel.getUserEmail());
        return adapter;
    }

    @Override
    public String fetchMessageDate() {
        return adapter.fetchDate();
    }


    @Override
    public void sendMessage(String msg) {
        if (isNetworkAvailable()) {
            chatModel.sendMessage(msg);
        } else {
            chatView.showNeedNetwork();
        }
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
    public void onChangeUserNameSuccess() {
        chatView.onChangeUserNameSuccess();
    }

    @Override
    public void logout() {
        chatModel.logout();
    }

    @Override
    public void onLogoutSuccess() {
        chatView.onLogoutSuccess();
    }

    @Override
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) ((Activity) chatView).getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = null ;
        if (connectivityManager!= null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo!=null && activeNetworkInfo.isConnected();
    }

    @Override
    public void quitChatRoom() {
        chatView.showQuitMessage();
    }
}

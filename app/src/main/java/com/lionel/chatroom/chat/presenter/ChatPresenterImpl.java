package com.lionel.chatroom.chat.presenter;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.SimpleAdapter;

import com.lionel.chatroom.R;
import com.lionel.chatroom.chat.adapter.ChatRecyclerAdapter;
import com.lionel.chatroom.chat.model.ChatModel;
import com.lionel.chatroom.chat.model.IChatModel;
import com.lionel.chatroom.chat.view.IChatView;

import java.util.List;
import java.util.Map;

public class ChatPresenterImpl implements IChatPresenter {
    private IChatView chatView;
    private IChatModel chatModel;
    private ChatRecyclerAdapter adapter;

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

            //使用者登入成功,更新成上線狀態
            chatModel.updateOnlineUserState(true);
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
    public ChatRecyclerAdapter getAdapter() {
        adapter = new ChatRecyclerAdapter(chatModel.getAdapterOptions(), chatModel.getUserEmail());
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
        //使用者登出成功,更新成下線狀態
        updateOfflineUserState();
    }

    @Override
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) ((Activity) chatView).getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void quitChatRoom() {
        chatView.showQuitMessage();
    }

    @Override
    public void updateOfflineUserState() {
        //使用者離開聊天室,更新成下線狀態
        chatModel.updateOnlineUserState(false);
    }

    @Override
    public void needOnlineUserList() {
        chatModel.needOnlineUserList();
    }

    @Override
    public void onOnlineUserListResult(List<Map<String, Object>> userList) {
        SimpleAdapter adapter = new SimpleAdapter(
                (Activity) chatView,
                userList,
                R.layout.item_chat_online_user,
                new String[]{"img_online_user", "txt_online_user"},
                new int[]{R.id.img_online_user, R.id.txt_online_user});

        chatView.showOnlineUser(adapter);
    }
}

package com.lionel.chatroom.chat.presenter;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.view.View;
import android.widget.SimpleAdapter;

import com.lionel.chatroom.R;
import com.lionel.chatroom.chat.adapter.ChatRecyclerAdapter;
import com.lionel.chatroom.chat.model.ChatModelFirebase;
import com.lionel.chatroom.chat.model.IChatModelFirebase;
import com.lionel.chatroom.chat.view.IChatView;
import com.takusemba.spotlight.SimpleTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChatPresenterImpl implements IChatPresenter {
    private IChatView chatView;
    private IChatModelFirebase chatModel;
    private ChatRecyclerAdapter adapter;

    public ChatPresenterImpl(Context context, IChatView view) {
        chatView = view;
        chatModel = new ChatModelFirebase(context, ChatPresenterImpl.this);
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
    public void sendImage(Uri localImageUri, boolean isNeedResend) {
        if (isNetworkAvailable()) {
            chatModel.sendImage(localImageUri, isNeedResend);
        } else {
            chatView.showNeedNetwork();
        }
    }

    @Override
    public void checkResendImage() {
        chatModel.checkResendImage();
    }

    @Override
    public void onSendMessageFailure(String msg) {
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
    public void updateOnlineUserState(boolean isOnline) {
        chatModel.updateOnlineUserState(isOnline);
    }

    @Override
    public void needOnlineUserList() {
        if (isNetworkAvailable()) {
            chatModel.needOnlineUserList();
        } else {
            chatView.showNeedNetwork();
        }
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

    @Override
    public List<SimpleTarget> needIntroTargets(View... targets) {
        List<SimpleTarget> list = new ArrayList<>();

        SimpleTarget target1 = new SimpleTarget.Builder((Activity) chatView)
                .setRadius(400f)
                .setPoint(targets[0])
                .setTitle("在這裡輸入訊息")
                .build();

        SimpleTarget target2 = new SimpleTarget.Builder((Activity) chatView)
                .setRadius(90f)
                .setPoint(targets[1])
                .setTitle("按下此按鈕來發送圖片")
                .build();

        SimpleTarget target3 = new SimpleTarget.Builder((Activity) chatView)
                .setRadius(90f)
                .setPoint(targets[2])
                .setTitle("按下此按鈕來發送訊息")
                .build();

        SimpleTarget target4 = new SimpleTarget.Builder((Activity) chatView)
                .setRadius(450f)
                .setPoint(targets[3])
                .setTitle("聊天內容會顯示在這裡")
                .build();

        SimpleTarget target5 = new SimpleTarget.Builder((Activity) chatView)
                .setRadius(90f)
                .setPoint(targets[4])
                .setTitle("顯示目前線上成員")
                .build();

        SimpleTarget target6 = new SimpleTarget.Builder((Activity) chatView)
                .setRadius(80f)
                .setPoint(targets[5])
                .setTitle("退出聊天室")
                .build();

        list.add(target1);
        list.add(target2);
        list.add(target3);
        list.add(target4);
        list.add(target5);
        list.add(target6);

        return list;
    }
}

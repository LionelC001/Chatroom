package com.lionel.chatroom.chat.presenter;

import android.net.Uri;

import com.lionel.chatroom.chat.adapter.ChatRecyclerAdapter;

import java.util.List;
import java.util.Map;

public interface IChatPresenter {
    //通知Model發送訊息
    void sendMessage(String msg);

    //通知Model準備發送圖片
    void sendImage(Uri localImageUri);

    //發送訊息失敗
    void onSendMessageFailure(String msg);

    // 準備RecyclerAdapter需要的參數
    void initAdapterParams();

    //RecyclerAdapter所需參數準備就緒
    void onAdapterParamsInitDone();

    //回傳FirebaseRecyclerAdapter物件
    ChatRecyclerAdapter getAdapter();

    //向Adapter取得發送訊息時的日期,並回傳
    String fetchMessageDate();

    //變更使用者名稱
    void changeUserName(String name);

    //變更名稱成功
    void onChangeUserNameSuccess();

    //請求登出
    void logout();

    //登出成功
    void onLogoutSuccess();

    //確認是否有網路連線
    boolean isNetworkAvailable();

    //準備離開聊天室頁面
    void quitChatRoom();

    //準備顯示正在線上的成員
    void needOnlineUserList();

    //回傳製作好的Adapter
    void onOnlineUserListResult(List<Map<String, Object>> userList);

    //更新使用者上線狀態
    void updateOnlineUserState(boolean isOnline);
}

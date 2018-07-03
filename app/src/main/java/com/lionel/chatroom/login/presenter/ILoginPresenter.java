package com.lionel.chatroom.login.presenter;

public interface ILoginPresenter {
    void login(String email, String password);

    void onSuccess(String msg);

    void onFailure(String msg);

    //確認是否有網路連線
    boolean isNetworkAvailable();
}

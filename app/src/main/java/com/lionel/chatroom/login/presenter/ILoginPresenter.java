package com.lionel.chatroom.login.presenter;

public interface ILoginPresenter {
    void login(String email, String password);
    void onSuccess(String msg,String email);
    void onFailure(String msg);
}

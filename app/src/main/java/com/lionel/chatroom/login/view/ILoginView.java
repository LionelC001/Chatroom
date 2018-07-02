package com.lionel.chatroom.login.view;

public interface ILoginView {
    void onLoginSuccess(String msg,String email);
    void onLoginFailure(String msg);
}

package com.lionel.chatroom.login.model;

public interface ILoginModel {
    void login(String email, String password);

    //開始重置密碼程序
    void resetPassword(String email);
}

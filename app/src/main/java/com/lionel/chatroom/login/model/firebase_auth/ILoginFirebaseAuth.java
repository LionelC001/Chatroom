package com.lionel.chatroom.login.model.firebase_auth;

public interface ILoginFirebaseAuth {
    //使用者登入
    void login(String email, String password);

    //開始重置密碼程序
    void resetPassword(String email);
}

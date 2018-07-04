package com.lionel.chatroom.login.model.firebaseauth;

public interface ILoginModelFirebaseAuth {
    //使用者登入
    void login(String email, String password);

    //開始重置密碼程序
    void resetPassword(String email);
}

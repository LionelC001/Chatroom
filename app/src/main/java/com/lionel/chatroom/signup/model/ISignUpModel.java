package com.lionel.chatroom.signup.model;

public interface ISignUpModel {
    //註冊使用者信箱密碼
    void signUp( String name, String email, String password, int colorIndex);

    // 儲存使用者資料
    void saveUserData(String name, String email, int colorIndex);
}

package com.lionel.chatroom.login.model;


public interface ILoginModelManager {
    //這些為LoginModelFirebaseAuth的調用和回呼方法
    void login(String email, String password);

    void resetPassword(String email);

    void onLoginSuccess(String msg);

    void onLoginFailure(String msg);

    void onResetPasswordResult(String msg);

    //這些為LoginModelPreferences的調用和回呼方法
    void saveIsRememberUser(boolean isRemember);

    boolean checkIsRememberUser();

    String getUserAccount();

    void setUserAccount(String email);
}

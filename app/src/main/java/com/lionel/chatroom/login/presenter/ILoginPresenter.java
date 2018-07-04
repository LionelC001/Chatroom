package com.lionel.chatroom.login.presenter;

public interface ILoginPresenter {
    //登入
    void login(String email, String password);

    //登入成功
    void onLoginSuccess(String msg);

    //登入失敗
    void onLoginFailure(String msg);

    //確認是否有網路連線
    boolean isNetworkAvailable();

    //申請密碼重置
    void doResetPassword(String email);

    //申請密碼重製成功
    void onResetPasswordResult(String msg);

    //將記住帳號功能的勾選存起來
    void saveIsRememberUser(boolean isRemember);

    //確認記住帳號功能是否有啟動
    boolean checkIsRememberUser();

    //取得使用者帳號
    String[] getUserAccount();

    //將使用者帳號存起來
    void setUserAccount(String email, String password);
}

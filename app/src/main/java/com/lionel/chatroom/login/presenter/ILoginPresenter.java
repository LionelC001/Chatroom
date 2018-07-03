package com.lionel.chatroom.login.presenter;

public interface ILoginPresenter {
    void login(String email, String password);

    void onLoginSuccess(String msg);

    void onLoginFailure(String msg);

    //確認是否有網路連線
    boolean isNetworkAvailable();

    //申請密碼重置
    void doResetPassword(String email);

    //申請密碼重製成功
    void onResetPasswordResult(String msg);
}

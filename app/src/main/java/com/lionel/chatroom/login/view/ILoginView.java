package com.lionel.chatroom.login.view;

public interface ILoginView {
    void onLoginSuccess(String msg);
    void onLoginFailure(String msg);

    //顯示進度畫面
    void onShowProgress(String title);
    //關閉進度畫面
    void onHideProgress();

    //提示需要網路連線
    void showNeedNetwork();

    //提示重置密碼信已發出
    void showResetPasswordResult(String msg);
}

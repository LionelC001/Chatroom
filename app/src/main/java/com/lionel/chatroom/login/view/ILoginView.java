package com.lionel.chatroom.login.view;

public interface ILoginView {
    void onLoginSuccess(String msg);
    void onLoginFailure(String msg);

    //顯示進度畫面
    void onShowProgress();
    //關閉進度畫面
    void onHideProgress();

    //提示需要網路連線
    void showNeedNetwork();
}

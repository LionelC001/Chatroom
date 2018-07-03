package com.lionel.chatroom.signup.view;

public interface ISignUpView {
    void onSignUpSuccess(String msg);
    void onSignUpFailure(String msg);

    //顯示進度畫面
    void onShowProgress();
    //關閉進度畫面
    void onHideProgress();

    //提示需要網路連線
    void showNeedNetwork();
}

package com.lionel.chatroom.signup.presenter;

public interface ISignUpPresenter {
    void signUp(String name, String email, String password);

    void onSignUpSuccess(String msg);

    void onSignUpFailure(String msg);

    //確認是否有網路連線
    boolean isNetworkAvailable();
}

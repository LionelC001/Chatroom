package com.lionel.chatroom.signup.presenter;

public interface ISignUpPresenter {
    void signUp(String name, String email, String password);
    void onSignUpSuccess(String msg);
    void onSignUpFailure(String msg);

}

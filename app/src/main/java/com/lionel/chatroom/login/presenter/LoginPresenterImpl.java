package com.lionel.chatroom.login.presenter;

import com.lionel.chatroom.login.model.ILoginModel;
import com.lionel.chatroom.login.model.LoginModel;
import com.lionel.chatroom.login.view.ILoginView;

public class LoginPresenterImpl implements ILoginPresenter {
    private ILoginView loginView;
    private ILoginModel loginModel;

    public LoginPresenterImpl(ILoginView view) {
        this.loginView = view;
        this.loginModel = new LoginModel(this);
    }

    @Override
    public void login(String email, String password) {
        if (email.equals("") ||password.equals("")) {
           String msg = "欄位不可空白";
           onFailure(msg);
        } else {
            loginModel.login(email, password);
        }
    }

    @Override
    public void onSuccess(String msg, String email) {
        loginView.onLoginSuccess(msg, email);
    }

    @Override
    public void onFailure(String msg) {
        loginView.onLoginFailure(msg);
    }
}

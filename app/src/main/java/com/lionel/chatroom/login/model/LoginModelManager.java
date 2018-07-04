package com.lionel.chatroom.login.model;

import android.content.Context;

import com.lionel.chatroom.login.model.firebaseauth.ILoginModelFirebaseAuth;
import com.lionel.chatroom.login.model.firebaseauth.LoginModelFirebaseAuth;
import com.lionel.chatroom.login.model.preferences.ILoginModelPreferences;
import com.lionel.chatroom.login.model.preferences.LoginModelPreferences;
import com.lionel.chatroom.login.presenter.ILoginPresenter;

public class LoginModelManager implements ILoginModelManager {
    private ILoginPresenter loginPresenter;
    private ILoginModelFirebaseAuth loginModelFirebaseAuth;
    private ILoginModelPreferences loginModelPreferences;

    public LoginModelManager(Context context, ILoginPresenter presenter) {
        this.loginPresenter = presenter;
        this.loginModelFirebaseAuth = new LoginModelFirebaseAuth(this);
        this.loginModelPreferences = new LoginModelPreferences(context, this);
    }

    @Override
    public void login(String email, String password) {
        loginModelFirebaseAuth.login(email, password);
    }

    @Override
    public void resetPassword(String email) {
        loginModelFirebaseAuth.resetPassword(email);
    }

    @Override
    public void onLoginSuccess(String msg) {
        loginPresenter.onLoginSuccess(msg);
    }

    @Override
    public void onLoginFailure(String msg) {
        loginPresenter.onLoginFailure(msg);
    }

    @Override
    public void onResetPasswordResult(String msg) {
        loginPresenter.onResetPasswordResult(msg);
    }

    @Override
    public void saveIsRememberUser(boolean isRemember) {
        loginModelPreferences.saveIsRememberUser(isRemember);
    }

    @Override
    public boolean checkIsRememberUser() {
        return loginModelPreferences.checkIsRememberUser();
    }

    @Override
    public String getUserAccount() {
        return loginModelPreferences.getUserAccount();
    }

    @Override
    public void setUserAccount(String email) {
        loginModelPreferences.setUserAccount(email);
    }
}

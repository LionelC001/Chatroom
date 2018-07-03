package com.lionel.chatroom.login.presenter;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

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

        if (email.equals("") || password.equals("")) {
            String msg = "欄位不可空白";
            onFailure(msg);
        } else {
            if (isNetworkAvailable()) {
                loginView.onShowProgress();
                loginModel.login(email, password);
            } else {
                loginView.showNeedNetwork();
            }
        }
    }

    @Override
    public void onSuccess(String msg) {
        loginView.onLoginSuccess(msg);
        loginView.onHideProgress();
    }

    @Override
    public void onFailure(String msg) {
        loginView.onLoginFailure(msg);
        loginView.onHideProgress();
    }

    @Override
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) ((Activity) loginView).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

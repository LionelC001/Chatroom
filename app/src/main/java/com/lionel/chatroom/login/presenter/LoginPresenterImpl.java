package com.lionel.chatroom.login.presenter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;

import com.lionel.chatroom.login.model.ILoginModelManager;
import com.lionel.chatroom.login.model.LoginModelManager;
import com.lionel.chatroom.login.view.ILoginView;

public class LoginPresenterImpl implements ILoginPresenter {
    private ILoginView loginView;
    private ILoginModelManager loginModelManager;

    public LoginPresenterImpl(Context context, ILoginView view) {
        this.loginView = view;
        this.loginModelManager = new LoginModelManager(context,this);
    }

    @Override
    public void login(String email, String password) {
        if (email.equals("") || password.equals("")) {
            String msg = "欄位不可空白";
            onLoginFailure(msg);
        } else {
            if (isNetworkAvailable()) {
                String title = "登入中";
                loginView.onShowProgress(title);
                loginModelManager.login(email.trim(), password);
            } else {
                loginView.showNeedNetwork();
            }
        }
    }

    @Override
    public void onLoginSuccess(String msg) {
        loginView.onLoginSuccess(msg);
        loginView.onHideProgress();
        loginView.saveUserAccountToSP();
    }

    @Override
    public void onLoginFailure(String msg) {
        loginView.onLoginFailure(msg);
        loginView.onHideProgress();
    }

    @Override
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) ((Fragment) loginView).getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void doResetPassword(String email) {
        if (email.equals("")) {
            String msg = "欄位不可空白";
            onResetPasswordResult(msg);
        } else {
            if (isNetworkAvailable()) {
                String title = "讀取中";
                loginView.onShowProgress(title);
                loginModelManager.resetPassword(email.trim());
            } else {
                loginView.showNeedNetwork();
            }
        }
    }

    @Override
    public void onResetPasswordResult(String msg) {
        loginView.showResetPasswordResult(msg);
        loginView.onHideProgress();
    }

    @Override
    public void saveIsRememberUser(boolean isRemember) {
        loginModelManager.saveIsRememberUser(isRemember);
    }

    @Override
    public boolean checkIsRememberUser() {
        return loginModelManager.checkIsRememberUser();
    }

    @Override
    public String[] getUserAccount() {
        return loginModelManager.getUserAccount();
    }

    @Override
    public void setUserAccount(String email, String password) {
        loginModelManager.setUserAccount(email, password);
    }
}

package com.lionel.chatroom.signup.presenter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;

import com.lionel.chatroom.signup.model.ISignUpModel;
import com.lionel.chatroom.signup.model.SignUpModel;
import com.lionel.chatroom.signup.view.ISignUpView;

import java.util.Random;

public class SignUpPresenterImpl implements ISignUpPresenter {
    private ISignUpView signUpView;
    private ISignUpModel signUpModel;

    public SignUpPresenterImpl(ISignUpView view) {
        signUpView = view;
        signUpModel = new SignUpModel(this);
    }

    @Override
    public void signUp(String name, String email, String password) {
        if (email.equals("") || password.equals("")) {
            String msg = "欄位不可空白";
            onSignUpFailure(msg);
        } else if (password.length() < 6) {
            String msg = "密碼需至少六個字符以上";
            onSignUpFailure(msg);
        } else {
            if (isNetworkAvailable()) {
                int colorIndex = (int)(Math.random()*10);
                signUpModel.signUp(name, email.trim(), password, colorIndex);
                signUpView.onShowProgress();
            } else {
                signUpView.showNeedNetwork();
            }
        }
    }

    @Override
    public void onSignUpSuccess(String msg) {
        signUpView.onSignUpSuccess(msg);
        signUpView.onHideProgress();
    }

    @Override
    public void onSignUpFailure(String msg) {
        signUpView.onSignUpFailure(msg);
        signUpView.onHideProgress();
    }

    @Override
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) ((Fragment) signUpView).getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

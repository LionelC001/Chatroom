package com.lionel.chatroom.login.model;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.lionel.chatroom.login.presenter.ILoginPresenter;

public class LoginModel implements ILoginModel {
    private final ILoginPresenter loginPresenter;
    private final FirebaseAuth mAuth;

    public LoginModel(ILoginPresenter presenter) {
        this.loginPresenter = presenter;
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String msg;
                        if (task.isSuccessful()) {
                            msg = "已成功登入";
                            loginPresenter.onSuccess(msg, mAuth.getCurrentUser().getEmail());
                        } else {
                            msg = "請確認您的帳號與密碼是否正確";
                            loginPresenter.onFailure(msg);
                        }
                    }
                });
    }
}

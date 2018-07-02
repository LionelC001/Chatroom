package com.lionel.chatroom.signup.model;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.lionel.chatroom.signup.presenter.ISignUpPresenter;

public class SignUpModel implements ISignUpModel {

    private final ISignUpPresenter signUpPresenter;
    private final FirebaseAuth mAuth;

    public SignUpModel(ISignUpPresenter presenter) {
        signUpPresenter = presenter;
        mAuth = FirebaseAuth.getInstance();
    }

    // 註冊成功後, 開始儲存使用者資料至Firebase Database
    @Override
    public void signUp(final String name, final String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            saveUserData(name, email);
                        } else {
                            String msg = "資料有誤,請重新確認";
                            signUpPresenter.onSignUpFailure(msg);
                        }
                    }
                });
    }

    //儲存使用者資料
    @Override
    public void saveUserData(String name, String email) {
        FirebaseDatabase.getInstance()
                .getReference("user")
                .child(email.replace(".",""))
                .setValue(new UserDataModel(name, email))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            String msg = "已成功註冊";
                            signUpPresenter.onSignUpSuccess(msg);
                        } else {
                            String msg = "註冊失敗";
                            signUpPresenter.onSignUpFailure(msg);
                        }
                    }
                });
    }
}

package com.lionel.chatroom.signup.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
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
                        }
                    }
                })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("<<<", "error: " + e.getMessage());
                String msg = null;
                switch(((FirebaseAuthException)e).getErrorCode()) {
                    case "ERROR_EMAIL_ALREADY_IN_USE":
                        msg = "此信箱已被使用過,請重新確認";
                        break;
                    default:
                        msg = "資料格式有誤,請重新確認";
                }
                signUpPresenter.onSignUpFailure(msg);
            }
        });
    }

    //儲存使用者資料
    @Override
    public void saveUserData(String name, String email) {
        FirebaseDatabase.getInstance()
                .getReference("user")
                .child(email.replace(".", ""))
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

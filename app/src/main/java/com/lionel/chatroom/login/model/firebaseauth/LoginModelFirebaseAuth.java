package com.lionel.chatroom.login.model.firebaseauth;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.lionel.chatroom.login.model.ILoginModelManager;

public class LoginModelFirebaseAuth implements ILoginModelFirebaseAuth {
    private final ILoginModelManager loginModelManager;
    private final FirebaseAuth mAuth;

    public LoginModelFirebaseAuth(ILoginModelManager modelManager) {
        loginModelManager = modelManager;
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
                            loginModelManager.onLoginSuccess(msg);
                        } else {
                            msg = "請確認您的帳號與密碼是否正確";
                            loginModelManager.onLoginFailure(msg);
                        }
                    }
                });
    }

    @Override
    public void resetPassword(String email) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            String msg;
                            msg = "密碼重置信已發到您的電子信箱,請確認";
                            loginModelManager.onResetPasswordResult(msg);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String msg;
                        switch (((FirebaseAuthException) e).getErrorCode()) {
                            case "ERROR_INVALID_EMAIL":
                                msg = "電子信箱格式錯誤,請確認";
                                break;
                            case "ERROR_USER_NOT_FOUND":
                                msg = "沒有此使用者信箱,請確認";
                                break;
                            default:
                                msg = "重置流程錯誤,請洽技術人員 ";
                                break;
                        }
                        loginModelManager.onResetPasswordResult(msg);
                    }
                });
    }
}

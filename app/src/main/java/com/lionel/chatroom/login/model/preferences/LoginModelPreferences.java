package com.lionel.chatroom.login.model.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.lionel.chatroom.login.model.ILoginModelManager;

public class LoginModelPreferences implements ILoginModelPreferences {
    private SharedPreferences mSp;
    private ILoginModelManager loginModelManager;

    public LoginModelPreferences(Context context, ILoginModelManager modelManager) {
        this.loginModelManager = modelManager;
        mSp = context.getSharedPreferences("remember_acc", Context.MODE_PRIVATE);
    }

    @Override
    public void saveIsRememberUser(boolean isRemember) {
        mSp.edit().putBoolean("is_remember_user", isRemember).apply();
    }

    @Override
    public boolean checkIsRememberUser() {
        return mSp.getBoolean("is_remember_user", false);
    }

    @Override
    public String[] getUserAccount() {
        return new String[]{
                mSp.getString("user_email", ""),
                mSp.getString("user_password", "")};
    }

    @Override
    public void setUserAccount(String email, String password) {
        mSp.edit().putString("user_email", email).apply();
        mSp.edit().putString("user_password", password).apply();
    }
}

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
    public String getUserAccount() {
        return mSp.getString("user_account", "");
    }

    @Override
    public void setUserAccount(String email) {
        mSp.edit().putString("user_account", email).apply();
    }
}

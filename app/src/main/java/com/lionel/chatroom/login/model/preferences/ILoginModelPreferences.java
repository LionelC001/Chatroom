package com.lionel.chatroom.login.model.preferences;

public interface ILoginModelPreferences {
    void saveIsRememberUser(boolean isRemember);

    boolean checkIsRememberUser();

    String getUserAccount();

    void setUserAccount(String email);
}

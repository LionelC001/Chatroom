package com.lionel.chatroom.chat.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;

import com.lionel.chatroom.R;

public class ChatOnlineUserDialog extends Dialog {
    public ChatOnlineUserDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_chat_online_user);
        setCancelable(true);
        getWindow().setWindowAnimations(R.style.AnimDialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}

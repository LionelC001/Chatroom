package com.lionel.chatroom.chat.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.lionel.chatroom.chat.listener.FirebaseDatabaseListener;
import com.lionel.chatroom.chat.presenter.IChatPresenter;

public class ChatModel implements IChatModel {
    private IChatPresenter chatPresenter;
    private String userName;

    public ChatModel(IChatPresenter presenter) {
        chatPresenter = presenter;
        checkUserName();
    }

    @Override
    public void checkUserName() {
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();



        Log.d("user", userEmail);
        Log.d("user", "Name: " + userName);
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public void sendMessage(String msg) {
        FirebaseDatabase.getInstance()
                .getReference("chat_room")
                .push()
                .setValue(new ChatMessage(userName, msg))
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                chatPresenter.onSendMessageFailure();
            }
        });
    }

    @Override
    public FirebaseRecyclerOptions getAdapterOptions() {
        //  查詢FirebaseDatabase裡的聊天紀錄
        Query query = FirebaseDatabase.getInstance()
                .getReference("chat_room")
                .limitToLast(100);

        query.addChildEventListener(new FirebaseDatabaseListener());

        // 表示要用剛剛的query物件,以及聊天內容物件ChatMessage.class
        FirebaseRecyclerOptions<ChatMessage> options =
                new FirebaseRecyclerOptions.Builder<ChatMessage>()
                .setQuery(query, ChatMessage.class)
                .build();

        return options;
    }
}

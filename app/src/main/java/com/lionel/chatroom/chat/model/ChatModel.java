package com.lionel.chatroom.chat.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lionel.chatroom.R;
import com.lionel.chatroom.chat.listener.FirebaseDatabaseListener;
import com.lionel.chatroom.chat.presenter.IChatPresenter;
import com.lionel.chatroom.signup.model.UserDataModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatModel implements IChatModel {
    private IChatPresenter chatPresenter;
    private String userName, userEmail;
    private FirebaseRecyclerOptions<ChatMessage> options;
    private int uesrColor;

    public ChatModel(IChatPresenter presenter) {
        chatPresenter = presenter;
    }

    @Override
    public void needUserData() {
        userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        FirebaseDatabase.getInstance()
                .getReference()
                .child("user")
                .orderByChild("email")  //按路徑下子節點的值做排序
                .equalTo(userEmail)
                .addChildEventListener(new FirebaseDatabaseListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        super.onChildAdded(dataSnapshot, s);
                        UserDataModel user = dataSnapshot.getValue(UserDataModel.class);
                        if (user != null) {
                            userName = user.getName();
                            uesrColor = user.getColorIndex();
                            chatPresenter.onAdapterParamsInitDone();
                        }
                    }
                });
    }

    @Override
    public void needAdapterOptions() {
        //  查詢FirebaseDatabase裡的聊天紀錄
        Query query = FirebaseDatabase.getInstance()
                .getReference("chat_room")
                .limitToLast(100);

        query.addChildEventListener(new FirebaseDatabaseListener());

        // 表示要用剛剛的query物件,以及聊天內容物件ChatMessage.class
        options = new FirebaseRecyclerOptions.Builder<ChatMessage>()
                .setQuery(query, ChatMessage.class)
                .build();
    }

    @Override
    public String getUserEmail() {
        return userEmail;
    }

    @Override
    public FirebaseRecyclerOptions getAdapterOptions() {
        return options;
    }

    @Override
    public void changeUserName(final String newName) {
        userName = newName;
        FirebaseDatabase.getInstance()
                .getReference("user")
                .child(userEmail.replace(".", ""))
                .child("name")
                .setValue(newName)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        chatPresenter.onChangeUserNameSuccess();
                    }
                });
    }

    @Override
    public void sendMessage(String msg) {
        FirebaseDatabase.getInstance()
                .getReference("chat_room")
                .push()
                .setValue(new ChatMessage(userName, msg, userEmail, uesrColor))
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        chatPresenter.onSendMessageFailure();
                    }
                });
    }

    @Override
    public void logout() {
        FirebaseAuth.getInstance().signOut();
        chatPresenter.onLogoutSuccess();
    }

    @Override
    public void needOnlineUserList() {
        FirebaseDatabase.getInstance()
                .getReference()
                .child("user")
                .orderByChild("isOnline")
                .equalTo(true)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Map<String, Object>> listOnlineUser = new ArrayList<>();
                        for (DataSnapshot element : dataSnapshot.getChildren()) {
                            UserDataModel user = element.getValue(UserDataModel.class);
                            Map<String, Object> map = new HashMap<>();
                            map.put("img_online_user", R.drawable.ic_chat_online_user_person);
                            map.put("txt_online_user", user.getName());
                            listOnlineUser.add(map);
                        }
                        chatPresenter.onOnlineUserListResult(listOnlineUser);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }

    @Override
    public void updateOnlineUserState(final Boolean is) {
        FirebaseDatabase.getInstance()
                .getReference("user")
                .child(userEmail.replace(".", ""))
                .child("isOnline")
                .setValue(is)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
    }
}

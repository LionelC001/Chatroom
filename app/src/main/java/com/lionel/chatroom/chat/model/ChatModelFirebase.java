package com.lionel.chatroom.chat.model;

import android.net.Uri;
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
import com.lionel.chatroom.chat.model.chat_massage.ChatMessage;
import com.lionel.chatroom.chat.presenter.IChatPresenter;
import com.lionel.chatroom.signup.model.UserDataModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatModelFirebase implements IChatModelFirebase {
    private IChatPresenter chatPresenter;
    private String userName, userEmail;
    private FirebaseRecyclerOptions<ChatMessage> options;
    private int userColor;

    public ChatModelFirebase(IChatPresenter presenter) {
        chatPresenter = presenter;
    }

    @Override
    public void needUserData() {
        //使用者本人的登記信箱
        userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        //以此信箱來比對使用者資料
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
                            userColor = user.getColorIndex();
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
                .setValue(new ChatMessage(userName, msg, "", userEmail, userColor))
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String msg = "訊息發送失敗";
                        chatPresenter.onSendMessageFailure(msg);
                    }
                });
    }

    @Override
    public void sendImage(Uri localImageUri) {
        // 暫時的圖片訊息, 向使用者顯示圖片上傳進度
        FirebaseDatabase.getInstance()
                .getReference("chat_room")
                .push()
                .setValue(new ChatMessage(userName, "", "", userEmail, userColor))
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String msg = "圖片發送失敗";
                        chatPresenter.onSendMessageFailure(msg);
                    }
                });
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
                            map.put("txt_online_user", user.getName());

                            //如果是使用者本人, 則用別種顏色的頭像
                            if (user.getName().equals(userName)) {
                                map.put("img_online_user", R.drawable.ic_chat_online_user_person_blue);
                            } else {
                                map.put("img_online_user", R.drawable.ic_chat_online_user_person);
                            }

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
                .setValue(is);
    }

    @Override
    public void logout() {
        FirebaseAuth.getInstance().signOut();
        chatPresenter.onLogoutSuccess();
    }
}

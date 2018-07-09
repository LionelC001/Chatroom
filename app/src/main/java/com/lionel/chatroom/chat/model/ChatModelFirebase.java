package com.lionel.chatroom.chat.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
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
    private SharedPreferences resendImageSP;

    public ChatModelFirebase(Context context, IChatPresenter presenter) {
        chatPresenter = presenter;
        resendImageSP = context.getSharedPreferences("resend_image", Context.MODE_PRIVATE);
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
    public void sendImage(final Uri localImageUri, boolean isNeedResend) {
        UploadTask uploadTask = null;

        // FirebaseDatabase, 先用push()生成一子節點, 並取得其路徑
        DatabaseReference imageMessageRef = FirebaseDatabase.getInstance()
                .getReference("chat_room")
                .push();

        //取得剛剛push()所生成的唯一鍵
        final String key = imageMessageRef.getKey();
        //檢查是否有舊的key, 若沒有則填入此key
        if (resendImageSP.getString("key", null) == null) {
            resendImageSP.edit().putString("key", key).apply();
        }

        //取得FirebaseStorage物件以及上傳路徑
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference imageRef = storage.getReference().child("image/" + localImageUri.getLastPathSegment());

        //如果不是重新上傳圖片, 用PutFile(Uri)上傳圖片
        if (!isNeedResend) {
            // 只有第一次才需要上傳暫時的圖片訊息, 向使用者顯示圖片正在上傳,
            imageMessageRef.setValue(new ChatMessage(userName, "", "temp", userEmail, userColor))
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String msg = "圖片發送失敗";
                            chatPresenter.onSendMessageFailure(msg);
                        }
                    });

            //將圖片上傳到Firebase Storage
            uploadTask = imageRef.putFile(localImageUri);
        } else {
            //如果是重新上傳圖片, 則用putFile(Uri, StorageMetadata, sessionUri)上傳圖片
            String sessionUri = resendImageSP.getString("resend_session", null);
            if (sessionUri != null) {
                uploadTask = imageRef.putFile(localImageUri, new StorageMetadata.Builder().build(), Uri.parse(sessionUri));
            }
        }

        //傾聽上傳任務的事件
        if (uploadTask != null) {
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    //取得此上傳任務的上傳會話
                    Uri sessionUri = taskSnapshot.getUploadSessionUri();
                    if (sessionUri != null) {
                        resendImageSP.edit()
                                .putBoolean("need_resend", true)
                                .putString("local_image", localImageUri.toString())
                                .putString("resend_session", sessionUri.toString())
                                .apply();
                    }
                }
            }).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    return imageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        //取得圖片下載位置
                        //並更新圖片訊息, 取代原本在database上的暫時圖片訊息
                        Uri downloadUrl = task.getResult();

                        //若資料庫裡有key, 則用此key做路徑
                        String path = key;
                        String oldKey = resendImageSP.getString("key", null);
                        if (oldKey != null) {
                            path = oldKey;
                        }
                        FirebaseDatabase.getInstance()
                                .getReference("chat_room")
                                .child(path)
                                .setValue(new ChatMessage(userName, "", downloadUrl.toString(), userEmail, userColor))
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        String msg = "圖片發送失敗";
                                        chatPresenter.onSendMessageFailure(msg);
                                    }
                                });

                        //若傳送圖片成功,則表示不需要再重新傳送
                        //重置資料庫裡FirebaseDatabase的key
                        resendImageSP.edit()
                                .putBoolean("need_resend", false)
                                .putString("key", null)
                                .apply();
                    }
                }
            });
        }
    }

    @Override
    public void checkResendImage() {
        boolean isNeedResend = resendImageSP.getBoolean("need_resend", false);
        if (isNeedResend) {
            String localImageUri = resendImageSP.getString("local_image", null);
            if (localImageUri != null) {
                chatPresenter.sendImage(Uri.parse(localImageUri), true);
            }
        }
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

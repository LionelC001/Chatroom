package com.lionel.chatroom.chat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lionel.chatroom.R;
import com.lionel.chatroom.chat.adapter.RecyclerAdapter;
import com.lionel.chatroom.chat.listener.MyAdapterDataObserver;
import com.lionel.chatroom.chat.listener.RecyclerViewScrollListener;
import com.lionel.chatroom.chat.presenter.ChatPresenterImpl;
import com.lionel.chatroom.chat.presenter.IChatPresenter;
import com.lionel.chatroom.chat.view.IChatView;

public class ChatActivity extends AppCompatActivity implements IChatView {
    private IChatPresenter chatPresenter;
    private EditText mEdtMsg;
    private RecyclerView mRecyclerViewChat;
    private RecyclerAdapter adapter;
    private MyAdapterDataObserver adapterDataObserver;
    private TextView mTxtDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chatPresenter = new ChatPresenterImpl(this);

        initView();
    }

    private void initView() {
        mEdtMsg = findViewById(R.id.edtMsg);
        mRecyclerViewChat = findViewById(R.id.recyclerViewChat);
        mTxtDate = findViewById(R.id.txt_date);
        needRecyclerAdapter();
    }

    @Override
    public void needRecyclerAdapter() {
        chatPresenter.initAdapterParams();
    }

    @Override
    public void recyclerAdapterIsReady() {
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        adapter = chatPresenter.getAdapter();
        mRecyclerViewChat.setAdapter(adapter);
        mRecyclerViewChat.setLayoutManager(new LinearLayoutManager(ChatActivity.this));

        //adapter的資料讀取不同步, 由此Observer才能獲取adapter的最新item count
        //目的是讓recyclerview保持在最新消息的位置
        adapterDataObserver = new MyAdapterDataObserver(mRecyclerViewChat, adapter);

        //捲動recyclerview時,顯示日期
        mRecyclerViewChat.addOnScrollListener(new RecyclerViewScrollListener(ChatActivity.this));

        //Adapter開始對Firebase Database傾聽
        adapter.startListening();
        //開始對adapter的data傾聽
        adapter.registerAdapterDataObserver(adapterDataObserver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
            adapter.registerAdapterDataObserver(adapterDataObserver);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
            adapter.unregisterAdapterDataObserver(adapterDataObserver);
        }
    }

    @Override
    public void showMessageDate() {
        mTxtDate.setText(chatPresenter.fetchMessageDate());
        mTxtDate.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideMessageDate() {
        mTxtDate.setVisibility(View.GONE);
    }

    //發送訊息
    public void onSend(View view) {
        String msg = mEdtMsg.getText().toString();
        chatPresenter.sendMessage(msg);
        mEdtMsg.setText("");
    }

    @Override
    public void onSendMessageFailure(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}

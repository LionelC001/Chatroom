package com.lionel.chatroom.chat;

import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
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
    private AlertDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chatPresenter = new ChatPresenterImpl(this);

        initView();
    }

    private void initView() {
        mEdtMsg = findViewById(R.id.edt_chat_msg);
        mRecyclerViewChat = findViewById(R.id.recycler_view_chat);
        mTxtDate = findViewById(R.id.txt_date);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chatview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_chatview_change_name:
                changeUserName();
                break;
            case R.id.menu_chatview_log_out:
                showLogoutMessage();
                break;
            case android.R.id.home:
               chatPresenter.quitChatRoom();
                break;
        }
        return true;
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

    private void changeUserName() {
        View view = LayoutInflater.from(ChatActivity.this).inflate(R.layout.dialog_chatroom_change_name, null);
        final EditText mEdtDialogChangeName = view.findViewById(R.id.edt_dialog_change_name);
        AlertDialog alertDialog = new AlertDialog.Builder(ChatActivity.this)
                .setTitle("變更名稱")
                .setView(view)
                .setMessage("(最多12個字)")
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chatPresenter.changeUserName(mEdtDialogChangeName.getText().toString());
                    }
                })
                .setNegativeButton("取消", null)
                .setCancelable(true)
                .show();

        alertDialog.getWindow().setWindowAnimations(R.style.AnimDialog);
    }

    @Override
    public void onChangeUserNameSuccess() {
        Toast.makeText(this, "名稱變更成功", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLogoutSuccess() {
        Toast.makeText(this, "已成功登出", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void showProgress() {
        AlertDialog.Builder adBuilder = new AlertDialog.Builder(ChatActivity.this);
        progress = adBuilder.create();
        View view = LayoutInflater.from(ChatActivity.this).inflate(R.layout.dialog_loading_progress, null);
        ((TextView) view.findViewById(R.id.txt_progress_title)).setText("讀取中");
        ((TextView) view.findViewById(R.id.txt_progress_message)).setText("請稍後...");
        ((ProgressBar) view.findViewById(R.id.progress_bar)).getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorDialogProgress), PorterDuff.Mode.MULTIPLY);
        progress.setView(view);
        progress.setCancelable(false);
        progress.show();

        //對話框出現時,底下視圖不可操控
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        //實現對話框動畫
        Window progressWindow = progress.getWindow();
        progressWindow.setWindowAnimations(R.style.AnimDialog);
    }

    @Override
    public void hideProgress() {
        if (progress != null) {
            progress.dismiss();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    @Override
    public void showNeedNetwork() {
        Toast.makeText(this, "網路訊號不穩,請確認網路狀態", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
       chatPresenter.quitChatRoom();
    }

    @Override
    public void showQuitMessage() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("離開")
                .setMessage("確定要離開聊天室嗎?")
                .setCancelable(true)
                .setNegativeButton("取消",null)
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();

        dialog.getWindow().setWindowAnimations(R.style.AnimDialog);
    }

    private void showLogoutMessage() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("登出")
                .setMessage("確定要登出聊天室嗎?")
                .setCancelable(true)
                .setNegativeButton("取消",null)
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chatPresenter.logout();
                    }
                })
                .show();

        dialog.getWindow().setWindowAnimations(R.style.AnimDialog);
    }
}

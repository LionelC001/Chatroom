package com.lionel.chatroom.chat.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.lionel.chatroom.R;
import com.lionel.chatroom.chat.model.ChatMessage;

import java.util.Arrays;
import java.util.List;

public class ChatRecyclerAdapter extends FirebaseRecyclerAdapter<ChatMessage, ChatRecyclerAdapter.ViewHolder>
        implements IChatRecyclerAdapter {
    private String userEmail;
    private String date;
    private List<String> dateList;  //用來記錄出現過的訊息日期

    public ChatRecyclerAdapter(@NonNull FirebaseRecyclerOptions<ChatMessage> options, String email) {
        super(options);
        userEmail = email;
        dateList = Arrays.asList(new String[100]);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout mSpeechBubbleLeft, mSpeechBubbleRight;
        private TextView mTxtNameLeft, mTxtMsgLeft, mTxtTimeLeft,
                mTxtMsgRight, mTxtTimeRight, mTxtDate;

        ViewHolder(View itemView) {
            super(itemView);
            mSpeechBubbleLeft = itemView.findViewById(R.id.speech_bubble_left);
            mSpeechBubbleRight = itemView.findViewById(R.id.speech_bubble_right);
            mTxtNameLeft = itemView.findViewById(R.id.txt_name_left);
            mTxtMsgLeft = itemView.findViewById(R.id.txt_msg_left);
            mTxtMsgRight = itemView.findViewById(R.id.txt_msg_right);
            mTxtTimeLeft = itemView.findViewById(R.id.txt_time_left);
            mTxtTimeRight = itemView.findViewById(R.id.txt_time_right);
            mTxtDate = itemView.findViewById(R.id.txt_date);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_chat_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull ChatMessage model) {
        // 如果是使用者發出的訊息,一律顯示在右側
        if (model.getEmail().equals(userEmail)) {
            holder.mSpeechBubbleLeft.setVisibility(View.GONE);
            holder.mSpeechBubbleRight.setVisibility(View.VISIBLE);
            holder.mTxtMsgRight.setText(model.getMessage());
            holder.mTxtTimeRight.setText(DateFormat.format("HH:mm", model.getTime()));
        } else {
            holder.mSpeechBubbleLeft.setVisibility(View.VISIBLE);
            holder.mSpeechBubbleRight.setVisibility(View.GONE);
            holder.mTxtNameLeft.setText(model.getName());
            holder.mTxtMsgLeft.setText(model.getMessage());
            holder.mTxtTimeLeft.setText(DateFormat.format("HH:mm", model.getTime()));
        }

        // 記下訊息的日期
        date = DateFormat.format("MM-dd, EEEE", model.getTime()).toString();
        dateList.set(position, date);

        // 預設隱藏日期
        holder.mTxtDate.setVisibility(View.GONE);
        // 一天只顯示一次日期
        if (isShowDateText(position)) {
            holder.mTxtDate.setVisibility(View.VISIBLE);
            holder.mTxtDate.setText(date);
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public String fetchDate() {
        return date;
    }

    private boolean isShowDateText(int position) {
        // 第一條訊息上方必顯示日期
        // 若和上一條訊息日期不同, 在該訊息上方顯示日期
        return position == 0 || !dateList.get(position).equals(dateList.get(position - 1));
    }
}

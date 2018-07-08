package com.lionel.chatroom.chat.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.lionel.chatroom.R;
import com.lionel.chatroom.chat.listener.ImageMessageClickListener;
import com.lionel.chatroom.chat.model.chat_massage.ChatMessage;
import com.lionel.chatroom.chat.model.chat_massage.ChatMessageBoxColor;

import java.util.Arrays;
import java.util.List;

public class ChatRecyclerAdapter extends FirebaseRecyclerAdapter<ChatMessage, ChatRecyclerAdapter.ViewHolder>
        implements IChatRecyclerAdapter {
    private Context mContext;
    private String userEmail;
    private String date;
    private List<String> dateList;  //用來記錄出現過的訊息日期

    public ChatRecyclerAdapter(@NonNull FirebaseRecyclerOptions<ChatMessage> options, String email) {
        super(options);
        userEmail = email;
        dateList = Arrays.asList(new String[100]);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImgMsgLeft, mImgMsgRight;
        private ProgressBar mImgMsgRightLoading;
        private RelativeLayout mSpeechBubbleLeft, mSpeechBubbleRight, mRLMsgLeft;
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
            mRLMsgLeft = itemView.findViewById(R.id.rl_msg_left);
            mImgMsgLeft = itemView.findViewById(R.id.img_msg_left);
            mImgMsgRight = itemView.findViewById(R.id.img_msg_right);
            mImgMsgRightLoading = itemView.findViewById(R.id.img_msg_right_loading);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_chat_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull ChatMessage model) {
        showDate(holder, position, model);

        //若ImageUri有資料, 表示為傳送圖片
        if (!model.getImageUrl().equals("")) {
            showImage(holder, model);
        } else {
            showMessage(holder, model);
        }
    }

    private void showDate(@NonNull ViewHolder holder, int position, @NonNull ChatMessage model) {
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

    private void showMessage(@NonNull ViewHolder holder, @NonNull ChatMessage model) {
        // 如果是使用者發出的訊息,一律顯示在右側
        if (model.getEmail().equals(userEmail)) {
            holder.mSpeechBubbleRight.setVisibility(View.VISIBLE);
            holder.mImgMsgRight.setVisibility(View.GONE);
            holder.mImgMsgRightLoading.setVisibility(View.GONE);
            holder.mTxtMsgRight.setVisibility(View.VISIBLE);
            holder.mSpeechBubbleLeft.setVisibility(View.GONE);

            holder.mTxtMsgRight.setText(model.getMessage());
            holder.mTxtTimeRight.setText(DateFormat.format("HH:mm", model.getTime()));
        } else {
            holder.mSpeechBubbleLeft.setVisibility(View.VISIBLE);
            holder.mImgMsgLeft.setVisibility(View.GONE);
            holder.mTxtNameLeft.setText(model.getName());
            holder.mTxtMsgLeft.setVisibility(View.VISIBLE);
            holder.mSpeechBubbleRight.setVisibility(View.GONE);

            holder.mTxtMsgLeft.setText(model.getMessage());
            holder.mTxtTimeLeft.setText(DateFormat.format("HH:mm", model.getTime()));

            //依據不同的使用者, 設定不同的對話框顏色
            GradientDrawable shape = (GradientDrawable) mContext.getResources().getDrawable(R.drawable.bg_chat_message_box_left);
            String color = ChatMessageBoxColor.getColor(model.getUserColor());
            shape.setColor(Color.parseColor(color));
            holder.mRLMsgLeft.setBackground(shape);
        }
    }

    private void showImage(@NonNull ViewHolder holder, @NonNull ChatMessage model) {
        // 如果是使用者發出的訊息,一律顯示在右側
        if (model.getEmail().equals(userEmail)) {
            holder.mSpeechBubbleRight.setVisibility(View.VISIBLE);
            holder.mTxtMsgRight.setVisibility(View.GONE);
            holder.mSpeechBubbleLeft.setVisibility(View.GONE);

            holder.mTxtTimeRight.setText(DateFormat.format("HH:mm", model.getTime()));

            //如果資料來源來自firebase storage, 則使用Glide讀取圖片
            //隱藏讀取狀態
            if (model.getImageUrl().startsWith("https://firebasestorage.googleapis.com/")) {
                holder.mImgMsgRight.setVisibility(View.VISIBLE);
                holder.mImgMsgRightLoading.setVisibility(View.GONE);
                holder.mImgMsgRight.setOnClickListener(new ImageMessageClickListener(mContext, model.getImageUrl()));
                Glide.with(mContext)
                        .load(model.getImageUrl())
                        .into(holder.mImgMsgRight);
            } else {
                //否則代表正在上傳中,則顯示讀取狀態
                holder.mImgMsgRight.setVisibility(View.GONE);
                holder.mImgMsgRightLoading.setVisibility(View.VISIBLE);
            }
        } else { //非使用者本人, 顯示在左側
            holder.mSpeechBubbleLeft.setVisibility(View.VISIBLE);
            holder.mTxtMsgLeft.setVisibility(View.GONE);
            holder.mSpeechBubbleRight.setVisibility(View.GONE);

            holder.mTxtNameLeft.setText(model.getName());
            holder.mTxtTimeLeft.setText(DateFormat.format("HH:mm", model.getTime()));

            //依據不同的使用者, 設定不同的對話框顏色
            GradientDrawable shape = (GradientDrawable) mContext.getResources().getDrawable(R.drawable.bg_chat_message_box_left);
            String color = ChatMessageBoxColor.getColor(model.getUserColor());
            shape.setColor(Color.parseColor(color));
            holder.mRLMsgLeft.setBackground(shape);

            //如果資料來源來自FirebaseStorage, 則使用Glide讀取圖片
            if (model.getImageUrl().startsWith("https://firebasestorage.googleapis.com/")) {
                holder.mImgMsgLeft.setVisibility(View.VISIBLE);
                holder.mImgMsgLeft.setOnClickListener(new ImageMessageClickListener(mContext, model.getImageUrl()));

                Glide.with(mContext)
                        .load(model.getImageUrl())
                        .into(holder.mImgMsgLeft);
            }
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

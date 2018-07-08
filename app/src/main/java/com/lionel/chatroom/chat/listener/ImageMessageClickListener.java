package com.lionel.chatroom.chat.listener;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.lionel.chatroom.chat.ImageActivity;

public class ImageMessageClickListener implements View.OnClickListener {

    private Context mContext;
    private String imageUrl;


    public ImageMessageClickListener(Context c, String imageUrl) {
        mContext  = c;
        this.imageUrl = imageUrl;
    }
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setClass(mContext,ImageActivity.class);
        intent.putExtra("image_url", imageUrl);
        mContext.startActivity(intent);
    }
}

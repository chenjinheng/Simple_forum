package com.example.libarary.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.libarary.R;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by 陈金桁 on 2018/2/22.
 */

public class ReceiveVoiceHolder extends RecyclerView.ViewHolder {
    public SimpleDraweeView simpleDraweeView;
    public ImageView imageView;
    public ReceiveVoiceHolder(View itemView) {
        super(itemView);
        simpleDraweeView = (SimpleDraweeView) itemView.findViewById(R.id.iv_avatar);
        imageView = (ImageView) itemView.findViewById(R.id.iv_voice);
    }
}


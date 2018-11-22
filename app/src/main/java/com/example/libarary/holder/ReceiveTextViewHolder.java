package com.example.libarary.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.libarary.R;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by 陈金桁 on 2018/2/22.
 */
public class ReceiveTextViewHolder extends RecyclerView.ViewHolder {
    public SimpleDraweeView simpleDraweeView;
    public TextView textView;

    public ReceiveTextViewHolder(View itemView) {
        super(itemView);
        simpleDraweeView = (SimpleDraweeView) itemView.findViewById(R.id.sdv_head);
        textView= (TextView) itemView.findViewById(R.id.tv_message);
    }
}

package com.example.libarary.holder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.libarary.R;

import com.example.libarary.view.AskActivity;



/**
 * Created by 陈金桁 on 2018/2/3.
 */

public class AddFriendViewHolder extends RecyclerView.ViewHolder {

    public TextView friend;
    public AddFriendViewHolder(View itemView, final Context context) {
        super(itemView);
        friend = (TextView) itemView.findViewById(R.id.add_newFriend);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AskActivity.class);
                context.startActivity(intent);
            }
        });
    }
}

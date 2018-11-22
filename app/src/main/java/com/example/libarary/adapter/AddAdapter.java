package com.example.libarary.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.libarary.R;
import com.example.libarary.view.AskFriendActivity;
import com.example.libarary.view.FriendActivity;
import com.example.libarary.xinxibao.User;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by 陈金桁 on 2018/1/27.
 */

public class AddAdapter extends RecyclerView.Adapter<AddAdapter.ViewHolder> {
    private Context context;
    private List<User> datas;
    public AddAdapter(Context context,List<User> datas){
        this.context = context;
        this.datas = datas;
    }
    @Override
    public AddAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.add_friend,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(AddAdapter.ViewHolder holder,  int position) {
        holder.textView.setText(datas.get(position).getUsername());
//        final User user = datas.get(position);
//        holder.add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context,AskFriendActivity.class);
//
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
     class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        private Button add;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.username);
            add = (Button) itemView.findViewById(R.id.add);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,AskFriendActivity.class);
                    intent.putExtra("adduser",datas.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}

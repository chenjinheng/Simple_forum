package com.example.libarary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.libarary.R;
import com.example.libarary.event.AgreeEvent;
import com.example.libarary.xinxibao.AskFriend;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.bmob.newim.bean.BmobIMUserInfo;

/**
 * Created by 陈金桁 on 2018/2/3.
 */

public class AddFriendAdapter extends RecyclerView.Adapter<AddFriendAdapter.AddFriendViewHolder>{
    private Context context;
    private List<BmobIMUserInfo> datas;

    public AddFriendAdapter(Context context, List<BmobIMUserInfo> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public AddFriendAdapter.AddFriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.new_friend,parent,false);
        AddFriendViewHolder holder = new AddFriendViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(AddFriendAdapter.AddFriendViewHolder holder, int position) {
        BmobIMUserInfo info = datas.get(position);
        holder.name.setText(info.getName());
        holder.content.setText("你好，很高兴认识你");
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
    class AddFriendViewHolder extends RecyclerView.ViewHolder {
        private TextView name,content;
        public AddFriendViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.newFriend_name);
            content = (TextView) itemView.findViewById(R.id.newFriend_content);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Logger.i("OnClick");
                    EventBus.getDefault().post(new AgreeEvent(datas.get(getAdapterPosition()),getAdapterPosition()));
                    Log.e("AddFriendAdapter",datas.get(getAdapterPosition()).getName());
                }
            });
        }
    }
    //  private List<BmobIMUserInfo> datas;
//    private Context context;
//
//    public AddFriendAdapter(Context context,List<BmobIMUserInfo> datas){
//        this.context = context;
//        this.datas = datas;
//        Log.e("AddFriendAdapter",datas.size() + "");
//    }
//    @Override
//    public AddFriendAdapter.AddFriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.friend_adapter1,parent,false);
//        return new AddFriendViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(AddFriendAdapter.AddFriendViewHolder holder, int position) {
//        BmobIMUserInfo newFriend = datas.get(position);
//        holder.Content.setText("很高兴认识你");
//        holder.Username.setText(newFriend.getName());
//    }
//
//    @Override
//    public int getItemCount() {
//
//        return datas.size();
//    }
//    class AddFriendViewHolder extends RecyclerView.ViewHolder {
//        TextView Username;
//        TextView Content;
//        public AddFriendViewHolder(View itemView) {
//            super(itemView);
//            Log.e("AddFriendAdapter",getItemCount() + "");
//            Username = (TextView) itemView.findViewById(R.id.newFriend);
//            Content = (TextView) itemView.findViewById(R.id.add_newFriend);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Logger.i("OnClick");
//                    EventBus.getDefault().post(new AgreeEvent(datas.get(getAdapterPosition()),getAdapterPosition()));
//                }
//            });
//        }
//    }
}

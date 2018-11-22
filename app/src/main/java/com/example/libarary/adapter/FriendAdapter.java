package com.example.libarary.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.libarary.R;
import com.example.libarary.dao.FriendDao;

import com.example.libarary.view.ChatActivity;
import com.example.libarary.xinxibao.User;

import org.w3c.dom.Text;

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.MessagesQueryListener;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by 陈金桁 on 2018/2/3.
 */

public class FriendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<User> datas;
    private Context context;
//    public static final int ADD_FRIEND = 1;
//    public static final int ITEM = 2;
    public FriendAdapter(List<User> datas,Context context){
        this.context = context;
        this.datas = datas;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (viewType == ADD_FRIEND) {
//            View view = LayoutInflater.from(context).inflate(R.layout.add_friend_adapter,parent,false);
//            return new AddFriendViewHolder(view,context);
//        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.friend_adapter, parent, false);
            return new ViewHolder(view);
//        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewHolder){
//            ((ViewHolder) holder).name.setText(datas.get(position - 1).getName());
//            ((ViewHolder) holder).info = datas.get(position - 1);
//            ((ViewHolder) holder).conversation = BmobIM.getInstance().
//                    startPrivateConversation(new BmobIMUserInfo(((ViewHolder) holder).info.getUserId()
//                            , ((ViewHolder) holder).info.getName(), ((ViewHolder) holder).info.getAvatar()), null);
//            ((ViewHolder) holder).conversation.queryMessages(null, 1, new MessagesQueryListener() {
//                @Override
//                public void done(List<BmobIMMessage> list, BmobException e) {
//
//                }
//            });
//            if(position == 2) {
                ((ViewHolder) holder).name.setText(datas.get(position).getUsername());

                ((ViewHolder) holder).info = datas.get(position).getUserInfo();
                ((ViewHolder) holder).conversation = BmobIM.getInstance().startPrivateConversation(new BmobIMUserInfo(((ViewHolder) holder).info.getUserId(),((ViewHolder) holder).info.getName(),((ViewHolder) holder).info.getAvatar()),null);
                ((ViewHolder) holder).conversation.queryMessages(null,1,new MessagesQueryListener(){

                    @Override
                    public void done(List<BmobIMMessage> list, BmobException e) {
                        if(e == null){
                            String s = "";
                            if(list.size() != 0){
                                 s = list.get(0).getContent();
                            }
                            if(s != null){
                                ((ViewHolder) holder).content.setText(s);
                            }
                        }
                    }
                });
            }
//        }
//        if(holder instanceof AddFriendViewHolder){
//            FriendDao frienddao = FriendDao.getInstance(null, null, null, 1);
//            SQLiteDatabase database = frienddao.getReadableDatabase();
//            Cursor cursor = database.rawQuery("select * from friend", null);
//            int count = cursor.getCount();
//            com.orhanobut.logger.Logger.i(count + "");
//            if (count == 0) {
//                ((AddFriendViewHolder) holder).friend.setText("没有新的好友添加");
//            }
//            cursor.close();
//            frienddao.close();
//        }
    }

//    @Override
//    public int getItemViewType(int position) {
//        if(position == 0){
//            return ADD_FRIEND;
//        }
//        else{
//            return ITEM;
//        }
//    }



    @Override
    public int getItemCount() {
        return datas.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {

        private BmobIMUserInfo info;
        private TextView content;
        private TextView name;
        BmobIMConversation conversation;

        public ViewHolder(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.add_newFriend1);
            name = (TextView) itemView.findViewById(R.id.newFriend1);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("z",conversation);
                    context.startActivity(intent);
                }
            });
        }
    }
}

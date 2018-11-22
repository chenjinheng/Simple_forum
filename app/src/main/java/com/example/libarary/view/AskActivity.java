package com.example.libarary.view;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.libarary.R;
import com.example.libarary.adapter.AddFriendAdapter;
import com.example.libarary.dao.FriendDao;
import com.example.libarary.event.AgreeEvent;
import com.example.libarary.utils.AgreeAddFriendMessage;
import com.example.libarary.xinxibao.AskFriend;
import com.example.libarary.xinxibao.Friend;
import com.example.libarary.xinxibao.User;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


public class AskActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<BmobIMUserInfo> datas = new ArrayList<>();
    private SQLiteDatabase database;
    private FriendDao friendDao;
    private Cursor cursor;
    private AddFriendAdapter addFriendAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);

        BmobQuery<AskFriend> query = new BmobQuery<>();
        query.findObjects(new FindListener<AskFriend>() {
            @Override
            public void done(List<AskFriend> list, BmobException e) {
                for(AskFriend data : list){
                    Log.e("askName","start");
                    if(data.getUser().getObjectId().equals(BmobUser.getCurrentUser(User.class).getObjectId())){
                        Log.e("askName",data.getInfo().getName());

                        datas.add(0,data.getInfo());
                        Log.e("askName",datas.size() + " 内部");
                    }
                }
                addFriendAdapter = new AddFriendAdapter(AskActivity.this,datas);
                recyclerView = (RecyclerView) findViewById(R.id.rv_addfriend);
                recyclerView.setLayoutManager(new LinearLayoutManager(AskActivity.this));
                recyclerView.setAdapter(addFriendAdapter);
                addFriendAdapter.notifyDataSetChanged();

            }
        });

        Log.e("askName",datas.size() + "");
        EventBus.getDefault().register(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
    @Subscribe
    public void onEventMainThread(final AgreeEvent event){
        final BmobIMUserInfo info = event.getInfo();
        Log.e("onEventMainThread",info.getName());
        Logger.i("发送同意消息");
        BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info,true,null);
        BmobIMConversation conversation = BmobIMConversation.obtain(BmobIMClient.getInstance(),c);
        AgreeAddFriendMessage message = new AgreeAddFriendMessage();
        conversation.sendMessage(message, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                if(e == null){
                    Friend friend = new Friend();
                    friend.setUser(BmobUser.getCurrentUser(User.class));
                    User newfriend = new User();
                    newfriend.setObjectId(info.getUserId());
                    friend.setFriendUser(newfriend);

                    friend.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if(e == null){
                                AskFriend askFriend = new AskFriend();
                                askFriend.setObjectId(MainView.id);
                                askFriend.delete(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if(e == null){
                                            Toast.makeText(AskActivity.this, "添加好友成功", Toast.LENGTH_SHORT).show();
                                            Log.e("askfriend","删除成功");
                                            addFriendAdapter.notifyDataSetChanged();
                                        }
                                        else{
                                            Log.e("askfriend",e.getMessage());
                                        }
                                    }
                                });
//                                datas.remove(event.getPositon());

//                                database.delete("friend", "objectid='" + event.getInfo().getUserId() + "'", null);
//                                database.execSQL("delete from friend where objectid ='" + event.getInfo().getUserId() + "'");

                            }
                            else{
                                Log.e("tianjia",e.getMessage());
                                Toast.makeText(AskActivity.this, "添加好友失败", Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
                }
                else{
                    Toast.makeText(AskActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

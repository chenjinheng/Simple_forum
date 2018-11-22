package com.example.libarary.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.libarary.R;
import com.example.libarary.utils.AddFriendMessage;
import com.example.libarary.xinxibao.Friend;
import com.example.libarary.xinxibao.User;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class AskFriendActivity extends AppCompatActivity {
    private Button add;
    private TextView userName;
    private Intent intent;
    private User user;
    BmobIMUserInfo info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_friend);
        init();
        initView();
    }
    private void init(){
        add = (Button) findViewById(R.id.add);
        userName = (TextView) findViewById(R.id.username);
        user = (User) getIntent().getSerializableExtra("adduser");
        info = user.getUserInfo();
    }
    private void initView(){
        userName.setText(user.getUsername());
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                  BmobQuery<Friend> query = new BmobQuery<>();
                query.addWhereEqualTo("user", BmobUser.getCurrentUser().getObjectId());
                query.addWhereEqualTo("friendUser",info.getUserId());
                query.findObjects(new FindListener<Friend>() {
                    @Override
                    public void done(List<Friend> list, BmobException e) {
                        if(e == null){
                            if(list.size() == 0){
                                sendAddFriendMessage();
                            }
                            else{
                                Toast.makeText(AskFriendActivity.this, "他已经是你的好友了", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Logger.i(e.getMessage());
                        }
                    }
                });
            }
        });
    }
    private void sendAddFriendMessage(){
        BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info,true,null);
        Logger.i(info.getName());
        BmobIMConversation conversation = BmobIMConversation.obtain(BmobIMClient.getInstance(),c);
        User currentUser = BmobUser.getCurrentUser(User.class);
        final AddFriendMessage msg = new AddFriendMessage();
        msg.setContent("很高兴认识你，可以加个好友吗?");//给对方的一个留言信息
        Map<String, Object> map = new HashMap<>();
        map.put("name", currentUser.getUsername());//发送者姓名，这里只是举个例子，其实可以不需要传发送者的信息过去
        map.put("avatar", currentUser.getAvatar());//发送者的头像
        map.put("uid", currentUser.getObjectId());//发送者的uid
        msg.setExtraMap(map);
        conversation.sendMessage(msg, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                if(e == null){
                    Log.e("test",msg.toString());
                    Toast.makeText(AskFriendActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                }
                else{
                    Logger.i(e.getMessage());
                }
            }
        });
    }
}

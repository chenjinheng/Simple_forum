package com.example.libarary.view;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.libarary.R;
import com.example.libarary.adapter.FriendAdapter;
import com.example.libarary.xinxibao.Friend;
import com.example.libarary.xinxibao.User;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class FriendActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private FriendAdapter friendAdapter;
    private List<User> datas;
    private BmobIMConversation c;
    private User myuser;
    private Button see;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        myuser = BmobUser.getCurrentUser(User.class);
        getFriend();
        initView();

    }
    private void initView(){
        see = (Button) findViewById(R.id.see_friend);
        see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FriendActivity.this,AskActivity.class);
                startActivity(intent);
            }
        });
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_friend);
        recyclerView = (RecyclerView) findViewById(R.id.rv_friend);
        toolbar.inflateMenu(R.menu.add);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.add){
                    Intent intent = new Intent(FriendActivity.this,AddActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

//        friendAdapter = new FriendAdapter(datas,this);
//       recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(friendAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFriend();
            }
        });
    }
    private void getFriend(){
//        BmobQuery<Friend> query = new BmobQuery<>("Friend");
//        query.addWhereEqualTo("user",myuser);
//        query.include("friendUser");
//        query.findObjects(new FindListener<Friend>() {
//            @Override
//            public void done(List<Friend> list, BmobException e) {
//                findFriends(list,e);
//            }
//        });
        datas = new ArrayList<>();
        BmobQuery<Friend> query = new BmobQuery<>();
        query.addWhereEqualTo("user",myuser);
        query.include("friendUser");
        query.findObjects(new FindListener<Friend>() {
            @Override
            public void done(List<Friend> list, BmobException e) {
                if(e == null){
                    for(Friend data : list){
                        datas.add(data.getFriendUser());
                        Log.e("datas",data.getFriendUser().getUsername());
                    }
                    friendAdapter = new FriendAdapter(datas,FriendActivity.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(FriendActivity.this));
                    recyclerView.setAdapter(friendAdapter);
                    friendAdapter.notifyDataSetChanged();
                }
                else{
                    Log.e("aaa",e.getMessage());
                }
            }

        });

    }
    private void findFriends(List<Friend> list,BmobException e){
//        if(e == null){
//            datas.clear();
//            for(Friend friend : list){
//                BmobIMUserInfo info = new BmobIMUserInfo(friend.getFriendUser().getObjectId(), friend.getFriendUser()
//                        .getUsername(), friend.getFriendUser().getAvatar());
//                BmobIM.getInstance().updateUserInfo(info);
//                datas.add(info);
//            }
//            friendAdapter.notifyDataSetChanged();
//            swipeRefreshLayout.setRefreshing(false);
//        }
//        else{
//            Logger.i(e.getMessage());
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        getFriend();
    }
}

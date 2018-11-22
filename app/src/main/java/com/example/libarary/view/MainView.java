package com.example.libarary.view;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.libarary.R;
import com.example.libarary.dao.FriendDao;
import com.example.libarary.event.RefreshEvent;
import com.example.libarary.fragment.MainFragment;
import com.example.libarary.xinxibao.AddFriendEvent;
import com.example.libarary.xinxibao.AskFriend;
import com.example.libarary.xinxibao.User;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class MainView extends AppCompatActivity {
    private User user;
    private MainFragment fragment = new MainFragment();
    private FriendDao frienddao;
    private SQLiteDatabase database;
    private Cursor cursor;
    public static String id = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);

        fragment.setRetainInstance(true);
        user = BmobUser.getCurrentUser(User.class);
        EventBus.getDefault().register(this);
        getSupportFragmentManager().beginTransaction().add(R.id.title,fragment).commit();
        if(user != null){
            Toast.makeText(this, "欢迎您 " + BmobUser.getCurrentUser(User.class).getUsername(), Toast.LENGTH_SHORT).show();
            BmobIM.getInstance().updateUserInfo(user.getUserInfo());
            BmobIM.connect(user.getObjectId(), new ConnectListener() {
                @Override
                public void done(String s, BmobException e) {
                    if(e == null){
                        Toast.makeText(MainView.this, "连接成功", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(MainView.this, "连接失败，检查网络", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            frienddao = FriendDao.getInstance(this, "MyFriend.db", null, 1);
        }

//        if(!TextUtils.isEmpty(user.getObjectId())){
//            BmobIM.connect(user.getObjectId(), new ConnectListener() {
//                @Override
//                public void done(String s, BmobException e) {
//                    if (e == null) {
//                        EventBus.getDefault().post(new RefreshEvent());
//                        BmobIM.getInstance().updateUserInfo(new BmobIMUserInfo(user.getObjectId(),user.getUsername(),user.getAvatar()));
//                        Toast.makeText(MainView.this, "连接成功", Toast.LENGTH_SHORT).show();
//                    }
//                    else{
//                        Logger.i(e.getMessage());
//                    }
//                }
//
//            });
////            BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
////                @Override
////                public void onChange(ConnectionStatus connectionStatus) {
////                    Toast.makeText(MainView.this,connectionStatus.getMsg() , Toast.LENGTH_SHORT).show();
////                }
////            });
//        }

    }
    @Subscribe
    public synchronized void onEventMainThread(AddFriendEvent event) {
        String msg = "收到了消息：";
        Logger.i(msg);
//        SQLiteDatabase database = frienddao.getWritableDatabase();
        BmobIMUserInfo info = event.getInfo();
        Logger.i(info.getName());
//        cursor = database.rawQuery("select * from friend",null);
//        while(cursor.moveToNext()){
//            if (cursor.getString(0).equals(event.getInfo().getUserId())) {
//                return;
//            }
//        }
//        String sql = "insert into friend values('" + info.getUserId() + "','" + info.getName() + "','" + info.getAvatar() + "')";
//        database.execSQL(sql);
        AskFriend askFriend = new AskFriend();
        askFriend.setUser(BmobUser.getCurrentUser(User.class));
        askFriend.setInfo(info);
        askFriend.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e == null){
                    id = s;
                    Logger.i("添加关系成功");
                }
                else{
                    Logger.i(e.getMessage());
                }
            }
        });
//        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
//        datas.add(event.getInfo());
//        adapter.notifyDataSetChanged();
//        IsAggreed(event);
    }
    @Override
    protected void onResume() {
        super.onResume();

    }
//    private void checkRedPoint(){
//        int count = (int) BmobIM.getInstance().getAllUnReadCount();
//        if(count > 0){
//
//        }
//    }
}

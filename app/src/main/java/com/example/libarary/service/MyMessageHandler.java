package com.example.libarary.service;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.libarary.R;
import com.example.libarary.utils.AgreeAddFriendMessage;
import com.example.libarary.utils.UserModel;
import com.example.libarary.view.MainView;
import com.example.libarary.xinxibao.AddFriendEvent;
import com.example.libarary.xinxibao.Friend;
import com.example.libarary.xinxibao.NewFriend;
import com.example.libarary.xinxibao.User;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;

import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by 陈金桁 on 2018/2/4.
 */

public class MyMessageHandler extends BmobIMMessageHandler {
    private Context context;

    public MyMessageHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onMessageReceive(MessageEvent messageEvent) {
        super.onMessageReceive(messageEvent);
//    当接受到服务器来的消息的时候，这个方法会调用
        Log.e("test",messageEvent.getMessage().toString());
//        Logger.i(messageEvent.getConversation().getConversationTitle() + "," +
//                messageEvent.getMessage().getMsgType() + "," + messageEvent.getMessage().getContent());
        excuteMessage(messageEvent);

    }


    @Override
    public void onOfflineReceive(OfflineMessageEvent offlineMessageEvent) {
        super.onOfflineReceive(offlineMessageEvent);
        Map<String, List<MessageEvent>> map = offlineMessageEvent.getEventMap();
        Logger.i("离线消息属于" + map.size() + "个用户");
        //挨个检测下离线消息所属的用户的信息是否需要更新
        for (Map.Entry<String, List<MessageEvent>> entry : map.entrySet()) {
            List<MessageEvent> list = entry.getValue();
            int size = list.size();
            for (int i = 0; i < size; i++) {
                excuteMessage(list.get(i));
            }
        }
    }

    /**
     * 处理消息
     *
     * @param event
     */
    private void excuteMessage(final MessageEvent event) {
        UserModel.getInstance().updateUserInfo(event, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                Logger.i("Done");
                BmobIMMessage msg = event.getMessage();
                if (BmobIMMessageType.getMessageTypeValue(msg.getMsgType()) == 0) {//用户自定义的消息类型，其类型值均为0
                    processCustomMessage(msg, event.getFromUserInfo());
                } else {//SDK内部内部支持的消息类型
                    if (BmobNotificationManager.getInstance(context).isShowNotification()) {//如果需要显示通知栏，SDK提供以下两种显示方式：
                        Intent pendingIntent = new Intent(context, MainView.class);
                        pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        //1、多个用户的多条消息合并成一条通知：有XX个联系人发来了XX条消息
                        BmobNotificationManager.getInstance(context).showNotification(event, pendingIntent);
                        Logger.i("Notification");
                        //2、自定义通知消息：始终只有一条通知，新消息覆盖旧消息
//                        BmobIMUserInfo info =event.getFromUserInfo();
//                        //这里可以是应用图标，也可以将聊天头像转成bitmap
//                        Bitmap largetIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
//                        BmobNotificationManager.getInstance(context).showNotification(largetIcon,
//                                info.getName(),msg.getContent(),"您有一条新消息",pendingIntent);
                    } else {//直接发送消息事件
                        Logger.i("当前处于应用内，发送event");
                        EventBus.getDefault().post(event);
                    }
                }
            }
        });
    }
    private void processSDKMessage(BmobIMMessage msg, MessageEvent event) {
        if (BmobNotificationManager.getInstance(context).isShowNotification()) {
            //如果需要显示通知栏，SDK提供以下两种显示方式：
            Intent pendingIntent = new Intent(context, MainView.class);
            pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);


            //TODO 消息接收：8.5、多个用户的多条消息合并成一条通知：有XX个联系人发来了XX条消息
            //BmobNotificationManager.getInstance(context).showNotification(event, pendingIntent);

            //TODO 消息接收：8.6、自定义通知消息：始终只有一条通知，新消息覆盖旧消息
            BmobIMUserInfo info = event.getFromUserInfo();
            //这里可以是应用图标，也可以将聊天头像转成bitmap
            Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
            BmobNotificationManager.getInstance(context).showNotification(largeIcon,
                    info.getName(), msg.getContent(), "您有一条新消息", pendingIntent);
        } else {
            //直接发送消息事件
            EventBus.getDefault().post(event);
        }
    }


    /**
     * 处理自定义消息类型
     *
     * @param msg
     */
    private void processCustomMessage(BmobIMMessage msg, final BmobIMUserInfo info) {
        //自行处理自定义消息类型
        Logger.i("收到了自定义消息");
        String type = msg.getMsgType();
        Logger.i(msg.getMsgType() + "," + msg.getContent() + "," + msg.getExtra());
//        //发送页面刷新的广播
        EventBus.getDefault().post(new AddFriendEvent(msg, info));
//        //处理消息
//        if(type.equals("add")){//接收到的添加好友的请求
//            NewFriend friend = AddFriendMessage.convert(msg);
        //本地好友请求表做下校验，本地没有的才允许显示通知栏--有可能离线消息会有些重复
//            long id = NewFriendManager.getInstance(context).insertOrUpdateNewFriend(friend);
//            if(id>0){
//                showAddNotify(friend);
//            }
//        }
        if (type.equals("agree")) {//接收到的对方同意添加自己为好友,此时需要做的事情：1、添加对方为好友，2、显示通知
            BmobQuery<Friend> query = new BmobQuery<>();
            query.addWhereEqualTo("user", BmobUser.getCurrentUser().getObjectId());
            query.addWhereEqualTo("friendUser", info.getUserId());
            query.findObjects(new FindListener<Friend>() {
                @Override
                public void done(List<Friend> list, BmobException e) {
                    if (e == null) {
                        if (list.size() == 0) {
                            Friend friend = new Friend();
                            friend.setUser(BmobUser.getCurrentUser(User.class));
                            User user = new User();
                            user.setObjectId(info.getUserId());
                            friend.setFriendUser(user);
                            friend.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if (e == null) {
                                        Logger.i("添加好友成功");
                                    }
                                }
                            });
                        }
                    }
                }
            });

//            AgreeAddFriendMessage agree = AgreeAddFriendMessage.convert(msg);
//            addFriend(agree.getFromId());//添加消息的发送方为好友
//            //这里应该也需要做下校验--来检测下是否已经同意过该好友请求，我这里省略了
//            showAgreeNotify(info,agree);
//        }else{
//            Toast.makeText(context,"接收到的自定义消息："+msg.getMsgType() + "," + msg.getContent() + "," + msg.getExtra(),Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 显示对方添加自己为好友的通知
     *
     * @param friend
     */
    private void showAddNotify(NewFriend friend) {
//        Intent pendingIntent = new Intent(context, MainActivity.class);
//        pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        //这里可以是应用图标，也可以将聊天头像转成bitmap
//        Bitmap largetIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
//        BmobNotificationManager.getInstance(context).showNotification(largetIcon,
//                friend.getName(), friend.getMsg(), friend.getName() + "请求添加你为朋友", pendingIntent);
    }

    /**
     * 显示对方同意添加自己为好友的通知
     *
     * @param info
     * @param agree
     */
    private void showAgreeNotify(BmobIMUserInfo info, AgreeAddFriendMessage agree) {
//        Intent pendingIntent = new Intent(context, MainActivity.class);
//        pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        Bitmap largetIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
//        BmobNotificationManager.getInstance(context).showNotification(largetIcon,info.getName(),agree.getMsg(),agree.getMsg(),pendingIntent);
    }

    /**
     * 添加对方为自己的好友
     *
     * @param uid
     */
    private void addFriend(String uid) {
//        User user =new User();
//        user.setObjectId(uid);
//        UserModel.getInstance().agreeAddFriend(user, new SaveListener() {
//            @Override
//            public void onSuccess() {
//                Log.i("bmob", "onSuccess");
//            }
//
//            @Override
//            public void onFailure(int i, String s) {
//                Log.i("bmob", "onFailure:"+s+"-"+i);
//            }
//        });
    }

}

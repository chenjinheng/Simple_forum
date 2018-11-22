package com.example.libarary.view;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.libarary.R;
import com.example.libarary.adapter.ChatAdapter;
import com.example.libarary.utils.Util;
import com.example.libarary.xinxibao.User;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.BindString;
import cn.bmob.newim.bean.BmobIMAudioMessage;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.core.BmobRecordManager;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.newim.listener.MessagesQueryListener;
import cn.bmob.newim.listener.ObseverListener;
import cn.bmob.newim.listener.OnRecordChangeListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

public class ChatActivity extends AppCompatActivity implements ObseverListener {
    private BmobIMConversation c;
    private EditText edit_msg;
    private ChatAdapter adapter;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private Button btn_chat_send;
    private Button btn_chat_keyboard;
    private Button btn_chat_voice;
    private User user;
    private SwipeRefreshLayout refresh;
    private Drawable[] drawable_Anims;// 话筒动画
    private Button btn_speak;
    BmobRecordManager recordManager;
    ImageView iv_record;
    private RelativeLayout layout_record;
    private TextView tv_voice_tips;
    private LinearLayout layout_more;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();
        initButton();
        initVoiceView();
        EventBus.getDefault().register(this);
    }
    private void initVoiceView(){
        btn_speak.setOnTouchListener(new VoiceTouchListener());
        initVoiceAnimRes();
        initRecordManager();
    }
    private void initVoiceAnimRes() {
        drawable_Anims = new Drawable[] {
                getResources().getDrawable(R.mipmap.chat_icon_voice2),
                getResources().getDrawable(R.mipmap.chat_icon_voice3),
                getResources().getDrawable(R.mipmap.chat_icon_voice4),
                getResources().getDrawable(R.mipmap.chat_icon_voice5),
                getResources().getDrawable(R.mipmap.chat_icon_voice6) };
    }
    /**
     * 长按说话
     * @author smile
     * @date 2014-7-1 下午6:10:16
     */
    private void initRecordManager(){
        // 语音相关管理器
        recordManager = BmobRecordManager.getInstance(this);
        // 设置音量大小监听--在这里开发者可以自己实现：当剩余10秒情况下的给用户的提示，类似微信的语音那样
        recordManager.setOnRecordChangeListener(new OnRecordChangeListener() {

            @Override
            public void onVolumnChanged(int value) {
                iv_record.setImageDrawable(drawable_Anims[value]);
            }

            @Override
            public void onTimeChanged(int recordTime, String localPath) {
                Logger.i("voice", "已录音长度:" + recordTime);
                if (recordTime >= BmobRecordManager.MAX_RECORD_TIME) {// 1分钟结束，发送消息
                    // 需要重置按钮
                    btn_speak.setPressed(false);
                    btn_speak.setClickable(false);
                    // 取消录音框
                    layout_record.setVisibility(View.INVISIBLE);
                    // 发送语音消息
                    sendVoiceMessage(localPath, recordTime);
                    //是为了防止过了录音时间后，会多发一条语音出去的情况。
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            btn_speak.setClickable(true);
                        }
                    }, 1000);
                }
            }
        });
    }
    class VoiceTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!Util.checkSdCard()) {
                        Toast.makeText(ChatActivity.this, "发送语音消息需要sdcard支持", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    try {
                        v.setPressed(true);
                        layout_record.setVisibility(View.VISIBLE);
                        tv_voice_tips.setText(getString(R.string.voice_cancel_tips));
                        // 开始录音
                        recordManager.startRecording(c.getConversationId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                case MotionEvent.ACTION_MOVE: {
                    if (event.getY() < 0) {
                        tv_voice_tips.setText(getString(R.string.voice_cancel_tips));
                        tv_voice_tips.setTextColor(Color.RED);
                    } else {
                        tv_voice_tips.setText(getString(R.string.voice_up_tips));
                        tv_voice_tips.setTextColor(Color.WHITE);
                    }
                    return true;
                }
                case MotionEvent.ACTION_UP:
                    v.setPressed(false);
                    layout_record.setVisibility(View.INVISIBLE);
                    try {
                        if (event.getY() < 0) {// 放弃录音
                            recordManager.cancelRecording();
                            Logger.i("voice", "放弃发送语音");
                        } else {
                            int recordTime = recordManager.stopRecording();
                            if (recordTime > 1) {
                                // 发送语音文件
                                sendVoiceMessage(recordManager.getRecordFilePath(c.getConversationId()),recordTime);
                            } else {// 录音时间过短，则提示录音过短的提示
                                layout_record.setVisibility(View.GONE);
                                showShortToast().show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                default:
                    return false;
            }
        }
    }
    Toast toast;

    /**
     * 显示录音时间过短的Toast
     * @Title: showShortToast
     * @return void
     */
    private Toast showShortToast() {
        if (toast == null) {
            toast = new Toast(this);
        }
        View view = LayoutInflater.from(this).inflate(
                R.layout.include_chat_voice_short, null);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        return toast;
    }
    /**
     * 发送语音消息
     * @Title: sendVoiceMessage
     * @param  local
     * @param  length
     * @return void
     */
    private void sendVoiceMessage(String local, int length) {
        Logger.i("发送语音消息");
        BmobIMAudioMessage audio =new BmobIMAudioMessage(local);
        c.sendMessage(audio, listener);
    }
    private void initView(){
        adapter = new ChatAdapter(this,c);
        btn_chat_voice = (Button) findViewById(R.id.btn_chat_voice);
        btn_chat_keyboard = (Button) findViewById(R.id.btn_chat_keyboard);
        btn_chat_send = (Button) findViewById(R.id.btn_chat_send);
        refresh = (SwipeRefreshLayout) findViewById(R.id.sw_refresh);
        c = BmobIMConversation.obtain(BmobIMClient.getInstance(), (BmobIMConversation) getIntent().getSerializableExtra("z"));
        if(!c.isTransient()){
            queryMessages(null);
            refresh = (SwipeRefreshLayout) findViewById(R.id.sw_refresh);
            refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    BmobIMMessage msg = adapter.getFirstMessage();
                    queryMessages(msg);
                }
            });
        }
        edit_msg = (EditText) findViewById(R.id.edit_msg);
        recyclerView = (RecyclerView) findViewById(R.id.rc_view);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        user = BmobUser.getCurrentUser(User.class);
        layout_more = (LinearLayout) findViewById(R.id.layout_more);
        btn_chat_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
                hideSoftInputView();
            }
        });
        btn_speak = (Button) findViewById(R.id.btn_speak);
        iv_record = (ImageView) findViewById(R.id.iv_record);
        layout_record = (RelativeLayout) findViewById(R.id.layout_record);
        tv_voice_tips = (TextView) findViewById(R.id.tv_voice_tips);
        btn_chat_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_msg.setVisibility(View.GONE);
                layout_more.setVisibility(View.GONE);
                btn_chat_voice.setVisibility(View.GONE);
                btn_chat_keyboard.setVisibility(View.VISIBLE);
                btn_speak.setVisibility(View.VISIBLE);
                hideSoftInputView();
            }
        });
        btn_chat_keyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_msg.setVisibility(View.VISIBLE);
                layout_more.setVisibility(View.VISIBLE);
                btn_chat_voice.setVisibility(View.VISIBLE);
                btn_chat_keyboard.setVisibility(View.GONE);
                btn_speak.setVisibility(View.GONE);
            }
        });
    }
    /**
     * 隐藏软键盘
     */
    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    public void initButton(){
        edit_msg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN || motionEvent.getAction() == MotionEvent.ACTION_UP){
                    scrollToBottom();
                }
                return false;
            }
        });
        edit_msg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                scrollToBottom();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence)){
                    btn_chat_send.setVisibility(View.VISIBLE);
                    btn_chat_keyboard.setVisibility(View.GONE);
                    btn_chat_voice.setVisibility(View.GONE);
                }
                else{
                    if(btn_chat_voice.getVisibility() != View.VISIBLE){
                        btn_chat_voice.setVisibility(View.VISIBLE);
                        btn_chat_send.setVisibility(View.GONE);
                        btn_chat_keyboard.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void queryMessages(BmobIMMessage msg){
        c.queryMessages(msg, 10, new MessagesQueryListener() {
            @Override
            public void done(List<BmobIMMessage> list, BmobException e) {
                refresh.setRefreshing(false);
                if(e == null){
                    if(null != list && list.size() > 0){
                        adapter.addMessages(list);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }
    private void scrollToBottom(){
        layoutManager.scrollToPositionWithOffset(adapter.getItemCount() - 1,0);
    }
    /**
     * 发送文本消息
     */
    private void sendMessage() {
        String text = edit_msg.getText().toString();
        if (TextUtils.isEmpty(text.trim())) {
            Toast.makeText(ChatActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
            return;
        }
        BmobIMTextMessage msg = new BmobIMTextMessage();
        msg.setContent(text);
        msg.setFromId(user.getObjectId());
        //可设置额外信息
        Map<String, Object> map = new HashMap<>();
        map.put("level", "1");//随意增加信息
        msg.setExtraMap(map);
        c.sendMessage(msg, listener);
    }
    public MessageSendListener listener = new MessageSendListener() {

        @Override
        public void onProgress(int value) {
            super.onProgress(value);
            //文件类型的消息才有进度值
            Logger.i("onProgress：" + value);
        }

        @Override
        public void onStart(BmobIMMessage msg) {
            super.onStart(msg);
            adapter.addMessage(msg);
            edit_msg.setText("");
//            scrollToBottom();
        }

        @Override
        public void done(BmobIMMessage msg, BmobException e) {
            adapter.notifyDataSetChanged();
            edit_msg.setText("");
//            scrollToBottom();
//            Logger.i("发送成功");
            if (e != null) {
                Toast.makeText(ChatActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    };
    /**
     * 使用EventBus获取信息
     */
    @Subscribe
    public void onEventMainThread(MessageEvent event) {
        String msg = "收到了消息：";
        addMessage2Chat(event);
        Log.e("event",event.getMessage().toString());
        Logger.i(msg);
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
    /**
     * 添加消息到聊天界面中
     *
     * @param event
     */
    private void addMessage2Chat(MessageEvent event) {
        BmobIMMessage msg = event.getMessage();
        if (c != null && event != null && c.getConversationId().equals(event.getConversation().getConversationId()) //如果是当前会话的消息
                && !msg.isTransient()) {//并且不为暂态消息
            adapter.addMessage(msg);
//                更新该会话下面的已读状态
            Log.i("IMGet", event.getMessage().getContent());
            Log.i("IMGet", "Success");
            c.updateReceiveStatus(msg);
//            }
//            scrollToBottom();
        } else {
            Log.i("IM", "不是与当前聊天对象的消息");
        }
    }
    @Override
    protected void onResume() {
        //锁屏期间的收到的未读消息需要添加到聊天界面中
//        addUnReadMessage();
//        添加页面消息监听器
//        BmobIM.getInstance().addMessageListHandler(this);
        // 有可能锁屏期间，在聊天界面出现通知栏，这时候需要清除通知
        BmobNotificationManager.getInstance(this).cancelNotification();
        super.onResume();
    }

    @Override
    protected void onPause() {
        //移除页面消息监听器
//        BmobIM.getInstance().removeMessageListHandler(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //清理资源
//        if(recordManager!=null){
//            recordManager.clear();
//        }
//        //更新此会话的所有消息为已读状态
//        if(c!=null){
//            c.updateLocalCache();
//        }
//        hideSoftInputView();
        super.onDestroy();
    }

}

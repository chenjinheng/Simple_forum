package com.example.libarary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.libarary.R;
import com.example.libarary.holder.ReceiveTextViewHolder;
import com.example.libarary.holder.ReceiveVoiceHolder;
import com.example.libarary.holder.SendVoiceViewHolder;
import com.example.libarary.utils.NewRecordPlayClickListener;
import com.example.libarary.xinxibao.User;
import com.facebook.drawee.view.SimpleDraweeView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMAudioMessage;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;
import cn.bmob.newim.core.BmobDownloadManager;
import cn.bmob.newim.listener.FileDownloadListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by 陈金桁 on 2018/2/20.
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    //文本
    private final int TYPE_RECEIVER_TXT = 0;
    private final int TYPE_SEND_TXT = 1;
    //图片
    private final int TYPE_SEND_IMAGE = 2;
    private final int TYPE_RECEIVER_IMAGE = 3;
    //位置
    private final int TYPE_SEND_LOCATION = 4;
    private final int TYPE_RECEIVER_LOCATION = 5;
    //语音
    private final int TYPE_SEND_VOICE = 6;
    private final int TYPE_RECEIVER_VOICE = 7;
    //视频
    private final int TYPE_SEND_VIDEO = 8;
    private final int TYPE_RECEIVER_VIDEO = 9;

    //同意添加好友成功后的样式
    private final int TYPE_AGREE = 10;

    /**
     * 显示时间间隔:10分钟
     */
    private final long TIME_INTERVAL = 10 * 60 * 1000;
    private List<BmobIMMessage> msgs = new ArrayList<>();
    private String currentUid = "";
    BmobIMConversation c;
    private Context context;
    public ChatAdapter(Context context,BmobIMConversation c){
        try {
            this.context = context;
            currentUid = BmobUser.getCurrentUser(User.class).getObjectId();
            inflater = LayoutInflater.from(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.c = c;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_SEND_TXT) {
            View view = inflater.inflate(R.layout.item_chat_send_text, parent, false);
            return new SendTextHolder(view);
        }
        if (viewType == TYPE_RECEIVER_TXT) {
            View view = inflater.inflate(R.layout.item_chat_receive_text, parent, false);
            return new ReceiveTextViewHolder(view);
        } else if (viewType == TYPE_RECEIVER_VOICE) {
            View view = inflater.inflate(R.layout.item_chat_receive_voice, parent, false);
            return new ReceiveVoiceHolder(view);
        } else if (viewType == TYPE_SEND_VOICE) {
            View view = inflater.inflate(R.layout.item_chat_send_voice, parent, false);
            return new SendVoiceViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_chat_receive_text, parent, false);
            return new ReceiveTextViewHolder(view);
        }
}

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof SendTextHolder){
            ((SendTextHolder) holder).draweeView.setImageURI(BmobUser.getCurrentUser(User.class).getAvatar());
            ((SendTextHolder) holder).textView.setText(msgs.get(position).getContent());
        }
        else if(holder instanceof ReceiveTextViewHolder){
            ((ReceiveTextViewHolder) holder).textView.setText(msgs.get(position).getContent());
            ((ReceiveTextViewHolder) holder).simpleDraweeView.setImageURI(BmobIM.getInstance()
                    .getUserInfo(msgs.get(position).getFromId()).getAvatar());
        }else if (holder instanceof SendVoiceViewHolder) {
            BmobIMMessage msg = msgs.get(position);
            final BmobIMAudioMessage message = BmobIMAudioMessage.buildFromDB(true, msg);
            ((SendVoiceViewHolder) holder).simpleDraweeView.setImageURI(BmobUser.getCurrentUser(User.class).getAvatar());
            ((SendVoiceViewHolder) holder).imageView.setOnClickListener
                    (new NewRecordPlayClickListener(context, message
                            , ((SendVoiceViewHolder) holder).imageView));
        } else if (holder instanceof ReceiveVoiceHolder) {
            BmobIMMessage msg = msgs.get(position);
            final BmobIMAudioMessage message = BmobIMAudioMessage.buildFromDB(true, msg);
            ((ReceiveVoiceHolder) holder).simpleDraweeView.setImageURI(BmobIM.getInstance()
                    .getUserInfo(msg.getFromId()).getAvatar());
            boolean isExists = BmobDownloadManager.isAudioExist(currentUid, message);
            Logger.i(!isExists + "");
            if (!isExists) {//若指定格式的录音文件不存在，则需要下载，因为其文件比较小，故放在此下载
                BmobDownloadManager downloadTask = new BmobDownloadManager(context, msg, new FileDownloadListener() {
                    @Override
                    public void onStart() {
                        Logger.i("开始下载");
//                        progress_load.setVisibility(View.VISIBLE);
//                        tv_voice_length.setVisibility(View.GONE);
//                        iv_voice.setVisibility(View.INVISIBLE);//只有下载完成才显示播放的按钮
                    }

                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Logger.i("下载完成");
//                            progress_load.setVisibility(View.GONE);
//                            tv_voice_length.setVisibility(View.VISIBLE);
//                            tv_voice_length.setText(message.getDuration()+"\''");
//                            iv_voice.setVisibility(View.VISIBLE);
                        } else {
                            Logger.i("发生了异常");
//                            progress_load.setVisibility(View.GONE);
//                            tv_voice_length.setVisibility(View.GONE);
//                            iv_voice.setVisibility(View.INVISIBLE);
                        }
                    }
                });
                downloadTask.execute(message.getContent());
            }
            ((ReceiveVoiceHolder) holder).imageView.setOnClickListener(
                    new NewRecordPlayClickListener(context, message
                            , ((ReceiveVoiceHolder) holder).imageView)
            );
        }
    }

    @Override
    public int getItemCount() {
        return msgs.size();
    }

    @Override
    public int getItemViewType(int position) {
        BmobIMMessage message = msgs.get(position);
        if(message.getMsgType().equals(BmobIMMessageType.IMAGE.getType())){
            return message.getFromId().equals(currentUid)? TYPE_SEND_IMAGE : TYPE_RECEIVER_IMAGE;
        }else if (message.getMsgType().equals(BmobIMMessageType.LOCATION.getType())) {
            return message.getFromId().equals(currentUid) ? TYPE_SEND_LOCATION : TYPE_RECEIVER_LOCATION;
        } else if (message.getMsgType().equals(BmobIMMessageType.VOICE.getType())) {
            return message.getFromId().equals(currentUid) ? TYPE_SEND_VOICE : TYPE_RECEIVER_VOICE;
        } else if (message.getMsgType().equals(BmobIMMessageType.TEXT.getType())) {
            return message.getFromId().equals(currentUid) ? TYPE_SEND_TXT : TYPE_RECEIVER_TXT;
        } else if (message.getMsgType().equals(BmobIMMessageType.VIDEO.getType())) {
            return message.getFromId().equals(currentUid) ? TYPE_SEND_VIDEO : TYPE_RECEIVER_VIDEO;
        } else if (message.getMsgType().equals("agree")) {//显示欢迎
            return TYPE_AGREE;
        } else {
            return -1;
        }
    }
    class SendTextHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private SimpleDraweeView draweeView;
        public SendTextHolder(View itemView) {
            super(itemView);
            draweeView = (SimpleDraweeView) itemView.findViewById(R.id.sdv_head);
            textView = (TextView) itemView.findViewById(R.id.tv_message);
        }
    }
    public void addMessages(List<BmobIMMessage> messages) {
        msgs.addAll(0, messages);
        notifyDataSetChanged();
    }
    public void addMessage(BmobIMMessage message) {
//        msgs.addAll(Arrays.asList(message));
        msgs.add(message);
//        com.orhanobut.logger.Logger.i("datachange");
        notifyDataSetChanged();
//        com.orhanobut.logger.Logger.i(msgs.size() + "");
    }
    /**
     * 获取消息
     *
     * @param position
     * @return
     */
    public BmobIMMessage getItem(int position) {
        return this.msgs == null ? null : (position >= this.msgs.size() ? null : this.msgs.get(position));
    }

    /**
     * 移除消息
     *
     * @param position
     */
    public void remove(int position) {
        msgs.remove(position);
        notifyDataSetChanged();
    }

    public BmobIMMessage getFirstMessage() {
        if (null != msgs && msgs.size() > 0) {
            return msgs.get(0);
        } else {
            return null;
        }
    }
}

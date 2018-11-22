package com.example.libarary.xinxibao;

import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;

/**
 * Created by 陈金桁 on 2018/2/4.
 */

public class AddFriendEvent {
    private BmobIMMessage message;
    private BmobIMUserInfo info;

    public AddFriendEvent(BmobIMMessage message, BmobIMUserInfo info) {
        this.info = info;
        this.message = message;
    }

    public BmobIMUserInfo getInfo() {
        return info;
    }

    public void setInfo(BmobIMUserInfo info) {
        this.info = info;
    }

    public BmobIMMessage getMessage() {
        return message;
    }

    public void setMessage(BmobIMMessage message) {
        this.message = message;
    }
}

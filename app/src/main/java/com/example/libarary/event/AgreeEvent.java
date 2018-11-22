package com.example.libarary.event;

import cn.bmob.newim.bean.BmobIMUserInfo;

/**
 * Created by 陈金桁 on 2018/1/31.
 */

public class AgreeEvent {
    private BmobIMUserInfo info;
    private int postion;
    public AgreeEvent(BmobIMUserInfo info,int postion) {
        this.info = info;
        this.postion = postion;
    }

    public BmobIMUserInfo getInfo() {
        return info;
    }

    public void setInfo(BmobIMUserInfo info) {
        this.info = info;
    }

    public int getPositon(){return postion;}
}

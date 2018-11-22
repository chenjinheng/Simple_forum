package com.example.libarary.xinxibao;

import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

/**
 * Created by 陈金桁 on 2018/1/14.
 */

public class User extends BmobUser{
    private String avatar;

    public String getAvatar() {
        return avatar;
    }
    public BmobIMUserInfo getUserInfo(){
        return new BmobIMUserInfo(getObjectId(),getUsername(),getAvatar());
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}

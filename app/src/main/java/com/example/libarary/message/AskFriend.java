package com.example.libarary.xinxibao;

import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

/**
 * Created by 陈金桁 on 2018/2/5.
 */

public class AskFriend extends BmobObject {
    private User user;
    private BmobIMUserInfo info;
    private User newUser;
    private String newName;

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BmobIMUserInfo getInfo() {
        return info;
    }

    public void setInfo(BmobIMUserInfo info) {
        this.info = info;
    }

    public User getNewUser() {
        return newUser;
    }

    public void setNewUser(User newUser) {
        this.newUser = newUser;
    }
}

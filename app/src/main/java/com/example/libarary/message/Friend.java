package com.example.libarary.xinxibao;


import cn.bmob.v3.BmobObject;

/**
 * Created by 陈金桁 on 2018/1/30.
 */

public class Friend extends BmobObject{
    private User user;
    private User friendUser;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getFriendUser() {
        return friendUser;
    }

    public void setFriendUser(User friendUser) {
        this.friendUser = friendUser;
    }
}

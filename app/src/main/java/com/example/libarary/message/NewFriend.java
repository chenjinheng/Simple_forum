package com.example.libarary.xinxibao;

import java.io.Serializable;

/**
 * Created by 陈金桁 on 2018/1/30.
 */

public class NewFriend implements Serializable {
    private Long id;
    //用户uid
    private String uid;
    //留言消息
    private String msg;
    //用户名
    private String name;
    //头像
    private String avatar;
    //状态：未读、已读、已添加、已拒绝等
    private Integer status;
    //请求时间
    private Long time;

    public NewFriend() {
    }

    public NewFriend(Long id) {
        this.id = id;
    }

    public NewFriend(Long id, String uid, String msg, String name, String avatar, Integer status, Long time) {
        this.id = id;
        this.uid = uid;
        this.msg = msg;
        this.name = name;
        this.avatar = avatar;
        this.status = status;
        this.time = time;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

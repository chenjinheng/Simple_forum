package com.example.libarary.xinxibao;

import cn.bmob.v3.BmobObject;

/**
 * Created by 陈金桁 on 2018/1/15.
 */

public class Comment extends BmobObject {
    private String content;
    private User user;
    private Post post;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

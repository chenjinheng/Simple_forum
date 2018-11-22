package com.example.libarary.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.libarary.R;
import com.example.libarary.xinxibao.Comment;
import com.example.libarary.xinxibao.Post;
import com.example.libarary.xinxibao.User;

import org.w3c.dom.Text;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class DiscussActivity extends AppCompatActivity {
    private String mTitle,mContent,mId;
    private TextView title,content;
    private EditText discuss;
    private Button finish;
    private User user;
    private Post post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discuss);
        Intent intent = getIntent();
        mTitle = intent.getStringExtra("user");
        mContent = intent.getStringExtra("user1");
        mId = intent.getStringExtra("user2");
        title = (TextView) findViewById(R.id.title);
        content = (TextView) findViewById(R.id.content);
        discuss = (EditText) findViewById(R.id.discuss);
        finish = (Button) findViewById(R.id.finish);
        title.setText(mTitle);
        content.setText(mContent);
        user = BmobUser.getCurrentUser(User.class);
        post = new Post();
        post.setObjectId(mId);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish.setEnabled(false);
                final Comment comment = new Comment();
                comment.setContent(discuss.getText().toString());
                comment.setPost(post);
                comment.setUser(user);
                comment.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if(e == null){
                            Toast.makeText(DiscussActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Log.e("discuss","评论失败");
                        }
                    }
                });
                discuss.setText("");
                finish.setEnabled(true);
                finish();
            }
        });
    }
}

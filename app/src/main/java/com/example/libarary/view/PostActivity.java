package com.example.libarary.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.libarary.R;
import com.example.libarary.xinxibao.Post;
import com.example.libarary.xinxibao.User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class PostActivity extends AppCompatActivity {
    private EditText title,context,phone;
    private Toolbar toolbar;
    private User user;
    private Post post;
    private String sTitle,sContext,sPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        init();
        user = BmobUser.getCurrentUser(User.class);
        Log.e("TGA",user.getUsername());
        post = new Post();
        toolbar = (Toolbar) findViewById(R.id.post_toolbar);
        toolbar.inflateMenu(R.menu.post_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.finish:
                        sTitle = title.getText().toString();
                        sContext = context.getText().toString();
                        sPhone = phone.getText().toString();
                        post.setName(user.getUsername());
                        post.setAuthor(user);

                        post.setContent(sContext);
                        post.setTitle(sTitle);

                        post.setPhone(sPhone);
                        post.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if(e == null){
                                    Toast.makeText(PostActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                                    title.setText("");
                                    context.setText("");
                                    phone.setText("");
                                    finish();
                                }
                                else{
                                    Log.e("bmob","发布失败" + e.getMessage());
                                }
                            }
                        });
                        break;
                }
                return true;
            }
        });
    }
    public void init(){
        title = (EditText) findViewById(R.id.post_title);
        context = (EditText) findViewById(R.id.post_context);

        phone = (EditText) findViewById(R.id.post_phone);
    }
}

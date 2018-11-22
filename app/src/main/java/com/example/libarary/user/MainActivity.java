package com.example.libarary.user;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.libarary.R;
import com.example.libarary.view.MainView;
import com.example.libarary.xinxibao.User;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Iterator;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class MainActivity extends AppCompatActivity {
    private SimpleDraweeView simpleDraweeView;
    private Button login,register,findKey;
    private EditText userName,userKey;
    private User user = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        simpleDraweeView = (SimpleDraweeView) findViewById(R.id.sdv_logo);
        simpleDraweeView.setImageURI("res://com.example.libarary/" + R.drawable.touxiang);
        init();
        userKey.setTransformationMethod(PasswordTransformationMethod.getInstance());
        BmobUser bmobUser = BmobUser.getCurrentUser();
        if(bmobUser != null){
            startActivity(new Intent(MainActivity.this, MainView.class));
        }
        initButton();
    }
    private void init(){
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.login_register);

        userName = (EditText) findViewById(R.id.login_username);
        userKey = (EditText) findViewById(R.id.login_userkey);
    }
    private void initButton(){
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register.setEnabled(false);
                Intent intent= new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
                register.setEnabled(true);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login.setEnabled(false);
                String name = userName.getText().toString();
                String key = userKey.getText().toString();
                user.setUsername(name);
                user.setPassword(key);
                user.login(new SaveListener<User>() {
                    @Override
                    public void done(User user, BmobException e) {
                         if(e == null){
                             userName.setText("");
                             userKey.setText("");
                             Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                             startActivity(new Intent(MainActivity.this, MainView.class));
                         }
                        else{
                             Toast.makeText(MainActivity.this, "密码或账号错误", Toast.LENGTH_SHORT).show();
                         }
                    }
                });
                userName.setText("");
                userKey.setText("");
                login.setEnabled(true);
            }

        });
//        findKey.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "点击了", Toast.LENGTH_SHORT).show();
//                findKey.setEnabled(false);
//                Intent intent = new Intent(MainActivity.this,FindActivity1.class);
//                startActivity(intent);
//                Toast.makeText(MainActivity.this, "跳转了", Toast.LENGTH_SHORT).show();
//                findKey.setEnabled(true);
//            }
//        });
    }

}

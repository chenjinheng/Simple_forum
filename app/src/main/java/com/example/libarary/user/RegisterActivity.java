package com.example.libarary.user;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.libarary.R;
import com.example.libarary.xinxibao.User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class RegisterActivity extends AppCompatActivity {
    private Button register,back;
    private EditText userName,passKey,eMail;
    private String name,key,email;
    User user = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        passKey.setTransformationMethod(PasswordTransformationMethod.getInstance());
        initButton();
    }
    private void init(){
        register = (Button) findViewById(R.id.zc_register);
        back = (Button) findViewById(R.id.zc_fanhui);
        userName = (EditText) findViewById(R.id.zc_name);
        passKey = (EditText) findViewById(R.id.zc_key);
        eMail = (EditText) findViewById(R.id.ed_rekey);



    }
    private void initButton(){

//        if(key.length() <= 6){
//            Toast.makeText(this, "密码长度过短", Toast.LENGTH_SHORT).show();
//        }
//        if (!key.equals(reKey)){
//            Toast.makeText(this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
//        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = userName.getText().toString();
                key = passKey.getText().toString();
                email = eMail.getText().toString();
                user.setUsername(name);
                user.setPassword(key);
                user.setEmail(email);
                user.signUp(new SaveListener<User>() {
                    @Override
                    public void done(User user, BmobException e) {
                        if(e == null){
                            userName.setText("");
                            passKey.setText("");
                            eMail.setText("");
                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Log.e("TAG",e.toString());
                        }
                    }
                });
                user.requestEmailVerify(email, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if( e == null){
                            Toast.makeText(RegisterActivity.this, "请到你的邮箱激活", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Log.e("TGA",e.toString());
                            Toast.makeText(RegisterActivity.this, "失败" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}

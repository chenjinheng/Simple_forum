package com.example.libarary.user;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.libarary.R;
import com.example.libarary.xinxibao.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by 陈金桁 on 2018/1/14.
 */

public class FindActivity extends Activity {
    private Button find;
    private EditText email;
    private User user = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        find = (Button) findViewById(R.id.login_find);
        email = (EditText) findViewById(R.id.login_findkey);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.resetPasswordByEmail(email.toString(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e == null){
                            Toast.makeText(FindActivity.this, "请到邮箱修改密码", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(FindActivity.this, "邮箱错误", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
            }

        });

    }
}

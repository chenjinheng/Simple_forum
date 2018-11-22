package com.example.libarary.user;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.libarary.R;
import com.example.libarary.xinxibao.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class FindActivity1 extends AppCompatActivity {
    private Button find;
    private EditText email;
    private User user = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find1);
        find = (Button) findViewById(R.id.login_find);
        email = (EditText) findViewById(R.id.login_findkey);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.resetPasswordByEmail(email.toString(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e == null){
                            Toast.makeText(FindActivity1.this, "请到邮箱修改密码", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(FindActivity1.this, "邮箱错误", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
            }

        });
        finish();
    }
}

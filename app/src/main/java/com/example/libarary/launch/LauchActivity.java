package com.example.libarary.launch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.Window;

import com.example.libarary.R;
import com.example.libarary.user.MainActivity;

/**
 * Created by 陈金桁 on 2018/1/13.
 */

public class LauchActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lanuch);
        Integer time = 2000;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(LauchActivity.this, MainActivity.class));
                LauchActivity.this.finish();
            }
        },time);
    }
}

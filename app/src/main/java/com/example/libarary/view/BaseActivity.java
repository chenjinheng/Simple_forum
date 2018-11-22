package com.example.libarary.view;

import android.support.v4.app.FragmentActivity;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by 陈金桁 on 2018/1/26.
 */

public class BaseActivity extends FragmentActivity {
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

    }
}

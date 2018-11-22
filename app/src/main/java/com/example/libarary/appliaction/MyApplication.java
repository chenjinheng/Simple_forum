package com.example.libarary.appliaction;

import android.app.Application;

import com.example.libarary.handler.DemoMessageHandler;
import com.example.libarary.service.MyMessageHandler;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.Bmob;

/**
 * Created by 陈金桁 on 2018/1/13.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {

        super.onCreate();
        Bmob.initialize(this,"bb85ece2864949b783a85781bb110376");
        Fresco.initialize(this);

//        if (getApplicationInfo().packageName.equals(getMyProcessName())){
            BmobIM.init(getApplicationContext());
            BmobIM.registerDefaultMessageHandler(new MyMessageHandler(this));
//        }
    }
//    public static String getMyProcessName(){
//        try {
//            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
//            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
//            String processName = mBufferedReader.readLine().trim();
//            mBufferedReader.close();
//            return processName;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}

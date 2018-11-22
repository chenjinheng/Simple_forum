package com.example.libarary.utils;

/**
 * Created by 陈金桁 on 2018/2/23.
 */

public class Util {
    public static boolean checkSdCard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }
}

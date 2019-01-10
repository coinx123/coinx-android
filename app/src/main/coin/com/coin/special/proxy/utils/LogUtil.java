package com.coin.special.proxy.utils;

import android.util.Log;

/**
 * Created by zinc on 2018/5/12.
 */
public class LogUtil {

    private static final String TAG = "LogUtil";

    public static void i(String tag, String msg) {
        Log.i(tag, "输出日志：\r\n" + msg);
    }

    public static void i(String msg) {
        Log.i(TAG, "输出日志：\r\n" + msg);
    }

    public static void e(String tag, String msg) {
        Log.e(tag, "输出日志：\r\n" + msg);
    }

    public static void e(String msg) {
        Log.e(TAG, "输出日志：\r\n" + msg);
    }

}

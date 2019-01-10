package com.coin.libbase.utils;

import android.app.Application;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/20
 * @description Toast
 */

public class ToastUtil {

    private static Toast sToast;

    public static void init(Application context) {
        sToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
    }

    public static void show(String message) {
        sToast.setText(message);
        sToast.show();
    }

    public static void showTop(String message) {
        sToast.setText(message);
        sToast.setGravity(Gravity.CENTER | Gravity.TOP, 0, 0);
        sToast.show();
    }

    public static void showCenter(String message) {
        sToast.setText(message);
        sToast.setGravity(Gravity.CENTER, 0, 0);
        sToast.show();
    }

    public static void showBottom(String message) {
        sToast.setText(message);
        sToast.setGravity(Gravity.CENTER | Gravity.BOTTOM, 0, 0);
        sToast.show();
    }
}

package com.coin.exchange.cache;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;

/**
 * @author dean
 * @date 创建时间：2018/11/8
 * @description Preference管理器
 */

public class PreferenceManager {

    //是否第一次安装 true表示已经安装过，false表示没有
    public final static String INSTALLATION_STATUS = "installation_status";
    //登录状态 true表示已经登录，false表示没有登录
    public final static String LOGIN_STATUS = "login_status";

    // 增长时，颜色为红
    public final static String INCREASE_COLOR_IS_RED = "INCREASE_COLOR_IS_RED";

    //美元兑换人民币汇率
    public final static String RATE = "rate";

    //交易确认对话框是否显示
    public final static String IS_SHOW_TRADE_DIALOG = "IS_SHOW_TRADE_DIALOG";

    //是否显示资产值
    public final static String IS_SHOW_ASSETS_VALUE = "is_show_asset_svalue";

    @NonNull
    private final SharedPreferences mPrefs;
    private static final String PREFERENCES = "coin_settings";

    @Inject
    PreferenceManager(@NonNull final Context context) {
        mPrefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
    }

    public void putBoolean(@NonNull String name, boolean value) {
        mPrefs.edit().putBoolean(name, value).apply();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return mPrefs.getBoolean(key, defValue);
    }

    public void putFloat(@NonNull String name, float value) {
        mPrefs.edit().putFloat(name, value).apply();
    }

    public float getFloat(String key, float defValue) {
        return mPrefs.getFloat(key, defValue);
    }
}

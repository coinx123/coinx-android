package com.coin.exchange.context;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.support.multidex.MultiDexApplication;

import com.coin.exchange.R;
import com.coin.exchange.cache.CacheHelper;
import com.coin.exchange.cache.PreferenceManager;
import com.coin.exchange.di.component.AppComponent;
import com.coin.exchange.di.component.DaggerAppComponent;
import com.coin.exchange.di.module.AppModule;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.coin.libbase.utils.ToastUtil;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/7
 * @description
 */
public class AppApplication extends MultiDexApplication {

    @SuppressLint("StaticFieldLeak")
    private static Context CONTEXT;

    @NonNull
    private static AppComponent appComponent;
    @Inject
    PreferenceManager preferenceManager;

    @Override
    public void onCreate() {
        super.onCreate();

        CONTEXT = this;
        ToastUtil.init(this);

        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        appComponent.inject(this);

        CacheHelper.init(this);

        // 初始化 bugly 启用自定义升级对话框界面
        Beta.autoDownloadOnWifi = false;
        Beta.autoCheckUpgrade = true;
        Beta.showInterruptedStrategy = true;
        Beta.initDelay = 1000 * 3;
        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        Beta.upgradeCheckPeriod = 10 * 1000;
        Beta.upgradeDialogLayoutId = R.layout.upgrade_dialog;

        //腾讯崩溃日志收集
        Bugly.init(getApplicationContext(),"50c17c48fc", false);//不能放在线程中

    }

    public static Context getContext() {
        return CONTEXT;
    }

    public static AppComponent getAppComponent(){
        return appComponent;
    }
}

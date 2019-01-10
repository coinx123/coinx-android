package com.coin.exchange.context;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.multidex.MultiDexApplication;
import android.os.Process;
import android.util.Log;

import com.coin.exchange.GetServerProcessDataAidL;
import com.coin.exchange.utils.AppUtils;
import com.coin.special.proxy.ProxyConfig;
import com.coin.special.proxy.service.GetServerProcessDataService;
import com.coin.special.proxy.service.ProxyService;

import info.guardianproject.netcipher.webkit.WebkitProxy;

public class ProxyApp extends MultiDexApplication {

    private static final String PROXY_PROCESS_NAME = ":coinServer";
    private static final String TAG = "ProxyApp";

    // 刚开启
    private static final int NONE = 0x001;
    // 连接中
    private static final int CONNECTING = 0x002;
    // 连接成功
    private static final int SUCCESS = 0x004;

    // 代理状态
    private static int proxyState = NONE;

    //与第二进程的通讯
    private final static ServiceConnection serverServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "成功连接 server service 成功");
            GetServerProcessDataAidL serverProcessDataHolder = GetServerProcessDataAidL.Stub.asInterface(service);

            proxyState = SUCCESS;

            // 连接成功，进行代理开启
            try {
                WebkitProxy.setProxy(ProxyApp.class.getName(), AppApplication.getContext(),
                        null, ProxyConfig.SERVER_ADDRESS, serverProcessDataHolder.getPort());
                Log.i(TAG, "代理开启成功[" + ProxyConfig.SERVER_ADDRESS + ":" + serverProcessDataHolder.getPort() + "]");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "断开第二进程获取数据服务成功");

            proxyState = NONE;

            // 连接断开，代理去除
            try {
                WebkitProxy.resetProxy(ProxyApp.class.getName(), AppApplication.getContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    // 页面数量
    private int pageCount = 0;
    //声明一个监听Activity们生命周期的接口
    private ActivityLifecycleCallbacks activityLifecycleCallbacks = new ActivityLifecycleCallbacks() {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {
            Log.d(TAG, "onActivityStarted.");
            if (pageCount == 0) {
                Log.i(TAG, "ActivityLifecycleCallbacks: 进入前台");
                if(proxyState == NONE){ // 代理的状态还未连接，则进行连接
                    Intent intent = new Intent(getApplicationContext(), GetServerProcessDataService.class);
                    bindService(intent, serverServiceConnection, Context.BIND_AUTO_CREATE);
                }
            }
            pageCount++;
        }

        @Override
        public void onActivityResumed(Activity activity) {
            Log.d(TAG, "onActivityResumed.");
        }

        @Override
        public void onActivityPaused(Activity activity) {
            Log.d(TAG, "onActivityPaused.");
        }

        @Override
        public void onActivityStopped(Activity activity) {
            Log.d(TAG, "onActivityStopped.");
            pageCount--;
            if (pageCount == 0) {
                Log.i(TAG, "ActivityLifecycleCallbacks: 切入后台");
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            Log.d(TAG, "onActivityDestroyed.");
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        int pid = Process.myPid();
        //不是:photonServer进程的开启
        if (!AppUtils.getAppNameByPID(getApplicationContext(), pid).contains(PROXY_PROCESS_NAME)) {
            startService(new Intent(getBaseContext(), ProxyService.class));

            proxyState = CONNECTING;

            Intent intent = new Intent(getApplicationContext(), GetServerProcessDataService.class);
            bindService(intent, serverServiceConnection, Context.BIND_AUTO_CREATE);

        }

        registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }

    @Override
    public void onTerminate() {
        unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks);
        super.onTerminate();
    }

}
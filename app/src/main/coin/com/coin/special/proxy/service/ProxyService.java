package com.coin.special.proxy.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.coin.exchange.ServerAidl;
import com.coin.special.proxy.HttpServer;

public class ProxyService extends Service {

    private static final String TAG = "ProxyService";

    private ProxyBinder mBinder;

    @Override
    public void onCreate() {
        super.onCreate();

        HttpServer.startServer(getBaseContext());
        mBinder = new ProxyBinder();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private static class ProxyBinder extends ServerAidl.Stub {

        @Override
        public String getServiceName() throws RemoteException {
            return "ProxyProcess";
        }

    }
}
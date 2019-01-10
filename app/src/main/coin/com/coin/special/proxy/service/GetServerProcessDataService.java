package com.coin.special.proxy.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.coin.exchange.GetServerProcessDataAidL;
import com.coin.special.proxy.ProxyConfig;

/**
 * @author dean
 * @date 创建时间：2018/07/11
 * @description
 */

public class GetServerProcessDataService extends Service {

    private IBinder mBinder = new GetServerProcessDataAidL.Stub() {

        @Override
        public int getPort() throws RemoteException {
            return ProxyConfig.PORT;
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

}

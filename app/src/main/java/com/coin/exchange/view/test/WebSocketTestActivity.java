package com.coin.exchange.view.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.coin.exchange.R;
import com.coin.exchange.config.okEx.ChannelHelper;
import com.coin.exchange.webSocket.okEx.okExFuture.FutureWebSocketManager;
import com.coin.exchange.webSocket.okEx.okExLogin.LoginWebSocketManager;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/6
 * @description
 */
public class WebSocketTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_socket_test);
    }

    public void subDetail(View view) {
        FutureWebSocketManager.getInstance().subscribeDetail(ChannelHelper.X.BTC, ChannelHelper.Y.THIS_WEEK);
    }

    public void unsubDetail(View view) {
        FutureWebSocketManager.getInstance().unsubscribeDetail(ChannelHelper.X.BTC, ChannelHelper.Y.THIS_WEEK);
    }

    public void subKLine(View view) {
        FutureWebSocketManager.getInstance().subscribeKLine(ChannelHelper.X.BTC,
                ChannelHelper.Y.THIS_WEEK, ChannelHelper.Z.ONE_MIN);
    }

    public void unsubKLine(View view) {
        FutureWebSocketManager.getInstance().unsubscribeKLine(ChannelHelper.X.BTC,
                ChannelHelper.Y.THIS_WEEK, ChannelHelper.Z.ONE_MIN);
    }

    public void subDepth(View view) {
        FutureWebSocketManager.getInstance().subscribeDepth(ChannelHelper.X.BTC, ChannelHelper.Y.THIS_WEEK);
    }

    public void unsubDepth(View view) {
        FutureWebSocketManager.getInstance().unsubscribeDepth(ChannelHelper.X.BTC, ChannelHelper.Y.THIS_WEEK);
    }

    public void subAllDepth(View view) {
        FutureWebSocketManager.getInstance().subscribeAllDepth(ChannelHelper.X.BTC,
                ChannelHelper.Y.THIS_WEEK, ChannelHelper.Z1.FIVE);
    }

    public void unsubAllDepth(View view) {
        FutureWebSocketManager.getInstance().unsubscribeAllDepth(ChannelHelper.X.BTC,
                ChannelHelper.Y.THIS_WEEK, ChannelHelper.Z1.FIVE);
    }

    public void subTrade(View view) {
        FutureWebSocketManager.getInstance().subscribeTrade(ChannelHelper.X.BTC, ChannelHelper.Y.THIS_WEEK);
    }

    public void unsubTrade(View view) {
        FutureWebSocketManager.getInstance().unsubscribeTrade(ChannelHelper.X.BTC, ChannelHelper.Y.THIS_WEEK);
    }

    public void subIndex(View view) {
        FutureWebSocketManager.getInstance().subscribeIndex(ChannelHelper.X.BTC);
    }

    public void unsubIndex(View view) {
        FutureWebSocketManager.getInstance().unsubscribeIndex(ChannelHelper.X.BTC);
    }

    public void subForecast(View view) {
        FutureWebSocketManager.getInstance().subscribeForecast(ChannelHelper.X.BTC);
    }

    public void unsubForecast(View view) {
        FutureWebSocketManager.getInstance().unsubscribeForecast(ChannelHelper.X.BTC);
    }


    public void login(View view) {
        new Thread() {
            @Override
            public void run() {
                LoginWebSocketManager.getInstance().connect();
            }
        }.start();
    }

    public void close(View view) {
        LoginWebSocketManager.getInstance().close();
    }

    public void closeMockErrorClose(View view) {
        LoginWebSocketManager.getInstance().close(false);
    }

    public void closeSub(View view) {
        FutureWebSocketManager.getInstance().close();
    }

    public void closeMockErrorCloseSub(View view) {
        FutureWebSocketManager.getInstance().mockErrorClose();
    }
}

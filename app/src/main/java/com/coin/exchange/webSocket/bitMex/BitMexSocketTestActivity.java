package com.coin.exchange.webSocket.bitMex;

import android.content.Intent;
import android.view.View;

import com.coin.exchange.R;
import com.coin.libbase.view.activity.JBaseActivity;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/12/14
 * @description
 */
public class BitMexSocketTestActivity extends JBaseActivity {

    @Override
    protected int getLayout() {
        return R.layout.activity_bitmex_socket_test;
    }

    @Override
    protected void initIntent(Intent intent) {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    public void subscribe(View view) {
        BitMEXWebSocketManager.getInstance().subscribeTest();
    }

    public void unsubscribe(View view) {
        BitMEXWebSocketManager.getInstance().unsubscribeTest();
    }

    public void errorClose(View view) {
        BitMEXWebSocketManager.getInstance().testCloseError();
    }

    public void normalClose(View view) {
        BitMEXWebSocketManager.getInstance().close();
    }

}

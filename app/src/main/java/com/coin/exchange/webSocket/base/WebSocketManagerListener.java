package com.coin.exchange.webSocket.base;

import okio.ByteString;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/12/14
 * @description 状态回调
 */
public interface WebSocketManagerListener {

    void onConnectSuc();

    void onConnectFailure(boolean reconnect);

    void onMessage(String msg);

    void onMessage(ByteString msg);

}

package com.coin.exchange.webSocket.base.listener;

import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/12/14
 * @description WebSocket的接口，BaseWebSocketManager中会通过这个接口，操作WebSocket
 */
public abstract class IWebSocketListener extends WebSocketListener {

    public static final int NOT_CONNECTED = 0x001;
    public static final int CONNECTING = 0x002;
    public static final int CONNECTED = 0x004;

    /**
     * 获取当前的状态
     *
     * @return
     */
    public abstract int getCurState();

    /**
     * 设置当前的状态
     *
     * @param curState
     */
    public abstract void setCurState(int curState);

    /**
     * 发送订阅
     *
     * @param msg 订阅信息
     */
    public abstract void sendMsg(String msg);

    /**
     * 释放
     */
    public abstract void release();

    /**
     * 复位
     */
    public abstract void reset();

    /**
     * 测试使用
     * @return
     */
    public abstract WebSocket _getWebSocket();

}

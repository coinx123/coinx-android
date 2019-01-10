package com.coin.exchange.webSocket.base;

import android.util.Log;

import com.coin.exchange.webSocket.base.listener.BaseWebSocketListener;
import com.coin.exchange.webSocket.base.listener.IWebSocketListener;
import com.coin.exchange.webSocket.bitMex.BitMEXWebSocketManager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.internal.Util;
import okio.ByteString;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/12/14
 * @description
 */
public abstract class BaseWebSocketManager implements WebSocketManagerListener {

    protected String TAG = this.getClass().getSimpleName();

    // 每10秒发一次ping [子类需要自行修改]
    protected static final int PING_INTERVAL = 10 * 1000;

    // 所有订阅的事件
    protected final Set<String> mAllEvent;
    // 成功的事件
    protected final Set<String> mSucEvent;
    // 需要重试的事件
    protected final Set<String> mRetryEvent;
    // 事件引用计数器
    protected final Map<String, Integer> mCounter;

    // 用于装载 未连接 或 处理连接 中发起的事件，在连接成功
    protected final Set<String> mWaitingEvent;

    // 通过这个进行操作真正的WebSocket
    protected final IWebSocketListener mWebSocketListener;

    // 线程池
    protected ScheduledExecutorService mExecutor;

    /**
     * 操作锁（锁的对象包括以下四个）：
     * {@link #mAllEvent}   所有订阅的事件
     * {@link #mSucEvent}   成功的事件
     * {@link #mRetryEvent} 需要重试的事件
     * {@link #mCounter}    事件引用计数器
     */
    protected final Object mLock;

    protected BaseWebSocketManager() {
        mAllEvent = new HashSet<>();
        mSucEvent = new HashSet<>();
        mRetryEvent = new HashSet<>();
        mCounter = new HashMap<>();

        mWaitingEvent = new HashSet<>();

        mLock = new Object();

        mWebSocketListener = new BaseWebSocketListener(this);
    }

    /**
     * 订阅事件
     *
     * @param msg
     */
    protected final void subscribe(String msg) {
        synchronized (mLock) {
            mAllEvent.add(msg);

            // 增加计数器
            int count = 1;
            if (mCounter.containsKey(msg)) {
                count = mCounter.get(msg) + 1;
            }
            mCounter.put(msg, count);

            // 如果在成功的集合中已经存在，则不用在继续往下走
            if (mSucEvent.size() != 0 && mSucEvent.contains(msg)) {
                return;
            }
        }

        // 如果已经连接，则进行发送订阅消息，
        // 如果未连接，则将订阅消息先添加至 mWaitingEvent 集合，然后进行连接，等待连接成功后发送订阅
        if (mWebSocketListener.getCurState() == IWebSocketListener.CONNECTED) {
            // 发送信息
            String result = assembleSendMsg(MsgType.SUBSCRIBE, msg);
            mWebSocketListener.sendMsg(result);
        } else {
            synchronized (mLock) {
                // 先加进等待集合，等待连接成功在发送
                mWaitingEvent.add(msg);
            }

            // 进行连接
            connect();
        }

    }

    /**
     * 取消订阅
     *
     * @param msg
     */
    protected final void unsubscribe(String msg) {
        synchronized (mLock) {
            int count;

            // 检测是否已经存在
            if (mCounter.containsKey(msg)) {
                // 获取当前的订阅数量
                count = mCounter.get(msg);
                // 如果订阅数量 大于 1，进行减一，并保存
                if (count > 0) {
                    count = count - 1;
                    mCounter.put(msg, count);
                } else {
                    // 如果订阅数量 小于等于 0，则不进行取消
                    return;
                }
            } else {
                // 如果不存在，则直接返回
                return;
            }

            // 如果 引用小于等于 0，则需要进行清理数据，并进行取消订阅
            if (count <= 0) {
                if (mWebSocketListener != null) {
                    String result = assembleSendMsg(MsgType.UNSUBSCRIBE, msg);
                    mWebSocketListener.sendMsg(result);
                    mSucEvent.remove(msg);
                    mAllEvent.remove(msg);
                    mRetryEvent.remove(msg);
                    mWaitingEvent.remove(msg);
                }
            }
        }

    }

    /**
     * 进行WebSocket连接
     */
    private void connect() {

        if (mWebSocketListener.getCurState() == IWebSocketListener.NOT_CONNECTED) {

            synchronized (this) {
                int curState = mWebSocketListener.getCurState();

                // 未连接
                if (curState == IWebSocketListener.NOT_CONNECTED) {

                    // 复位
                    mWebSocketListener.reset();
                    // 设置为连接中
                    mWebSocketListener.setCurState(IWebSocketListener.CONNECTING);
                    Request request = new Request
                            .Builder()
                            .url(getWebSocketUrl())
                            .build();
                    getOkHttpClient().newWebSocket(request, mWebSocketListener);
                } else {
                    Log.i(TAG, "WebSocket 已经处于连接状态，code：" + curState);
                }
            }

        }

    }

    @Override
    public void onConnectSuc() {

        // 连接成功进行心跳包配置
        mExecutor = new ScheduledThreadPoolExecutor(1,
                Util.threadFactory("LoginPingPongThread", false));
        mExecutor.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        String pingPongMsg = getPingPongMsg();
                        if (mWebSocketListener != null) {
                            Log.i(TAG, "PING-PONG[Listener not null]:" + pingPongMsg);

                            mWebSocketListener.sendMsg(pingPongMsg);

                            // 发送 需要重试的事件
                            synchronized (mLock) {
                                showEventInfo();

                                for (String msg : mRetryEvent) {
                                    String result = assembleSendMsg(MsgType.SUBSCRIBE, msg);
                                    mWebSocketListener.sendMsg(result);
                                }

                                for (String msg : mWaitingEvent) {
                                    String result = assembleSendMsg(MsgType.SUBSCRIBE, msg);
                                    mWebSocketListener.sendMsg(result);
                                }

                            }

                        } else {
                            Log.i(TAG, "PING-PONG[Listener is null]:" + getPingPongMsg());
                        }
                    }
                }, 0, getPingInterval(), MILLISECONDS);

    }

    /**
     * 正常关闭
     */
    public void close() {
        // 释放 WebSocket
        if (mWebSocketListener != null) {
            mWebSocketListener.release();
        }

        // 释放线程池
        if (mExecutor != null) {
            mExecutor.shutdown();
            mExecutor = null;
        }

        // 清空所有的数据
        synchronized (mLock) {
            mAllEvent.clear();
            mSucEvent.clear();
            mRetryEvent.clear();
            mWaitingEvent.clear();
            mCounter.clear();
        }
    }

    /**
     * 连接失败
     *
     * @param reconnect 是否要重新连接
     */
    @Override
    public void onConnectFailure(boolean reconnect) {

        if (reconnect) { // 需要重连
            Log.i(TAG, "onConnectFailure: 进行重连");
            synchronized (mLock) {
                // 清除之前的状态
                mSucEvent.clear();
                mRetryEvent.clear();
                mWaitingEvent.clear();

                // 将所有需要订阅的数据加进 mWaitingEvent 中，等连接成功，进行订阅
                mWaitingEvent.addAll(mAllEvent);
            }
            connect();
        } else {  //不需要进行重连，则直接中断
            Log.i(TAG, "onConnectFailure: 不进行重连");
            showEventInfo();

            // 清空所有的数据
            synchronized (mLock) {
                mAllEvent.clear();
                mSucEvent.clear();
                mRetryEvent.clear();
                mWaitingEvent.clear();
                mCounter.clear();
            }
        }

    }

    /**
     * 需要子类自行管理 msg中的内容。
     * 需要对之前发出的msg进行管理，判断其是否订阅成功：
     * 1、成功:
     * (1)添加至{@link #mSucEvent};
     * (2)删除{@link #mRetryEvent}中的对应的msg;
     * <p>
     * 2、失败:
     * (1)a.先检查一次{@link #mSucEvent}中是否存在(因为有可能存在发送两次，而其中一次成功的可能)；
     * ---b.同时删除{@link #mRetryEvent}中的对应的msg；
     * (2)若{@link #mSucEvent}中不存在，则添加至{@link #mRetryEvent}中，等待心跳包 或是 用户再次调用订阅；
     *
     * @param msg
     */
    @Override
    public abstract void onMessage(String msg);

    /**
     * 需要子类自行管理 msg中的内容。
     * 需要对之前发出的msg进行管理，判断其是否订阅成功：
     * 1、成功:
     * (1)添加至{@link #mSucEvent};
     * (2)删除{@link #mRetryEvent}中的对应的msg;
     * <p>
     * 2、失败:
     * (1)a.先检查一次{@link #mSucEvent}中是否存在(因为有可能存在发送两次，而其中一次成功的可能)；
     * ---b.同时删除{@link #mRetryEvent}中的对应的msg；
     * (2)若{@link #mSucEvent}中不存在，则添加至{@link #mRetryEvent}中，等待心跳包 或是 用户再次调用订阅；
     *
     * @param msg
     */
    @Override
    public abstract void onMessage(ByteString msg);

    protected synchronized void showEventInfo() {
        Log.i(TAG, "sendMessage: \r\n" +
                "All: " + mAllEvent.toString() + "\r\n" +
                "Suc: " + mSucEvent.toString() + "\r\n" +
                "Retry: " + mRetryEvent.toString() + "\r\n" +
                "Waiting: " + mWaitingEvent.toString() + "\r\n" +
                "Counter" + mCounter.toString() + "\r\n");
    }

    /**
     * 获取时间间隔，单位毫秒{@link java.util.concurrent.TimeUnit#MILLISECONDS}
     *
     * @return
     */
    protected int getPingInterval() {
        return PING_INTERVAL;
    }

    /**
     * 获取心跳包信息
     *
     * @return
     */
    protected abstract String getPingPongMsg();

    /**
     * 发送信息
     *
     * @param msgType 消息类型
     * @param msg     订阅的信息
     */
    protected abstract String assembleSendMsg(MsgType msgType, String msg);

    /**
     * 获取 WebSocket 的请求 url
     *
     * @return
     */
    protected abstract String getWebSocketUrl();

    /**
     * 获取 请求的 OkHttpClient 实例
     *
     * @return
     */
    protected abstract OkHttpClient getOkHttpClient();

    public enum MsgType {
        SUBSCRIBE,  //订阅
        UNSUBSCRIBE,//取消订阅
    }

}

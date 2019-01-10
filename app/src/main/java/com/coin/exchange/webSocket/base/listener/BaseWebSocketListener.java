package com.coin.exchange.webSocket.base.listener;

import android.util.Log;

import com.coin.exchange.webSocket.base.WebSocketManagerListener;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/12/14
 * @description
 */
public class BaseWebSocketListener extends IWebSocketListener {

    protected String TAG = this.getClass().getSimpleName();

    // 是否需要重连
    private boolean mReconnect = true;

    // 当前的状态
    protected int mCurState = IWebSocketListener.NOT_CONNECTED;
    // 回调的监听器
    protected final WebSocketManagerListener mListener;

    protected WebSocket mWebSocket;

    public BaseWebSocketListener(WebSocketManagerListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {

        mWebSocket = webSocket;
        setCurState(IWebSocketListener.CONNECTED);

        if (mListener != null) {
            mListener.onConnectSuc();
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        if (mListener != null) {
            mListener.onMessage(text);
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        if (mListener != null) {
            mListener.onMessage(bytes);
        }
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        Log.e(TAG, "onClosing[ code: " + code + "; msg: " + reason + " ]");
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        Log.e(TAG, "onClosed[ code: " + code + "; msg: " + reason + " ]");
        close();
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        Log.e(TAG, "onFailure[ msg: " + t.getMessage() + " ]");
        close();
    }

    /**
     * 获取当前 WebSocket 的状态
     *
     * @return
     */
    @Override
    public synchronized int getCurState() {
        return mCurState;
    }

    /**
     * 设置当前 WebSocket 的状态
     *
     * @param curState 当前状态
     *                 {@link IWebSocketListener#NOT_CONNECTED} 未连接
     *                 {@link IWebSocketListener#CONNECTING}    连接中
     *                 {@link IWebSocketListener#CONNECTED}     连接完毕
     */
    @Override
    public synchronized void setCurState(int curState) {
        this.mCurState = curState;
    }

    @Override
    public void sendMsg(String msg) {
        if (mWebSocket != null) {
            mWebSocket.send(msg);
        }
    }

    @Override
    public void release() {
        mReconnect = false;
        if (mWebSocket != null) {
            mWebSocket.cancel();
        }
    }

    @Override
    public void reset() {
        mReconnect = true;
    }

    @Override
    public WebSocket _getWebSocket() {
        return mWebSocket;
    }

    /**
     * 关闭状态
     */
    private void close() {
        // 置空，释放
        mWebSocket = null;

        // 设置成未连接
        setCurState(IWebSocketListener.NOT_CONNECTED);

        // 失败回调
        if (mListener != null) {
            mListener.onConnectFailure(mReconnect);
        }
    }

}

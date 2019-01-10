package com.coin.exchange.webSocket.okEx.okExLogin;

import android.util.Log;

import com.coin.exchange.config.NetConfig;
import com.coin.exchange.net.OkHttpHelper;
import com.coin.exchange.webSocket.okEx.MessageListener;

import okhttp3.Request;
import okhttp3.WebSocket;
import okio.ByteString;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/9
 * @description LoginWebSocket管理
 */
public class LoginWebSocketManager implements MessageListener {

    private static final String TAG = "LoginWebSocketManager";

    private static volatile LoginWebSocketManager instance;

    // WebSocket 监听器
    private final LoginWebSocketListener mListener;

    public static LoginWebSocketManager getInstance() {
        if (instance == null) {
            synchronized (LoginWebSocketManager.class) {
                if (instance == null) {
                    instance = new LoginWebSocketManager();
                }
            }
        }
        return instance;
    }

    private LoginWebSocketManager() {
        mListener = new LoginWebSocketListener(this);
    }

    /**
     * 连接服务器
     */
    public void connect() {
        int loginState = mListener.getLoginState();
        // 未登录
        if (loginState == LoginWebSocketListener.LOGIN_BEF) {
            Request request = new Request
                    .Builder()
                    .url(NetConfig.OK_EX_FUTURE_WEB_SOCKET)
                    .build();
            OkHttpHelper.getOkExInstance().newWebSocket(request, mListener);
        } else {
            Log.i(TAG, "WebSocket 已经处于连接状态，code：" + loginState);
        }
    }

    /**
     * 关闭WebSocket，并且取消{@link LoginWebSocketListener#mLoopLogin}逻辑
     */
    public void close() {
        close(true);
    }

    /**
     * 关闭WebSocket
     *
     * @param isDisableLoopLogin 是否要取消{@link LoginWebSocketListener#mLoopLogin}的逻辑，
     *                           true：取消逻辑，会将其置为false，当WebSocket关闭后，不会再次开启；
     *                           false：继续延续逻辑，至于WebSocket关闭后，会不会再次开启，
     *                           <续>由onMessage返回的result决定，具体可看{{@link LoginWebSocketListener#onMessage(WebSocket, ByteString)}}
     *                           <续>中使用的{@link LoginWebSocketListener#checkLoginState(String)}
     */
    public void close(boolean isDisableLoopLogin) {
        mListener.release(isDisableLoopLogin);
    }

    @Override
    public void onConnect() {
        // 空实现
    }

    @Override
    public void onMessage(String text) {
        Log.i(TAG, "onMessage: " + text);
    }

    @Override
    public void onClose(boolean loopLogin) {
        // 需要重连 并且 重连次数不超过 RETRY_COUNT 数量
        if (loopLogin) {
            connect();
        }

    }
}

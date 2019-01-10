package com.coin.exchange.webSocket.okEx.okExFuture;

import android.support.annotation.Nullable;
import android.util.Log;

import com.coin.exchange.model.okex.webSocket.SubscribeReq;
import com.coin.exchange.utils.GsonUtils;
import com.coin.exchange.webSocket.okEx.MessageListener;

import org.apache.commons.compress.compressors.deflate64.Deflate64CompressorInputStream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.internal.Util;
import okio.ByteString;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/9
 * @description
 */
public class FutureWebSocketListener extends WebSocketListener {

    private static final String TAG = "FutureWebSocketListener";
    private static final String PING_MSG = "{\"event\":\"ping\"}";

    private static final String CHANNEL = "channel";
    private static final String RESULT = "result";
    private static final String DATA = "data";
    private static final String ADD_CHANNEL = "addChannel";

    // 每10秒发一次ping
    private static final int PING_INTERVAL = 10 * 1000;

    // 未连接
    public static final int CONNECT_BEF = 0x001;
    // 连接中
    public static final int CONNECT_ING = 0x002;
    // 已连接
    public static final int CONNECT_ED = 0x004;

    private final MessageListener mListener;

    //连接状态
    private int mConnectState;

    private WebSocket mSocket;

    private final Object mLockObject;
    private final Set<String> mRetrySet;
    private final Set<String> mSucSet;

    private ScheduledExecutorService mExecutor;

    FutureWebSocketListener(MessageListener listener) {
        this.mListener = listener;

        this.mLockObject = new Object();
        this.mRetrySet = new HashSet<>();
        this.mSucSet = new HashSet<>();

        this.mConnectState = CONNECT_BEF;
    }

    public synchronized int getConnectState() {
        return mConnectState;
    }

    public synchronized void setConnectState(int connectState) {
        this.mConnectState = connectState;

        mExecutor = new ScheduledThreadPoolExecutor(1,
                Util.threadFactory("LoginPingPongThread", false));
        mExecutor.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        if (mSocket != null) {
                            Log.i(TAG, "PING-PONG[not null]:" + PING_MSG);

                            Log.i(TAG, "sendMessage: \r\n" +
                                    "RetrySet:" + mRetrySet.toString() + "\r\n" +
                                    "SucSet:" + mSucSet.toString());

                            synchronized (mLockObject) {
                                for (String channel : mRetrySet) {
                                    SubscribeReq req = new SubscribeReq();
                                    req.setEvent(ADD_CHANNEL);
                                    req.setChannel(channel);
                                    String sub = GsonUtils.getInstance().toJson(req);
                                    Log.i(TAG, "retry: " + sub);
                                    mSocket.send(sub);
                                }
                            }
                            mSocket.send(PING_MSG);
                        } else {
                            Log.i(TAG, "PING-PONG[null]:" + PING_MSG);
                        }
                    }
                }, PING_INTERVAL, PING_INTERVAL, MILLISECONDS);
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);

        Log.i(TAG, "onOpen");

        mSocket = webSocket;
        setConnectState(CONNECT_ED);

        mExecutor = new ScheduledThreadPoolExecutor(1,
                Util.threadFactory("LoginPingPongThread", false));
        mExecutor.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        if (mSocket != null) {
                            Log.i(TAG, "PING-PONG[not null]:" + PING_MSG);

                            Log.i(TAG, "sendMessage: \r\n" +
                                    "RetrySet:" + mRetrySet.toString() + "\r\n" +
                                    "SucSet:" + mSucSet.toString());

                            synchronized (mLockObject) {
                                for (String channel : mRetrySet) {
                                    SubscribeReq req = new SubscribeReq();
                                    req.setEvent(ADD_CHANNEL);
                                    req.setChannel(channel);
                                    String sub = GsonUtils.getInstance().toJson(req);
                                    Log.i(TAG, "retry: " + sub);
                                    mSocket.send(sub);
                                }
                            }
                            mSocket.send(PING_MSG);
                        } else {
                            Log.i(TAG, "PING-PONG[null]:" + PING_MSG);
                        }
                    }
                }, PING_INTERVAL, PING_INTERVAL, MILLISECONDS);

        mListener.onConnect();
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
        Log.i(TAG, "onMessage(text)");
        checkSubState(text);

        mListener.onMessage(text);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        super.onMessage(webSocket, bytes);
        Log.i(TAG, "onMessage(bytes)");
        String text = uncompress(bytes.toByteArray());
        checkSubState(text);

        mListener.onMessage(text);
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        super.onClosing(webSocket, code, reason);
        Log.i(TAG, "onClosing [code:" + code + "; reason:" + reason + "]");
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
        Log.i(TAG, "onClosed:[code:" + code + "; reason:" + reason + "]");
        close();
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
        super.onFailure(webSocket, t, response);
        StringBuilder error = new StringBuilder();
        if (response != null) {
            error.append("msg:").append(response.message()).append("; ");
            error.append("code:").append(response.code()).append("; ");
        }
        if (t != null) {
            error.append("t_msg:").append(t.getMessage()).append("; ");
        }
        Log.i(TAG, "onFailure:[" + error.toString() + "]");
        close();
    }

    /**
     * 发送信息
     *
     * @param req
     */
    public void sendMessage(SubscribeReq req) {

        if (req.getEvent().equals(FutureWebSocketManager.REMOVE_CHANNEL)) {
            synchronized (mLockObject) {
                mRetrySet.remove(req.getChannel());
                mSucSet.remove(req.getChannel());
            }
        }

        if (mSocket != null) {
            mSocket.send(GsonUtils.getInstance().toJson(req));
        }
    }

    /**
     * 检测是否订阅成功
     * 成功订阅：
     * [{"binary":1,"channel":"addChannel","data":{"result":true,"channel":"ok_sub_futureusd_btc_ticker_this_week"}}]
     * 失败订阅：
     * [{"binary":1,"channel":"ok_sub_futu_btc_ticker_this_week","data":{"result":false,"error_msg":"param not match.","error_code":20116}}]
     *
     * @param text 返回的json串
     */
    private void checkSubState(String text) {
        try {
            JSONArray jsonArray = new JSONArray(text);
            // 长度为0，直接返回
            if (jsonArray.length() <= 0) {
                return;
            }
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            // 获取 CHANNEL 的对象，如果没有，就不继续
            if (!jsonObject.has(CHANNEL)) {
                return;
            }

            // 如果 channel 不是 addChannel，则不继续
            String channel = jsonObject.getString(CHANNEL);
            if (!channel.equals(ADD_CHANNEL)) {
                return;
            }

            // 检查是否由 DATA 字段，若没有，就不继续
            if (!jsonObject.has(DATA)) {
                return;
            }

            JSONObject dataJsonObject = jsonObject.getJSONObject(DATA);
            // 检查是否由 RESULT 字段，若没有，就不继续
            if (!dataJsonObject.has(RESULT)) {
                return;
            }

            // 检查是否由 RESULT 字段，若没有，就不继续
            if (!dataJsonObject.has(CHANNEL)) {
                return;
            }

            boolean result = dataJsonObject.getBoolean(RESULT);
            String subChannel = dataJsonObject.getString(CHANNEL);

            if (result) {     //订阅成功
                synchronized (mLockObject) {
                    // 如果在重试中有该channel，就移除
                    mRetrySet.remove(subChannel);
                    mSucSet.add(subChannel);
                }
            } else {          // 订阅失败
                synchronized (mLockObject) {
                    mSucSet.remove(subChannel);
                    mRetrySet.add(subChannel);
                }
            }

        } catch (JSONException e) {
//            e.printStackTrace();
        }
    }

    /**
     * 解压数据
     *
     * @param bytes 数据
     * @return
     */
    private String uncompress(byte[] bytes) {
        try (final ByteArrayOutputStream out = new ByteArrayOutputStream();
             final ByteArrayInputStream in = new ByteArrayInputStream(bytes);
             final Deflate64CompressorInputStream zin = new Deflate64CompressorInputStream(in)) {
            final byte[] buffer = new byte[1024];
            int offset;
            while (-1 != (offset = zin.read(buffer))) {
                out.write(buffer, 0, offset);
            }
            return out.toString();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 释放WebSocket
     */
    public void release() {
        if (mSocket != null) {
            mSocket.cancel();
        }
    }

    /**
     * 关闭状态
     */
    private void close() {
        mSocket = null;

        if (mExecutor != null) {
            mExecutor.shutdown();
            mExecutor = null;
        }

        setConnectState(CONNECT_BEF);
        mListener.onClose(true);
    }

}
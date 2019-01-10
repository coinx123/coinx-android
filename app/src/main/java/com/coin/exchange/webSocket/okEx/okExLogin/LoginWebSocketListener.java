package com.coin.exchange.webSocket.okEx.okExLogin;

import android.support.annotation.Nullable;
import android.util.Log;

import com.coin.exchange.cache.OkExUserCache;
import com.coin.exchange.model.okex.OkExUserModel;
import com.coin.exchange.model.okex.webSocket.LoginReq;
import com.coin.exchange.utils.GsonUtils;
import com.coin.exchange.utils.SignUtils;
import com.coin.exchange.webSocket.okEx.MessageListener;

import org.apache.commons.compress.compressors.deflate64.Deflate64CompressorInputStream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
public class LoginWebSocketListener extends WebSocketListener {

    private static final String TAG = "LoginWebSocketListener";

    private static final String RESULT = "result";
    private static final String DATA = "data";

    // 登陆使用的数据
    private static final String METHOD = "GET";
    private static final String PATH = "/users/self/verify";
    private static final String LOGIN_EVENT = "login";

    private static final String PING_MSG = "{\"event\":\"ping\"}";

    // 每10秒发一次ping
    private static final int PING_INTERVAL = 10 * 1000;

    // 未登录
    public static final int LOGIN_BEF = 0x001;
    // 登录中
    public static final int LOGIN_ING = 0x002;
    // 已登录
    public static final int LOGIN_ED = 0x004;

    // WebSocket
    private WebSocket mSocket;
    // 是否需要重连
    private boolean mLoopLogin;
    // 登录状态：1、未登录；2、登录中；3、已登录
    private int mLoginState;

    private ScheduledExecutorService mExecutor;

    private final MessageListener mListener;

    LoginWebSocketListener(MessageListener listener) {
        mLoginState = LOGIN_BEF;
        mLoopLogin = false;

        mListener = listener;
    }

    /**
     * 获取登录状态
     *
     * @return {@link #LOGIN_BEF}\{@link #LOGIN_ING}\{@link #LOGIN_ED}
     */
    public synchronized int getLoginState() {
        return mLoginState;
    }

    /**
     * 释放WebSocket
     *
     * @param isDisableLoopLogin 是否要取消{@link #mLoopLogin}的逻辑，
     *                           true：取消逻辑，会将其置为false，当WebSocket关闭后，不会再次开启；
     *                           false：继续延续逻辑，至于WebSocket关闭后，会不会再次开启，
     *                           <续>由onMessage返回的result决定，具体可看{{@link #onMessage(WebSocket, ByteString)}}
     *                           <续>中使用的{@link #checkLoginState(String)}
     */
    public void release(boolean isDisableLoopLogin) {
        if (isDisableLoopLogin) {
            mLoopLogin = false;
        }

        if (mSocket != null) {
            mSocket.cancel();
        }
    }

    private synchronized void setLoginState(int mLoginState) {
        this.mLoginState = mLoginState;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        Log.i(TAG, "Login WebSocket connect success.");
        mSocket = webSocket;
        sendLoginEvent(webSocket);

        mExecutor = new ScheduledThreadPoolExecutor(1,
                Util.threadFactory("LoginPingPongThread", false));
        mExecutor.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        if (mSocket != null) {
                            Log.i(TAG, "PING-PONG[not null]:" + PING_MSG);
                            mSocket.send(PING_MSG);
                        } else {
                            Log.i(TAG, "PING-PONG[null]:" + PING_MSG);
                        }
                    }
                }, PING_INTERVAL, PING_INTERVAL, MILLISECONDS);
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
        Log.i(TAG, "onMessage(text)");
        checkLoginState(text);

        mListener.onMessage(text);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        super.onMessage(webSocket, bytes);
        Log.i(TAG, "onMessage(bytes)");
        String text = uncompress(bytes.toByteArray());
        checkLoginState(text);

        mListener.onMessage(text);
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        super.onClosing(webSocket, code, reason);
        Log.i(TAG, "onClosing");
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
     * 进行登陆，只有当登陆成功了，才能订阅其余的
     * <p>
     * 成功登录
     * [ {"channel": "login","data": {"result": true}} ]
     * 失败登录
     * [ {"data":{"error_code":20102,"error_msg":"Invalid logon.","result":false}} ]
     */
    private void sendLoginEvent(WebSocket webSocket) {
        // 无用户信息
        if (OkExUserCache.isEmpty()) {
            Log.i(TAG, "onOpen: 不需要发送登录信息");
            return;
        }

        // 设置为正在登录
        setLoginState(LOGIN_ING);

        // 时间戳
        String expires = System.currentTimeMillis() / 1000 + "";

        // 签名
        OkExUserModel okExUserModel = OkExUserCache.getDefault();
        String sign = SignUtils.getOkExSign(okExUserModel.getSecretKey(),
                METHOD,
                PATH,
                expires,
                "");

        // 组装登录请求信息
        LoginReq.Parameters parameters = new LoginReq.Parameters();
        parameters.setApi_key(okExUserModel.getApiKey());
        parameters.setPassphrase(okExUserModel.getPassphrase());
        parameters.setSign(sign.trim());
        parameters.setTimestamp(expires);
        LoginReq loginReq = new LoginReq();
        loginReq.setEvent(LOGIN_EVENT);
        loginReq.setParameters(parameters);

        String message = GsonUtils.getInstance().toJson(loginReq);
        Log.i(TAG, "onOpen: 发送登录信息[" + message + "]");

        if (webSocket != null) {
            webSocket.send(message);
        } else {
            Log.e(TAG, "Login WebSocket is null.");
        }
    }

    /**
     * 检测是否登录成功
     *
     * @param text 返回的json串
     */
    private void checkLoginState(String text) {
        try {
            JSONArray jsonArray = new JSONArray(text);
            // 长度为0，直接返回
            if (jsonArray.length() <= 0) {
                return;
            }
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            // channel:login
            // 获取 data 的对象
            if (jsonObject.has(DATA)) {
                JSONObject dataJSONObject = jsonObject.getJSONObject(DATA);

                // 不包含 RESULT 就直接中断
                if (!dataJSONObject.has(RESULT)) {
                    return;
                }

                // 设置登录状态
                mLoopLogin = dataJSONObject.getBoolean(RESULT);
                setLoginState(mLoopLogin ? LOGIN_ED : LOGIN_BEF);
            }

        } catch (JSONException e) {
            e.printStackTrace();
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
     * 关闭状态
     */
    private void close() {
        mSocket = null;

        if (mExecutor != null) {
            mExecutor.shutdown();
            mExecutor = null;
        }

        setLoginState(LOGIN_BEF);
        mListener.onClose(mLoopLogin);

    }
}

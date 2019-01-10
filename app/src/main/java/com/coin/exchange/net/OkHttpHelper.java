package com.coin.exchange.net;

import com.coin.exchange.config.NetConfig;
import com.coin.exchange.net.interceptor.CommonParamsInterceptor;
import com.coin.exchange.net.interceptor.LogInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/5/15
 * @description 获取OkHttp实例
 */

public class OkHttpHelper {

    private volatile static OkHttpClient bitMexOkHttpClient;
    private volatile static OkHttpClient bitMexWsOkHttpClient;

    private volatile static OkHttpClient okExOkHttpClient;
    private volatile static OkHttpClient okExPureOkHttpClient;

    private static final Object BIT_MEX_LOCK = new Object();
    private static final Object BIT_MEX_WS_LOCK = new Object();

    private static final Object OK_EX_LOCK = new Object();
    private static final Object OK_EX_PURE_LOCK = new Object();

    /**
     * 获取测试的 BitMex OkHttp 实例
     *
     * @return
     */
    public static OkHttpClient getBitMexInstance() {

        if (bitMexOkHttpClient == null) {
            synchronized (BIT_MEX_LOCK) {
                if (bitMexOkHttpClient == null) {
                    OkHttpClient.Builder okHttpHelper = new OkHttpClient.Builder();
                    bitMexOkHttpClient = okHttpHelper
                            .connectTimeout(NetConfig.CONNECT_TIME_OUT, TimeUnit.SECONDS)
                            .readTimeout(NetConfig.READ_TIME_OUT, TimeUnit.SECONDS)
                            .writeTimeout(NetConfig.WRITE_TIME_OUT, TimeUnit.SECONDS)
                            .addInterceptor(new CommonParamsInterceptor(NetConfig.BIT_MEX_HOST))
                            .addInterceptor(new LogInterceptor())
                            .pingInterval(4500, TimeUnit.MILLISECONDS)
                            .hostnameVerifier(new SSLUtils.TrustAllHostnameVerifier())
                            .build();
                }
            }
        }

        return bitMexOkHttpClient;
    }

    /**
     * 获取测试的 BitMex 的 WebSocket的 OkHttp 实例
     *
     * @return
     */
    public static OkHttpClient getBitMexPureInstance() {

        if (bitMexWsOkHttpClient == null) {
            synchronized (BIT_MEX_WS_LOCK) {
                if (bitMexWsOkHttpClient == null) {
                    OkHttpClient.Builder okHttpHelper = new OkHttpClient.Builder();
                    bitMexWsOkHttpClient = okHttpHelper
                            .connectTimeout(NetConfig.CONNECT_TIME_OUT, TimeUnit.SECONDS)
                            .readTimeout(NetConfig.READ_TIME_OUT, TimeUnit.SECONDS)
                            .writeTimeout(NetConfig.WRITE_TIME_OUT, TimeUnit.SECONDS)
                            .addInterceptor(new CommonParamsInterceptor(NetConfig.BIT_MEX_HOST))
                            .addInterceptor(new LogInterceptor())
                            .hostnameVerifier(new SSLUtils.TrustAllHostnameVerifier())
                            .build();
                }
            }
        }

        return bitMexWsOkHttpClient;
    }

    /**
     * 获取正式的 BitMex OkHttp 实例
     *
     * @return
     */
    public static OkHttpClient getOkExInstance() {

        if (okExOkHttpClient == null) {
            synchronized (OK_EX_LOCK) {
                if (okExOkHttpClient == null) {
                    OkHttpClient.Builder okHttpHelper = new OkHttpClient.Builder();
                    okExOkHttpClient = okHttpHelper
                            .connectTimeout(NetConfig.CONNECT_TIME_OUT, TimeUnit.SECONDS)
                            .readTimeout(NetConfig.READ_TIME_OUT, TimeUnit.SECONDS)
                            .writeTimeout(NetConfig.WRITE_TIME_OUT, TimeUnit.SECONDS)
                            .pingInterval(15000, TimeUnit.MILLISECONDS)
                            .addInterceptor(new CommonParamsInterceptor(NetConfig.OK_EX_HOST))
                            .addInterceptor(new LogInterceptor())
                            .hostnameVerifier(new SSLUtils.TrustAllHostnameVerifier())
                            .build();
                }
            }
        }

        return okExOkHttpClient;
    }

    /**
     * 获取正式的 OKEX OkHttp 实例 (不带任何拦截器)
     *
     * @return
     */
    public static OkHttpClient getOkExPureInstance() {

        if (okExPureOkHttpClient == null) {
            synchronized (OK_EX_PURE_LOCK) {
                if (okExPureOkHttpClient == null) {
                    OkHttpClient.Builder okHttpHelper = new OkHttpClient.Builder();
                    okExPureOkHttpClient = okHttpHelper
                            .connectTimeout(NetConfig.CONNECT_TIME_OUT, TimeUnit.SECONDS)
                            .readTimeout(NetConfig.READ_TIME_OUT, TimeUnit.SECONDS)
                            .writeTimeout(NetConfig.WRITE_TIME_OUT, TimeUnit.SECONDS)
                            .addInterceptor(new CommonParamsInterceptor(NetConfig.OK_EX_HOST))
                            .addInterceptor(new LogInterceptor())
                            .hostnameVerifier(new SSLUtils.TrustAllHostnameVerifier())
                            .build();
                }
            }
        }

        return okExPureOkHttpClient;
    }

    /**
     * 获取 OkEX websocket 实例
     *
     * @return
     */
    public static OkHttpClient getOkExForWsInstance() {

        if (okExOkHttpClient == null) {
            synchronized (OK_EX_LOCK) {
                if (okExOkHttpClient == null) {
                    OkHttpClient.Builder okHttpHelper = new OkHttpClient.Builder();
                    okExOkHttpClient = okHttpHelper
                            .connectTimeout(NetConfig.CONNECT_TIME_OUT, TimeUnit.SECONDS)
                            .readTimeout(NetConfig.READ_TIME_OUT, TimeUnit.SECONDS)
                            .writeTimeout(NetConfig.WRITE_TIME_OUT, TimeUnit.SECONDS)
                            .pingInterval(15000, TimeUnit.MILLISECONDS)
                            .addInterceptor(new CommonParamsInterceptor(NetConfig.OK_EX_FUTURE_WEB_SOCKET_HOST))
                            .addInterceptor(new LogInterceptor())
                            .hostnameVerifier(new SSLUtils.TrustAllHostnameVerifier())
                            .build();
                }
            }
        }

        return okExOkHttpClient;
    }

}

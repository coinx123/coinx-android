package com.coin.exchange.config.okEx;

import android.text.TextUtils;
import android.util.Log;

import com.coin.exchange.net.SSLUtils;
import com.google.gson.Gson;
import com.coin.exchange.config.NetConfig;
import com.coin.exchange.model.okex.response.TimeStampInfoRes;
import com.coin.exchange.net.interceptor.LogInterceptor;
import com.coin.exchange.utils.DateUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/2
 * @description
 */
public class ServerTimeStampHelper {

    // 默认相差 8 小时
    public static final long DEFAULT_DELAY = 8 * 60 * 60 * 1000;
    //提前24小时
    public static final long HOURS_24_DELAY = 24 * 60 * 60 * 1000;

    private static final String TAG = "ServerTimeStampHelper";
    private static final String TIMESTAMP_URL = "api/general/v3/time";

    private static ServerTimeStampHelper instance;

    private OkHttpClient timeStampOkHttpClient;
    private Gson gson;

    // 获取服务器时间的时间戳
    private long mObtainTimeStamp;
    // 从服务获取到的时间戳信息
    private TimeStampInfoRes mTimeStampInfoRes;
    // 本地时间和服务器的偏差值
    private long mDelay = DEFAULT_DELAY;
    // 是否已经初始化
    private boolean mIsInit = false;

    private Response response;
//    private Call call;

    public static ServerTimeStampHelper getInstance() {
        if (instance == null) {
            synchronized (ServerTimeStampHelper.class) {
                if (instance == null) {
                    instance = new ServerTimeStampHelper();
                }
            }
        }
        return instance;
    }

    private ServerTimeStampHelper() {

        timeStampOkHttpClient = new OkHttpClient
                .Builder()
                .connectTimeout(NetConfig.CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(NetConfig.READ_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(NetConfig.WRITE_TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor(new LogInterceptor())
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request.Builder requestBuilder = request.newBuilder();
                        requestBuilder.addHeader(OkExHeaderHelper.HOST, NetConfig.OK_EX_HOST);

                        request = requestBuilder.build();

                        return chain.proceed(request);
                    }
                })
                .hostnameVerifier(new SSLUtils.TrustAllHostnameVerifier())
                .build();

        gson = new Gson();

    }

    public enum Type {
        // ISO8601格式
        ISO8601,
        // 10位 时间戳（单位秒）
        TIMESTAMP,
        // ISO8601格式,往前面24小时
        ISO8601_24,
        // 获取 181224 这样的数据格式
        YYMMDD,
    }

    /**
     * 获取 ISO8601 格式时间戳
     *
     * @return 获取到这样的格式 2014-11-06T10:34:47.123Z
     */
    public String getCurrentTimeStamp(Type type) {

        //已经初始化
        if (mIsInit) {
            return getTimeString(type);
        }

        synchronized (this) {

            if (mIsInit) {
                return getTimeString(type);
            }

            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        Request.Builder builder = new Request.Builder().url(NetConfig.OK_EX_URL + TIMESTAMP_URL);
                        Call call = timeStampOkHttpClient.newCall(builder.build());

                        mObtainTimeStamp = System.currentTimeMillis();
                        response = call.execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                        response = null;
                    }
                }
            };
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (response == null) {
                return errorExecute(type);
            }

            String resContent;
            try {
                // 请求不成功，isInit设置为false，下次请求重新获取系统时间
                if (response == null || response.code() != 200 || response.body() == null) {
                    return errorExecute(type);
                }

                resContent = response.body().string();
                if (TextUtils.isEmpty(resContent)) {
                    return errorExecute(type);
                }

            } catch (IOException e) {
                e.printStackTrace();
                return errorExecute(type);
            }

            // 反序列化服务器时间数据
            try {
                mTimeStampInfoRes = gson.fromJson(resContent, TimeStampInfoRes.class);
                if (mTimeStampInfoRes == null || TextUtils.isEmpty(mTimeStampInfoRes.getIso())) {
                    return errorExecute(type);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return errorExecute(type);
            }

            Log.i(TAG, "mTimeStampInfoRes: " + mTimeStampInfoRes);
            long timeStamp = DateUtils.getTimeStampViaISO8601String(mTimeStampInfoRes.getIso());
            if (timeStamp == -1) {
                return errorExecute(type);
            }

            mDelay = System.currentTimeMillis() - timeStamp;
            mIsInit = true;
            return getTimeString(type);
        }
    }

    private String getTimeString(Type type) {
        long result = System.currentTimeMillis() - mDelay;
        switch (type) {
            case ISO8601:
                return DateUtils.getISO8601StringViaTimeStamp(result);
            case TIMESTAMP:
//                return (result / 1000) + "";
//                return mTimeStampInfoRes.getEpoch().substring(0, 10);
                return System.currentTimeMillis() / 1000 + "";
            case ISO8601_24:
                long real_result = result - HOURS_24_DELAY;
                return DateUtils.getISO8601StringViaTimeStamp(real_result);
            case YYMMDD:
                return DateUtils.getYYMMDDViaTimeStamp(result);
            default:
                return DateUtils.getISO8601StringViaTimeStamp(result);
        }

    }

    private String errorExecute(Type type) {
        mObtainTimeStamp = -1;
        mDelay = DEFAULT_DELAY;
        mIsInit = false;
        return getTimeString(type);
    }

    public void log() {
        String builder = "isInit: " + mIsInit + "\r\n" +
                "delay: " + mDelay + "\r\n" +
                "obtainTimeStamp: " + mObtainTimeStamp + "\r\n" +
                "timeStampInfoRes: " + (mTimeStampInfoRes == null ? "is null" : mTimeStampInfoRes.toString()) + "\r\n";
        Log.i(TAG, "log: \r\n" + builder);
    }

    public long getDelay() {
        if (!mIsInit) {
            // 先进行初始化
            getCurrentTimeStamp(Type.ISO8601);
        }
        return mDelay;
    }

}

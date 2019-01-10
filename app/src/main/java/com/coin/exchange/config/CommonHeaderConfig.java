package com.coin.exchange.config;

import android.text.TextUtils;

import com.coin.exchange.cache.BitMexUserCache;
import com.coin.exchange.cache.OkExUserCache;
import com.coin.exchange.config.bitMex.BitMexHeaderHelper;
import com.coin.exchange.config.okEx.OkExHeaderHelper;
import com.coin.exchange.config.okEx.ServerTimeStampHelper;
import com.coin.exchange.utils.SignUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/10/31
 * @description
 */
public class CommonHeaderConfig {

    public static Map<String, String> getBitMexHeader(Request request, String host) {
        Map<String, String> result = new HashMap<>();
        result.put(OkExHeaderHelper.HOST, host);

        if (BitMexUserCache.isEmpty()) {
            return result;
        }

        long expiresNum = System.currentTimeMillis() / 1000 + 5;
        String expires = expiresNum + "";

        String body;
        if (request.body() == null) {
            body = "";
        } else {
            body = bodyToString(request.body());
        }

        String requestPath;
        if (TextUtils.isEmpty(request.url().query())) {
            requestPath = request.url().encodedPath();
        } else {
            requestPath = request.url().encodedPath() + "?" + request.url().encodedQuery().toString();
        }

        String sign = SignUtils.getBitMexSign(BitMexUserCache.getDefault().getSecretKey(),
                request.method(),
                requestPath,
                expires,
                body);

        result.put(BitMexHeaderHelper.KEY, BitMexUserCache.getDefault().getApiKey());
        result.put(BitMexHeaderHelper.EXPIRES, expires);
        result.put(BitMexHeaderHelper.SIGN, sign);
        result.put("Accept", "application/json");

        return result;
    }

    public static Map<String, String> getOkExHeader(Request request, String host) {
        Map<String, String> result = new HashMap<>();
        result.put(OkExHeaderHelper.HOST, host);

        if (OkExUserCache.isEmpty()) {
            return result;
        }

        String expires = ServerTimeStampHelper.getInstance().getCurrentTimeStamp(ServerTimeStampHelper.Type.ISO8601);

        String body;
        if (request.body() == null) {
            body = "";
        } else {
            body = bodyToString(request.body());
        }

        String requestPath;
        if (TextUtils.isEmpty(request.url().query())) {
            requestPath = request.url().encodedPath();
        } else {
            requestPath = request.url().encodedPath() + "?" + request.url().query().toString();
        }
        String sign = SignUtils.getOkExSign(OkExUserCache.getDefault().getSecretKey(),
                request.method(),
                requestPath,
                expires, body);

        result.put(OkExHeaderHelper.KEY, OkExUserCache.getDefault().getApiKey());
        result.put(OkExHeaderHelper.PASSPHRASE, OkExUserCache.getDefault().getPassphrase());
        result.put(OkExHeaderHelper.SIGN, sign.trim());
        result.put(OkExHeaderHelper.TIMESTAMP, expires);
        result.put(OkExHeaderHelper.CONTENT_TYPE, OkExHeaderHelper.CONTENT_TYPE_VALUE);

        return result;
    }

    private static String bodyToString(final RequestBody request) {
        try {
            final Buffer buffer = new Buffer();
            if (request == null) {
                return "";
            }

            request.writeTo(buffer);
            return buffer.readUtf8();

        } catch (final IOException e) {
            e.printStackTrace();
            return "";
        }
    }

}

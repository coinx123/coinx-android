package com.coin.exchange.net.interceptor;

import android.support.annotation.NonNull;

import com.coin.exchange.config.CommonHeaderConfig;
import com.coin.exchange.config.IPConfig;
import com.coin.exchange.config.NetConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/6/6
 * @description 通用参数拦截器
 */

public class CommonParamsInterceptor implements Interceptor {

    private String mHost;

    public CommonParamsInterceptor(String host) {
        this.mHost = host;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        Request request = chain.request();
        Request.Builder requestBuilder = request.newBuilder();

        rebuildHeader(request, requestBuilder);
        rebuildParams(request, requestBuilder);

        request = requestBuilder.build();

        return chain.proceed(request);
    }

    /**
     * 创建新的头部参数
     *
     * @param oldRequest     旧请求
     * @param requestBuilder 重构
     */
    private void rebuildHeader(Request oldRequest, Request.Builder requestBuilder) {

        if (oldRequest == null) {
            return;
        }

        Headers.Builder headerBuilder = oldRequest.headers().newBuilder();
        Map<String, String> commonHeaders;

        String url = oldRequest.url().toString();
        // 判断不同的域名，增加不同的header
        if (url.contains(IPConfig.getBitMexIp(NetConfig.isDebug))) {
            commonHeaders = CommonHeaderConfig.getBitMexHeader(oldRequest, mHost);
        } else if (url.contains(IPConfig.getOkExIp()) || url.contains(IPConfig.getOkExWsIp())) {
            commonHeaders = CommonHeaderConfig.getOkExHeader(oldRequest, mHost);
        } else {
            commonHeaders = new HashMap<>();
        }

        for (Map.Entry<String, String> entry : commonHeaders.entrySet()) {
            headerBuilder.add(entry.getKey(), entry.getValue());
        }
        requestBuilder.headers(headerBuilder.build());

    }

    private void rebuildParams(Request oldRequest, Request.Builder requestBuilder) {
        // 留空
    }

}

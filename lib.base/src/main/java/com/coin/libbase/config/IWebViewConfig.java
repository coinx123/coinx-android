package com.coin.libbase.config;

import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;

import com.coin.libbase.web.JWebViewFragment;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/23
 * @description
 */

public interface IWebViewConfig {

    /**
     * @date 创建时间 2018/4/23
     * @author Jiang zinc
     * @Description 获取白名单
     * @version
     */
    ArrayList<String> getWhiteList();

    /**
     * @date 创建时间 2018/4/23
     * @author Jiang zinc
     * @Description 获取WebView加载链接时头部参数
     * @version
     */
    Map<String, String> getHeader();

    /**
     * @date 创建时间 2018/4/23
     * @author Jiang zinc
     * @Description js回调app
     * @version
     */
    void jsCallBackApp(JWebViewFragment fragment, String type, String content);


    /**
     * @date 创建时间 2018/4/23
     * @author Jiang zinc
     * @Description google建议，如果ssl需不需要通过，以弹框形式询问用户
     * =============如果用户同意，调用{@link SslErrorHandler#proceed()}
     * =============如果用户不同意，调用{@link SslErrorHandler#cancel()}
     * @version
     */
    void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error);

}

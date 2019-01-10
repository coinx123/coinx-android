package com.coin.libbase.web;

import android.annotation.SuppressLint;

import android.graphics.Bitmap;

import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.coin.libbase.R;
import com.coin.libbase.config.JWebViewManager;
import com.coin.libbase.view.fragment.JBaseFragment;

import java.util.HashSet;
import java.util.Set;

import okhttp3.MediaType;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/23
 * @description
 */

public class JWebViewFragment extends JBaseFragment {

    private static final int RETRY_TIMES = 3;

    //回车和换行
    static final String CRLF = "\r\n";
    static final String SPACE = " ";
    static final String VERSION = "HTTP/1.1";
    static final String COLON = ":";

    private static String TAG = JWebViewFragment.class.getSimpleName();

    private final Set<String> htmlSet = new HashSet<>();

    private WebView mWebView;

    private String mUrl;
    private boolean mIsOpenFilter;
    private String mUserAgent;

    public static JWebViewFragment newInstance(String url) {
        JWebViewFragment fragment = new JWebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("LOAD_URL", url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initArgs(Bundle arguments) {
        super.initArgs(arguments);

        this.mUrl = arguments.getString("LOAD_URL");

    }

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.j_fragment_web_view, container, false);
    }

    @Override
    protected void initView(View view) {
        this.mWebView = view.findViewById(R.id.webview);

//        initWebViewSetting(this.mWebView.getSettings());
//        initWebView(this.mWebView);

        //支持javascript
        mWebView.getSettings().setJavaScriptEnabled(true);
// 设置可以支持缩放
        mWebView.getSettings().setSupportZoom(true);
// 设置出现缩放工具
        mWebView.getSettings().setBuiltInZoomControls(true);
//扩大比例的缩放
        mWebView.getSettings().setUseWideViewPort(true);
//自适应屏幕
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setLoadWithOverviewMode(true);

        mWebView.setWebViewClient(new JWebViewClient());
//        mWebView.setWebChromeClient(new JWebChromeClient());

        mUserAgent = mWebView.getSettings().getUserAgentString();

//        ProxySettings.setProxy(mWebView,"127.0.0.1",12580, null);
        htmlSet.add(this.mUrl);
        mWebView.loadUrl(this.mUrl, JWebViewManager.getInstance().getWebViewConfig().getHeader());

    }

    private void initWebView(WebView webView) {
        //设置缩放比例
//        webView.setInitialScale(35);
        //设置滚动条隐藏
//        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

        //第三方的cookie
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        } else {
            CookieManager.getInstance().setAcceptCookie(true);
        }

        webView.setLongClickable(true);
        webView.setWebViewClient(new JWebViewClient());
        webView.setWebChromeClient(new JWebChromeClient());

        addJavascriptInterface();

    }

    private void initWebViewSetting(WebSettings settings) {

        //支持js脚本
        settings.setJavaScriptEnabled(true);
        //支持缩放
        settings.setSupportZoom(true);
        //支持缩放
        settings.setBuiltInZoomControls(true);
        //去除缩放按钮
        settings.setDisplayZoomControls(false);

        //扩大比例的缩放
        settings.setUseWideViewPort(true);
        //自适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);

        //支持内容从新布局
//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //多窗口
        settings.supportMultipleWindows();
        //关闭webview中缓存
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //设置可以访问文件
        settings.setAllowFileAccess(true);
        //当webview调用requestFocus时为webview设置节点
        settings.setNeedInitialFocus(true);
        //支持通过JS打开新窗口
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        //支持自动加载图片
        settings.setLoadsImagesAutomatically(true);

        //启用地理定位
//        settings.setGeolocationEnabled(true);
        //设置渲染优先级
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);

        // 设置支持本地存储
        settings.setDatabaseEnabled(true);
        //设置支持DomStorage
        settings.setDomStorageEnabled(true);
    }

    public WebView getWebView() {
        return mWebView;
    }

    @SuppressLint({"JavascriptInterface", "AddJavascriptInterface"})
    private void addJavascriptInterface() {
        //添加js回调对象，在js回调时，可用android.xxx()
        try {
            IWebViewJavaScriptInterface javaScriptInterface = JWebViewManager.getInstance().getJavaScriptInterfaceClazz().newInstance();
            javaScriptInterface.setWebViewFragment(this);
            this.mWebView.addJavascriptInterface(javaScriptInterface, "android");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //处理WebView中回退的事件
    @Override
    public boolean onConsumeBackEvent(FragmentManager fragmentManager) {
        WebBackForwardList webBackForwardList = this.mWebView.copyBackForwardList();

        //如果有回退历史，而且可回退，就消费该事件
        if (webBackForwardList.getSize() > 0 && this.mWebView.canGoBack()) {
            this.mWebView.goBack();
            return true;
        }

        return super.onConsumeBackEvent(fragmentManager);
    }

    private class JWebViewClient extends WebViewClient {

        //https的ssl验证，需要弹框询问
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            /**
             * google建议，如果ssl需不需要通过，以弹框形式询问用户
             * 如果用户同意，调用{@link SslErrorHandler#proceed()}
             * 如果用户不同意，调用{@link SslErrorHandler#cancel()}
             */
//            JWebViewManager.getInstance().getWebViewConfig().onReceivedSslError(view, handler, error);
            handler.proceed();
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            logi("onLoadResource: " + url);
        }

        private String getMimeType(MediaType mediaType) {
            String mimeType = null;
            if (mediaType.type() != null && mediaType.subtype() != null) {
                mimeType = mediaType.type() + "/" + mediaType.subtype();
            }
            return mimeType;
        }

        private String getEncoding(MediaType mediaType) {
            String encoding = "UTF-8";
            if (mediaType.charset() != null) {
                encoding = mediaType.charset().name();
            }
            return encoding;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            logi("onPageStarted: " + url);
            synchronized (htmlSet) {
                htmlSet.add(url);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            logi("onPageFinished: " + url);
        }
    }

    private class JWebChromeClient extends WebChromeClient {

        @Override
        public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
            Log.i(TAG, "onJsBeforeUnload: " + url);
            return super.onJsBeforeUnload(view, url, message, result);
        }


    }

}

package com.coin.libbase.web;

import java.lang.ref.WeakReference;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/23
 * @description
 */

public class IWebViewJavaScriptInterface {

    protected WeakReference<JWebViewFragment> mFragment;

    public JWebViewFragment getWebViewFragment() {
        return this.mFragment.get();
    }

    public void setWebViewFragment(JWebViewFragment fragment) {
        this.mFragment = new WeakReference<JWebViewFragment>(fragment);
    }

}

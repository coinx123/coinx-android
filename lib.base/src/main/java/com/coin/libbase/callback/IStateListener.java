package com.coin.libbase.callback;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/21
 * @description
 */

public interface IStateListener {

    //点击"重试"回调
    public void onRetry();

    //点击"加载"回调
    public void onLoading();

    //点击"无数据"回调
    public void onEmpty();

}

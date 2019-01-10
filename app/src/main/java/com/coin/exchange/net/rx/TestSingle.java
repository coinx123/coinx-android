package com.coin.exchange.net.rx;

import android.util.Log;

import com.coin.exchange.utils.GsonUtils;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/2
 * @description
 */
public class TestSingle<T> implements SingleObserver<T> {
    private static final String TAG = "TestSingle";

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onSuccess(T t) {
        Log.i(TAG, "onSuccess: " + GsonUtils.getGson().toJson(t));
    }

    @Override
    public void onError(Throwable e) {
        Log.e(TAG, "onError: " + e.getMessage());
    }
}

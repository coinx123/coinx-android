package com.coin.libbase.net.rxjava;

import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.coin.libbase.model.ErrorBody;
import com.coin.libbase.model.ErrorCode;
import com.coin.libbase.net.ApiStatus;
import com.coin.libbase.presenter.BasePresenter;
import com.coin.libbase.utils.ToastUtil;

import java.io.IOException;
import java.lang.ref.WeakReference;

import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/20
 * @description
 */
public abstract class RxBaseSubscriber<T> {

    private static final String UNKNOWN_MSG = "网络异常，请稍后再试...";
    private static final String SEVER_ERROR_MSG = "服务器开小差...";

    private WeakReference<BasePresenter> mBasePresenter;

    public RxBaseSubscriber(BasePresenter basePresenter) {
        this.mBasePresenter = new WeakReference<>(basePresenter);
    }

    protected void checkThrowable(Throwable e) {
        e.printStackTrace();

        if (e instanceof HttpException) {
            ResponseBody body = ((HttpException) e).response().errorBody();
            int code = ((HttpException) e).code();

//            //400-499 的 错误
//            if (code >= 400 && code <= 499) {
//                onError(ApiStatus.CLIENT_ERROR, SEVER_ERROR_MSG);
//                return;
//            }
//
//            //500-599 的 错误
//            if (code >= 500 && code <= 505) {
//                onError(ApiStatus.SERVICE_ERROR, SEVER_ERROR_MSG);
//                return;
//            }

            //空响应体
            if (body == null) {
                onError(ApiStatus.HTTP_ERROR, UNKNOWN_MSG);
                return;
            }

            try {
                onError(code, body.string());
            } catch (IOException e1) {
                onError(ApiStatus.HTTP_ERROR, UNKNOWN_MSG);
            }

        } else {
            onError(ApiStatus.UNKNOWN, UNKNOWN_MSG);
        }
    }

    protected void addDisposable(Disposable d) {
        BasePresenter basePresenter = mBasePresenter.get();
        if (basePresenter != null) {
            basePresenter.getDisposable().add(d);
        }
    }

    /**
     * 错误回调
     */
    protected abstract void onError(int code, String message);

    /**
     * 成功回调
     *
     * @param value
     */
    protected abstract void onSuccessRes(T value);

}

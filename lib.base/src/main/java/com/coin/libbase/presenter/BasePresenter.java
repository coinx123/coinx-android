package com.coin.libbase.presenter;

import com.coin.libbase.view.IView;

import io.reactivex.disposables.CompositeDisposable;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/14
 * @description
 */
public class BasePresenter<V extends IView> implements IPresenter {

    protected String TAG = this.getClass().getSimpleName();

    private CompositeDisposable mDisposable;
    protected V mView;

    public BasePresenter() {
    }

    public BasePresenter(V mView) {
        this.mDisposable = new CompositeDisposable();
        this.mView = mView;
    }

    @Override
    public void onDestroy() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            // 保证activity结束时取消所有正在执行的订阅
            mDisposable.dispose();
        }
        mDisposable = null;
        mView = null;
    }

    public CompositeDisposable getDisposable() {
        return mDisposable;
    }

}

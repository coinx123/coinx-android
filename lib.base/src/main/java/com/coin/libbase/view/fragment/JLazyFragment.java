package com.coin.libbase.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.coin.libbase.presenter.BasePresenter;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/21
 * @description 懒加载的fragment
 */

public abstract class JLazyFragment<T extends BasePresenter> extends JBaseFragment<T> {

    //视图是否已经创建
    private boolean mIsCreateView = false;

    //当前的fragment是否可见
    protected boolean mIsVisible = false;

    protected boolean mIsFirst = true;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (!this.isLazyLoad()) {
            this.mIsVisible = true;
        }
        super.onViewCreated(view, savedInstanceState);
        this.mIsCreateView = true;
        logw("onViewCreated：mIsCreateView=" + this.mIsVisible + "; mIsVisible=" + this.mIsVisible);
        this.onVisible();

    }
    //是否要懒加载；true开启懒加载，false关闭懒加载
    protected boolean isLazyLoad() {
        return true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        logi("setUserVisibleHint------start");
        super.setUserVisibleHint(isVisibleToUser);

        this.mIsVisible = isVisibleToUser;
        if (this.mIsVisible) {
            logw("onViewCreated：mIsCreateView=" + this.mIsVisible + "; mIsVisible=" + this.mIsVisible);
            this.onVisible();
        } else {
            this.onHide();
        }

        logi("setUserVisibleHint------end");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        logi("onHiddenChanged------start");
        super.onHiddenChanged(hidden);

        this.mIsVisible = !hidden;
        if (this.mIsVisible) {// 在最前端显示 相当于调用了onResume();
            logw("onViewCreated：mIsCreateView=" + this.mIsVisible + "; mIsVisible=" + this.mIsVisible);
            onVisible();
        } else {              //不在最前端显示 相当于调用了onPause();
            onHide();
        }

        logi("onHiddenChanged------end");
    }

    protected void onHide() {
        logw("onHide");
    }

    private void onVisible() {
        logw("onVisible");

        if (!this.mIsVisible || !this.mIsCreateView) {
            return;
        }

        if (hasInitialized()) {   //已经初始化
            logw("createView");
            updateData();
        } else {                  //还未初始化
            logw("initData");
            initData();
        }

        onFragmentVisiable();

    }

    /**
     * 初始化数据
     * 当{@link JLazyFragment#hasInitialized()}返回了false，则说明还没有初始化过数据，则调用该方法；
     * 当{@link JLazyFragment#hasInitialized()}返回了true，则说明已经没有初始化过数据，则调用{@link JLazyFragment#updateData()}；
     */
    protected void initData() {
    }

    /**
     * 初始化数据
     * 当{@link JLazyFragment#hasInitialized()}返回了false，则说明还没有初始化过数据，则调用{@link JLazyFragment#initData()}；
     * 当{@link JLazyFragment#hasInitialized()}返回了true，则说明已经没有初始化过数据，则调用该方法；
     */
    protected void updateData() {
    }

    //是否已经初始化，默认为false
    protected boolean hasInitialized() {
        return false;
    }

    //每次fragment显示就会调用该方法
    protected void onFragmentVisiable() {
    }

}

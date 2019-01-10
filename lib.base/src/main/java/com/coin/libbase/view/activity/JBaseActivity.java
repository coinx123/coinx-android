package com.coin.libbase.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.coin.libbase.R;
import com.coin.libbase.presenter.BasePresenter;
import com.coin.libbase.model.CommonRes;
import com.coin.libbase.utils.FragmentCompat;
import com.coin.libbase.view.fragment.dialog.JCommonLoadingFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/20
 * @description activity 基类
 */

public abstract class JBaseActivity<T extends BasePresenter> extends AppCompatActivity {

    protected String TAG = this.getClass().getSimpleName();

    //装载FrameLayout的布局容器
    protected final static int COMMON_FRAME_LAYOUT = com.coin.libbase.R.layout.j_common_frame_layout;
    //装载JRecycleView的布局容器
    protected final static int COMMON_RECYCLE_VIEW_LAYOUT = com.coin.libbase.R.layout.j_common_recycle_view;
    //装载FrameLayout的布局容器中的FrameLayout的id
    protected final static int ID_FRAME_LAYOUT_CONTAINER = com.coin.libbase.R.id.frame_layout_container;
    //装载JRecycleView的布局容器中的JRecycleView的id
    protected final static int ID_RECYCLE_VIEW = com.coin.libbase.R.id.recycle_view;

    //当前存活的activity，用于关闭所有的activity
    private static final LinkedList<Activity> EXIST_ACTIVITIES = new LinkedList<>();

    protected Bundle mSavedInstanceState;

    protected static final int NONE = -1;
    private JCommonLoadingFragment loadingFragment;

    private Unbinder unbinder;

    // butterKnife
    protected static final int BUTTER_KNIFE = 0x001;
    // eventBus
    protected static final int EVENT_BUS = 0x002;

    @Inject
    @Nullable
    protected T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mSavedInstanceState = savedInstanceState;
        EXIST_ACTIVITIES.add(this);

        //传NONE，不进行设置视图的layout，可以由子类自行操作
        if (getLayout() != NONE) {
            setContentView(getLayout());
        }

        registerDagger();
        registerLogic();
        setStatusBarColor(R.color.jColorStateBar);

        initIntent(getIntent());
        initView();
        initData();

    }

    // dagger注入
    protected void registerDagger() {

    }

    @Override
    protected void onDestroy() {

        EXIST_ACTIVITIES.remove(this);
        unregisterLogic();

        // 释放 presenter
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        mPresenter = null;

        super.onDestroy();
    }

    /**
     * 退出，清空所有的存活activity
     */
    public void exit() {
        Iterator<Activity> activityIterator = EXIST_ACTIVITIES.iterator();
        while (activityIterator.hasNext()) {
            Activity next = activityIterator.next();
            activityIterator.remove();
            next.finish();
        }
    }

    /**
     * 当前activity是否为用户所见的activity
     *
     * @return true：当前activity是用户显示的activity
     */
    private boolean isCurActivity() {
        return EXIST_ACTIVITIES.getLast() == this;
    }

    /**
     * 添加一个fragment
     *
     * @param containerId 容器id
     * @param fragment    现实的fragment
     */
    public void addLayerFragment(int containerId, Fragment fragment) {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(fragment);
        addLayerFragment(containerId, fragments, 0);
    }

    /**
     * 添加 fragment 列表，显示下标为0的fragment
     *
     * @param containerId 容器id
     * @param fragments   容器列表
     */
    public void addLayerFragment(int containerId, List<Fragment> fragments) {
        addLayerFragment(containerId, fragments, 0);
    }

    /**
     * 添加 fragment 列表，显示指定下标
     *
     * @param containerId  容器id
     * @param fragments    fragment的列表
     * @param showPosition 显示固定的下标
     */
    public void addLayerFragment(int containerId,
                                 List<Fragment> fragments,
                                 int showPosition) {
        if (fragments == null) {
            fragments = new ArrayList<>();
        }
        if (mSavedInstanceState == null) {
            FragmentCompat.Layer.init(getSupportFragmentManager(), containerId, showPosition, fragments);
        } else {
            FragmentCompat.Layer.restoreInstance(getSupportFragmentManager(), fragments);
        }
    }

    /**
     * 切换fragment
     *
     * @param from 当前的fragment
     * @param to   前往的fragment
     */
    public void toggleLayerFragment(Fragment from, Fragment to) {
        FragmentCompat.Layer.toggle(getSupportFragmentManager(), from, to);
    }

    //获取视图layout的id
    protected abstract int getLayout();

    //初始化intent
    protected abstract void initIntent(Intent intent);

    //初始化视图的View
    protected abstract void initView();

    //初始化数据
    protected abstract void initData();


    //=======================状态栏控制===========================

    /**
     * 设置状态栏颜色
     */
    protected void setStatusBarColor(int color) {
        // 5.0 以前，不做处理
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        Window window = getWindow();
        int realColor = ContextCompat.getColor(this, color);

        // 5.0 和 5.1 没办法设置状态栏的颜色，所以加灰色遮罩
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            window.setStatusBarColor(getObscuredColor(realColor));
            return;
        }

        // 6.0 及以后
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(realColor);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

    }

    /**
     * 加灰度
     *
     * @param c 设置的色值
     * @return 加灰度后的色值
     */
    protected int getObscuredColor(int c) {
        float[] hsv = new float[3];
        int color = c;
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.85f; // value component
        color = Color.HSVToColor(hsv);
        return color;
    }

    //===========================================================
    //======================Loading 的 Fragment==================

    public void showDialog() {
        if (loadingFragment == null) {
            loadingFragment = new JCommonLoadingFragment();
        }
        loadingFragment.show(this);
    }

    public void hideDialog() {
        if (loadingFragment != null) {
            loadingFragment.dismiss();
        }
    }

    //===========================================================

    //====================是否需要butterKnife=====================

    /**
     * 返回需要注册的类型，默认注册 ButterKnife 、 EventBus
     *
     * @return 用或（|）连接的值；如果只需要ButterKnife，则只返回{@link #BUTTER_KNIFE}
     */
    protected int getInitRegister() {
        return BUTTER_KNIFE | EVENT_BUS;
    }

    /**
     * 注册一些第三方框架
     */
    private void registerLogic() {

        // 注册 ButterKnife
        if (isContain(BUTTER_KNIFE)) {
            unbinder = ButterKnife.bind(this);
        }

        // 注册 EventBus
        if (isContain(EVENT_BUS)) {
            EventBus.getDefault().register(this);
        }

    }

    /**
     * 注销一些第三方框架
     */
    private void unregisterLogic() {

        // 注册 EventBus
        if (isContain(EVENT_BUS)) {
            EventBus.getDefault().unregister(this);
        }

        if (isContain(BUTTER_KNIFE) && unbinder != null) {
            unbinder.unbind();
        }

    }

    /**
     * 确认是否含有某个标记
     *
     * @param tag 标记
     * @return true：包含；false：不包含
     */
    private boolean isContain(int tag) {
        return (getInitRegister() & tag) == tag;
    }

//    @Subscribe
//    public void onWebSocket(CommonRes commonRes) {
//        onMessage(commonRes);
//    }
//
//    protected void onMessage(CommonRes commonRes) {
//    }

    @Subscribe
    public void onWebSocket(List<CommonRes> commonResList) {
        onMessage(commonResList);
    }

    protected void onMessage(List<CommonRes> commonResList) {
    }
    //===========================================================
}

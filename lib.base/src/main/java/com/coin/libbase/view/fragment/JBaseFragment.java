package com.coin.libbase.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coin.libbase.R;
import com.coin.libbase.config.JFrameManager;
import com.coin.libbase.presenter.BasePresenter;
import com.coin.libbase.model.CommonRes;
import com.coin.libbase.view.fragment.dialog.JCommonLoadingFragment;
import com.coin.libbase.widget.StateLayout;
import com.coin.libbase.utils.FragmentCompat;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/21
 * @description 对fragment的最基础封装
 */

public abstract class JBaseFragment<T extends BasePresenter> extends Fragment {

    protected String TAG = this.getClass().getSimpleName();

    protected Bundle mSaveInstanceState;

    //装载FrameLayout的布局容器
    protected final static int COMMON_FRAME_LAYOUT = R.layout.j_common_frame_layout;
    //装载JRecycleView的布局容器
    protected final static int COMMON_RECYCLE_VIEW_LAYOUT = R.layout.j_common_recycle_view;
    //装载FrameLayout的布局容器中的FrameLayout的id
    protected final static int ID_FRAME_LAYOUT_CONTAINER = R.id.frame_layout_container;
    //装载JRecycleView的布局容器中的JRecycleView的id
    protected final static int ID_RECYCLE_VIEW = R.id.recycle_view;

    private JCommonLoadingFragment loadingFragment;

    private Unbinder unbinder;

    // butterKnife
    protected static final int BUTTER_KNIFE = 0x001;
    // eventBus
    protected static final int EVENT_BUS = 0x002;

    @Inject
    protected T mPresenter;

    @Override
    public void onAttach(Context context) {
        logi("onAttach------start");
        super.onAttach(context);

        Bundle arguments = getArguments();
        if (arguments != null) {
            initArgs(arguments);
        }
        logi("onAttach------end");
    }

    //初始化参数
    protected void initArgs(Bundle arguments) {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        logi("onCreate------start");
        super.onCreate(savedInstanceState);

        this.mSaveInstanceState = savedInstanceState;

        logi("onCreate------end");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        logi("onCreateView------start");
        View fragmentView = onCreateFragmentView(inflater, container, savedInstanceState);
        registerDagger();
        registerLogic(fragmentView);
        logi("onCreateView------end");
        return fragmentView;
    }

    /**
     * {@link JBaseFragment#onCreateView(LayoutInflater, ViewGroup, Bundle)} 与其相同 只是用于父类JFragment中包装
     */
    protected abstract View onCreateFragmentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    // 包装fragment，添加多状态
    protected View wrapFragmentView(View view) {
        StateLayout stateLayout = new StateLayout(getActivity());
        stateLayout.setContentView(view);
        stateLayout.setRetryView(JFrameManager.getInstance().getRetryViewLayout());
        stateLayout.setEmptyView(JFrameManager.getInstance().getEmptyViewLayout());
        stateLayout.setLoadingView(JFrameManager.getInstance().getLoadingViewLayout());
        return stateLayout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        logi("onViewCreated------start");
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        logi("onViewCreated------end");
    }

    protected abstract void initView(View view);

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        logi("onActivityCreated------start");
        super.onActivityCreated(savedInstanceState);
        logi("onActivityCreated------end");
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        logi("onViewStateRestored------start");
        super.onViewStateRestored(savedInstanceState);
        logi("onViewStateRestored------end");
    }

    @Override
    public void onStart() {
        logi("onStart------start");
        super.onStart();
        logi("onStart------end");
    }

    @Override
    public void onResume() {
        logi("onResume------start");
        super.onResume();
        logi("onResume------end");
    }

    @Override
    public void onPause() {
        logi("onPause------start");
        super.onPause();
        logi("onPause------end");
    }

    @Override
    public void onStop() {
        logi("onStop------start");
        super.onStop();
        logi("onStop------end");
    }

    @Override
    public void onDestroyView() {
        logi("onDestroyView------start");
        super.onDestroyView();
        logi("onDestroyView------end");
    }

    @Override
    public void onDestroy() {
        logi("onDestroy------start");
        unregisterLogic();

        // 释放 presenter
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        mPresenter = null;
        super.onDestroy();
        logi("onDestroy------end");
    }

    @Override
    public void onDetach() {
        logi("onDetach------start");
        super.onDetach();
        logi("onDetach------end");
    }

    protected String getUniqueId() {
        return Integer.toHexString(System.identityHashCode(this));
    }

    protected String getFragmentTag() {
        if (TextUtils.isEmpty(getMyTag())) {
            return getClass().getSimpleName() + "{" + getUniqueId() + "}";
        } else {
            return getClass().getSimpleName() + "{" + getMyTag() + "}";
        }
    }

    protected String getMyTag() {
        return "";
    }

    protected void logi(String msg) {
        if (JFrameManager.getInstance().isDebug()) {
            Log.i(getFragmentTag(), msg);
        }
    }

    protected void logw(String msg) {
        if (JFrameManager.getInstance().isDebug()) {
            Log.e(getFragmentTag(), msg);
        }
    }

    protected void loge(String msg) {
        if (JFrameManager.getInstance().isDebug()) {
            Log.e(getFragmentTag(), msg);
        }
    }

    /**
     * 判断当前fragment是否消费回退事件
     *
     * @param fragmentManager 上级fragment 的管理
     * @return
     */
    public boolean onConsumeBackEvent(FragmentManager fragmentManager) {
        boolean consume = FragmentCompat.isConsumeBackEvent(getChildFragmentManager());
        if (consume) {
            //子fragment消费成功以后，判断当前fragment是否还有子fragment，没有的话，看情况决定是否关闭
            if (getChildFragmentManager().getBackStackEntryCount() == 0 && noChild2Finish()) {
                return fragmentManager.popBackStackImmediate();
            }
            return true;
        } else {
            //子fragment未消费回退事件,则由当前fragment进行消费
            return fragmentManager.popBackStackImmediate();
        }
    }

    /**
     * 当 FragmentManager 内的子fragment数为0时，判断是否关闭当前 Fragment
     *
     * @return true：需要关闭；false：不需要关闭
     */
    protected boolean noChild2Finish() {
        return true;
    }

    /**
     * 添加单个fragment
     *
     * @param containerId 容器id
     * @param fragment    显示的fragment
     */
    public void addLayerFragment(int containerId, Fragment fragment) {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(fragment);
        addLayerFragment(containerId, fragments, 0);
    }

    /**
     * 添加 Fragment 列表，显示第一个
     *
     * @param containerId 容器id
     * @param fragments   fragment列表
     */
    public void addLayerFragment(int containerId, List<Fragment> fragments) {
        addLayerFragment(containerId, fragments, 0);
    }

    /**
     * 添加 Fragment 列表，显示 showPosition
     *
     * @param containerId  容器id
     * @param fragments    fragment列表
     * @param showPosition 显示的下表
     */
    public void addLayerFragment(int containerId, List<Fragment> fragments, int showPosition) {
        if (this.mSaveInstanceState == null) {
            FragmentCompat.Layer.init(getChildFragmentManager(), containerId, showPosition, fragments);
        } else {
            FragmentCompat.Layer.restoreInstance(getChildFragmentManager(), fragments);
        }
    }

    /**
     * 切换fragment
     *
     * @param from 当前显示的fragment
     * @param to   将要显示的fragment
     */
    public void toggleLayerFragment(Fragment from, Fragment to) {
        FragmentCompat.Layer.toggle(getChildFragmentManager(), from, to);
    }

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
     * dagger注入
     */
    protected void registerDagger() {

    }

    /**
     * 注册一些第三方框架
     */
    private void registerLogic(View fragmentView) {

        // 注册 ButterKnife
        if (isContain(BUTTER_KNIFE)) {
            unbinder = ButterKnife.bind(this, fragmentView);
        }

        // 注册 EventBus
        if (isContain(EVENT_BUS)) {
            if (!EventBus.getDefault().isRegistered(this)) {//重复注册会崩溃
                EventBus.getDefault().register(this);
            }
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
//
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

package com.coin.libbase.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coin.libbase.presenter.BasePresenter;
import com.zinc.jrecycleview.JRecycleView;
import com.zinc.jrecycleview.adapter.JRefreshAndLoadMoreAdapter;
import com.coin.libbase.adapter.JFrameAdapter;
import com.coin.libbase.callback.IStateListener;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/21
 * @description 带刷新的listFragment
 */

public abstract class JListFragment<T extends BasePresenter> extends JLazyFragment<T> implements IStateListener {

    protected JRecycleView mRecycleView;

    //包装adapter
    protected JFrameAdapter _mAdapter;

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(COMMON_RECYCLE_VIEW_LAYOUT, container, false);
    }

    @Override
    protected void initView(View view) {
        this.mRecycleView = view.findViewById(ID_RECYCLE_VIEW);
        this.initRecycleView(this.mRecycleView);
        this.setAdapter(getAdapter());
    }

    protected void initRecycleView(JRecycleView recycleView) {
        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (isShowDivider()) {
            recycleView.addItemDecoration(new DividerItemDecoration(getContext(), 1));
        }
        recycleView.setItemAnimator(new DefaultItemAnimator());
    }

    private void setAdapter(RecyclerView.Adapter adapter) {
        this._mAdapter = new JFrameAdapter(getContext(), adapter);

        this._mAdapter.setIsOpenRefresh(requestRefresh());
        if (requestRefresh()) {
            this._mAdapter.setOnRefreshListener(new JRefreshAndLoadMoreAdapter.OnRefreshListener() {
                @Override
                public void onRefreshing() {
                    JListFragment.this.getFirstData();
                }
            });
        }

        this._mAdapter.setIsOpenLoadMore(false);
        this._mAdapter.setStateListener(this);

        this.initAdapterForChild(adapter);


        this.mRecycleView.setAdapter(this._mAdapter);

    }

    //获取真正的adapter
    protected abstract RecyclerView.Adapter getAdapter();

    //暴露给子类初始化adapter
    protected void initAdapterForChild(RecyclerView.Adapter adapter) {
    }

    @Override
    protected void initData() {
        super.initData();
        this._mAdapter.onLoading();
        this.getFirstData();
    }

    @Override
    protected boolean hasInitialized() {
        if (this._mAdapter == null) {
            return false;
        }
        return !this._mAdapter.isState();
    }

    public JFrameAdapter getRootAdapter() {
        return _mAdapter;
    }

    //是否需要刷新
    protected boolean requestRefresh() {
        return true;
    }

    //是否需要分割线
    protected boolean isShowDivider() {
        return false;
    }

    @Override
    public void onLoading() {
        logw("已经正在刷新......");
    }

    @Override
    public void onEmpty() {
        getFirstData();
    }

    @Override
    public void onRetry() {
        getFirstData();
    }

    //第一次获取数据
    public abstract void getFirstData();

}

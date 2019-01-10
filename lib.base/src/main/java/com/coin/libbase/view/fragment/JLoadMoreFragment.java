package com.coin.libbase.view.fragment;

import android.support.v7.widget.RecyclerView;

import com.coin.libbase.presenter.BasePresenter;
import com.zinc.jrecycleview.adapter.JRefreshAndLoadMoreAdapter;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/22
 * @description 带上拉加载和刷新的fragment
 */

public abstract class JLoadMoreFragment<T extends BasePresenter> extends JListFragment<T> {

    //暴露给子类初始化adapter
    protected void initAdapterForChild(RecyclerView.Adapter adapter) {
        this._mAdapter.setIsOpenLoadMore(requestLoadMore());
        if(requestLoadMore()){
            this._mAdapter.setOnLoadMoreListener(new JRefreshAndLoadMoreAdapter.OnLoadMoreListener() {
                @Override
                public void onLoading() {
                    getLoadMoreData();
                }
            });
        }
    }

    @Override
    protected boolean requestRefresh() {
        return true;
    }

    protected boolean requestLoadMore(){
        return true;
    }

    public abstract void getLoadMoreData();

}

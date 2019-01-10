package com.coin.exchange.view.fragment.trade.delegation;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.coin.exchange.context.AppApplication;
import com.coin.exchange.adapter.DelegationAdapter;
import com.coin.exchange.di.component.DaggerDelegationContentComponent;
import com.coin.exchange.di.module.DelegationContentModule;
import com.coin.exchange.model.okex.vo.DelegationItemVO;
import com.coin.exchange.presenter.DelegationContentPresenter;
import com.coin.exchange.utils.AppUtils;
import com.coin.libbase.utils.ToastUtil;
import com.coin.libbase.view.fragment.JLoadMoreFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/26
 * @description 交易界面 —— 委托fragment —— 内容fragment
 */

public class DelegationContentFragment extends JLoadMoreFragment<DelegationContentPresenter>
        implements DelegationContentView, DelegationAdapter.ItemClickListener {

    // 一页最多的数据
    private static final int PAGE_COUNT = 100;

    private static final String INS_ID = "insId";
    private static final String STATUS = "status";
    private static final String TYPE = "type";

    // 币种id
    private String mInsId;
    // 状态：WAITING、DONE、CANCEL
    private String mStatus;
    // 类型：当周、次周、当季
    private String mType;
    // 开始页面
    private int mFrom;

    // 平台
    private String mPlatform;

    private final List<DelegationItemVO> mData = new ArrayList<>();

    public static DelegationContentFragment newInstance(String insId,
                                                        String status,
                                                        String type,
                                                        String platform) {

        Bundle bundle = new Bundle();
        bundle.putString(INS_ID, insId);
        bundle.putString(STATUS, status);
        bundle.putString(TYPE, type);
        bundle.putString(AppUtils.PLATFORM, platform);

        DelegationContentFragment fragment = new DelegationContentFragment();
        fragment.initArgs(bundle);

        return fragment;

    }

    @Override
    protected void initArgs(Bundle arguments) {
        mInsId = arguments.getString(INS_ID);
        mStatus = arguments.getString(STATUS);
        mType = arguments.getString(TYPE);
        mPlatform = arguments.getString(AppUtils.PLATFORM);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new DelegationAdapter(getContext(), mData, this);
    }

    @Override
    protected boolean requestLoadMore() {
        if (mPlatform.equals(AppUtils.BITMEX)) {
            return false;
        }
        return true;
    }

    @Override
    protected void registerDagger() {
        DaggerDelegationContentComponent
                .builder()
                .appComponent(AppApplication.getAppComponent())
                .delegationContentModule(new DelegationContentModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    public void getFirstData() {
        mFrom = 0;
        if (mPlatform.equals(AppUtils.BITMEX)) {
            mPresenter.getCommissionInfo(mStatus, PAGE_COUNT);
        } else if (mPlatform.equals(AppUtils.OKEX)) {
            mPresenter.getFutureInfo(mInsId, mStatus, mFrom, mType);
        }
    }

    @Override
    public void onGetDelegationListSuc(List<DelegationItemVO> list, boolean isFirst) {

        getRootAdapter().onSuccess();

        if (list.size() == 0 && mData.size() == 0) {    // 完全没数据，显示空
            getRootAdapter().onEmpty();
            return;
        }

        if (mPlatform.equals(AppUtils.BITMEX)) {
            mData.clear();
            mData.addAll(list);
            getRootAdapter().notifyDataSetChanged();
            return;
        } else {
            ++mFrom;

            // 如果是第一次，则进行清除
            if (isFirst) {
                mData.clear();
            }
            mData.addAll(list);

            if (list.size() < PAGE_COUNT) {          // 数据不够 100 ，显示没有更多
                getRootAdapter().setNoMore();
            }

            getRootAdapter().notifyDataSetChanged();
        }


    }

    @Override
    public void onGetDelegationListError() {
        getRootAdapter().onError();
    }

    @Override
    public void onCancelError() {
        hideDialog();
        ToastUtil.show("撤销失败，请重新撤销");
    }

    @Override
    public void onCancelSuc(String orderId, int position) {

        DelegationItemVO itemVO = mData.get(position);
        if (itemVO.getOrderId().equals(orderId)) {
            mData.remove(position);
        }

        if (mData.isEmpty()) {
            getRootAdapter().onEmpty();
        } else {
            getRootAdapter().notifyDataSetChanged();
        }

        hideDialog();
    }

    @Override
    public void getLoadMoreData() {
        if (mPlatform.equals(AppUtils.BITMEX)) {

        } else if (mPlatform.equals(AppUtils.OKEX)) {
            mPresenter.getFutureInfo(mInsId, mStatus, mFrom, mType);
        }
    }

    /**
     * 进行请求
     */
    public void sendReq(String insId, String status, String type) {

        getRootAdapter().resetLoadMore();
        getRootAdapter().onLoading();

        mInsId = insId;
        mStatus = status;
        mType = type;
        mFrom = 0;

        mData.clear();

        getFirstData();
    }

    @Override
    public void onClickCancel(String orderId, String insId, int position) {
        showDialog();
        if(mPlatform.equals(AppUtils.BITMEX)){
            mPresenter.cancelBitMexOrder(orderId, position);
        }else if(mPlatform.equals(AppUtils.OKEX)){
            mPresenter.cancelOrder(orderId, insId, position);
        }
    }
}

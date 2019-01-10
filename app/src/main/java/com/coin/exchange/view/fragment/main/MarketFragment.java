package com.coin.exchange.view.fragment.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coin.exchange.context.AppApplication;
import com.coin.exchange.R;
import com.coin.exchange.adapter.MarketAdapter;
import com.coin.exchange.config.FragmentConfig;
import com.coin.exchange.di.component.DaggerMarketComponent;
import com.coin.exchange.di.module.MarketModule;
import com.coin.exchange.model.okex.vo.HotCoinItemVO;
import com.coin.exchange.model.okex.vo.RankItemVO;
import com.coin.exchange.model.okex.vo.RankStateVO;
import com.coin.exchange.presenter.MarketPresenter;
import com.coin.exchange.utils.AppUtils;
import com.coin.exchange.view.MainActivity;
import com.coin.libbase.utils.StatusBarUtil;
import com.coin.libbase.utils.ToastUtil;
import com.coin.libbase.utils.UIUtils;
import com.coin.libbase.view.fragment.JBaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/9
 * @description 行情
 */

public class MarketFragment extends JBaseFragment<MarketPresenter>
        implements MarketView, MarketAdapter.ClickListener {

    private static final int REQ_COUNT = 3;
    private int haveReceive = 0;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycle_view)
    RecyclerView recyclerView;

    private static int THRESHOLD_Y = 0;
    private int delayY = 0;

    private MarketAdapter mMarketAdapter;
    private final List<RankItemVO> mOkExRankList = new ArrayList<>();
    private final RankStateVO mOkExRankState = new RankStateVO(RankStateVO.LOADING, RankStateVO.LOADING_MSG);
    private final List<RankItemVO> mBitMexRankList = new ArrayList<>();
    private final RankStateVO mBitMexRankState = new RankStateVO(RankStateVO.LOADING, RankStateVO.LOADING_MSG);

    public static Fragment newInstance() {
        return new MarketFragment();
    }

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_market, container, false);
    }

    @Override
    protected void initView(View view) {
        THRESHOLD_Y = UIUtils.dip2px(getContext(), 186.5f) -
                StatusBarUtil.getStatusBarHeight(getContext());

        mMarketAdapter = new MarketAdapter(getContext());

        mMarketAdapter.setListener(this);
        setRankContent();

        refreshLayout.setColorSchemeResources(R.color.swipe_color_1,
                R.color.swipe_color_2,
                R.color.swipe_color_3,
                R.color.swipe_color_4);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reqData();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mMarketAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                Log.i(TAG, "onScrolled: [dx:" + dx + "; dy:" + dy + "]");

                delayY += dy;

                if (delayY > THRESHOLD_Y) {
                    if (getActivity() instanceof MainActivity) {
                        MainActivity mainActivity = (MainActivity) getActivity();
                        StatusBarUtil.setLightMode(mainActivity);
                    }
                } else {
                    if (getActivity() instanceof MainActivity) {
                        MainActivity mainActivity = (MainActivity) getActivity();
                        StatusBarUtil.setDarkMode(mainActivity);
                    }
                }

            }
        });

        reqData();

    }

    @Override
    protected void registerDagger() {
        DaggerMarketComponent
                .builder()
                .appComponent(AppApplication.getAppComponent())
                .marketModule(new MarketModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onGetHotCoinInfoList(List<HotCoinItemVO> hotCoinItemVOList) {
        mMarketAdapter.setHotCoinItemVOList(hotCoinItemVOList);
        onCloseRefresh();
    }

    @Override
    public void onGetHotCoinInfoListError() {
        ToastUtil.show("热门货币获取失败，请下拉刷新");
        onCloseRefresh();
    }

    @Override
    public void onGetOkExRankListInfo(List<RankItemVO> rankItemVOList) {
        mOkExRankList.clear();
        mOkExRankList.addAll(rankItemVOList);
        mOkExRankState.setState(RankStateVO.SUCCESS);
        mOkExRankState.setStateMsg(RankStateVO.SUCCESS_MSG);
        reqUpdateRank(FragmentConfig.OKEX);
    }

    @Override
    public void onGetOkExError() {
        mOkExRankList.clear();
        mOkExRankState.setState(RankStateVO.ERROR);
        mOkExRankState.setStateMsg(RankStateVO.ERROR_MSG);
        reqUpdateRank(FragmentConfig.OKEX);
    }

    @Override
    public void onGetBitMexRankListInfo(List<RankItemVO> rankItemVOList) {
        mBitMexRankList.clear();
        mBitMexRankList.addAll(rankItemVOList);

        mBitMexRankState.setState(RankStateVO.SUCCESS);
        mBitMexRankState.setStateMsg(RankStateVO.SUCCESS_MSG);
        reqUpdateRank(FragmentConfig.BITMEX);
    }

    @Override
    public void onGetBitMexError() {
        mBitMexRankList.clear();

        mBitMexRankState.setState(RankStateVO.ERROR);
        mBitMexRankState.setStateMsg(RankStateVO.ERROR_MSG);
        reqUpdateRank(FragmentConfig.BITMEX);
    }

    /**
     * 如果当前显示的是对应的排行榜则进行刷新
     */
    private void reqUpdateRank(int type) {
        setRankContent();
        onCloseRefresh();
    }

    /**
     * 装载对应的数据
     */
    private void setRankContent() {
        for (int i = 0; i < FragmentConfig.getRankNav().size(); ++i) {
            if (FragmentConfig.getRankNav().get(i).isSelect()) {
                switch (i) {
                    case FragmentConfig.BITMEX:
                        if (mBitMexRankState.getState() != RankStateVO.SUCCESS) {
                            mBitMexRankList.clear();
                        }
                        mMarketAdapter.setRankItemVOList(AppUtils.BITMEX, mBitMexRankList);
                        mMarketAdapter.setRankState(mBitMexRankState);
                        break;
                    case FragmentConfig.OKEX:
                        if (mOkExRankState.getState() != RankStateVO.SUCCESS) {
                            mOkExRankList.clear();
                        }
                        mMarketAdapter.setRankItemVOList(AppUtils.OKEX, mOkExRankList);
                        mMarketAdapter.setRankState(mOkExRankState);
                        break;
                }
            }
        }
    }

    private void reqData() {
        haveReceive = 0;
        refreshLayout.setRefreshing(true);
        mPresenter.getHotCoinInfoList();
        mPresenter.getOkExRankInfoList();
        mPresenter.getBitMexRankInfoList();
    }

    private void onCloseRefresh() {
        ++haveReceive;
        if (haveReceive >= REQ_COUNT) {
            mMarketAdapter.notifyDataSetChanged();
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onClick() {
        setRankContent();
        mMarketAdapter.notifyDataSetChanged();
//        mMarketAdapter.notifyItemRangeChanged(MarketAdapter.HEADER_COUNT, mMarketAdapter.getItemCount()-MarketAdapter.HEADER_COUNT);
    }

}

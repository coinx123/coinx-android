package com.coin.exchange.view.fragment.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coin.exchange.R;
import com.coin.exchange.config.FragmentConfig;
import com.coin.exchange.model.okex.vo.MenuItemVO;
import com.coin.libbase.view.fragment.JLazyFragment;
import com.coin.libbase.widget.NoScrollViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author dean
 * @date 创建时间：2018/11/8
 * @description
 */
public class TradeFragment extends JLazyFragment {

    @BindView(R.id.tv_bitmex)
    TextView tvBitmex;
    @BindView(R.id.tv_bitmex_line)
    TextView tvBitmexLine;
    @BindView(R.id.tv_okex)
    TextView tvOkex;
    @BindView(R.id.tv_okex_line)
    TextView tvOkexLine;
    @BindView(R.id.trade_view_pager)
    NoScrollViewPager mViewPager;
    private int currency = 1;

    public static Fragment newInstance() {
        return new TradeFragment();
    }

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trade, container, false);
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initData() {
        final ArrayList<MenuItemVO> tradeNav = FragmentConfig.getTradeNav();
//        mViewPager.setNoScroll(false);//设置是否可以滑动
        mViewPager.setAdapter(new TradePagerAdapter(getChildFragmentManager()) {
            @Override
            public CharSequence getPageTitle(int position) {
                return tradeNav.get(position).getName();
            }
        });
        // 设置不会回收的数
        mViewPager.setOffscreenPageLimit(tradeNav.size());
        if (currency == 1) {
            mViewPager.setCurrentItem(1);
            currency = 1;
        }else {
            mViewPager.setCurrentItem(0);
            currency = 0;
        }
    }

    @OnClick({R.id.rl_bitmex_icon, R.id.rl_okex_icon})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.rl_bitmex_icon:
                mViewPager.setCurrentItem(0);
                currency = 0;
                tvOkexLine.setVisibility(View.GONE);
                tvBitmexLine.setVisibility(View.VISIBLE);
                tvOkex.setTextColor(getResources().getColor(R.color.time_blue));
                tvBitmex.setTextColor(getResources().getColor(R.color.blue));
                break;
            case R.id.rl_okex_icon:
                mViewPager.setCurrentItem(1);
                currency = 1;
                tvOkexLine.setVisibility(View.VISIBLE);
                tvBitmexLine.setVisibility(View.GONE);
                tvOkex.setTextColor(getResources().getColor(R.color.blue));
                tvBitmex.setTextColor(getResources().getColor(R.color.time_blue));
                break;
        }
    }

    public class TradePagerAdapter extends FragmentPagerAdapter {

        TradePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return FragmentConfig.getTradeFragment(position);
        }

        @Override
        public int getCount() {
            return FragmentConfig.getTradeNav().size();
        }
    }
}

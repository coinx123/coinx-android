package com.coin.exchange.view;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.ImageView;

import com.coin.exchange.R;
import com.coin.exchange.config.FragmentConfig;
import com.coin.exchange.model.okex.vo.MenuItemVO;
import com.coin.exchange.utils.IndicatorLineUtil;
import com.coin.libbase.view.activity.JBaseActivity;
import com.coin.libbase.widget.NoScrollViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author dean
 * @date 创建时间：2018/11/19
 * @description 交易界面
 */
public class TradeActivity extends JBaseActivity {
    @BindView(R.id.iv_trade_back)
    ImageView ivTradeBack;
    @BindView(R.id.trade_tab)
    TabLayout tradeTab;
    @BindView(R.id.trade_view_pager)
    NoScrollViewPager tradeViewPager;

    private String insId;
    private String type;
    private String from;

    @Override
    protected int getLayout() {
        return R.layout.activity_trade;
    }

    @Override
    protected void initIntent(Intent intent) {
        insId = intent.getStringExtra(FragmentConfig.INSTRUMENT_ID);
        type = intent.getStringExtra(FragmentConfig.TYPE);
        from = intent.getStringExtra(FragmentConfig.FROM);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        final ArrayList<MenuItemVO> tradeActivityNav = FragmentConfig.getTradeActivityNav();
//        tradeViewPager.setNoScroll(false);//设置是否可以滑动
        tradeViewPager.setAdapter(new TradeActivityPagerAdapter(getSupportFragmentManager()) {
            @Override
            public CharSequence getPageTitle(int position) {
                return tradeActivityNav.get(position).getName();
            }
        });
        tradeTab.post(new Runnable() { //设置指示器长度
            @Override
            public void run() {
                IndicatorLineUtil.setIndicator(tradeTab, 15, 15);
            }
        });
        //绑定
        tradeTab.setupWithViewPager(tradeViewPager);
        // 设置不会回收的数
        tradeViewPager.setOffscreenPageLimit(tradeActivityNav.size());
    }

    @OnClick({R.id.iv_trade_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_trade_back:
                finish();
                break;
        }
    }

    public class TradeActivityPagerAdapter extends FragmentPagerAdapter {

        TradeActivityPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return FragmentConfig.getTradeActivityFragment(position,from);
        }

        @Override
        public int getCount() {
            return FragmentConfig.getTradeActivityNav().size();
        }
    }

    // 获取当前也的instrumentId
    public String getInsId() {
        return insId;
    }

    // 获取当前也的type
    public String getType() {
        return type;
    }
}

package com.coin.exchange.view.fragment.main.trade;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coin.exchange.R;
import com.coin.exchange.config.FragmentConfig;
import com.coin.exchange.model.okex.vo.MenuItemVO;
import com.coin.exchange.utils.AppUtils;
import com.coin.libbase.model.eventbus.Event;
import com.coin.libbase.view.fragment.JBaseFragment;
import com.coin.libbase.widget.NoScrollViewPager;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author dean
 * @date 创建时间：2018/11/8
 * @description
 */
public class TradeOkAndBitFragment extends JBaseFragment {
    private static final String FROM = "from";
    @BindView(R.id.tv_okex_trade_optional)
    TextView tvOkexTradeOptional;
    @BindView(R.id.tv_okex_trade_all)
    TextView tvOkexTradeAll;
    @BindView(R.id.okex_trade_view_pager)
    NoScrollViewPager mViewPager;
    @BindView(R.id.ll_show_collection)
    LinearLayout ll_show_collection;
    @BindView(R.id.ll_gone_collection)
    LinearLayout ll_gone_collection;
    private ActionListener mListener;
    private String from = "";
    private int currency = 0;

    public static TradeOkAndBitFragment newInstance(String from) {
        TradeOkAndBitFragment fragment = new TradeOkAndBitFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FROM, from);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trade_okex_bitmex, container, false);
    }

    @Override
    protected void initView(View view) {
        Bundle arguments = getArguments();
        from = arguments.getString(FROM);
        if (from.contains("trade")) {//含有"trade"代表下单界面进入的，影藏右边和谐边框
            from = from.replace("trade", "");
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ll_show_collection.getLayoutParams();
            params.setMargins(0, 0, AppUtils.dpToPx(50), 0);
            ll_show_collection.setLayoutParams(params);
            ll_gone_collection.setVisibility(View.VISIBLE);
        }

        final ArrayList<MenuItemVO> okTradeNav = FragmentConfig.getOKTradeNav();

        mViewPager.setAdapter(new OkTradePagerAdapter(getChildFragmentManager()) {
            @Override
            public CharSequence getPageTitle(int position) {
                return okTradeNav.get(position).getName();
            }
        });
        // 设置不会回收的数
        mViewPager.setOffscreenPageLimit(okTradeNav.size());
        if (currency != 0 && mViewPager != null
                && tvOkexTradeOptional != null
                && tvOkexTradeAll != null) {
            mViewPager.setCurrentItem(currency);
            tvOkexTradeOptional.setTextColor(getResources().getColor(R.color.time_blue));
            tvOkexTradeAll.setTextColor(getResources().getColor(R.color.blue));
        }
    }

    @OnClick({R.id.tv_okex_trade_optional, R.id.tv_okex_trade_all, R.id.ll_gone_collection})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_okex_trade_optional:
                mViewPager.setCurrentItem(0);
                currency = 0;
                tvOkexTradeOptional.setTextColor(getResources().getColor(R.color.blue));
                tvOkexTradeAll.setTextColor(getResources().getColor(R.color.time_blue));
                break;
            case R.id.tv_okex_trade_all:
                mViewPager.setCurrentItem(1);
                currency = 1;
                tvOkexTradeOptional.setTextColor(getResources().getColor(R.color.time_blue));
                tvOkexTradeAll.setTextColor(getResources().getColor(R.color.blue));
                break;
            case R.id.ll_gone_collection:
                if (mListener != null) {
                    mListener.onViewClick();
                }
                break;
        }
    }

    public void setListener(ActionListener listener) {
        this.mListener = listener;
    }

    @Subscribe
    public void onOptional(Event.AddOptionalEvent event) {  //自选页面点击添加自选后EventBus的实现处
        mViewPager.setCurrentItem(1);
        currency = 1;
        tvOkexTradeOptional.setTextColor(getResources().getColor(R.color.sub_text_color));
        tvOkexTradeAll.setTextColor(getResources().getColor(R.color.blue));
    }

    public class OkTradePagerAdapter extends FragmentPagerAdapter {

        OkTradePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return FragmentConfig.getOkTradeFragment(position, from);
        }

        @Override
        public int getCount() {
            return FragmentConfig.getOKTradeNav().size();
        }

    }

    public interface ActionListener {
        void onViewClick();
    }
}

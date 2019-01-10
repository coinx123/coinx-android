package com.coin.exchange.view.fragment.trade.delegation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coin.exchange.R;
import com.coin.exchange.config.FragmentConfig;
import com.coin.exchange.model.okex.vo.MenuItemVO;
import com.coin.exchange.utils.AppUtils;
import com.coin.libbase.view.fragment.JBaseFragment;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/12/19
 * @description
 */
public class BitMEXDelegationFragment extends JBaseFragment {

    @BindView(R.id.tab)
    TabLayout tabLayout;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    public static Fragment newInstance() {
        return new BitMEXDelegationFragment();
    }

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater,
                                        @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_delega_bitmex, container, false);
    }

    @Override
    protected void initView(View view) {

        viewPager.setAdapter(new PagerAdapter(getChildFragmentManager()) {
            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return FragmentConfig.getBitMEXDeleNav().get(position).getName();
            }
        });
        viewPager.setOffscreenPageLimit(FragmentConfig.getBitMEXDeleNav().size());

        tabLayout.setupWithViewPager(viewPager);

    }

    public static class PagerAdapter extends FragmentPagerAdapter {

        PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return DelegationContentFragment.newInstance(null,
                    FragmentConfig.getBitMEXDeleNav().get(position).getStatus(),
                    null,
                    AppUtils.BITMEX);
        }

        @Override
        public int getCount() {
            return FragmentConfig.getBitMEXDeleNav().size();
        }

    }

}

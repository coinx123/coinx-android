package com.coin.exchange.view.fragment.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coin.exchange.R;
import com.coin.exchange.config.FragmentConfig;
import com.coin.exchange.model.okex.vo.MenuItemVO;
import com.coin.exchange.utils.AppUtils;
import com.coin.exchange.view.AboutActivity;
import com.coin.exchange.view.SettingActivity;
import com.coin.exchange.view.ShapeActivity;
import com.coin.libbase.view.fragment.JBaseFragment;
import com.coin.libbase.widget.NoScrollViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author dean
 * @date 创建时间：2018/11/8
 * @description 我的
 */
public class MineFragment extends JBaseFragment {

    @BindView(R.id.mine_view_pager)
    NoScrollViewPager mineViewPager;

    @BindView(R.id.tv_shape)
    TextView tvShape;
    @BindView(R.id.tv_setting)
    TextView tvSetting;
    @BindView(R.id.tv_one)
    View tvOne;
    @BindView(R.id.tv_two)
    View tvTwo;

    public static MineFragment newInstance() {
        return new MineFragment();
    }


    @Override
    protected View onCreateFragmentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    protected void initView(View view) {
        final ArrayList<MenuItemVO> mineNav = FragmentConfig.getMineNav();
        mineViewPager.setAdapter(new MinePagerAdapter(getChildFragmentManager()));

        mineViewPager.setNoScroll(false);
        mineViewPager.setPageMargin(AppUtils.dpToPx(8));
        // 设置不会回收的数
        mineViewPager.setOffscreenPageLimit(mineNav.size());
        mineViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ViewGroup.LayoutParams lpOne = tvOne.getLayoutParams();
                ViewGroup.LayoutParams lpTwo = tvTwo.getLayoutParams();
                if (position == 1) {
                    lpOne.width = AppUtils.dpToPx(5);
                    tvOne.setLayoutParams(lpOne);
                    tvOne.setBackgroundResource(R.drawable.shape_rank_other_bg);
                    lpTwo.width = AppUtils.dpToPx(10);
                    tvTwo.setLayoutParams(lpTwo);
                    tvTwo.setBackgroundResource(R.drawable.future_trade_bg_blue_20dp);
                } else {
                    lpOne.width = AppUtils.dpToPx(10);
                    tvOne.setLayoutParams(lpOne);
                    tvOne.setBackgroundResource(R.drawable.future_trade_bg_blue_20dp);
                    lpTwo.width = AppUtils.dpToPx(5);
                    tvTwo.setLayoutParams(lpTwo);
                    tvTwo.setBackgroundResource(R.drawable.shape_rank_other_bg);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public class MinePagerAdapter extends FragmentPagerAdapter {

        MinePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return FragmentConfig.getMineFragment(position);
        }

        @Override
        public int getCount() {
            return FragmentConfig.getMineNav().size();
        }

    }

    @OnClick({R.id.tv_about, R.id.tv_shape, R.id.tv_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_about:
                startActivity(new Intent(getContext(), AboutActivity.class));
                break;
            case R.id.tv_shape:
                startActivity(new Intent(getContext(), ShapeActivity.class));
                break;
            case R.id.tv_setting:
                startActivity(new Intent(getContext(), SettingActivity.class));
                break;
        }
    }
}

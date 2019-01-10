package com.coin.exchange.view;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.coin.exchange.context.AppApplication;
import com.coin.exchange.R;
import com.coin.exchange.config.FragmentConfig;
import com.coin.exchange.cache.PreferenceManager;
import com.coin.exchange.di.component.AppComponent;
import com.coin.exchange.model.okex.vo.MenuItemVO;
import com.coin.exchange.utils.AppUtils;
import com.coin.exchange.widget.MainTabItem;
import com.coin.libbase.model.eventbus.Event;
import com.coin.libbase.utils.DoubleClickExitDetector;
import com.coin.libbase.utils.StatusBarUtil;
import com.coin.libbase.view.activity.JBaseActivity;

import java.util.ArrayList;

import com.coin.exchange.model.okex.response.FuturesRateRes;
import com.coin.exchange.net.RetrofitFactory;

import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends JBaseActivity {

    private static final String TAG = "MainActivity";

    private static final int INDEX_HOME = 0;
    private DoubleClickExitDetector mDoubleClickExitDetector;

    @Inject
    PreferenceManager preferenceManager;

    @BindView(R.id.tab)
    TabLayout mTab;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @BindView(R.id.v_status_bar)
    View vStatusBar;

    @Override
    protected void registerDagger() {
        AppApplication.getAppComponent()
                .inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initIntent(Intent intent) {
        mDoubleClickExitDetector = new DoubleClickExitDetector();
    }

    @Override
    protected void initView() {

        getFuturesRate();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            vStatusBar.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams layoutParams = vStatusBar.getLayoutParams();
            layoutParams.height = AppUtils.getStatusBarHeight(this);
            vStatusBar.setBackgroundColor(ContextCompat.getColor(this, R.color.jColorStateBarAlpha));
            vStatusBar.setLayoutParams(layoutParams);
        }

    }

    @Override
    protected void setStatusBarColor(int color) {
        changeStatus(0, color);
    }

    private void changeStatus(int position, int color) {

        switch (position) {
            case 0:
                StatusBarUtil.setTranslucentForImageViewInFragment(this,
                        0,
                        null);
                StatusBarUtil.setDarkMode(this);
                break;
            default:
                StatusBarUtil.setTranslucentForImageViewInFragment(this,
                        0,
                        null);
                StatusBarUtil.setLightMode(this);
                break;
        }

    }

    @Override
    protected void initData() {
        final ArrayList<MenuItemVO> mainNav = FragmentConfig.getMainNav();

        mViewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()) {
            @Override
            public CharSequence getPageTitle(int position) {
                return mainNav.get(position).getName();
            }
        });
        //绑定
        mTab.setupWithViewPager(mViewPager);
        //设置自定义视图(curIndex默认选定为第0个)
        int curIndex = 0;
        for (int i = 0; i < mTab.getTabCount(); i++) {

            TabLayout.Tab tab = mTab.getTabAt(i);
            MainTabItem tabItem = new MainTabItem(this)
                    .initData(mainNav.get(i).getName(),
                            mainNav.get(i).getResId(),
                            mainNav.get(i).getId() == curIndex);

            if (tab != null) {
                tab.setCustomView(tabItem);
                if (curIndex == i) {
                    tab.select();
                }
            } else {
                Log.e(TAG, "Tab is null!!!");
            }

        }
        // 设置不会回收的数
        mViewPager.setOffscreenPageLimit(mainNav.size());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeStatus(position, R.color.white);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public static class PagerAdapter extends FragmentPagerAdapter {

        PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return FragmentConfig.getMainFragment(position);
        }

        @Override
        public int getCount() {
            return FragmentConfig.getMainNav().size();
        }

    }


    /**
     * 获取美元兑换人民币的汇率
     */
    private void getFuturesRate() {
        RetrofitFactory
                .getOkExApiService()
                .getFuturesRate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<FuturesRateRes>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(FuturesRateRes futuresRateRes) {
                        preferenceManager.putFloat(PreferenceManager.RATE, Float.parseFloat(futuresRateRes.getRate()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "onError: " + e.getMessage());
                    }
                });
    }

    @Subscribe()
    public void restartForIncreaseStyle(Event.RestartEvent event) {
        recreate();
    }

    @Override
    public void onBackPressed() {
        if (mTab.getSelectedTabPosition() == INDEX_HOME) {
            boolean doubleClick = mDoubleClickExitDetector.click();
            if (doubleClick) {
                super.onBackPressed();
            }
        } else {
            TabLayout.Tab tab = mTab.getTabAt(0);
            if (tab != null) {
                tab.select();
            }
        }
    }

}

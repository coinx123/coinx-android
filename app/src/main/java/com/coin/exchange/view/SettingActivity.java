package com.coin.exchange.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.coin.exchange.R;
import com.coin.exchange.cache.CacheHelper;
import com.coin.exchange.cache.PreferenceManager;
import com.coin.libbase.model.eventbus.Event;
import com.coin.libbase.view.activity.JToolbarActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/29
 * @description 设置页面
 */

public class SettingActivity extends JToolbarActivity implements View.OnClickListener {

    @BindView(R.id.tb_increase_style)
    ToggleButton tbIncreaseStyle;
    @BindView(R.id.ll_increase_style)
    LinearLayout llIncreaseStyle;
    @BindView(R.id.tb_commit_affirm)
    ToggleButton tbCommitAffirm;
    @BindView(R.id.ll_commit_affirm)
    LinearLayout llCommitAffirm;

    @Override
    protected int getLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initIntent(Intent intent) {

    }

    @Override
    protected void initView() {
        setToolbarBackground(R.color.white);
        setTitle("设置");

        boolean increase = CacheHelper.getInstance().getBooleanCache(PreferenceManager.INCREASE_COLOR_IS_RED);
        boolean showTrade = CacheHelper.getInstance().getBooleanCache(PreferenceManager.IS_SHOW_TRADE_DIALOG);

        tbIncreaseStyle.setChecked(increase);
        tbCommitAffirm.setChecked(showTrade);

        llCommitAffirm.setOnClickListener(this);
        llIncreaseStyle.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_commit_affirm: // 交易确认
                boolean affirmCheckResult = !tbCommitAffirm.isChecked();
                CacheHelper.getInstance().setCache(PreferenceManager.IS_SHOW_TRADE_DIALOG, affirmCheckResult);
                tbCommitAffirm.setChecked(affirmCheckResult);
                break;
            case R.id.ll_increase_style:    // 红涨绿跌
                boolean increaseCheckResult = !tbIncreaseStyle.isChecked();
                CacheHelper.getInstance().setCache(PreferenceManager.INCREASE_COLOR_IS_RED, increaseCheckResult);
                EventBus.getDefault().post(new Event.RestartEvent());
                tbIncreaseStyle.setChecked(increaseCheckResult);
                break;
        }
    }
}

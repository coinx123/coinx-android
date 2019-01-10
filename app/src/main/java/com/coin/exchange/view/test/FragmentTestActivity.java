package com.coin.exchange.view.test;

import android.content.Intent;

import com.coin.exchange.R;
import com.coin.exchange.utils.AppUtils;
import com.coin.exchange.view.fragment.trade.delegation.BitMEXDelegationFragment;
import com.coin.exchange.view.fragment.trade.delegation.DelegationFragment;
import com.coin.exchange.view.fragment.trade.position.PositionFragment;
import com.coin.libbase.view.activity.JBaseActivity;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/12/18
 * @description
 */
public class FragmentTestActivity extends JBaseActivity {
    @Override
    protected int getLayout() {
        return COMMON_FRAME_LAYOUT;
    }

    @Override
    protected void initIntent(Intent intent) {

    }

    @Override
    protected void initView() {
//        addLayerFragment(R.id.frame_layout_container, PositionFragment.newInstance(AppUtils.BITMEX));
        addLayerFragment(R.id.frame_layout_container, BitMEXDelegationFragment.newInstance());
    }

    @Override
    protected void initData() {

    }
}

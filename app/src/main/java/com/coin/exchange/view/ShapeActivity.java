package com.coin.exchange.view;

import android.content.Intent;

import com.coin.exchange.R;
import com.coin.libbase.utils.StatusBarUtil;
import com.coin.libbase.view.activity.JBaseActivity;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/29
 * @description 设置页面
 */

public class ShapeActivity extends JBaseActivity {

    @Override
    protected int getLayout() {
        return R.layout.activity_shape;
    }

    @Override
    protected void initIntent(Intent intent) {

    }

    @Override
    protected void initView() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this,
                0,
                null);
        StatusBarUtil.setDarkMode(this);
    }

    @Override
    protected void initData() {

    }

}

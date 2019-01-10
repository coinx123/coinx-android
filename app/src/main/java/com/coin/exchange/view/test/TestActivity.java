package com.coin.exchange.view.test;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.coin.exchange.context.AppApplication;
import com.coin.exchange.R;
import com.coin.exchange.di.component.DaggerTestComponent;
import com.coin.exchange.di.module.TestModule;
import com.coin.exchange.presenter.TestPresenter;
import com.coin.libbase.view.activity.JBaseActivity;

import butterknife.BindView;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/15
 * @description
 */
public class TestActivity extends JBaseActivity<TestPresenter> implements TestView {

    @BindView(R.id.btn_click)
    Button btnClick;

    @Override
    protected int getLayout() {
        return R.layout.activity_test;
    }

    @Override
    protected void initIntent(Intent intent) {

    }

    @Override
    protected void registerDagger() {
        DaggerTestComponent.builder()
                .appComponent(AppApplication.getAppComponent())
                .testModule(new TestModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initView() {
        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.test();
            }
        });
    }

    @Override
    protected void initData() {

    }

//    public void onSuccess() {
//        Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
//    }
}

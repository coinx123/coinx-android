package com.coin.libbase.view.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coin.libbase.R;
import com.coin.libbase.config.JFrameManager;
import com.coin.libbase.presenter.BasePresenter;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/23
 * @description
 */

public abstract class JToolbarActivity<T extends BasePresenter> extends JBaseActivity<T> {

//    private final static String TAG = JToolbarActivity.class.getSimpleName();

    protected Toolbar mToolbar;

    protected TextView mTvTitle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (getSupportActionBar() == null) {
            initToolbar();
            initToolbarView(this.mToolbar);
        } else {
            Log.w(TAG, "You had set a toolbar.Maybe change the theme to NoActionBar in Manifest ");
        }

        super.onCreate(savedInstanceState);
    }

    protected void initToolbarView(Toolbar toolbar) {
        this.mTvTitle = toolbar.findViewById(R.id.tv_title);

        if (getTitle() != null) {
            this.mTvTitle.setText(getTitle());
        }
    }

    protected void setToolbarBackground(int color) {
        mToolbar.setBackgroundColor(getResources().getColor(color));
    }

    //初始化toolbar
    private void initToolbar() {

        ViewGroup decorView = (ViewGroup) getWindow().getDecorView();

        //获取到titleView
        ViewGroup viewGroup = (ViewGroup) decorView.getChildAt(0);

        if (viewGroup instanceof LinearLayout) {
            View toolbar = LayoutInflater.from(this).inflate(JFrameManager.getInstance().getToolbarLayout(), null, false);

            viewGroup.addView(toolbar, 0);

            this.mToolbar = toolbar.findViewById(R.id.toolbar);

            setSupportActionBar(this.mToolbar);

            if (getSupportActionBar() != null) {
                //隐藏标题
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                //显示回退按钮
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

            ViewGroup contentFrameLayout = findViewById(Window.ID_ANDROID_CONTENT);
            View parentView = contentFrameLayout.getChildAt(0);
            if (parentView != null && Build.VERSION.SDK_INT >= 14) {
                parentView.setFitsSystemWindows(true);
            }
        }

    }

    @Override
    public void setTitle(CharSequence title) {
        if (!TextUtils.isEmpty(title) && this.mTvTitle != null) {
            this.mTvTitle.setText(title);
        }
    }

    //隐藏回退按钮
    protected void hideHomeBtn() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

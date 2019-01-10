package com.coin.exchange.widget;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coin.exchange.R;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/12
 * @description 首页的项
 */

public class MainTabItem extends LinearLayout {

    private TextView mText;
    private ImageView mImage;

    public MainTabItem(Context context) {
        this(context, null);
    }

    public MainTabItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.widget_main_tab_item, this, true);

        setGravity(Gravity.CENTER);
        setOrientation(VERTICAL);

        mText = findViewById(R.id.text1);
        mImage = findViewById(R.id.image1);

    }

    /**
     * 初始化数据
     *
     * @param text
     * @param resID
     * @param isChecked
     * @param isTint    是否渲染
     * @return
     */
    public MainTabItem initData(String text,
                                int resID,
                                boolean isChecked,
                                boolean isTint) {
        mText.setText(text);
        mImage.setImageResource(resID);
        if (isTint) {
            DrawableCompat.setTintList(mImage.getDrawable(),
                    ResourcesCompat.getColorStateList(getResources(),
                            R.color.tint_colors_gray2blue, null));
        }
        setSelected(isChecked);
        return this;
    }

    /**
     * 初始化数据 [默认渲染]
     *
     * @param text      文本
     * @param resID     图标资源
     * @param isChecked 是否被选中
     * @return 返回当前tab
     */
    public MainTabItem initData(String text, int resID, boolean isChecked) {
        return initData(text, resID, isChecked, true);
    }

    public View getView() {
        return mImage;
    }
}

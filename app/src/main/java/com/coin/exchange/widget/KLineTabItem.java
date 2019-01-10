package com.coin.exchange.widget;

import android.content.Context;
import android.content.res.Resources;
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
 * @description K线图的项
 */

public class KLineTabItem extends LinearLayout {

    private TextView mText;
    private ImageView mImage;

    public KLineTabItem(Context context) {
        this(context, null);
    }

    public KLineTabItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.widget_kline_tab_item, this, true);

        setGravity(Gravity.CENTER);
        setOrientation(HORIZONTAL);

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
    public KLineTabItem initData(String text,
                                 int resID,
                                 boolean isChecked,
                                 boolean isTint) {
        mText.setText(text);
        if (resID != 0) {
            mImage.setImageResource(resID);
            mImage.setVisibility(VISIBLE);
        } else {
            mImage.setVisibility(GONE);
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
    public KLineTabItem initData(String text, int resID, boolean isChecked) {
        return initData(text, resID, isChecked, true);
    }

    public View getView() {
        return mImage;
    }
}

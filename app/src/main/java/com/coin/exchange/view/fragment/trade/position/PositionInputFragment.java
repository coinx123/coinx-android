package com.coin.exchange.view.fragment.trade.position;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.coin.exchange.R;
import com.coin.exchange.adapter.NumbersAdapter;
import com.coin.libbase.view.fragment.dialog.JBaseDialogFragment;
import com.coin.libbase.view.fragment.dialog.JBaseFloatingFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/22
 * @description
 */
public abstract class PositionInputFragment extends JBaseFloatingFragment
        implements NumbersAdapter.ClickListener {

    protected static final String DOT = ".";
    protected static final String ZERO = "0";
    protected static final String DELETE = "#";

    // 是否接受小数点
    protected boolean mIsAcceptDot = true;

    /**
     * 检测输入
     *
     * @param curTextView 输入的TextView
     * @param key         键盘输入
     * @param content     当前内容
     */
    protected void checkInput(TextView curTextView,
                              String key,
                              String content) {

        if (content == null || curTextView == null) {
            return;
        }

        // 如果输入小数点
        if (key.equals(DOT)) {

            // 不接受小数点
            if (!mIsAcceptDot) {
                return;
            }

            // 检查是否已经有一个小数点，有的话直接返回
            if (content.contains(DOT)) {
                return;
            }

            content += key;

        } else if (key.equals(ZERO)) { // 如果输入的是0

            // 小数点前只能一个零
            if (content.equals(ZERO)) {
                return;
            }

            content += key;

        } else if (key.equals(DELETE)) { // 如果输入的删除

            if (content.length() == 1) { // 只剩下一位
                content = ZERO;
            } else {
                content = content.substring(0, content.length() - 1);
            }

        } else {        // 其余数字

            if (content.equals(ZERO)) {
                content = key;
            } else {
                content += key;
            }

        }

        curTextView.setText(content);

    }

}

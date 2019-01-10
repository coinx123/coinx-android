package com.coin.exchange.utils;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * @author dean
 * @date 创建时间：2018/11/22
 * @description 限制输入框小数的位数
 */
public class PointLengthFilterUtils implements InputFilter {
    /**
     * 输入框小数的位数  示例保留2位小数
     */
    private int DECIMAL_DIGITS = 2;

    /**
     * @param point 输入框小数的位数
     */
    public PointLengthFilterUtils(int point) {
        DECIMAL_DIGITS = point;
    }

    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        // 删除等特殊字符，直接返回
        if ("".equals(source.toString())) {
            return null;
        }
        String dValue = dest.toString();
        String[] splitArray = dValue.split("\\.");
        if (splitArray.length > 1) {
            String dotValue = splitArray[1];
            int diff = dotValue.length() + 1 - DECIMAL_DIGITS;
            if (diff > 0) {
                return source.subSequence(start, end - diff);
            }
        }
        return null;
    }

}

package com.coin.exchange.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coin.exchange.R;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/22
 * @description 数字键盘的适配器
 */

public class NumbersAdapter extends BaseAdapter {

    private static final String NUMBERS = "123456789.0#";
    private final Context mContext;
    private final ClickListener mListener;

    public NumbersAdapter(Context context, ClickListener listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    @Override
    public int getCount() {
        return NUMBERS.length();
    }

    @Override
    public String getItem(int position) {
        return String.valueOf(NUMBERS.charAt(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemHolder itemHolder;
        if (convertView == null) {
            itemHolder = new ItemHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_view_input_group_code, null);
            itemHolder.mRootView = convertView.findViewById(R.id.number_root_view);
            itemHolder.mNumberTextView = convertView.findViewById(R.id.number_textView);
            itemHolder.mDeleteImageView = convertView.findViewById(R.id.number_delete_imageView);
            convertView.setTag(itemHolder);
        } else {
            itemHolder = (ItemHolder) convertView.getTag();
        }
        final String curNumber = getItem(position);
        if ("C".equals(curNumber)) {
            itemHolder.mDeleteImageView.setVisibility(GONE);
            itemHolder.mNumberTextView.setVisibility(View.INVISIBLE);
            itemHolder.mNumberTextView.setText(curNumber);
            itemHolder.mRootView.setBackgroundColor(
                    ContextCompat.getColor(mContext, R.color.keyboard_gray));
        } else if ("#".equals(curNumber)) {
            itemHolder.mRootView.setBackgroundColor(
                    ContextCompat.getColor(mContext, R.color.keyboard_gray));
            itemHolder.mNumberTextView.setVisibility(GONE);
            itemHolder.mDeleteImageView.setVisibility(VISIBLE);
        } else {
            itemHolder.mRootView.setBackgroundResource(R.drawable.list_selector);
            itemHolder.mDeleteImageView.setVisibility(GONE);
            itemHolder.mNumberTextView.setVisibility(VISIBLE);
            itemHolder.mNumberTextView.setText(curNumber);
        }

        if (!curNumber.equals("C")) {
            itemHolder.mRootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onClick(curNumber);
                    }
                }
            });
        }
        return convertView;
    }

    private static class ItemHolder {
        RelativeLayout mRootView;
        TextView mNumberTextView;
        ImageView mDeleteImageView;
    }

    public interface ClickListener {
        void onClick(String clickNum);
    }

}
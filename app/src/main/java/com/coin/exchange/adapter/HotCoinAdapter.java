package com.coin.exchange.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coin.exchange.R;
import com.coin.exchange.aop.CheckLogin;
import com.coin.exchange.cache.CacheHelper;
import com.coin.exchange.config.FragmentConfig;
import com.coin.exchange.model.okex.vo.HotCoinItemVO;
import com.coin.exchange.mvp.KLine.KLineActivity;
import com.coin.exchange.utils.AppUtils;
import com.coin.libbase.utils.DoubleUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/13
 * @description
 */
public class HotCoinAdapter extends RecyclerView.Adapter<HotCoinAdapter.ViewHolder> {

    private final List<HotCoinItemVO> mData;
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;

    public HotCoinAdapter(Context context, List<HotCoinItemVO> mData) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mData = mData;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_hot_coin, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final HotCoinItemVO item = mData.get(position);
        // 名称
        holder.tvName.setText(item.getName());
        // 价格
        holder.tvValue.setText(DoubleUtils.formatTwoDecimalString(item.getValue()));
        // 涨幅
        holder.tvRange.setText(
                String.format(
                        mContext.getResources().getString(R.string.market_range),
                        item.getRange()));

        if (item.isIncrease()) {
            holder.tvValue.setTextColor(AppUtils.getLightIncreaseColor());
            holder.tvRange.setTextColor(AppUtils.getLightIncreaseColor());
        } else {
            holder.tvValue.setTextColor(AppUtils.getLightDecreaseColor());
            holder.tvRange.setTextColor(AppUtils.getLightDecreaseColor());
        }

        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @CheckLogin(AppUtils.BITMEX)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(FragmentConfig.INSTRUMENT_ID, item.getId()); //KLineActivity 一样存在要一起改
                intent.putExtra(FragmentConfig.TYPE, item.getDesName());
                intent.putExtra(FragmentConfig.FROM, AppUtils.BITMEX);
                intent.setClass(mContext, KLineActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ll_item)
        LinearLayout llItem;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_value)
        TextView tvValue;
        @BindView(R.id.tv_range)
        TextView tvRange;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

}

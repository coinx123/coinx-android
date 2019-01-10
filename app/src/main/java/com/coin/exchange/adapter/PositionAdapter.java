package com.coin.exchange.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coin.exchange.R;
import com.coin.exchange.model.okex.vo.PositionItemVO;
import com.coin.exchange.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/21
 * @description
 */
public class PositionAdapter extends RecyclerView.Adapter<PositionAdapter.ViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private final List<PositionItemVO> mData;
    private final ClickListener mListener;
    private Context mContext;

    public PositionAdapter(Context context,
                           List<PositionItemVO> data,
                           ClickListener listener) {
        mLayoutInflater = LayoutInflater.from(context);
        mData = data;
        mListener = listener;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_position_content, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final int curPosition = position;
        PositionItemVO item = mData.get(position);

        holder.tvSellBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.clickSell(curPosition);
                }
            }
        });

        holder.tvSettingSecurityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.clickSettingSecurity(curPosition);
                }
            }
        });

        // 标题 "xx"
        holder.tvRootName.setText(item.getTitleName());
        // 币种主类型 "xx"
        holder.tvName.setText(item.getTypeName());
        // 币种次类型 "xx-12121"
        holder.tvNameDes.setText(item.getNameDes());

        // 单类型 "多头 10x"
        holder.tvSellType.setText(String.format(mContext.getString(R.string.position_type), item.getType(), item.getLeverage()));

        // 开仓价格
        holder.tvBuildValueNum.setText(item.getBuyPrice());
        // 收益率
        holder.tvIncomeRateNum.setText(item.getIncomeRate());

        // 收益
        holder.tvIncomeNum.setText(item.getIncome());

        // 持仓量
        holder.tvPositionNum.setText(item.getPosition());
        // 可平仓量
        holder.tvSellNum.setText(item.getSell());
        // 保证金
        holder.tvSecurityNum.setText(item.getSecurity());
        // 强平价格
        holder.tvForceSellNum.setText(item.getSellForce());

        if (item.isWantIncrease()) {
            holder.tvSellType.setBackground(AppUtils.getIncreaseBg());
        } else {
            holder.tvSellType.setBackground(AppUtils.getDecreaseBg());
        }

        if (item.isIncrease()) {
            holder.tvIncomeRateNum.setTextColor(AppUtils.getIncreaseColor());
        } else {
            holder.tvIncomeRateNum.setTextColor(AppUtils.getDecreaseColor());
        }

        if (!TextUtils.isEmpty(item.getUnit())) {
            holder.tvIncomeTitle.setText("收益(" + item.getUnit() + ")");
        }else{
            holder.tvIncomeTitle.setText("已收益(BTC)");
        }

        if (!TextUtils.isEmpty(item.getUnit())) {
            holder.tvForceSell.setText("强平价格(" + item.getUnit() + ")");
        }

        if (item.getPlatform().equals(AppUtils.OKEX)) {
            holder.tvSettingSecurityBtn.setVisibility(View.GONE);
            holder.tvSecurity.setText("保证金(BTC)");
        } else if (item.getPlatform().equals(AppUtils.BITMEX)) {
            holder.tvSettingSecurityBtn.setVisibility(View.VISIBLE);
            holder.tvSecurity.setText("保证金(XBT)");
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.tv_root_name)
        TextView tvRootName;
        @BindView(R.id.tv_sell_type)
        TextView tvSellType;
        @BindView(R.id.ll_top)
        LinearLayout llTop;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_name_des)
        TextView tvNameDes;
        @BindView(R.id.tv_income)
        TextView tvIncome;
        @BindView(R.id.tv_income_num)
        TextView tvIncomeNum;
        @BindView(R.id.tv_income_rate_num)
        TextView tvIncomeRateNum;
        @BindView(R.id.tv_build_value)
        TextView tvBuildValue;
        @BindView(R.id.tv_build_value_num)
        TextView tvBuildValueNum;
        @BindView(R.id.tv_income_title)
        TextView tvIncomeTitle;
        @BindView(R.id.tv_sell)
        TextView tvSell;
        @BindView(R.id.tv_sell_num)
        TextView tvSellNum;
        @BindView(R.id.tv_position)
        TextView tvPosition;
        @BindView(R.id.tv_position_num)
        TextView tvPositionNum;
        @BindView(R.id.tv_force_sell)
        TextView tvForceSell;
        @BindView(R.id.tv_force_sell_num)
        TextView tvForceSellNum;
        @BindView(R.id.tv_security)
        TextView tvSecurity;
        @BindView(R.id.tv_security_num)
        TextView tvSecurityNum;
        @BindView(R.id.tv_sell_btn)
        TextView tvSellBtn;
        @BindView(R.id.tv_setting_security_btn)
        TextView tvSettingSecurityBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 按钮回调
     */
    public interface ClickListener {
        // 点击平仓
        void clickSell(int position);

        // 点击调整保证金
        void clickSettingSecurity(int position);
    }

}

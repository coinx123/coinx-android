package com.coin.exchange.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.coin.exchange.R;
import com.coin.exchange.model.okex.vo.DelegationItemVO;
import com.coin.exchange.utils.AppUtils;
import com.coin.exchange.view.fragment.trade.delegation.DelegationContentFragment;
import com.coin.exchange.view.fragment.trade.delegation.DelegationFragment;
import com.coin.exchange.widget.LeanView;
import com.coin.libbase.utils.DoubleUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/27
 * @description
 */
public class DelegationAdapter extends RecyclerView.Adapter<DelegationAdapter.ViewHolder> {

    private final List<DelegationItemVO> mData;
    private final LayoutInflater mInflater;
    private final Context mContext;

    private final ItemClickListener mListener;

    public DelegationAdapter(Context context,
                             List<DelegationItemVO> mData,
                             ItemClickListener listener) {
        this.mData = mData;
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_delegation_content, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final int index = position;
        final DelegationItemVO item = mData.get(position);
        switch (item.getStatus()) {
            case DelegationFragment.WAITING:
                holder.tvCancel.setVisibility(View.VISIBLE);
                holder.leanView.setVisibility(View.GONE);
                break;
            case DelegationFragment.DONE:
                holder.tvCancel.setVisibility(View.GONE);
                holder.leanView.setVisibility(View.VISIBLE);
                holder.leanView.setBgColor(ContextCompat.getColor(mContext, R.color.color_delegation_done));
                holder.leanView.setText("已成交");
                break;
            case DelegationFragment.CANCEL:
                holder.tvCancel.setVisibility(View.GONE);
                holder.leanView.setVisibility(View.VISIBLE);
                holder.leanView.setBgColor(ContextCompat.getColor(mContext, R.color.color_delegation_cancel));
                holder.leanView.setText("已撤销");
                break;
        }

        if (item.isSell()) {
            holder.tvDealType.setBackground(ContextCompat.getDrawable(mContext, R.drawable.future_trade_bg_green));
        } else {
            holder.tvDealType.setBackground(ContextCompat.getDrawable(mContext, R.drawable.future_trade_bg_red));
        }

        holder.tvName.setText(item.getName());
        holder.tvNameDes.setText(item.getNameDes());
        holder.tvDealType.setText(item.getDealType());
        holder.tvSecurityNum.setText(DoubleUtils.formatSixDecimalString(item.getSecurity()));
        try {
            holder.tvFeeNum.setText(DoubleUtils.formatSixDecimalString(Double.parseDouble(item.getFee())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.tvDelegationNum.setText(item.getDelegationPrice());
        holder.tvDelegationCountNum.setText(item.getDelegationValue());
        holder.tvDoneNum.setText(item.getDonePrice());
        holder.tvDoneCountNum.setText(item.getDoneValue());
        holder.tvControlTime.setText(item.getTime());

        holder.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mListener != null) {
                    String content;
                    if (TextUtils.isEmpty(item.getNameDes())) {
                        content = "确定对 " + item.getName() +
                                " " + item.getDealType() + " 进行撤销委托么？";
                    } else {
                        content = "确定对 " + item.getName() + " " + item.getNameDes() +
                                " " + item.getDealType() + " 进行撤销委托么？";
                    }

                    new MaterialDialog.Builder(mContext)
                            .title("撤销提示")
                            .content(content)
                            .negativeColor(ContextCompat.getColor(mContext, R.color.main_text_color))
                            .positiveColor(ContextCompat.getColor(mContext, R.color.blue))
                            .positiveText("取消")
                            .negativeText("确定")
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    mListener.onClickCancel(item.getOrderId(), item.getInsId(), index);
                                }
                            })
                            .show();

                }

            }
        });

        if (item.getPlatform().equals(AppUtils.BITMEX)) {
            holder.tvSecurityName.setVisibility(View.GONE);
            holder.tvSecurityNum.setVisibility(View.GONE);
            holder.tvFeeName.setText("手续费(XBT)");
        } else {
            holder.tvSecurityName.setVisibility(View.VISIBLE);
            holder.tvSecurityNum.setVisibility(View.VISIBLE);
            holder.tvFeeName.setText("手续费(BTC)");
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_name_des)
        TextView tvNameDes;
        @BindView(R.id.leanView)
        LeanView leanView;
        @BindView(R.id.tv_deal_type)
        TextView tvDealType;
        @BindView(R.id.tv_control_time)
        TextView tvControlTime;
        @BindView(R.id.tv_security_name)
        TextView tvSecurityName;
        @BindView(R.id.tv_security_num)
        TextView tvSecurityNum;
        @BindView(R.id.tv_fee_name)
        TextView tvFeeName;
        @BindView(R.id.tv_fee_num)
        TextView tvFeeNum;
        @BindView(R.id.tv_delegation_name)
        TextView tvDelegationName;
        @BindView(R.id.tv_delegation_num)
        TextView tvDelegationNum;
        @BindView(R.id.tv_done_name)
        TextView tvDoneName;
        @BindView(R.id.tv_done_num)
        TextView tvDoneNum;
        @BindView(R.id.tv_delegation_count_name)
        TextView tvDelegationCountName;
        @BindView(R.id.tv_delegation_count_num)
        TextView tvDelegationCountNum;
        @BindView(R.id.tv_done_count_name)
        TextView tvDoneCountName;
        @BindView(R.id.tv_done_count_num)
        TextView tvDoneCountNum;
        @BindView(R.id.tv_cancel)
        TextView tvCancel;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ItemClickListener {
        void onClickCancel(String orderId, String insId, int position);
    }

}

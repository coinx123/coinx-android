package com.coin.exchange.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coin.exchange.R;
import com.coin.exchange.model.okex.response.FuturesAccountsResItem;
import com.coin.exchange.utils.AppUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;

/**
 * @author dean
 * @date 创建时间：2018/11/9
 * @description
 */
public class AccountEquityAdapter extends RecyclerView.Adapter<AccountEquityAdapter.TradeListViewHolder> {


    private Activity context;
    @NonNull
    private final List<FuturesAccountsResItem> tradesList = new ArrayList<>();
    private DecimalFormat df = new DecimalFormat("######0.0000");

    public AccountEquityAdapter(Activity activity) {
        this.context = activity;
    }

    public void updateItems(@NonNull List<FuturesAccountsResItem> list) {
        tradesList.clear();
        tradesList.addAll(list);
        this.notifyDataSetChanged();
    }


    @Override
    public TradeListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_accoubnt_equity_list, parent, false);
        return new TradeListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TradeListViewHolder holder, final int position) {
        FuturesAccountsResItem listItem = tradesList.get(position);
        holder.tvCurrency.setCompoundDrawablesWithIntrinsicBounds(AppUtils.ICON_MAP.get(listItem.getCurrency()), 0, 0, 0);
        holder.tvCurrency.setText("  " + listItem.getCurrency());
        holder.tvUsedMargin.setText("已用保证金(" + listItem.getCurrency() + ")");
        holder.tvRealizedMarginValue.setText(listItem.getRealized_margin());
        holder.tvAllEquityValue.setText(listItem.getAll_equity());
        holder.tvUsedMarginValue.setText(listItem.getUsed_margin());
        holder.tvFreezingDepositValue.setText(listItem.getFreezing_deposit());
        holder.tvAvailableMarginValue.setText(listItem.getAvailable_margin());

    }


    @Override
    public int getItemCount() {
        return tradesList.size();
    }

    static class TradeListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_currency)
        TextView tvCurrency;
        @BindView(R.id.tv_realized_margin_value)
        TextView tvRealizedMarginValue;
        @BindView(R.id.tv_all_equity_value)
        TextView tvAllEquityValue;
        @BindView(R.id.tv_used_margin)
        TextView tvUsedMargin;
        @BindView(R.id.tv_used_margin_value)
        TextView tvUsedMarginValue;
        @BindView(R.id.tv_freezing_deposit_value)
        TextView tvFreezingDepositValue;
        @BindView(R.id.tv_available_margin_value)
        TextView tvAvailableMarginValue;

        public TradeListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

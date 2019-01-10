package com.coin.exchange.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coin.exchange.R;
import com.coin.exchange.model.okex.response.FuturesInstrumentsBookRes;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;

/**
 * @author dean
 * @date 创建时间：2018/11/9
 * @description
 */
public class TradeDepthAdapter extends RecyclerView.Adapter<TradeDepthAdapter.DepthListViewHolder> {

    private Activity context;
    private OnItemClickListen onItemClickListen;
    @NonNull
    private FuturesInstrumentsBookRes depthList = new FuturesInstrumentsBookRes();
    private DecimalFormat df_0 = new DecimalFormat("######0");

    public TradeDepthAdapter(Activity activity) {
        this.context = activity;
    }

    public void updateItems(@NonNull FuturesInstrumentsBookRes list) {
        depthList = list;
        this.notifyDataSetChanged();
    }


    @Override
    public DepthListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_trade_depth_list, parent, false);
        return new DepthListViewHolder(itemView, depthList, onItemClickListen);
    }

    @Override
    public void onBindViewHolder(final DepthListViewHolder holder, final int position) {
        if (depthList.getAsks().size() + depthList.getBids().size() >= 10) {
            if (position < 5) {
                holder.tvTradeDepthPrise.setText(BigDecimal.valueOf(depthList.getAsks().get(position).get(0)) + "");
                holder.tvTradeDepthPrise.setTextColor(context.getResources().getColor(R.color.kline_red));
                holder.tvTradeDepthNumber.setText(df_0.format(depthList.getAsks().get(position).get(1)));
            } else {
                holder.tvTradeDepthPrise.setText(BigDecimal.valueOf(depthList.getBids().get(position - 5).get(0)) + "");
                holder.tvTradeDepthPrise.setTextColor(context.getResources().getColor(R.color.kline_green));
                holder.tvTradeDepthNumber.setText(df_0.format(depthList.getBids().get(position - 5).get(1)));
            }
        } else if (position < depthList.getAsks().size()) {
            holder.tvTradeDepthPrise.setText(BigDecimal.valueOf(depthList.getAsks().get(position).get(0)) + "");
            holder.tvTradeDepthPrise.setTextColor(context.getResources().getColor(R.color.kline_red));
            holder.tvTradeDepthNumber.setText(df_0.format(depthList.getAsks().get(position).get(1)));
        } else {
            holder.tvTradeDepthPrise.setText(BigDecimal.valueOf(depthList.getBids().get(position - depthList.getAsks().size()).get(0)) + "");
            holder.tvTradeDepthPrise.setTextColor(context.getResources().getColor(R.color.kline_green));
            holder.tvTradeDepthNumber.setText(df_0.format(depthList.getBids().get(position - depthList.getAsks().size()).get(1)));
        }
    }


    @Override
    public int getItemCount() {
        if (depthList.getBids() == null || depthList.getAsks() == null) {
            return 0;
        }
        if (depthList.getAsks().size() + depthList.getBids().size() > 10) {
            return 10;
        }
        return depthList.getAsks().size() + depthList.getBids().size();
    }

    public void setOnItemClickListener(OnItemClickListen listener) {
        this.onItemClickListen = listener;
    }

    static class DepthListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_trade_depth_prise)
        TextView tvTradeDepthPrise;
        @BindView(R.id.tv_trade_depth_number)
        TextView tvTradeDepthNumber;
        FuturesInstrumentsBookRes depthList;
        private OnItemClickListen onItemClickListen;

        public DepthListViewHolder(View itemView, FuturesInstrumentsBookRes mDepthList, OnItemClickListen Listen) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            depthList = mDepthList;
            onItemClickListen = Listen;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (depthList.getAsks().size() + depthList.getBids().size() >= 10) {
                if (getAdapterPosition() < 5) {
                    onItemClickListen.onItemClick(depthList.getAsks().get(getAdapterPosition()).get(0));
                } else {
                    onItemClickListen.onItemClick(depthList.getBids().get(getAdapterPosition() - 5).get(0));
                }
            } else {
                if (getAdapterPosition() < depthList.getAsks().size()) {
                    onItemClickListen.onItemClick(depthList.getAsks().get(getAdapterPosition()).get(0));
                } else {
                    onItemClickListen.onItemClick(depthList.getBids().get(getAdapterPosition() - depthList.getAsks().size()).get(0));
                }
            }

        }
    }

    public interface OnItemClickListen {
        public void onItemClick(double prise);
    }
}

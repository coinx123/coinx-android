package com.coin.exchange.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coin.exchange.R;
import com.coin.exchange.model.okex.response.FuturesInstrumentsTradesList;
import com.coin.exchange.utils.DateUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;

/**
 * @author dean
 * @date 创建时间：2018/11/9
 * @description
 */
public class KlineTradeAdapter extends RecyclerView.Adapter<KlineTradeAdapter.TradeListViewHolder> {

    private Activity context;
    @NonNull
    private final List<FuturesInstrumentsTradesList> tradesList = new ArrayList<>();
    private DecimalFormat df = new DecimalFormat("######0.00");
    private DecimalFormat df3 = new DecimalFormat("######0.000");
    private SimpleDateFormat sd = new SimpleDateFormat("HH:mm:ss");

    public KlineTradeAdapter(Activity activity) {
        this.context = activity;
    }

    public void updateItems(@NonNull List<FuturesInstrumentsTradesList> list) {
        tradesList.clear();
        tradesList.addAll(list);
        this.notifyDataSetChanged();
    }


    @Override
    public TradeListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_trade_list, parent, false);
        return new TradeListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TradeListViewHolder holder, final int position) {
        FuturesInstrumentsTradesList listItem = tradesList.get(position);

        if (listItem.getSide().equalsIgnoreCase("buy") || listItem.getSide().equalsIgnoreCase("bid")) {
            holder.klineTradeListDirection.setText("买入");
            holder.klineTradeListDirection.setTextColor(context.getResources().getColor(R.color.kline_green));
        } else {
            holder.klineTradeListDirection.setText("卖出");
            holder.klineTradeListDirection.setTextColor(context.getResources().getColor(R.color.kline_red));
        }
        if (listItem.getSymbol() == null) {  //okex
            if (listItem.getTimestamp().length() > 9) {
                holder.klineTradeListTime.setText(sd.format(new Date((DateUtils.getTimeStampViaISO8601String(listItem.getTimestamp())))));
            } else {
                holder.klineTradeListTime.setText(listItem.getTimestamp());
            }
            holder.klineTradeListNumber.setText(listItem.getQty() + "");
        } else {
            if (listItem.getTimestamp().length() > 9) {
                holder.klineTradeListTime.setText(DateUtils.getBitmexTimeStampViaISO8601String(listItem.getTimestamp()));
            } else {
                holder.klineTradeListTime.setText(listItem.getTimestamp());
            }
            holder.klineTradeListNumber.setText(listItem.getSize() + "");
        }

        if (listItem.getSymbol() != null) {  //用XBT 结算的
            holder.klineTradeListPrise.setText(BigDecimal.valueOf(listItem.getPrice()) + "");
        } else {
            holder.klineTradeListPrise.setText(df.format(listItem.getPrice()));
        }
    }


    @Override
    public int getItemCount() {
        return tradesList.size();
    }

    static class TradeListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.kline_trade_list_time)
        TextView klineTradeListTime;
        @BindView(R.id.kline_trade_list_direction)
        TextView klineTradeListDirection;
        @BindView(R.id.kline_trade_list_number)
        TextView klineTradeListNumber;
        @BindView(R.id.kline_trade_list_prise)
        TextView klineTradeListPrise;

        public TradeListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

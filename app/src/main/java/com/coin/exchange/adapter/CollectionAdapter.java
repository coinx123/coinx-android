package com.coin.exchange.adapter;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coin.exchange.R;
import com.coin.exchange.config.okEx.ServerTimeStampHelper;
import com.coin.exchange.database.CollectionItem;
import com.coin.exchange.model.bitMex.response.InstrumentItemRes;
import com.coin.exchange.model.okex.response.FuturesInstrumentsTickerList;
import com.coin.exchange.net.RetrofitFactory;
import com.coin.exchange.utils.AppUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author dean
 * @date 创建时间：2018/11/9
 * @description
 */
public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.OptionalViewHolder> {
    private Activity context;
    @NonNull
    private final List<CollectionItem> collectionItems = new ArrayList<>();
    private DecimalFormat df = new DecimalFormat("######0.00");
    private float rate = 1.0f;
    private CollectionAdapter.OnLongClickListener mOnLongClickListener;
    private CollectionAdapter.OnClickListener onClickListener;

    public CollectionAdapter(Activity activity, float rate) {
        this.context = activity;
        this.rate = rate;
    }

    public void updateItems(@NonNull List<CollectionItem> list) {
        collectionItems.clear();
        collectionItems.addAll(list);
        this.notifyDataSetChanged();
    }

    public void setOnLongListener(CollectionAdapter.OnLongClickListener onLongListener) {
        mOnLongClickListener = onLongListener;
    }

    public void setOnClickListener(CollectionAdapter.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public OptionalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_optional, parent, false);
        return new OptionalViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final OptionalViewHolder holder, final int position) {
        holder.rl_okex_optional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClick(v, position);
                }
            }
        });

        holder.rl_okex_optional.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnLongClickListener != null) {
                    mOnLongClickListener.onLongClick(v, position);
                }
                return true;
            }
        });

        final CollectionItem collectionItem = collectionItems.get(position);
        String currency = collectionItem.getUrl().substring(0, 3);
        holder.iv_optional.setBackground(ContextCompat.getDrawable(context, AppUtils.ICON_MAP.get(currency)));
        holder.tv_name.setText(currency);
        holder.tv_show_time.setText(collectionItem.getTitle());

        if (collectionItem.getPosition() == 0) {  //0代表okex
            if (collectionItem.getVolume_24h() != 0) {  //推送回来后这些有数据了不用再去请求了
                holder.tv_memo.setText("24h量:" + collectionItem.getVolume_24h() + "");
                holder.tv_usa.setText("$" + df.format(collectionItem.getLast()));
                holder.tv_cny.setText(df.format(collectionItem.getLast() * rate) + "");
                getFutureCandels(holder, collectionItem.getUrl(), collectionItem.getLast());
            } else {
                RetrofitFactory
                        .getOkExApiService()
                        .getFuturesInstrumentsTickerSingle(collectionItem.getUrl())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<FuturesInstrumentsTickerList>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(final FuturesInstrumentsTickerList futuresInstrumentsTicker) {
                                holder.tv_memo.setText("24h量:" + futuresInstrumentsTicker.getVolume_24h() + "");
                                holder.tv_usa.setText("$" + df.format(futuresInstrumentsTicker.getLast()));
                                holder.tv_cny.setText(df.format(futuresInstrumentsTicker.getLast() * rate) + "");
                                getFutureCandels(holder, futuresInstrumentsTicker.getInstrument_id(), futuresInstrumentsTicker.getLast());
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        });
            }
        } else {  //bitmex
            if (collectionItem.getLast() != 0) { //表示推送过来的
                holder.tv_memo.setText("24h量:" + collectionItem.getVolume_24h() + "");
                if (collectionItem.getQuoteCurrency().equals("USD")) {
                    holder.tv_usa.setText("$" + df.format(collectionItem.getLast()));
                    holder.tv_cny.setText(df.format(collectionItem.getIndicativeSettlePrice()));
                } else {
                    holder.tv_usa.setText("฿" + BigDecimal.valueOf(collectionItem.getLast()));
                    holder.tv_cny.setText(BigDecimal.valueOf(collectionItem.getIndicativeSettlePrice()) + "");
                }

                double p = collectionItem.getLastChangePcnt() * 100;
                if (p < 0) {
                    holder.tv_percentage.setBackground(AppUtils.getDecreaseBg());
                    holder.tv_percentage.setText("" + df.format(p) + "%");
                } else {
                    holder.tv_percentage.setBackground(AppUtils.getIncreaseBg());
                    holder.tv_percentage.setText("+" + df.format(p) + "%");
                }
            } else {
                RetrofitFactory
                        .getBitMexApiService()
                        .getInstrument(collectionItem.getUrl())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<InstrumentItemRes>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(List<InstrumentItemRes> instrumentItemRes) {
                                InstrumentItemRes res = instrumentItemRes.get(0);
                                holder.tv_memo.setText("24h量:" + res.getForeignNotional24h());
                                if (res.getQuoteCurrency().equals("USD")) {
                                    holder.tv_usa.setText("$" + df.format(res.getLastPrice()));
                                    holder.tv_cny.setText(df.format(res.getIndicativeSettlePrice()));
                                } else {
                                    holder.tv_usa.setText("฿" + BigDecimal.valueOf(res.getLastPrice()));
                                    holder.tv_cny.setText(BigDecimal.valueOf(res.getIndicativeSettlePrice()) + "");
                                }

                                double p = res.getLastChangePcnt() * 100;
                                if (p < 0) {
                                    holder.tv_percentage.setBackground(AppUtils.getDecreaseBg());
                                    holder.tv_percentage.setText("" + df.format(p) + "%");
                                } else {
                                    holder.tv_percentage.setBackground(AppUtils.getIncreaseBg());
                                    holder.tv_percentage.setText("+" + df.format(p) + "%");
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        }
    }

    private void getFutureCandels(final OptionalViewHolder holder, final String instrument_id, final double last) {
        RetrofitFactory
                .getOkExApiService()
                .getFuturesInstrumentsCandles(instrument_id,
                        ServerTimeStampHelper.getInstance().getCurrentTimeStamp(ServerTimeStampHelper.Type.ISO8601_24), null, "60")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<List<Double>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<List<Double>> futuresInstrumentsTickerLists) {
                        try {
                            double p = (((last - futuresInstrumentsTickerLists.get(0).get(1))) / futuresInstrumentsTickerLists.get(0).get(1)) * 100;
                            if (p < 0) {
                                holder.tv_percentage.setBackground(AppUtils.getDecreaseBg());
                                holder.tv_percentage.setText("" + df.format(p) + "%");
                            } else {
                                holder.tv_percentage.setBackground(AppUtils.getIncreaseBg());
                                holder.tv_percentage.setText("+" + df.format(p) + "%");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return collectionItems.size();
    }

    static class OptionalViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rl_okex_optional)
        RelativeLayout rl_okex_optional;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_show_time)
        TextView tv_show_time;
        @BindView(R.id.tv_memo)
        TextView tv_memo;
        @BindView(R.id.tv_percentage)
        TextView tv_percentage;
        @BindView(R.id.tv_usa)
        TextView tv_usa;
        @BindView(R.id.tv_cny)
        TextView tv_cny;
        @BindView(R.id.iv_optional)
        ImageView iv_optional;

        public OptionalViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnLongClickListener {
        void onLongClick(View itemView, int Position);
    }

    public interface OnClickListener {
        void onClick(View itemView, int Position);
    }
}

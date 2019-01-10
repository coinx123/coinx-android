package com.coin.exchange.adapter.groundrecycleradapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.coin.exchange.R;
import com.coin.exchange.config.okEx.ServerTimeStampHelper;
import com.coin.exchange.database.CollectionModel;
import com.coin.exchange.model.okex.response.FuturesInstrumentsTickerList;
import com.coin.exchange.net.RetrofitFactory;
import com.coin.exchange.utils.AppUtils;
import com.coin.exchange.utils.DateUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MemberViewHolder extends RecyclerView.ViewHolder {

    private final TextView mNameView;
    private final TextView mTimeView;
    private final TextView mMemoView;
    private final TextView tv_usa;
    private final TextView tv_cny;
    private final TextView tv_percentage;
    private final ImageView iv_click_collection;
    private DecimalFormat df = new DecimalFormat("######0.00");
    private float rate = 1.0f;
    @NonNull
    final private CollectionModel collectionModel;

    public MemberViewHolder(View itemView, float rate, @NonNull CollectionModel collectionModel) {
        super(itemView);
        this.rate = rate;
        this.collectionModel = collectionModel;
        mNameView = itemView.findViewById(R.id.tv_name);
        mTimeView = itemView.findViewById(R.id.tv_show_time);
        mMemoView = itemView.findViewById(R.id.tv_memo);
        tv_usa = itemView.findViewById(R.id.tv_usa);
        tv_cny = itemView.findViewById(R.id.tv_cny);
        tv_percentage = itemView.findViewById(R.id.tv_percentage);
        iv_click_collection = itemView.findViewById(R.id.iv_click_collection);
    }

    public void update(final FuturesInstrumentsTickerList member, String time) {
        String from = member.getIsOkex();
        mMemoView.setText("24h量:" + member.getVolume_24h() + "");

        if (from.equals(AppUtils.OKEX)) {
            mNameView.setText(time);
            tv_usa.setText("$" + df.format(member.getLast()));
            tv_cny.setText("¥" + df.format(member.getLast() * rate) + "");
            mTimeView.setText("-" + member.getInstrument_id().substring(
                    member.getInstrument_id().length() - 4, member.getInstrument_id().length()));
            RetrofitFactory
                    .getOkExApiService()
                    .getFuturesInstrumentsCandles(member.getInstrument_id(),
                            ServerTimeStampHelper.getInstance().getCurrentTimeStamp(
                                    ServerTimeStampHelper.Type.ISO8601_24), null, "60")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<List<List<Double>>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onSuccess(List<List<Double>> futuresInstrumentsTickerLists) {
                            double p = (((member.getLast() - futuresInstrumentsTickerLists.get(0).get(1)))
                                    / futuresInstrumentsTickerLists.get(0).get(1)) * 100;
                            if (p < 0) {
                                tv_percentage.setBackground(AppUtils.getDecreaseBg());
                                tv_percentage.setText("" + df.format(p) + "%");
                            } else {
                                tv_percentage.setBackground(AppUtils.getIncreaseBg());
                                tv_percentage.setText("+" + df.format(p) + "%");
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                        }
                    });
        } else {  //bitmex
            mTimeView.setText("");
            mNameView.setText(DateUtils.forBitMexTime(member.getTimestamp()));
            if (member.getQuoteCurrency().equalsIgnoreCase("USD")) {
                tv_usa.setText("$" + df.format(member.getLast()));
                tv_cny.setText("指数:" + df.format(member.getIndicativeSettlePrice()));
            } else {
                tv_usa.setText("฿" + BigDecimal.valueOf(member.getLast()));
                tv_cny.setText("指数:" + BigDecimal.valueOf(member.getIndicativeSettlePrice()));
            }
            double p = member.getLastChangePcnt() * 100;
            if (p < 0) {
                tv_percentage.setBackground(AppUtils.getDecreaseBg());
                tv_percentage.setText("" + df.format(p) + "%");
            } else {
                tv_percentage.setBackground(AppUtils.getIncreaseBg());
                tv_percentage.setText("+" + df.format(p) + "%");
            }
        }
        AppUtils.isExitAndDelOrAdd(collectionModel, member.getInstrument_id(),
                iv_click_collection, null);
    }
}

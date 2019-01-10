package com.coin.exchange.mvp.KLine;

import com.coin.exchange.config.okEx.ServerTimeStampHelper;
import com.coin.exchange.model.bitMex.response.InstrumentItemRes;
import com.coin.exchange.model.okex.response.FuturesInstrumentsTickerList;
import com.coin.exchange.model.okex.response.FuturesInstrumentsTradesList;
import com.coin.exchange.net.RetrofitFactory;
import com.coin.libbase.net.rxjava.RxObservableSubscriber;
import com.coin.libbase.net.rxjava.RxSingleSubscriber;
import com.coin.libbase.presenter.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public final class KLinePresenter extends BasePresenter<KLineView> {
    @Inject
    public KLinePresenter(KLineView view) {
        super(view);
    }

    public void getFuturesInfo(String instrumentId) {
        RetrofitFactory
                .getOkExApiService()
                .getFuturesInstrumentsTickerSingle(instrumentId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSingleSubscriber<FuturesInstrumentsTickerList>(KLinePresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        mView.onGetFuturesInfoError(message);
                    }

                    @Override
                    protected void onSuccessRes(FuturesInstrumentsTickerList value) {
                        mView.onGetFuturesInfo(value);
                    }
                });
    }

    public void getCandles(String instrumentId, final double prise) {
        RetrofitFactory
                .getOkExApiService()
                .getFuturesInstrumentsCandles(instrumentId,
                        ServerTimeStampHelper.getInstance().getCurrentTimeStamp(ServerTimeStampHelper.Type.ISO8601_24), null, "60")
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSingleSubscriber<List<List<Double>>>(KLinePresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        mView.onGetCandlesError(message);
                    }

                    @Override
                    protected void onSuccessRes(List<List<Double>> value) {
                        mView.onGetCandles(value, prise);
                    }
                });
    }

    public void getFuturesInstrumentsTrades(String instrumentId, String from, String to, String limit) {
        RetrofitFactory
                .getOkExApiService()
                .getFuturesInstrumentsTrades(instrumentId, from, to, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSingleSubscriber<List<FuturesInstrumentsTradesList>>(KLinePresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        mView.onGetFuturesInstrumentsTradesError(message);
                    }

                    @Override
                    protected void onSuccessRes(List<FuturesInstrumentsTradesList> value) {
                        mView.onGetFuturesInstrumentsTrades(value);
                    }
                });
    }

    public void getBitmexInstrument(String instrumentId) {
        RetrofitFactory
                .getBitMexApiService()
                .getInstrument(instrumentId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObservableSubscriber<List<InstrumentItemRes>>(KLinePresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        mView.onGetBitmexInstrumentError(message);
                    }

                    @Override
                    protected void onSuccessRes(List<InstrumentItemRes> value) {
                        mView.onGetBitmexInstrument(value);
                    }
                });
    }

    public void getBitmexTradeList(String instrumentId) {
        RetrofitFactory
                .getBitMexApiService()
                .getInstrumentTradeList(instrumentId, 10, true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObservableSubscriber<List<FuturesInstrumentsTradesList>>(KLinePresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        mView.onGetBitmexTradeListError(message);
                    }

                    @Override
                    protected void onSuccessRes(List<FuturesInstrumentsTradesList> value) {
                        mView.onGetBitmexTradeList(value);
                    }
                });
    }
}

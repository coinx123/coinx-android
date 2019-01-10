package com.coin.exchange.presenter;

import android.util.Log;

import com.coin.exchange.model.okex.response.FuturesInstrumentsTickerList;
import com.coin.exchange.net.RetrofitFactory;
import com.coin.exchange.utils.IconInfoUtils;
import com.coin.exchange.view.fragment.trade.delegation.DelegationView;
import com.coin.libbase.net.rxjava.RxObservableSubscriber;
import com.coin.libbase.presenter.BasePresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/27
 * @description
 */
public class DelegationPresenter extends BasePresenter<DelegationView> {

    @Inject
    public DelegationPresenter(DelegationView mView) {
        super(mView);
    }

    /**
     * 获取 币种的名称 和 类型
     */
    public void getIconInfo() {
        RetrofitFactory
                .getOkExApiService()
                .getFutInsTicker()
                .subscribeOn(Schedulers.io())
                .map(new Function<List<FuturesInstrumentsTickerList>, Map<String, List<FuturesInstrumentsTickerList>>>() {
                    @Override
                    public Map<String, List<FuturesInstrumentsTickerList>> apply(List<FuturesInstrumentsTickerList> tickerLists) throws Exception {
                        return IconInfoUtils.sortTheTicker(tickerLists);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObservableSubscriber<Map<String, List<FuturesInstrumentsTickerList>>>(DelegationPresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        Log.i(TAG, "onError: [code:" + code + "; message:" + message + "]");
                    }

                    @Override
                    protected void onSuccessRes(Map<String, List<FuturesInstrumentsTickerList>> map) {
                        mView.onGetIconInfo(map);
                    }
                });
    }



}

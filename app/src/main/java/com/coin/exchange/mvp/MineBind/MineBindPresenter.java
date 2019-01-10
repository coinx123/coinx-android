package com.coin.exchange.mvp.MineBind;

import com.coin.exchange.model.bitMex.response.InstrumentItemRes;
import com.coin.exchange.model.bitMex.response.PositionItemRes;
import com.coin.exchange.model.bitMex.response.UserMarginRes;
import com.coin.exchange.model.okex.response.FuturesAccountsRes;
import com.coin.exchange.model.okex.response.FuturesAccountsResItem;
import com.coin.exchange.model.okex.response.FuturesInstrumentsTickerList;
import com.coin.exchange.model.okex.response.SpotInstrumentTickerRes;
import com.coin.exchange.net.RetrofitFactory;
import com.coin.exchange.utils.NumberUtil;
import com.coin.libbase.net.rxjava.RxObservableSubscriber;
import com.coin.libbase.net.rxjava.RxSingleSubscriber;
import com.coin.libbase.presenter.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public final class MineBindPresenter extends BasePresenter<MineBindView> {
    @Inject
    public MineBindPresenter(MineBindView view) {
        super(view);
    }

    //获取所有的合约，然后获取获取比特币兑换美元换的值
    public void getOkexFutures() {
        RetrofitFactory
                .getOkExApiService()
                .getFutInsTicker()
                .subscribeOn(Schedulers.io())
                .map(new Function<List<FuturesInstrumentsTickerList>, Double>() {
                    @Override
                    public Double apply(List<FuturesInstrumentsTickerList> Tickers) throws Exception {
                        Double BTC_USDValue = 1.0;
                        for (int i = 0; i < Tickers.size(); i++) {
                            if (Tickers.get(i).getInstrument_id().contains("BTC-USD")) {
                                BTC_USDValue = Tickers.get(i).getLast();//获取比特币兑换美元换的值
                                break;
                            }
                        }
                        return BTC_USDValue;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObservableSubscriber<Double>(MineBindPresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        mView.onGetOkexFuturesError(message);
                    }

                    @Override
                    protected void onSuccessRes(Double value) {
                        mView.onGetOkexFutures(value);
                    }
                });
    }

    //获取合约币种资产估值
    public void getAssetsValue() {
        RetrofitFactory
                .getOkExApiService()
                .getFuturesAccounts()
                .subscribeOn(Schedulers.io())
                .map(new Function<FuturesAccountsRes, List<FuturesAccountsResItem>>() {
                    @Override
                    public List<FuturesAccountsResItem> apply(FuturesAccountsRes futuresAccountsRes) {
                        List<FuturesAccountsResItem> tradesList = new ArrayList<>();
                        // TODO 还需要添加另外6个币种
                        if (futuresAccountsRes.getInfo().getBtc() != null) {
                            FuturesAccountsRes.Btc btc = futuresAccountsRes.getInfo().getBtc();
                            FuturesAccountsResItem fartBtc = new FuturesAccountsResItem();
                            fartBtc.setCurrency("BTC");
                            fartBtc.setAvailable_margin(btc.getTotal_avail_balance());//可用保证金
                            fartBtc.setAll_equity(btc.getEquity());//总权益
                            double Realized_pnl = 0; //已实现盈亏
                            double margin_frozen = 0; //已用保证金
                            double margin_for_unfilled = 0; //冻结保证金
                            if (btc.getMargin_mode().equals("crossed")) { //全仓
                                fartBtc.setRealized_margin(NumberUtil.getDouble(btc.getRealized_pnl(), 0.0) + "");
                                fartBtc.setUsed_margin(NumberUtil.getDouble(btc.getMargin(), 0.0) + "" + "");
                                fartBtc.setFreezing_deposit(0.0 + "");//全才没有冻结保证金
                            } else { //逐仓
                                for (int i = 0; i < btc.getContracts().size(); i++) {
                                    Realized_pnl = Realized_pnl + NumberUtil.getDouble(btc.getContracts().get(i).getRealized_pnl(), 0.0);
                                    margin_frozen = margin_frozen + NumberUtil.getDouble(btc.getContracts().get(i).getMargin_frozen(), 0.0);
                                    margin_for_unfilled = margin_for_unfilled + NumberUtil.getDouble(btc.getContracts().get(i).getMargin_for_unfilled(), 0.0);
                                }
                                fartBtc.setRealized_margin(Realized_pnl + "");
                                fartBtc.setUsed_margin(margin_frozen + "");
                                fartBtc.setFreezing_deposit(margin_for_unfilled + "");
                            }
                            tradesList.add(fartBtc);
                        }
                        if (futuresAccountsRes.getInfo().getEth() != null) {
                            FuturesAccountsRes.Eth eth = futuresAccountsRes.getInfo().getEth();
                            FuturesAccountsResItem fartEth = new FuturesAccountsResItem();
                            fartEth.setCurrency("ETH");
                            fartEth.setAvailable_margin(eth.getTotal_avail_balance());
                            fartEth.setAll_equity(eth.getEquity());
                            double Realized_pnl = 0; //已实现盈亏
                            double margin_frozen = 0; //已用保证金
                            double margin_for_unfilled = 0; //冻结保证金

                            if (eth.getMargin_mode().equals("crossed")) { //全仓
                                fartEth.setRealized_margin(NumberUtil.getDouble(eth.getRealized_pnl(), 0.0) + "");
                                fartEth.setUsed_margin(NumberUtil.getDouble(eth.getMargin(), 0.0) + "" + "");
                                fartEth.setFreezing_deposit(0.0 + "");//全才没有冻结保证金
                            } else { //逐仓
                                for (int i = 0; i < eth.getContracts().size(); i++) {
                                    Realized_pnl = Realized_pnl + NumberUtil.getDouble(eth.getContracts().get(i).getRealized_pnl(), 0.0);
                                    margin_frozen = margin_frozen + NumberUtil.getDouble(eth.getContracts().get(i).getMargin_frozen(), 0.0);
                                    margin_for_unfilled = margin_for_unfilled + NumberUtil.getDouble(eth.getContracts().get(i).getMargin_for_unfilled(), 0.0);
                                }
                                fartEth.setRealized_margin(Realized_pnl + "");
                                fartEth.setUsed_margin(margin_frozen + "");
                                fartEth.setFreezing_deposit(margin_for_unfilled + "");
                            }
                            for (int i = 0; i < eth.getContracts().size(); i++) {
                                Realized_pnl = Realized_pnl + NumberUtil.getDouble(eth.getContracts().get(i).getRealized_pnl(), 0.0);
                                margin_frozen = margin_frozen + NumberUtil.getDouble(eth.getContracts().get(i).getMargin_frozen(), 0.0);
                                margin_for_unfilled = margin_for_unfilled + NumberUtil.getDouble(eth.getContracts().get(i).getMargin_for_unfilled(), 0.0);
                            }
                            fartEth.setRealized_margin(Realized_pnl + "");
                            fartEth.setUsed_margin(margin_frozen + "");
                            fartEth.setFreezing_deposit(margin_for_unfilled + "");
                            tradesList.add(fartEth);
                        }
                        return tradesList;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObservableSubscriber<List<FuturesAccountsResItem>>(MineBindPresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        mView.onGetAssetsValueError(message);
                    }

                    @Override
                    protected void onSuccessRes(List<FuturesAccountsResItem> value) {
                        mView.onGetAssetsValue(value);
                    }
                });
    }

    //获取币币合约
    public void getSpotTickerSing(final List<FuturesAccountsResItem> tradesList) {
        RetrofitFactory
                .getOkExApiService()
                .getSpotTickerSingInfo("ETH-BTC")
                .subscribeOn(Schedulers.io())
                .map(new Function<SpotInstrumentTickerRes, Double>() {
                    @Override
                    public Double apply(SpotInstrumentTickerRes spotInstrumentTickerRes) throws Exception {
                        double EThValue = 0;
                        double BTCValue = 0;
                        for (int i = 0; i < tradesList.size(); i++) {
                            if (tradesList.get(i).getCurrency().contains("ETH")) {
                                EThValue = NumberUtil.getDouble(tradesList.get(i).getAll_equity(), 0) * NumberUtil.getDouble(spotInstrumentTickerRes.getLast(), 0.0);
                            } else if (tradesList.get(i).getCurrency().contains("BTC")) {
                                BTCValue = NumberUtil.getDouble(tradesList.get(i).getAll_equity(), 0);
                            }
                        }
                        return EThValue + BTCValue;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObservableSubscriber<Double>(MineBindPresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        mView.onGetSpotTickerSingError(message);
                    }

                    @Override
                    protected void onSuccessRes(Double value) {
                        mView.onGetSpotTickerSing(value);
                    }
                });
    }

    //获取bitmex用户钱包
    public void getBitmexUserMargin() {
        RetrofitFactory
                .getBitMexApiService()
                .getUserMargin()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSingleSubscriber<UserMarginRes>(MineBindPresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        mView.onGetBitmexUserMarginError(message);
                    }

                    @Override
                    protected void onSuccessRes(UserMarginRes value) {
                        mView.onGetBitmexUserMargin(value);
                    }
                });
    }

    public void getBitmexInstrument(String instrumentId) {
        RetrofitFactory
                .getBitMexApiService()
                .getInstrument(instrumentId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObservableSubscriber<List<InstrumentItemRes>>(MineBindPresenter.this) {
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

    /**
     * 获取 bitMex 持仓
     */
    public void getBitMexPosition(final double all_equity, final double available_margin) {
        RetrofitFactory.getBitMexApiService()
                .getPosition("{\"isOpen\":true}")
                .subscribeOn(Schedulers.io())
                .map(new Function<List<PositionItemRes>, List<FuturesAccountsResItem>>() {
                    @Override
                    public List<FuturesAccountsResItem> apply(List<PositionItemRes> positionItemList) throws Exception {

                        List<FuturesAccountsResItem> listItem = new ArrayList<>();
                        for (int i = 0; i < positionItemList.size(); i++) {
                            FuturesAccountsResItem item = new FuturesAccountsResItem();
                            item.setCurrency(positionItemList.get(i).getUnderlying());
                            item.setAll_equity(all_equity / 100000000 + "");
                            item.setRealized_margin(positionItemList.get(i).getUnrealisedPnl() / 100000000 + "");
                            item.setUsed_margin(positionItemList.get(i).getMaintMargin() / 100000000 + positionItemList.get(i).getInitMargin() / 100000000 + "");
                            item.setFreezing_deposit(positionItemList.get(i).getInitMargin() / 100000000 + "");
                            item.setAvailable_margin(available_margin / 100000000 + "");
                            listItem.add(item);
                        }
                        return listItem;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObservableSubscriber<List<FuturesAccountsResItem>>(MineBindPresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        mView.onGetBitMexPositionError(message);
                    }

                    @Override
                    protected void onSuccessRes(List<FuturesAccountsResItem> value) {
                        mView.onGetBitMexPosition(value);
                    }
                });
    }
}


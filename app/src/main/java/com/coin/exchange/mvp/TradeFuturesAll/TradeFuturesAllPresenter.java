package com.coin.exchange.mvp.TradeFuturesAll;

import com.coin.exchange.adapter.groundrecycleradapter.Team;
import com.coin.exchange.model.bitMex.response.InstrumentItemRes;
import com.coin.exchange.model.okex.response.FuturesInstrumentsTickerList;
import com.coin.exchange.net.RetrofitFactory;
import com.coin.exchange.utils.AppUtils;
import com.coin.libbase.net.rxjava.RxObservableSubscriber;
import com.coin.libbase.presenter.BasePresenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public final class TradeFuturesAllPresenter extends BasePresenter<TradeFuturesAllView> {
    @Inject
    public TradeFuturesAllPresenter(TradeFuturesAllView view) {
        super(view);
    }

    public void getOkexFutures(final String from) {
        RetrofitFactory
                .getOkExApiService()
                .getFutInsTicker()
                .subscribeOn(Schedulers.io())
                .map(new Function<List<FuturesInstrumentsTickerList>, List<Team>>() {
                    @Override
                    public List<Team> apply(List<FuturesInstrumentsTickerList> lists) {
                        List<Team> teams = new ArrayList<>();
                        setSort(lists, from, teams);
                        for (int j = 0; j < teams.size(); j++) {
                            Collections.sort(teams.get(j).members, new SortByInstrumentId());
                        }
                        return teams;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObservableSubscriber<List<Team>>(TradeFuturesAllPresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        mView.onGetOkexFuturesError(message);
                    }

                    @Override
                    protected void onSuccessRes(List<Team> value) {
                        mView.onGetOkexFutures(value);
                    }
                });
    }

    public void getBitmexFutures(final String from) {
        RetrofitFactory
                .getBitMexApiService()
                .getInstrumentList()
                .subscribeOn(Schedulers.io())
                .map(new Function<List<InstrumentItemRes>, List<Team>>() {
                    @Override
                    public List<Team> apply(List<InstrumentItemRes> instrumentItemList) throws Exception {
                        List<Team> teams = new ArrayList<>();
                        List<FuturesInstrumentsTickerList> bitMexList = new ArrayList<>();
                        for (InstrumentItemRes item : instrumentItemList) {
                            FuturesInstrumentsTickerList itemVO = new FuturesInstrumentsTickerList();
                            itemVO.setVolume_24h((long) item.getForeignNotional24h());
                            itemVO.setTimestamp(item.getExpiry());
                            itemVO.setIndicativeSettlePrice(item.getIndicativeSettlePrice());
                            itemVO.setLast(item.getLastPrice());
                            itemVO.setLastChangePcnt(item.getLastChangePcnt());
                            itemVO.setRootName(item.getRootSymbol());
                            itemVO.setInstrument_id(item.getSymbol());
                            itemVO.setTyp(item.getTyp());
                            itemVO.setQuoteCurrency(item.getQuoteCurrency());
                            itemVO.setIsOkex(AppUtils.BITMEX);
                            if ((item.getTyp().equals("FFCCSX") || item.getTyp().equals("FFWCSX"))
                                    && item.getSymbol().contains(item.getRootSymbol())) {
                                bitMexList.add(itemVO);
                            }
                        }
                        setSort(bitMexList, from, teams);
                        return teams;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObservableSubscriber<List<Team>>(TradeFuturesAllPresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        mView.onGetBitmexFuturesError(message);
                    }

                    @Override
                    protected void onSuccessRes(List<Team> value) {
                        mView.onGetBitmexFutures(value);
                    }
                });
    }

    private void setSort(List<FuturesInstrumentsTickerList> futuresInstrumentsTickerLists,
                         String from, List<Team> teams) {
        //不能排序
        HashMap<String, List<FuturesInstrumentsTickerList>> name = new HashMap<>();
        for (int i = 0; i < futuresInstrumentsTickerLists.size(); i++) {
            String temp = futuresInstrumentsTickerLists.get(i).getInstrument_id().substring(0, 3);
            if (name.containsKey(temp)) {
                List<FuturesInstrumentsTickerList> ls = name.get(temp);
                ls.add(futuresInstrumentsTickerLists.get(i));
                name.put(temp, ls);
            } else {
                List<FuturesInstrumentsTickerList> tempList = new ArrayList<>();
                tempList.add(futuresInstrumentsTickerLists.get(i));
                name.put(temp, tempList);
            }
        }

        List<Team> sortlist = new ArrayList<>();
        for (Map.Entry<String, List<FuturesInstrumentsTickerList>> entry : name.entrySet()) {
            sortlist.add(new Team(entry.getKey(), entry.getValue()));
        }
        List<String> coin = null;
        if (from.equals(AppUtils.OKEX)) {
            coin = Arrays.asList("BTC", "ETH", "LTC", "ETC", "BCH", "XRP", "EOS", "BTG", "BSV");
        } else {
            coin = Arrays.asList("XBT", "ETH", "ADA", "BCH", "EOS", "LTC", "TRX", "XRP", "BSV");
        }
        for (int i = 0; i < coin.size(); i++) {
            for (int j = 0; j < sortlist.size(); j++) {
                if (sortlist.get(j).title.startsWith(coin.get(i))) {
                    teams.add(new Team(coin.get(i), sortlist.get(j).members));
                }
            }
        }
    }

    class SortByInstrumentId implements Comparator {
        public int compare(Object o1, Object o2) {
            FuturesInstrumentsTickerList s1 = (FuturesInstrumentsTickerList) o1;
            FuturesInstrumentsTickerList s2 = (FuturesInstrumentsTickerList) o2;
            return s1.getInstrument_id().compareTo(s2.getInstrument_id());
        }
    }
}

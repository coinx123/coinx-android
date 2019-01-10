package com.coin.exchange.mvp.KLine;

import com.coin.exchange.model.bitMex.response.InstrumentItemRes;
import com.coin.exchange.model.okex.response.FuturesInstrumentsTickerList;
import com.coin.exchange.model.okex.response.FuturesInstrumentsTradesList;
import com.coin.libbase.view.IView;

import java.util.List;

public interface KLineView extends IView {

    void onGetFuturesInfo(FuturesInstrumentsTickerList futuresInfo);

    void onGetFuturesInfoError(String msg);

    void onGetCandles(List<List<Double>> futuresInstrumentsTickerLists, double prise);

    void onGetCandlesError(String msg);

    void onGetFuturesInstrumentsTrades(List<FuturesInstrumentsTradesList> tradesLists);

    void onGetFuturesInstrumentsTradesError(String msg);

    //bitmex
    void onGetBitmexInstrument(List<InstrumentItemRes> instrumentItemResList);

    void onGetBitmexInstrumentError(String msg);

    void onGetBitmexTradeList(List<FuturesInstrumentsTradesList> futuresInstrumentsTradesLists);

    void onGetBitmexTradeListError(String msg);
}

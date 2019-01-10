package com.coin.exchange.mvp.TradeBusiness;

import com.coin.exchange.model.bitMex.response.InstrumentItemRes;
import com.coin.exchange.model.bitMex.response.OrderItemRes;
import com.coin.exchange.model.bitMex.response.PositionItemRes;
import com.coin.exchange.model.bitMex.response.UserMarginRes;
import com.coin.exchange.model.okex.response.FuturesAccountsCurrencyRes;
import com.coin.exchange.model.okex.response.FuturesCancelOrderRes;
import com.coin.exchange.model.okex.response.FuturesInstrumentsBookRes;
import com.coin.exchange.model.okex.response.FuturesInstrumentsIndexRes;
import com.coin.exchange.model.okex.response.FuturesInstrumentsTickerList;
import com.coin.exchange.model.okex.response.FuturesOrderRes;
import com.coin.exchange.model.okex.response.SingleFuturesPositionRes;
import com.coin.exchange.model.okex.vo.DelegationItemVO;
import com.coin.libbase.view.IView;

import java.util.List;

public interface TradeBusinessView extends IView {

    void onGetFuturesInfo(FuturesInstrumentsTickerList futuresInfo);

    void onGetFuturesInfoError(String msg);

    void onGetCandles(List<List<Double>> futuresInstrumentsTickerLists, double prise);

    void onGetCandlesError(String msg);

    void onGetFuturesPositionInfo(SingleFuturesPositionRes PositionRes);

    void onGetFuturesPositionInfoError(String msg);

    void onGetFuturesBook(FuturesInstrumentsBookRes futuresInstrumentsBookRes);

    void onGetFuturesBookError(String msg);

    void onGetFuturesIndex(FuturesInstrumentsIndexRes futuresInstrumentsIndexRes);

    void onGetFuturesIndexError(String msg);

    void onGetFuturesCurrencyInfo(FuturesAccountsCurrencyRes AccountsCurrencyRes);

    void onGetFuturesCurrencyInfoError(String msg);

    void onFuturesOrder(FuturesOrderRes futuresOrderRes);

    void onFuturesOrderError(String msg);

    void onCancelOrder(FuturesCancelOrderRes futuresCancelOrderRes);

    void onCancelOrderError(String msg);

    void onGetDelegationList(List<DelegationItemVO> delegationItemVOS);

    void onGetDelegationListError(String msg);

    //bitmex
    void onGetBitmexInstrument(List<InstrumentItemRes> instrumentItemResList);

    void onGetBitmexInstrumentError(String msg);

    void onGetBitmexPositionInfo(List<PositionItemRes> positionItemResList);

    void onGetBitmexPositionInfoError(String msg);

    void onGetBitmexUserMargin(UserMarginRes userMarginRes);

    void onGetBitmexUserMarginError(String msg);

    void onPostLeverage(PositionItemRes positionItemRes);

    void onPostLeverageError(String msg);

    void onCancelBitMexOrder(List<OrderItemRes> orderItemResList);

    void onCancelBitMexOrderError(String msg);
}

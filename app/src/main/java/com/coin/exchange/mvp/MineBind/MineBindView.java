package com.coin.exchange.mvp.MineBind;

import com.coin.exchange.model.bitMex.response.InstrumentItemRes;
import com.coin.exchange.model.bitMex.response.UserMarginRes;
import com.coin.exchange.model.okex.response.FuturesAccountsResItem;
import com.coin.libbase.view.IView;

import java.util.List;

public interface MineBindView extends IView {

    void onGetOkexFutures(Double value);

    void onGetOkexFuturesError(String msg);

    void onGetAssetsValue(List<FuturesAccountsResItem> futuresAccountsResItemList);

    void onGetAssetsValueError(String msg);

    void onGetSpotTickerSing(Double value);

    void onGetSpotTickerSingError(String msg);

    void onGetBitmexUserMargin(UserMarginRes userMarginRes);

    void onGetBitmexUserMarginError(String msg);

    void onGetBitmexInstrument(List<InstrumentItemRes> instrumentItemResList);

    void onGetBitmexInstrumentError(String msg);

    void onGetBitMexPosition(List<FuturesAccountsResItem> futuresAccountsResItemList);

    void onGetBitMexPositionError(String msg);
}

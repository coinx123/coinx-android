package com.coin.exchange.mvp.TradeFuturesAll;

import com.coin.libbase.view.IView;
import com.coin.exchange.adapter.groundrecycleradapter.Team;

import java.util.List;

public interface TradeFuturesAllView extends IView {

    void onGetOkexFutures(List<Team> teamList);

    void onGetOkexFuturesError(String msg);

    void onGetBitmexFutures(List<Team> teamList);

    void onGetBitmexFuturesError(String msg);
}

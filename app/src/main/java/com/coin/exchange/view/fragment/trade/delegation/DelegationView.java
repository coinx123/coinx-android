package com.coin.exchange.view.fragment.trade.delegation;

import com.coin.exchange.model.okex.response.FuturesInstrumentsTickerList;
import com.coin.libbase.view.IView;

import java.util.List;
import java.util.Map;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/27
 * @description
 */
public interface DelegationView extends IView {


    void onGetIconInfo(Map<String, List<FuturesInstrumentsTickerList>> map);

}

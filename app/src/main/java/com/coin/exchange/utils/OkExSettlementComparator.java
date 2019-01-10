package com.coin.exchange.utils;


import com.coin.exchange.model.okex.response.FuturesInstrumentsTickerList;

import java.util.Comparator;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/20
 * @description
 */
public class OkExSettlementComparator implements Comparator<FuturesInstrumentsTickerList> {

    @Override
    public int compare(FuturesInstrumentsTickerList o1, FuturesInstrumentsTickerList o2) {

        if (o1.getDayTime() > o2.getDayTime()) {
            return 1;
        } else if (o1.getDayTime() == o2.getDayTime()) {
            return 0;
        }
        return -1;

    }
}

package com.coin.exchange.utils;


import com.coin.exchange.model.okex.response.FuturesInstrumentsTickerList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/20
 * @description
 */
public class OkExTickerDayTimeComparator implements Comparator<FuturesInstrumentsTickerList> {

    @Override
    public int compare(FuturesInstrumentsTickerList o1, FuturesInstrumentsTickerList o2) {
        if (o1.getDayTime() > o2.getDayTime()) {
            return 1;
        } else if (o1.getDayTime() == o2.getDayTime()) {
            return 0;
        }
        return -1;
    }

    public static void main(String[] args) {
        OkExTickerDayTimeComparator okExTickerDayTimeComparator = new OkExTickerDayTimeComparator();

        List<FuturesInstrumentsTickerList> list = new ArrayList<>();

        FuturesInstrumentsTickerList item1 = new FuturesInstrumentsTickerList();
        item1.setDayTime(181228);
        list.add(item1);

        FuturesInstrumentsTickerList item2 = new FuturesInstrumentsTickerList();
        item2.setDayTime(181126);
        list.add(item2);

        FuturesInstrumentsTickerList item3 = new FuturesInstrumentsTickerList();
        item3.setDayTime(190127);
        list.add(item3);

        FuturesInstrumentsTickerList item4 = new FuturesInstrumentsTickerList();
        item4.setDayTime(190111);
        list.add(item4);

        System.out.println("before: " + list);
        Collections.sort(list, okExTickerDayTimeComparator);

        System.out.println("after: " + list);

    }

}

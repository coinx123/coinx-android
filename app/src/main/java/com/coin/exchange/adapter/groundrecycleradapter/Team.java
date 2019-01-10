package com.coin.exchange.adapter.groundrecycleradapter;

import com.coin.exchange.model.okex.response.FuturesInstrumentsTickerList;

import java.util.List;



public class Team {
    public final String title;
    public final List<FuturesInstrumentsTickerList> members;

    public Team(String title, List<FuturesInstrumentsTickerList> members) {
        this.title = title;
        this.members = members;
    }
}

package com.coin.exchange.model.okex.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author dean
 * @date 创建时间：2018/11/6
 * @description
 */
public class FuturesInstrumentsBookRes {

    @SerializedName("symbol")
    private String symbol;

    @SerializedName("timestamp")
    private String timestamp;
    //买单深度
    @SerializedName("bids")
    private List<List<Double>> bids;
    //卖单深度
    @SerializedName("asks")
    private List<List<Double>> asks;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public List<List<Double>> getBids() {
        return bids;
    }

    public void setBids(List<List<Double>> bids) {
        this.bids = bids;
    }

    public List<List<Double>> getAsks() {
        return asks;
    }

    public void setAsks(List<List<Double>> asks) {
        this.asks = asks;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

}

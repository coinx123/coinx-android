package com.coin.exchange.model.bitMex.response;

import com.google.gson.annotations.SerializedName;

/**
 * @author dean
 * @date 创建时间：2018/12/22
 * @description
 */
public class bucketetRes {

    @SerializedName("foreignNotional")
    private double foreignNotional;
    @SerializedName("homeNotional")
    private double homeNotional;
    @SerializedName("turnover")
    private double turnover;
    @SerializedName("lastSize")
    private double lastSize;
    @SerializedName("vwap")
    private double vwap;
    @SerializedName("volume")
    private double volume;
    @SerializedName("trades")
    private double trades;
    @SerializedName("close")
    private double close;
    @SerializedName("low")
    private double low;
    @SerializedName("high")
    private double high;
    @SerializedName("open")
    private double open;
    @SerializedName("symbol")
    private String symbol;
    @SerializedName("timestamp")
    private String timestamp;

    public double getForeignNotional() {
        return foreignNotional;
    }

    public void setForeignNotional(double foreignNotional) {
        this.foreignNotional = foreignNotional;
    }

    public double getHomeNotional() {
        return homeNotional;
    }

    public void setHomeNotional(double homeNotional) {
        this.homeNotional = homeNotional;
    }

    public double getTurnover() {
        return turnover;
    }

    public void setTurnover(double turnover) {
        this.turnover = turnover;
    }

    public double getLastSize() {
        return lastSize;
    }

    public void setLastSize(double lastSize) {
        this.lastSize = lastSize;
    }

    public double getVwap() {
        return vwap;
    }

    public void setVwap(double vwap) {
        this.vwap = vwap;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getTrades() {
        return trades;
    }

    public void setTrades(double trades) {
        this.trades = trades;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

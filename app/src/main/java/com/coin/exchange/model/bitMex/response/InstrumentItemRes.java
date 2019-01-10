package com.coin.exchange.model.bitMex.response;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/16
 * @description
 */
public class InstrumentItemRes implements Comparable<InstrumentItemRes> {

    @SerializedName("symbol")
    private String symbol;

    // 合约币种
    @SerializedName("rootSymbol")
    private String rootSymbol;

    // 24小时变换率
    @SerializedName("lastChangePcnt")
    private double lastChangePcnt;

    // 最新价格
    @SerializedName("lastPrice")
    private double lastPrice;

    @SerializedName("quoteCurrency")
    private String quoteCurrency;

    // 最近24小时交易量
    @SerializedName("foreignNotional24h")
    private double foreignNotional24h;

    @SerializedName("expiry")
    private String expiry;

    // 指数价格
    @SerializedName("indicativeSettlePrice")
    private double indicativeSettlePrice;

    // 类型
    @SerializedName("typ")
    private String typ;

    // 市值
    @SerializedName("totalVolume")
    private double totalVolume;

    // 24H高
    @SerializedName("highPrice")
    private double highPrice;

    // 24H低
    @SerializedName("lowPrice")
    private double lowPrice;

    @SerializedName("timestamp")
    private String timestamp;

    public double getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(double totalVolume) {
        this.totalVolume = totalVolume;
    }

    public double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(double highPrice) {
        this.highPrice = highPrice;
    }

    public double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(double lowPrice) {
        this.lowPrice = lowPrice;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getRootSymbol() {
        return rootSymbol;
    }

    public void setRootSymbol(String rootSymbol) {
        this.rootSymbol = rootSymbol;
    }

    public double getLastChangePcnt() {
        return lastChangePcnt;
    }

    public void setLastChangePcnt(double lastChangePcnt) {
        this.lastChangePcnt = lastChangePcnt;
    }

    public double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public String getQuoteCurrency() {
        return quoteCurrency;
    }

    public void setQuoteCurrency(String quoteCurrency) {
        this.quoteCurrency = quoteCurrency;
    }

    public double getForeignNotional24h() {
        return foreignNotional24h;
    }

    public void setForeignNotional24h(double foreignNotional24h) {
        this.foreignNotional24h = foreignNotional24h;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public double getIndicativeSettlePrice() {
        return indicativeSettlePrice;
    }

    public void setIndicativeSettlePrice(double indicativeSettlePrice) {
        this.indicativeSettlePrice = indicativeSettlePrice;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int compareTo(@NonNull InstrumentItemRes o) {
        if (getLastChangePcnt() > o.getLastChangePcnt()) {
            return -1;
        }
        return 1;
    }
}

package com.coin.exchange.model.okex.response;

import android.support.annotation.NonNull;

import com.coin.exchange.utils.AppUtils;
import com.google.gson.annotations.SerializedName;

/**
 * @author dean
 * @date 创建时间：2018/11/7
 * @description
 */
public class FuturesInstrumentsTickerList implements Comparable<FuturesInstrumentsTickerList> {
    // 24小时成交量，按张数统计
    @SerializedName("volume_24h")
    private long volume_24h;
    // 系统时间戳
    @SerializedName("timestamp")
    private String timestamp;
    // 24小时最低价
    @SerializedName("low_24h")
    private double low_24h;
    // 最新成交价
    @SerializedName("last")
    private double last;
    // 合约ID，如BTC-USD-180213
    @SerializedName("instrument_id")
    private String instrument_id;
    // 24小时最高价
    @SerializedName("high_24h")
    private double high_24h;
    // 买一价
    @SerializedName("best_bid")
    private double best_bid;
    // 卖一价
    @SerializedName("best_ask")
    private double best_ask;

    private String displayName;

//    private int settlementDate;

    // 币种类型 两个时：当周、当季；三个时：次周、当周、当季
    private String type;
    // 根名称 例如：BTC
    private String rootName;
    // 详情名称 例如：1228
    private String desName;
    // 时间
    private int dayTime;
    private String typ;
    private double indicativeSettlePrice;//指数价格
    private String isOkex = AppUtils.OKEX;
    private double lastChangePcnt;// 24小时变换率
    private String quoteCurrency; //结算币种

    public String getQuoteCurrency() {
        return quoteCurrency;
    }

    public void setQuoteCurrency(String quoteCurrency) {
        this.quoteCurrency = quoteCurrency;
    }

    public double getLastChangePcnt() {
        return lastChangePcnt;
    }

    public void setLastChangePcnt(double lastChangePcnt) {
        this.lastChangePcnt = lastChangePcnt;
    }


    public String getIsOkex() {
        return isOkex;
    }

    public void setIsOkex(String isOkex) {
        this.isOkex = isOkex;
    }


    public double getIndicativeSettlePrice() {
        return indicativeSettlePrice;
    }

    public void setIndicativeSettlePrice(double indicativeSettlePrice) {
        this.indicativeSettlePrice = indicativeSettlePrice;
    }


    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }


    public long getVolume_24h() {
        return volume_24h;
    }

    public void setVolume_24h(long volume_24h) {
        this.volume_24h = volume_24h;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public double getLow_24h() {
        return low_24h;
    }

    public void setLow_24h(double low_24h) {
        this.low_24h = low_24h;
    }

    public double getLast() {
        return last;
    }

    public void setLast(double last) {
        this.last = last;
    }

    public String getInstrument_id() {
        return instrument_id;
    }

    public void setInstrument_id(String instrument_id) {
        this.instrument_id = instrument_id;
    }

    public double getHigh_24h() {
        return high_24h;
    }

    public void setHigh_24h(double high_24h) {
        this.high_24h = high_24h;
    }

    public double getBest_bid() {
        return best_bid;
    }

    public void setBest_bid(double best_bid) {
        this.best_bid = best_bid;
    }

    public double getBest_ask() {
        return best_ask;
    }

    public void setBest_ask(double best_ask) {
        this.best_ask = best_ask;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

//    public int getSettlementDate() {
//        return settlementDate;
//    }
//
//    public void setSettlementDate(int settlementDate) {
//        this.settlementDate = settlementDate;
//    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRootName() {
        return rootName;
    }

    public void setRootName(String rootName) {
        this.rootName = rootName;
    }

    public String getDesName() {
        return desName;
    }

    public void setDesName(String desName) {
        this.desName = desName;
    }

    public int getDayTime() {
        return dayTime;
    }

    public void setDayTime(int dayTime) {
        this.dayTime = dayTime;
    }

    @Override
    public String toString() {
        return "FuturesInstrumentsTickerList{" +
                ", dayTime=" + dayTime +
                '}';
    }

    @Override
    public int compareTo(@NonNull FuturesInstrumentsTickerList o) {
        if (last > o.getLast()) {
            return -1;
        }

        return 1;
    }
}

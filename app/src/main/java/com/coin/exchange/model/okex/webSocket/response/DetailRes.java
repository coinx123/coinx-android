package com.coin.exchange.model.okex.webSocket.response;

import com.google.gson.annotations.SerializedName;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/6
 * @description 合约行情数据
 */
public class DetailRes {

    // 最高买入限制价格
    @SerializedName("limitHigh")
    private String limitHigh;

    // 24小时成交量 TODO 对应restapi v3 >合约api>获取ticke信息>  volume_24h （24小时成交量。按张数统计）
    @SerializedName("vol")
    private int vol;

    // 最新价格
    @SerializedName("last")
    private double last;

    // 卖一价格    TODO 对应restapi v3> 合约api>获取ticker信息> best_ask  ( 卖一价)
    @SerializedName("sell")
    private double sell;

    // 买一价格    TODO 对应restapi v3 > 合约api> 获取ticker信息> best_bid   (买一价)
    @SerializedName("buy")
    private double buy;

    // 合约价值
    @SerializedName("unitAmount")
    private int unitAmount;

    // 当前持仓量   TODO 对应restapi v3>合约api>获取平台总持仓量> amount （总持仓量）
    @SerializedName("hold_amount")
    private int hold_amount;

    // 合约ID      TODO 对应restapi v3>合约api>获取ticker信息> instrument_id ( 合约ID)。
    @SerializedName("contractId")
    private String contractId;

    // 24小时最高价格 TODO 对应restapi v3>合约api>获取ticker信息> high_24h  (24小时最高价)
    @SerializedName("high")
    private double high;

    // 24小时最低价格 TODO 对应restapi v3>合约api>获取ticker信息>  low_24h   (24小时最低价)
    @SerializedName("low")
    private double low;

    // 最低卖出限制价
    @SerializedName("limitLow")
    private String limitLow;

    public String getLimitHigh() {
        return limitHigh;
    }

    public void setLimitHigh(String limitHigh) {
        this.limitHigh = limitHigh;
    }

    public int getVol() {
        return vol;
    }

    public void setVol(int vol) {
        this.vol = vol;
    }

    public double getLast() {
        return last;
    }

    public void setLast(double last) {
        this.last = last;
    }

    public double getSell() {
        return sell;
    }

    public void setSell(double sell) {
        this.sell = sell;
    }

    public double getBuy() {
        return buy;
    }

    public void setBuy(double buy) {
        this.buy = buy;
    }

    public int getUnitAmount() {
        return unitAmount;
    }

    public void setUnitAmount(int unitAmount) {
        this.unitAmount = unitAmount;
    }

    public int getHold_amount() {
        return hold_amount;
    }

    public void setHold_amount(int hold_amount) {
        this.hold_amount = hold_amount;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public String getLimitLow() {
        return limitLow;
    }

    public void setLimitLow(String limitLow) {
        this.limitLow = limitLow;
    }

    @Override
    public String toString() {
        return "DetailRes{" +
                "limitHigh='" + limitHigh + '\'' +
                ", vol=" + vol +
                ", last=" + last +
                ", sell=" + sell +
                ", buy=" + buy +
                ", unitAmount=" + unitAmount +
                ", hold_amount=" + hold_amount +
                ", contractId='" + contractId + '\'' +
                ", high=" + high +
                ", low=" + low +
                ", limitLow='" + limitLow + '\'' +
                '}';
    }
}

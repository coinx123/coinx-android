package com.coin.exchange.model.okex.response;

import com.google.gson.annotations.SerializedName;

/**
 * @author dean
 * @date 创建时间：2018/11/6
 * @description 获取合约信息
 */
public class FuturesInstrumentsRes {
    //交易货币币种，如：btc-usd中的btc
    @SerializedName("underlying_index")
    private String underlying_index;
    //	下单数量精度,当trade_increment为1，委托数量传入1.21将被系统按截尾法修正为1。合约trade_increment值为1
    @SerializedName("trade_increment")
    private int trade_increment;
    //	下单价格精度,委托价格必须是tick_size的倍数。例如，当tick_size为0.01，委托价格传入0.022将被系统按截尾法修正为0.02。
    @SerializedName("tick_size")
    private double tick_size;
    //计价货币币种，如：btc-usd中的usd
    @SerializedName("quote_currency")
    private String quote_currency;
    //上线日期
    @SerializedName("listing")
    private String listing;
    //	合约ID，如BTC-USD-180213
    @SerializedName("instrument_id")
    private String instrument_id;
    //交割日期
    @SerializedName("delivery")
    private String delivery;
    //	合约面值(美元)
    @SerializedName("contract_val")
    private int contract_val;

    public String getUnderlying_index() {
        return underlying_index;
    }

    public void setUnderlying_index(String underlying_index) {
        this.underlying_index = underlying_index;
    }

    public int getTrade_increment() {
        return trade_increment;
    }

    public void setTrade_increment(int trade_increment) {
        this.trade_increment = trade_increment;
    }

    public double getTick_size() {
        return tick_size;
    }

    public void setTick_size(double tick_size) {
        this.tick_size = tick_size;
    }

    public String getQuote_currency() {
        return quote_currency;
    }

    public void setQuote_currency(String quote_currency) {
        this.quote_currency = quote_currency;
    }

    public String getListing() {
        return listing;
    }

    public void setListing(String listing) {
        this.listing = listing;
    }

    public String getInstrument_id() {
        return instrument_id;
    }

    public void setInstrument_id(String instrument_id) {
        this.instrument_id = instrument_id;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public int getContract_val() {
        return contract_val;
    }

    public void setContract_val(int contract_val) {
        this.contract_val = contract_val;
    }

    @Override
    public String toString() {
        return "FuturesInstrumentsRes{" +
                "underlying_index='" + underlying_index + '\'' +
                ", trade_increment=" + trade_increment +
                ", tick_size=" + tick_size +
                ", quote_currency='" + quote_currency + '\'' +
                ", listing='" + listing + '\'' +
                ", instrument_id='" + instrument_id + '\'' +
                ", delivery='" + delivery + '\'' +
                ", contract_val=" + contract_val +
                '}';
    }
}

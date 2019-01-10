package com.coin.exchange.model.okex.response;

import com.google.gson.annotations.SerializedName;

/**
 * @author dean
 * @date 创建时间：2018/11/7
 * @description
 */
public class FuturesInstrumentsOpenInterestRes {
    //	合约ID，如BTC-USD-180213
    @SerializedName("timestamp")
    private String timestamp;
    //系统时间戳
    @SerializedName("instrument_id")
    private String instrument_id;
    //	总持仓量
    @SerializedName("amount")
    private int amount;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getInstrument_id() {
        return instrument_id;
    }

    public void setInstrument_id(String instrument_id) {
        this.instrument_id = instrument_id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "FuturesInstrumentsOpenInterestRes{" +
                "timestamp='" + timestamp + '\'' +
                ", instrument_id='" + instrument_id + '\'' +
                ", amount=" + amount +
                '}';
    }
}

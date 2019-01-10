package com.coin.exchange.model.okex.response;

import com.google.gson.annotations.SerializedName;

/**
 * @author dean
 * @date 创建时间：2018/11/12
 * @description
 */
public class FuturesRateRes {
    @SerializedName("rate")
    public String rate;
    @SerializedName("instrument_id")
    public String instrument_id;

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getInstrument_id() {
        return instrument_id;
    }

    public void setInstrument_id(String instrument_id) {
        this.instrument_id = instrument_id;
    }

    @Override
    public String toString() {
        return "FuturesRateRes{" +
                "rate='" + rate + '\'' +
                ", instrument_id='" + instrument_id + '\'' +
                '}';
    }
}

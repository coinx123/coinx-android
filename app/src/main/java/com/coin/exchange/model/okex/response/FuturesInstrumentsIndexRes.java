package com.coin.exchange.model.okex.response;

import com.google.gson.annotations.SerializedName;

/**
 * @author dean
 * @date 创建时间：2018/11/6
 * @description
 */
public class FuturesInstrumentsIndexRes {
    //系统时间戳
    @SerializedName("timestamp")
    private String timestamp;
    //	指数币对，如BTC-USD(应该及时合约id)
    @SerializedName("instrument_id")
    private String instrument_id;
    //	指数价格
    @SerializedName("index")
    private double index;

    //	推送的指数价格（还有美元兑换人民币的字段，需要的话可以取）
    @SerializedName("futureIndex")
    private double futureIndex;

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

    public double getIndex() {
        return index;
    }

    public void setIndex(double index) {
        this.index = index;
    }

    public double getFutureIndex() {
        return futureIndex;
    }

    public void setFutureIndex(double futureIndex) {
        this.futureIndex = futureIndex;
    }

    @Override
    public String toString() {
        return "FuturesInstrumentsIndexRes{" +
                "timestamp='" + timestamp + '\'' +
                ", instrument_id='" + instrument_id + '\'' +
                ", index=" + index +
                ", futureIndex=" + futureIndex +
                '}';
    }
}

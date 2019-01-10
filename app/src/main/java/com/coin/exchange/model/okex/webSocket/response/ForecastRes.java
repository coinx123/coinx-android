package com.coin.exchange.model.okex.webSocket.response;

import com.google.gson.annotations.SerializedName;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/7
 * @description
 */
public class ForecastRes {

    @SerializedName("channel")
    private String channel;

    // 时间戳
    @SerializedName("timestamp")
    private String timestamp;

    // 预估交割价格   TODO 对应rest api v3>获取预估交割价>estimated_price  预估价格
    @SerializedName("data")
    private String data;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

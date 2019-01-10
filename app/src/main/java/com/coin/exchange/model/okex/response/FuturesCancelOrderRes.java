package com.coin.exchange.model.okex.response;

import com.google.gson.annotations.SerializedName;

/**
 * @author dean
 * @date 创建时间：2018/11/6
 * @description
 */
public class FuturesCancelOrderRes {

    @SerializedName("timestamp")
    private String timestamp;
    @SerializedName("result")
    private boolean result;
    @SerializedName("order_id")
    private String order_id;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    @Override
    public String toString() {
        return "FuturesCancelOrderRes{" +
                "timestamp='" + timestamp + '\'' +
                ", result='" + result + '\'' +
                ", order_id='" + order_id + '\'' +
                '}';
    }
}

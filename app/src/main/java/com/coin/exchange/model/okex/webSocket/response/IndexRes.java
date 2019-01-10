package com.coin.exchange.model.okex.webSocket.response;

import com.google.gson.annotations.SerializedName;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/6
 * @description 订阅合约指数
 */
public class IndexRes {

    // 时间戳
    @SerializedName("timestamp")
    private String timestamp;

    // 指数 TODO 对应rest pai v3> 获取指数信息 >index 指数价格
    @SerializedName("futureIndex")
    private String futureIndex;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getFutureIndex() {
        return futureIndex;
    }

    public void setFutureIndex(String futureIndex) {
        this.futureIndex = futureIndex;
    }
}

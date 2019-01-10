package com.coin.exchange.model.okex.webSocket.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/6
 * @description 订阅合约市场深度(200增量数据返回)
 * <p>
 * 1、第一次返回全量数据
 * 2、根据接下来数据对第一次返回数据进行，如下操作
 * 删除（量为0时）
 * 修改（价格相同量不同）
 * 增加（价格不存在）
 */
public class DepthRes {

    // 服务器时间戳
    @SerializedName("timestamp")
    private String timestamp;

    // 卖单深度 数组索引(string) 0 价格, 1 量(张), 2 量(币) 3, 累计量(币)
    @SerializedName("asks")
    private List<List<String>> asks;

    // bids(array):买单深度 数组索引(string) 0 价格, 1 量(张), 2 量(币) 3, 累计量(币)
    @SerializedName("bids")
    private List<List<String>> bids;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public List<List<String>> getAsks() {
        return asks;
    }

    public void setAsks(List<List<String>> asks) {
        this.asks = asks;
    }

    public List<List<String>> getBids() {
        return bids;
    }

    public void setBids(List<List<String>> bids) {
        this.bids = bids;
    }
}

package com.coin.exchange.model.okex.response;

import com.google.gson.annotations.SerializedName;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/1
 * @description
 */
public class TimeStampInfoRes {

    // ISO8601标准的时间格式
    @SerializedName("iso")
    private String iso;

    // UTC时区Unix时间戳的十进制秒数格式
    @SerializedName("epoch")
    private String epoch;

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getEpoch() {
        return epoch;
    }

    public void setEpoch(String epoch) {
        this.epoch = epoch;
    }

    @Override
    public String toString() {
        return "TimeStampInfoRes{" +
                "iso='" + iso + '\'' +
                ", epoch='" + epoch + '\'' +
                '}';
    }
}

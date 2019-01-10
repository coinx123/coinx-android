package com.coin.exchange.model.okex.request;

import com.google.gson.annotations.SerializedName;

/**
 * @author dean
 * @date 创建时间：2018/11/6
 * @description 下单
 */
public class FuturesOrderReq {
    //	要设定的杠杆倍数，10或20
    @SerializedName("leverage")
    private String leverage;
    //是否以对手价下单(0:不是 1:是)，默认为0，当取值为1时。price字段无效
    @SerializedName("match_price")
    private String match_price;
    //买入或卖出合约的数量（以张计数）
    @SerializedName("size")
    private String size;
    //	每张合约的价格
    @SerializedName("price")
    private String price;
    //	1:开多2:开空3:平多4:平空
    @SerializedName("type")
    private String type;
    // 合约ID，如BTC-USD-180213
    @SerializedName("instrument_id")
    private String instrument_id;

    public FuturesOrderReq(String leverage,
                           String match_price,
                           String size,
                           String price,
                           String type,
                           String instrument_id) {
        this.leverage = leverage;
        this.match_price = match_price;
        this.size = size;
        this.price = price;
        this.type = type;
        this.instrument_id = instrument_id;
    }

    public String getLeverage() {
        return leverage;
    }

    public void setLeverage(String leverage) {
        this.leverage = leverage;
    }

    public String getMatch_price() {
        return match_price;
    }

    public void setMatch_price(String match_price) {
        this.match_price = match_price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInstrument_id() {
        return instrument_id;
    }

    public void setInstrument_id(String instrument_id) {
        this.instrument_id = instrument_id;
    }

    @Override
    public String toString() {
        return "FuturesOrderReq{" +
                "leverage='" + leverage + '\'' +
                ", match_price='" + match_price + '\'' +
                ", size='" + size + '\'' +
                ", price='" + price + '\'' +
                ", type='" + type + '\'' +
                ", instrument_id='" + instrument_id + '\'' +
                '}';
    }
}


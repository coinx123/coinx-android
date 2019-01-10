package com.coin.exchange.model.okex.response;

import com.google.gson.annotations.SerializedName;

/**
 * @author dean
 * @date 创建时间：2018/11/7
 * @description
 */
public class FuturesInstrumentsTradesList {
    //	成交ID
    @SerializedName("trade_id")
    private String trade_id;
    //	合约ID bitmex
    @SerializedName("symbol")
    private String symbol;
    //	成交时间
    @SerializedName("timestamp")
    private String timestamp;
    //	成交方向
    @SerializedName("side")
    private String side;
    //成交数量  bitmex
    @SerializedName("size")
    private int size;
    //成交数量
    @SerializedName("qty")
    private int qty;
    //	成交价格
    @SerializedName("price")
    private double price;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getTrade_id() {
        return trade_id;
    }

    public void setTrade_id(String trade_id) {
        this.trade_id = trade_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "FuturesInstrumentsTradesList{" +
                "trade_id='" + trade_id + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", side='" + side + '\'' +
                ", qty=" + qty +
                ", price=" + price +
                '}';
    }
}

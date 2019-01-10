package com.coin.exchange.model.bitMex.request;

import com.google.gson.annotations.SerializedName;

/**
 * @author dean
 * @date 创建时间：2018/12/19
 * @description 调整杠杆的请求值
 */
public class LeverageReq {
    //合约id
    @SerializedName("symbol")
    private String symbol;

    // 杠杆
    @SerializedName("leverage")
    private double leverage;

    public LeverageReq(String symbol, double leverage) {
        this.symbol = symbol;
        this.leverage = leverage;
    }

    public double getLeverage() {
        return leverage;
    }

    public void setLeverage(double leverage) {
        this.leverage = leverage;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}

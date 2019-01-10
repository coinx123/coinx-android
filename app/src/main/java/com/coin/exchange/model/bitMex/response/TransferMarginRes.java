package com.coin.exchange.model.bitMex.response;

import com.google.gson.annotations.SerializedName;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/12/19
 * @description 调整保证金的返回值
 */
public class TransferMarginRes {
    @SerializedName("symbol")
    private String symbol;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}

package com.coin.exchange.model.bitMex.request;

import com.google.gson.annotations.SerializedName;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/12/19
 * @description 调整保证金的请求值
 */
public class TransferMarginReq {
    @SerializedName("symbol")
    private String symbol;

    // 调整保证金的值，+为增加；-为减少
    @SerializedName("amount")
    private String amount;

    public TransferMarginReq(String symbol, String amount) {
        this.symbol = symbol;
        this.amount = amount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}

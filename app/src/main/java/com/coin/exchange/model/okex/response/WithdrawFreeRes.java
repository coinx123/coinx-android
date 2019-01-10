package com.coin.exchange.model.okex.response;

import com.google.gson.annotations.SerializedName;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/2
 * @description 提现响应
 */
public class WithdrawFreeRes {

    // 币种
    @SerializedName("currency")
    private String currency;

    // 最大提币手续费数量
    @SerializedName("max_fee")
    private double max_fee;

    // 最小提币手续费数量
    @SerializedName("min_fee")
    private double min_fee;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getMax_fee() {
        return max_fee;
    }

    public void setMax_fee(double max_fee) {
        this.max_fee = max_fee;
    }

    public double getMin_fee() {
        return min_fee;
    }

    public void setMin_fee(double min_fee) {
        this.min_fee = min_fee;
    }
}

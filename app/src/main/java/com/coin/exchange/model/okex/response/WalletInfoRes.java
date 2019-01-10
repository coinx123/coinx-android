package com.coin.exchange.model.okex.response;

import com.google.gson.annotations.SerializedName;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/1
 * @description 币种账户信息
 */
public class WalletInfoRes {

    // 可用于提现或资金划转的数量
    @SerializedName("available")
    private double available;

    // 余额
    @SerializedName("balance")
    private double balance;

    // 币种，如btc
    @SerializedName("currency")
    private String currency;

    // 冻结(不可用)
    @SerializedName("hold")
    private int hold;

    public double getAvailable() {
        return available;
    }

    public void setAvailable(double available) {
        this.available = available;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getHold() {
        return hold;
    }

    public void setHold(int hold) {
        this.hold = hold;
    }

    @Override
    public String toString() {
        return "WalletInfoRes{" +
                "available=" + available +
                ", balance=" + balance +
                ", currency='" + currency + '\'' +
                ", hold=" + hold +
                '}';
    }
}

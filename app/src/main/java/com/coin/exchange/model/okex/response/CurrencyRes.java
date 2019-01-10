package com.coin.exchange.model.okex.response;

import com.google.gson.annotations.SerializedName;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/2
 * @description 币种信息
 */
public class CurrencyRes {

    // 是否可充值，0表示不可充值，1表示可以充值
    @SerializedName("can_deposit")
    private int can_deposit;

    // 是否可提币，0表示不可提币，1表示可以提币
    @SerializedName("can_withdraw")
    private int can_withdraw;

    // 币种名称，如btc
    @SerializedName("currency")
    private String currency;

    // 币种最小提币量
    @SerializedName("min_withdrawal")
    private double min_withdrawal;

    // 币种中文名称，不显示则无对应名称
    @SerializedName("name")
    private String name;

    public int getCan_deposit() {
        return can_deposit;
    }

    public void setCan_deposit(int can_deposit) {
        this.can_deposit = can_deposit;
    }

    public int getCan_withdraw() {
        return can_withdraw;
    }

    public void setCan_withdraw(int can_withdraw) {
        this.can_withdraw = can_withdraw;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getMin_withdrawal() {
        return min_withdrawal;
    }

    public void setMin_withdrawal(double min_withdrawal) {
        this.min_withdrawal = min_withdrawal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CurrencyRes{" +
                "can_deposit=" + can_deposit +
                ", can_withdraw=" + can_withdraw +
                ", currency='" + currency + '\'' +
                ", min_withdrawal=" + min_withdrawal +
                ", name='" + name + '\'' +
                '}';
    }
}

package com.coin.exchange.model.okex.webSocket.response;

import com.google.gson.annotations.SerializedName;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/7
 * @description 合约账户信息——全仓返回
 */
public class AllInRes {

    // 账户余额
    @SerializedName("balance")
    private double balance;

    // 币种
    @SerializedName("symbol")
    private String symbol;

    // 保证金。 TODO 对应rest v3>合约账户信息 > margin 保证金
    @SerializedName("keep_deposit")
    private double keep_deposit;

    // 已实现盈亏 TODO 对应rest v3>合约账户信息>realized_pnl  已实现盈亏
    @SerializedName("profit_real")
    private double profit_real;

    // 合约价值
    @SerializedName("unit_amount")
    private int unit_amount;

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getKeep_deposit() {
        return keep_deposit;
    }

    public void setKeep_deposit(double keep_deposit) {
        this.keep_deposit = keep_deposit;
    }

    public double getProfit_real() {
        return profit_real;
    }

    public void setProfit_real(double profit_real) {
        this.profit_real = profit_real;
    }

    public int getUnit_amount() {
        return unit_amount;
    }

    public void setUnit_amount(int unit_amount) {
        this.unit_amount = unit_amount;
    }
}

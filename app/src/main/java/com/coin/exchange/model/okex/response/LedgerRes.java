package com.coin.exchange.model.okex.response;

import com.google.gson.annotations.SerializedName;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/2
 * @description 账单流水项
 */
public class LedgerRes {

    // 变动数量
    @SerializedName("amount")
    private double amount;

    // 余额
    @SerializedName("balance")
    private int balance;

    // 币种
    @SerializedName("currency")
    private String currency;

    // 手续费
    @SerializedName("fee")
    private int fee;

    // 账单ID
    @SerializedName("ledger_id")
    private int ledger_id;

    // 账单创建时间
    @SerializedName("timestamp")
    private String timestamp;

    // 账单类型
    @SerializedName("typename")
    private String typename;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public int getLedger_id() {
        return ledger_id;
    }

    public void setLedger_id(int ledger_id) {
        this.ledger_id = ledger_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }
}

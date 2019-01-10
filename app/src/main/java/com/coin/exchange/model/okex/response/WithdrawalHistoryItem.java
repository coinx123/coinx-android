package com.coin.exchange.model.okex.response;

import com.google.gson.annotations.SerializedName;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/2
 * @description 充值历史项
 */
public class WithdrawalHistoryItem {

    // 充值数量
    @SerializedName("amount")
    private double amount;

    // 区块转账哈希记录
    @SerializedName("txid")
    private String txid;

    // 币种名称，如：btc
    @SerializedName("currency")
    private String currency;

    // 此笔充值到账地址
    @SerializedName("to")
    private String to;

    // 充值到账时间
    @SerializedName("timestamp")
    private String timestamp;

    // 提现状态（0:等待确认;1:确认到账;2:充值成功；）
    @SerializedName("status")
    private int status;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

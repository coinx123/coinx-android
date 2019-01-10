package com.coin.exchange.model.okex.response;

import com.google.gson.annotations.SerializedName;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/2
 * @description 提币记录项
 */
public class HistoryItemRes {

    // 数量
    @SerializedName("amount")
    private double amount;

    // 提币手续费
    @SerializedName("fee")
    private String fee;

    // 提币哈希记录(内部转账将不返回此字段)
    @SerializedName("txid")
    private String txid;

    // 币种
    @SerializedName("currency")
    private String currency;

    // 提币地址(如果收币地址是OKEx平台地址，则此处将显示用户账户)
    @SerializedName("from")
    private String from;

    // 收币地址
    @SerializedName("to")
    private String to;

    // 提币申请时间
    @SerializedName("timestamp")
    private String timestamp;

    // 提现状态（-3:撤销中;-2:已撤销;-1:失败;0:等待提现;1:提现中;2:已汇出;3:邮箱确认;4:人工审核中5:等待身份认证）
    @SerializedName("status")
    private int status;

    // 部分币种提币需要此字段，若不需要则不返回此字段
    @SerializedName("payment_id")
    private String payment_id;

    // 部分币种提币需要标签，若不需要则不返回此字段
    @SerializedName("tag")
    private String tag;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
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

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
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

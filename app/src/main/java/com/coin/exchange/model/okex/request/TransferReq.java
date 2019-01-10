package com.coin.exchange.model.okex.request;

import com.google.gson.annotations.SerializedName;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/2
 * @description 资金划转请求
 */
public class TransferReq {

    // 划转数量
    @SerializedName("amount")
    private String amount;

    // 币种
    @SerializedName("currency")
    private String currency;

    // 转出账户(0:子账户 1:币币 3:合约 4:C2C 5:币币杠杆 6:钱包 7:ETT)
    @SerializedName("from")
    private String from;

    // 转入账户(0:子账户 1:币币 3:合约 4:C2C 5:币币杠杆 6:钱包 7:ETT)
    @SerializedName("to")
    private String to;

    // 杠杆币对，如：eos-usdt，仅限已开通杠杆的币对
    @SerializedName("instrument_id")
    private String instrument_id;

    // 子账号登录名，from或to指定为0时，sub_account为必填项
    @SerializedName("sub_account")
    private String sub_account;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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

    public String getInstrument_id() {
        return instrument_id;
    }

    public void setInstrument_id(String instrument_id) {
        this.instrument_id = instrument_id;
    }

    public String getSub_account() {
        return sub_account;
    }

    public void setSub_account(String sub_account) {
        this.sub_account = sub_account;
    }
}

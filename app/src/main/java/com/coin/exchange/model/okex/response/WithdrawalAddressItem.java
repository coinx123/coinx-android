package com.coin.exchange.model.okex.response;

import com.google.gson.annotations.SerializedName;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/2
 * @description
 */
public class WithdrawalAddressItem {

    // 币种，如btc
    @SerializedName("currency")
    private String currency;

    // 充值地址
    @SerializedName("address")
    private String address;

    // 部分币种提币需要标签，若不需要则不返回此字段
    @SerializedName("tag")
    private String tag;

    // 部分币种提币需要此字段，若不需要则不返回此字段
    @SerializedName("payment_id")
    private String payment_id;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}

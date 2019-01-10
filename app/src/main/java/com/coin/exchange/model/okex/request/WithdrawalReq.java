package com.coin.exchange.model.okex.request;

import com.google.gson.annotations.SerializedName;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/2
 * @description 提币请求
 */
public class WithdrawalReq {

    // 数量
    @SerializedName("amount")
    private int amount;

    // 网络手续费≥0.提币到OKCoin国际或OKEx免手续费，请设置为0.提币到数字货币地址所需网络手续费可通过提币手续费接口查询
    @SerializedName("fee")
    private double fee;

    // 交易密码
    @SerializedName("trade_pwd")
    private String trade_pwd;

    // 提币到(2:OKCoin国际 3:OKEx 4:数字货币地址)
    @SerializedName("destination")
    private int destination;

    // 币种
    @SerializedName("currency")
    private String currency;

    // 认证过的数字货币地址、邮箱或手机号
    @SerializedName("to_address")
    private String to_address;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public String getTrade_pwd() {
        return trade_pwd;
    }

    public void setTrade_pwd(String trade_pwd) {
        this.trade_pwd = trade_pwd;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTo_address() {
        return to_address;
    }

    public void setTo_address(String to_address) {
        this.to_address = to_address;
    }
}

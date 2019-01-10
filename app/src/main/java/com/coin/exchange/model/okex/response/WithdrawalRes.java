package com.coin.exchange.model.okex.response;

import com.google.gson.annotations.SerializedName;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/2
 * @description 提现响应
 */
public class WithdrawalRes {

    // 提币数量
    @SerializedName("amount")
    private double amount;

    // 提币申请ID
    @SerializedName("withdrawal_id")
    private int withdrawal_id;

    // 提币币种
    @SerializedName("currency")
    private String currency;

    // 提币申请结果。若是提现申请失败，将给出错误码提示
    @SerializedName("result")
    private boolean result;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getWithdrawal_id() {
        return withdrawal_id;
    }

    public void setWithdrawal_id(int withdrawal_id) {
        this.withdrawal_id = withdrawal_id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}

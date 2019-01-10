package com.coin.exchange.model.okex.response;

import com.google.gson.annotations.SerializedName;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/2
 * @description from或to指定为0时，sub_account为必填项。
 * <p>
 * from或to指定为5时，instrument_id为必填项。
 * <p>
 * OK06ETT只能在ETT组合账户和币币账户中互转，不支持转到其他账户
 */
public class TransferRes {

    // 划转ID
    @SerializedName("transfer_id")
    private String transfer_id;

    // 划转币种
    @SerializedName("currency")
    private String currency;

    // 转出账户
    @SerializedName("from")
    private String from;

    // 划转量
    @SerializedName("amount")
    private String amount;

    // 转入账户
    @SerializedName("to")
    private String to;

    // 划转结果。若是划转失败，将给出错误码提示
    @SerializedName("result")
    private boolean result;

    public String getTransfer_id() {
        return transfer_id;
    }

    public void setTransfer_id(String transfer_id) {
        this.transfer_id = transfer_id;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}

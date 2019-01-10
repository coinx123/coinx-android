package com.coin.exchange.model.bitMex.response;

import com.google.gson.annotations.SerializedName;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/12/20
 * @description
 */
public class CommissionRes {

    @SerializedName("maxFee")
    private double maxFee;
    @SerializedName("makerFee")
    private double makerFee;
    @SerializedName("takerFee")
    private double takerFee;
    @SerializedName("settlementFee")
    private double settlementFee;

    public double getMaxFee() {
        return maxFee;
    }

    public void setMaxFee(double maxFee) {
        this.maxFee = maxFee;
    }

    public double getMakerFee() {
        return makerFee;
    }

    public void setMakerFee(double makerFee) {
        this.makerFee = makerFee;
    }

    public double getTakerFee() {
        return takerFee;
    }

    public void setTakerFee(double takerFee) {
        this.takerFee = takerFee;
    }

    public double getSettlementFee() {
        return settlementFee;
    }

    public void setSettlementFee(double settlementFee) {
        this.settlementFee = settlementFee;
    }
}

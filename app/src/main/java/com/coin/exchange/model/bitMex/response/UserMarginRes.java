package com.coin.exchange.model.bitMex.response;

import com.google.gson.annotations.SerializedName;

/**
 * @author dean
 * @date 创建时间：2018/12/14
 * @description 用户钱包
 */
public class UserMarginRes {

    @SerializedName("commission")
    private double commission;

    @SerializedName("grossLastValue")
    private double grossLastValue;
    @SerializedName("timestamp")
    private String timestamp;
    @SerializedName("withdrawableMargin")
    private double withdrawableMargin;
    @SerializedName("availableMargin")
    private double availableMargin;
    @SerializedName("excessMarginPcnt")
    private double excessMarginPcnt;
    @SerializedName("excessMargin")
    private double excessMargin;
    @SerializedName("marginUsedPcnt")
    private double marginUsedPcnt;
    @SerializedName("marginLeverage")
    private double marginLeverage;
    @SerializedName("marginBalancePcnt")
    private double marginBalancePcnt;
    @SerializedName("marginBalance")
    private double marginBalance;
    @SerializedName("walletBalance")
    private double walletBalance;
    @SerializedName("syntheticMargin")
    private double syntheticMargin;
    @SerializedName("unrealisedProfit")
    private double unrealisedProfit;
    @SerializedName("indicativeTax")
    private double indicativeTax;
    @SerializedName("unrealisedPnl")
    private double unrealisedPnl;
    @SerializedName("realisedPnl")
    private double realisedPnl;
    @SerializedName("varMargin")
    private double varMargin;
    @SerializedName("targetExcessMargin")
    private double targetExcessMargin;
    @SerializedName("sessionMargin")
    private double sessionMargin;
    @SerializedName("madoubleMargin")
    private double madoubleMargin;
    @SerializedName("initMargin")
    private double initMargin;
    @SerializedName("taxableMargin")
    private double taxableMargin;
    @SerializedName("riskValue")
    private double riskValue;
    @SerializedName("grossMarkValue")
    private double grossMarkValue;
    @SerializedName("grossExecCost")
    private double grossExecCost;
    @SerializedName("grossOpenPremium")
    private double grossOpenPremium;
    @SerializedName("grossOpenCost")
    private double grossOpenCost;
    @SerializedName("grossComm")
    private double grossComm;
    @SerializedName("prevUnrealisedPnl")
    private double prevUnrealisedPnl;
    @SerializedName("prevRealisedPnl")
    private double prevRealisedPnl;
    @SerializedName("confirmedDebit")
    private double confirmedDebit;
    @SerializedName("pendingDebit")
    private double pendingDebit;
    @SerializedName("pendingCredit")
    private double pendingCredit;
    @SerializedName("amount")
    private double amount;
    @SerializedName("action")
    private String action;
    @SerializedName("state")
    private String state;
    @SerializedName("prevState")
    private String prevState;
    @SerializedName("riskLimit")
    private double riskLimit;
    @SerializedName("currency")
    private String currency;
    @SerializedName("maintMargin")
    private double maintMargin;

    public double getMaintMargin() {
        return maintMargin;
    }

    public void setMaintMargin(double maintMargin) {
        this.maintMargin = maintMargin;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    public double getGrossLastValue() {
        return grossLastValue;
    }

    public void setGrossLastValue(double grossLastValue) {
        this.grossLastValue = grossLastValue;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public double getWithdrawableMargin() {
        return withdrawableMargin;
    }

    public void setWithdrawableMargin(double withdrawableMargin) {
        this.withdrawableMargin = withdrawableMargin;
    }

    public double getAvailableMargin() {
        return availableMargin;
    }

    public void setAvailableMargin(double availableMargin) {
        this.availableMargin = availableMargin;
    }

    public double getExcessMarginPcnt() {
        return excessMarginPcnt;
    }

    public void setExcessMarginPcnt(double excessMarginPcnt) {
        this.excessMarginPcnt = excessMarginPcnt;
    }

    public double getExcessMargin() {
        return excessMargin;
    }

    public void setExcessMargin(double excessMargin) {
        this.excessMargin = excessMargin;
    }

    public double getMarginUsedPcnt() {
        return marginUsedPcnt;
    }

    public void setMarginUsedPcnt(double marginUsedPcnt) {
        this.marginUsedPcnt = marginUsedPcnt;
    }

    public double getMarginLeverage() {
        return marginLeverage;
    }

    public void setMarginLeverage(double marginLeverage) {
        this.marginLeverage = marginLeverage;
    }

    public double getMarginBalancePcnt() {
        return marginBalancePcnt;
    }

    public void setMarginBalancePcnt(double marginBalancePcnt) {
        this.marginBalancePcnt = marginBalancePcnt;
    }

    public double getMarginBalance() {
        return marginBalance;
    }

    public void setMarginBalance(double marginBalance) {
        this.marginBalance = marginBalance;
    }

    public double getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(double walletBalance) {
        this.walletBalance = walletBalance;
    }

    public double getSyntheticMargin() {
        return syntheticMargin;
    }

    public void setSyntheticMargin(double syntheticMargin) {
        this.syntheticMargin = syntheticMargin;
    }

    public double getUnrealisedProfit() {
        return unrealisedProfit;
    }

    public void setUnrealisedProfit(double unrealisedProfit) {
        this.unrealisedProfit = unrealisedProfit;
    }

    public double getIndicativeTax() {
        return indicativeTax;
    }

    public void setIndicativeTax(double indicativeTax) {
        this.indicativeTax = indicativeTax;
    }

    public double getUnrealisedPnl() {
        return unrealisedPnl;
    }

    public void setUnrealisedPnl(double unrealisedPnl) {
        this.unrealisedPnl = unrealisedPnl;
    }

    public double getRealisedPnl() {
        return realisedPnl;
    }

    public void setRealisedPnl(double realisedPnl) {
        this.realisedPnl = realisedPnl;
    }

    public double getVarMargin() {
        return varMargin;
    }

    public void setVarMargin(double varMargin) {
        this.varMargin = varMargin;
    }

    public double getTargetExcessMargin() {
        return targetExcessMargin;
    }

    public void setTargetExcessMargin(double targetExcessMargin) {
        this.targetExcessMargin = targetExcessMargin;
    }

    public double getSessionMargin() {
        return sessionMargin;
    }

    public void setSessionMargin(double sessionMargin) {
        this.sessionMargin = sessionMargin;
    }

    public double getMadoubleMargin() {
        return madoubleMargin;
    }

    public void setMadoubleMargin(double madoubleMargin) {
        this.madoubleMargin = madoubleMargin;
    }

    public double getInitMargin() {
        return initMargin;
    }

    public void setInitMargin(double initMargin) {
        this.initMargin = initMargin;
    }

    public double getTaxableMargin() {
        return taxableMargin;
    }

    public void setTaxableMargin(double taxableMargin) {
        this.taxableMargin = taxableMargin;
    }

    public double getRiskValue() {
        return riskValue;
    }

    public void setRiskValue(double riskValue) {
        this.riskValue = riskValue;
    }

    public double getGrossMarkValue() {
        return grossMarkValue;
    }

    public void setGrossMarkValue(double grossMarkValue) {
        this.grossMarkValue = grossMarkValue;
    }

    public double getGrossExecCost() {
        return grossExecCost;
    }

    public void setGrossExecCost(double grossExecCost) {
        this.grossExecCost = grossExecCost;
    }

    public double getGrossOpenPremium() {
        return grossOpenPremium;
    }

    public void setGrossOpenPremium(double grossOpenPremium) {
        this.grossOpenPremium = grossOpenPremium;
    }

    public double getGrossOpenCost() {
        return grossOpenCost;
    }

    public void setGrossOpenCost(double grossOpenCost) {
        this.grossOpenCost = grossOpenCost;
    }

    public double getGrossComm() {
        return grossComm;
    }

    public void setGrossComm(double grossComm) {
        this.grossComm = grossComm;
    }

    public double getPrevUnrealisedPnl() {
        return prevUnrealisedPnl;
    }

    public void setPrevUnrealisedPnl(double prevUnrealisedPnl) {
        this.prevUnrealisedPnl = prevUnrealisedPnl;
    }

    public double getPrevRealisedPnl() {
        return prevRealisedPnl;
    }

    public void setPrevRealisedPnl(double prevRealisedPnl) {
        this.prevRealisedPnl = prevRealisedPnl;
    }

    public double getConfirmedDebit() {
        return confirmedDebit;
    }

    public void setConfirmedDebit(double confirmedDebit) {
        this.confirmedDebit = confirmedDebit;
    }

    public double getPendingDebit() {
        return pendingDebit;
    }

    public void setPendingDebit(double pendingDebit) {
        this.pendingDebit = pendingDebit;
    }

    public double getPendingCredit() {
        return pendingCredit;
    }

    public void setPendingCredit(double pendingCredit) {
        this.pendingCredit = pendingCredit;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPrevState() {
        return prevState;
    }

    public void setPrevState(String prevState) {
        this.prevState = prevState;
    }

    public double getRiskLimit() {
        return riskLimit;
    }

    public void setRiskLimit(double riskLimit) {
        this.riskLimit = riskLimit;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getAccount() {
        return account;
    }

    public void setAccount(double account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "UserMarginRes{" +
                "commission=" + commission +
                ", grossLastValue=" + grossLastValue +
                ", timestamp='" + timestamp + '\'' +
                ", withdrawableMargin=" + withdrawableMargin +
                ", availableMargin=" + availableMargin +
                ", excessMarginPcnt=" + excessMarginPcnt +
                ", excessMargin=" + excessMargin +
                ", marginUsedPcnt=" + marginUsedPcnt +
                ", marginLeverage=" + marginLeverage +
                ", marginBalancePcnt=" + marginBalancePcnt +
                ", marginBalance=" + marginBalance +
                ", walletBalance=" + walletBalance +
                ", syntheticMargin=" + syntheticMargin +
                ", unrealisedProfit=" + unrealisedProfit +
                ", indicativeTax=" + indicativeTax +
                ", unrealisedPnl=" + unrealisedPnl +
                ", realisedPnl=" + realisedPnl +
                ", varMargin=" + varMargin +
                ", targetExcessMargin=" + targetExcessMargin +
                ", sessionMargin=" + sessionMargin +
                ", madoubleMargin=" + madoubleMargin +
                ", initMargin=" + initMargin +
                ", taxableMargin=" + taxableMargin +
                ", riskValue=" + riskValue +
                ", grossMarkValue=" + grossMarkValue +
                ", grossExecCost=" + grossExecCost +
                ", grossOpenPremium=" + grossOpenPremium +
                ", grossOpenCost=" + grossOpenCost +
                ", grossComm=" + grossComm +
                ", prevUnrealisedPnl=" + prevUnrealisedPnl +
                ", prevRealisedPnl=" + prevRealisedPnl +
                ", confirmedDebit=" + confirmedDebit +
                ", pendingDebit=" + pendingDebit +
                ", pendingCredit=" + pendingCredit +
                ", amount=" + amount +
                ", action='" + action + '\'' +
                ", state='" + state + '\'' +
                ", prevState='" + prevState + '\'' +
                ", riskLimit=" + riskLimit +
                ", currency='" + currency + '\'' +
                ", account=" + account +
                '}';
    }

    @SerializedName("account")
    private double account;
}

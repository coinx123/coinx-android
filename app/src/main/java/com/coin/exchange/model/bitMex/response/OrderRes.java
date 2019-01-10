package com.coin.exchange.model.bitMex.response;

import com.google.gson.annotations.SerializedName;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/12/18
 * @description
 */
public class OrderRes {

    @SerializedName("orderID")
    private String orderID;
    @SerializedName("clOrdID")
    private String clOrdID;
    @SerializedName("clOrdLinkID")
    private String clOrdLinkID;
    @SerializedName("account")
    private double account;
    @SerializedName("symbol")
    private String symbol;
    @SerializedName("side")
    private String side;
    @SerializedName("simpleOrderQty")
    private double simpleOrderQty;
    @SerializedName("orderQty")
    private double orderQty;
    @SerializedName("price")
    private double price;
    @SerializedName("displayQty")
    private double displayQty;
    @SerializedName("stopPx")
    private double stopPx;
    @SerializedName("pegOffsetValue")
    private double pegOffsetValue;
    @SerializedName("pegPriceType")
    private String pegPriceType;
    @SerializedName("currency")
    private String currency;
    @SerializedName("settlCurrency")
    private String settlCurrency;
    @SerializedName("ordType")
    private String ordType;
    @SerializedName("timeInForce")
    private String timeInForce;
    @SerializedName("execInst")
    private String execInst;
    @SerializedName("contingencyType")
    private String contingencyType;
    @SerializedName("exDestination")
    private String exDestination;
    @SerializedName("ordStatus")
    private String ordStatus;
    @SerializedName("triggered")
    private String triggered;
    @SerializedName("workingIndicator")
    private boolean workingIndicator;
    @SerializedName("ordRejReason")
    private String ordRejReason;
    @SerializedName("simpleLeavesQty")
    private double simpleLeavesQty;
    @SerializedName("leavesQty")
    private double leavesQty;
    @SerializedName("simpleCumQty")
    private double simpleCumQty;
    @SerializedName("cumQty")
    private double cumQty;
    @SerializedName("avgPx")
    private double avgPx;
    @SerializedName("multiLegReportingType")
    private String multiLegReportingType;
    @SerializedName("text")
    private String text;
    @SerializedName("transactTime")
    private String transactTime;
    @SerializedName("timestamp")
    private String timestamp;

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getClOrdID() {
        return clOrdID;
    }

    public void setClOrdID(String clOrdID) {
        this.clOrdID = clOrdID;
    }

    public String getClOrdLinkID() {
        return clOrdLinkID;
    }

    public void setClOrdLinkID(String clOrdLinkID) {
        this.clOrdLinkID = clOrdLinkID;
    }

    public double getAccount() {
        return account;
    }

    public void setAccount(double account) {
        this.account = account;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public double getSimpleOrderQty() {
        return simpleOrderQty;
    }

    public void setSimpleOrderQty(double simpleOrderQty) {
        this.simpleOrderQty = simpleOrderQty;
    }

    public double getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(double orderQty) {
        this.orderQty = orderQty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDisplayQty() {
        return displayQty;
    }

    public void setDisplayQty(double displayQty) {
        this.displayQty = displayQty;
    }

    public double getStopPx() {
        return stopPx;
    }

    public void setStopPx(double stopPx) {
        this.stopPx = stopPx;
    }

    public double getPegOffsetValue() {
        return pegOffsetValue;
    }

    public void setPegOffsetValue(double pegOffsetValue) {
        this.pegOffsetValue = pegOffsetValue;
    }

    public String getPegPriceType() {
        return pegPriceType;
    }

    public void setPegPriceType(String pegPriceType) {
        this.pegPriceType = pegPriceType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSettlCurrency() {
        return settlCurrency;
    }

    public void setSettlCurrency(String settlCurrency) {
        this.settlCurrency = settlCurrency;
    }

    public String getOrdType() {
        return ordType;
    }

    public void setOrdType(String ordType) {
        this.ordType = ordType;
    }

    public String getTimeInForce() {
        return timeInForce;
    }

    public void setTimeInForce(String timeInForce) {
        this.timeInForce = timeInForce;
    }

    public String getExecInst() {
        return execInst;
    }

    public void setExecInst(String execInst) {
        this.execInst = execInst;
    }

    public String getContingencyType() {
        return contingencyType;
    }

    public void setContingencyType(String contingencyType) {
        this.contingencyType = contingencyType;
    }

    public String getExDestination() {
        return exDestination;
    }

    public void setExDestination(String exDestination) {
        this.exDestination = exDestination;
    }

    public String getOrdStatus() {
        return ordStatus;
    }

    public void setOrdStatus(String ordStatus) {
        this.ordStatus = ordStatus;
    }

    public String getTriggered() {
        return triggered;
    }

    public void setTriggered(String triggered) {
        this.triggered = triggered;
    }

    public boolean isWorkingIndicator() {
        return workingIndicator;
    }

    public void setWorkingIndicator(boolean workingIndicator) {
        this.workingIndicator = workingIndicator;
    }

    public String getOrdRejReason() {
        return ordRejReason;
    }

    public void setOrdRejReason(String ordRejReason) {
        this.ordRejReason = ordRejReason;
    }

    public double getSimpleLeavesQty() {
        return simpleLeavesQty;
    }

    public void setSimpleLeavesQty(double simpleLeavesQty) {
        this.simpleLeavesQty = simpleLeavesQty;
    }

    public double getLeavesQty() {
        return leavesQty;
    }

    public void setLeavesQty(double leavesQty) {
        this.leavesQty = leavesQty;
    }

    public double getSimpleCumQty() {
        return simpleCumQty;
    }

    public void setSimpleCumQty(double simpleCumQty) {
        this.simpleCumQty = simpleCumQty;
    }

    public double getCumQty() {
        return cumQty;
    }

    public void setCumQty(double cumQty) {
        this.cumQty = cumQty;
    }

    public double getAvgPx() {
        return avgPx;
    }

    public void setAvgPx(double avgPx) {
        this.avgPx = avgPx;
    }

    public String getMultiLegReportingType() {
        return multiLegReportingType;
    }

    public void setMultiLegReportingType(String multiLegReportingType) {
        this.multiLegReportingType = multiLegReportingType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTransactTime() {
        return transactTime;
    }

    public void setTransactTime(String transactTime) {
        this.transactTime = transactTime;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

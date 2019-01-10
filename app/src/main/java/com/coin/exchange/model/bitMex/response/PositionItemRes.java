package com.coin.exchange.model.bitMex.response;

import com.google.gson.annotations.SerializedName;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/12/17
 * @description
 */
public class PositionItemRes {

    @SerializedName("account")
    private double account;
    @SerializedName("symbol")
    private String symbol;
    @SerializedName("currency")
    private String currency;
    @SerializedName("underlying")
    private String underlying;
    @SerializedName("quoteCurrency")
    private String quoteCurrency;
    @SerializedName("commission")
    private double commission;
    @SerializedName("initMarginReq")
    private double initMarginReq;
    @SerializedName("madoubleMarginReq")
    private double maintMarginReq;
    @SerializedName("riskLimit")
    private double riskLimit;
    @SerializedName("leverage")
    private int leverage;
    @SerializedName("crossMargin")
    private boolean crossMargin;
    @SerializedName("deleveragePercentile")
    private double deleveragePercentile;
    @SerializedName("rebalancedPnl")
    private double rebalancedPnl;
    @SerializedName("prevRealisedPnl")
    private double prevRealisedPnl;
    @SerializedName("prevUnrealisedPnl")
    private double prevUnrealisedPnl;
    @SerializedName("prevClosePrice")
    private double prevClosePrice;
    @SerializedName("openingTimestamp")
    private String openingTimestamp;
    @SerializedName("openingQty")
    private double openingQty;
    @SerializedName("openingCost")
    private double openingCost;
    @SerializedName("openingComm")
    private double openingComm;
    @SerializedName("openOrderBuyQty")
    private double openOrderBuyQty;
    @SerializedName("openOrderBuyCost")
    private double openOrderBuyCost;
    @SerializedName("openOrderBuyPremium")
    private double openOrderBuyPremium;
    @SerializedName("openOrderSellQty")
    private double openOrderSellQty;
    @SerializedName("openOrderSellCost")
    private double openOrderSellCost;
    @SerializedName("openOrderSellPremium")
    private double openOrderSellPremium;
    @SerializedName("execBuyQty")
    private double execBuyQty;
    @SerializedName("execBuyCost")
    private double execBuyCost;
    @SerializedName("execSellQty")
    private double execSellQty;
    @SerializedName("execSellCost")
    private double execSellCost;
    @SerializedName("execQty")
    private double execQty;
    @SerializedName("execCost")
    private double execCost;
    @SerializedName("execComm")
    private double execComm;
    @SerializedName("currentTimestamp")
    private String currentTimestamp;
    @SerializedName("currentQty")
    private double currentQty;
    @SerializedName("currentCost")
    private double currentCost;
    @SerializedName("currentComm")
    private double currentComm;
    @SerializedName("realisedCost")
    private double realisedCost;
    @SerializedName("unrealisedCost")
    private double unrealisedCost;
    @SerializedName("grossOpenCost")
    private double grossOpenCost;
    @SerializedName("grossOpenPremium")
    private double grossOpenPremium;
    @SerializedName("grossExecCost")
    private double grossExecCost;
    @SerializedName("isOpen")
    private boolean isOpen;
    @SerializedName("markPrice")
    private double markPrice;
    @SerializedName("markValue")
    private double markValue;
    @SerializedName("riskValue")
    private double riskValue;
    @SerializedName("homeNotional")
    private double homeNotional;
    @SerializedName("foreignNotional")
    private double foreignNotional;
    @SerializedName("posState")
    private String posState;
    @SerializedName("posCost")
    private double posCost;
    @SerializedName("posCost2")
    private double posCost2;
    @SerializedName("posCross")
    private double posCross;
    @SerializedName("posInit")
    private double posInit;
    @SerializedName("posComm")
    private double posComm;
    @SerializedName("posLoss")
    private double posLoss;
    @SerializedName("posMargin")
    private double posMargin;
    @SerializedName("posMaint")
    private double posMaint;
    @SerializedName("posAllowance")
    private double posAllowance;
    @SerializedName("taxableMargin")
    private double taxableMargin;
    @SerializedName("initMargin")
    private double initMargin;
    @SerializedName("maintMargin")
    private double maintMargin;
    @SerializedName("sessionMargin")
    private double sessionMargin;
    @SerializedName("targetExcessMargin")
    private double targetExcessMargin;
    @SerializedName("varMargin")
    private double varMargin;
    @SerializedName("realisedGrossPnl")
    private double realisedGrossPnl;
    @SerializedName("realisedTax")
    private double realisedTax;
    @SerializedName("realisedPnl")
    private double realisedPnl;
    @SerializedName("unrealisedGrossPnl")
    private double unrealisedGrossPnl;
    @SerializedName("longBankrupt")
    private double longBankrupt;
    @SerializedName("shortBankrupt")
    private double shortBankrupt;
    @SerializedName("taxBase")
    private double taxBase;
    @SerializedName("indicativeTaxRate")
    private double indicativeTaxRate;
    @SerializedName("indicativeTax")
    private double indicativeTax;
    @SerializedName("unrealisedTax")
    private double unrealisedTax;
    @SerializedName("unrealisedPnl")
    private double unrealisedPnl;
    @SerializedName("unrealisedPnlPcnt")
    private double unrealisedPnlPcnt;
    @SerializedName("unrealisedRoePcnt")
    private double unrealisedRoePcnt;
    @SerializedName("simpleQty")
    private double simpleQty;
    @SerializedName("simpleCost")
    private double simpleCost;
    @SerializedName("simpleValue")
    private double simpleValue;
    @SerializedName("simplePnl")
    private double simplePnl;
    @SerializedName("simplePnlPcnt")
    private double simplePnlPcnt;
    @SerializedName("avgCostPrice")
    private String avgCostPrice;
    @SerializedName("avgEntryPrice")
    private double avgEntryPrice;
    @SerializedName("breakEvenPrice")
    private double breakEvenPrice;
    @SerializedName("marginCallPrice")
    private double marginCallPrice;
    @SerializedName("liquidationPrice")
    private String liquidationPrice;
    @SerializedName("bankruptPrice")
    private double bankruptPrice;
    @SerializedName("timestamp")
    private String timestamp;
    @SerializedName("lastPrice")
    private String lastPrice;
    @SerializedName("lastValue")
    private double lastValue;

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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getUnderlying() {
        return underlying;
    }

    public void setUnderlying(String underlying) {
        this.underlying = underlying;
    }

    public String getQuoteCurrency() {
        return quoteCurrency;
    }

    public void setQuoteCurrency(String quoteCurrency) {
        this.quoteCurrency = quoteCurrency;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    public double getInitMarginReq() {
        return initMarginReq;
    }

    public void setInitMarginReq(double initMarginReq) {
        this.initMarginReq = initMarginReq;
    }

    public double getMaintMarginReq() {
        return maintMarginReq;
    }

    public void setMaintMarginReq(double maintMarginReq) {
        this.maintMarginReq = maintMarginReq;
    }

    public double getRiskLimit() {
        return riskLimit;
    }

    public void setRiskLimit(double riskLimit) {
        this.riskLimit = riskLimit;
    }

    public int getLeverage() {
        return leverage;
    }

    public void setLeverage(int leverage) {
        this.leverage = leverage;
    }

    public boolean isCrossMargin() {
        return crossMargin;
    }

    public void setCrossMargin(boolean crossMargin) {
        this.crossMargin = crossMargin;
    }

    public double getDeleveragePercentile() {
        return deleveragePercentile;
    }

    public void setDeleveragePercentile(double deleveragePercentile) {
        this.deleveragePercentile = deleveragePercentile;
    }

    public double getRebalancedPnl() {
        return rebalancedPnl;
    }

    public void setRebalancedPnl(double rebalancedPnl) {
        this.rebalancedPnl = rebalancedPnl;
    }

    public double getPrevRealisedPnl() {
        return prevRealisedPnl;
    }

    public void setPrevRealisedPnl(double prevRealisedPnl) {
        this.prevRealisedPnl = prevRealisedPnl;
    }

    public double getPrevUnrealisedPnl() {
        return prevUnrealisedPnl;
    }

    public void setPrevUnrealisedPnl(double prevUnrealisedPnl) {
        this.prevUnrealisedPnl = prevUnrealisedPnl;
    }

    public double getPrevClosePrice() {
        return prevClosePrice;
    }

    public void setPrevClosePrice(double prevClosePrice) {
        this.prevClosePrice = prevClosePrice;
    }

    public String getOpeningTimestamp() {
        return openingTimestamp;
    }

    public void setOpeningTimestamp(String openingTimestamp) {
        this.openingTimestamp = openingTimestamp;
    }

    public double getOpeningQty() {
        return openingQty;
    }

    public void setOpeningQty(double openingQty) {
        this.openingQty = openingQty;
    }

    public double getOpeningCost() {
        return openingCost;
    }

    public void setOpeningCost(double openingCost) {
        this.openingCost = openingCost;
    }

    public double getOpeningComm() {
        return openingComm;
    }

    public void setOpeningComm(double openingComm) {
        this.openingComm = openingComm;
    }

    public double getOpenOrderBuyQty() {
        return openOrderBuyQty;
    }

    public void setOpenOrderBuyQty(double openOrderBuyQty) {
        this.openOrderBuyQty = openOrderBuyQty;
    }

    public double getOpenOrderBuyCost() {
        return openOrderBuyCost;
    }

    public void setOpenOrderBuyCost(double openOrderBuyCost) {
        this.openOrderBuyCost = openOrderBuyCost;
    }

    public double getOpenOrderBuyPremium() {
        return openOrderBuyPremium;
    }

    public void setOpenOrderBuyPremium(double openOrderBuyPremium) {
        this.openOrderBuyPremium = openOrderBuyPremium;
    }

    public double getOpenOrderSellQty() {
        return openOrderSellQty;
    }

    public void setOpenOrderSellQty(double openOrderSellQty) {
        this.openOrderSellQty = openOrderSellQty;
    }

    public double getOpenOrderSellCost() {
        return openOrderSellCost;
    }

    public void setOpenOrderSellCost(double openOrderSellCost) {
        this.openOrderSellCost = openOrderSellCost;
    }

    public double getOpenOrderSellPremium() {
        return openOrderSellPremium;
    }

    public void setOpenOrderSellPremium(double openOrderSellPremium) {
        this.openOrderSellPremium = openOrderSellPremium;
    }

    public double getExecBuyQty() {
        return execBuyQty;
    }

    public void setExecBuyQty(double execBuyQty) {
        this.execBuyQty = execBuyQty;
    }

    public double getExecBuyCost() {
        return execBuyCost;
    }

    public void setExecBuyCost(double execBuyCost) {
        this.execBuyCost = execBuyCost;
    }

    public double getExecSellQty() {
        return execSellQty;
    }

    public void setExecSellQty(double execSellQty) {
        this.execSellQty = execSellQty;
    }

    public double getExecSellCost() {
        return execSellCost;
    }

    public void setExecSellCost(double execSellCost) {
        this.execSellCost = execSellCost;
    }

    public double getExecQty() {
        return execQty;
    }

    public void setExecQty(double execQty) {
        this.execQty = execQty;
    }

    public double getExecCost() {
        return execCost;
    }

    public void setExecCost(double execCost) {
        this.execCost = execCost;
    }

    public double getExecComm() {
        return execComm;
    }

    public void setExecComm(double execComm) {
        this.execComm = execComm;
    }

    public String getCurrentTimestamp() {
        return currentTimestamp;
    }

    public void setCurrentTimestamp(String currentTimestamp) {
        this.currentTimestamp = currentTimestamp;
    }

    public double getCurrentQty() {
        return currentQty;
    }

    public void setCurrentQty(double currentQty) {
        this.currentQty = currentQty;
    }

    public double getCurrentCost() {
        return currentCost;
    }

    public void setCurrentCost(double currentCost) {
        this.currentCost = currentCost;
    }

    public double getCurrentComm() {
        return currentComm;
    }

    public void setCurrentComm(double currentComm) {
        this.currentComm = currentComm;
    }

    public double getRealisedCost() {
        return realisedCost;
    }

    public void setRealisedCost(double realisedCost) {
        this.realisedCost = realisedCost;
    }

    public double getUnrealisedCost() {
        return unrealisedCost;
    }

    public void setUnrealisedCost(double unrealisedCost) {
        this.unrealisedCost = unrealisedCost;
    }

    public double getGrossOpenCost() {
        return grossOpenCost;
    }

    public void setGrossOpenCost(double grossOpenCost) {
        this.grossOpenCost = grossOpenCost;
    }

    public double getGrossOpenPremium() {
        return grossOpenPremium;
    }

    public void setGrossOpenPremium(double grossOpenPremium) {
        this.grossOpenPremium = grossOpenPremium;
    }

    public double getGrossExecCost() {
        return grossExecCost;
    }

    public void setGrossExecCost(double grossExecCost) {
        this.grossExecCost = grossExecCost;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public double getMarkPrice() {
        return markPrice;
    }

    public void setMarkPrice(double markPrice) {
        this.markPrice = markPrice;
    }

    public double getMarkValue() {
        return markValue;
    }

    public void setMarkValue(double markValue) {
        this.markValue = markValue;
    }

    public double getRiskValue() {
        return riskValue;
    }

    public void setRiskValue(double riskValue) {
        this.riskValue = riskValue;
    }

    public double getHomeNotional() {
        return homeNotional;
    }

    public void setHomeNotional(double homeNotional) {
        this.homeNotional = homeNotional;
    }

    public double getForeignNotional() {
        return foreignNotional;
    }

    public void setForeignNotional(double foreignNotional) {
        this.foreignNotional = foreignNotional;
    }

    public String getPosState() {
        return posState;
    }

    public void setPosState(String posState) {
        this.posState = posState;
    }

    public double getPosCost() {
        return posCost;
    }

    public void setPosCost(double posCost) {
        this.posCost = posCost;
    }

    public double getPosCost2() {
        return posCost2;
    }

    public void setPosCost2(double posCost2) {
        this.posCost2 = posCost2;
    }

    public double getPosCross() {
        return posCross;
    }

    public void setPosCross(double posCross) {
        this.posCross = posCross;
    }

    public double getPosInit() {
        return posInit;
    }

    public void setPosInit(double posInit) {
        this.posInit = posInit;
    }

    public double getPosComm() {
        return posComm;
    }

    public void setPosComm(double posComm) {
        this.posComm = posComm;
    }

    public double getPosLoss() {
        return posLoss;
    }

    public void setPosLoss(double posLoss) {
        this.posLoss = posLoss;
    }

    public double getPosMargin() {
        return posMargin;
    }

    public void setPosMargin(double posMargin) {
        this.posMargin = posMargin;
    }

    public double getPosMaint() {
        return posMaint;
    }

    public void setPosMaint(double posMaint) {
        this.posMaint = posMaint;
    }

    public double getPosAllowance() {
        return posAllowance;
    }

    public void setPosAllowance(double posAllowance) {
        this.posAllowance = posAllowance;
    }

    public double getTaxableMargin() {
        return taxableMargin;
    }

    public void setTaxableMargin(double taxableMargin) {
        this.taxableMargin = taxableMargin;
    }

    public double getInitMargin() {
        return initMargin;
    }

    public void setInitMargin(double initMargin) {
        this.initMargin = initMargin;
    }

    public double getMaintMargin() {
        return maintMargin;
    }

    public void setMaintMargin(double maintMargin) {
        this.maintMargin = maintMargin;
    }

    public double getSessionMargin() {
        return sessionMargin;
    }

    public void setSessionMargin(double sessionMargin) {
        this.sessionMargin = sessionMargin;
    }

    public double getTargetExcessMargin() {
        return targetExcessMargin;
    }

    public void setTargetExcessMargin(double targetExcessMargin) {
        this.targetExcessMargin = targetExcessMargin;
    }

    public double getVarMargin() {
        return varMargin;
    }

    public void setVarMargin(double varMargin) {
        this.varMargin = varMargin;
    }

    public double getRealisedGrossPnl() {
        return realisedGrossPnl;
    }

    public void setRealisedGrossPnl(double realisedGrossPnl) {
        this.realisedGrossPnl = realisedGrossPnl;
    }

    public double getRealisedTax() {
        return realisedTax;
    }

    public void setRealisedTax(double realisedTax) {
        this.realisedTax = realisedTax;
    }

    public double getRealisedPnl() {
        return realisedPnl;
    }

    public void setRealisedPnl(double realisedPnl) {
        this.realisedPnl = realisedPnl;
    }

    public double getUnrealisedGrossPnl() {
        return unrealisedGrossPnl;
    }

    public void setUnrealisedGrossPnl(double unrealisedGrossPnl) {
        this.unrealisedGrossPnl = unrealisedGrossPnl;
    }

    public double getLongBankrupt() {
        return longBankrupt;
    }

    public void setLongBankrupt(double longBankrupt) {
        this.longBankrupt = longBankrupt;
    }

    public double getShortBankrupt() {
        return shortBankrupt;
    }

    public void setShortBankrupt(double shortBankrupt) {
        this.shortBankrupt = shortBankrupt;
    }

    public double getTaxBase() {
        return taxBase;
    }

    public void setTaxBase(double taxBase) {
        this.taxBase = taxBase;
    }

    public double getIndicativeTaxRate() {
        return indicativeTaxRate;
    }

    public void setIndicativeTaxRate(double indicativeTaxRate) {
        this.indicativeTaxRate = indicativeTaxRate;
    }

    public double getIndicativeTax() {
        return indicativeTax;
    }

    public void setIndicativeTax(double indicativeTax) {
        this.indicativeTax = indicativeTax;
    }

    public double getUnrealisedTax() {
        return unrealisedTax;
    }

    public void setUnrealisedTax(double unrealisedTax) {
        this.unrealisedTax = unrealisedTax;
    }

    public double getUnrealisedPnl() {
        return unrealisedPnl;
    }

    public void setUnrealisedPnl(double unrealisedPnl) {
        this.unrealisedPnl = unrealisedPnl;
    }

    public double getUnrealisedPnlPcnt() {
        return unrealisedPnlPcnt;
    }

    public void setUnrealisedPnlPcnt(double unrealisedPnlPcnt) {
        this.unrealisedPnlPcnt = unrealisedPnlPcnt;
    }

    public double getUnrealisedRoePcnt() {
        return unrealisedRoePcnt;
    }

    public void setUnrealisedRoePcnt(double unrealisedRoePcnt) {
        this.unrealisedRoePcnt = unrealisedRoePcnt;
    }

    public double getSimpleQty() {
        return simpleQty;
    }

    public void setSimpleQty(double simpleQty) {
        this.simpleQty = simpleQty;
    }

    public double getSimpleCost() {
        return simpleCost;
    }

    public void setSimpleCost(double simpleCost) {
        this.simpleCost = simpleCost;
    }

    public double getSimpleValue() {
        return simpleValue;
    }

    public void setSimpleValue(double simpleValue) {
        this.simpleValue = simpleValue;
    }

    public double getSimplePnl() {
        return simplePnl;
    }

    public void setSimplePnl(double simplePnl) {
        this.simplePnl = simplePnl;
    }

    public double getSimplePnlPcnt() {
        return simplePnlPcnt;
    }

    public void setSimplePnlPcnt(double simplePnlPcnt) {
        this.simplePnlPcnt = simplePnlPcnt;
    }

    public String getAvgCostPrice() {
        return avgCostPrice;
    }

    public void setAvgCostPrice(String avgCostPrice) {
        this.avgCostPrice = avgCostPrice;
    }

    public double getAvgEntryPrice() {
        return avgEntryPrice;
    }

    public void setAvgEntryPrice(double avgEntryPrice) {
        this.avgEntryPrice = avgEntryPrice;
    }

    public double getBreakEvenPrice() {
        return breakEvenPrice;
    }

    public void setBreakEvenPrice(double breakEvenPrice) {
        this.breakEvenPrice = breakEvenPrice;
    }

    public double getMarginCallPrice() {
        return marginCallPrice;
    }

    public void setMarginCallPrice(double marginCallPrice) {
        this.marginCallPrice = marginCallPrice;
    }

    public String getLiquidationPrice() {
        return liquidationPrice;
    }

    public void setLiquidationPrice(String liquidationPrice) {
        this.liquidationPrice = liquidationPrice;
    }

    public double getBankruptPrice() {
        return bankruptPrice;
    }

    public void setBankruptPrice(double bankruptPrice) {
        this.bankruptPrice = bankruptPrice;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(String lastPrice) {
        this.lastPrice = lastPrice;
    }

    public double getLastValue() {
        return lastValue;
    }

    public void setLastValue(double lastValue) {
        this.lastValue = lastValue;
    }
}

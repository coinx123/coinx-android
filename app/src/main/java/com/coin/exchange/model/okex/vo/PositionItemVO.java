package com.coin.exchange.model.okex.vo;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/21
 * @description
 */
public class PositionItemVO {

    private String titleName;
    private String typeName;
    private String nameDes;
    private String type;
    private String buyPrice;
    private String incomeRate;
    private String income;
    private String position;
    private String sell;
    private String security;
    private String sellForce;

    private boolean isWantIncrease;
    private boolean isIncrease;
    private String leverage;

    private String insId;
    private String curPrice;

    /**
     * 平台
     * BitMex: {@link com.coin.exchange.utils.AppUtils#BITMEX}
     * OkEx:   {@link com.coin.exchange.utils.AppUtils#OKEX}
     */
    private String platform;

    // 单位
    private String unit;

    // 可增加保证金
    private String canIncreaseSecurity;
    // 可减少保证金
    private String canDecreaseSecurity;

    public PositionItemVO() {
    }

    public PositionItemVO(String platform) {
        this.platform = platform;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getNameDes() {
        return nameDes;
    }

    public void setNameDes(String nameDes) {
        this.nameDes = nameDes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(String buyPrice) {
        this.buyPrice = buyPrice;
    }

    public String getIncomeRate() {
        return incomeRate;
    }

    public void setIncomeRate(String incomeRate) {
        this.incomeRate = incomeRate;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSell() {
        return sell;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public String getSellForce() {
        return sellForce;
    }

    public void setSellForce(String sellForce) {
        this.sellForce = sellForce;
    }

    public boolean isIncrease() {
        return isIncrease;
    }

    public void setIncrease(boolean increase) {
        isIncrease = increase;
    }

    public String getLeverage() {
        return leverage;
    }

    public void setLeverage(String leverage) {
        this.leverage = leverage;
    }

    public boolean isWantIncrease() {
        return isWantIncrease;
    }

    public void setWantIncrease(boolean wantIncrease) {
        isWantIncrease = wantIncrease;
    }

    public String getInsId() {
        return insId;
    }

    public void setInsId(String insId) {
        this.insId = insId;
    }

    public String getCurPrice() {
        return curPrice;
    }

    public void setCurPrice(String curPrice) {
        this.curPrice = curPrice;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getCanIncreaseSecurity() {
        return canIncreaseSecurity;
    }

    public void setCanIncreaseSecurity(String canIncreaseSecurity) {
        this.canIncreaseSecurity = canIncreaseSecurity;
    }

    public String getCanDecreaseSecurity() {
        return canDecreaseSecurity;
    }

    public void setCanDecreaseSecurity(String canDecreaseSecurity) {
        this.canDecreaseSecurity = canDecreaseSecurity;
    }
}

package com.coin.exchange.model.okex.vo;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/27
 * @description
 */
public class DelegationItemVO {

    // 币种id
    private String insId;
    // 订单id
    private String orderId;

    // 名称
    private String name;
    // icon类型
    private String nameDes;
    // 交易类型 "卖出开空 10X"
    private String dealType;
    // 时间 "11-5 09:46"
    private String time;
    // 保证金
    private double security;
    // 手续费
    private String fee;
    // 成交价
    private String donePrice;
    // 成交量
    private String doneValue;
    // 委托价
    private String delegationPrice;
    // 委托量
    private String delegationValue;
    // 状态
    private String status;

    // 是否卖出
    private boolean isSell;

    // 平台
    private String platform;

    public DelegationItemVO(String platform) {
        this.platform = platform;
    }

    public String getInsId() {
        return insId;
    }

    public void setInsId(String insId) {
        this.insId = insId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameDes() {
        return nameDes;
    }

    public void setNameDes(String nameDes) {
        this.nameDes = nameDes;
    }

    public String getDealType() {
        return dealType;
    }

    public void setDealType(String dealType) {
        this.dealType = dealType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getSecurity() {
        return security;
    }

    public void setSecurity(double security) {
        this.security = security;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getDonePrice() {
        return donePrice;
    }

    public void setDonePrice(String donePrice) {
        this.donePrice = donePrice;
    }

    public String getDoneValue() {
        return doneValue;
    }

    public void setDoneValue(String doneValue) {
        this.doneValue = doneValue;
    }

    public String getDelegationPrice() {
        return delegationPrice;
    }

    public void setDelegationPrice(String delegationPrice) {
        this.delegationPrice = delegationPrice;
    }

    public String getDelegationValue() {
        return delegationValue;
    }

    public void setDelegationValue(String delegationValue) {
        this.delegationValue = delegationValue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isSell() {
        return isSell;
    }

    public void setSell(boolean sell) {
        isSell = sell;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPlatform() {
        return platform;
    }

}

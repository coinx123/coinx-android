package com.coin.exchange.model.okex.vo;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/13
 * @description 热门货币
 */
public class HotCoinItemVO {

    // 币种名称
    private String name;
    // 市值
    private double value;
    // 涨幅
    private String range;
    // 涨跌
    private boolean isIncrease;
    // id
    private String id;
    // 描述名称
    private String desName;

    public HotCoinItemVO() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public void setIncrease(boolean increase) {
        isIncrease = increase;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    public String getRange() {
        return range;
    }

    public boolean isIncrease() {
        return isIncrease;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesName() {
        return desName;
    }

    public void setDesName(String desName) {
        this.desName = desName;
    }
}

package com.coin.exchange.model.okex.vo;

import android.support.annotation.NonNull;

import com.coin.libbase.utils.DoubleUtils;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/14
 * @description
 */
public class RankItemVO implements Comparable<RankItemVO> {

    private int num;
    private String name;
    private String nameDes;
    // 24小时交易量
    private String volume;
    private String value;
    private double range;
    private boolean isIncrease;

    private String insId;

    // 指数
    private String index;
// 当前的市价
//    private double curLast;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
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

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public boolean isIncrease() {
        return isIncrease;
    }

    public void setIncrease(boolean increase) {
        isIncrease = increase;
    }

//    public double getCurLast() {
//        return curLast;
//    }
//
//    public void setCurLast(double curLast) {
//        this.curLast = curLast;
//    }

    public String getInsId() {
        return insId;
    }

    public void setInsId(String insId) {
        this.insId = insId;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getRangeString() {
        return isIncrease ?
                "+" + DoubleUtils.formatTwoDecimalString(range) + "%" :
                DoubleUtils.formatTwoDecimalString(range) + "%";
    }

    @Override
    public int compareTo(@NonNull RankItemVO o) {
        if (range > o.getRange()) {
            return -1;
        }
        return 1;
    }
}

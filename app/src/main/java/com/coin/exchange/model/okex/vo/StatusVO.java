package com.coin.exchange.model.okex.vo;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/27
 * @description
 */
public class StatusVO {

    private String id;
    private String name;

    private boolean isSelect;

    public StatusVO(String id, String name, boolean isSelect) {
        this.id = id;
        this.name = name;
        this.isSelect = isSelect;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}

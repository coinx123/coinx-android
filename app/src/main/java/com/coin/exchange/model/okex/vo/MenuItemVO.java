package com.coin.exchange.model.okex.vo;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/12
 * @description 菜单模型
 */
public class MenuItemVO {

    // 按钮id，为了区分
    private int id;
    // 显示名字
    private String name;
    // 图标资源
    private int resId;

    private boolean isSelect;

    private String status;

    public MenuItemVO(int id, String name, int resId) {
        this.id = id;
        this.name = name;
        this.resId = resId;
    }

    public MenuItemVO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public MenuItemVO(int id, String name, int resId, boolean isSelect) {
        this.id = id;
        this.name = name;
        this.resId = resId;
        this.isSelect = isSelect;
    }

    public MenuItemVO(int id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getResId() {
        return resId;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getStatus() {
        return status;
    }
}

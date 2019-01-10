package com.coin.exchange.model.bitMex.request;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/12/18
 * @description
 */
public class OrderReq {

    // 币种id
    private String symbol;

    // 数量
    private String orderQty;

    // 价格
    private String price;

    // Market、Limit
    private String ordType;

    // Buy, Sell 买卖类型
    private String side;

    // 开仓还是平仓
    private boolean isOpen;

    public OrderReq(String symbol, String orderQty, String price, String ordType, String side) {
        this.symbol = symbol;
        this.orderQty = orderQty;
        this.price = price;
        this.ordType = ordType;
        this.side = side;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(String orderQty) {
        this.orderQty = orderQty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOrdType() {
        return ordType;
    }

    public void setOrdType(String ordType) {
        this.ordType = ordType;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

}

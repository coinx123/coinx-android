package com.coin.exchange.model.bitMex.request;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/12/20
 * @description
 */
public class CancelOrderReq {

    private String orderID;

    public CancelOrderReq(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }
}

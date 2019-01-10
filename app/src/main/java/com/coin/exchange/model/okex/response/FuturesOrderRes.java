package com.coin.exchange.model.okex.response;

import com.google.gson.annotations.SerializedName;

/**
 * @author dean
 * @date 创建时间：2018/11/6
 * @description
 */
public class FuturesOrderRes {
    //调用接口返回结果
    @SerializedName("result")
    private boolean result;
    //	订单ID，下单失败时，此字段值为-1
    @SerializedName("order_id")
    private String order_id;
    //错误信息，下单成功时为空，下单失败时会显示错误信息
    @SerializedName("error_messsage")
    private String error_messsage;
    //	错误码，下单成功时为0，下单失败时会显示相应错误码
    @SerializedName("error_code")
    private int error_code;
    //	由您设置的订单ID来识别您的订单
    @SerializedName("client_oid")
    private String client_oid;

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getError_messsage() {
        return error_messsage;
    }

    public void setError_messsage(String error_messsage) {
        this.error_messsage = error_messsage;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getClient_oid() {
        return client_oid;
    }

    public void setClient_oid(String client_oid) {
        this.client_oid = client_oid;
    }

    @Override
    public String toString() {
        return "FuturesOrderRes{" +
                "result=" + result +
                ", order_id='" + order_id + '\'' +
                ", error_messsage='" + error_messsage + '\'' +
                ", error_code=" + error_code +
                ", client_oid='" + client_oid + '\'' +
                '}';
    }
}

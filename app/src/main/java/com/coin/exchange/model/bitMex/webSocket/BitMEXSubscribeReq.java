package com.coin.exchange.model.bitMex.webSocket;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/6
 * @description WebSocket 订阅请求
 */
public class BitMEXSubscribeReq {

    @SerializedName("op")
    private String op;
    @SerializedName("args")
    private List<String> args;

    public BitMEXSubscribeReq(String op, List<String> args) {
        this.op = op;
        this.args = args;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public List<String> getArgs() {
        return args;
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }

}

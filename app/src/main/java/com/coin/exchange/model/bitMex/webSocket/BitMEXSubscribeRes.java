package com.coin.exchange.model.bitMex.webSocket;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/12/17
 * @description
 */
public class BitMEXSubscribeRes {

    @SerializedName("success")
    private boolean success;
    @SerializedName("subscribe")
    private String subscribe;
    @SerializedName("request")
    private Request request;

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(String subscribe) {
        this.subscribe = subscribe;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public static class Request {
        @SerializedName("args")
        private List<String> args;
        @SerializedName("op")
        private String op;

        public List<String> getArgs() {
            return args;
        }

        public void setArgs(List<String> args) {
            this.args = args;
        }

        public String getOp() {
            return op;
        }

        public void setOp(String op) {
            this.op = op;
        }
    }
}

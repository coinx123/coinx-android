package com.coin.exchange.model.okex.webSocket;

import com.google.gson.annotations.SerializedName;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/7
 * @description
 */
public class LoginReq {

    @SerializedName("event")
    private String event;
    @SerializedName("parameters")
    private Parameters parameters;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    public static class Parameters {
        @SerializedName("api_key")
        private String api_key;
        @SerializedName("sign")
        private String sign;
        @SerializedName("passphrase")
        private String passphrase;
        @SerializedName("timestamp")
        private String timestamp;

        public String getApi_key() {
            return api_key;
        }

        public void setApi_key(String api_key) {
            this.api_key = api_key;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getPassphrase() {
            return passphrase;
        }

        public void setPassphrase(String passphrase) {
            this.passphrase = passphrase;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }
}

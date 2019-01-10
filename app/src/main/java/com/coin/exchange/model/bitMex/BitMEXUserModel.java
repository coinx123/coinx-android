package com.coin.exchange.model.bitMex;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/12/17
 * @description
 */
public class BitMEXUserModel {

    // 即 OK_EX_ID
    private String apiKey;

    // 即 OK_EX_SECRET
    private String secretKey;


    public BitMEXUserModel() {
    }

    public BitMEXUserModel(String apiKey, String secretKey) {
        this.apiKey = apiKey;
        this.secretKey = secretKey;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

}

package com.coin.exchange.model.okex;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/7
 * @description
 */
public class OkExUserModel {

    // 即 OK_EX_ID
    private String apiKey;

    // 即 OK_EX_SECRET
    private String secretKey;

    // 即 OK_EX_PASSPHRASE
    private String passphrase;

    public OkExUserModel() {
    }

    public OkExUserModel(String apiKey, String secretKey, String passphrase) {
        this.apiKey = apiKey;
        this.secretKey = secretKey;
        this.passphrase = passphrase;
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

    public String getPassphrase() {
        return passphrase;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }
}

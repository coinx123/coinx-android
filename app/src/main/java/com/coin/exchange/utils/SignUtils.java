package com.coin.exchange.utils;

import android.util.Base64;
import android.util.Log;

import com.coin.exchange.config.NetConfig;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class SignUtils {

    /**
     * 将加密后的字节数组转换成字符串
     *
     * @param b 字节数组
     * @return 字符串
     */
    private static String Hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toLowerCase();
    }

    /**
     * SHA256_HMAC加密
     *
     * @param secret  秘钥
     * @param message 消息
     * @return 加密后字符串
     */
    private static byte[] SHA256_HMAC(String secret, String message) {

        byte[] bytes;
        try {
            Mac SHA256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            SHA256_HMAC.init(secret_key);
            bytes = SHA256_HMAC.doFinal(message.getBytes());
        } catch (Exception e) {
            System.out.println("Error HmacSHA256 ===========" + e.getMessage());
            bytes = new byte[0];
        }

        return bytes;
    }

    /**
     * 获取bit mex 的签名密钥
     *
     * @param secretKey 密钥
     * @param verb      请求方法
     * @param path      请求路径
     * @param expires   时间戳（不含秒）
     * @param data      body数据
     * @return
     */
    public static String getBitMexSign(String secretKey,
                                       String verb,
                                       String path,
                                       String expires,
                                       String data) {
        String content = verb + path + expires + data;
//        Log.i("SignUtils", "content: " + content);
        return Hex(SHA256_HMAC(secretKey, content));
    }

    /**
     * 获取 okex 签名
     *
     * @param secretKey   API Key
     * @param method      请求方式（字母全部大写）
     * @param requestPath 请求接口路径
     * @param timestamp   时间戳（毫秒）[值与OK-ACCESS-TIMESTAMP请求头相同]
     * @param body        请求主体的字符串
     * @return
     */
    public static String getOkExSign(String secretKey,
                                     String method,
                                     String requestPath,
                                     String timestamp,
                                     String body) {

        String content = timestamp + method + requestPath + body;
//        Log.i("getOkExSign", "content: " + content);
        return Base64.encodeToString(SHA256_HMAC(secretKey, content), Base64.DEFAULT);
    }

    public static void main(String[] args) {

//        String apiSecret = "chNOOS4KvNXR_Xq4k4c9qsfoKWvnDecLATCRlcBwyKDYnWgO";
//
//        String verb = "GET";
//        String path = "/api/v1/instrument";
//        String expires = "1518064236";
//        String data = "";

        String apiSecret = "ArQO1EMNBDbC0O3xxOZWtDn_";

        String verb = "GET";
        String path = "/api/v1/" + NetConfig.BIT_MEX_CHECK_ACCOUNT_URL;
        String expires = "1545035743";
        String data = "";

        System.out.println(getBitMexSign(apiSecret, verb, path, expires, data));

//        String timestamp = "2018-03-08T10:59:25.789Z";
//        String method = "POST";
//        String requestPath = "/orders?before=2&limit=30";
//        String body = "{\"product_id\":\"BTC-USD-0309\",\"order_id\":\"377454671037440\"}";
//
//        // 这里运行不了，需要app中运行
//        System.out.println(getOkExSign(apiSecret, timestamp, method, requestPath, body));

    }

}
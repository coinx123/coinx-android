package com.coin.exchange.config.bitMex;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/10/30
 * @description
 */
public class BitMexHeaderHelper {

    // 5分钟内请求的总数量
    public static final String RATE_LIMIT = "x-ratelimit-limit";
    // 剩余可请求的头
    public static final String RATE_REMAINING = "x-ratelimit-remaining";
    // 重制时间
    public static final String RATE_RESET = "x-ratelimit-reset";

    public static final String EXPIRES = "api-expires";
    public static final String KEY = "api-key";
    public static final String SIGN = "api-signature";

}

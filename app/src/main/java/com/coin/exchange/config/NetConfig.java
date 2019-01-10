package com.coin.exchange.config;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/10/31
 * @description
 */
public class NetConfig {

    public static final int CONNECT_TIME_OUT = 30;
    public static final int READ_TIME_OUT = 30;
    public static final int WRITE_TIME_OUT = 30;

    //    public static final String BIT_MEX_TEST_URL = "https://testnet.bitmex.com/api/v1/";
//public static final String BIT_MEX_TEST_WEB_SOCKET = "wss://testnet.bitmex.com/realtime";

    public static final boolean isDebug = false;
    public static final String BIT_MEX_DEBUG_URL = "https://" + IPConfig.getBitMexIp(true) + "/api/v1/";
    public static final String BIT_MEX_DEBUG_HOST = "testnet.bitmex.com";
    public static final String BIT_MEX_DEBUG_WS = "wss://" + IPConfig.getBitMexIp(true) + "/realtime";

    public static final String BIT_MEX_RELEASE_URL = "https://" + IPConfig.getBitMexIp(false) + "/api/v1/";
    public static final String BIT_MEX_RELEASE_HOST = "www.bitmex.com";
    public static final String BIT_MEX_RELEASE_WS = "wss://" + IPConfig.getBitMexIp(false) + "/realtime";

    public static final String BIT_MEX_URL = isDebug ? BIT_MEX_DEBUG_URL : BIT_MEX_RELEASE_URL;
    public static final String BIT_MEX_HOST = isDebug ? BIT_MEX_DEBUG_HOST : BIT_MEX_RELEASE_HOST;
    public static final String BIT_MEX_WEB_SOCKET = isDebug ? BIT_MEX_DEBUG_WS : BIT_MEX_RELEASE_WS;
    public static final String BIT_MEX_CHECK_ACCOUNT_URL = "user";

    //    public static final String OK_EX_URL = "https://www.okex.com/";
    public static final String OK_EX_URL = "https://" + IPConfig.getOkExIp() + "/";
    public static final String OK_EX_HOST = "www.okex.com";

    // 合约交易WebSocket
//    public static final String OK_EX_FUTURE_WEB_SOCKET = "wss://real.okex.com:10440/websocket/okexapi";
    public static final String OK_EX_FUTURE_WEB_SOCKET = "wss://" + IPConfig.getOkExWsIp() + ":10440/websocket/okexapi";
    public static final String OK_EX_FUTURE_WEB_SOCKET_HOST = "real.okex.com";
    public static final String OK_EX_CHECK_ACCOUNT_URL = "/api/account/v3/wallet";


    private static final Set<String> SERVER_BASE_URL_SET = new HashSet<>();

    static {
        SERVER_BASE_URL_SET.add(BIT_MEX_DEBUG_URL);
        SERVER_BASE_URL_SET.add(BIT_MEX_DEBUG_WS);

        SERVER_BASE_URL_SET.add(BIT_MEX_RELEASE_URL);
        SERVER_BASE_URL_SET.add(BIT_MEX_RELEASE_WS);

        SERVER_BASE_URL_SET.add(OK_EX_URL);
        SERVER_BASE_URL_SET.add(OK_EX_FUTURE_WEB_SOCKET);

    }

    // 判断是否有服务器的url
    public static boolean isContainServerHost(String host) {
        for (String url : SERVER_BASE_URL_SET) {
            if (url.contains(host)) {
                return true;
            }
        }
        return false;
    }

}

package com.coin.libbase.net;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/6/7
 * @description
 */

public class ApiStatus {

    // 未知错误
    public static final int UNKNOWN = 1000;

    // 解析错误
    public static final int PARSE_ERROR = 1001;

    // 网络错误
    public static final int NETWORK_ERROR = 1002;

    // 协议出错
    public static final int HTTP_ERROR = 1003;

    // 服务器出错
    public static final int SERVICE_ERROR = 1004;

    // 客户端出错
    public static final int CLIENT_ERROR = 1005;

    public static final String SUCCESS = "success";
}

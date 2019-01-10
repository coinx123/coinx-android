package com.coin.special.proxy;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/8/31
 * @description 代理的配置
 */
public class ProxyConfig {

    // 服务器地址
    public static final String SERVER_ADDRESS = "127.0.0.1";

    // 代理端口
    public static int PORT = 15121;

    public static final int ERROR_TYPE = -1;
    // 主进程 代理端口，初始化为零
    public static int MAIN_PORT = ERROR_TYPE;

    // 是否显示io异常
    public static final boolean IS_SHOW_IO_EXCEPTION = true;

    // 缓冲区
    public static final int BUFFER_SIZE = 2 * 1024;

    // socket 连接最大时限
    public static final int TIMEOUT = 0;

    // 远程服务器的地址
    public static final String REMOTE_SERVER = "www0.qqweb0.com";
    // 远程服务器的端口
    public static final int REMOTE_SERVER_PORT = 28090;


}

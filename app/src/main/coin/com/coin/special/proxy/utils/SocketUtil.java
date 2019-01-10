package com.coin.special.proxy.utils;

import com.coin.special.proxy.ProxyConfig;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by zinc on 2018/5/12.
 */
public class SocketUtil {
    private static final String TAG = "SocketUtil";

    public static void close(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                if (ProxyConfig.IS_SHOW_IO_EXCEPTION) {
                    e.printStackTrace();
                }
            }
        }
    }

}

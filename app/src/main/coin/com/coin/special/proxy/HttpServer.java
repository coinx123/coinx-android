package com.coin.special.proxy;

import android.content.Context;

import com.coin.special.proxy.utils.LogUtil;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/5/11
 * @description
 */

public class HttpServer implements Runnable {

    private static String TAG = "HttpServer";

    public static HttpServer httpServer;

    final static ExecutorService executorPool = Executors.newCachedThreadPool();

    private volatile boolean isRunning = true;
    private ServerSocket server;

    public static void startServer(Context context) {
        httpServer = new HttpServer();
        Thread thread = new Thread(httpServer);
        thread.start();
    }

    public static void stopServer() {
        try {
            if (httpServer.server != null) {
                httpServer.server.close();
            }
            httpServer.isRunning = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                server = new ServerSocket(ProxyConfig.PORT);

                LogUtil.i(TAG, "port:" + server.getLocalPort() + "/" + ProxyConfig.PORT
                        + " waiting for connect... ... ");

                while (isRunning) {

                    try {
                        Socket socketIn = server.accept();
                        socketIn.setKeepAlive(true);
                        socketIn.setSoTimeout(ProxyConfig.TIMEOUT);

                        LogUtil.i(TAG, "远程IP和端口：" + socketIn.getRemoteSocketAddress().toString());

                        executorPool.execute(new SockInThread(socketIn));

                    } catch (IOException e) {
                        if (ProxyConfig.IS_SHOW_IO_EXCEPTION) {
                            e.printStackTrace();
                        }
                    }

                }
            } catch (BindException e) {

                ProxyConfig.PORT++;
                LogUtil.i(TAG, "run: BindException");
                e.printStackTrace();
                continue;

            } catch (IOException e) {
                if (ProxyConfig.IS_SHOW_IO_EXCEPTION) {
                    e.printStackTrace();
                }
            } finally {
                try {
                    if (server != null) {
                        server.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            break;
        }
    }

}

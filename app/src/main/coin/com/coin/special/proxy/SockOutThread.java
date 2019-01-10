package com.coin.special.proxy;

import com.coin.special.proxy.utils.SocketUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @date 创建时间：2018/5/12
 * @author Jiang zinc
 * @description 源服务器的socket输入流 传给 客户端的socket的输出流【源服务器======》客户端】
 *
 */

public class SockOutThread implements Runnable {

    private Socket mSocketIn;
    private Socket mSocketOut;

    public SockOutThread(Socket socketIn, Socket socketOut) {
        this.mSocketIn = socketIn;
        this.mSocketOut = socketOut;
    }

    @Override
    public void run() {

        try {
            InputStream isOut = new BufferedInputStream(mSocketOut.getInputStream());
            OutputStream osIn = new BufferedOutputStream(mSocketIn.getOutputStream());

            byte[] buffer = new byte[ProxyConfig.BUFFER_SIZE];

            int len;
            while ((len = isOut.read(buffer)) != -1) {
                if (len > 0) {
                    osIn.write(buffer, 0, len);
                    osIn.flush();
                }
                if (mSocketIn.isOutputShutdown() || mSocketOut.isInputShutdown()) {
                    break;
                }
            }

        } catch (IOException e) {
            if (ProxyConfig.IS_SHOW_IO_EXCEPTION) {
                e.printStackTrace();
            }
        } finally {
            SocketUtil.close(mSocketIn);
            SocketUtil.close(mSocketOut);
        }

    }

}

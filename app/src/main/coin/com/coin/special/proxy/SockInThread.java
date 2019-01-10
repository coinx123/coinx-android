package com.coin.special.proxy;

import android.text.TextUtils;
import android.util.Log;

import com.coin.exchange.config.NetConfig;
import com.coin.special.proxy.model.Message;
import com.coin.special.proxy.utils.LogUtil;
import com.coin.special.proxy.utils.SocketUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.SSLSocketFactory;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/5/12
 * @description
 */

public class SockInThread implements Runnable {

    private static final String TAG = "SockInThread";
    /**
     * 已连接到请求的服务器
     */
    private static final String AUTHORED = "HTTP/1.1 200 Connection established\r\n\r\n";
    /**
     * 本代理登陆失败
     */
    private static final String UNAUTHORED = "HTTP/1.1 407 Unauthorized\r\n\r\n";
    /**
     * 内部错误
     */
    private static final String SERVERERROR = "HTTP/1.1 500 Connection FAILED\r\n\r\n";

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    public final static Map<String, String> domainParseIp = new HashMap<>();
    public final static Map<String, Boolean> ipLocationAddress = new HashMap<>();

    private Socket mSocketIn;
    private Socket mSocketOut;

    private HttpCodec mHttpCodec;

    private Message mMessage;

    private String mAddress;

    public SockInThread(Socket socketIn) {
        mSocketIn = socketIn;
        mAddress = mSocketIn.getRemoteSocketAddress().toString();

        mHttpCodec = new HttpCodec();
        mMessage = new Message();
    }

    @Override
    public void run() {

        try {

            //与客户端的输入流
            InputStream isIn = new BufferedInputStream(mSocketIn.getInputStream());
            //与客户端的输出流
            OutputStream osIn = new BufferedOutputStream(mSocketIn.getOutputStream());

            //代理ip
            String proxyHost;
            //代理端口
            int proxyPort;

            //解析输入流
            boolean isError = localProxyParse(isIn, osIn);
            if (isError) {
                return;
            }

            String host = mMessage.getHost();
            boolean isConnectToRemote = NetConfig.isContainServerHost(host);

            if (isConnectToRemote) {
                try {
                    proxyHost = ProxyConfig.REMOTE_SERVER;
                    proxyPort = ProxyConfig.REMOTE_SERVER_PORT;
                    //创建与远程代理服务器的连接
                    mSocketOut = SSLSocketFactory.getDefault().createSocket(proxyHost, proxyPort);
                    Log.i(TAG, "连接 代理服务器 SSLSocket: [" + proxyHost + ":" + proxyPort + "]");
                } catch (Exception e) {
                    //创建与源服务器的连接
                    mSocketOut = connectSocket();
                    e.printStackTrace();
                }
            } else {
                mSocketOut = connectSocket();
            }

            //mSocketOut 为空
            if (mSocketOut == null) {
                LogUtil.e(TAG, "mSocketOut 为空！！！");
                mSocketIn.close();
                return;
            }

            mSocketOut.setSoTimeout(ProxyConfig.TIMEOUT);
            mSocketOut.setKeepAlive(true);

            String reqInfo = "Request Time  ：" + sdf.format(new Date()) + "\r\n" +
                    "TO    Host  ：" + mSocketOut.getInetAddress() + "\r\n" +
                    "TO    Port  ：" + mSocketOut.getPort() + "\r\n";
            Log.i(TAG, reqInfo);

            OutputStream osOut = new BufferedOutputStream(mSocketOut.getOutputStream());

            HttpServer.executorPool.execute(new SockOutThread(mSocketIn, mSocketOut));

            Log.i(TAG, mMessage.toString());

            byte[] headerData = mMessage.toString().getBytes();
            osOut.write(headerData);
            osOut.flush();

            readForwardDate(isIn, osOut);

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            if (ProxyConfig.IS_SHOW_IO_EXCEPTION) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SocketUtil.close(mSocketIn);
            SocketUtil.close(mSocketOut);
            LogUtil.i(TAG, "The address(" + mAddress + ") is stop.");
        }

    }

    /**
     * 创建与源服务器的连接
     *
     * @return
     */
    private Socket connectSocket() {
        try {
            String proxyHost = mMessage.getHost(); // 连接的域名
            int proxyPort = Integer.parseInt(mMessage.getPort());// 连接的端口
            Log.i(TAG, "run:Socket");
            return new Socket(proxyHost, proxyPort);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @return 是否失败 true说明失败
     * @date 创建时间 2018/5/25
     * @author Jiang zinc
     * @Description 本地代理开启与服务器连接socket前的报文解析
     * @version
     */
    private boolean localProxyParse(InputStream isIn, OutputStream osIn) throws IOException {
        //读取状态
        mHttpCodec.readStateLine(isIn, mMessage);

        //读取头部
        mHttpCodec.readHeaders(isIn, mMessage);

        //读取body
        mHttpCodec.readBody(isIn, mMessage);

        String connectInfo = "Request Time  ：" + sdf.format(new Date()) + "\r\n" +
                "From    Host  ：" + mSocketIn.getInetAddress() + "\r\n" +
                "From    Port  ：" + mSocketIn.getPort() + "\r\n" +
                "Proxy   Method：" + mMessage.getMethod() + "\r\n" +
                "Request Host  ：" + mMessage.getHost() + "\r\n" +
                "Request Port  ：" + mMessage.getPort() + "\r\n";
        LogUtil.i(TAG, connectInfo);

        //如果没解析出请求请求地址和端口，则返回错误信息
        if (mMessage.getHost() == null || mMessage.getPort() == null) {
            osIn.write(SERVERERROR.getBytes());
            osIn.flush();
            return true;
        }

        return false;

    }

    /**
     * 读取客户端发送过来的数据，发送给服务器端
     *
     * @param isIn
     * @param osOut
     */
    private void readForwardDate(InputStream isIn, OutputStream osOut) {
        byte[] buffer = new byte[ProxyConfig.BUFFER_SIZE];
        try {
            int len;
            while ((len = isIn.read(buffer)) != -1) {
                if (len > 0) {
                    osOut.write(buffer, 0, len);
                    osOut.flush();
                }
                if (mSocketIn.isInputShutdown() || mSocketOut.isOutputShutdown()) {
                    break;
                }
            }
        } catch (Exception e) {
            SocketUtil.close(mSocketIn);
            SocketUtil.close(mSocketOut);
        }
    }

}

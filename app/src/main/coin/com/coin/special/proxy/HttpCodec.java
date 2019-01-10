package com.coin.special.proxy;

import android.text.TextUtils;
import android.util.Log;

import com.coin.special.proxy.model.Message;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

/**
 * Created by zinc on 2018/5/12.
 */
public class HttpCodec {

    private String TAG = "HttpCodec";

    //回车和换行
    static final String CRLF = "\r\n";
    static final int CR = 13;
    static final int LF = 10;

    public static final String HEAD_HOST = "Host";

    private final ByteBuffer byteBuffer;

    public HttpCodec() {
        this.byteBuffer = ByteBuffer.allocate(10 * 1024);
    }

    /**
     * @Author: zinc
     * @Descrption: 读取状态行
     * @Date 下午6:18 2018/5/12
     */
    public void readStateLine(InputStream is, Message message) throws IOException {

        String stateLine = readLine(is);
        message.setStateLine(stateLine);

        String[] states = stateLine.split(" ");
        message.setMethod(states.length >= 1 ? states[0] : "");

    }

    /**
     * @Author: zinc
     * @Descrption: 读取头部
     * @Date 下午6:17 2018/5/12
     */
    public void readHeaders(InputStream is, Message message) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();

        while (true) {
            String line = readLine(is);

            stringBuilder.append(line);

            //如果读到空行 \r\n 响应头读完了
            if (isEmptyLine(line)) {
                break;
            }
            int index = line.indexOf(":");
            if (index > 0) {
                String key = line.substring(0, index);
                //+2 是为了要跳过":"和" "（空格） -2是为了去掉\r\n
                String value = line.substring(index + 2, line.length() - 2);
                message.addHeader(key, value);

                //是否为Host
                if (TextUtils.equals(HEAD_HOST, key)) {
                    String[] hosts = value.split(":");
                    message.setHost(hosts[0]);
                    if (message.getMethod().equals(Method.CONNECT)) {

                        message.setPort(hosts.length >= 2 ? hosts[1] : "443");

                    } else {

                        message.setPort(hosts.length >= 2 ? hosts[1] : "80");

                    }
                }
            }

        }

        Log.i(TAG, "头参数 >> " + stringBuilder.toString());

    }

    public void readBody(InputStream is, Message message) throws IOException {

        Map<String, List<String>> headers = message.getHeader();

        int contentLength = -1;
        if (headers.containsKey("Content-Length")) {
            contentLength = Integer.valueOf(message.getHeaderValue("Content-Length").get(0));
        }

        //根据分块编码 解析
        boolean isChunked = false;
        if (headers.containsKey("Transfer-Encoding")) {
            isChunked = headers.get("Transfer-Encoding").get(0).equalsIgnoreCase("chunked");
        }

        String body = null;
        if (contentLength > 0) {
            byte[] bytes = readBytes(is, contentLength);
            body = new String(bytes);
        } else if (isChunked) {
            body = readChunked(is);
        }

        message.setBody(body);

    }

    private boolean isEmptyLine(String line) {
        return TextUtils.equals(line, CRLF);
    }

    private String readLine(InputStream is) throws IOException {
        //清理byteBuffer
        byteBuffer.clear();
        //标记
        byteBuffer.mark();
        boolean isMaybeEndOfLine = false;
        byte b;
        //一次读一个字节
        while ((b = (byte) is.read()) != -1) {

            byteBuffer.put(b);

            //如果当前读到一个 \r
            if (b == CR) {
                isMaybeEndOfLine = true;
            } else if (isMaybeEndOfLine) {
                //读到 /n , 需要\r\n才能真正表明一行结束
                if (b == LF) {
                    //一行数据 position是长度
                    byte[] lineBytes = new byte[byteBuffer.position()];
                    //重回到mark的一行
                    byteBuffer.reset();
                    //从byteBuffer获得数据
                    byteBuffer.get(lineBytes);

                    //清空数据，并将标记移回开始
                    byteBuffer.clear();
                    byteBuffer.mark();

                    return new String(lineBytes);
                }
                isMaybeEndOfLine = false;
            }
        }
        throw new IOException("Response read Line");
    }

    public byte[] readBytes(InputStream is, int len) throws IOException {
        byte[] bytes = new byte[len];
        int readNum = 0;
        while (true) {
            readNum += is.read(bytes, readNum, len - readNum);
            //读取完毕
            if (readNum == len) {
                return bytes;
            }
        }
    }

    public String readChunked(InputStream is) throws IOException {
        int len = -1;
        boolean isEmptyData = false;
        StringBuffer chunked = new StringBuffer();
        while (true) {
            if (len < 0) {
                // chunk的长度
                String line = readLine(is);
                line = line.substring(0, line.length() - 2);
                //获得长度 16进制字符串转成10进制整型
                len = Integer.valueOf(line, 16);
                //如果长度是0 再读一个/r/n 响应结束
                isEmptyData = len == 0;
            } else {
                //读内容
                byte[] bytes = readBytes(is, len + 2);
                chunked.append(new String(bytes));
                len = -1;
                if (isEmptyData) {
                    return chunked.toString();
                }
            }
        }
    }

}

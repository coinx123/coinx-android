package com.coin.special.proxy.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zinc
 * @Describe: 报文信息
 * @Date: Created in 下午4:34 2018/5/12
 * @Modified By:
 */

public class Message {

    static final String CRLF = "\r\n";
    static final String SPACE = " ";
    static final String VERSION = "HTTP/1.1";
    static final String COLON = ":";

    private String method;
    private String host;
    private String port;

    private String body;

    private String stateLine;
    private Map<String, List<String>> header = new HashMap<>();

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getHost() {
        return host;

    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void addHeader(String key, String value) {
        if (header.containsKey(key)) {
            List<String> valueList = header.get(key);
            valueList.add(value);
        } else {
            List<String> valueList = new ArrayList<>();
            valueList.add(value);
            header.put(key, valueList);
        }
    }

    public List<String> getHeaderValue(String key) {
        return header.get(key);
    }

    public String getStateLine() {
        return stateLine;
    }

    public void setStateLine(String stateLine) {
        this.stateLine = stateLine;
    }

    public Map<String, List<String>> getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(stateLine);

        for (Map.Entry<String, List<String>> entry : header.entrySet()) {

            for (String value : entry.getValue()) {
                sb.append(entry.getKey());
                sb.append(COLON);
                sb.append(SPACE);
                sb.append(value);
                sb.append(CRLF);
            }

        }

        sb.append(CRLF);

        if (body != null) {
            //添加body
            sb.append(body);
        }

        return sb.toString();
    }

}

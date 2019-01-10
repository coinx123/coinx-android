package com.coin.exchange.webSocket.okEx;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/9
 * @description
 */
public interface MessageListener {

    /**
     * 连接成功回调
     */
    void onConnect();

    /**
     * 消息回调
     *
     * @param text 内容 json
     */
    void onMessage(String text);

    /**
     * 关闭回调
     *
     * @param loopLogin 是否需要重连
     */
    void onClose(boolean loopLogin);

}

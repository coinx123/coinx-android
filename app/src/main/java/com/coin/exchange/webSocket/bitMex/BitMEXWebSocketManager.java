package com.coin.exchange.webSocket.bitMex;

import android.util.Log;

import com.coin.exchange.config.NetConfig;
import com.coin.exchange.model.bitMex.webSocket.BitMEXSubscribeReq;
import com.coin.exchange.model.bitMex.webSocket.BitMEXSubscribeRes;
import com.coin.exchange.net.OkHttpHelper;
import com.coin.exchange.utils.GsonUtils;
import com.coin.exchange.webSocket.base.BaseWebSocketManager;
import com.google.gson.reflect.TypeToken;
import com.coin.libbase.model.CommonRes;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okio.ByteString;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/12/14
 * @description
 */
public class BitMEXWebSocketManager extends BaseWebSocketManager {

    private static BitMEXWebSocketManager mInstance;

    private static final String SUBSCRIBE = "subscribe";
    private static final String UNSUBSCRIBE = "unsubscribe";

    private static final String REPLACE_MSG = "{\"op\":\"%1$s\",\"args\":\"%2$s\"}";

    private BitMEXWebSocketManager() {
    }

    public static BitMEXWebSocketManager getInstance() {
        if (mInstance == null) {
            synchronized (BitMEXWebSocketManager.class) {
                if (mInstance == null) {
                    mInstance = new BitMEXWebSocketManager();
                }
            }
        }
        return mInstance;
    }

    @Override
    public void onMessage(String msg) {
        Log.i(TAG, "onMessage[String]: " + msg);

        // 解析订阅 与 取消订阅消息
        try {
            BitMEXSubscribeRes res = GsonUtils.getGson().fromJson(msg, BitMEXSubscribeRes.class);
            // 订阅结果
            boolean result = res.getSuccess();
            // 订阅信息
            String subscribeMsg = res.getSubscribe();
            // 订阅类型[订阅还是取消]
            String subscribeType = res.getRequest().getOp();

            synchronized (mLock) {
                if (subscribeType.equals(SUBSCRIBE)) {
                    // 订阅成功
                    if (result) {
                        // 添加至 成功队列中
                        mSucEvent.add(subscribeMsg);
                        mRetryEvent.remove(subscribeMsg);
                        mWaitingEvent.remove(subscribeMsg);
                    } else {
                        if (mSucEvent.contains(subscribeMsg)) {
                            mRetryEvent.remove(subscribeMsg);
                            mWaitingEvent.remove(subscribeMsg);
                        } else {
                            mRetryEvent.add(subscribeMsg);
                            mWaitingEvent.remove(subscribeMsg);
                        }
                    }
                }
            }
            return;
        } catch (Exception e) {
//            e.printStackTrace();
        }

        //TODO 需要添加解析的在这里添加
        try {
            List<CommonRes> list = new ArrayList<>();
            CommonRes commonRes = (CommonRes) GsonUtils.getInstance().fromJson(msg, new TypeToken<CommonRes>() {
            }.getType());//把JSON格式的字符串转为List
            list.add(commonRes);
            EventBus.getDefault().post(list); //  发送消息
        } catch (Exception e1) {
//                e1.printStackTrace();
        }
    }

    @Override
    public void onMessage(ByteString msg) {
        Log.i(TAG, "onMessage[byte]: " + msg);
    }

    @Override
    protected String getPingPongMsg() {
        return "ping";
    }

    @Override
    protected synchronized String assembleSendMsg(MsgType msgType, String msg) {
        List<String> list = new ArrayList<>();
        list.add(msg);
        BitMEXSubscribeReq model = new BitMEXSubscribeReq(
                msgType.equals(MsgType.SUBSCRIBE) ? SUBSCRIBE : UNSUBSCRIBE, list);

        String result = GsonUtils.getInstance().toJson(model);
        Log.i(TAG, "assembleSendMsg: " + result);
        return result;
    }

    @Override
    protected String getWebSocketUrl() {
        return NetConfig.BIT_MEX_WEB_SOCKET;
    }

    @Override
    protected OkHttpClient getOkHttpClient() {
        return OkHttpHelper.getBitMexInstance();
    }

    public void subscribeTest() {
        subscribe("instrument:XBTUSD");
    }

    public void unsubscribeTest() {
        unsubscribe("instrument:XBTUSD");
    }

    public void testCloseError() {
        mWebSocketListener._getWebSocket().cancel();
    }

    public void subscribeInstrument(String instrumentId) {
        subscribe("instrument:" + instrumentId);
    }

    public void unsubscribeInstrument(String instrumentId) {
        unsubscribe("instrument:" + instrumentId);
    }

    public void subscribeOrderBook10(String instrumentId) {
        subscribe("orderBook10:" + instrumentId);
    }

    public void unsubscribeOrderBook10(String instrumentId) {
        unsubscribe("orderBook10:" + instrumentId);
    }

    public void subscribeTradeList(String instrumentId) {
        subscribe("trade:" + instrumentId);
    }

    public void unsubscribeTradeList(String instrumentId) {
        unsubscribe("trade:" + instrumentId);
    }
}

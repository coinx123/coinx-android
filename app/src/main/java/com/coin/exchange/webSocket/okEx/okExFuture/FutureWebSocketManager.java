package com.coin.exchange.webSocket.okEx.okExFuture;

import android.util.Log;

import com.coin.exchange.config.NetConfig;
import com.coin.exchange.config.okEx.ChannelHelper;
import com.coin.exchange.model.okex.webSocket.SubscribeReq;
import com.coin.exchange.net.OkHttpHelper;
import com.coin.exchange.utils.GsonUtils;
import com.coin.exchange.webSocket.okEx.MessageListener;
import com.google.gson.reflect.TypeToken;
import com.coin.libbase.model.CommonRes;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.Request;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/9
 * @description
 */
public class FutureWebSocketManager implements MessageListener {

    private static final String TAG = "FutureWebSocketManager";
    public static final String ADD_CHANNEL = "addChannel";
    public static final String REMOVE_CHANNEL = "removeChannel";

    // 所有订阅的渠道
    private final Set<String> mALLSubSet;

    private final FutureWebSocketListener mListener;

    //depth 的引用计数法，为了在destroy的时候计数为1的时候取消订阅
    public static HashMap<String, Integer> depthMap = new HashMap<>();

    //指数 的引用计数法，为了在destroy的时候计数为1的时候取消订阅
    public static HashMap<String, Integer> indexMap = new HashMap<>();

    //Detail 的引用计数法，为了在destroy的时候计数为1的时候取消订阅
    public static HashMap<String, Integer> detailMap = new HashMap<>();

    private static volatile FutureWebSocketManager instance;

    public static FutureWebSocketManager getInstance() {
        if (instance == null) {
            synchronized (FutureWebSocketManager.class) {
                if (instance == null) {
                    instance = new FutureWebSocketManager();
                }
            }
        }
        return instance;
    }

    public FutureWebSocketManager() {
        this.mALLSubSet = new HashSet<>();
        this.mListener = new FutureWebSocketListener(this);
    }

    /**
     * 进行连接
     */
    private synchronized void connect() {

        int connectState = mListener.getConnectState();
        // 未连接
        if (connectState == FutureWebSocketListener.CONNECT_BEF) {
            // 设置为连接中
            mListener.setConnectState(FutureWebSocketListener.CONNECT_ING);
            Request request = new Request
                    .Builder()
                    .url(NetConfig.OK_EX_FUTURE_WEB_SOCKET)
                    .build();
            OkHttpHelper.getOkExForWsInstance().newWebSocket(request, mListener);
        } else {
            Log.i(TAG, "WebSocket 已经处于连接状态，code：" + connectState);
        }

    }

    @Override
    public void onConnect() {
        reconnect();
    }

    @Override
    public void onMessage(String text) {
//        Log.i(TAG, "onMessage: " + text);

        try {
            List<CommonRes> jsonListObject = (List<CommonRes>) GsonUtils.getInstance().fromJson(text, new TypeToken<List<CommonRes>>() {
            }.getType());//把JSON格式的字符串转为List
            EventBus.getDefault().post(jsonListObject); //  发送消息
        } catch (Exception e) {
//            e.printStackTrace();
            try {
                List<CommonRes> list = new ArrayList<>();
                CommonRes commonRes = (CommonRes) GsonUtils.getInstance().fromJson(text, new TypeToken<CommonRes>() {
                }.getType());//把JSON格式的字符串转为List
                list.add(commonRes);
                EventBus.getDefault().post(list); //  发送消息
            } catch (Exception e1) {
//                e1.printStackTrace();
            }
        }
    }

    @Override
    public void onClose(boolean loopLogin) {
        if (mALLSubSet.isEmpty()) {
            return;
        }
        connect();
    }

    /**
     * 关闭WebSocket
     */
    public void close() {
        synchronized (mALLSubSet) {
            Log.i(TAG, "onClose: " + mALLSubSet.toString());
            mALLSubSet.clear();
            mListener.release();
        }
    }

    /**
     * 测试使用
     * 模拟因错误导致关闭
     */
    public void mockErrorClose() {
        Log.i(TAG, "onClose: " + mALLSubSet.toString());
        mListener.release();
    }

    private void reconnect() {
        synchronized (mALLSubSet) {
            Log.i(TAG, "reconnect: " + mALLSubSet.toString());
            // 如果为空，不继续进行，节省空间
            if (mALLSubSet.isEmpty()) {
                return;
            }

            // 循环发出需要订阅的channel
            for (String channel : mALLSubSet) {
                SubscribeReq req = new SubscribeReq();
                req.setEvent(ADD_CHANNEL);
                req.setChannel(channel);
                mListener.sendMessage(req);
            }
        }
    }

    /**
     * 发送订阅信息
     *
     * @param event   事件：添加{@link #ADD_CHANNEL} 或 删除 {@link #REMOVE_CHANNEL}
     * @param channel 渠道{@link ChannelHelper}
     */
    private void sendMessage(String event, String channel) {

        if (event.equals(ADD_CHANNEL)) {
            synchronized (mALLSubSet) {
                mALLSubSet.add(channel);
            }
        } else if (event.equals(REMOVE_CHANNEL)) {
            synchronized (mALLSubSet) {
                mALLSubSet.remove(channel);
            }
        }

        // 已经连接上了，可以直接发
        if (mListener.getConnectState() == FutureWebSocketListener.CONNECT_ED) {
            SubscribeReq req = new SubscribeReq();
            req.setEvent(event);
            req.setChannel(channel);
            String message = GsonUtils.getInstance().toJson(req);
            Log.i(TAG, "sendMessage: " + message);
            mListener.sendMessage(req);
            return;
        }

        connect();

    }

    //==============================================================================================
    //===================================订阅 和 取消订阅的对外方======================================
    //==============================================================================================

    /**
     * 订阅合约详情
     *
     * @param x {@link ChannelHelper.X}
     * @param y {@link ChannelHelper.Y}
     */
    public void subscribeDetail(String x, String y) {
        String channel = String.format(ChannelHelper.FUTURE_DETAIL, x, y);
        if (detailMap.get(x) == null) {
            int de = 0;
            detailMap.put(x, ++de);
        } else {
            int de = detailMap.get(x);
            detailMap.put(x, ++de);
        }
        sendMessage(ADD_CHANNEL, channel);
    }

    /**
     * 取消订阅合约详情
     *
     * @param x {@link ChannelHelper.X}
     * @param y {@link ChannelHelper.Y}
     */
    public void unsubscribeDetail(String x, String y) {
        String channel = String.format(ChannelHelper.FUTURE_DETAIL, x, y);
        sendMessage(REMOVE_CHANNEL, channel);
    }

    /**
     * 订阅合约K线数据
     * [时间 ,开盘价,最高价,最低价,收盘价,成交量(张),成交量(币)]
     *
     * @param x {@link ChannelHelper.X}
     * @param y {@link ChannelHelper.Y}
     * @param z {@link ChannelHelper.Z}
     */
    public void subscribeKLine(String x, String y, String z) {
        String channel = String.format(ChannelHelper.FUTURE_K_LINE, x, y, z);
        sendMessage(ADD_CHANNEL, channel);
    }

    /**
     * 取消订阅合约K线数据
     *
     * @param x {@link ChannelHelper.X}
     * @param y {@link ChannelHelper.Y}
     * @param z {@link ChannelHelper.Z}
     */
    public void unsubscribeKLine(String x, String y, String z) {
        String channel = String.format(ChannelHelper.FUTURE_K_LINE, x, y, z);
        sendMessage(REMOVE_CHANNEL, channel);
    }

    /**
     * 订阅合约市场深度(200增量数据返回)
     *
     * @param x {@link ChannelHelper.X}
     * @param y {@link ChannelHelper.Y}
     */
    public void subscribeDepth(String x, String y) {
        String channel = String.format(ChannelHelper.FUTURE_DEPTH, x, y);
        sendMessage(ADD_CHANNEL, channel);
    }

    /**
     * 取消订阅合约市场深度(200增量数据返回)
     *
     * @param x {@link ChannelHelper.X}
     * @param y {@link ChannelHelper.Y}
     */
    public void unsubscribeDepth(String x, String y) {
        String channel = String.format(ChannelHelper.FUTURE_DEPTH, x, y);
        sendMessage(REMOVE_CHANNEL, channel);
    }

    /**
     * 订阅合约市场深度(全量返回)
     *
     * @param x {@link ChannelHelper.X}
     * @param y {@link ChannelHelper.Y}
     * @param z {@link ChannelHelper.Z}
     */
    public void subscribeAllDepth(String x, String y, String z) {
        String channel = String.format(ChannelHelper.FUTURE_ALL_DEPTH, x, y, z);
        if (depthMap.get(x) == null) {
            int de = 0;
            depthMap.put(x, ++de);
        } else {
            int de = depthMap.get(x);
            depthMap.put(x, ++de);
        }
        sendMessage(ADD_CHANNEL, channel);
    }

    /**
     * 取消订阅合约市场深度(全量返回)
     *
     * @param x {@link ChannelHelper.X}
     * @param y {@link ChannelHelper.Y}
     * @param z {@link ChannelHelper.Z1}
     */
    public void unsubscribeAllDepth(String x, String y, String z) {
        String channel = String.format(ChannelHelper.FUTURE_ALL_DEPTH, x, y, z);
        sendMessage(REMOVE_CHANNEL, channel);
    }


    /**
     * 订阅合约交易信息
     * [交易序号, 价格, 成交量(张), 时间, 买卖类型，成交量(币-新增)]
     * restapi v3> 获取成交数据>  trade_id 成交id， price 成交价格，qty 成交数量 timestamp  成交时间 side成交方向 
     *
     * @param x {@link ChannelHelper.X}
     * @param y {@link ChannelHelper.Y}
     */
    public void subscribeTrade(String x, String y) {
        String channel = String.format(ChannelHelper.FUTURE_TRADE, x, y);
        sendMessage(ADD_CHANNEL, channel);
    }

    /**
     * 取消订阅合约交易信息
     *
     * @param x {@link ChannelHelper.X}
     * @param y {@link ChannelHelper.Y}
     */
    public void unsubscribeTrade(String x, String y) {
        String channel = String.format(ChannelHelper.FUTURE_TRADE, x, y);
        sendMessage(REMOVE_CHANNEL, channel);
    }

    /**
     * 订阅合约指数
     *
     * @param x {@link ChannelHelper.X}
     */
    public void subscribeIndex(String x) {
        String channel = String.format(ChannelHelper.FUTURE_INDEX, x);
        if (indexMap.get(x) == null) {
            int de = 0;
            indexMap.put(x, ++de);
        } else {
            int de = indexMap.get(x);
            indexMap.put(x, ++de);
        }
        sendMessage(ADD_CHANNEL, channel);
    }

    /**
     * 取消订阅合约指数
     *
     * @param x {@link ChannelHelper.X}
     */
    public void unsubscribeIndex(String x) {
        String channel = String.format(ChannelHelper.FUTURE_INDEX, x);
        sendMessage(REMOVE_CHANNEL, channel);
    }

    /**
     * 合约预估交割价格
     *
     * @param x {@link ChannelHelper.X}
     */
    public void subscribeForecast(String x) {
        String channel = String.format(ChannelHelper.FUTURE_FORECAST, x);
        sendMessage(ADD_CHANNEL, channel);
    }

    /**
     * 取消订阅合约指数
     *
     * @param x {@link ChannelHelper.X}
     */
    public void unsubscribeForecast(String x) {
        String channel = String.format(ChannelHelper.FUTURE_FORECAST, x);
        sendMessage(REMOVE_CHANNEL, channel);
    }

}

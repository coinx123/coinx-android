package com.coin.exchange.net;

import com.coin.exchange.config.NetConfig;
import com.coin.exchange.net.api.BitMexApiService;
import com.coin.exchange.net.api.OkExApiService;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/6/6
 * @description 创建retrofit
 */

public class RetrofitFactory {

    private static final Object BIT_MEX_LOCK = new Object();
    private static final Object BIT_MEX_PURE_LOCK = new Object();

    private static final Object OK_EX_LOCK = new Object();
    private static final Object OK_EX_PURE_LOCK = new Object();

    private static BitMexApiService bitMexApiService;
    private static BitMexApiService bitMexPureApiService;

    private static OkExApiService okExApiService;
    private static OkExApiService okExPureApiService;

    public static BitMexApiService getBitMexApiService() {

        if (bitMexApiService == null) {
            synchronized (BIT_MEX_LOCK) {
                if (bitMexApiService == null) {
                    bitMexApiService = new RetrofitHelper(NetConfig.BIT_MEX_URL,
                            OkHttpHelper.getBitMexInstance())
                            .create(BitMexApiService.class);
                }
            }
        }
        return bitMexApiService;

    }

    public static BitMexApiService getBitMexPureApiService() {

        if (bitMexPureApiService == null) {
            synchronized (BIT_MEX_PURE_LOCK) {
                if (bitMexPureApiService == null) {
                    bitMexPureApiService = new RetrofitHelper(NetConfig.BIT_MEX_URL,
                            OkHttpHelper.getBitMexPureInstance())
                            .create(BitMexApiService.class);
                }
            }
        }
        return bitMexPureApiService;

    }

    public static OkExApiService getOkExApiService() {

        if (okExApiService == null) {
            synchronized (OK_EX_LOCK) {
                if (okExApiService == null) {
                    okExApiService = new RetrofitHelper(NetConfig.OK_EX_URL,
                            OkHttpHelper.getOkExInstance())
                            .create(OkExApiService.class);
                }
            }
        }
        return okExApiService;

    }

    public static OkExApiService getOkExPureApiService() {

        if (okExPureApiService == null) {
            synchronized (OK_EX_PURE_LOCK) {
                if (okExPureApiService == null) {
                    okExPureApiService = new RetrofitHelper(NetConfig.OK_EX_URL,
                            OkHttpHelper.getOkExPureInstance())
                            .create(OkExApiService.class);
                }
            }
        }
        return okExPureApiService;

    }

}

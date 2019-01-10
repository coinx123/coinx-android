package com.coin.exchange.net.api;


import com.coin.exchange.config.NetConfig;
import com.coin.exchange.config.okEx.OkExHeaderHelper;
import com.coin.exchange.model.okex.request.FuturesOrderReq;
import com.coin.exchange.model.okex.request.TransferReq;
import com.coin.exchange.model.okex.request.WithdrawalReq;
import com.coin.exchange.model.okex.response.CurrencyRes;
import com.coin.exchange.model.okex.response.FuturesAccountsCurrencyRes;
import com.coin.exchange.model.okex.response.FuturesAccountsLedger;
import com.coin.exchange.model.okex.response.FuturesAccountsRes;
import com.coin.exchange.model.okex.response.FuturesCancelOrderRes;
import com.coin.exchange.model.okex.response.FuturesInstrumentsBookRes;
import com.coin.exchange.model.okex.response.FuturesInstrumentsIndexRes;
import com.coin.exchange.model.okex.response.FuturesInstrumentsOpenInterestRes;
import com.coin.exchange.model.okex.response.FuturesInstrumentsRes;
import com.coin.exchange.model.okex.response.FuturesInstrumentsTickerList;
import com.coin.exchange.model.okex.response.FuturesInstrumentsTradesList;
import com.coin.exchange.model.okex.response.FuturesOrderListRes;
import com.coin.exchange.model.okex.response.FuturesOrderRes;
import com.coin.exchange.model.okex.response.FuturesPositionRes;
import com.coin.exchange.model.okex.response.FuturesRateRes;
import com.coin.exchange.model.okex.response.HistoryItemRes;
import com.coin.exchange.model.okex.response.LedgerRes;
import com.coin.exchange.model.okex.response.SingleFuturesPositionRes;
import com.coin.exchange.model.okex.response.SpotInstrumentTickerRes;
import com.coin.exchange.model.okex.response.TimeStampInfoRes;
import com.coin.exchange.model.okex.response.TransferRes;
import com.coin.exchange.model.okex.response.WalletInfoRes;
import com.coin.exchange.model.okex.response.WithdrawFreeRes;
import com.coin.exchange.model.okex.response.WithdrawalAddressItem;
import com.coin.exchange.model.okex.response.WithdrawalHistoryItem;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/6/7
 * @description api接口
 */

public interface OkExApiService {

    /**
     * 服务器时间戳
     *
     * @return
     */
    @GET("/api/general/v3/time")
    Single<TimeStampInfoRes> getTimeStamp();

    //================================华丽的分割线 钱包API start======================================

    /**
     * 获取币种列表
     *
     * @return
     */
    @GET("/api/account/v3/currencies")
    Single<List<CurrencyRes>> getCurrencyList();

    /**
     * 钱包账户信息
     *
     * @return 账户信息
     */
    @GET("api/account/v3/wallet")
    Single<List<WalletInfoRes>> getWalletInfo();

    /**
     * 单一币种账户信息
     * 获取钱包账户单个币种的余额、冻结和可用等信息。
     *
     * @param currency 当前币种
     */
    @GET("/api/account/v3/wallet/{currency}")
    Single<WalletInfoRes> getAccountOfCurrentInfo(@Path("currency") String currency);

    /**
     * 资金划转
     * OKEx站内在钱包账户、交易账户和子账户之间进行资金划转。
     *
     * @param transferReq
     * @return
     */
    @POST("/api/account/v3/transfer")
    Single<TransferRes> accountTransfer(@Body TransferReq transferReq);

    /**
     * 提币
     * 提币到OKCoin国际站账户，OKEx账户或数字货币地址。
     *
     * @param withdrawalReq
     * @return
     */
    @POST("/api/account/v3/withdrawal")
    Single<WalletInfoRes> withdrawal(@Body WithdrawalReq withdrawalReq);

    /**
     * 提币手续费
     * 查询提现到数字货币地址时，建议网络手续费信息。手续费越高，网络确认越快。
     *
     * @return
     */
    @GET("/api/account/v3/withdrawal/fee")
    Single<List<WithdrawFreeRes>> getWithdrawFree(@Query("currency") String currency);

    /**
     * 查询最近所有币种的提币记录
     *
     * @return
     */
    @GET("/api/account/v3/withdrawal/history")
    Single<List<HistoryItemRes>> getHistoryList();

    /**
     * 查询单个币种提币记录
     *
     * @return
     */
    @GET("/api/account/v3/withdrawal/history/{currency}")
    Single<List<HistoryItemRes>> getSingleCoinHistoryList(@Path("currency") String currency);

    /**
     * 账单流水查询
     * 查询钱包账户账单流水。流水会分页，并且按时间倒序排序和储存，最新的排在最前面。请参阅分页部分以获取第一页之后的其他记录。
     * <p>
     * <p>
     * <p>
     *
     * @param currency 币种，如btc
     * @param type     填写相应数字：1:充值
     *                 2:提现
     *                 13:撤销提现
     *                 18:转入合约账户
     *                 19:合约账户转出
     *                 20:转入子账户
     *                 21:子账户转出
     *                 28:领取
     *                 29:转入指数交易区
     *                 30:指数交易区转出
     *                 31:转入点对点账户
     *                 32:点对点账户转出
     *                 33:转入币币杠杆账户
     *                 34:币币杠杆账户转出
     *                 37:转入币币账户
     *                 38:币币账户转出
     * @param from     请求此页码之后的分页内容（举例页码为：1，2，3，4，5。from 4 只返回第5页，to 4只返回第3页）
     * @param to       请求此页码之前的分页内容（举例页码为：1，2，3，4，5。from 4 只返回第5页，to 4只返回第3页）
     * @param limit    分页返回的结果集数量，默认为100，最大为100，按时间顺序排列，越早下单的在前面
     * @return
     */
    @GET("/api/account/v3/ledger")
    Single<LedgerRes> getLedger(@Query("currency") String currency,
                                @Query("type") String type,
                                @Query("from") String from,
                                @Query("to") String to,
                                @Query("limit") String limit);

    /**
     * 获取充值地址
     * 获取各个币种的充值地址，包括曾使用过的老地址。
     *
     * @param currency 币种，如btc
     * @return
     */
    @GET("/api/account/v3/deposit/address")
    Single<List<WithdrawalAddressItem>> getWithdrawalAddressList(@Query("currency") String currency);

    /**
     * 获取所有币种充值记录
     *
     * @return
     */
    @GET("/api/account/v3/deposit/history")
    Single<List<WithdrawalHistoryItem>> getWithdrawalHistoryList();

    /**
     * 获取单个币种充值记录
     *
     * @return
     */
    @GET("/api/account/v3/deposit/history/{currency}")
    Single<List<WithdrawalHistoryItem>> getWithdrawalHistoryListViaCurrency(@Path("currency") String currency);

    //================================华丽的分割线 钱包API end========================================

    //================================华丽的分割线 合约API start========================================

    /**
     * 合约持仓信息
     *
     * @return
     */
    @GET("/api/futures/v3/position")
    Observable<FuturesPositionRes> getFuturesPositionRes();

    /**
     * 单个合约持仓信息合约持仓信息
     *
     * @return
     */
    @GET("/api/futures/v3/{instrument_id}/position")
    Single<SingleFuturesPositionRes> getSingleFuturesPositionRes(@Path("instrument_id") String instrumentId);

    /**
     * 所有币种合约账户信息
     *
     * @return
     */
    @GET("/api/futures/v3/accounts")
    Observable<FuturesAccountsRes> getFuturesAccounts();

    /**
     * 单个币种合约账户信息
     *
     * @return
     */
    @GET("/api/futures/v3/accounts/{currency}")
    Single<FuturesAccountsCurrencyRes> getFuturesAccountsCurrency(@Path("currency") String currency);

    /**
     * 账单流水查询
     *
     * @return
     */
    @GET("/api/futures/v3/accounts/{currency}/ledger")
    Single<List<FuturesAccountsLedger>> getFuturesAccountsLedger(@Path("currency") String currency);

    /**
     * 下单
     *
     * @return
     */
    @POST("/api/futures/v3/order")
    Single<FuturesOrderRes> futuresOrder(@Body FuturesOrderReq futuresOrderReq);

    /**
     * 撤单
     *
     * @return
     */
    @POST("/api/futures/v3/cancel_order/{instrument_id}/{order_id}")
    Single<FuturesCancelOrderRes> futuresCancelOrder(@Path("instrument_id") String instrumentId, @Path("order_id") String orderId);


    /**
     * 获取订单列表
     *
     * @param instrumentId 必填 合约ID，如BTC-USD-180213
     * @param status       必填 订单状态(-1.撤单成功；0:等待成交 1:部分成交 2:已完成）
     * @param from         选填，请求此页码之后的分页内容（举例页码为：1，2，3，4，5。from 4 只返回第5页，to 4只返回第3页）
     * @param to           选填，请求此页码之前的分页内容（举例页码为：1，2，3，4，5。from 4 只返回第5页，to 4只返回第3页）
     * @param limit        选填，分页返回的结果集数量，默认为100，最大为100，按时间顺序排列，越早下单的在前面
     * @return
     */
    @GET("/api/futures/v3/orders/{instrument_id}")
    Observable<FuturesOrderListRes> getFuturesOrderList(@Path("instrument_id") String instrumentId,
                                                        @Query("status") String status,
                                                        @Query("from") String from,
                                                        @Query("to") String to,
                                                        @Query("limit") String limit);

    /**
     * 获取合约信息
     *
     * @return
     */
    @GET("/api/futures/v3/instruments")
    Observable<List<FuturesInstrumentsRes>> getFuturesInstruments();

    /**
     * 获取深度数据
     * size 是选填
     *
     * @return
     */
    @GET("/api/futures/v3/instruments/{instrument_id}/book")
    Single<FuturesInstrumentsBookRes> getFuturesInstrumentsBook(@Path("instrument_id") String instrumentId,
                                                                @Query("size") String size);

    /**
     * 获取全部ticker信息
     *
     * @return
     */
    @GET("/api/futures/v3/instruments/ticker")
    Single<List<FuturesInstrumentsTickerList>> getFuturesInstrumentsTicker();

    /**
     * 获取全部ticker信息
     *
     * @return
     */
    @GET("/api/futures/v3/instruments/ticker")
    Observable<List<FuturesInstrumentsTickerList>> getFutInsTicker();

    /**
     * 获取单个ticker信息
     *
     * @return
     */
    @GET("/api/futures/v3/instruments/{instrument_id}/ticker")
    Single<FuturesInstrumentsTickerList> getFuturesInstrumentsTickerSingle(@Path("instrument_id") String instrumentId);

    /**
     * 获取成交数据
     *
     * @param instrumentId 必填 合约ID，如BTC-USD-180213
     * @param from         选填，请求此页码之后的分页内容（举例页码为：1，2，3，4，5。from 4 只返回第5页，to 4只返回第3页）
     * @param to           选填，请求此页码之前的分页内容（举例页码为：1，2，3，4，5。from 4 只返回第5页，to 4只返回第3页）不填的话写null
     * @param limit        选填，分页返回的结果集数量，默认为100，最大为100，按时间顺序排列，越早下单的在前面
     * @return
     */
    @GET("/api/futures/v3/instruments/{instrument_id}/trades")
    Single<List<FuturesInstrumentsTradesList>> getFuturesInstrumentsTrades(@Path("instrument_id") String instrumentId,
                                                                           @Query("from") String from,
                                                                           @Query("to") String to,
                                                                           @Query("limit") String limit);

    /**
     * 获取K线数据
     *
     * @param instrumentId 必填 合约ID，如BTC-USD-180213
     * @param start        选填，开始时间(ISO UTC标准，例如：2018-06-20T02:31:00Z)不填的话写null
     * @param end          选填，结束时间(ISO UTC标准，例如：2018-06-20T02:31:00Z)不填的话写null
     * @param granularity  选填，时间粒度，以秒为单位，如[60/180/300 900/1800/3600/7200/14400/21600/43200/86400/604800]
     * @return 返回值分别为[timestamp(开始时间), open(开盘价格), high(最高价格), low(最低价格), close(收盘价格), volume(交易量 ( 张)),currency_volume(按币种折算的交易量)]
     */
    @GET("/api/futures/v3/instruments/{instrument_id}/candles")
    Single<List<List<Double>>> getFuturesInstrumentsCandles(@Path("instrument_id") String instrumentId,
                                                            @Query("start") String start,
                                                            @Query("end") String end,
                                                            @Query("granularity") String granularity);


    /**
     * 获取指数信息
     *
     * @return
     */
    @GET("/api/futures/v3/instruments/{instrument_id}/index")
    Single<FuturesInstrumentsIndexRes> getFuturesInstrumentsIndex(@Path("instrument_id") String instrumentId);

    /**
     * 获取法币汇率
     *
     * @return
     */
    @GET("/api/futures/v3/rate")
    Single<FuturesRateRes> getFuturesRate();

    /**
     * 获取平台总持仓量
     *
     * @return
     */
    @GET("/api/futures/v3/instruments/{instrument_id}/open_interest")
    Single<FuturesInstrumentsOpenInterestRes> getFuturesInstrumentsOpenInterest(@Path("instrument_id") String instrumentId);

    @Headers(OkExHeaderHelper.CONTENT_TYPE + ":" + OkExHeaderHelper.CONTENT_TYPE_VALUE)
    @GET(NetConfig.OK_EX_CHECK_ACCOUNT_URL)
    Observable<List<WalletInfoRes>> validUserInfo(@Header(OkExHeaderHelper.KEY) String apiKey,
                                                  @Header(OkExHeaderHelper.PASSPHRASE) String passphraseKey,
                                                  @Header(OkExHeaderHelper.SIGN) String sign,
                                                  @Header(OkExHeaderHelper.TIMESTAMP) String expires);

    //================================华丽的分割线 合约API end========================================

    //================================华丽的分割线 币币API start========================================

    /**
     * 获取全部ticker信息
     *
     * @return ticker信息
     */
    @GET("/api/spot/v3/instruments/ticker")
    Single<List<SpotInstrumentTickerRes>> getSpotTickerInfo();

    /**
     * 获取某个ticker信息
     *
     * @return ticker信息
     */
    @GET("/api/spot/v3/instruments/{instrument_id}/ticker")
    Observable<SpotInstrumentTickerRes> getSpotTickerSingInfo(@Path("instrument_id") String instrumentId);
    //================================华丽的分割线 币币API end========================================

}

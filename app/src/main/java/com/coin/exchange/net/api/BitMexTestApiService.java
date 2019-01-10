//package com.coin.exchange.net.api;
//
//
//import com.coin.exchange.config.NetConfig;
//import com.coin.exchange.config.bitMex.BitMexHeaderHelper;
//import com.coin.exchange.config.okEx.OkExHeaderHelper;
//import com.coin.exchange.model.bitMex.request.LeverageReq;
//import com.coin.exchange.model.bitMex.request.CancelOrderReq;
//import com.coin.exchange.model.bitMex.request.OrderReq;
//import com.coin.exchange.model.bitMex.request.TransferMarginReq;
//import com.coin.exchange.model.bitMex.response.AnnouncementItemRes;
//import com.coin.exchange.model.bitMex.response.CommissionRes;
//import com.coin.exchange.model.bitMex.response.InstrumentItemRes;
//import com.coin.exchange.model.bitMex.response.OrderItemRes;
//import com.coin.exchange.model.bitMex.response.OrderRes;
//import com.coin.exchange.model.bitMex.response.PositionItemRes;
//import com.coin.exchange.model.bitMex.response.TransferMarginRes;
//import com.coin.exchange.model.bitMex.response.UserMarginRes;
//import com.coin.exchange.model.bitMex.response.bucketetRes;
//import com.coin.exchange.model.okex.response.FuturesInstrumentsTradesList;
//
//import java.util.List;
//import java.util.Map;
//
//import io.reactivex.Observable;
//import io.reactivex.Single;
//import retrofit2.http.Body;
//import retrofit2.http.GET;
//import retrofit2.http.HTTP;
//import retrofit2.http.Header;
//import retrofit2.http.Headers;
//import retrofit2.http.POST;
//import retrofit2.http.Query;
//
///**
// * @author Jiang zinc
// * @date 创建时间：2018/6/7
// * @description api接口
// */
//
//public interface BitMexTestApiService {
//
//    /**
//     * 公告
//     *
//     * @return 公告列表
//     */
//    @GET("announcement")
//    Single<List<AnnouncementItemRes>> getAnnouncementList();
//
//    @GET("instrument/active")
//    Observable<List<InstrumentItemRes>> getInstrumentList();
//
//    @GET("user/margin")
//    Single<UserMarginRes> getUserMargin();
//
//    /**
//     * 账户信息验证
//     *
//     * @param key
//     * @param expires
//     * @param sign
//     * @return
//     */
//    @Headers(OkExHeaderHelper.ACCEPT + ":" + OkExHeaderHelper.CONTENT_TYPE_VALUE)
//    @GET(NetConfig.BIT_MEX_CHECK_ACCOUNT_URL)
//    Observable<Object> validUserInfo(@Header(BitMexHeaderHelper.KEY) String key,
//                                     @Header(BitMexHeaderHelper.EXPIRES) String expires,
//                                     @Header(BitMexHeaderHelper.SIGN) String sign);
//
//    @GET("position")
//    Observable<List<PositionItemRes>> getPosition(@Query("filter") String filter);
//
//    /**
//     * 提交委托
//     */
//    @POST("order")
//    Single<OrderRes> postOrder(@Body OrderReq orderReq);
//
//    /**
//     * 调整保证金
//     *
//     * @param transferMarginReq
//     * @return
//     */
//    @POST("position/transferMargin")
//    Single<TransferMarginRes> postTransferMargin(@Body TransferMarginReq transferMarginReq);
//
//    @GET("user/margin")
//    Observable<UserMarginRes> getUserMarginForMargin();
//
//    /**
//     * 获取委托订单
//     *
//     * @param filter
//     * @param count  默认100
//     * @return
//     */
//    @GET("order")
//    Observable<List<OrderItemRes>> getOrder(@Query("symbol") String symbol,
//                                            @Query("filter") String filter,
//                                            @Query("count") int count);
//
//    @GET("user/commission")
//    Observable<Map<String, CommissionRes>> getCommission();
//
//    //    @DELETE("order")
//    @HTTP(method = "DELETE", path = "order", hasBody = true)
//    Single<List<OrderItemRes>> cancelOrder(@Body CancelOrderReq req);
//
//    /**
//     * 单个币种合约信息
//     *
//     * @return
//     */
//    @GET("instrument")
//    Observable<List<InstrumentItemRes>> getInstrument(@Query("symbol") String symbol);
//
//    /**
//     * 调整杠杆
//     *
//     * @param leverageReq
//     * @return
//     */
//    @POST("position/leverage")
//    Single<PositionItemRes> postLeverage(@Body LeverageReq leverageReq);
//
//    /**
//     * 合约成交列表
//     *
//     * @return
//     */
//    @GET("trade")
//    Observable<List<FuturesInstrumentsTradesList>> getInstrumentTradeList(@Query("symbol") String symbol,
//                                                                          @Query("count") double count,
//                                                                          @Query("reverse") boolean reverse);
//
//
//    /**
//     * K线图
//     *
//     * @return
//     */
//    @GET("trade/bucketed")
//    Single<List<bucketetRes>> getInstrumentTradeKList(@Query("symbol") String symbol,
//                                                      @Query("binSize") String binSize,
//                                                      @Query("count") double count,
//                                                      @Query("partial") boolean partial,
//                                                      @Query("reverse") boolean reverse);
//}

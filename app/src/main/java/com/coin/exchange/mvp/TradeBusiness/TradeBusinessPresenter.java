package com.coin.exchange.mvp.TradeBusiness;

import com.coin.exchange.config.okEx.ServerTimeStampHelper;
import com.coin.exchange.model.bitMex.request.CancelOrderReq;
import com.coin.exchange.model.bitMex.request.LeverageReq;
import com.coin.exchange.model.bitMex.request.OrderReq;
import com.coin.exchange.model.bitMex.response.CommissionRes;
import com.coin.exchange.model.bitMex.response.InstrumentItemRes;
import com.coin.exchange.model.bitMex.response.OrderItemRes;
import com.coin.exchange.model.bitMex.response.OrderRes;
import com.coin.exchange.model.bitMex.response.PositionItemRes;
import com.coin.exchange.model.bitMex.response.UserMarginRes;
import com.coin.exchange.model.okex.request.FuturesOrderReq;
import com.coin.exchange.model.okex.response.FuturesAccountsCurrencyRes;
import com.coin.exchange.model.okex.response.FuturesCancelOrderRes;
import com.coin.exchange.model.okex.response.FuturesInstrumentsBookRes;
import com.coin.exchange.model.okex.response.FuturesInstrumentsIndexRes;
import com.coin.exchange.model.okex.response.FuturesInstrumentsTickerList;
import com.coin.exchange.model.okex.response.FuturesOrderListRes;
import com.coin.exchange.model.okex.response.FuturesOrderRes;
import com.coin.exchange.model.okex.response.SingleFuturesPositionRes;
import com.coin.exchange.model.okex.vo.DelegationItemVO;
import com.coin.exchange.net.RetrofitFactory;
import com.coin.exchange.utils.AppUtils;
import com.coin.exchange.utils.DateUtils;
import com.coin.exchange.utils.IconInfoUtils;
import com.coin.libbase.net.rxjava.RxObservableSubscriber;
import com.coin.libbase.net.rxjava.RxSingleSubscriber;
import com.coin.libbase.presenter.BasePresenter;
import com.coin.libbase.utils.DoubleUtils;
import com.coin.libbase.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.coin.exchange.view.fragment.trade.delegation.DelegationFragment.CANCEL;
import static com.coin.exchange.view.fragment.trade.delegation.DelegationFragment.DONE;
import static com.coin.exchange.view.fragment.trade.delegation.DelegationFragment.WAITING;
import static com.coin.exchange.view.fragment.trade.position.PositionContentFragment.BUY;
import static com.coin.exchange.view.fragment.trade.position.PositionContentFragment.SELL;

public final class TradeBusinessPresenter extends BasePresenter<TradeBusinessView> {
    private static final String STATUS_FILTER = "{\"ordStatus\": \"%1$s\"}";

    @Inject
    public TradeBusinessPresenter(TradeBusinessView view) {
        super(view);
    }

    //获取单个ticker信息
    public void getFuturesInfo(String instrumentId) {
        RetrofitFactory
                .getOkExApiService()
                .getFuturesInstrumentsTickerSingle(instrumentId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSingleSubscriber<FuturesInstrumentsTickerList>(TradeBusinessPresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        mView.onGetFuturesInfoError(message);
                    }

                    @Override
                    protected void onSuccessRes(FuturesInstrumentsTickerList value) {
                        mView.onGetFuturesInfo(value);
                    }
                });
    }

    //获取24小时之前k线图信息
    public void getCandles(String instrumentId, final double prise) {
        RetrofitFactory
                .getOkExApiService()
                .getFuturesInstrumentsCandles(instrumentId,
                        ServerTimeStampHelper.getInstance().getCurrentTimeStamp(ServerTimeStampHelper.Type.ISO8601_24),
                        null,
                        "60")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSingleSubscriber<List<List<Double>>>(TradeBusinessPresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        mView.onGetCandlesError(message);
                    }

                    @Override
                    protected void onSuccessRes(List<List<Double>> value) {
                        mView.onGetCandles(value, prise);
                    }
                });
    }

    //获取合约持仓信息
    public void getFuturesPositionInfo(String instrumentId) {
        RetrofitFactory
                .getOkExApiService()
                .getSingleFuturesPositionRes(instrumentId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSingleSubscriber<SingleFuturesPositionRes>(TradeBusinessPresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        mView.onGetFuturesPositionInfoError(message);
                    }

                    @Override
                    protected void onSuccessRes(SingleFuturesPositionRes value) {
                        mView.onGetFuturesPositionInfo(value);
                    }
                });
    }

    //获取深度数据
    public void getFuturesBook(String instrumentId) {
        RetrofitFactory
                .getOkExApiService()
                .getFuturesInstrumentsBook(instrumentId, "5")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSingleSubscriber<FuturesInstrumentsBookRes>(TradeBusinessPresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        mView.onGetFuturesBookError(message);
                    }

                    @Override
                    protected void onSuccessRes(FuturesInstrumentsBookRes value) {
                        mView.onGetFuturesBook(value);
                    }
                });
    }

    //获取指数信息
    public void getFuturesIndex(String instrumentId) {
        RetrofitFactory
                .getOkExApiService()
                .getFuturesInstrumentsIndex(instrumentId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSingleSubscriber<FuturesInstrumentsIndexRes>(TradeBusinessPresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        mView.onGetFuturesIndexError(message);
                    }

                    @Override
                    protected void onSuccessRes(FuturesInstrumentsIndexRes value) {
                        mView.onGetFuturesIndex(value);
                    }
                });
    }

    //单个币种合约账户信息
    public void getFuturesCurrencyInfo(String instrumentId) {
        RetrofitFactory
                .getOkExApiService()
                .getFuturesAccountsCurrency(instrumentId.split("-")[0].toLowerCase())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSingleSubscriber<FuturesAccountsCurrencyRes>(TradeBusinessPresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        mView.onGetFuturesCurrencyInfoError(message);
                    }

                    @Override
                    protected void onSuccessRes(FuturesAccountsCurrencyRes value) {
                        mView.onGetFuturesCurrencyInfo(value);
                    }
                });
    }

    //下单
    public void futuresOrder(FuturesOrderReq futuresOrderReq) {
        RetrofitFactory
                .getOkExApiService()
                .futuresOrder(futuresOrderReq)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSingleSubscriber<FuturesOrderRes>(TradeBusinessPresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        mView.onFuturesOrderError(message);
                    }

                    @Override
                    protected void onSuccessRes(FuturesOrderRes value) {
                        mView.onFuturesOrder(value);
                    }
                });
    }

    //撤单
    public void cancelOrder(final String orderId, String insId) {
        RetrofitFactory
                .getOkExApiService()
                .futuresCancelOrder(insId, orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSingleSubscriber<FuturesCancelOrderRes>(TradeBusinessPresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        mView.onCancelOrderError(message);
                    }

                    @Override
                    protected void onSuccessRes(FuturesCancelOrderRes value) {
                        mView.onCancelOrder(value);
                    }
                });
    }

    //获取委托列表
    public void getDelegationList(String insId, String status, final int from, final String type) {
        RetrofitFactory
                .getOkExApiService()
                .getFuturesOrderList(insId, status, from + "", null, null)
                .subscribeOn(Schedulers.io())
                .map(new Function<FuturesOrderListRes, List<DelegationItemVO>>() {
                    @Override
                    public List<DelegationItemVO> apply(FuturesOrderListRes futuresOrderListRes) throws Exception {
                        List<DelegationItemVO> result = new ArrayList<>();

                        if (!futuresOrderListRes.getResult()) {
                            throw new RuntimeException("Request failure.");
                        }

                        for (FuturesOrderListRes.Order_info orderInfo : futuresOrderListRes.getOrder_info()) {
                            DelegationItemVO itemVO = new DelegationItemVO(AppUtils.OKEX);

                            // 币种id
                            itemVO.setInsId(orderInfo.getInstrument_id());
                            // 订单id
                            itemVO.setOrderId(orderInfo.getOrder_id());
                            // 名称
                            itemVO.setName(IconInfoUtils.getRootName(orderInfo.getInstrument_id()));
                            // icon类型
                            itemVO.setNameDes(type + "-" + IconInfoUtils.getDesName(orderInfo.getInstrument_id()));
                            // 状态
                            itemVO.setStatus(orderInfo.getStatus());
                            // 委托量
                            itemVO.setDelegationValue(orderInfo.getSize());
                            // 委托价
                            itemVO.setDelegationPrice(orderInfo.getPrice());
                            // 成交量
                            itemVO.setDoneValue(orderInfo.getFilled_qty());
                            // 成交价
                            itemVO.setDonePrice(orderInfo.getPrice_avg());
                            // 手续费
                            itemVO.setFee(orderInfo.getFee());

                            // 保证金
                            // 合约面值（美金）
                            int contractVal = 100;
                            try {
                                double buyPrice = Double.parseDouble(orderInfo.getPrice());
                                double qty = Double.parseDouble(orderInfo.getSize());
                                double leverage = Integer.parseInt(orderInfo.getLeverage());
                                double security = contractVal / buyPrice * qty / leverage;
                                itemVO.setSecurity(security);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            // 时间
                            try {
                                itemVO.setTime(DateUtils.getMM_DD_HH_MMViaTimeStamp(
                                        Long.parseLong(orderInfo.getTimestamp())));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            // 交易类型
                            String type = orderInfo.getType();
                            String dealType = "";
                            switch (type) {
                                case "1":
                                    dealType = "买入开多 " + orderInfo.getLeverage() + "X";
                                    itemVO.setSell(false);
                                    break;
                                case "2":
                                    dealType = "买入开空 " + orderInfo.getLeverage() + "X";
                                    itemVO.setSell(false);
                                    break;
                                case "3":
                                    dealType = "卖出平多 " + orderInfo.getLeverage() + "X";
                                    itemVO.setSell(true);
                                    break;
                                case "4":
                                    dealType = "卖出平空 " + orderInfo.getLeverage() + "X";
                                    itemVO.setSell(true);
                                    break;
                            }
                            itemVO.setDealType(dealType);

                            result.add(itemVO);
                        }

                        return result;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObservableSubscriber<List<DelegationItemVO>>(TradeBusinessPresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        mView.onGetDelegationListError(message);
                    }

                    @Override
                    protected void onSuccessRes(List<DelegationItemVO> value) {
                        mView.onGetDelegationList(value);
                    }
                });

    }

    public void getBitmexInstrument(String instrumentId) {
        RetrofitFactory
                .getBitMexApiService()
                .getInstrument(instrumentId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObservableSubscriber<List<InstrumentItemRes>>(TradeBusinessPresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        mView.onGetBitmexInstrumentError(message);
                    }

                    @Override
                    protected void onSuccessRes(List<InstrumentItemRes> value) {
                        mView.onGetBitmexInstrument(value);
                    }
                });
    }

    public void getBitmexPositionInfo(String instrumentId) {
        RetrofitFactory.getBitMexApiService()
                .getPosition("{\"symbol\": \"" + instrumentId + "\"}")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObservableSubscriber<List<PositionItemRes>>(TradeBusinessPresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        mView.onGetBitmexPositionInfoError(message);
                    }

                    @Override
                    protected void onSuccessRes(List<PositionItemRes> value) {
                        mView.onGetBitmexPositionInfo(value);
                    }
                });

    }

    //获取用户余额
    public void getBitmexUserMargin() {
        RetrofitFactory
                .getBitMexApiService()
                .getUserMargin()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSingleSubscriber<UserMarginRes>(TradeBusinessPresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        mView.onGetBitmexUserMarginError(message);
                    }

                    @Override
                    protected void onSuccessRes(UserMarginRes value) {
                        mView.onGetBitmexUserMargin(value);
                    }
                });
    }

    //调整杠杆
    public void postLeverage(String instrumentId, double leverage) {
        LeverageReq leverageReq = new LeverageReq(instrumentId, leverage);
        RetrofitFactory
                .getBitMexApiService()
                .postLeverage(leverageReq)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSingleSubscriber<PositionItemRes>(TradeBusinessPresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        mView.onPostLeverageError(message);
                    }

                    @Override
                    protected void onSuccessRes(PositionItemRes value) {
                        mView.onPostLeverage(value);
                    }
                });
    }

    //bitmex下单
    public void bitmexFuturesOrder(OrderReq orderReq) {
        RetrofitFactory
                .getBitMexApiService()
                .postOrder(orderReq)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSingleSubscriber<OrderRes>(TradeBusinessPresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        ToastUtil.showCenter(message);
                    }

                    @Override
                    protected void onSuccessRes(OrderRes value) {
                        ToastUtil.showCenter("下单成功");
                        getCommissionInfo(value.getSymbol(), "New", 100);
                    }
                });
    }

    //bitmex 获取佣金费率，委托中要用到
    public void getCommissionInfo(final String insId, final String mStatus, final int pageCount) {
        RetrofitFactory
                .getBitMexApiService()
                .getCommission()
                .subscribeOn(Schedulers.io())
                .doOnNext(new Consumer<Map<String, CommissionRes>>() {
                    @Override
                    public void accept(Map<String, CommissionRes> commissionResMap) throws Exception {
                        getBitMEXDeleInfo(insId, commissionResMap, mStatus, pageCount);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObservableSubscriber<Map<String, CommissionRes>>(TradeBusinessPresenter.this) {
                    @Override
                    protected void onError(int code, String message) {

                    }

                    @Override
                    protected void onSuccessRes(Map<String, CommissionRes> value) {

                    }
                });
    }

    //获取委托列表
    public void getBitMEXDeleInfo(final String insId, final Map<String, CommissionRes> commissionResMap,
                                  String mStatus,
                                  int pageCount) {
        RetrofitFactory
                .getBitMexApiService()
                .getOrder(insId, String.format(STATUS_FILTER, mStatus), pageCount)
                .subscribeOn(Schedulers.io())
                .map(new Function<List<OrderItemRes>, List<DelegationItemVO>>() {
                    @Override
                    public List<DelegationItemVO> apply(List<OrderItemRes> orderItemResList) throws Exception {
                        List<DelegationItemVO> result = new ArrayList<>();
                        for (OrderItemRes item : orderItemResList) {
                            DelegationItemVO itemVO = new DelegationItemVO(AppUtils.BITMEX);

                            // 订单id
                            itemVO.setOrderId(item.getOrderID());

                            // 名称
                            itemVO.setName(item.getSymbol());

                            // 订单时间
                            itemVO.setTime(DateUtils.getMM_DD_HH_MMViaISO8601ForBitMex(item.getTimestamp()));

                            // 成交量
                            itemVO.setDoneValue(item.getCumQty());

                            // 委托量
                            itemVO.setDelegationValue(item.getOrderQty() + "");

                            // 委托价
                            itemVO.setDelegationPrice(DoubleUtils.showAllDecimalString(item.getPrice()));

                            // 成交均价
                            itemVO.setDonePrice(item.getAvgPx() == null ? "0" : item.getAvgPx());

                            // 手续费 需要计算
                            double fee;
                            if (item.getOrdType().toLowerCase().equals("market")) {
                                fee = commissionResMap.get(item.getSymbol()).getTakerFee();
                            } else {
                                fee = commissionResMap.get(item.getSymbol()).getMakerFee();
                            }
                            double resultFee = fee * item.getPrice() * item.getOrderQty();
                            itemVO.setFee(DoubleUtils.formatSixDecimalString(resultFee));

                            // 类型
                            itemVO.setDealType(item.getSide().equals(BUY) ? "买入做多" : "卖出做空");

                            // 设置状态
                            switch (item.getOrdStatus().toLowerCase()) {
                                case "new":
                                    itemVO.setStatus(WAITING);
                                    break;

                                case "filled":
                                    itemVO.setStatus(DONE);
                                    break;

                                case "canceled":
                                    itemVO.setStatus(CANCEL);
                                    break;
                            }

                            itemVO.setSell(item.getSide().equals(SELL));

                            result.add(itemVO);

                        }
                        return result;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObservableSubscriber<List<DelegationItemVO>>(TradeBusinessPresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        mView.onGetDelegationListError(message);
                    }

                    @Override
                    protected void onSuccessRes(List<DelegationItemVO> value) {
                        mView.onGetDelegationList(value);
                    }
                });
    }

    //撤单
    public void cancelBitMexOrder(String orderId) {
        CancelOrderReq req = new CancelOrderReq(orderId);
        RetrofitFactory
                .getBitMexApiService()
                .cancelOrder(req)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSingleSubscriber<List<OrderItemRes>>(TradeBusinessPresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        mView.onCancelBitMexOrderError(message);
                    }

                    @Override
                    protected void onSuccessRes(List<OrderItemRes> value) {
                        mView.onCancelBitMexOrder(value);
                    }
                });
    }
}

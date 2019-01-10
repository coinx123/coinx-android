package com.coin.exchange.presenter;

import android.util.Log;

import com.coin.exchange.model.bitMex.request.CancelOrderReq;
import com.coin.exchange.model.bitMex.response.CommissionRes;
import com.coin.exchange.model.bitMex.response.OrderItemRes;
import com.coin.exchange.model.okex.response.FuturesCancelOrderRes;
import com.coin.exchange.model.okex.response.FuturesInstrumentsRes;
import com.coin.exchange.model.okex.response.FuturesOrderListRes;
import com.coin.exchange.model.okex.vo.DelegationItemVO;
import com.coin.exchange.net.RetrofitFactory;
import com.coin.exchange.utils.AppUtils;
import com.coin.exchange.utils.DateUtils;
import com.coin.exchange.utils.IconInfoUtils;
import com.coin.exchange.view.fragment.trade.delegation.DelegationContentView;
import com.coin.libbase.net.rxjava.RxObservableSubscriber;
import com.coin.libbase.net.rxjava.RxSingleSubscriber;
import com.coin.libbase.presenter.BasePresenter;
import com.coin.libbase.utils.DoubleUtils;

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

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/27
 * @description
 */
public class DelegationContentPresenter extends BasePresenter<DelegationContentView> {

    private static final String STATUS_FILTER = "{\"ordStatus\": \"%1$s\"}";

    @Inject
    public DelegationContentPresenter(DelegationContentView mView) {
        super(mView);
    }

    private void getDelegationList(final List<FuturesInstrumentsRes> futuresList,
                                   String insId,
                                   String status,
                                   final int from,
                                   final String type) {

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
                            int contractVal = 0;
                            for (FuturesInstrumentsRes futureItem : futuresList) {
                                if (futureItem.getInstrument_id().equals(orderInfo.getInstrument_id())) {
                                    contractVal = futureItem.getContract_val();
                                    break;
                                }
                            }
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
                                itemVO.setTime(DateUtils.getMM_DD_HH_MMViaISO8601(orderInfo.getTimestamp()));
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
                .subscribe(new RxObservableSubscriber<List<DelegationItemVO>>(DelegationContentPresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        mView.onGetDelegationListError();
                        Log.i(TAG, "onError: [code:" + code + "; message:" + message + "]");
                    }

                    @Override
                    protected void onSuccessRes(List<DelegationItemVO> list) {
                        mView.onGetDelegationListSuc(list, from == 0);
                    }
                });

    }

    public void getFutureInfo(final String insId,
                              final String status,
                              final int from,
                              final String type) {
        RetrofitFactory
                .getOkExApiService()
                .getFuturesInstruments()
                .subscribeOn(Schedulers.io())
                .doOnNext(new Consumer<List<FuturesInstrumentsRes>>() {
                    @Override
                    public void accept(List<FuturesInstrumentsRes> futuresInstrumentsRes) throws Exception {
                        getDelegationList(futuresInstrumentsRes, insId, status, from, type);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObservableSubscriber<List<FuturesInstrumentsRes>>(DelegationContentPresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        mView.onGetDelegationListError();
                        Log.i(TAG, "onError: [code:" + code + "; message:" + message + "]");
                    }

                    @Override
                    protected void onSuccessRes(List<FuturesInstrumentsRes> value) {

                    }
                });
    }

    public void cancelOrder(final String orderId, String insId, final int position) {
        RetrofitFactory
                .getOkExApiService()
                .futuresCancelOrder(insId, orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSingleSubscriber<FuturesCancelOrderRes>(DelegationContentPresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        mView.onCancelError();
                    }

                    @Override
                    protected void onSuccessRes(FuturesCancelOrderRes value) {
                        if (value.isResult()) {
                            mView.onCancelSuc(orderId, position);
                        } else {
                            mView.onCancelError();
                        }
                    }
                });
    }

    public void cancelBitMexOrder(final String orderId, final int position) {
        CancelOrderReq req = new CancelOrderReq(orderId);
        RetrofitFactory
                .getBitMexApiService()
                .cancelOrder(req)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSingleSubscriber<List<OrderItemRes>>(DelegationContentPresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        mView.onCancelError();
                    }

                    @Override
                    protected void onSuccessRes(List<OrderItemRes> value) {
                        mView.onCancelSuc(orderId, position);
                    }
                });
    }

    public void getCommissionInfo(final String mStatus, final int pageCount) {
        RetrofitFactory
                .getBitMexApiService()
                .getCommission()
                .subscribeOn(Schedulers.io())
                .doOnNext(new Consumer<Map<String, CommissionRes>>() {
                    @Override
                    public void accept(Map<String, CommissionRes> commissionResMap) throws Exception {
                        getBitMEXDeleInfo(commissionResMap, mStatus, pageCount);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObservableSubscriber<Map<String, CommissionRes>>(DelegationContentPresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        mView.onGetDelegationListError();
                    }

                    @Override
                    protected void onSuccessRes(Map<String, CommissionRes> value) {

                    }
                });
    }

    public void getBitMEXDeleInfo(final Map<String, CommissionRes> commissionResMap,
                                  String mStatus,
                                  int pageCount) {
        RetrofitFactory
                .getBitMexApiService()
                .getOrder(null,String.format(STATUS_FILTER, mStatus), pageCount)
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
                .subscribe(new RxObservableSubscriber<List<DelegationItemVO>>(DelegationContentPresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        mView.onGetDelegationListError();
                    }

                    @Override
                    protected void onSuccessRes(List<DelegationItemVO> value) {
                        mView.onGetDelegationListSuc(value, true);
                    }
                });
    }
}

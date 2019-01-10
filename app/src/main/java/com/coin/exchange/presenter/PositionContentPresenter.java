package com.coin.exchange.presenter;

import android.util.Log;

import com.coin.exchange.model.bitMex.request.OrderReq;
import com.coin.exchange.model.bitMex.request.TransferMarginReq;
import com.coin.exchange.model.bitMex.response.OrderRes;
import com.coin.exchange.model.bitMex.response.PositionItemRes;
import com.coin.exchange.model.bitMex.response.TransferMarginRes;
import com.coin.exchange.model.bitMex.response.UserMarginRes;
import com.coin.exchange.model.okex.request.FuturesOrderReq;
import com.coin.exchange.model.okex.response.FuturesInstrumentsRes;
import com.coin.exchange.model.okex.response.FuturesInstrumentsTickerList;
import com.coin.exchange.model.okex.response.FuturesOrderRes;
import com.coin.exchange.model.okex.response.FuturesPositionRes;
import com.coin.exchange.model.okex.vo.PositionItemVO;
import com.coin.exchange.net.RetrofitFactory;
import com.coin.exchange.utils.AppUtils;
import com.coin.exchange.utils.DateUtils;
import com.coin.exchange.utils.IconInfoUtils;
import com.coin.exchange.view.fragment.trade.position.PositionContentView;
import com.coin.libbase.net.rxjava.RxObservableSubscriber;
import com.coin.libbase.net.rxjava.RxSingleSubscriber;
import com.coin.libbase.presenter.BasePresenter;
import com.coin.libbase.utils.DoubleUtils;
import com.github.fujianlian.klinechart.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/21
 * @description
 */
public class PositionContentPresenter extends BasePresenter<PositionContentView> {

    public static final String INCREASE = "多头";
    public static final String DECREASE = "空头";

    // 1亿
    public static final long ONE_HUNDRED_MILLION = 1_0000_0000;

    @Inject
    public PositionContentPresenter(PositionContentView mView) {
        super(mView);
    }

    public void getBitMexPositionInfo() {
        RetrofitFactory
                .getBitMexApiService()
                .getUserMarginForMargin()
                .subscribeOn(Schedulers.io())
                .doOnNext(new Consumer<UserMarginRes>() {
                    @Override
                    public void accept(UserMarginRes userMarginRes) throws Exception {
                        getBitMexPosition(userMarginRes);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObservableSubscriber<UserMarginRes>(PositionContentPresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        Log.i(TAG, "onError: [code:" + code + "; message:" + message + "]");
                        mView.onGetFuturesPositionError();
                    }

                    @Override
                    protected void onSuccessRes(UserMarginRes value) {

                    }
                });
    }

    /**
     * 获取 bitMex 持仓
     */
    public void getBitMexPosition(final UserMarginRes userMarginRes) {
        RetrofitFactory.getBitMexApiService()
                .getPosition("{\"isOpen\":true}")
                .subscribeOn(Schedulers.io())
                .map(new Function<List<PositionItemRes>, List<List>>() {
                    @Override
                    public List<List> apply(List<PositionItemRes> positionItemList) throws Exception {
                        List<String> typeList = new ArrayList<>();
                        List<PositionItemVO> contentList = new ArrayList<>();

                        List<List> result = new ArrayList<>();
                        result.add(typeList);
                        result.add(contentList);

                        // 如果没有内容
                        if (positionItemList.size() <= 0) {
                            return result;
                        }

                        for (PositionItemRes item : positionItemList) {
                            PositionItemVO positionItemVO = new PositionItemVO(AppUtils.BITMEX);
                            // 添加至选择列表
                            if (!typeList.contains(item.getUnderlying())) {
                                typeList.add(item.getUnderlying());
                            }

                            //添加单位
                            positionItemVO.setUnit(item.getQuoteCurrency());

                            double currentQty = item.getCurrentQty();
                            double openOrderSellQty = item.getOpenOrderSellQty();

                            // 判断多头空头；大于零多头；小于零空头
                            positionItemVO.setWantIncrease(currentQty >= 0);
                            // 多头、空头
                            positionItemVO.setType(currentQty >= 0 ? INCREASE : DECREASE);

                            // 可平仓量
                            double sell;
                            if (positionItemVO.isWantIncrease()) {
                                sell = currentQty - openOrderSellQty;
                            } else {
                                sell = currentQty + openOrderSellQty;
                            }
                            positionItemVO.setSell((int) Math.abs(sell) + "");

                            // ins id
                            positionItemVO.setInsId(item.getSymbol());
                            // 标题 "BTC"
                            positionItemVO.setTitleName(item.getUnderlying());
                            // 类别 "BTC"
                            positionItemVO.setTypeName(item.getUnderlying());
                            // 描述 "181228"
                            positionItemVO.setNameDes(DateUtils.forBitMexTime(item.getTimestamp()));
                            // 收益
                            double income = Math.abs(item.getForeignNotional()) * item.getLeverage() * item.getUnrealisedPnlPcnt();
                            positionItemVO.setIncome(DoubleUtils.formatFourDecimalString(income));
                            // 强平价格
                            positionItemVO.setSellForce(item.getLiquidationPrice());
                            // 杠杆
                            positionItemVO.setLeverage(item.getLeverage() + "");

                            // 现在价格
                            positionItemVO.setCurPrice(item.getLastPrice());

                            // 收益率
                            double unrealisedPnlPcnt = item.getUnrealisedPnlPcnt();
                            double incomeRate = unrealisedPnlPcnt * item.getLeverage() * 100;
                            positionItemVO.setIncomeRate(DoubleUtils.formatTwoDecimalString(incomeRate) + "%");

                            // 开仓价格
                            positionItemVO.setBuyPrice("$" + item.getAvgCostPrice());
                            // 持仓量
                            positionItemVO.setPosition(Math.abs((int) item.getCurrentQty()) + "");
                            // 保证金
                            positionItemVO.setSecurity(DoubleUtils.formatFourDecimalString(item.getMaintMargin() / ONE_HUNDRED_MILLION));
                            // 增长
                            positionItemVO.setIncrease(item.getUnrealisedPnlPcnt() >= 0);

                            // 设置可增加保证金
                            positionItemVO.setCanIncreaseSecurity(
                                    DoubleUtils.formatDecimalFloor(userMarginRes.getAvailableMargin() / ONE_HUNDRED_MILLION, 4));

                            // 设置可减少保证金
                            String canDecSecurity = DoubleUtils
                                    .formatDecimalFloor(
                                            (item.getMaintMargin() - item.getPosMaint() - item.getPosInit()) / ONE_HUNDRED_MILLION, 4);
                            positionItemVO.setCanDecreaseSecurity(canDecSecurity);

                            contentList.add(positionItemVO);

                        }

                        return result;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObservableSubscriber<List<List>>(PositionContentPresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        Log.i(TAG, "onError: [code:" + code + "; message:" + message + "]");
                        mView.onGetFuturesPositionError();
                    }

                    @Override
                    protected void onSuccessRes(List<List> value) {
                        if (value.size() != 2) {
                            mView.onGetFuturesPositionError();
                            return;
                        }

                        List<String> typeList = (List<String>) value.get(0);
                        List<PositionItemVO> positionItemVOList = (List<PositionItemVO>) value.get(1);

                        mView.onGetType(typeList);
                        mView.onGetFuturesPosition(positionItemVOList);
                    }
                });
    }

    /**
     * 合约持仓信息
     */
    public void getFuturesPosition(final List<FuturesInstrumentsRes> futuresInstrumentsRes,
                                   final List<FuturesInstrumentsTickerList> tickerLists) {

        RetrofitFactory
                .getOkExApiService()
                .getFuturesPositionRes()
                .subscribeOn(Schedulers.io())
                .map(new Function<FuturesPositionRes, List<List>>() {
                    @Override
                    public List<List> apply(FuturesPositionRes futuresPositionRes) throws Exception {

                        if (!futuresPositionRes.getResult()) {
                            throw new RuntimeException("request failure.");
                        }

                        List<String> typeList = new ArrayList<>();
                        List<PositionItemVO> contentList = new ArrayList<>();

                        List<List> result = new ArrayList<>();
                        result.add(typeList);
                        result.add(contentList);

                        // 如果没有内容
                        if (futuresPositionRes.getHolding().size() <= 0) {
                            return result;
                        }

                        for (List<FuturesPositionRes.Holding> holding : futuresPositionRes.getHolding()) {
                            for (FuturesPositionRes.Holding item : holding) {

                                if (!item.getLong_qty().equals("0")) {
                                    createTheOkExPositionData(item, contentList,
                                            typeList, futuresInstrumentsRes,
                                            tickerLists, true);
                                }

                                if (!item.getShort_qty().equals("0")) {
                                    createTheOkExPositionData(item, contentList,
                                            typeList, futuresInstrumentsRes,
                                            tickerLists, false);
                                }

                            }
                        }


                        return result;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObservableSubscriber<List<List>>(PositionContentPresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        Log.i(TAG, "onError: [code:" + code + "; message:" + message + "]");
                        mView.onGetFuturesPositionError();
                    }

                    // 规定第一个为type；第二个为list
                    @Override
                    protected void onSuccessRes(List<List> list) {

                        if (list.size() != 2) {
                            mView.onGetFuturesPositionError();
                            return;
                        }

                        List<String> typeList = (List<String>) list.get(0);
                        List<PositionItemVO> positionItemVOList = (List<PositionItemVO>) list.get(1);

                        mView.onGetType(typeList);
                        mView.onGetFuturesPosition(positionItemVOList);
                    }
                });

    }


    private void createTheOkExPositionData(FuturesPositionRes.Holding item,
                                           List<PositionItemVO> contentList,
                                           List<String> typeList,
                                           List<FuturesInstrumentsRes> futuresInstrumentsRes,
                                           List<FuturesInstrumentsTickerList> tickerLists,
                                           boolean isWantIncrease) {
        // 是否为全仓
        boolean isCrossed = item.getMargin_mode().equals(FuturesPositionRes.CROSSED);
        PositionItemVO positionItemVO = new PositionItemVO(AppUtils.OKEX);

        String rootName = IconInfoUtils.getRootName(item.getInstrument_id());
        // 添加至选择列表
        if (!typeList.contains(rootName)) {
            typeList.add(rootName);
        }

        // true:看多; false:看空
        positionItemVO.setWantIncrease(isWantIncrease);

        // ins id
        positionItemVO.setInsId(item.getInstrument_id());
        // 标题 "BTC"
        positionItemVO.setTitleName(rootName);
        // 类别 "BTC"
        positionItemVO.setTypeName(rootName);
        // 描述 "181228"
        positionItemVO.setNameDes("" + IconInfoUtils.getDayTime(item.getInstrument_id()));
        // 收益
        positionItemVO.setIncome(item.getRealised_pnl());

        // 现在价格
        double curPrice = 1;
        for (FuturesInstrumentsTickerList tickerItem : tickerLists) {
            if (tickerItem.getInstrument_id().equals(item.getInstrument_id())) {
                curPrice = tickerItem.getLast();
                break;
            }
        }
        positionItemVO.setCurPrice(curPrice + "");

        // 按类型进行添加数据
        if (isCrossed) {
            assemblyCrossedData(item, positionItemVO);
        } else {
            assemblyFixedData(item, positionItemVO);
        }

        // 合约面值（美金）
        int contractVal = 0;
        for (FuturesInstrumentsRes futureItem : futuresInstrumentsRes) {
            if (futureItem.getInstrument_id().equals(item.getInstrument_id())) {
                contractVal = futureItem.getContract_val();
                break;
            }
        }

        // 开仓价
        double buyPrice = 1;
        // 张数
        int qty = 0;
        // 杠杆倍数
        int leverage = 1;

        if (isWantIncrease) {
            // 多头、空头
            positionItemVO.setType(INCREASE);
            // 开仓价格
            positionItemVO.setBuyPrice("$" + item.getLong_avg_cost());
            // 可平仓量
            positionItemVO.setSell(item.getLong_avail_qty());
            // 持仓量
            positionItemVO.setPosition(item.getLong_qty());

            try {
                buyPrice = Double.parseDouble(item.getLong_avg_cost());
                qty = Integer.parseInt(item.getLong_qty());
                leverage = Integer.parseInt(positionItemVO.getLeverage());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            // 多头、空头
            positionItemVO.setType(DECREASE);
            // 开仓价格
            positionItemVO.setBuyPrice("$" + item.getShort_avg_cost());
            // 可平仓量
            positionItemVO.setSell(item.getShort_avail_qty());
            // 持仓量
            positionItemVO.setPosition(item.getShort_qty());

            try {
                buyPrice = Double.parseDouble(item.getShort_avg_cost());
                qty = Integer.parseInt(item.getShort_qty());
                leverage = Integer.parseInt(positionItemVO.getLeverage());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        // 持仓量
//        double curPriceFuture = contractVal / curPrice;
//        positionItemVO.setPosition("" + (int) (curPriceFuture * qty));

        // 保证金
        double security = contractVal / buyPrice * qty / leverage;
        positionItemVO.setSecurity("" + security);

        // 计算 全仓的收益率
        if (isCrossed) {
            String incomeRate = "0.00";
            try {
                double realPnl = Double.parseDouble(item.getRealised_pnl());
                double ratio = realPnl / security;
                incomeRate = DoubleUtils.formatTwoDecimalString(ratio * 100);
                if (ratio >= 0) {
                    incomeRate = "+" + incomeRate;
                    positionItemVO.setIncrease(true);
                } else {
                    positionItemVO.setIncrease(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            positionItemVO.setIncomeRate(incomeRate + "%");
        }

        contentList.add(positionItemVO);
    }


    /**
     * 组装逐仓数据
     *
     * @param item
     */
    private void assemblyFixedData(FuturesPositionRes.Holding item, PositionItemVO positionItemVO) {
        if (positionItemVO.isWantIncrease()) {
            // 强平价格
            positionItemVO.setSellForce(item.getLong_liqui_price());
            // 杠杆
            positionItemVO.setLeverage(item.getLong_leverage());

            // 收益率
            String incomeRate = "0.00";
            try {
                double ratio = Double.parseDouble(item.getLong_pnl_ratio());

                incomeRate = DoubleUtils.formatTwoDecimalString(ratio * 100);
                if (ratio >= 0) {
                    incomeRate = "+" + incomeRate;
                    positionItemVO.setIncrease(true);
                } else {
                    positionItemVO.setIncrease(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            positionItemVO.setIncomeRate(incomeRate + "%");

        } else {
            // 强平价格
            positionItemVO.setSellForce(item.getShort_liqui_price());
            // 杠杆
            positionItemVO.setLeverage(item.getShort_leverage());

            // 收益率
            String incomeRate = "0.00";
            try {
                double ratio = Double.parseDouble(item.getShort_pnl_ratio());

                incomeRate = DoubleUtils.formatTwoDecimalString(ratio * 100);
                if (ratio >= 0) {
                    incomeRate = "+" + incomeRate;
                    positionItemVO.setIncrease(true);
                } else {
                    positionItemVO.setIncrease(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            positionItemVO.setIncomeRate(incomeRate + "%");

        }
    }

    /**
     * 组装全仓数据
     *
     * @param item
     */
    private void assemblyCrossedData(FuturesPositionRes.Holding item, PositionItemVO positionItemVO) {
        // 强平价格
        positionItemVO.setSellForce(item.getLiquidation_price());
        // 杠杆
        positionItemVO.setLeverage(item.getLeverage());
    }

    public void getFutureInfo() {
        RetrofitFactory
                .getOkExApiService()
                .getFuturesInstruments()
                .subscribeOn(Schedulers.io())
                .doOnNext(new Consumer<List<FuturesInstrumentsRes>>() {
                    @Override
                    public void accept(List<FuturesInstrumentsRes> futuresInstrumentsRes) throws Exception {
                        getOkExRankInfoList(futuresInstrumentsRes);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObservableSubscriber<List<FuturesInstrumentsRes>>(PositionContentPresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        mView.onGetFuturesPositionError();
                    }

                    @Override
                    protected void onSuccessRes(List<FuturesInstrumentsRes> value) {

                    }
                });
    }

    /**
     * 获取 OkEx 排行榜的信息
     */
    private void getOkExRankInfoList(final List<FuturesInstrumentsRes> futuresInstrumentsRes) {

        RetrofitFactory
                .getOkExApiService()
                .getFutInsTicker()
                .subscribeOn(Schedulers.io())
                .doOnNext(new Consumer<List<FuturesInstrumentsTickerList>>() {
                    @Override
                    public void accept(List<FuturesInstrumentsTickerList> tickerLists) throws Exception {
                        getFuturesPosition(futuresInstrumentsRes, tickerLists);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObservableSubscriber<List<FuturesInstrumentsTickerList>>(PositionContentPresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        Log.e(TAG, "onError: [code:" + code + "; message" + message + "]");
                        mView.onGetFuturesPositionError();
                    }

                    @Override
                    protected void onSuccessRes(List<FuturesInstrumentsTickerList> list) {

                    }
                });
    }

    public void postSell(String insId,
                         String type,
                         String price,
                         String size,
                         String matchPrice,
                         String leverage,
                         final int position) {

        FuturesOrderReq req = new FuturesOrderReq(leverage, matchPrice, size, price, type, insId);
        RetrofitFactory
                .getOkExApiService()
                .futuresOrder(req)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSingleSubscriber<FuturesOrderRes>(PositionContentPresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        Log.e(TAG, "onError: [code:" + code + "; message" + message + "]");
                        mView.onPostSellError(message);
                    }

                    @Override
                    protected void onSuccessRes(FuturesOrderRes value) {
                        if (value.getResult()) {
                            mView.onPostSellSuc(position);
                        } else {
                            mView.onPostSellError(value.getError_messsage());
                        }
                    }
                });

    }

    /**
     * bitmex 平仓
     *
     * @param symbol
     * @param orderQty
     * @param price
     * @param ordType
     * @param side
     * @param position
     */
    public void postBitMEXSell(String symbol,
                               String orderQty,
                               String price,
                               String ordType,
                               String side,
                               final int position) {

        OrderReq req = new OrderReq(symbol, orderQty, price, ordType, side);
        RetrofitFactory
                .getBitMexApiService()
                .postOrder(req)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSingleSubscriber<OrderRes>(PositionContentPresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        mView.onPostSellError(message);
                    }

                    @Override
                    protected void onSuccessRes(OrderRes value) {
                        mView.onPostSellSuc(position);
                    }
                });

    }

    /**
     * 提交保证金
     */
    public void postAdjustSecurity(String symbol, String security) {

        double secResult;
        try {
            double sec = Double.parseDouble(security);
            secResult = sec * ONE_HUNDRED_MILLION;
        } catch (Exception e) {
            mView.onAdjustSecError("保证金异常，请重新输入");
            return;
        }

        RetrofitFactory
                .getBitMexApiService()
                .postTransferMargin(new TransferMarginReq(symbol, secResult + ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSingleSubscriber<TransferMarginRes>(PositionContentPresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        mView.onAdjustSecError(message);
                    }

                    @Override
                    protected void onSuccessRes(TransferMarginRes value) {
                        mView.onAdjustSec();
                    }
                });
    }

}

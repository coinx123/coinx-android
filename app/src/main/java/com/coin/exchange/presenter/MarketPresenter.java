package com.coin.exchange.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.coin.exchange.model.bitMex.response.InstrumentItemRes;
import com.coin.exchange.model.okex.response.FuturesInstrumentsTickerList;
import com.coin.exchange.model.okex.vo.HotCoinItemVO;
import com.coin.exchange.model.okex.vo.RankItemVO;
import com.coin.exchange.net.RetrofitFactory;
import com.coin.exchange.utils.DateUtils;
import com.coin.exchange.utils.IconInfoUtils;
import com.coin.exchange.utils.OkExSettlementComparator;
import com.coin.exchange.view.fragment.main.MarketView;
import com.coin.libbase.net.rxjava.RxObservableSubscriber;
import com.coin.libbase.presenter.BasePresenter;
import com.coin.libbase.utils.DoubleUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/15
 * @description
 */
public class MarketPresenter extends BasePresenter<MarketView> {

    private static final String XBTUSD = "XBTUSD";
    private static final String XBT = "XBT";
    private static final String XBT_NAME = "XBT/USD";

    private static final String ETHUSD = "ETHUSD";
    private static final String ETH = "ETH";
    private static final String ETH_NAME = "ETH/USD";

    private static final String BCHZ18 = "BCHZ18";
    private static final String BCH = "BCH";
    private static final String BCH_NAME = "BCH/Z18";
    private static final String BITMEX_XBT = "xbt";
    private static final String BITMEX_USD = "usd";

    private static final String[] ONE = {"当周"};
    private static final String[] TWO = {"当周", "当季"};
    private static final String[] THREE = {"当周", "次周", "当季"};

    // 1万
    private static final long TEN_THOUSAND = 10000;

    private static final int MAX_SIZE = 10;

    @Inject
    public MarketPresenter(MarketView mView) {
        super(mView);
    }

    /**
     * 获取热门的币种信息列表
     */
    public void getHotCoinInfoList() {
        RetrofitFactory
                .getBitMexApiService()
                .getInstrumentList()
                .subscribeOn(Schedulers.io())
                .map(new Function<List<InstrumentItemRes>, List<HotCoinItemVO>>() {
                    @Override
                    public List<HotCoinItemVO> apply(List<InstrumentItemRes> instrumentItemList) throws Exception {
                        List<HotCoinItemVO> list = new ArrayList<>();

                        boolean isAddBCH = false;

                        for (InstrumentItemRes item : instrumentItemList) {
                            if (item.getRootSymbol().equals(XBT) && item.getSymbol().equals(XBTUSD)) {
                                setData(list, item, XBT_NAME);
                            } else if (item.getRootSymbol().equals(ETH) && item.getSymbol().equals(ETHUSD)) {
                                setData(list, item, ETH_NAME);
                            } else if (item.getRootSymbol().equals(BCH) && !isAddBCH) {
                                setData(list, item, BCH_NAME);
                                isAddBCH = true;
                            }
                        }

                        return list;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObservableSubscriber<List<HotCoinItemVO>>(MarketPresenter.this) {

                    @Override
                    protected void onError(int code, String message) {
                        Log.i(TAG, "onError: [code:" + code + "; message:" + message + "]");
                        mView.onGetHotCoinInfoListError();
                    }

                    @Override
                    protected void onSuccessRes(List<HotCoinItemVO> list) {
                        mView.onGetHotCoinInfoList(list);
                    }
                });
    }

    private void setData(List<HotCoinItemVO> list, InstrumentItemRes item, String name) {
        HotCoinItemVO itemVO = new HotCoinItemVO();
        itemVO.setName(name);
        itemVO.setValue(item.getLastPrice());
        itemVO.setIncrease(item.getLastChangePcnt() >= 0);
        itemVO.setRange(DoubleUtils.formatTwoDecimalString(item.getLastChangePcnt() * 100));
        itemVO.setId(item.getSymbol());
        itemVO.setDesName(DateUtils.forBitMexTime(item.getExpiry()));
        list.add(itemVO);
    }

    /**
     * 获取 OkEx 排行榜的信息
     */
    public void getOkExRankInfoList() {

        RetrofitFactory
                .getOkExApiService()
                .getFutInsTicker()
                .subscribeOn(Schedulers.io())
                .map(new Function<List<FuturesInstrumentsTickerList>, List<RankItemVO>>() {
                    @Override
                    public List<RankItemVO> apply(List<FuturesInstrumentsTickerList> tickerList) throws Exception {

                        OkExSettlementComparator comparator = new OkExSettlementComparator();

                        // 归组
                        Map<String, List<FuturesInstrumentsTickerList>> map = new HashMap<>();
                        for (FuturesInstrumentsTickerList insItem : tickerList) {
                            String rootName = IconInfoUtils.getRootName(insItem.getInstrument_id());
                            List<FuturesInstrumentsTickerList> list;
                            if (map.containsKey(rootName)) {
                                list = map.get(rootName);
                            } else {
                                list = new ArrayList<>(3);
                                map.put(rootName, list);
                            }

                            insItem.setDayTime(IconInfoUtils.getDayTime(insItem.getInstrument_id()));
                            list.add(insItem);
                        }

                        List<FuturesInstrumentsTickerList> resultList = new ArrayList<>(24);
                        for (Map.Entry<String, List<FuturesInstrumentsTickerList>> entry : map.entrySet()) {
                            List<FuturesInstrumentsTickerList> list = entry.getValue();
                            // 进行每项排序
                            Collections.sort(entry.getValue(), comparator);

                            // 进行赋值
                            String[] useDisArray;
                            int listSize = list.size();
                            switch (listSize) {
                                case 1:
                                    useDisArray = ONE;
                                    break;
                                case 2:
                                    useDisArray = TWO;
                                    break;
                                case 3:
                                    useDisArray = THREE;
                                    break;
                                default:
                                    useDisArray = THREE;
                                    break;
                            }
                            for (int i = 0; i < listSize; ++i) {
                                FuturesInstrumentsTickerList curItem = list.get(i);
                                String nameDes = IconInfoUtils.getDesName(curItem.getInstrument_id());
                                nameDes = TextUtils.isEmpty(nameDes) ? "" : "-" + nameDes;
                                if (i < 3) {
                                    curItem.setDisplayName(useDisArray[i] + nameDes);
                                } else {
                                    curItem.setDisplayName(useDisArray[2] + nameDes);
                                }
                            }

                            resultList.addAll(list);
                        }

                        // 进行数据转换
                        List<RankItemVO> rankItemVOList = new ArrayList<>();
                        for (int i = 0; i < resultList.size(); ++i) {
                            FuturesInstrumentsTickerList item = resultList.get(i);

                            String instrumentId = item.getInstrument_id();

                            RankItemVO rankItemVO = new RankItemVO();
                            // 排行榜 名次
                            rankItemVO.setNum(i + 1);
                            // 名称
                            rankItemVO.setName(IconInfoUtils.getRootName(instrumentId));
                            // 当季、当周、次周
                            rankItemVO.setNameDes(item.getDisplayName());
                            // 最新价
                            rankItemVO.setValue(item.getLast() + "");
                            // 交易量
                            rankItemVO.setVolume(getFormatVolume(item.getVolume_24h()));
                            // 设置当前市价
//                            rankItemVO.setCurLast(item.getLast());

                            rankItemVO.setInsId(instrumentId);

                            double mean = (item.getHigh_24h() + item.getLow_24h()) / 2;
                            double range = (item.getLast() - mean) / mean;

                            rankItemVO.setRange(DoubleUtils.formatTwoDecimal(range * 100));
                            rankItemVO.setIncrease(range >= 0);

                            rankItemVOList.add(rankItemVO);

                        }

                        // 将结果排序
                        Collections.sort(rankItemVOList);

                        if (rankItemVOList.size() > MAX_SIZE) {
                            rankItemVOList = rankItemVOList.subList(0, MAX_SIZE);
                        }
                        for (int j = 0; j < rankItemVOList.size(); ++j) {
                            rankItemVOList.get(j).setNum(j + 1);
                        }

                        return rankItemVOList;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObservableSubscriber<List<RankItemVO>>(MarketPresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        Log.e(TAG, "onError: [code:" + code + "; message" + message + "]");
                        mView.onGetOkExError();
                    }

                    @Override
                    protected void onSuccessRes(List<RankItemVO> rankItemVOList) {
                        mView.onGetOkExRankListInfo(rankItemVOList);
                    }
                });
    }

    public void getBitMexRankInfoList() {

        RetrofitFactory.getBitMexApiService()
                .getInstrumentList()
                .subscribeOn(Schedulers.io())
                .map(new Function<List<InstrumentItemRes>, List<RankItemVO>>() {
                    @Override
                    public List<RankItemVO> apply(List<InstrumentItemRes> instrumentItemRes) throws Exception {
                        List<RankItemVO> result = new ArrayList<>();

                        Iterator<InstrumentItemRes> iterator = instrumentItemRes.iterator();
                        while (iterator.hasNext()) {
                            InstrumentItemRes item = iterator.next();

                            if (!item.getQuoteCurrency().toLowerCase().equals("xbt")
                                    && !item.getQuoteCurrency().toLowerCase().equals("usd")) {
                                iterator.remove();
                            }
                        }

                        Collections.sort(instrumentItemRes);

                        int size = instrumentItemRes.size() > 10 ? 10 : instrumentItemRes.size();
                        for (int i = 0; i < size; i++) {
                            RankItemVO rankItemVO = new RankItemVO();
                            InstrumentItemRes item = instrumentItemRes.get(i);

                            // 排行
                            rankItemVO.setNum(i + 1);
                            // 涨跌
                            rankItemVO.setIncrease(item.getLastChangePcnt() >= 0);
                            // 涨跌幅
                            rankItemVO.setRange(getRangeForBitMex(item.getLastChangePcnt()));
                            // 最新价
                            rankItemVO.setValue(getNotFixedDecimalForBitMex(item.getLastPrice()));
                            // 最近24小时成交量
                            rankItemVO.setVolume(getNotFixedDecimalForBitMex(item.getForeignNotional24h()));
                            // 设置名称
                            rankItemVO.setName(item.getRootSymbol());
                            // 唯一id
                            rankItemVO.setInsId(item.getSymbol());
                            // 指数
                            rankItemVO.setIndex(getNotFixedDecimalForBitMex(item.getIndicativeSettlePrice()));
                            // 描述
                            rankItemVO.setNameDes(DateUtils.forBitMexTime(item.getExpiry()));

                            result.add(rankItemVO);
                        }

                        return result;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObservableSubscriber<List<RankItemVO>>(MarketPresenter.this) {

                    @Override
                    protected void onError(int code, String message) {
                        mView.onGetBitMexError();
                    }

                    @Override
                    protected void onSuccessRes(List<RankItemVO> value) {
                        mView.onGetBitMexRankListInfo(value);
                    }

                });
    }

    /**
     * 格式化，过万时返回：xxxx万；未过万时返回：xxx
     *
     * @param volume
     * @return
     */
    private String getFormatVolume(long volume) {
        // 大于一万，则用万做单位
        if (volume > TEN_THOUSAND) {
            return (volume / TEN_THOUSAND) + "万";
        }
        return volume + "";
    }

    /**
     * 处理bitmex的最新价
     *
     * @param lastPrice
     * @return
     */
    private String getNotFixedDecimalForBitMex(double lastPrice) {
        return DoubleUtils.formatSixDecimalString(lastPrice);
    }

    /**
     * 处理bitmex的涨跌幅
     *
     * @param range
     * @return
     */
    private double getRangeForBitMex(double range) {
        return DoubleUtils.formatTwoDecimal(range * 100);
    }

}

package com.coin.exchange.mvp.KLine;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coin.exchange.R;
import com.coin.exchange.adapter.KlineTradeAdapter;
import com.coin.exchange.cache.PreferenceManager;
import com.coin.exchange.config.FragmentConfig;
import com.coin.exchange.config.okEx.ChannelHelper;
import com.coin.exchange.context.AppApplication;
import com.coin.exchange.database.CollectionItem;
import com.coin.exchange.database.CollectionModel;
import com.coin.exchange.model.bitMex.response.InstrumentItemRes;
import com.coin.exchange.model.okex.response.FuturesInstrumentsTickerList;
import com.coin.exchange.model.okex.response.FuturesInstrumentsTradesList;
import com.coin.exchange.model.okex.vo.MenuItemVO;
import com.coin.exchange.mvp.KLine.di.DaggerKLineComponent;
import com.coin.exchange.mvp.KLine.di.KLineModule;
import com.coin.exchange.utils.AppUtils;
import com.coin.exchange.utils.GsonUtils;
import com.coin.exchange.utils.IndicatorLineUtil;
import com.coin.exchange.utils.NumberUtil;
import com.coin.exchange.view.PopWindow.MoreIndexPopWindow;
import com.coin.exchange.view.PopWindow.MoreTimePopWindow;
import com.coin.exchange.view.TradeActivity;
import com.coin.exchange.webSocket.bitMex.BitMEXWebSocketManager;
import com.coin.exchange.webSocket.okEx.okExFuture.FutureWebSocketManager;
import com.coin.exchange.widget.KLineTabItem;
import com.google.gson.reflect.TypeToken;
import com.coin.libbase.model.CommonRes;
import com.coin.libbase.model.eventbus.Event;
import com.coin.libbase.utils.ToastUtil;
import com.coin.libbase.view.activity.JBaseActivity;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;


public final class KLineActivity extends JBaseActivity<KLinePresenter> implements KLineView {

    private DecimalFormat df = new DecimalFormat("######0.00");
    private DecimalFormat df4 = new DecimalFormat("######0.0000");
    @NonNull
    private final List<FuturesInstrumentsTradesList> tradesList = new ArrayList<>();
    @Inject
    PreferenceManager preferenceManager;
    @Inject
    CollectionModel collectionModel;

    @BindView(R.id.kline_tab)
    TabLayout mTab;

    @BindView(R.id.time_show_line)
    View time_show_line;

    @BindView(R.id.kline_view_pager)
    ViewPager mViewPager;
    @BindView(R.id.iv_kline_back)
    ImageView ivKlineBack;
    @BindView(R.id.tv_kline_currency)
    TextView tvKlineCurrency;
    @BindView(R.id.kline_trade_time)
    TextView kline_trade_time;
    @BindView(R.id.iv_kline_collection)
    ImageView ivKlineCollection;
    @BindView(R.id.rl_kline_top)
    RelativeLayout rlKlineTop;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.iv_kline_up_down)
    ImageView ivKlineUpDown;
    @BindView(R.id.tv_percent)
    TextView tvPercent;
    @BindView(R.id.tv_percent_prise)
    TextView tvPercentPrise;
    @BindView(R.id.tv_kline_24high_value)
    TextView tvKline24highValue;
    @BindView(R.id.tv_kline_market)
    TextView tvKlineMarket;
    @BindView(R.id.tv_kline_market_value)
    TextView tvKlineMarketValue;
    @BindView(R.id.tv_kline_24low)
    TextView tvKline24low;
    @BindView(R.id.tv_kline_24number)
    TextView tvKline24number;
    @BindView(R.id.ll_kline_value_show)
    LinearLayout llKlineValueShow;
    @BindView(R.id.time_bottom_show_line)
    View timeBottomShowLine;

    @BindView(R.id.ll_kline_bottom)
    LinearLayout llKlineBottom;
    @BindView(R.id.rv_kline_trade)
    RecyclerView rvOptionalList;
    @BindView(R.id.ll_trade_list_show)
    LinearLayout ll_trade_list_show;

    private KlineTradeAdapter klineTradeAdapter;

    private MoreTimePopWindow moreTimePopWindow;
    private MoreIndexPopWindow moreIndexPopWindow;
    private String instrumentId = "";
    private String insIdCase_3 = "";//合约id前面3位小写
    private String time = "";
    private String from = "";

    @Override
    protected int getLayout() {
        return R.layout.activity_kline;
    }

    @Override
    protected void initIntent(Intent intent) {
        instrumentId = getIntent().getStringExtra(FragmentConfig.INSTRUMENT_ID);
        insIdCase_3 = instrumentId.substring(0, 3).toLowerCase();
        time = getIntent().getStringExtra(FragmentConfig.TYPE);
        from = getIntent().getStringExtra(FragmentConfig.FROM);
    }

    protected void initView() {
        tvKlineCurrency.setText(insIdCase_3.toUpperCase() + " " + time);
        AppUtils.isExitAndDelOrAdd(collectionModel, instrumentId,
                ivKlineCollection, null);

        rvOptionalList.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));

        klineTradeAdapter = new KlineTradeAdapter(this);
        rvOptionalList.setAdapter(klineTradeAdapter);
        rvOptionalList.setHasFixedSize(true);
    }

    protected void initData() {
        initNAV(from);

        if (from.equals(AppUtils.OKEX)) {
            mViewPager.setOffscreenPageLimit(8); // 设置不会回收的数

            mPresenter.getFuturesInfo(instrumentId);

            mPresenter.getFuturesInstrumentsTrades(instrumentId, "1", null, "10");
        } else {
            mViewPager.setOffscreenPageLimit(4);

            mPresenter.getBitmexInstrument(instrumentId);

            mPresenter.getBitmexTradeList(instrumentId);
        }
    }

    private void initNAV(final String from) {
        ArrayList<MenuItemVO> kLineNAV = null;
        if (from.equals(AppUtils.OKEX)) {
            kLineNAV = FragmentConfig.getKLineNAV();
        } else {
            kLineNAV = FragmentConfig.getBitmexKLineNAV();
        }
        mViewPager.setAdapter(new KLInePagerAdapter(getSupportFragmentManager()));

        for (int i = 0; i < kLineNAV.size(); i++) { //mViewPager 和 tab没有绑定必须要创建
            mTab.addTab(mTab.newTab().setText("Tab" + i));
        }

        //设置自定义视图(curIndex默认选定为第0个)
        int curIndex = 0;
        for (int i = 0; i < mTab.getTabCount(); i++) {
            TabLayout.Tab tab = mTab.getTabAt(i);
            KLineTabItem tabItem = new KLineTabItem(this)
                    .initData(kLineNAV.get(i).getName(), kLineNAV.get(i).getResId(),
                            kLineNAV.get(i).getId() == curIndex);

            if (tab != null) {
                tab.setCustomView(tabItem);
                if (curIndex == i) {
                    tab.select();
                }
            }
        }

        mTab.post(new Runnable() { //设置指示器长度
            @Override
            public void run() {
                IndicatorLineUtil.setIndicator(mTab, 10, 10);
            }
        });

        mTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(final TabLayout.Tab tab) {
                if (tab.getPosition() == 4 && from.equals(AppUtils.BITMEX)) {
                    if (moreIndexPopWindow == null) {
                        moreIndexPopWindow = new MoreIndexPopWindow(KLineActivity.this);
                        moreIndexPopWindow.setListener(
                                new MoreIndexPopWindow.ActionListener() {
                                    @Override
                                    public void onViewClick(int id) { //没有用到可以全部不要
                                    }
                                }
                        );
                    }
                    moreIndexPopWindow.show(time_show_line);
                } else if (tab.getPosition() == 5) {
                    if (moreTimePopWindow == null) {
                        moreTimePopWindow = new MoreTimePopWindow(KLineActivity.this);
                        moreTimePopWindow.setListener(new MoreTimePopWindow.ActionListener() {
                            @Override
                            public void fifteenMinuteClick() {
                                mViewPager.setCurrentItem(5);
                            }

                            @Override
                            public void thirtyMinuteClick() {
                                mViewPager.setCurrentItem(6);
                            }

                            @Override
                            public void oneWeekClick() {
                                mViewPager.setCurrentItem(7);
                            }
                        });
                    }
                    moreTimePopWindow.show(time_show_line);
                } else if (tab.getPosition() == 6) {
                    if (moreIndexPopWindow == null) {
                        moreIndexPopWindow = new MoreIndexPopWindow(KLineActivity.this);
                        moreIndexPopWindow.setListener(
                                new MoreIndexPopWindow.ActionListener() {
                                    @Override
                                    public void onViewClick(int id) { //没有用到可以全部不要
                                    }
                                }
                        );
                    }
                    moreIndexPopWindow.show(time_show_line);
                } else {
                    mViewPager.setCurrentItem(tab.getPosition());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 4 && from.equals(AppUtils.BITMEX)) {
                    moreIndexPopWindow.show(time_show_line);
                } else if (tab.getPosition() == 5) {
                    moreTimePopWindow.show(time_show_line);
                } else if (tab.getPosition() == 6) {
                    moreIndexPopWindow.show(time_show_line);
                }
            }
        });
    }

    @Override
    protected void registerDagger() {
        DaggerKLineComponent.builder()
                .appComponent(AppApplication.getAppComponent())
                .kLineModule(new KLineModule(this))
                .build()
                .inject(this);
    }

    @OnClick({R.id.iv_kline_zoom, R.id.iv_kline_back, R.id.iv_kline_collection,
            R.id.tv_kline_buy, R.id.tv_kline_sell})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_kline_zoom:
                boolean isVertical = (getResources().getConfiguration().orientation
                        == Configuration.ORIENTATION_PORTRAIT);
                if (isVertical) {
                    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {
                    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                }
                break;
            case R.id.iv_kline_collection:
                CollectionItem collectionItem = new CollectionItem(instrumentId, time);
                AppUtils.isExitAndDelOrAdd(collectionModel, instrumentId,
                        ivKlineCollection, collectionItem);
                break;
            case R.id.tv_kline_buy:
                ToTradeActivity();
                break;
            case R.id.tv_kline_sell:
                ToTradeActivity();
                break;
            case R.id.iv_kline_back:
                if (this.getResources().getConfiguration().orientation
                        == Configuration.ORIENTATION_LANDSCAPE) {
                    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                } else if (this.getResources().getConfiguration().orientation
                        == Configuration.ORIENTATION_PORTRAIT) {
                    finish();
                }
                break;
        }
    }

    private void ToTradeActivity() {
        EventBus.getDefault().postSticky(new Event.SendInstrument(time, instrumentId, from));
        Intent intent = new Intent(this, TradeActivity.class);
        intent.putExtra(FragmentConfig.INSTRUMENT_ID, instrumentId);
        intent.putExtra(FragmentConfig.TYPE, time);
        intent.putExtra(FragmentConfig.FROM, from);
        startActivity(intent);
    }

    @Override
    public void onGetFuturesInfo(FuturesInstrumentsTickerList futuresInfo) {
        tvPrice.setText("$" + df.format(futuresInfo.getLast()));
        tvKline24highValue.setText(df.format(futuresInfo.getHigh_24h()));
        tvKline24number.setText(futuresInfo.getVolume_24h() + "");
        tvKline24low.setText(df.format(futuresInfo.getLow_24h()));
        mPresenter.getCandles(instrumentId, futuresInfo.getLast());
    }

    @Override
    public void onGetFuturesInfoError(String msg) {
        ToastUtil.showCenter(msg);
    }

    @Override
    public void onGetCandles(List<List<Double>> lists, double prise) {
        double spread = prise - lists.get(0).get(1);
        double p = ((spread) / lists.get(0).get(1)) * 100;
        setPriceValue(p, spread);
    }

    private void setPriceValue(double p, double spread) {
        if (p < 0) {
            tvPrice.setTextColor(AppUtils.getDecreaseColor());
            ivKlineUpDown.setBackgroundResource(AppUtils.getDownBg());
            tvPercent.setBackground(AppUtils.getDecreaseBg());
            tvPercent.setText("" + df.format(p) + "%");
            tvPercentPrise.setText("" + df.format(spread));
        } else {
            tvPrice.setTextColor(AppUtils.getIncreaseColor());
            ivKlineUpDown.setBackgroundResource(AppUtils.getUpBg());
            tvPercent.setBackground(AppUtils.getIncreaseBg());
            tvPercent.setText("+" + df.format(p) + "%");
            tvPercentPrise.setText("+" + df.format(spread));
        }
    }

    @Override
    public void onGetCandlesError(String msg) {
        ToastUtil.showCenter(msg);
    }

    @Override
    public void onGetFuturesInstrumentsTrades(List<FuturesInstrumentsTradesList> tradesLists) {
        tradesList.clear();
        tradesList.addAll(tradesLists);
        Collections.reverse(tradesList);
        klineTradeAdapter.updateItems(tradesList);
        //发送订阅成交信息，要在的destroy 判断是否应该取消订阅
        FutureWebSocketManager.getInstance().subscribeTrade(insIdCase_3, ChannelHelper.getTime(time));
    }

    @Override
    public void onGetFuturesInstrumentsTradesError(String msg) {
        ToastUtil.showCenter(msg);
    }

    @Override
    public void onGetBitmexInstrument(List<InstrumentItemRes> instrumentItemResList) {
        InstrumentItemRes res = instrumentItemResList.get(0);
        DecimalFormat d = df;
        if (res.getQuoteCurrency().contains("USD")) {
            tvPrice.setText("$" + df.format(res.getLastPrice()));
        } else {
            kline_trade_time.setText(R.string.quantity_btc);
            tvPrice.setText("฿" + BigDecimal.valueOf(res.getLastPrice()));
            d = df4;
        }
        tvKline24highValue.setText(d.format(res.getHighPrice()));
        tvKline24number.setText(d.format(res.getForeignNotional24h() / 10000) + "万");
        tvKline24low.setText(d.format(res.getLowPrice()));
        tvKlineMarket.setVisibility(View.VISIBLE);
        tvKlineMarketValue.setVisibility(View.VISIBLE);
        tvKlineMarketValue.setText(d.format(res.getTotalVolume() / 100000000) + "亿");

        double p = res.getLastChangePcnt() * 100;
        setPriceValue(p, res.getLastPrice() * res.getLastChangePcnt());
    }

    @Override
    public void onGetBitmexInstrumentError(String msg) {
        ToastUtil.showCenter(msg);
    }

    @Override
    public void onGetBitmexTradeList(List<FuturesInstrumentsTradesList> tradesLists) {
        tradesList.addAll(tradesLists);
        klineTradeAdapter.updateItems(tradesList);
        BitMEXWebSocketManager.getInstance().subscribeTradeList(instrumentId);
    }

    @Override
    public void onGetBitmexTradeListError(String msg) {
        ToastUtil.showCenter(msg);
    }

    public class KLInePagerAdapter extends FragmentPagerAdapter {
        KLInePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (from.equals(AppUtils.OKEX)) {
                return FragmentConfig.getKLineFragment(position, instrumentId, from);
            } else {
                return FragmentConfig.getBitmexKLineFragment(position, instrumentId, from);
            }
        }

        @Override
        public int getCount() {
            if (from.equals(AppUtils.OKEX)) {
                return 8;
            } else {
                return 4;
            }
        }
    }

    @Override
    protected void onMessage(List list) {
        try {
            List<CommonRes> commonResList = list;
            if (from.equals(AppUtils.OKEX)) {
                String channel = commonResList.get(0).getChannel();
                if (channel.contains("trade")) {  //对trade的推送消息才进行处理
                    String icon = channel.substring(17, 20); //截取币种，如btc
                    if (insIdCase_3.equals(icon)) {
                        String toJson = GsonUtils.getGson().toJson(commonResList.get(0).getData());//object 转String
                        final List<List<String>> tradeList = GsonUtils.getGson().fromJson(toJson,
                                new TypeToken<List<List<String>>>() {
                                }.getType());

                        if (tradeList.size() > 0) {
                            final List<FuturesInstrumentsTradesList> ftList = new ArrayList<>();
                            FuturesInstrumentsTradesList ftRes = new FuturesInstrumentsTradesList();
                            for (int i = 0; i < tradeList.size(); i++) {
                                ftRes.setTrade_id(tradeList.get(i).get(0));
                                ftRes.setPrice(NumberUtil.getDouble(tradeList.get(i).get(1), 0.0));
                                ftRes.setQty(NumberUtil.getInt(tradeList.get(i).get(2), 0, 10));
                                ftRes.setTimestamp(tradeList.get(i).get(3));
                                ftRes.setSide(tradeList.get(i).get(4));
                                if (i < 9) {
                                    ftList.add(ftRes);
                                }
                            }

                            final List<FuturesInstrumentsTradesList> tempftList = new ArrayList<>();
                            for (int i = 0; i < tradesList.size() - ftList.size(); i++) {
                                tempftList.add(tradesList.get(i));
                            }
                            //返回到主线程刷新数据
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tradesList.clear();
                                    tradesList.addAll(ftList);
                                    tradesList.addAll(tempftList);
                                    klineTradeAdapter.updateItems(tradesList);
                                }
                            });
                        }
                    }
                }
            } else {  //bitmex
                String table = commonResList.get(0).getTable();
                Object object = commonResList.get(0).getData();
                if (table == null || object == null) {
                    return;
                }
                String sub = GsonUtils.getInstance().toJson(object);
                if (table.equals("trade")) {
                    List<FuturesInstrumentsTradesList> lists = GsonUtils.getGson().fromJson(sub,
                            new TypeToken<List<FuturesInstrumentsTradesList>>() {
                            }.getType());

                    if (lists.size() > 0) {
                        final List<FuturesInstrumentsTradesList> ftList = new ArrayList<>();
                        for (int i = 0; i < lists.size(); i++) {
                            if (i < 10) {
                                ftList.add(lists.get(i));
                            }
                        }

                        final List<FuturesInstrumentsTradesList> tempftList = new ArrayList<>();
                        for (int i = 0; i < tradesList.size() - lists.size(); i++) {
                            if (i < 10) {
                                tempftList.add(tradesList.get(i));
                            }
                        }

                        //返回到主线程刷新数据
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tradesList.clear();
                                tradesList.addAll(ftList);
                                tradesList.addAll(tempftList);
                                klineTradeAdapter.updateItems(tradesList);

                            }
                        });
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            llKlineValueShow.setVisibility(View.GONE);
            timeBottomShowLine.setVisibility(View.GONE);
            llKlineBottom.setVisibility(View.GONE);
            ll_trade_list_show.setVisibility(View.GONE);
        } else if (this.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT) {
            ll_trade_list_show.setVisibility(View.VISIBLE);
            llKlineValueShow.setVisibility(View.VISIBLE);
            timeBottomShowLine.setVisibility(View.VISIBLE);
            llKlineBottom.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (from.equals(AppUtils.OKEX)) {
            FutureWebSocketManager.getInstance().unsubscribeTrade(insIdCase_3,
                    ChannelHelper.getTime(time));
        } else {
            BitMEXWebSocketManager.getInstance().unsubscribeTradeList(instrumentId);
        }
    }
}

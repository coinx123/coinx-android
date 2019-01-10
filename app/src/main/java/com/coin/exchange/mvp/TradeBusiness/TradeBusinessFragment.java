package com.coin.exchange.mvp.TradeBusiness;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.coin.exchange.R;
import com.coin.exchange.adapter.DelegationAdapter;
import com.coin.exchange.adapter.TradeDepthAdapter;
import com.coin.exchange.cache.PreferenceManager;
import com.coin.exchange.config.FragmentConfig;
import com.coin.exchange.config.okEx.ChannelHelper;
import com.coin.exchange.context.AppApplication;
import com.coin.exchange.model.bitMex.request.OrderReq;
import com.coin.exchange.model.bitMex.response.InstrumentItemRes;
import com.coin.exchange.model.bitMex.response.OrderItemRes;
import com.coin.exchange.model.bitMex.response.PositionItemRes;
import com.coin.exchange.model.bitMex.response.UserMarginRes;
import com.coin.exchange.model.okex.request.FuturesOrderReq;
import com.coin.exchange.model.okex.response.FuturesAccountsCurrencyRes;
import com.coin.exchange.model.okex.response.FuturesCancelOrderRes;
import com.coin.exchange.model.okex.response.FuturesInstrumentsBookRes;
import com.coin.exchange.model.okex.response.FuturesInstrumentsIndexRes;
import com.coin.exchange.model.okex.response.FuturesInstrumentsTickerList;
import com.coin.exchange.model.okex.response.FuturesOrderRes;
import com.coin.exchange.model.okex.response.SingleFuturesPositionRes;
import com.coin.exchange.model.okex.vo.DelegationItemVO;
import com.coin.exchange.model.okex.webSocket.response.DetailRes;
import com.coin.exchange.mvp.KLine.KLineActivity;
import com.coin.exchange.mvp.TradeBusiness.di.DaggerTradeBusinessComponent;
import com.coin.exchange.mvp.TradeBusiness.di.TradeBusinessModule;
import com.coin.exchange.utils.AppUtils;
import com.coin.exchange.utils.GsonUtils;
import com.coin.exchange.utils.NumberUtil;
import com.coin.exchange.utils.PointLengthFilterUtils;
import com.coin.exchange.utils.StringHelper;
import com.coin.exchange.view.fragment.trade.business.LoopViewFragment;
import com.coin.exchange.view.fragment.trade.business.TradeConfirmFragment;
import com.coin.exchange.webSocket.bitMex.BitMEXWebSocketManager;
import com.coin.exchange.webSocket.okEx.okExFuture.FutureWebSocketManager;
import com.coin.libbase.model.CommonRes;
import com.coin.libbase.model.eventbus.Event;
import com.coin.libbase.utils.ToastUtil;
import com.coin.libbase.view.fragment.JBaseFragment;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.Subscribe;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public final class TradeBusinessFragment extends JBaseFragment<TradeBusinessPresenter> implements TradeBusinessView {
    private static final String MARKET = "Market";
    private static final String LIMIT = "Limit";
    public static final String BUY = "Buy";
    public static final String SELL = "Sell";
    private final DecimalFormat df = new DecimalFormat("######0.00");

    @BindView(R.id.tv_trade_optional_show)
    TextView tvTradeOptionalShow;
    @BindView(R.id.tv_trade_time_show)
    TextView tvTradeTimeShow;
    @BindView(R.id.tv_trade_kline_show)
    TextView tvTradeKlineShow;
    @BindView(R.id.tv_trade_prise_show)
    TextView tvTradePriseShow;
    @BindView(R.id.tv_trade_up_down_show)
    TextView tvTradeUpDownShow;
    @BindView(R.id.tv_trade_open)
    TextView tvTradeOpen;
    @BindView(R.id.tv_trade_close)
    TextView tvTradeClose;
    @BindView(R.id.tv_trade_lever_value)
    TextView tvTradeLeverValue;
    @BindView(R.id.tv_clear_prise)
    TextView tvClearPrise;
    @BindView(R.id.et_prise)
    EditText etPrise;
    @BindView(R.id.tv_prise)
    TextView tvPrise;
    @BindView(R.id.tv_market_price)
    TextView tvMarketPrice;
    @BindView(R.id.tv_buy_one_price)
    TextView tvBuyOnePrice;
    @BindView(R.id.tv_sell_one_price)
    TextView tvSellOnePrice;
    @BindView(R.id.tv_number_show)
    TextView tvNumberShow;
    @BindView(R.id.et_number)
    EditText etNumber;
    @BindView(R.id.tv_bullish)
    TextView tvBullish;
    @BindView(R.id.tv_available)
    TextView tvAvailable;
    @BindView(R.id.tv_available_value)
    TextView tvAvailableValue;
    @BindView(R.id.tv_open)
    TextView tvOpen;
    @BindView(R.id.tv_open_value)
    TextView tvOpenValue;
    @BindView(R.id.tv_bearish)
    TextView tvBearish;
    @BindView(R.id.tv_sell_available)
    TextView tvSellAvailable;
    @BindView(R.id.tv_sell_available_value)
    TextView tvSellAvailableValue;
    @BindView(R.id.tv_open_sell)
    TextView tvOpenSell;
    @BindView(R.id.tv_open_sell_value)
    TextView tvOpenSellValue;
    @BindView(R.id.rv_trade_list)
    RecyclerView rvTradeList;
    @BindView(R.id.tv_trade_list_index_value)
    TextView tvTradeListIndexValue;
    @BindView(R.id.rv_delegation_list)
    RecyclerView rvDelegationList;
    @BindView(R.id.tv_trade_list_prise)
    TextView tvTradeListPrise;

    @Inject
    PreferenceManager preferenceManager;
    FuturesInstrumentsBookRes BitdepthRes = null;
    private ActionListener mListener;
    private String instrumentId = "";
    private String insIdCase_3 = ""; //合约id前面3位小写
    private String time = "";
    private String from = "";
    private TradeDepthAdapter tradeDepthAdapter;
    private DelegationAdapter delegationAdapter;

    private boolean isOpen = true;//是否开仓，true 表示开仓。false 表示平仓
    private int lever = 10;//杠杆倍数，默认10倍,0代表Bitmex全仓
    private LoopViewFragment loopViewFragment;
    private TradeConfirmFragment tradeConfirmFragment;
    private String[] PLANETS = new String[]{"10×", "20×"};
    private double buyOnePrise = 0.0;
    private double sellOnePrise = 0.0;
    private double last = 0.0;
    private SingleFuturesPositionRes PositionRes;
    private PositionItemRes positionItemRes;
    private FuturesAccountsCurrencyRes futuresAccountsCurrencyRes;
    private UserMarginRes userMarginRes;

    public static TradeBusinessFragment newInstance() {
        return new TradeBusinessFragment();
    }

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater, ViewGroup container,
                                        Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trade_business, container, false);
    }

    @Override
    protected void initView(View view) {
        if (instrumentId.isEmpty() || time.isEmpty()) {
            return;
        }
        if (from.equals(AppUtils.OKEX)) {
            PLANETS = new String[]{"10×", "20×"};
            tvNumberShow.setText(R.string.quantity_z);
        } else {
            PLANETS = new String[]{"全仓", "1×", "2×", "3×", "4×", "5×",
                    "10×", "15×", "20×", "25×", "50×", "100×"};
            tvNumberShow.setText(R.string.quantity_usd);
        }

        tvTradeOptionalShow.setText("  " + instrumentId.substring(0, 3));
        tvTradeTimeShow.setText(time);
        rvTradeList.setLayerType(View.LAYER_TYPE_NONE, null);
        rvTradeList.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));

        tradeDepthAdapter = new TradeDepthAdapter(getActivity());
        rvTradeList.setAdapter(tradeDepthAdapter);
        rvTradeList.setHasFixedSize(true);
        tradeDepthAdapter.setOnItemClickListener(new TradeDepthAdapter.OnItemClickListen() {
            @Override
            public void onItemClick(double prise) {
                etPrise.setText(BigDecimal.valueOf(prise).toString());
            }
        });

        etPrise.setFilters(new InputFilter[]{new PointLengthFilterUtils(3)});

        etNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                double prise = NumberUtil.getDouble(s.toString(), 0.0);
                if (prise > 0) {
                    tvBullish.setBackgroundResource(R.drawable.future_trade_bg_green_4dp);
                    tvBullish.setClickable(true);
                    tvBearish.setBackgroundResource(R.drawable.future_trade_bg_red_4dp);
                    tvBearish.setClickable(true);
                } else {
                    tvBullish.setBackgroundResource(R.drawable.future_trade_bg_gray_4dp);
                    tvBullish.setClickable(false);
                    tvBearish.setBackgroundResource(R.drawable.future_trade_bg_gray_4dp);
                    tvBearish.setClickable(false);
                }
            }
        });
        tvBullish.setClickable(false);
        tvBearish.setClickable(false);

        loopViewFragment = new LoopViewFragment();
        loopViewFragment.setListener(new LoopViewFragment.ActionListener() {
            @Override
            public void onViewClick(int index) {
                tvTradeLeverValue.setText(PLANETS[index] + " ");
                if (from.equals(AppUtils.OKEX)) {
                    lever = NumberUtil.getInt(PLANETS[index].replace("×", ""), 10, 10);
                    setOpenValueShow(futuresAccountsCurrencyRes);
                } else {
                    if (index == 0) {
                        lever = 0;
                    } else {
                        lever = NumberUtil.getInt(PLANETS[index].replace("×", ""), 0, 10);
                    }
                    mPresenter.postLeverage(instrumentId, lever);
                }
            }
        });
        tradeConfirmFragment = new TradeConfirmFragment();
        if (from.equals(AppUtils.OKEX)) {
            mPresenter.getFuturesInfo(instrumentId); //获取单个ticker信息
            mPresenter.getFuturesPositionInfo(instrumentId);
            mPresenter.getFuturesBook(instrumentId);
            mPresenter.getFuturesIndex(instrumentId);
            mPresenter.getFuturesCurrencyInfo(instrumentId);
        } else {
            mPresenter.getBitmexInstrument(instrumentId);
            mPresenter.getBitmexPositionInfo(instrumentId);
            BitMEXWebSocketManager.getInstance().subscribeOrderBook10(instrumentId);
        }
        getDelegationList(instrumentId, "0", 0, time.substring(0, 2));
    }

    @Override
    protected void registerDagger() {
        DaggerTradeBusinessComponent.builder()
                .appComponent(AppApplication.getAppComponent())
                .tradeBusinessModule(new TradeBusinessModule(this))
                .build()
                .inject(this);
    }

    public void setListener(ActionListener listener) {
        this.mListener = listener;
    }

    public void Update() {
        getDelegationList(instrumentId, "0", 0, time.substring(0, 2));
    }

    @OnClick({R.id.tv_trade_optional_show, R.id.tv_trade_kline_show, R.id.tv_trade_open,
            R.id.tv_trade_close, R.id.rl_trade_lever, R.id.tv_clear_prise, R.id.et_prise,
            R.id.tv_market_price, R.id.tv_buy_one_price, R.id.tv_sell_one_price,
            R.id.tv_clear_number, R.id.et_number, R.id.tv_bullish, R.id.tv_bearish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_trade_optional_show:  //到侧边栏自选
                if (mListener != null) {
                    mListener.onViewClick();
                }
                break;
            case R.id.tv_trade_kline_show:   //到K线图
                Intent intent = new Intent();
                intent.putExtra(FragmentConfig.INSTRUMENT_ID, instrumentId);
                intent.putExtra(FragmentConfig.TYPE, time);
                intent.putExtra(FragmentConfig.FROM, from);
                intent.setClass(getContext(), KLineActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_trade_open: //开仓
                isOpen = true;
                tvTradeOpen.setBackgroundResource(R.color.trade_blue);
                tvTradeOpen.setTextColor(getResources().getColor(R.color.white));
                tvTradeClose.setBackgroundResource(R.drawable.future_trade_bg_cfdeee);
                tvTradeClose.setTextColor(getResources().getColor(R.color.time_blue));
                tvBullish.setText("买入开多(看涨)");
                tvAvailable.setText("可用:");
                tvOpen.setText("可开多:");
                tvBearish.setText("卖出开空(看跌)");
                tvSellAvailable.setText("可用:");
                tvOpenSell.setText("可开空:");
                if (from.equals(AppUtils.OKEX)) {
                    setOpenValueShow(futuresAccountsCurrencyRes);
                } else {
                    setBitmexOpenValueShow();
                }
                break;
            case R.id.tv_trade_close:  //平仓
                isOpen = false;
                tvTradeOpen.setBackgroundResource(R.drawable.future_trade_bg_cfdeee);
                tvTradeOpen.setTextColor(getResources().getColor(R.color.time_blue));
                tvTradeClose.setBackgroundResource(R.color.trade_blue);
                tvTradeClose.setTextColor(getResources().getColor(R.color.white));
                tvBullish.setText("买入平空");
                tvAvailable.setText("可平:");
                tvOpen.setText("持仓:");
                tvBearish.setText("卖出平多");
                tvSellAvailable.setText("可平:");
                tvOpenSell.setText("持仓:");
                if (from.equals(AppUtils.OKEX)) {
                    setSellOpenValueShow();
                } else {
                    setBitmexSellOpenValueShow();
                }
                break;
            case R.id.rl_trade_lever: //显示杠杆对话框
                loopViewFragment.update(Arrays.asList(PLANETS));
                loopViewFragment.show(this);
                break;
            case R.id.tv_clear_prise:
                etPrise.setText("");
                break;
            case R.id.et_prise:
                break;
            case R.id.tv_market_price:
                if (tvMarketPrice.getTag() == null || !(Boolean) tvMarketPrice.getTag()) {
                    setPriseInfo("市场价", false,
                            tvMarketPrice, tvBuyOnePrice, tvSellOnePrice);
                } else {
                    setPriseInfo("", true, tvMarketPrice,
                            tvBuyOnePrice, tvSellOnePrice);
                }
                break;
            case R.id.tv_buy_one_price:
                if (tvBuyOnePrice.getTag() == null || !(Boolean) tvBuyOnePrice.getTag()) {
                    setPriseInfo("买一价", false, tvBuyOnePrice,
                            tvMarketPrice, tvSellOnePrice);
                } else {
                    setPriseInfo("", true, tvBuyOnePrice,
                            tvMarketPrice, tvSellOnePrice);
                }
                break;
            case R.id.tv_sell_one_price:
                if (tvSellOnePrice.getTag() == null || !(Boolean) tvSellOnePrice.getTag()) {
                    setPriseInfo("卖一价", false, tvSellOnePrice,
                            tvMarketPrice, tvBuyOnePrice);
                } else {
                    setPriseInfo("", true, tvSellOnePrice,
                            tvMarketPrice, tvBuyOnePrice);
                }
                break;
            case R.id.tv_clear_number:
                etNumber.setText("");
                break;
            case R.id.et_number:
                break;
            case R.id.tv_bullish:
                if (etPrise.getText().toString().equals("")) {
                    ToastUtil.showCenter("请填写价格");
                    return;
                }
                if (from.equals(AppUtils.OKEX)) {
                    String tradeType = "1";
                    if (!isOpen) {
                        tradeType = "4";
                    }
                    String lever = tvTradeLeverValue.getText().toString().trim().
                            replace("×", "");
                    NumberUtil.getInt(etNumber.getText().toString(), 0, 10);
                    String number = NumberUtil.getInt(etNumber.getText().toString(), 0, 10) + "";
                    final FuturesOrderReq futuresOrderReq = new FuturesOrderReq(lever,
                            "0", number, etPrise.getText().toString(),
                            tradeType, instrumentId);

                    if (preferenceManager.getBoolean(PreferenceManager.IS_SHOW_TRADE_DIALOG, false)) {
                        futuresOrder(futuresOrderReq);
                    } else {
                        tradeConfirmFragment.update(futuresOrderReq, null, time);
                        tradeConfirmFragment.show(this);
                        tradeConfirmFragment.setListener(new TradeConfirmFragment.ActionListener() {
                            @Override
                            public void onViewClick() {
                                futuresOrder(futuresOrderReq);
                            }
                        });
                    }
                } else {
                    if (BitdepthRes == null) {
                        return;
                    }
                    String ordType = LIMIT;
                    String prise = etPrise.getText().toString();
                    if (prise.equals(getString(R.string.trade_market_price))) {
                        ordType = MARKET;
                    } else if (prise.equals(getString(R.string.buy_one_price))) {
                        prise = BitdepthRes.getBids().get(0).get(0) + "";
                    } else if (prise.equals(getString(R.string.sell_one_price))) {
                        prise = BitdepthRes.getAsks().get(0).get(0) + "";
                    }

                    final OrderReq req = new OrderReq(instrumentId,
                            etNumber.getText().toString(), prise, ordType, BUY);
                    req.setOpen(isOpen);
                    if (preferenceManager.getBoolean(PreferenceManager.IS_SHOW_TRADE_DIALOG, false)) {
                        if (req.getOrdType().equals(MARKET)) {
                            req.setPrice(null);
                        }
                        mPresenter.bitmexFuturesOrder(req);
                    } else {
                        tradeConfirmFragment.update(null, req, time);
                        tradeConfirmFragment.show(this);
                        tradeConfirmFragment.setListener(new TradeConfirmFragment.ActionListener() {
                            @Override
                            public void onViewClick() {
                                if (req.getOrdType().equals(MARKET)) {
                                    req.setPrice(null);
                                }
                                mPresenter.bitmexFuturesOrder(req);
                            }
                        });
                    }
                }
                break;
            case R.id.tv_bearish:
                if (etPrise.getText().toString().equals("")) {
                    ToastUtil.showCenter("请填写价格");
                    return;
                }
                String sellLever = tvTradeLeverValue.getText().toString().trim()
                        .replace("×", "");
                if (from.equals(AppUtils.OKEX)) {
                    String tradeSellType = "2";
                    if (!isOpen) {
                        tradeSellType = "3";
                    }

                    String sellNumber = (int) (NumberUtil.getDouble(etNumber.getText().toString(), 0.0)) + "";
                    final FuturesOrderReq sell_FuturesOrderReq = new FuturesOrderReq(sellLever, "0",
                            sellNumber, etPrise.getText().toString(), tradeSellType, instrumentId);
                    if (preferenceManager.getBoolean(PreferenceManager.IS_SHOW_TRADE_DIALOG, false)) {
                        futuresOrder(sell_FuturesOrderReq);
                    } else {
                        tradeConfirmFragment.update(sell_FuturesOrderReq, null, time);
                        tradeConfirmFragment.show(this);
                        tradeConfirmFragment.setListener(new TradeConfirmFragment.ActionListener() {
                            @Override
                            public void onViewClick() {
                                futuresOrder(sell_FuturesOrderReq);
                            }
                        });
                    }
                } else {
                    if (BitdepthRes == null) {
                        return;
                    }
                    String ordType = LIMIT;
                    String prise = etPrise.getText().toString();
                    if (prise.equals(getString(R.string.trade_market_price))) {
                        ordType = MARKET;
                    } else if (prise.equals(getString(R.string.buy_one_price))) {
                        prise = BitdepthRes.getBids().get(0).get(0) + "";
                    } else if (prise.equals(getString(R.string.sell_one_price))) {
                        prise = BitdepthRes.getAsks().get(0).get(0) + "";
                    }

                    final OrderReq req = new OrderReq(instrumentId,
                            etNumber.getText().toString(), prise, ordType, SELL);
                    req.setOpen(isOpen);
                    if (preferenceManager.getBoolean(PreferenceManager.IS_SHOW_TRADE_DIALOG, false)) {
                        if (req.getOrdType().equals(MARKET)) {
                            req.setPrice(null);
                        }
                        mPresenter.bitmexFuturesOrder(req);
                    } else {
                        tradeConfirmFragment.update(null, req, time);
                        tradeConfirmFragment.show(this);
                        tradeConfirmFragment.setListener(new TradeConfirmFragment.ActionListener() {
                            @Override
                            public void onViewClick() {
                                if (req.getOrdType().equals(MARKET)) {
                                    req.setPrice(null);
                                }
                                mPresenter.bitmexFuturesOrder(req);
                            }
                        });
                    }
                }
                break;
        }
    }

    private void setSellOpenValueShow() {
        String currency = instrumentId.substring(0, 3);
        try {
            if (PositionRes == null || PositionRes.getHolding() == null) {
                return;
            }

            int long_qty = NumberUtil.getInt(PositionRes.getHolding().get(0).getLong_qty(), 0, 10);//多仓数量
            int long_avail_qty = NumberUtil.getInt(PositionRes.getHolding().get(0).getLong_avail_qty(), 0, 10);//	多仓可平仓数量
            int short_qty = NumberUtil.getInt(PositionRes.getHolding().get(0).getShort_qty(), 0, 10);//		空仓数量
            int short_avail_qty = NumberUtil.getInt(PositionRes.getHolding().get(0).getShort_avail_qty(), 0, 10);//			空仓可平仓数量

            tvAvailableValue.setText(StringHelper.keepDecimal(short_avail_qty * 100 / last + "", 4) + " " + currency);
            tvOpenValue.setText(StringHelper.keepDecimal(short_qty * 100 / last + "", 4) + " " + currency);
            tvSellAvailableValue.setText(StringHelper.keepDecimal(long_avail_qty * 100 / last + "", 4) + " " + currency);
            tvOpenSellValue.setText(StringHelper.keepDecimal(long_qty * 100 / last + "" + "", 4) + " " + currency);
        } catch (Exception e) {
            tvAvailableValue.setText("0.0000 " + currency);
            tvOpenValue.setText("0.0000 " + currency);
            tvSellAvailableValue.setText("0.0000 " + currency);
            tvOpenSellValue.setText("0.0000 " + currency);
            e.printStackTrace();
        }
    }

    private void setBitmexSellOpenValueShow() {
        String currency = instrumentId.substring(0, 3);
        try {
            if (positionItemRes == null) {
                return;
            }
            int long_qty = 0;
            int short_qty = 0;
            if (positionItemRes.getCurrentQty() > 0) {
                long_qty = (int) Math.abs(positionItemRes.getCurrentQty());//多仓数量
            } else {
                short_qty = (int) Math.abs(positionItemRes.getCurrentQty());//		空仓数量
            }

            tvAvailableValue.setText(short_qty + " " + currency);
            tvOpenValue.setText(short_qty + " " + currency);
            tvSellAvailableValue.setText(long_qty + " " + currency);
            tvOpenSellValue.setText(long_qty + " " + currency);
        } catch (Exception e) {
            tvAvailableValue.setText("0.0000 " + currency);
            tvOpenValue.setText("0.0000 " + currency);
            tvSellAvailableValue.setText("0.0000 " + currency);
            tvOpenSellValue.setText("0.0000 " + currency);
            e.printStackTrace();
        }
    }

    //下单
    private void futuresOrder(FuturesOrderReq futuresOrderReq) {
        if (futuresOrderReq.getPrice().equals("市场价")) {
            futuresOrderReq.setMatch_price("1");
            futuresOrderReq.setPrice("0.0");
        } else if (futuresOrderReq.getPrice().equals("买一价")) {
            futuresOrderReq.setPrice(buyOnePrise + "");
        } else if (futuresOrderReq.getPrice().equals("卖一价")) {
            futuresOrderReq.setPrice(sellOnePrise + "");
        }
        mPresenter.futuresOrder(futuresOrderReq);//下单

    }

    private void setPriseInfo(String prise, boolean market, TextView selected,
                              TextView unselected1, TextView unselected2) {
        etPrise.setText(prise);
        etPrise.setEnabled(market);
        tvClearPrise.setClickable(market);
        selected.setTag(!market);
        if (market) {
            selected.setBackgroundResource(R.drawable.future_trade_bg_cfdeee);
            selected.setTextColor(getResources().getColor(R.color.time_blue));
        } else {
            selected.setBackgroundResource(R.color.trade_blue);
            selected.setTextColor(getResources().getColor(R.color.white));
            unselected1.setTag(market);
            unselected2.setTag(market);
        }
        unselected1.setBackgroundResource(R.drawable.future_trade_bg_cfdeee);
        unselected2.setBackgroundResource(R.drawable.future_trade_bg_cfdeee);
        unselected1.setTextColor(getResources().getColor(R.color.time_blue));
        unselected2.setTextColor(getResources().getColor(R.color.time_blue));
    }

    //获取委托列表
    private void getDelegationList(String insId, String status,
                                   final int from, final String type) {
        if (this.from.equals(AppUtils.OKEX)) {
            mPresenter.getDelegationList(instrumentId, "0", 0, time.substring(0, 2));
        } else {
            //先获取佣金列表在获取委托列表
            mPresenter.getCommissionInfo(instrumentId, "New", 100);
        }
    }

    private void setOpenValueShow(FuturesAccountsCurrencyRes AccountsCurrencyRes) {
        if (AccountsCurrencyRes == null) {
            return;
        }
        String currency = instrumentId.substring(0, 3);
        String Available_qty = "0.00000";
        if (AccountsCurrencyRes.getMargin_mode().equals("crossed")) {
            Available_qty = AccountsCurrencyRes.getTotal_avail_balance();
        } else if (AccountsCurrencyRes.getContracts() != null) {
            Available_qty = AccountsCurrencyRes.getContracts().get(0).getAvailable_qty();
        }
        String prise5 = StringHelper.keepDecimal(Available_qty, 5);
        double available_qty = NumberUtil.getDouble(prise5, 0.0) * lever;
        tvAvailableValue.setText(StringHelper.keepDecimal(Available_qty, 4) + " " + currency);
        tvOpenValue.setText(StringHelper.keepDecimal(available_qty + "", 4) + " " + currency);
        tvSellAvailableValue.setText(StringHelper.keepDecimal(Available_qty, 4) + " " + currency);
        tvOpenSellValue.setText(StringHelper.keepDecimal(available_qty + "", 4) + " " + currency);
    }

    private void setBitmexOpenValueShow() {
        if (userMarginRes == null) {
            return;
        }
        String currency = instrumentId.substring(0, 3);
        String Available_qty = "0.00000";
        Available_qty = userMarginRes.getAvailableMargin() / 100000000 + "";
        String prise5 = StringHelper.keepDecimal(Available_qty, 5);
        double available_qty = NumberUtil.getDouble(prise5, 0.0) * lever;
        tvAvailableValue.setText(StringHelper.keepDecimal(Available_qty, 4) + " " + currency);
        tvOpenValue.setText(StringHelper.keepDecimal(available_qty + "", 4) + " " + currency);
        tvSellAvailableValue.setText(StringHelper.keepDecimal(Available_qty, 4) + " " + currency);
        tvOpenSellValue.setText(StringHelper.keepDecimal(available_qty + "", 4) + " " + currency);
    }

    @Subscribe(sticky = true)
    public void onAcceptInstrument(Event.SendInstrument sendInstrument) {
        instrumentId = sendInstrument.getInstrumentId();
        insIdCase_3 = instrumentId.substring(0, 3).toLowerCase();
        time = sendInstrument.getTime();
        from = sendInstrument.getFrom();
    }

    @Override
    public void onGetFuturesInfo(FuturesInstrumentsTickerList futuresInfo) {
        //发送订阅合约信息，要在的destroy 判断是否应该取消订阅
        FutureWebSocketManager.getInstance().subscribeDetail(insIdCase_3, ChannelHelper.getTime(time));

        tvTradePriseShow.setText("$" + df.format(futuresInfo.getLast()));
        etPrise.setText(df.format(futuresInfo.getLast()));
        buyOnePrise = futuresInfo.getBest_bid();
        sellOnePrise = futuresInfo.getBest_ask();
        last = futuresInfo.getLast();
        mPresenter.getCandles(instrumentId, last);
    }

    @Override
    public void onGetFuturesInfoError(String msg) {
        ToastUtil.showCenter(msg);
    }

    @Override
    public void onGetCandles(List<List<Double>> lists, double prise) {
        if (lists == null || lists.get(0).size() == 0) {
            return;
        }
        double p = (((prise - lists.get(0).get(1))) / lists.get(0).get(1)) * 100;
        if (p < 0) {
            tvTradePriseShow.setTextColor(AppUtils.getDecreaseColor());
            tvTradeKlineShow.setBackground(AppUtils.getDecreaseBg());
            tvTradeKlineShow.setText("" + df.format(p) + "%");
            tvTradeUpDownShow.setText("" + df.format(prise - lists.get(0).get(1)));
        } else {
            tvTradePriseShow.setTextColor(AppUtils.getIncreaseColor());
            tvTradeKlineShow.setBackground(AppUtils.getIncreaseBg());
            tvTradeKlineShow.setText("+" + df.format(p) + "%");
            tvTradeUpDownShow.setText("+" + df.format(prise - lists.get(0).get(1)));
        }
    }

    @Override
    public void onGetCandlesError(String msg) {
        ToastUtil.showCenter(msg);
    }

    @Override
    public void onGetFuturesPositionInfo(SingleFuturesPositionRes PositionRes) {
        this.PositionRes = PositionRes;
        if (!isOpen) {
            setSellOpenValueShow();
        }
    }

    @Override
    public void onGetFuturesPositionInfoError(String msg) {
        ToastUtil.showCenter(msg);
    }

    @Override
    public void onGetFuturesBook(FuturesInstrumentsBookRes futuresInstrumentsBookRes) {
        tradeDepthAdapter.updateItems(futuresInstrumentsBookRes);
        //发送订阅深度数据，要在的destroy 判断是否应该取消订阅
        FutureWebSocketManager.getInstance().subscribeAllDepth(insIdCase_3,
                ChannelHelper.getTime(time), ChannelHelper.Z1.FIVE);
    }

    @Override
    public void onGetFuturesBookError(String msg) {
        ToastUtil.showCenter(msg);
    }

    @Override
    public void onGetFuturesIndex(FuturesInstrumentsIndexRes futuresInstrumentsIndexRes) {
        tvTradeListIndexValue.setText(df.format(futuresInstrumentsIndexRes.getIndex()));
        FutureWebSocketManager.getInstance().subscribeIndex(insIdCase_3);
    }

    @Override
    public void onGetFuturesIndexError(String msg) {
        ToastUtil.showCenter(msg);
    }

    @Override
    public void onGetFuturesCurrencyInfo(FuturesAccountsCurrencyRes AccountsCurrencyRes) {
        futuresAccountsCurrencyRes = AccountsCurrencyRes;
        if (isOpen) {  //开仓
            setOpenValueShow(futuresAccountsCurrencyRes);
        }
    }

    @Override
    public void onGetFuturesCurrencyInfoError(String msg) {
        ToastUtil.showCenter(msg);
    }

    @Override
    public void onFuturesOrder(FuturesOrderRes futuresOrderRes) {
        if (futuresOrderRes.getResult()) {
            ToastUtil.showCenter("下单成功");
            mPresenter.getFuturesPositionInfo(instrumentId);
            mPresenter.getFuturesCurrencyInfo(instrumentId);
            getDelegationList(instrumentId, "0", 0, time.substring(0, 2));
        }
    }

    @Override
    public void onFuturesOrderError(String msg) {
        ToastUtil.showCenter(msg);
    }

    @Override
    public void onCancelOrder(FuturesCancelOrderRes futuresCancelOrderRes) {
        ToastUtil.showCenter("取消成功");
        getDelegationList(instrumentId, "0", 0, time.substring(0, 2));
    }

    @Override
    public void onCancelOrderError(String msg) {
        ToastUtil.showCenter(msg);
    }

    @Override
    public void onGetDelegationList(List<DelegationItemVO> delegationItemVOS) {
        delegationAdapter = new DelegationAdapter(getContext(), delegationItemVOS,
                new DelegationAdapter.ItemClickListener() {
                    @Override
                    public void onClickCancel(String orderId, String insId, int position) {
                        if (from.equals(AppUtils.OKEX)) {
                            mPresenter.cancelOrder(orderId, insId);
                        } else {
                            mPresenter.cancelBitMexOrder(orderId);
                        }
                    }
                });
        rvDelegationList.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        rvDelegationList.setAdapter(delegationAdapter);

    }

    @Override
    public void onGetDelegationListError(String msg) {
        ToastUtil.showCenter(msg);
    }

    @Override
    public void onGetBitmexInstrument(List<InstrumentItemRes> instrumentItemResList) {
        InstrumentItemRes res = instrumentItemResList.get(0);
        if (res.getQuoteCurrency().contains("USD")) {
            tvTradePriseShow.setText("$" + df.format(res.getLastPrice()));
            tvTradeListIndexValue.setText(df.format(res.getIndicativeSettlePrice()));
            tvPrise.setText(getString(R.string.price_usd));
            etPrise.setText(df.format(res.getLastPrice()));
            tvTradeListPrise.setText(getString(R.string.price_usd));
        } else {
            tvTradePriseShow.setText("฿" + BigDecimal.valueOf(res.getLastPrice()) + "");
            tvTradeListIndexValue.setText(BigDecimal.valueOf(res.getIndicativeSettlePrice()) + "");
            tvPrise.setText(getString(R.string.price_btc));
            etPrise.setText(BigDecimal.valueOf(res.getLastPrice()) + "");
            tvTradeListPrise.setText(getString(R.string.price_btc));
        }

        double p = res.getLastChangePcnt() * 100;
        if (p < 0) {
            tvTradePriseShow.setTextColor(AppUtils.getDecreaseColor());
            tvTradeKlineShow.setBackground(AppUtils.getDecreaseBg());
            tvTradeKlineShow.setText("" + df.format(p) + "%");
            if (res.getQuoteCurrency().contains("USD")) {
                tvTradeUpDownShow.setText("" + df.format(res.getLastPrice() * res.getLastChangePcnt()));
            } else {
                tvTradeUpDownShow.setText("" + BigDecimal.valueOf(res.getLastPrice() * res.getLastChangePcnt()));
            }
        } else {
            tvTradePriseShow.setTextColor(AppUtils.getIncreaseColor());
            tvTradeKlineShow.setBackground(AppUtils.getIncreaseBg());
            tvTradeKlineShow.setText("+" + df.format(p) + "%");
            if (res.getQuoteCurrency().contains("USD")) {
                tvTradeUpDownShow.setText("+" + df.format(res.getLastPrice() * res.getLastChangePcnt()));
            } else {
                tvTradeUpDownShow.setText("+" + BigDecimal.valueOf(res.getLastPrice() * res.getLastChangePcnt()));
            }
        }
    }

    @Override
    public void onGetBitmexInstrumentError(String msg) {
        ToastUtil.showCenter(msg);
    }

    @Override
    public void onGetBitmexPositionInfo(List<PositionItemRes> positionItemResList) {
        if (positionItemResList == null || positionItemResList.size() == 0) {
            tvTradeLeverValue.setText("全仓" + "  ");
            lever = 0;
            return;
        }
        positionItemRes = positionItemResList.get(0);
        if (positionItemRes.isCrossMargin()) {
            tvTradeLeverValue.setText("全仓" + "  ");
            lever = 0;
        } else {
            tvTradeLeverValue.setText(positionItemRes.getLeverage() + " ×");
            lever = positionItemRes.getLeverage();
        }
        mPresenter.getBitmexUserMargin();
    }

    @Override
    public void onGetBitmexPositionInfoError(String msg) {
        ToastUtil.showCenter(msg);
    }

    @Override
    public void onGetBitmexUserMargin(UserMarginRes userMarginRes) {
        this.userMarginRes = userMarginRes;
        setBitmexOpenValueShow();
    }

    @Override
    public void onGetBitmexUserMarginError(String msg) {
        ToastUtil.showCenter(msg);
    }

    @Override
    public void onPostLeverage(PositionItemRes positionItemRes) {
        if (isOpen) {
            setBitmexOpenValueShow();
        }
    }

    @Override
    public void onPostLeverageError(String msg) {
        ToastUtil.showCenter(msg);
    }

    @Override
    public void onCancelBitMexOrder(List<OrderItemRes> orderItemResList) {
        ToastUtil.showCenter("取消成功");
        getDelegationList(instrumentId, "0", 0, time.substring(0, 2));
    }

    @Override
    public void onCancelBitMexOrderError(String msg) {
        ToastUtil.showCenter(msg);
    }

    public interface ActionListener {
        void onViewClick();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (from.equals(AppUtils.OKEX)) {
            int detail = FutureWebSocketManager.detailMap.get(insIdCase_3);
            if (detail == 1) {
                //发送取消订阅深度数据
                FutureWebSocketManager.getInstance().unsubscribeDetail(insIdCase_3, ChannelHelper.getTime(time));
            }
            //无论是否最后一个都减少一个计数
            FutureWebSocketManager.detailMap.put(insIdCase_3, --detail);

            int de = FutureWebSocketManager.depthMap.get(insIdCase_3);
            if (de == 1) {
                //发送取消订阅深度数据
                FutureWebSocketManager.getInstance().unsubscribeAllDepth(insIdCase_3,
                        ChannelHelper.getTime(time), ChannelHelper.Z1.FIVE);

            }
            //无论是否最后一个都减少一个计数
            FutureWebSocketManager.depthMap.put(insIdCase_3, --de);

            int index = FutureWebSocketManager.indexMap.get(insIdCase_3);
            if (index == 1) {
                //发送取消订阅指数
                FutureWebSocketManager.getInstance().unsubscribeIndex(insIdCase_3);
            }
            //无论是否最后一个都减少一个计数
            FutureWebSocketManager.indexMap.put(insIdCase_3, --index);
        } else {
            BitMEXWebSocketManager.getInstance().unsubscribeOrderBook10(instrumentId);
        }
    }

    @Override
    protected void onMessage(List list) {
        try {
            List<CommonRes> commonResList = list;
            final FuturesInstrumentsBookRes depthRes;
            if (from.equals(AppUtils.OKEX)) { //okex
                final FuturesInstrumentsIndexRes indexRes;
                final DetailRes detailRes;
                String channel = commonResList.get(0).getChannel();
                if (channel.contains("depth")) {  //对depth的推送消息才进行处理
                    String icon = channel.substring(17, 20); //截取币种，如btc
                    if (insIdCase_3.equals(icon)) {
                        depthRes = (FuturesInstrumentsBookRes) GsonUtils.getInstance().fromJson(
                                commonResList.get(0).getData().toString(), FuturesInstrumentsBookRes.class);
                        if (depthRes != null && tradeDepthAdapter != null) {
                            //返回到主线程刷新数据
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tradeDepthAdapter.updateItems(depthRes);
                                }
                            });
                        }
                    }
                } else if (channel.contains("index")) {  //对指数的推送消息才进行处理
                    String icon = channel.substring(17, 20); //截取币种，如btc
                    if (insIdCase_3.equals(icon)) {
                        indexRes = (FuturesInstrumentsIndexRes) GsonUtils.getInstance().fromJson(
                                commonResList.get(0).getData().toString(), FuturesInstrumentsIndexRes.class);
                        if (indexRes != null && tvTradeListIndexValue != null) {
                            //返回到主线程刷新数据
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvTradeListIndexValue.setText(df.format(indexRes.getFutureIndex()));
                                }
                            });
                        }
                    }
                } else if (channel.contains("ticker")) {  //对ticker的推送消息才进行处理
                    String icon = channel.substring(17, 20); //截取币种，如btc
                    if (insIdCase_3.equals(icon)) {
                        detailRes = (DetailRes) GsonUtils.getInstance().fromJson(
                                commonResList.get(0).getData().toString(), DetailRes.class);
                        if (detailRes != null && tvTradePriseShow != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (tvTradePriseShow == null) {
                                        return;
                                    }
                                    if (!tvTradePriseShow.getText().toString()
                                            .contains(df.format(detailRes.getLast()))) {  //推送过来的价格和上一次不一样才刷新item
                                        tvTradePriseShow.setText("$" + df.format(detailRes.getLast()));
                                        buyOnePrise = detailRes.getBuy();
                                        sellOnePrise = detailRes.getSell();
                                        last = detailRes.getLast();
                                        mPresenter.getCandles(instrumentId, last);
                                    }
                                }
                            });
                        }
                    }
                }
            } else if (from.equals(AppUtils.BITMEX)) {  //bitmex
                String table = commonResList.get(0).getTable();
                Object object = commonResList.get(0).getData();
                if (table == null || object == null) {
                    return;
                }
                String sub = GsonUtils.getInstance().toJson(commonResList.get(0).getData());
                if (table.equals("instrument")) {
                    List<InstrumentItemRes> instrumentItemResList = GsonUtils.getGson().
                            fromJson(sub, new TypeToken<List<InstrumentItemRes>>() {
                            }.getType());
                    final InstrumentItemRes res = instrumentItemResList.get(0);
                    if (!res.getSymbol().contains(instrumentId)) {
                        return;
                    }
                    //返回到主线程刷新数据
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (res.getLastChangePcnt() != 0) {
                                double p = res.getLastChangePcnt() * 100;
                                if (p < 0) {
                                    tvTradePriseShow.setTextColor(AppUtils.getDecreaseColor());
                                    tvTradeKlineShow.setBackground(AppUtils.getDecreaseBg());
                                    tvTradeKlineShow.setText("" + df.format(p) + "%");
                                } else {
                                    tvTradePriseShow.setTextColor(AppUtils.getIncreaseColor());
                                    tvTradeKlineShow.setBackground(AppUtils.getIncreaseBg());
                                    tvTradeKlineShow.setText("+" + df.format(p) + "%");
                                }
                            }
                            if (res.getLastPrice() != 0) {
                                if (tvTradePriseShow.getText().toString().contains("$")) {
                                    tvTradePriseShow.setText("$" + df.format(res.getLastPrice()));
                                } else {
                                    tvTradePriseShow.setText("฿" + BigDecimal.valueOf(res.getLastPrice()) + "");
                                }
                            }
                        }
                    });
                } else if (table.equals("orderBook10")) {
                    List<FuturesInstrumentsBookRes> instrumentItemResList = GsonUtils.getGson()
                            .fromJson(sub, new TypeToken<List<FuturesInstrumentsBookRes>>() {
                            }.getType());
                    depthRes = instrumentItemResList.get(0);
                    BitdepthRes = depthRes;
                    if (instrumentId.equals(depthRes.getSymbol())) {
                        if (depthRes != null && tradeDepthAdapter != null) {
                            //返回到主线程刷新数据
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tradeDepthAdapter.updateItems(depthRes);
                                }
                            });
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

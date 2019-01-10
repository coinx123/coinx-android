package com.coin.exchange.mvp.MineBind;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coin.exchange.R;
import com.coin.exchange.cache.BitMexUserCache;
import com.coin.exchange.cache.OkExUserCache;
import com.coin.exchange.cache.PreferenceManager;
import com.coin.exchange.config.FragmentConfig;
import com.coin.exchange.context.AppApplication;
import com.coin.exchange.model.bitMex.response.InstrumentItemRes;
import com.coin.exchange.model.bitMex.response.UserMarginRes;
import com.coin.exchange.model.okex.response.FuturesAccountsResItem;
import com.coin.exchange.mvp.MineBind.di.DaggerMineBindComponent;
import com.coin.exchange.mvp.MineBind.di.MineBindModule;
import com.coin.exchange.utils.AppUtils;
import com.coin.exchange.view.AccountEquityActivity;
import com.coin.exchange.view.BindActivity;
import com.coin.libbase.utils.ToastUtil;
import com.coin.libbase.view.fragment.JBaseFragment;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public final class MineBindFragment extends JBaseFragment<MineBindPresenter> implements MineBindView {


    private final DecimalFormat df = new DecimalFormat("######0.00");
    private final DecimalFormat df4 = new DecimalFormat("######0.0000");
    private static final String FROM_WHERE = "fromWhere";

    @Inject
    PreferenceManager preferenceManager;

    @BindView(R.id.rl_bing)
    RelativeLayout rlBing;
    @BindView(R.id.tv_bind_name)
    TextView tvBindName;
    @BindView(R.id.tv_bind_name_value)
    TextView tvBindNameValue;
    @BindView(R.id.tv_bind_show)
    TextView tvBindShow;
    @BindView(R.id.tv_assets)
    TextView tvAssets;
    @BindView(R.id.tv_assets_value)
    TextView tvAssetsValue;
    @BindView(R.id.tv_position_to)
    TextView tvPositionTo;
    @BindView(R.id.tv_bind)
    TextView tvBind;
    @BindView(R.id.rl_bind)
    LinearLayout rlBind;
    private double BTC_USDValue = 1;//okex btc 转换美元
    private double XBT_USDValue = 1;//bitmex XBT 转换美元
//    private double EThValue = 0;
//    private double BTCValue = 0;
    private double AllBTCValue = 0;
    private double AllValue = 0;
    private double all_equity = 0;
    private List<FuturesAccountsResItem> tradesList = new ArrayList<>();
    private String from;

    public static MineBindFragment newInstance(String from) {
        MineBindFragment fragment = new MineBindFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FROM_WHERE, from);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bind, container, false);
    }

    @Override
    protected void initView(View view) {
        Bundle arguments = getArguments();
        from = arguments.getString(FROM_WHERE);
        if (from.equals(AppUtils.BITMEX)) {
            rlBing.setBackgroundResource(R.drawable.future_trade_bg_black_10dp);
            tvBindName.setBackgroundResource(R.drawable.bitmex_icon_round);
            tvBindNameValue.setText(from);
            tvBind.setTextColor(getResources().getColor(R.color.color_black_f83b));
            tvBind.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bind_black_icon, 0, 0, 0);
        }
    }

    @Override
    protected void registerDagger() {
        DaggerMineBindComponent.builder()
                .appComponent(AppApplication.getAppComponent())
                .mineBindModule(new MineBindModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OkExUserCache.isEmpty() && from.equals(AppUtils.OKEX)) {
            tvBindShow.setVisibility(View.VISIBLE);
            tvAssets.setVisibility(View.VISIBLE);
            tvAssetsValue.setVisibility(View.VISIBLE);
            tvPositionTo.setVisibility(View.VISIBLE);
            tvPositionTo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.forwad_icon, 0);
            rlBind.setVisibility(View.GONE);
            mPresenter.getOkexFutures();  //获取所有的合约，然后获取获取比特币兑换美元换的值
        } else if (!BitMexUserCache.isEmpty() && from.equals(AppUtils.BITMEX)) {
            tvBindShow.setVisibility(View.VISIBLE);
            tvAssets.setVisibility(View.VISIBLE);
            tvAssetsValue.setVisibility(View.VISIBLE);
            tvPositionTo.setVisibility(View.VISIBLE);
            tvPositionTo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.forward_gray, 0);
            rlBind.setVisibility(View.GONE);
            mPresenter.getBitmexInstrument("XBTUSD"); //获取永续的合约，将XBT转成美元的汇率
        } else {
            tvBindShow.setVisibility(View.GONE);
            tvAssets.setVisibility(View.GONE);
            tvAssetsValue.setVisibility(View.GONE);
            tvPositionTo.setVisibility(View.GONE);
            rlBind.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.ll_bind_show, R.id.rl_position_to, R.id.rl_bind})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_bind_show:
                if (preferenceManager.getBoolean(PreferenceManager.IS_SHOW_ASSETS_VALUE, true)) {
                    preferenceManager.putBoolean(PreferenceManager.IS_SHOW_ASSETS_VALUE, false);
                    tvAssetsValue.setText("$****");
                    tvBindShow.setBackgroundResource(R.drawable.show_hide);
                } else {
                    preferenceManager.putBoolean(PreferenceManager.IS_SHOW_ASSETS_VALUE, true);
                    tvAssetsValue.setText("≈$ " + df.format(AllValue));
                    tvBindShow.setBackgroundResource(R.drawable.show_icon);
                }
                break;
            case R.id.rl_position_to:
                Intent intentAccount = new Intent();
                intentAccount.putExtra(FragmentConfig.FROM, from);
                intentAccount.putExtra("AllValue", df.format(AllValue)); //AccountEquityActivity 一样存在要一起改
                intentAccount.putExtra("AllBTCValue", df4.format(AllBTCValue)); //AccountEquityActivity 一样存在要一起改
                intentAccount.putExtra("AccountEquityList", (Serializable) tradesList); //AccountEquityActivity 一样存在要一起改
                intentAccount.setClass(getContext(), AccountEquityActivity.class);
                startActivity(intentAccount);
                break;
            case R.id.rl_bind:
                Intent intent = new Intent(getContext(), BindActivity.class);
                if (from.equals(AppUtils.OKEX)) {
                    intent.putExtra(BindActivity.TYPE, AppUtils.OKEX);
                } else {
                    intent.putExtra(BindActivity.TYPE, AppUtils.BITMEX);
                }
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onGetOkexFutures(Double value) {
        BTC_USDValue = value;
        mPresenter.getAssetsValue();
    }

    @Override
    public void onGetOkexFuturesError(String msg) {
        ToastUtil.showCenter(msg);
    }

    @Override
    public void onGetAssetsValue(List<FuturesAccountsResItem> futuresAccountsResItemList) {
        tradesList.clear();
        tradesList.addAll(futuresAccountsResItemList);
        mPresenter.getSpotTickerSing(tradesList);//获取币币合约
    }

    @Override
    public void onGetAssetsValueError(String msg) {
        ToastUtil.showCenter(msg);
    }

    @Override
    public void onGetSpotTickerSing(Double value) {
        AllBTCValue = value;
        AllValue = value * BTC_USDValue;
        if (preferenceManager.getBoolean(PreferenceManager.IS_SHOW_ASSETS_VALUE, true)) {
            tvAssetsValue.setText("≈$ " + df.format(AllValue));
            tvBindShow.setBackgroundResource(R.drawable.show_icon);
        } else {
            tvAssetsValue.setText("$****");
            tvBindShow.setBackgroundResource(R.drawable.show_hide);
        }
    }

    @Override
    public void onGetSpotTickerSingError(String msg) {
        ToastUtil.showCenter(msg);
    }

    @Override
    public void onGetBitmexUserMargin(UserMarginRes userMarginRes) {
        all_equity = userMarginRes.getMaintMargin();
        AllBTCValue = userMarginRes.getWalletBalance();
        AllValue = userMarginRes.getWalletBalance() * XBT_USDValue / 100000000;
        tvAssetsValue.setText("≈$ " + df.format(AllValue));
        mPresenter.getBitMexPosition(all_equity, AllBTCValue);
    }

    @Override
    public void onGetBitmexUserMarginError(String msg) {
        ToastUtil.showCenter(msg);
    }

    @Override
    public void onGetBitmexInstrument(List<InstrumentItemRes> instrumentItemResList) {
        XBT_USDValue = instrumentItemResList.get(0).getLastPrice();
        mPresenter.getBitmexUserMargin();
    }

    @Override
    public void onGetBitmexInstrumentError(String msg) {
        ToastUtil.showCenter(msg);
    }

    @Override
    public void onGetBitMexPosition(List<FuturesAccountsResItem> futuresAccountsResItemList) {
        tradesList.clear();
        tradesList.addAll(futuresAccountsResItemList);
    }

    @Override
    public void onGetBitMexPositionError(String msg) {
        ToastUtil.showCenter(msg);
    }

}

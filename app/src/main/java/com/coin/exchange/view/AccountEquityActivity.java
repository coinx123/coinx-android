package com.coin.exchange.view;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coin.exchange.R;
import com.coin.exchange.adapter.AccountEquityAdapter;
import com.coin.exchange.cache.OkExUserCache;
import com.coin.exchange.cache.PreferenceManager;
import com.coin.exchange.config.FragmentConfig;
import com.coin.exchange.context.AppApplication;
import com.coin.exchange.model.okex.response.FuturesAccountsResItem;
import com.coin.exchange.utils.AppUtils;
import com.coin.exchange.utils.NumberUtil;
import com.coin.exchange.view.fragment.main.UnBindFragment;
import com.coin.libbase.view.activity.JBaseActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author dean
 * @date 创建时间：2018/11/28
 * @description 账户权益
 */
public class AccountEquityActivity extends JBaseActivity {

    @Inject
    PreferenceManager preferenceManager;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_bind_show)
    TextView tvBindShow;
    @BindView(R.id.tv_assets)
    TextView tvAssets;
    @BindView(R.id.tv_assets_value)
    TextView tvAssetsValue;
    @BindView(R.id.tv_btc_value)
    TextView tvBtcValue;
    @BindView(R.id.rv_hold_assets)
    RecyclerView rvHoldAssets;
    @BindView(R.id.rl_bind)
    RelativeLayout rlBind;
    private AccountEquityAdapter accountEquityAdapter;
    private String from = "";
    private String AllValue = "";
    private String AllBTCValue = "";
    private List<FuturesAccountsResItem> AccountEquityList = new ArrayList<>();

    private UnBindFragment unBindFragment;

    @Override
    protected int getLayout() {
        AppApplication.getAppComponent().inject(this);
        return R.layout.activity_account_equity;
    }

    @Override
    protected void initIntent(Intent intent) {
        from = getIntent().getStringExtra(FragmentConfig.FROM);
        AllValue = getIntent().getStringExtra("AllValue"); //MineBindFragment 一样存在要一起改
        AllBTCValue = getIntent().getStringExtra("AllBTCValue"); //MineBindFragment 一样存在要一起改
        AccountEquityList = (List<FuturesAccountsResItem>) getIntent().getSerializableExtra("AccountEquityList");
        tvTitle.setText("  " + from);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("解绑");
        if (from.equals(AppUtils.OKEX)) {
            tvTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.okex_icon_24dp, 0, 0, 0);
        } else {
            tvTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bitmex_icon_24dp, 0, 0, 0);
            rlBind.setBackgroundResource(R.drawable.future_trade_bg_black_10dp);
        }
    }

    @Override
    protected void initView() {
        if (preferenceManager.getBoolean(PreferenceManager.IS_SHOW_ASSETS_VALUE, true)) {
            tvAssetsValue.setText("≈$ " + AllValue);
            tvBindShow.setBackgroundResource(R.drawable.show_icon);
            if (from.equals(AppUtils.OKEX)) {
                tvBtcValue.setText(AllBTCValue + " BTC");
            } else {
                tvBtcValue.setText(NumberUtil.getDouble(AllBTCValue, 0) / 100000000 + " BTC");
            }
        } else {
            tvAssetsValue.setText("$****");
            tvBtcValue.setText("****");
            tvBindShow.setBackgroundResource(R.drawable.show_hide);
        }
        rvHoldAssets.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        accountEquityAdapter = new AccountEquityAdapter(this);
        rvHoldAssets.setAdapter(accountEquityAdapter);
        rvHoldAssets.setHasFixedSize(true);
        if (AccountEquityList != null) {
            accountEquityAdapter.updateItems(AccountEquityList);
        }
        unBindFragment = new UnBindFragment();
        unBindFragment.setListener(new UnBindFragment.ActionListener() {
            @Override
            public void onViewClick() {
                OkExUserCache.remove();
                finish();
            }
        });
    }

    @Override
    protected void initData() {

    }


    @OnClick({R.id.ib_back, R.id.tv_right, R.id.rl_bind_show})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.tv_right: //解绑
                unBindFragment.show(this);
                break;
            case R.id.rl_bind_show:
                if (preferenceManager.getBoolean(PreferenceManager.IS_SHOW_ASSETS_VALUE, true)) {
                    preferenceManager.putBoolean(PreferenceManager.IS_SHOW_ASSETS_VALUE, false);
                    tvAssetsValue.setText("$****");
                    tvBtcValue.setText("****");
                    tvBindShow.setBackgroundResource(R.drawable.show_hide);
                } else {
                    preferenceManager.putBoolean(PreferenceManager.IS_SHOW_ASSETS_VALUE, true);
                    tvAssetsValue.setText("≈$ " + AllValue);
                    if (from.equals(AppUtils.OKEX)) {
                        tvBtcValue.setText(AllBTCValue + " BTC");
                    } else {
                        tvBtcValue.setText(NumberUtil.getDouble(AllBTCValue, 0) / 100000000 + " BTC");
                    }
                    tvBindShow.setBackgroundResource(R.drawable.show_icon);
                }
                break;
        }
    }
}

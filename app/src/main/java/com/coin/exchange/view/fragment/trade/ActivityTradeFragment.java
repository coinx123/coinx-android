package com.coin.exchange.view.fragment.trade;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coin.exchange.R;
import com.coin.exchange.mvp.TradeBusiness.TradeBusinessFragment;
import com.coin.exchange.view.fragment.main.trade.TradeOkAndBitFragment;
import com.coin.libbase.model.eventbus.Event;
import com.coin.libbase.view.fragment.JBaseFragment;

import org.greenrobot.eventbus.Subscribe;

/**
 * @author dean
 * @date 创建时间：2018/11/19
 * @description 交易activity界面的交易fragment
 */
public class ActivityTradeFragment extends JBaseFragment {
    private static final String FROM = "from";
    private TradeOkAndBitFragment tradeOkexFragment;
    private TradeBusinessFragment tradeBusinessFragment;
    private String from = "";
    public static Fragment newInstance(String from) {
        ActivityTradeFragment fragment = new ActivityTradeFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FROM, from);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_activity_trade, container, false);
    }

    @Override
    protected void initView(View view) {
        Bundle arguments = getArguments();
        from = arguments.getString(FROM);
        tradeBusinessFragment = TradeBusinessFragment.newInstance();
        tradeBusinessFragment.setListener(new TradeBusinessFragment.ActionListener() {
            @Override
            public void onViewClick() {
                getChildFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_left_in,
                                R.anim.slide_right_out,
                                R.anim.slide_right_in,
                                R.anim.slide_left_out
                        ).replace(R.id.fl_trade_change, tradeOkexFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });
        tradeOkexFragment = TradeOkAndBitFragment.newInstance(from+"trade");//from+"trade"代表下单界面进入的
        tradeOkexFragment.setListener(new TradeOkAndBitFragment.ActionListener() {
            @Override
            public void onViewClick() {
                ToTradeBusess();
            }
        });

        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.fl_trade_change, tradeBusinessFragment)
                .commit();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && tradeBusinessFragment != null) {
            tradeBusinessFragment.Update();
        }
    }

    @Subscribe()
    public void restartForIncreaseStyle(Event.HideInstrument event) {
        ToTradeBusess();
    }

    private void ToTradeBusess() {
        getChildFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_right_in,
                        R.anim.slide_left_out,
                        R.anim.slide_left_in,
                        R.anim.slide_right_out
                ).replace(R.id.fl_trade_change, tradeBusinessFragment)
                .addToBackStack(null)
                .commit();
    }
}

package com.coin.exchange.view.fragment.trade.business;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coin.exchange.context.AppApplication;
import com.coin.exchange.R;
import com.coin.exchange.cache.PreferenceManager;
import com.coin.exchange.model.bitMex.request.OrderReq;
import com.coin.exchange.model.okex.request.FuturesOrderReq;
import com.coin.exchange.mvp.TradeBusiness.TradeBusinessFragment;
import com.coin.exchange.view.fragment.trade.position.PositionInputFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * @author dean
 * @date 创建时间：2018/11/22
 * @description 确认对话框fragment
 */
public class TradeConfirmFragment extends PositionInputFragment implements View.OnClickListener {

    @BindView(R.id.ll_content)
    LinearLayout llContent;

    @BindView(R.id.tv_coin)
    TextView tvCoin;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_lever)
    TextView tvLever;
    @BindView(R.id.tv_prise)
    TextView tvPrise;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.tv_trade_type)
    TextView tvTradeType;
    @BindView(R.id.tv_next_show)
    TextView tvNextShow;
    @BindView(R.id.ll_trade)
    LinearLayout llTrade;

    private ActionListener mListener;

    private FuturesOrderReq futuresOrderReq; //okex 的数据
    private OrderReq orderReq; //bitmex 的数据
    private String time;
    @Inject
    PreferenceManager preferenceManager;

    private boolean isClickNext = false;

    @Override
    protected void initArgs(Bundle arguments) {

    }

    public void update(FuturesOrderReq mFuturesOrderReq, OrderReq morderReq, String mTime) {
        futuresOrderReq = mFuturesOrderReq;
        orderReq = morderReq;
        time = mTime;
    }

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AppApplication.getAppComponent().inject(this);
        return inflater.inflate(R.layout.dialog_trade_confirm, container, false);
    }

    @Override
    protected void initView(View view) {
        tvTime.setText(time);
        if (futuresOrderReq != null) {
            tvCoin.setText(futuresOrderReq.getInstrument_id().substring(0, 3));
            tvLever.setText(futuresOrderReq.getLeverage() + "×");
            if (futuresOrderReq.getType().equals("1") || futuresOrderReq.getType().equals("4")) {
                llTrade.setBackgroundResource(R.drawable.green_round_bg);
                tvPrise.setTextColor(getResources().getColor(R.color.green));
                tvNumber.setTextColor(getResources().getColor(R.color.green));
                tvTradeType.setTextColor(getResources().getColor(R.color.green));
                if (futuresOrderReq.getType().equals("1")) {
                    tvTradeType.setText("买入开多");
                } else {
                    tvTradeType.setText("买入平空");
                }
            } else {
                llTrade.setBackgroundResource(R.drawable.red_round_bg);
                tvPrise.setTextColor(getResources().getColor(R.color.confirm_red));
                tvNumber.setTextColor(getResources().getColor(R.color.confirm_red));
                tvTradeType.setTextColor(getResources().getColor(R.color.confirm_red));
                if (futuresOrderReq.getType().equals("2")) {
                    tvTradeType.setText("卖出开空");
                } else {
                    tvTradeType.setText("卖出平多");
                }
            }
            tvPrise.setText(futuresOrderReq.getPrice());
            tvNumber.setText("  ×" + futuresOrderReq.getSize());
        }

        if (orderReq != null) {
            tvCoin.setText(orderReq.getSymbol().substring(0, 3));
            tvLever.setText("");
            if (orderReq.getSide().equals(TradeBusinessFragment.BUY)) {
                llTrade.setBackgroundResource(R.drawable.green_round_bg);
                tvPrise.setTextColor(getResources().getColor(R.color.green));
                tvNumber.setTextColor(getResources().getColor(R.color.green));
                tvTradeType.setTextColor(getResources().getColor(R.color.green));
                if (orderReq.isOpen()) {
                    tvTradeType.setText("买入开多");
                } else {
                    tvTradeType.setText("买入平空");
                }
            } else {
                llTrade.setBackgroundResource(R.drawable.red_round_bg);
                tvPrise.setTextColor(getResources().getColor(R.color.confirm_red));
                tvNumber.setTextColor(getResources().getColor(R.color.confirm_red));
                tvTradeType.setTextColor(getResources().getColor(R.color.confirm_red));
                if (orderReq.isOpen()) {
                    tvTradeType.setText("卖出开空");
                } else {
                    tvTradeType.setText("卖出平多");
                }
            }
            tvPrise.setText(orderReq.getPrice());
            tvNumber.setText("  ×" + orderReq.getOrderQty());
        }

    }

    @Override
    protected ViewGroup getRootView() {
        return llContent;
    }

    @Override
    public void onClick(String clickNum) {

    }

    @OnClick({R.id.tv_cancel, R.id.tv_next_show, R.id.tv_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_next_show:
                if (isClickNext) {
                    isClickNext = false;
                    tvNextShow.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.dialog_trade_confirm), null, null, null);
                } else {
                    isClickNext = true;
                    tvNextShow.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.dialog_trade_confirm_ok), null, null, null);
                }
                break;
            case R.id.tv_confirm:
                if (isClickNext) {
                    preferenceManager.putBoolean(PreferenceManager.IS_SHOW_TRADE_DIALOG, true);
                }
                if (mListener != null) {
                    dismiss();
                    mListener.onViewClick();
                }
                break;
        }
    }

    public void setListener(ActionListener listener) {
        this.mListener = listener;
    }

    public interface ActionListener {
        void onViewClick();
    }
}

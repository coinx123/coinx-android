package com.coin.exchange.view.fragment.trade.position;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coin.exchange.R;
import com.coin.exchange.adapter.NumbersAdapter;
import com.coin.exchange.utils.AppUtils;
import com.coin.libbase.utils.ToastUtil;
import com.coin.libbase.view.fragment.dialog.JBaseDialogFragment;

import butterknife.BindView;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/21
 * @description 持仓——平仓框
 */
public class PositionSellFragment extends PositionInputFragment
        implements View.OnClickListener, NumbersAdapter.ClickListener {

    private static final int PRICE = 1;
    private static final int SELL = 2;

    private static final String DOT = ".";

    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.tv_cancle)
    TextView tvCancel;
    @BindView(R.id.tv_price_name)
    TextView tvPriceName;
    @BindView(R.id.tv_price_num)
    TextView tvPriceNum;
    @BindView(R.id.iv_price_clear)
    ImageView ivPriceClear;
    @BindView(R.id.ll_price)
    LinearLayout llPrice;
    @BindView(R.id.tv_sell_name)
    TextView tvSellName;
    @BindView(R.id.tv_sell_num)
    TextView tvSellNum;
    @BindView(R.id.iv_sell_clear)
    ImageView ivSellClear;
    @BindView(R.id.ll_sell)
    LinearLayout llSell;
    @BindView(R.id.tv_market_sell)
    TextView tvMarketSell;
    @BindView(R.id.tv_common_sell)
    TextView tvCommonSell;
    @BindView(R.id.gv_key_board)
    GridView gvKeyBoard;

    // 当前选中的输入项
    private int curType = PRICE;

    private SellClickListener mSellClickListener;

    // 合约Id
    private String mInsId;
    // 1:开多2:开空3:平多4:平空
    private String mType;
    // 每张合约价格
    private String mPrice;
    // 数量（张）
    private String mSize;
    // 杠杆
    private String mLeverage;
    // 选择的位置
    private int mPosition;
    // 平台
    private String mPlatform;

    @Override
    protected void initArgs(Bundle arguments) {

    }

    public void setInfo(String insId,
                        String type,
                        String price,
                        String size,
                        String leverage,
                        int position,
                        String platform,
                        SellClickListener listener) {
        mInsId = insId;
        mType = type;
        mPrice = price;
        mSize = size;
        mLeverage = leverage;
        mPosition = position;
        mPlatform = platform;
        mSellClickListener = listener;
    }

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_sell, container, false);
    }

    @Override
    protected void initView(View view) {
        gvKeyBoard.setAdapter(new NumbersAdapter(getContext(), this));

        llPrice.setOnClickListener(this);
        llSell.setOnClickListener(this);
        ivSellClear.setOnClickListener(this);
        ivPriceClear.setOnClickListener(this);
        tvCancel.setOnClickListener(this);

        tvPriceNum.setText(mPrice);
        tvSellNum.setText(mSize);

        tvCommonSell.setOnClickListener(this);
        tvMarketSell.setOnClickListener(this);

        clickForInput(curType);
    }

    private void clickForInput(int type) {
        if (getContext() == null) {
            return;
        }

        switch (type) {
            case PRICE:

                llPrice.setSelected(true);
                tvPriceName.setTextColor(ContextCompat.getColor(getContext(), R.color.position_sell_dialog_select_color));

                llSell.setSelected(false);
                tvSellName.setTextColor(ContextCompat.getColor(getContext(), R.color.position_sell_dialog_unselect_color));

                break;
            case SELL:
                llPrice.setSelected(false);
                tvPriceName.setTextColor(ContextCompat.getColor(getContext(), R.color.position_sell_dialog_unselect_color));

                llSell.setSelected(true);
                tvSellName.setTextColor(ContextCompat.getColor(getContext(), R.color.position_sell_dialog_select_color));
                break;
            default:
                return;
        }
        curType = type;
        // 为价格时才接受小数点
        mIsAcceptDot = curType == PRICE;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_sell_clear:
                tvSellNum.setText("0");
                clickForInput(SELL);
                break;
            case R.id.iv_price_clear:
                tvPriceNum.setText("0");
                clickForInput(PRICE);
                break;
            case R.id.ll_sell:
                clickForInput(SELL);
                break;
            case R.id.ll_price:
                clickForInput(PRICE);
                break;
            case R.id.tv_cancle:
                dismiss();
                break;
            case R.id.tv_common_sell:
                if (mPlatform.equals(AppUtils.BITMEX)) {
                    String priceString = tvPriceNum.getText().toString().trim();
                    double price = Double.parseDouble(priceString);
                    double remainder = price % 0.5d;
                    if (remainder != 0) { //不能被0.5，则中断，并提示
                        ToastUtil.show("价格必须是0.5的倍数");
                        return;
                    }
                }

                if (mSellClickListener != null) {
                    String price = tvPriceNum.getText().toString().trim();
                    if (price.endsWith(DOT)) {
                        price = price.substring(0, price.length() - 1);
                    }
                    mSellClickListener.onSellNormal(mInsId,
                            mType,
                            price,
                            tvSellNum.getText().toString().trim(),
                            mLeverage,
                            mPosition);
                    dismiss();
                }
                break;
            case R.id.tv_market_sell:
                if (mSellClickListener != null) {
                    dismiss();
                    mSellClickListener.onSellAllWithMarketPrice(mInsId,
                            mType,
                            tvSellNum.getText().toString().trim(),
                            mLeverage,
                            mPosition);
                }
                break;
        }
    }

    @Override
    public void onClick(String key) {
        String content = null;
        TextView curTextView = null;
        switch (curType) {
            case PRICE:
                content = tvPriceNum.getText().toString().trim();
                curTextView = tvPriceNum;
                break;
            case SELL:
                content = tvSellNum.getText().toString().trim();
                curTextView = tvSellNum;
                break;
        }

        checkInput(curTextView, key, content);
    }

    @Override
    protected ViewGroup getRootView() {
        return llContent;
    }

    public interface SellClickListener {

        // 市场全平
        void onSellAllWithMarketPrice(String insId,
                                      String type,
                                      String size,
                                      String leverage,
                                      int position);

        // 平仓
        void onSellNormal(String insId,
                          String type,
                          String price,
                          String size,
                          String leverage,
                          int position);

    }

}

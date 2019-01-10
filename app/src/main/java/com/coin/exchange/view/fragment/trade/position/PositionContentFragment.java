package com.coin.exchange.view.fragment.trade.position;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.coin.exchange.context.AppApplication;
import com.coin.exchange.R;
import com.coin.exchange.adapter.PositionAdapter;
import com.coin.exchange.di.component.DaggerPositionContentComponent;
import com.coin.exchange.di.module.PositionContentModule;
import com.coin.exchange.model.okex.vo.PositionItemVO;
import com.coin.exchange.presenter.PositionContentPresenter;
import com.coin.exchange.utils.AppUtils;
import com.coin.libbase.utils.ToastUtil;
import com.coin.libbase.view.fragment.JListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/21
 * @description 交易界面 —— 持仓fragment —— 内容Fragment
 */

public class PositionContentFragment extends JListFragment<PositionContentPresenter>
        implements PositionContentView,
        PositionAdapter.ClickListener,
        PositionSellFragment.SellClickListener,
        PositionSecurityFragment.PosSecurityListener {

    private static final String MARKET = "Market";
    private static final String LIMIT = "Limit";

    public static final String BUY = "Buy";
    public static final String SELL = "Sell";

    // 平台
    private String mPlatform;

    private PositionAdapter mAdapter;
    private List<PositionItemVO> allData = new ArrayList<>();
    private List<PositionItemVO> mData = new ArrayList<>();

    private PositionSellFragment mSellDialog;
    private PositionSecurityFragment mSecurityDialog;

    public static PositionContentFragment newInstance(String type) {
        Bundle bundle = new Bundle();
        bundle.putString(AppUtils.PLATFORM, type);
        PositionContentFragment positionContentFragment = new PositionContentFragment();
        positionContentFragment.initArgs(bundle);
        return positionContentFragment;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);

        mSellDialog = new PositionSellFragment();
        mSecurityDialog = new PositionSecurityFragment();
    }

    @Override
    protected void initArgs(Bundle arguments) {
        super.initArgs(arguments);
        mPlatform = arguments.getString(AppUtils.PLATFORM);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        mAdapter = new PositionAdapter(getContext(), mData, this);
        return mAdapter;
    }

    @Override
    public void getFirstData() {
        switch (mPlatform) {
            case AppUtils.BITMEX:
                mPresenter.getBitMexPositionInfo();
                break;
            case AppUtils.OKEX:
                mPresenter.getFutureInfo();
                break;
        }
    }

    @Override
    protected boolean requestRefresh() {
        return true;
    }

    @Override
    public void onGetType(List<String> typeList) {
        if (getParentFragment() instanceof PositionFragment) {
            ((PositionFragment) getParentFragment()).setTypeList(typeList);
        }
    }

    @Override
    public void onGetFuturesPosition(List<PositionItemVO> positionItemVOList) {
        getRootAdapter().onSuccess();
        allData.clear();
        allData.addAll(positionItemVOList);
        if (allData.size() <= 0) {
            getRootAdapter().onEmpty();
        } else {
            mData.clear();
            mData.addAll(allData);
            getRootAdapter().notifyDataSetChanged();
        }
    }


    @Override
    public void onGetFuturesPositionError() {
        getRootAdapter().onError();
    }

    @Override
    public void onPostSellSuc(int position) {
        hideDialog();

        mData.clear();
        getRootAdapter().onLoading();
        getFirstData();

    }

    @Override
    public void onPostSellError(String msg) {
        hideDialog();
        ToastUtil.show(msg);
    }

    @Override
    public void onAdjustSecError(String msg) {
        hideDialog();
        ToastUtil.show(msg);
    }

    @Override
    public void onAdjustSec() {
        hideDialog();

        mData.clear();
        getRootAdapter().onLoading();
        getFirstData();
    }

    @Override
    protected void registerDagger() {
        DaggerPositionContentComponent
                .builder()
                .appComponent(AppApplication.getAppComponent())
                .positionContentModule(new PositionContentModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void clickSell(int position) {
        PositionItemVO item = mData.get(position);
        mSellDialog.setInfo(item.getInsId(),
                item.getType(),
                item.getCurPrice(),
                item.getSell(),
                item.getLeverage(),
                position,
                mPlatform,
                this);
        mSellDialog.show(this);
    }

    @Override
    public void clickSettingSecurity(int position) {
        PositionItemVO item = allData.get(position);
        mSecurityDialog.setInfo(this,
                mPlatform,
                item.getInsId(),
                getString(R.string.position_info, item.getPosition(), item.getType(), item.getLeverage() + "X"),
                item.getSecurity() + "XBT",
                item.getCanIncreaseSecurity() + "XBT",
                item.getCanDecreaseSecurity() + "XBT");
        mSecurityDialog.show(this);
    }

    public void setType(String type) {
        mData.clear();
        if (type.equals(PositionFragment.ALL_TYPE)) {
            mData.addAll(allData);
        } else {
            for (PositionItemVO item : allData) {
                if (item.getTitleName().startsWith(type)) {
                    mData.add(item);
                }
            }
        }

        if (mData.isEmpty()) {
            getRootAdapter().onEmpty();
        } else {
            getRootAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onSellAllWithMarketPrice(final String insId,
                                         final String type,
                                         final String size,
                                         final String leverage,
                                         final int position) {

        if (getContext() == null) {
            return;
        }

        if (mPlatform.equals(AppUtils.OKEX)) {
            showOkExMarketPrice(insId, type, size, leverage, position);
        } else if (mPlatform.equals(AppUtils.BITMEX)) {
            showBitMEXMarketPrice(insId, type, size, leverage, position);
        }


    }

    @Override
    public void onSellNormal(final String insId,
                             final String type,
                             final String price,
                             final String size,
                             final String leverage,
                             final int position) {

        if (getContext() == null) {
            return;
        }

        if (mPlatform.equals(AppUtils.OKEX)) {
            showNormalOkExSellDialog(insId, type, price, size, leverage, position);
        } else if (mPlatform.equals(AppUtils.BITMEX)) {
            showNormalBitMEXSellDialog(insId, type, price, size, leverage, position);
        }

    }

    private void showNormalOkExSellDialog(final String insId,
                                          final String type,
                                          final String price,
                                          final String size,
                                          final String leverage,
                                          final int position) {

        new MaterialDialog.Builder(getContext())
                .title("市价全平提示")
                .content("合约类型：" + insId + "\r\n"
                        + "类型：" + type + "\r\n"
                        + "价格：" + price + "\r\n"
                        + "数量（张）：" + size + "\r\n"
                        + "杠杆：" + leverage)
                .negativeColor(ContextCompat.getColor(getContext(), R.color.main_text_color))
                .positiveColor(ContextCompat.getColor(getContext(), R.color.blue))
                .positiveText("取消")
                .negativeText("确定")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        showDialog();
                        String realType;
                        if (type.equals(PositionContentPresenter.INCREASE)) {
                            realType = "3";
                        } else {
                            realType = "4";
                        }
                        mPresenter.postSell(insId, realType, price, size, "0", leverage, position);
                    }
                })
                .show();

    }

    private void showNormalBitMEXSellDialog(final String insId,
                                            final String type,
                                            final String price,
                                            final String size,
                                            final String leverage,
                                            final int position) {

        new MaterialDialog.Builder(getContext())
                .title("市价全平提示")
                .content("合约类型：" + insId + "\r\n"
                        + "类型：" + type + "\r\n"
                        + "价格：" + price + "\r\n"
                        + "数量（张）：" + size + "\r\n"
                        + "杠杆：" + leverage)
                .negativeColor(ContextCompat.getColor(getContext(), R.color.main_text_color))
                .positiveColor(ContextCompat.getColor(getContext(), R.color.blue))
                .positiveText("取消")
                .negativeText("确定")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        showDialog();
                        if (type.equals(PositionContentPresenter.INCREASE)) {
                            mPresenter.postBitMEXSell(insId, size, price, LIMIT, SELL, position);
                        } else {
                            mPresenter.postBitMEXSell(insId, size, price, LIMIT, BUY, position);
                        }
                    }
                })
                .show();

    }

    private void showOkExMarketPrice(final String insId,
                                     final String type,
                                     final String size,
                                     final String leverage,
                                     final int position) {
        new MaterialDialog.Builder(getContext())
                .title("市价全平提示")
                .content("合约类型：" + insId + "\r\n"
                        + "类型：" + type + "\r\n"
                        + "数量（张）：" + size + "\r\n"
                        + "杠杆：" + leverage)
                .negativeColor(ContextCompat.getColor(getContext(), R.color.main_text_color))
                .positiveColor(ContextCompat.getColor(getContext(), R.color.blue))
                .positiveText("取消")
                .negativeText("确定")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        showDialog();
                        String realType;
                        if (type.equals(PositionContentPresenter.INCREASE)) {
                            realType = "3";
                        } else {
                            realType = "4";
                        }
                        mPresenter.postSell(insId, realType, "999999999", size, "1", leverage, position);
                    }
                })
                .show();
    }

    private void showBitMEXMarketPrice(final String insId,
                                       final String type,
                                       final String size,
                                       final String leverage,
                                       final int position) {
        new MaterialDialog.Builder(getContext())
                .title("市价全平提示")
                .content("合约类型：" + insId + "\r\n"
                        + "类型：" + type + "\r\n"
                        + "数量（张）：" + size + "\r\n"
                        + "杠杆：" + leverage)
                .negativeColor(ContextCompat.getColor(getContext(), R.color.main_text_color))
                .positiveColor(ContextCompat.getColor(getContext(), R.color.blue))
                .positiveText("取消")
                .negativeText("确定")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        showDialog();
                        if (type.equals(PositionContentPresenter.INCREASE)) {
                            mPresenter.postBitMEXSell(insId, size, null, MARKET, SELL, position);
                        } else {
                            mPresenter.postBitMEXSell(insId, size, null, MARKET, BUY, position);
                        }
                    }
                })
                .show();
    }

    @Override
    public void onChangeSecCommit(String platform,
                                  final String id,
                                  boolean isInc,
                                  String posInfo,
                                  String assignSec,
                                  String adjustSec,
                                  final String security) {
        Log.i(TAG, "onChangeSecCommit: [platform: " + platform +
                "; id:" + id +
                "; security:" + security + "]");

        new MaterialDialog.Builder(getContext())
                .title("调整保证金提示")
                .content("合约类型：" + id + "\r\n"
                        + "仓位信息：" + posInfo + "\r\n"
                        + "已分配保证金：" + assignSec + "\r\n"
                        + (isInc ? "增加保证金：" : "减少保证金：") + adjustSec + "XBT")
                .negativeColor(ContextCompat.getColor(getContext(), R.color.main_text_color))
                .positiveColor(ContextCompat.getColor(getContext(), R.color.blue))
                .positiveText("取消")
                .negativeText("确定")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        showDialog();
                        mPresenter.postAdjustSecurity(id, security);
                    }
                })
                .show();
    }
}

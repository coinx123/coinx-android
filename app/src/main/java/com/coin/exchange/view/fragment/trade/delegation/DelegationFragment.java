package com.coin.exchange.view.fragment.trade.delegation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coin.exchange.context.AppApplication;
import com.coin.exchange.R;
import com.coin.exchange.di.component.DaggerDelegationComponent;
import com.coin.exchange.di.module.DelegationModule;
import com.coin.exchange.model.okex.response.FuturesInstrumentsTickerList;
import com.coin.exchange.presenter.DelegationPresenter;
import com.coin.exchange.utils.AppUtils;
import com.coin.exchange.utils.IconInfoUtils;
import com.coin.exchange.view.TradeActivity;
import com.coin.exchange.view.fragment.trade.position.WheelSelectFragment;
import com.coin.libbase.view.fragment.JBaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/26
 * @description 交易界面 —— 委托fragment(OKEX)
 */

public class DelegationFragment extends JBaseFragment<DelegationPresenter>
        implements DelegationView, View.OnClickListener {

    // 未成交
    public static final String WAITING = "0";
    // 已成交
    public static final String DONE = "2";
    // 已撤销
    public static final String CANCEL = "-1";

    @BindView(R.id.frame_layout)
    FrameLayout frameLayout;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.ll_name)
    LinearLayout llName;
    @BindView(R.id.tv_name_des)
    TextView tvNameDes;
    @BindView(R.id.ll_name_des)
    LinearLayout llNameDes;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.ll_status)
    LinearLayout llStatus;

    private WheelSelectFragment wheelSelectFragment;
    private WheelSelectFragment.OnSelectedListener statusListener;
    private WheelSelectFragment.OnSelectedListener coinNameListener;
    private WheelSelectFragment.OnSelectedListener coinNameDesListener;

    private final Map<String, String> mStatusMap = new HashMap<>();
    private final List<String> mStatusList = new ArrayList<>();

    private final List<String> mCoinNameList = new ArrayList<>();
    private final List<String> mCoinNameDesList = new ArrayList<>();

    public static Fragment newInstance() {
        return new DelegationFragment();
    }

    private final Map<String, List<FuturesInstrumentsTickerList>> mSelectType = new HashMap<>();
    private DelegationContentFragment mFragment;

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_delega, container, false);
    }

    @Override
    protected void registerDagger() {
        DaggerDelegationComponent
                .builder()
                .appComponent(AppApplication.getAppComponent())
                .delegationModule(new DelegationModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initView(View view) {

        wheelSelectFragment = new WheelSelectFragment();

        initListener();
        initSelectList();

        if (getActivity() instanceof TradeActivity) {
            TradeActivity activity = (TradeActivity) getActivity();
            String insId = activity.getInsId();
            String type = activity.getType();

            if (type.length() >= 2) {
                type = type.substring(0, 2);
            }

            if (TextUtils.isEmpty(insId)) {
                return;
            }

            String rootName = IconInfoUtils.getRootName(insId);

            mCoinNameList.add(rootName);
            mCoinNameDesList.add(type);

            String status = mStatusList.get(0);
            tvName.setText(rootName);
            tvNameDes.setText(type);
            tvStatus.setText(mStatusList.get(0));

            List<FuturesInstrumentsTickerList> list = new ArrayList<>();
            FuturesInstrumentsTickerList curItem = new FuturesInstrumentsTickerList();
            curItem.setInstrument_id(insId);
            curItem.setRootName(rootName);
            curItem.setType(type);
            list.add(curItem);
            mSelectType.put(rootName, list);

            mFragment = DelegationContentFragment.newInstance(insId, mStatusMap.get(status), type, AppUtils.OKEX);
            addLayerFragment(frameLayout.getId(), mFragment);
        }

        llName.setOnClickListener(this);
        llNameDes.setOnClickListener(this);
        llStatus.setOnClickListener(this);

        mPresenter.getIconInfo();

    }

    private void initSelectList() {
        mStatusList.add("未交易");
        mStatusList.add("已交易");
        mStatusList.add("已撤销");

        mStatusMap.put("未交易", WAITING);
        mStatusMap.put("已交易", DONE);
        mStatusMap.put("已撤销", CANCEL);
    }

    private void initListener() {
        statusListener = new WheelSelectFragment.OnSelectedListener() {
            @Override
            public void getData(int index, String data) {
                Log.i(TAG, "onItemSelected: status=" + data);
                tvStatus.setText(data);
                sendReq();
            }
        };
        coinNameListener = new WheelSelectFragment.OnSelectedListener() {
            @Override
            public void getData(int index, String data) {
                Log.i(TAG, "onItemSelected: coinName=" + data);
                tvName.setText(data);

                List<FuturesInstrumentsTickerList> list = mSelectType.get(data);

                mCoinNameDesList.clear();
                for (FuturesInstrumentsTickerList item : list) {
                    mCoinNameDesList.add(item.getType());
                }

                if (mCoinNameDesList.size() <= 0) {
                    return;
                }

                // 默认选中第一个
                tvNameDes.setText(mCoinNameDesList.get(0));

                sendReq();
            }
        };

        coinNameDesListener = new WheelSelectFragment.OnSelectedListener() {
            @Override
            public void getData(int index, String data) {
                Log.i(TAG, "onItemSelected: coinNameDes=" + data);
                tvNameDes.setText(data);
                sendReq();
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_name:
                showSelectDialog(mCoinNameList,
                        tvName.getText().toString().trim(),
                        coinNameListener);
                break;

            case R.id.ll_name_des:
                showSelectDialog(mCoinNameDesList,
                        tvNameDes.getText().toString().trim(),
                        coinNameDesListener);
                break;

            case R.id.ll_status:
                showSelectDialog(mStatusList,
                        tvStatus.getText().toString().trim(),
                        statusListener);
                break;
        }
    }

    private void showSelectDialog(List<String> data,
                                  String selectContent,
                                  WheelSelectFragment.OnSelectedListener listener) {
        int position = 0;
        for (int index = 0; index < data.size(); ++index) {
            if (data.get(index).equals(selectContent)) {
                position = index;
            }
        }

        wheelSelectFragment.setData(data);
        wheelSelectFragment.setSelectPosition(position);
        wheelSelectFragment.setOnSelectedListener(listener);
        wheelSelectFragment.show(this);
    }

    @Override
    public void onGetIconInfo(Map<String, List<FuturesInstrumentsTickerList>> map) {
        if (map.size() <= 0) {
            return;
        }

        mSelectType.clear();
        mSelectType.putAll(map);

        mCoinNameList.clear();
        mCoinNameList.addAll(map.keySet());

        mCoinNameDesList.clear();
        List<FuturesInstrumentsTickerList> list = map.get(tvName.getText().toString().trim());
        try {
            for (FuturesInstrumentsTickerList item : list) {
                mCoinNameDesList.add(item.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发起请求
     */
    private void sendReq() {
        String rootName = tvName.getText().toString().trim();
        String desName = tvNameDes.getText().toString().trim();
        String status = tvStatus.getText().toString().trim();

        List<FuturesInstrumentsTickerList> list = mSelectType.get(rootName);

        if (list == null) {
            return;
        }

        String insId = null;
        for (FuturesInstrumentsTickerList item : list) {
            if (item.getType() != null && item.getType().equals(desName)) {
                insId = item.getInstrument_id();
                break;
            }
        }

        if (insId == null) {
            return;
        }

        if (mFragment != null) {
            mFragment.sendReq(insId, mStatusMap.get(status), tvNameDes.getText().toString().trim());
        }

    }

}

package com.coin.exchange.view.fragment.trade.position;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coin.exchange.R;
import com.coin.exchange.utils.AppUtils;
import com.coin.libbase.view.fragment.JBaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/21
 * @description 交易界面 —— 持仓fragment
 */

public class PositionFragment extends JBaseFragment implements WheelSelectFragment.OnSelectedListener {

    public static final String ALL_TYPE = "全部";

    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.ll_type)
    LinearLayout llType;
    @BindView(R.id.frame_layout)
    FrameLayout frameLayout;

    private List<String> mTypeList;

    private PositionContentFragment mFragment;
    private WheelSelectFragment mTypeFragment;

    private String mPlatform;

    public static PositionFragment newInstance(String platform) {

        Bundle bundle = new Bundle();
        bundle.putString(AppUtils.PLATFORM, platform);

        PositionFragment positionFragment = new PositionFragment();
        positionFragment.initArgs(bundle);

        return positionFragment;
    }

    @Override
    protected void initArgs(Bundle arguments) {
        super.initArgs(arguments);
        mPlatform = arguments.getString(AppUtils.PLATFORM);
    }

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_position, container, false);
    }

    @Override
    protected void initView(View view) {
        mTypeList = new ArrayList<>();
        mTypeList.add("全部");

        mTypeFragment = new WheelSelectFragment();
        mTypeFragment.setData(mTypeList);
        mTypeFragment.setOnSelectedListener(this);

        mFragment = PositionContentFragment.newInstance(mPlatform);
        addLayerFragment(frameLayout.getId(), mFragment);

        llType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTypeFragment.show(PositionFragment.this);
            }
        });
    }

    public void setTypeList(List<String> typeList) {
        mTypeList.clear();
        mTypeList.add(ALL_TYPE);
        mTypeList.addAll(typeList);
        tvType.setText(ALL_TYPE);
    }

    @Override
    public void getData(int index, String data) {
        tvType.setText(data);
        mFragment.setType(data);
    }
}

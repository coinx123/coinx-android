package com.coin.exchange.view.fragment.trade.position;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coin.exchange.R;
import com.coin.exchange.adapter.NumbersAdapter;
import com.coin.exchange.widget.SliceView;

import butterknife.BindView;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/22
 * @description 持仓——调整保证金
 */
public class PositionSecurityFragment extends PositionInputFragment
        implements SliceView.ClickListener, NumbersAdapter.ClickListener, View.OnClickListener {
    @BindView(R.id.tv_cancle)
    TextView tvCancel;
    @BindView(R.id.slice_view)
    SliceView sliceView;
    @BindView(R.id.tv_security_name)
    TextView tvSecurityName;
    @BindView(R.id.tv_security_num)
    TextView tvSecurityNum;
    @BindView(R.id.iv_security_clear)
    ImageView ivSecurityClear;
    @BindView(R.id.ll_security)
    LinearLayout llSecurity;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.tv_sec_has)
    TextView tvSecHas;
    @BindView(R.id.tv_sec_can_control)
    TextView tvSecCanControl;
    @BindView(R.id.tv_commit)
    TextView tvCommit;
    @BindView(R.id.gv_key_board)
    GridView gvKeyBoard;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.tv_security_setting_title)
    TextView tvSecuritySettingTitle;

    private String platform;
    private String id;
    private String posInfo;
    private String assignSec;
    private String adjustIncSec;
    private String adjustDecSec;

    private PosSecurityListener listener;

    // 是否选中增加，默认选中
    private boolean isInc = true;

    public static PositionSecurityFragment newInstance() {
        Bundle bundle = new Bundle();

        PositionSecurityFragment positionSecurityFragment = new PositionSecurityFragment();
        positionSecurityFragment.initArgs(bundle);

        return positionSecurityFragment;
    }

    @Override
    protected void initArgs(Bundle arguments) {
    }

    public void setInfo(PosSecurityListener listener,
                        String platform,
                        String id,
                        String positionInfo,
                        String assignSecurity,
                        String adjustIncreaseSecurity,
                        String adjustDecreaseSecurity) {

        this.listener = listener;
        this.platform = platform;
        this.id = id;
        this.posInfo = positionInfo;
        this.assignSec = assignSecurity;
        this.adjustIncSec = adjustIncreaseSecurity;
        this.adjustDecSec = adjustDecreaseSecurity;
        this.isInc = true;

    }

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_security, container, false);
    }

    @Override
    protected void initView(View view) {
        sliceView.setListener(this);
        gvKeyBoard.setAdapter(new NumbersAdapter(getContext(), this));

        // 仓位信息
        tvInfo.setText(posInfo);
        // 已分配的保证金
        tvSecHas.setText(assignSec);
        // 可增加的保证金
        tvSecCanControl.setText(adjustIncSec);

        tvCancel.setOnClickListener(this);
        ivSecurityClear.setOnClickListener(this);
        tvCommit.setOnClickListener(this);

    }

    @Override
    protected ViewGroup getRootView() {
        return llContent;
    }

    @Override
    public void onClickSlicePos(int index) {

        switch (index) {
            case SliceView.LEFT:
                isInc = true;
                tvCommit.setText(getString(R.string.position_security_add));
                tvSecuritySettingTitle.setText(getString(R.string.position_security_add_title));
                tvSecCanControl.setText(adjustIncSec);
                break;

            case SliceView.RIGHT:
                isInc = false;
                tvCommit.setText(getString(R.string.position_security_reduce));
                tvSecuritySettingTitle.setText(getString(R.string.position_security_reduce_title));
                tvSecCanControl.setText(adjustDecSec);
                break;
        }

    }

    @Override
    public void onClick(String key) {
        String curText = tvSecurityNum.getText().toString();
        checkInput(tvSecurityNum, key, curText);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancle:
                dismiss();
                break;
            case R.id.iv_security_clear:
                tvSecurityNum.setText("0");
                break;
            case R.id.tv_commit:
                if (listener != null) {
                    String secNum = tvSecurityNum.getText().toString().trim();
                    if (secNum.endsWith(DOT)) {
                        secNum = secNum.substring(0, secNum.length() - 1);
                    }
                    listener.onChangeSecCommit(platform,
                            id,
                            isInc,
                            posInfo,
                            assignSec,
                            secNum,
                            isInc ? secNum : "-" + secNum);
                }
                dismiss();
                break;
        }
    }

    /**
     * 回调
     */
    public interface PosSecurityListener {
        // 提交
        void onChangeSecCommit(String platform,
                               String id,
                               boolean isInc,
                               String posInfo,
                               String assignSec,
                               String adjustSec,
                               String security);
    }
}

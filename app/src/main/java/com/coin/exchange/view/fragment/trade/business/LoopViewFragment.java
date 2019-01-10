package com.coin.exchange.view.fragment.trade.business;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coin.exchange.R;
import com.coin.exchange.view.fragment.trade.position.PositionInputFragment;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * @author dean
 * @date 创建时间：2018/11/22
 * @description 滚轮对话框fragment
 */
public class LoopViewFragment extends PositionInputFragment implements View.OnClickListener {

    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_done)
    TextView tvDone;
    private ActionListener mListener;
    @BindView(R.id.loop_view)
    LoopView loopView;
    private int mIndex = 0;
    private final List<String> list = new ArrayList<>();

    @Override
    protected void initArgs(Bundle arguments) {

    }

    public void update(List<String> mList) {
        mIndex = 0;
        list.clear();
        list.addAll(mList);
    }

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_loop_view, container, false);
    }

    @Override
    protected void initView(View view) {
        loopView.setNotLoop();
        loopView.setItems(list);
        //滚动监听
        loopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                mIndex = index;
            }
        });
    }

    @Override
    protected ViewGroup getRootView() {
        return llContent;
    }

    @Override
    public void onClick(String clickNum) {

    }

    @OnClick({R.id.tv_cancel, R.id.tv_done})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_done:
                if (mListener != null) {
                    dismiss();
                    mListener.onViewClick(mIndex);
                }
                break;
        }
    }

    public void setListener(ActionListener listener) {
        this.mListener = listener;
    }

    public interface ActionListener {
        void onViewClick(int index);
    }
}

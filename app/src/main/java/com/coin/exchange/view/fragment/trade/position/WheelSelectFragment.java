package com.coin.exchange.view.fragment.trade.position;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coin.exchange.R;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;
import com.coin.libbase.view.fragment.dialog.JBaseFloatingFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/24
 * @description
 */
public class WheelSelectFragment extends JBaseFloatingFragment {

    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_done)
    TextView tvDone;
    @BindView(R.id.loop_view)
    LoopView loopView;
    @BindView(R.id.ll_content)
    LinearLayout llContent;

    OnSelectedListener listener;

    List<String> mData = new ArrayList<>();
    private int mPosition = 0;

    @Override
    protected ViewGroup getRootView() {
        return llContent;
    }

    public void setOnSelectedListener(OnSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    protected void initArgs(Bundle arguments) {

    }

    public void setData(List<String> data) {
        this.mData = data;
    }

    public void setSelectPosition(int position) {
        this.mPosition = position;
    }

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_loop_view, container, false);
    }

    @Override
    protected void initView(View view) {
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        loopView.setItems(mData);
        loopView.setNotLoop();
        loopView.setCurrentPosition(mPosition);
        // 滚动监听
        loopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (!TextUtils.isEmpty(mData.get(index))) {
                    Log.i(TAG, "onItemSelected: " + mData.get(index));
                    mPosition = index;
                }
            }
        });


        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    if (!TextUtils.isEmpty(mData.get(mPosition))) {
                        listener.getData(mPosition, mData.get(mPosition));
                    }
                }
                dismiss();
            }
        });

    }

    public interface OnSelectedListener {
        void getData(int index, String data);
    }

}

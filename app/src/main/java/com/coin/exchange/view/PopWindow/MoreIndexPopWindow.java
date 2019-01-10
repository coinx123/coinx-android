package com.coin.exchange.view.PopWindow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.coin.exchange.R;
import com.coin.exchange.utils.AppUtils;
import com.coin.libbase.model.eventbus.Event;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MoreIndexPopWindow {

    @BindView(R.id.tv_MA)
    TextView tvMA;
    @BindView(R.id.tv_BOLL)
    TextView tvBOLL;
    @BindView(R.id.tv_MACD)
    TextView tvMACD;
    @BindView(R.id.tv_KDJ)
    TextView tvKDJ;
    @BindView(R.id.tv_RSI)
    TextView tvRSI;
    @BindView(R.id.tv_WR)
    TextView tvWR;
    @BindView(R.id.tv_gone)
    TextView tvGONE;
    private PopupWindow popWindow;
    private Context context;
    private LayoutInflater inflater;
    private ActionListener mListener;


    public MoreIndexPopWindow(Context context) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setViews();
    }

    private void setViews() {
        View popupView = inflater.inflate(R.layout.more_index_popupwindow, null);
        popWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, AppUtils.dpToPx(80));
        ButterKnife.bind(this, popupView);
        popWindow.setFocusable(true);
        popWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popWindow.setOutsideTouchable(true);
        popWindow.setTouchable(true);
//        popWindow.setAnimationStyle(R.style.menu_popwin_anim_style);
    }

    public void show(View anchor) {
        popWindow.showAsDropDown(anchor);
    }

    public void hide() {
        popWindow.dismiss();
    }

    public boolean isShowing() {  //是否正在显示弹窗
        return popWindow.isShowing();
    }

    public void setListener(ActionListener listener) {
        this.mListener = listener;
    }

    @OnClick({R.id.tv_MA, R.id.tv_BOLL, R.id.tv_MACD, R.id.tv_KDJ, R.id.tv_RSI, R.id.tv_WR, R.id.tv_gone})
    public void onClick(View view) {
        hide();
        mListener.onViewClick(view.getId());
        EventBus.getDefault().post(new Event.KlineIndex(view.getId()));
        switch (view.getId()) {
            case R.id.tv_MA:
                tvMA.setTextColor(context.getResources().getColor(R.color.select_blue));
                tvBOLL.setTextColor(context.getResources().getColor(R.color.time_blue));
                break;
            case R.id.tv_BOLL:
                tvMA.setTextColor(context.getResources().getColor(R.color.time_blue));
                tvBOLL.setTextColor(context.getResources().getColor(R.color.select_blue));
                break;
            case R.id.tv_MACD:
                tvMACD.setTextColor(context.getResources().getColor(R.color.select_blue));
                tvKDJ.setTextColor(context.getResources().getColor(R.color.time_blue));
                tvRSI.setTextColor(context.getResources().getColor(R.color.time_blue));
                tvWR.setTextColor(context.getResources().getColor(R.color.time_blue));
                tvGONE.setTextColor(context.getResources().getColor(R.color.time_blue));
                break;
            case R.id.tv_KDJ:
                tvMACD.setTextColor(context.getResources().getColor(R.color.time_blue));
                tvKDJ.setTextColor(context.getResources().getColor(R.color.select_blue));
                tvRSI.setTextColor(context.getResources().getColor(R.color.time_blue));
                tvWR.setTextColor(context.getResources().getColor(R.color.time_blue));
                tvGONE.setTextColor(context.getResources().getColor(R.color.time_blue));
                break;
            case R.id.tv_RSI:
                tvMACD.setTextColor(context.getResources().getColor(R.color.time_blue));
                tvKDJ.setTextColor(context.getResources().getColor(R.color.time_blue));
                tvRSI.setTextColor(context.getResources().getColor(R.color.select_blue));
                tvWR.setTextColor(context.getResources().getColor(R.color.time_blue));
                tvGONE.setTextColor(context.getResources().getColor(R.color.time_blue));
                break;
            case R.id.tv_WR:
                tvMACD.setTextColor(context.getResources().getColor(R.color.time_blue));
                tvKDJ.setTextColor(context.getResources().getColor(R.color.time_blue));
                tvRSI.setTextColor(context.getResources().getColor(R.color.time_blue));
                tvWR.setTextColor(context.getResources().getColor(R.color.select_blue));
                tvGONE.setTextColor(context.getResources().getColor(R.color.time_blue));
                break;
            case R.id.tv_gone:
                tvMACD.setTextColor(context.getResources().getColor(R.color.time_blue));
                tvKDJ.setTextColor(context.getResources().getColor(R.color.time_blue));
                tvRSI.setTextColor(context.getResources().getColor(R.color.time_blue));
                tvWR.setTextColor(context.getResources().getColor(R.color.time_blue));
                tvGONE.setTextColor(context.getResources().getColor(R.color.select_blue));
                break;
        }
    }

    public interface ActionListener {
        void onViewClick(int id);
    }
}

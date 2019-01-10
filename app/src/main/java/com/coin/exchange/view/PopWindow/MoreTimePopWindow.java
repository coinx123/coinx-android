package com.coin.exchange.view.PopWindow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.coin.exchange.R;
import com.coin.exchange.utils.AppUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class MoreTimePopWindow {

    private PopupWindow popWindow;
    private Context context;
    private LayoutInflater inflater;
    private ActionListener mListener;


    public MoreTimePopWindow(Context context) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setViews();
    }

    private void setViews() {
        View popupView = inflater.inflate(R.layout.more_time_popupwindow, null);
        popWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, AppUtils.dpToPx(40));
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

    @OnClick({R.id.tv_15minute, R.id.tv_30minute, R.id.tv_1week})
    public void onViewClick(View view) {
        hide();
        switch (view.getId()) {
            case R.id.tv_15minute:
                mListener.fifteenMinuteClick();
                break;
            case R.id.tv_30minute:
                mListener.thirtyMinuteClick();
                break;
            case R.id.tv_1week:
                mListener.oneWeekClick();
                break;

        }
    }

    public interface ActionListener {
        void fifteenMinuteClick();

        void thirtyMinuteClick();

        void oneWeekClick();

    }

}

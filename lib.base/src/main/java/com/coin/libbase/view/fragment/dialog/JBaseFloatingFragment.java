package com.coin.libbase.view.fragment.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.coin.libbase.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/24
 * @description
 */
public abstract class JBaseFloatingFragment extends JBaseDialogFragment {

    private Unbinder unbinder;

    private Animation dismissAnim;

    private boolean mIsDismissing = false;

    @Override
    protected void initStyle() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.TranslucentNoTitle);
    }

    @Override
    protected void initWindows(Window window) {
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        super.initWindows(window);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = onCreateFragmentView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void dismiss() {
        if (getRootView() == null) {
            super.dismiss();
            return;
        }

        if (mIsDismissing) {
            return;
        }

        mIsDismissing = true;
        getRootView().startAnimation(dismissAnim);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAnim();
    }

    protected void initAnim() {
        if (getRootView() == null) {
            return;
        }
        getRootView().startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.j_dialog_show_anim));
        dismissAnim = AnimationUtils.loadAnimation(getContext(), R.anim.j_dialog_dismiss_anim);
        dismissAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                JBaseFloatingFragment.super.dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                dismiss();
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });

        mIsDismissing = false;
    }

    /**
     * 获取需要动画的视图
     *
     * @return
     */
    protected abstract ViewGroup getRootView();

}

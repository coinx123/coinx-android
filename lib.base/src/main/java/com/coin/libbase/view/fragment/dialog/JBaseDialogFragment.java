package com.coin.libbase.view.fragment.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.coin.libbase.R;
import com.coin.libbase.config.JFrameManager;
import com.coin.libbase.utils.FragmentCompat;

import java.util.List;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/23
 * @description
 */

public abstract class JBaseDialogFragment extends AppCompatDialogFragment {

    public static final int CLOSE = -2;
    public static final int NONE = -1;

    protected final String TAG = Integer.toHexString(System.identityHashCode(this));
    private Bundle mSavedInstanceState;
    private int mAnim = NONE;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle args = getArguments();
        if (args != null) {
            this.initArgs(args);
        }
    }

    protected abstract void initArgs(Bundle arguments);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStyle();
    }

    protected void initStyle(){
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.JFrameDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return this.onCreateFragmentView(inflater, container, savedInstanceState);
    }

    protected abstract View onCreateFragmentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mSavedInstanceState = savedInstanceState;
        initWindows(getDialog().getWindow());
        initView(view);
    }

    protected void initWindows(Window window) {

        if(this.mAnim != CLOSE){
            if (this.mAnim == NONE) {
                //取消默认动画可传NONE
                if (JFrameManager.getInstance().getDialogAnim() != NONE) {
                    window.getAttributes().windowAnimations = JFrameManager.getInstance().getDialogAnim();
                }
            } else {
                window.getAttributes().windowAnimations = this.mAnim;
            }
        }


    }

    protected abstract void initView(View view);

    public JBaseDialogFragment show(FragmentActivity activity) {
        show(activity.getSupportFragmentManager(), TAG);
        return this;
    }

    public JBaseDialogFragment show(Fragment fragment) {
        show(fragment.getChildFragmentManager(), TAG);
        return this;
    }

    public boolean isShowing() {
        return getDialog() != null && getDialog().isShowing();
    }

    public JBaseDialogFragment showWithAllow(FragmentActivity activity) {

        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.add(this, TAG);
        ft.commitAllowingStateLoss();
        return this;

    }

    public void addLayerFragment(int containerId, List<Fragment> fragments) {
        addLayerFragment(containerId, fragments, 0);
    }

    public void addLayerFragment(int containerId, List<Fragment> fragments, int showPosition) {
        if (mSavedInstanceState == null) {
            FragmentCompat.Layer.init(getChildFragmentManager(), containerId, showPosition, fragments);
        } else {
            FragmentCompat.Layer.restoreInstance(getChildFragmentManager(), fragments);
        }
    }

    public void toggleLayerFragment(Fragment from, Fragment to) {
        FragmentCompat.Layer.toggle(getChildFragmentManager(), from, to);
    }

    //设置动画
    public void setAnim(int animSource) {
        this.mAnim = animSource;
    }

    //关闭动画
    public void closeAnim(){
        this.mAnim = CLOSE;
    }

}

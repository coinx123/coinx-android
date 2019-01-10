package com.coin.exchange.mvp;

import com.coin.exchange.context.AppApplication;
import com.coin.exchange.di.component.AppComponent;
import com.coin.exchange.di.scope.ActivityScope;
import com.coin.libbase.presenter.BasePresenter;
import com.coin.libbase.view.IView;
import com.coin.libbase.view.activity.JBaseActivity;
import com.coin.libbase.view.fragment.JBaseFragment;
import com.coin.mvpCreator.utils.ClzNameHelper;
import com.coin.mvpCreator.utils.MVPFactory;

import java.io.IOException;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/12/12
 * @description
 */
public class MyMVPLauncher {

    // 存放的根路径
    private static final String path = "app/src/main/java";
    // 包名
    private static final String pkgName = "com.coin.exchange.mvp";

    public static void main(String[] args) throws IOException {

        // 模块名字（改这里）
        String preFixName = "KLine";
        // 是否为fragment true：为fragment；false：为activity
        boolean isFragment = false;

        ClzNameHelper.getInstance()
                .putClass(ClzNameHelper.VIEW, IView.class)
                .putClass(ClzNameHelper.PRESENTER, BasePresenter.class)
                .putClass(ClzNameHelper.FRAGMENT, JBaseFragment.class)
                .putClass(ClzNameHelper.ACTIVITY, JBaseActivity.class)
                .putClass(ClzNameHelper.SCOPE, ActivityScope.class)
                .putClass(ClzNameHelper.COMPONENT, AppComponent.class)
                .setAppClass(AppApplication.class);

        new MVPFactory(preFixName,
                path,
                pkgName,
                isFragment).launch();

    }

}

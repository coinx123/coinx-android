package com.coin.exchange.di.module;

import com.coin.exchange.di.scope.ActivityScope;
import com.coin.exchange.view.BindView;
import com.coin.exchange.view.fragment.trade.position.PositionContentView;

import dagger.Module;
import dagger.Provides;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/21
 * @description
 */
@Module
public class BindApiModule {

    private BindView bindView;

    public BindApiModule(BindView bindView) {
        this.bindView = bindView;
    }

    @Provides
    @ActivityScope
    public BindView provideBindView() {
        return bindView;
    }

}

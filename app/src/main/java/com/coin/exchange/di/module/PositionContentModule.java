package com.coin.exchange.di.module;

import com.coin.exchange.di.scope.ActivityScope;
import com.coin.exchange.view.fragment.trade.position.PositionContentView;

import dagger.Module;
import dagger.Provides;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/21
 * @description
 */
@Module
public class PositionContentModule {

    private PositionContentView positionContentView;

    public PositionContentModule(PositionContentView positionContentView) {
        this.positionContentView = positionContentView;
    }

    @Provides
    @ActivityScope
    public PositionContentView providePositionContentView() {
        return positionContentView;
    }

}

package com.coin.exchange.di.module;

import com.coin.exchange.di.scope.ActivityScope;
import com.coin.exchange.view.fragment.main.MarketView;

import dagger.Module;
import dagger.Provides;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/15
 * @description
 */
@Module
public class MarketModule {

    private MarketView marketView;

    public MarketModule(MarketView marketView) {
        this.marketView = marketView;
    }

    @Provides
    @ActivityScope
    public MarketView provideMarketView() {
        return marketView;
    }

}

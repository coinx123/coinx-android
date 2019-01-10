package com.coin.exchange.di.module;

import com.coin.exchange.di.scope.ActivityScope;
import com.coin.exchange.view.fragment.trade.delegation.DelegationContentView;
import com.coin.exchange.view.fragment.trade.position.PositionContentView;

import dagger.Module;
import dagger.Provides;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/21
 * @description
 */
@Module
public class DelegationContentModule {

    private DelegationContentView delegationContentView;

    public DelegationContentModule(DelegationContentView delegationContentView) {
        this.delegationContentView = delegationContentView;
    }

    @Provides
    @ActivityScope
    public DelegationContentView provideDelegationContentView() {
        return delegationContentView;
    }

}

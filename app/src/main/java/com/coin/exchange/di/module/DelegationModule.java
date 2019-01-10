package com.coin.exchange.di.module;

import com.coin.exchange.di.scope.ActivityScope;
import com.coin.exchange.view.fragment.trade.delegation.DelegationContentView;
import com.coin.exchange.view.fragment.trade.delegation.DelegationView;

import dagger.Module;
import dagger.Provides;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/21
 * @description
 */
@Module
public class DelegationModule {

    private DelegationView delegationView;

    public DelegationModule(DelegationView delegationView) {
        this.delegationView = delegationView;
    }

    @Provides
    @ActivityScope
    public DelegationView provideDelegationView() {
        return delegationView;
    }

}

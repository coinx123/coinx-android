package com.coin.exchange.di.component;

import com.coin.exchange.di.module.DelegationContentModule;
import com.coin.exchange.di.module.DelegationModule;
import com.coin.exchange.di.scope.ActivityScope;
import com.coin.exchange.view.fragment.trade.delegation.DelegationContentFragment;
import com.coin.exchange.view.fragment.trade.delegation.DelegationFragment;

import dagger.Component;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/15
 * @description
 */

@ActivityScope
@Component(modules = {DelegationModule.class}, dependencies = AppComponent.class)
public interface DelegationComponent {

    void inject(DelegationFragment delegationFragment);

}

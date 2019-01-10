package com.coin.exchange.di.component;

import com.coin.exchange.di.module.BindApiModule;
import com.coin.exchange.di.module.DelegationModule;
import com.coin.exchange.di.scope.ActivityScope;
import com.coin.exchange.view.BindActivity;
import com.coin.exchange.view.fragment.trade.delegation.DelegationFragment;

import dagger.Component;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/15
 * @description
 */

@ActivityScope
@Component(modules = {BindApiModule.class}, dependencies = AppComponent.class)
public interface BindApiComponent {

    void inject(BindActivity bindActivity);

}

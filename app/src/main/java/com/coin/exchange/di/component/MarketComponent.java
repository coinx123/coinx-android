package com.coin.exchange.di.component;

import com.coin.exchange.di.module.MarketModule;
import com.coin.exchange.di.module.TestModule;
import com.coin.exchange.di.scope.ActivityScope;
import com.coin.exchange.view.fragment.main.MarketFragment;

import javax.inject.Inject;

import dagger.Component;
import dagger.Module;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/15
 * @description
 */

@ActivityScope
@Component(modules = {MarketModule.class}, dependencies = AppComponent.class)
public interface MarketComponent {

    void inject(MarketFragment marketFragment);

}

package com.coin.exchange.di.component;

import com.coin.exchange.di.module.TestModule;
import com.coin.exchange.di.scope.ActivityScope;
import com.coin.exchange.view.test.TestActivity;

import dagger.Component;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/15
 * @description
 */

@ActivityScope
@Component(modules = {TestModule.class}, dependencies = AppComponent.class)
public interface TestComponent {

    void inject(TestActivity testActivity);

}

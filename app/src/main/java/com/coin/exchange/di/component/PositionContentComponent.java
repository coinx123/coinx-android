package com.coin.exchange.di.component;

import com.coin.exchange.di.module.PositionContentModule;
import com.coin.exchange.di.scope.ActivityScope;
import com.coin.exchange.view.fragment.trade.position.PositionContentFragment;

import dagger.Component;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/15
 * @description
 */

@ActivityScope
@Component(modules = {PositionContentModule.class}, dependencies = AppComponent.class)
public interface PositionContentComponent {

    void inject(PositionContentFragment positionContentFragment);

}

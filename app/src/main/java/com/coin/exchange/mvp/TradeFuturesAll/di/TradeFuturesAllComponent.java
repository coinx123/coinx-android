package com.coin.exchange.mvp.TradeFuturesAll.di;

import com.coin.exchange.di.component.AppComponent;
import com.coin.exchange.di.scope.ActivityScope;
import com.coin.exchange.mvp.TradeFuturesAll.TradeFuturesAllFragment;
import dagger.Component;

@ActivityScope
@Component(
    modules = {TradeFuturesAllModule.class},
    dependencies = AppComponent.class
)
public interface TradeFuturesAllComponent {
  void inject(TradeFuturesAllFragment tradeFuturesAllFragment);
}

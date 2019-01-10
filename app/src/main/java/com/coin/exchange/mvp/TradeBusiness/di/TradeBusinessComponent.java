package com.coin.exchange.mvp.TradeBusiness.di;

import com.coin.exchange.di.component.AppComponent;
import com.coin.exchange.di.scope.ActivityScope;
import com.coin.exchange.mvp.TradeBusiness.TradeBusinessFragment;
import dagger.Component;

@ActivityScope
@Component(
    modules = {TradeBusinessModule.class},
    dependencies = AppComponent.class
)
public interface TradeBusinessComponent {
  void inject(TradeBusinessFragment tradeBusinessFragment);
}

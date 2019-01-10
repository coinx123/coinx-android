package com.coin.exchange.mvp.TradeBusiness.di;

import com.coin.exchange.di.scope.ActivityScope;
import com.coin.exchange.mvp.TradeBusiness.TradeBusinessView;
import dagger.Module;
import dagger.Provides;

@Module
public final class TradeBusinessModule {
  private final TradeBusinessView mView;

  public TradeBusinessModule(TradeBusinessView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public TradeBusinessView getTradeBusinessView() {
    return this.mView;
  }
}

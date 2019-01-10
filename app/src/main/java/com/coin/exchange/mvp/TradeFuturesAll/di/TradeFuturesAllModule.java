package com.coin.exchange.mvp.TradeFuturesAll.di;

import com.coin.exchange.di.scope.ActivityScope;
import com.coin.exchange.mvp.TradeFuturesAll.TradeFuturesAllView;
import dagger.Module;
import dagger.Provides;

@Module
public final class TradeFuturesAllModule {
  private final TradeFuturesAllView mView;

  public TradeFuturesAllModule(TradeFuturesAllView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public TradeFuturesAllView getTradeFuturesAllView() {
    return this.mView;
  }
}

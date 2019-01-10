package com.coin.exchange.mvp.KLine.di;

import com.coin.exchange.di.scope.ActivityScope;
import com.coin.exchange.mvp.KLine.KLineView;
import dagger.Module;
import dagger.Provides;

@Module
public final class KLineModule {
  private final KLineView mView;

  public KLineModule(KLineView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public KLineView getKLineView() {
    return this.mView;
  }


}

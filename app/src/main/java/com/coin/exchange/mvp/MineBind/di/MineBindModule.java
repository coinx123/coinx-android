package com.coin.exchange.mvp.MineBind.di;

import com.coin.exchange.di.scope.ActivityScope;
import com.coin.exchange.mvp.MineBind.MineBindView;
import dagger.Module;
import dagger.Provides;

@Module
public final class MineBindModule {
  private final MineBindView mView;

  public MineBindModule(MineBindView view) {
    this.mView = view;
  }

  @Provides
  @ActivityScope
  public MineBindView getMineBindView() {
    return this.mView;
  }
}

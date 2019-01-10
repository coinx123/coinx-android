package com.coin.exchange.mvp.MineBind.di;

import com.coin.exchange.di.component.AppComponent;
import com.coin.exchange.di.scope.ActivityScope;
import com.coin.exchange.mvp.MineBind.MineBindFragment;
import dagger.Component;

@ActivityScope
@Component(
    modules = {MineBindModule.class},
    dependencies = AppComponent.class
)
public interface MineBindComponent {
  void inject(MineBindFragment mineBindFragment);
}

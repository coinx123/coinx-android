package com.coin.exchange.mvp.KLine.di;

import com.coin.exchange.di.component.AppComponent;
import com.coin.exchange.di.scope.ActivityScope;
import com.coin.exchange.mvp.KLine.KLineActivity;
import dagger.Component;

@ActivityScope
@Component(
    modules = {KLineModule.class},
    dependencies = AppComponent.class
)
public interface KLineComponent {
  void inject(KLineActivity kLineActivity);
}

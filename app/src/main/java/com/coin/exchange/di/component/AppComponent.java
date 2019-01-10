package com.coin.exchange.di.component;

import android.app.Application;
import android.content.Context;

import com.coin.exchange.context.AppApplication;
import com.coin.exchange.database.CollectionModel;
import com.coin.exchange.di.module.AppModule;
import com.coin.exchange.view.AccountEquityActivity;
import com.coin.exchange.view.MainActivity;
import com.coin.exchange.view.fragment.main.trade.TradeFuturesOptionalFragment;
import com.coin.exchange.view.fragment.trade.business.TradeConfirmFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author dean
 * @date 创建时间：2018/11/8
 * @description
 */

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    Application application();

    Context context();

    CollectionModel provideCollectionModel();

    void inject(AppApplication appApplication);

    void inject(MainActivity mainActivity);

//    void inject(KLineActivity kLineActivity);

//    void inject(TradeFuturesAllFragment tradeOkexAllFragment);

    void inject(TradeFuturesOptionalFragment tradeFuturesOptionalFragment);

    void inject(TradeConfirmFragment tradeConfirmFragment);

//    void inject(TradeBusinessFragment tradeBusinessFragment);

//    void inject(MineBindFragment mineBindFragment);

    void inject(AccountEquityActivity accountEquityActivity);
}

package com.coin.exchange.di.module;

import android.app.Application;
import android.content.Context;

import com.coin.exchange.context.AppApplication;
import com.coin.exchange.database.CollectionDatabase;
import com.coin.exchange.database.CollectionModel;
import com.coin.libbase.presenter.BasePresenter;

import javax.inject.Singleton;

import dagger.Provides;
import dagger.Module;
import io.reactivex.annotations.NonNull;

/**
 * @author dean
 * @date 创建时间：2018/11/8
 * @description
 */
@Module
public class AppModule {
    private final AppApplication mAppApplication;

    public AppModule(AppApplication mAppApplication) {
        this.mAppApplication = mAppApplication;
    }

    @Provides
    public Application provideApplication() {
        return mAppApplication;
    }

    @Provides
    public Context provideContext() {
        return mAppApplication.getApplicationContext();
    }

    @NonNull
    @Provides
    @Singleton
    public CollectionModel provideCollectionModel(CollectionDatabase database) {
        return database;
    }

    @Provides
    public BasePresenter provideBasePresenter() {
        return new BasePresenter();
    }

}

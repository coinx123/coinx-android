package com.coin.exchange.di.module;

import com.coin.exchange.di.scope.ActivityScope;
import com.coin.exchange.view.test.TestView;
import com.coin.libbase.view.IView;

import dagger.Module;
import dagger.Provides;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/15
 * @description
 */
@Module
public class TestModule {

    private TestView mView;

    public TestModule(TestView testView) {
        mView = testView;
    }

    @Provides
    @ActivityScope
    public TestView provideTestView() {
        return mView;
    }

}

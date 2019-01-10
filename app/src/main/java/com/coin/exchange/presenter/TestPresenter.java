package com.coin.exchange.presenter;

import com.coin.exchange.view.test.TestView;
import com.coin.libbase.presenter.BasePresenter;

import javax.inject.Inject;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/15
 * @description
 */
public class TestPresenter extends BasePresenter<TestView> {

    @Inject
    public TestPresenter(TestView mView) {
        super(mView);
    }

    public void test(){
//        mView.onSuccess();
    }

}

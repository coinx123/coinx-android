package com.coin.exchange.presenter;


import com.coin.exchange.config.NetConfig;
import com.coin.exchange.config.okEx.ServerTimeStampHelper;
import com.coin.exchange.model.okex.response.WalletInfoRes;
import com.coin.exchange.net.RetrofitFactory;
import com.coin.exchange.utils.SignUtils;
import com.coin.exchange.view.BindView;
import com.coin.libbase.net.rxjava.RxObservableSubscriber;
import com.coin.libbase.presenter.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/15
 * @description
 */
public class BindApiPresenter extends BasePresenter<BindView> {

    @Inject
    public BindApiPresenter(BindView mView) {
        super(mView);
    }

    public void checkBitMEXInfo(final String apiKey, final String apiSecret) {

        Observable
                .just(true)
                .flatMap(new Function<Boolean, ObservableSource<Object>>() {
                    @Override
                    public ObservableSource<Object> apply(Boolean aBoolean) throws Exception {


                        long expiresNum = System.currentTimeMillis() / 1000 + 5;
                        String expires = expiresNum + "";

                        String data = "";
                        String verb = "GET";
                        String path = "/api/v1/" + NetConfig.BIT_MEX_CHECK_ACCOUNT_URL;

                        String sign = SignUtils.getBitMexSign(apiSecret, verb, path, expires, data);

//                        result.put("Accept", "application/json");

                        return RetrofitFactory
                                .getBitMexPureApiService()
                                .validUserInfo(apiKey, expires, sign);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObservableSubscriber<Object>(BindApiPresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        mView.onBindError("请输入正确的账号信息");
                    }

                    @Override
                    protected void onSuccessRes(Object value) {
                        mView.onBitMEXBindSuc(apiKey, apiSecret);
                    }

                });

    }

    public void checkUserInfo(final String apiKey,
                              final String secretKey,
                              final String passphraseKey) {

        Observable
                .just(true)
                .flatMap(new Function<Boolean, Observable<List<WalletInfoRes>>>() {
                    @Override
                    public Observable<List<WalletInfoRes>> apply(Boolean aBoolean) throws Exception {

                        String expires = ServerTimeStampHelper.getInstance()
                                .getCurrentTimeStamp(ServerTimeStampHelper.Type.ISO8601);

                        String body = "";

                        String requestPath = NetConfig.OK_EX_CHECK_ACCOUNT_URL;

                        String sign = SignUtils.getOkExSign(secretKey,
                                "GET",
                                requestPath,
                                expires,
                                body);

                        return RetrofitFactory
                                .getOkExPureApiService()
                                .validUserInfo(apiKey, passphraseKey, sign.trim(), expires);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObservableSubscriber<List<WalletInfoRes>>(BindApiPresenter.this) {
                    @Override
                    protected void onError(int code, String message) {
                        mView.onBindError("请输入正确的账号信息");
                    }

                    @Override
                    protected void onSuccessRes(List<WalletInfoRes> value) {
                        mView.onBindSuc(apiKey, secretKey, passphraseKey);
                    }
                });

    }


}

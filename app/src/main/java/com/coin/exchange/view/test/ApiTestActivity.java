package com.coin.exchange.view.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.coin.exchange.R;
import com.coin.exchange.config.okEx.ServerTimeStampHelper;
import com.coin.exchange.model.okex.request.FuturesOrderReq;
import com.coin.exchange.model.okex.response.CurrencyRes;
import com.coin.exchange.model.okex.response.FuturesAccountsCurrencyRes;
import com.coin.exchange.model.okex.response.FuturesAccountsLedger;
import com.coin.exchange.model.okex.response.FuturesCancelOrderRes;
import com.coin.exchange.model.okex.response.FuturesInstrumentsBookRes;
import com.coin.exchange.model.okex.response.FuturesInstrumentsIndexRes;
import com.coin.exchange.model.okex.response.FuturesInstrumentsOpenInterestRes;
import com.coin.exchange.model.okex.response.FuturesInstrumentsRes;
import com.coin.exchange.model.okex.response.FuturesInstrumentsTickerList;
import com.coin.exchange.model.okex.response.FuturesInstrumentsTradesList;
import com.coin.exchange.model.okex.response.FuturesOrderListRes;
import com.coin.exchange.model.okex.response.FuturesOrderRes;
import com.coin.exchange.model.okex.response.FuturesPositionRes;
import com.coin.exchange.model.okex.response.SingleFuturesPositionRes;
import com.coin.exchange.model.okex.response.TimeStampInfoRes;
import com.coin.exchange.model.okex.response.WalletInfoRes;
import com.coin.exchange.net.RetrofitFactory;
import com.coin.exchange.net.rx.TestSingle;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ApiTestActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_test);

        findViewById(R.id.btn_wallet_req).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrofitFactory
                        .getOkExApiService()
                        .getWalletInfo()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<List<WalletInfoRes>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(List<WalletInfoRes> walletInfoRes) {
                                Log.i(TAG, "onSuccess: " + walletInfoRes.toString());
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.i(TAG, "onError: " + e.getMessage());
                            }
                        });
            }
        });

        findViewById(R.id.btn_timestamp_req).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrofitFactory
                        .getOkExApiService()
                        .getTimeStamp()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<TimeStampInfoRes>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(TimeStampInfoRes timeStampInfoRes) {
                                Log.i(TAG, "onSuccess: " + timeStampInfoRes.toString());

                                long timestamp = (long) (Double.parseDouble(timeStampInfoRes.getEpoch()));

                                Log.i(TAG, "timestamp: " + timestamp + 5);

//                                CommonHeaderConfig.TIME_DELAY = timeStampInfoRes.getIso();

                                RetrofitFactory
                                        .getOkExApiService()
                                        .getWalletInfo()
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new SingleObserver<List<WalletInfoRes>>() {
                                            @Override
                                            public void onSubscribe(Disposable d) {

                                            }

                                            @Override
                                            public void onSuccess(List<WalletInfoRes> walletInfoRes) {
                                                Log.i(TAG, "onSuccess: " + walletInfoRes.toString());
                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                Log.i(TAG, "onError: " + e.getMessage());
                                            }
                                        });
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.i(TAG, "onError: " + e.getMessage());
                            }
                        });
            }
        });


//        String apiSecret = "chNOOS4KvNXR_Xq4k4c9qsfoKWvnDecLATCRlcBwyKDYnWgO";
//
//        String verb = "GET";
//        String path = "/api/v1/instrument";
//        String expires = "1518064236";
//        String data = "";
//
//        System.out.println(SignUtils.getBitMexSign(apiSecret, verb, path, expires, data));
//
//        String timestamp = "2018-03-08T10:59:25.789Z";
//        String method = "POST";
//        String requestPath = "/orders?before=2&limit=30";
//        String body = "{\"product_id\":\"BTC-USD-0309\",\"order_id\":\"377454671037440\"}";
//
//        System.out.println(SignUtils.getOkExSign(apiSecret, timestamp, method, requestPath, body));

        findViewById(R.id.btn_currency_list_req).setOnClickListener(this);
        findViewById(R.id.btn_server_time_utils_test).setOnClickListener(this);
        findViewById(R.id.FuturesPositionRes).setOnClickListener(this);
        findViewById(R.id.SingleFuturesPositionRes).setOnClickListener(this);
        findViewById(R.id.FuturesAccountsLedger).setOnClickListener(this);
        findViewById(R.id.FuturesAccountsLedgerCan).setOnClickListener(this);
        findViewById(R.id.FuturesAccountsLedgerlist).setOnClickListener(this);
        findViewById(R.id.instruments).setOnClickListener(this);
        findViewById(R.id.instrumentsbook).setOnClickListener(this);
        findViewById(R.id.FuturesInstrumentsIndexRes).setOnClickListener(this);
        findViewById(R.id.accounts).setOnClickListener(this);
        findViewById(R.id.trades).setOnClickListener(this);
        findViewById(R.id.ledger).setOnClickListener(this);
        findViewById(R.id.ticker).setOnClickListener(this);
        findViewById(R.id.kline).setOnClickListener(this);
        findViewById(R.id.open_interest).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_currency_list_req:
                RetrofitFactory
                        .getOkExApiService()
                        .getCurrencyList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new TestSingle<List<CurrencyRes>>());
                break;
            case R.id.btn_server_time_utils_test:
                new Thread() {
                    @Override
                    public void run() {
                        Log.i(TAG, "timeStamp: " + ServerTimeStampHelper.getInstance().getCurrentTimeStamp(ServerTimeStampHelper.Type.ISO8601));
                    }
                }.start();
                break;
            case R.id.FuturesPositionRes:
//                RetrofitFactory
//                        .getOkExApiService()
//                        .getFuturesPositionRes()
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new TestSingle<FuturesPositionRes>());
                break;
            case R.id.SingleFuturesPositionRes:
                RetrofitFactory
                        .getOkExApiService()
                        .getSingleFuturesPositionRes("BTC-USD-181109")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new TestSingle<SingleFuturesPositionRes>());
                break;
            case R.id.FuturesAccountsLedger:
                FuturesOrderReq futuresOrderReq = new FuturesOrderReq("10", "0",
                        "1", "6300", "1", "BTC-USD-181109");
                RetrofitFactory
                        .getOkExApiService()
                        .futuresOrder(futuresOrderReq)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new TestSingle<FuturesOrderRes>());
                break;
            case R.id.FuturesAccountsLedgerCan:
                RetrofitFactory
                        .getOkExApiService()
                        .futuresCancelOrder("BTC-USD-181109", "1752974289748993")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new TestSingle<FuturesCancelOrderRes>());
                break;
            case R.id.FuturesAccountsLedgerlist:
//                RetrofitFactory
//                        .getOkExApiService()
//                        .getFuturesOrderList("BTC-USD-181109", "0", null, null, null)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new TestSingle<FuturesOrderListRes>());
                break;
            case R.id.instruments:
//                RetrofitFactory
//                        .getOkExApiService()
//                        .getFuturesInstruments()
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new TestSingle<List<FuturesInstrumentsRes>>());
                break;
            case R.id.instrumentsbook:
                RetrofitFactory
                        .getOkExApiService()
                        .getFuturesInstrumentsBook("BTC-USD-181109", "50")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new TestSingle<FuturesInstrumentsBookRes>());
                break;
            case R.id.FuturesInstrumentsIndexRes:
                RetrofitFactory
                        .getOkExApiService()
                        .getFuturesInstrumentsIndex("BTC-USD-181109")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new TestSingle<FuturesInstrumentsIndexRes>());
                break;
            case R.id.trades:
                RetrofitFactory
                        .getOkExApiService()
                        .getFuturesInstrumentsTrades("BTC-USD-181123", null, null, "200")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new TestSingle<List<FuturesInstrumentsTradesList>>());
                break;
            case R.id.accounts:
                RetrofitFactory
                        .getOkExApiService()
                        .getFuturesAccountsCurrency("btc")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new TestSingle<FuturesAccountsCurrencyRes>());
                break;
            case R.id.ledger:
                RetrofitFactory
                        .getOkExApiService()
                        .getFuturesAccountsLedger("btc")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new TestSingle<List<FuturesAccountsLedger>>());
                break;
            case R.id.ticker:
                RetrofitFactory
                        .getOkExApiService()
                        .getFuturesInstrumentsTicker()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new TestSingle<List<FuturesInstrumentsTickerList>>());
                break;
            case R.id.kline:
                RetrofitFactory
                        .getOkExApiService()
                        .getFuturesInstrumentsCandles("BTC-USD-181116", null, null, "60")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new TestSingle<List<List<Double>>>());
                break;
            case R.id.open_interest:
                RetrofitFactory
                        .getOkExApiService()
                        .getFuturesInstrumentsOpenInterest("BTC-USD-181116")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new TestSingle<FuturesInstrumentsOpenInterestRes>());
                break;
        }
    }

    public void goToWebSocket(View view) {
        startActivity(new Intent(this, WebSocketTestActivity.class));
    }
}

package com.coin.exchange.view.fragment.kline;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coin.exchange.R;
import com.coin.exchange.model.bitMex.response.bucketetRes;
import com.coin.exchange.net.RetrofitFactory;
import com.coin.exchange.utils.AppUtils;
import com.github.fujianlian.klinechart.DataHelper;
import com.github.fujianlian.klinechart.KLineChartAdapter;
import com.github.fujianlian.klinechart.KLineChartView;
import com.github.fujianlian.klinechart.KLineEntity;
import com.github.fujianlian.klinechart.draw.Status;
import com.github.fujianlian.klinechart.formatter.DateFormatter;
import com.coin.libbase.model.eventbus.Event;
import com.coin.libbase.view.fragment.JBaseFragment;

import org.greenrobot.eventbus.Subscribe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import butterknife.BindView;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author dean
 * @date 创建时间：2018/11/8
 * @description K线图界面
 */
public class KFragmentLine extends JBaseFragment {
    private static final String ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final SimpleDateFormat format = new SimpleDateFormat(ISO_8601);
    public static final String TIME_KEY = "timeKey";
    public static final String INSTRUMENT_ID = "instrumentId";
    public static final String FROM = "from";

    @BindView(R.id.chartView)
    KLineChartView mMinuteChartView;
    KLineChartAdapter adapter;

    private int mTime = 0;
    private String instrumentId = "";
    private String from = "";
    private Disposable candlesDis;

    public static Fragment newInstance(int time, String instrumentId, String from) {
        KFragmentLine fragment = new KFragmentLine();
        Bundle bundle = new Bundle();
        bundle.putInt(TIME_KEY, time);
        bundle.putString(INSTRUMENT_ID, instrumentId);
        bundle.putString(FROM, from);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_kline, container, false);
    }

    @Override
    protected void initView(View view) {
        Bundle arguments = getArguments();
        mTime = arguments.getInt(TIME_KEY);
        instrumentId = arguments.getString(INSTRUMENT_ID);
        from = arguments.getString(FROM);
        if (mTime == 0 || instrumentId.isEmpty() || from.isEmpty()) {
            return;
        }

        adapter = new KLineChartAdapter();
        mMinuteChartView.setAdapter(adapter);
        mMinuteChartView.setDateTimeFormatter(new DateFormatter());
        mMinuteChartView.setGridRows(4);
        mMinuteChartView.setGridColumns(4);
        initData();
        if (mTime == 60) {
            mMinuteChartView.setMainDrawLine(true);
        }
    }

    private void initData() {
        mMinuteChartView.justShowLoading();
        if (mTime == 60 || mTime == 300) {    //只对1分钟或者5分钟的进行轮询
            java.util.Timer timer = new java.util.Timer(true);
            TimerTask task = new TimerTask() {
                public void run() {
                    if (from.equals(AppUtils.OKEX)) {
                        getInstrumentsCandles();
                    } else {
                        getbitmexInstrumentsCandles();
                    }
                }
            };
            timer.schedule(task, 0, mTime * 1000);
        } else {
            if (from.equals(AppUtils.OKEX)) {
                getInstrumentsCandles();
            } else {
                getbitmexInstrumentsCandles();
            }
        }
    }

    //获取K线图数据
    private void getInstrumentsCandles() {
        RetrofitFactory
                .getOkExApiService()
                .getFuturesInstrumentsCandles(instrumentId, null, null, mTime + "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<List<Double>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        candlesDis = d;
                    }

                    @Override
                    public void onSuccess(List<List<Double>> ftList) {
                        try {
                            final List<KLineEntity> datas = new ArrayList<>();
                            int size = ftList.size();
                            for (int i = 0; i < size; i++) {
                                KLineEntity data = new KLineEntity();
                                data.Date = longToDate(ftList.get(size - 1 - i).get(0).longValue());
                                data.Open = ftList.get(size - 1 - i).get(1).floatValue();
                                data.High = ftList.get(size - 1 - i).get(2).floatValue();
                                data.Low = ftList.get(size - 1 - i).get(3).floatValue();
                                data.Close = ftList.get(size - 1 - i).get(4).floatValue();
                                data.Volume = ftList.get(size - 1 - i).get(5).floatValue();

                                datas.add(data);
                            }
                            DataHelper.calculate(datas);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.addFooterData(datas);
                                    adapter.notifyDataSetChanged();
                                    mMinuteChartView.startAnimation();
                                    mMinuteChartView.refreshEnd();
                                    mMinuteChartView.setChildDraw(0);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    //获取K线图数据
    private void getbitmexInstrumentsCandles() {
        String time = "1m";
        if (mTime == 60) {
            time = "1m";
        } else if (mTime == 300) {
            time = "5m";
        } else if (mTime == 60 * 60) {
            time = "1h";
        } else if (mTime == 24 * 60 * 60) {
            time = "1d";
        }
        RetrofitFactory
                .getBitMexApiService()
                .getInstrumentTradeKList(instrumentId, time, 100, false, true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<bucketetRes>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        candlesDis = d;
                    }

                    @Override
                    public void onSuccess(List<bucketetRes> bucketetRes) {
                        Collections.reverse(bucketetRes);
                        try {
                            final List<KLineEntity> datas = new ArrayList<>();
                            int size = bucketetRes.size();
                            for (int i = 0; i < size; i++) {
                                KLineEntity data = new KLineEntity();
                                data.Date = bitmexLongToDate(bucketetRes.get(i).getTimestamp());
                                data.Open = (float) bucketetRes.get(i).getOpen();
                                data.High = (float) bucketetRes.get(i).getHigh();
                                data.Low = (float) bucketetRes.get(i).getLow();
                                data.Close = (float) bucketetRes.get(i).getClose();
                                data.Volume = (float) bucketetRes.get(i).getVolume();

                                datas.add(data);
                            }
                            DataHelper.calculate(datas);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.addFooterData(datas);
                                    adapter.notifyDataSetChanged();
                                    mMinuteChartView.startAnimation();
                                    mMinuteChartView.refreshEnd();
                                    mMinuteChartView.setChildDraw(0);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });

    }

    public String longToDate(long lo) {
        Date date = new Date(lo);
        String time = "HH:mm";
        if (mTime > 7200) {
            time = "yyyy-MM-dd";
        }
        SimpleDateFormat sd = new SimpleDateFormat(time);
        return sd.format(date);
    }

    public String bitmexLongToDate(String timestamp) {
        try {
            Date time = format.parse(timestamp);
            long uinx = time.getTime();
            long time_teal = uinx + 8 * 60 * 60 * 1000; //多增加8个小时
            Date date = new Date(time_teal);
            String time_H = "HH:mm";
            if (mTime > 7200) {
                time_H = "yyyy-MM-dd";
            }
            SimpleDateFormat sd = new SimpleDateFormat(time_H);
            return sd.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Subscribe
    public void onIndex(Event.KlineIndex klineIndex) {
        switch (klineIndex.getIndex()) {
            case R.id.tv_MA:
                mMinuteChartView.changeMainDrawType(Status.MA);
                break;
            case R.id.tv_BOLL:
                mMinuteChartView.changeMainDrawType(Status.BOLL);
                break;
            case R.id.tv_MACD:
                mMinuteChartView.setChildDraw(0);
                break;
            case R.id.tv_KDJ:
                mMinuteChartView.setChildDraw(1);
                break;
            case R.id.tv_RSI:
                mMinuteChartView.setChildDraw(2);
                break;
            case R.id.tv_WR:
                mMinuteChartView.setChildDraw(3);
                break;
            case R.id.tv_gone:
                mMinuteChartView.hideChildDraw();
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mMinuteChartView.setGridRows(3);
            mMinuteChartView.setGridColumns(8);
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mMinuteChartView.setGridRows(4);
            mMinuteChartView.setGridColumns(4);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (candlesDis != null) {
            candlesDis.dispose();
        }
    }
}

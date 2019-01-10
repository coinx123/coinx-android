package com.coin.exchange.view.fragment.main.trade;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anthonycr.bonsai.Schedulers;
import com.anthonycr.bonsai.SingleOnSubscribe;
import com.coin.exchange.R;
import com.coin.exchange.adapter.CollectionAdapter;
import com.coin.exchange.aop.CheckLogin;
import com.coin.exchange.cache.PreferenceManager;
import com.coin.exchange.config.FragmentConfig;
import com.coin.exchange.config.okEx.ChannelHelper;
import com.coin.exchange.context.AppApplication;
import com.coin.exchange.database.CollectionItem;
import com.coin.exchange.database.CollectionModel;
import com.coin.exchange.model.bitMex.response.InstrumentItemRes;
import com.coin.exchange.model.okex.webSocket.response.DetailRes;
import com.coin.exchange.utils.AppUtils;
import com.coin.exchange.utils.GsonUtils;
import com.coin.exchange.view.TradeActivity;
import com.coin.exchange.webSocket.bitMex.BitMEXWebSocketManager;
import com.coin.exchange.webSocket.okEx.okExFuture.FutureWebSocketManager;
import com.coin.libbase.view.fragment.JBaseFragment;
import com.google.gson.reflect.TypeToken;
import com.coin.libbase.model.CommonRes;
import com.coin.libbase.model.eventbus.Event;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;

/**
 * @author dean
 * @date 创建时间：2018/11/8
 * @description
 */
public class TradeFuturesOptionalFragment extends JBaseFragment {
    private static final String FROM = "from";
    @BindView(R.id.rv_optional_list)
    RecyclerView rvOptionalList;
    @BindView(R.id.tv_add_optional)
    TextView tvAddOptional;
    @BindView(R.id.tv_add_optional_normal)
    TextView tvAddOptionalNormal;

    @Inject
    PreferenceManager preferenceManager;
    @Inject
    CollectionModel collectionModel;
    private CollectionAdapter collectionAdapter;
    private DeleteOptionalFragment deleteOptionalFragment;
    protected boolean isCreated = false;
    @NonNull
    private final List<CollectionItem> okex_collectionItems = new ArrayList<>();
    private final List<CollectionItem> bitmex_collectionItems = new ArrayList<>();
    private String from = "";

    public static Fragment newInstance(String from) {
        TradeFuturesOptionalFragment fragment = new TradeFuturesOptionalFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FROM, from);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isCreated = true;
        AppApplication.getAppComponent().inject(this);
        return inflater.inflate(R.layout.fragment_trade_okex_optional, container, false);
    }

    @Override
    protected void initView(View view) {
        Bundle arguments = getArguments();
        from = arguments.getString(FROM);
        rvOptionalList.setLayerType(View.LAYER_TYPE_NONE, null);
        rvOptionalList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        collectionAdapter = new CollectionAdapter(getActivity(), preferenceManager.getFloat(PreferenceManager.RATE, 1.0f));
        rvOptionalList.setAdapter(collectionAdapter);
        rvOptionalList.setHasFixedSize(true);

        if (from.equals(AppUtils.OKEX)) {
            collectionAdapter.setOnClickListener(new CollectionAdapter.OnClickListener() {
                @Override
                @CheckLogin(value = AppUtils.OKEX)
                public void onClick(View itemView, int Position) {
                    ToTradeActivity(Position, okex_collectionItems);
                }
            });
        } else {
            collectionAdapter.setOnClickListener(new CollectionAdapter.OnClickListener() {
                @Override
                @CheckLogin(value = AppUtils.BITMEX)
                public void onClick(View itemView, int Position) {
                    ToTradeActivity(Position, bitmex_collectionItems);
                }
            });
        }

        collectionAdapter.setOnLongListener(new CollectionAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(View itemView, int Position) {
                deleteOptionalFragment.update(Position);
                deleteOptionalFragment.show(TradeFuturesOptionalFragment.this);
            }
        });

        deleteOptionalFragment = new DeleteOptionalFragment();
        deleteOptionalFragment.setListener(new DeleteOptionalFragment.ActionListener() {
            @Override
            public void onViewClick(int position) {
                if (from.equals(AppUtils.OKEX)) {
                    deleteOptional(position, okex_collectionItems);
                } else {
                    deleteOptional(position, bitmex_collectionItems);
                }
            }
        });
        getOptional();
    }

    private void deleteOptional(int position, List<CollectionItem> collectionItems) {
        CollectionItem collectionItem = new CollectionItem(collectionItems.get(position).getUrl(), collectionItems.get(position).getTitle());
        collectionModel.deleteCollection(collectionItem)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.main())
                .subscribe(new SingleOnSubscribe<Boolean>() {
                    @Override
                    public void onItem(@Nullable Boolean item) {
                        super.onItem(item);
                        if (item) {
                            getOptional();
                        }
                    }
                });
    }

    private void ToTradeActivity(int Position, List<CollectionItem> collectionItems) {
        EventBus.getDefault().postSticky(new Event.SendInstrument(collectionItems.get(Position).getTitle(), collectionItems.get(Position).getUrl(), from));
        EventBus.getDefault().post(new Event.HideInstrument());
        Intent intent = new Intent(getContext(), TradeActivity.class);
        intent.putExtra(FragmentConfig.INSTRUMENT_ID, collectionItems.get(Position).getUrl());
        intent.putExtra(FragmentConfig.TYPE, collectionItems.get(Position).getTitle());
        intent.putExtra(FragmentConfig.FROM, from);
        startActivity(intent);
    }

    @OnClick({R.id.tv_add_optional, R.id.tv_add_optional_normal}) //自选页面点击添加自选发送消息
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add_optional:
                EventBus.getDefault().post(new Event.AddOptionalEvent(true));
                break;
            case R.id.tv_add_optional_normal:
                EventBus.getDefault().post(new Event.AddOptionalEvent(true));
                break;
        }
    }

    //从数据库中获取自选
    private void getOptional() {
        collectionModel.getAllCollection().subscribeOn(Schedulers.io())
                .observeOn(Schedulers.main())
                .subscribe(new SingleOnSubscribe<List<CollectionItem>>() {
                    @Override
                    public void onItem(@Nullable List<CollectionItem> item) {
                        super.onItem(item);
                        List<CollectionItem> okex = new ArrayList<>();
                        List<CollectionItem> bitmex = new ArrayList<>();
                        for (int i = 0; i < item.size(); i++) {
                            if (item.get(i).getPosition() == 0) {//0 代表的是okex
                                okex.add(item.get(i));
                            } else {
                                bitmex.add(item.get(i));
                            }
                        }
                        if (from.equals(AppUtils.OKEX)) {
                            collectionAdapter.updateItems(okex);
                            okex_collectionItems.clear();
                            okex_collectionItems.addAll(okex);
                            if (okex.size() > 0) {
                                for (int i = 0; i < okex.size(); i++) {
                                    FutureWebSocketManager.getInstance().subscribeDetail(
                                            okex.get(i).getUrl().substring(0, 3).toLowerCase(),
                                            ChannelHelper.getTime(okex.get(i).getTitle()));
                                }
                                rvOptionalList.setVisibility(View.VISIBLE);
                                tvAddOptionalNormal.setVisibility(View.VISIBLE);
                                tvAddOptional.setVisibility(View.GONE);
                            } else {
                                rvOptionalList.setVisibility(View.GONE);
                                tvAddOptionalNormal.setVisibility(View.GONE);
                                tvAddOptional.setVisibility(View.VISIBLE);
                            }
                        } else {
                            collectionAdapter.updateItems(bitmex);
                            bitmex_collectionItems.clear();
                            bitmex_collectionItems.addAll(bitmex);
                            if (bitmex.size() > 0) {
                                for (int i = 0; i < bitmex.size(); i++) {
                                    BitMEXWebSocketManager.getInstance().subscribeInstrument(bitmex.get(i).getUrl());
                                }
                                rvOptionalList.setVisibility(View.VISIBLE);
                                tvAddOptionalNormal.setVisibility(View.VISIBLE);
                                tvAddOptional.setVisibility(View.GONE);
                            } else {
                                rvOptionalList.setVisibility(View.GONE);
                                tvAddOptionalNormal.setVisibility(View.GONE);
                                tvAddOptional.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isCreated) {
            getOptional(); //点击自选和全部之后页面刷新
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        getOptional();  //TODO 从K线图页面调回来刷新一下
    }

    @Override
    protected void onMessage(List list) {
        try {
            List<CommonRes> commonResList = list;
            DetailRes detailRes;
            if (from.equals(AppUtils.OKEX)) { //okex
                String channel = commonResList.get(0).getChannel();
                String icon = channel.substring(17, 20); //截取币种，如btc
                String time = "";
                if (channel.contains("ticker")) {  //对ticker的推送消息才进行处理
                    detailRes = (DetailRes) GsonUtils.getInstance().fromJson(
                            commonResList.get(0).getData().toString(), DetailRes.class);
                    if (detailRes != null) {
                        if (channel.contains(ChannelHelper.Y.THIS_WEEK)) {
                            time = FragmentConfig.WEEK;
                        } else if (channel.contains(ChannelHelper.Y.NEXT_WEEK)) {
                            time = FragmentConfig.NEXT_WEEK;
                        } else if (channel.contains(ChannelHelper.Y.QUARTER)) {
                            time = FragmentConfig.QUARTER;
                        }
                        for (int j = 0; j < okex_collectionItems.size(); j++) {
                            if (okex_collectionItems.get(j).getTitle().contains(time)
                                    && okex_collectionItems.get(j).getUrl().contains(icon.toUpperCase())
                                    && detailRes.getLast() != okex_collectionItems.get(j).getLast()) {  //推送过来的价格和上一次不一样才刷新item
                                okex_collectionItems.get(j).setVolume_24h(detailRes.getVol());
                                okex_collectionItems.get(j).setLast(detailRes.getLast());
                                collectionAdapter.notifyItemChanged(j);
                                break;
                            }
                        }
                    }
                }
            } else if (from.equals(AppUtils.BITMEX)) {  //bitmex
                String table = commonResList.get(0).getTable();
                Object object = commonResList.get(0).getData();
                if (table == null || object == null) {
                    return;
                }
                if (table.equals("instrument")) {
                    String sub = GsonUtils.getInstance().toJson(commonResList.get(0).getData());
                    List<InstrumentItemRes> instrumentItemResList = GsonUtils.getGson()
                            .fromJson(sub, new TypeToken<List<InstrumentItemRes>>() {
                            }.getType());
                    InstrumentItemRes res = instrumentItemResList.get(0);
                    for (int j = 0; j < bitmex_collectionItems.size(); j++) {
                        if (bitmex_collectionItems.get(j).getUrl().equals(res.getSymbol())) {
                            if (res.getLastPrice() != 0) {
                                bitmex_collectionItems.get(j).setLast(res.getLastPrice());
                            }
                            if (res.getForeignNotional24h() != 0) {
                                bitmex_collectionItems.get(j).setVolume_24h((int) res.getForeignNotional24h());
                            }
                            if (res.getIndicativeSettlePrice() != 0) {
                                bitmex_collectionItems.get(j).setIndicativeSettlePrice(res.getIndicativeSettlePrice());
                            }
                            if (res.getLastChangePcnt() != 0) {
                                bitmex_collectionItems.get(j).setLastChangePcnt(res.getLastChangePcnt());
                            }

                            if (res.getQuoteCurrency() != null) {
                                bitmex_collectionItems.get(j).setQuoteCurrency(res.getQuoteCurrency());
                            }
                            collectionAdapter.notifyItemChanged(j);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
    }
}

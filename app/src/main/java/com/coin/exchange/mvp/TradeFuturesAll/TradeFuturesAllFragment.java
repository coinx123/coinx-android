package com.coin.exchange.mvp.TradeFuturesAll;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.coin.exchange.R;
import com.coin.exchange.adapter.groundrecycleradapter.GroupRecyclerAdapter;
import com.coin.exchange.adapter.groundrecycleradapter.MemberViewHolder;
import com.coin.exchange.adapter.groundrecycleradapter.Team;
import com.coin.exchange.adapter.groundrecycleradapter.TeamViewHolder;
import com.coin.exchange.aop.CheckLogin;
import com.coin.exchange.cache.PreferenceManager;
import com.coin.exchange.config.FragmentConfig;
import com.coin.exchange.context.AppApplication;
import com.coin.exchange.database.CollectionItem;
import com.coin.exchange.database.CollectionModel;
import com.coin.exchange.model.okex.response.FuturesInstrumentsTickerList;
import com.coin.exchange.mvp.TradeFuturesAll.di.DaggerTradeFuturesAllComponent;
import com.coin.exchange.mvp.TradeFuturesAll.di.TradeFuturesAllModule;
import com.coin.exchange.utils.AppUtils;
import com.coin.exchange.view.TradeActivity;
import com.coin.libbase.model.eventbus.Event;
import com.coin.libbase.utils.ToastUtil;
import com.coin.libbase.view.fragment.JBaseFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public final class TradeFuturesAllFragment extends JBaseFragment<TradeFuturesAllPresenter> implements TradeFuturesAllView {

    private static final String FROM = "from";
    @Inject
    PreferenceManager preferenceManager;
    @Inject
    CollectionModel collectionModel;

    @BindView(R.id.rv_futures_list)
    RecyclerView rvFuturesList;

    private final List<Team> teams = new ArrayList<>();
    private GroupRecyclerAdapter<Team, TeamViewHolder, MemberViewHolder> recyclerAdapter;
    private LayoutInflater layoutInflater = null;
    private String from = "";

    public static Fragment newInstance(String from) {
        TradeFuturesAllFragment fragment = new TradeFuturesAllFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FROM, from);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutInflater = inflater;
        return inflater.inflate(R.layout.fragment_trade_okex_bitmex_all, container, false);
    }

    @Override
    protected void initView(View view) {
        Bundle arguments = getArguments();
        from = arguments.getString(FROM);
        rvFuturesList.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Team> temp = new ArrayList(teams);
        recyclerAdapter = new GroupRecyclerAdapter<Team, TeamViewHolder, MemberViewHolder>(temp) {
            @Override
            protected TeamViewHolder onCreateGroupViewHolder(ViewGroup parent) {
                return new TeamViewHolder(layoutInflater.inflate(R.layout.item_team_title, parent, false), getActivity());
            }

            @Override
            protected MemberViewHolder onCreateChildViewHolder(ViewGroup parent) {
                return new MemberViewHolder(layoutInflater.inflate(R.layout.item_team_member, parent, false),
                        preferenceManager.getFloat(PreferenceManager.RATE, 1.0f), collectionModel);
            }

            @Override
            protected void onBindGroupViewHolder(TeamViewHolder holder, int groupPosition) {
                if (groupPosition == 0) { //对第一个的头部进行隐藏
                    holder.update(getGroup(groupPosition), View.GONE);
                } else {
                    holder.update(getGroup(groupPosition), View.VISIBLE);
                }
            }

            @Override
            protected void onBindChildViewHolder(final MemberViewHolder holder, final int groupPosition, final int childPosition) {
                if (from.equals(AppUtils.OKEX)) {
                    if (getGroup(groupPosition).members.size() == 2) { //okex当周五交割结算的时候当周的没有了然后次周的变成当周的了
                        if (childPosition == 0) {
                            holder.update(getGroup(groupPosition).members.get(childPosition), FragmentConfig.WEEK);
                        } else if (childPosition == 1) {
                            holder.update(getGroup(groupPosition).members.get(childPosition), FragmentConfig.QUARTER);
                        }
                    } else {
                        if (childPosition == 0) {
                            holder.update(getGroup(groupPosition).members.get(childPosition), FragmentConfig.WEEK);
                        } else if (childPosition == 1) {
                            holder.update(getGroup(groupPosition).members.get(childPosition), FragmentConfig.NEXT_WEEK);
                        } else if (childPosition == 2) {
                            holder.update(getGroup(groupPosition).members.get(childPosition), FragmentConfig.QUARTER);
                        }
                    }
                } else {   //bitmex
                    holder.update(getGroup(groupPosition).members.get(childPosition), "");
                }
            }

            @Override
            protected int getChildCount(Team group) {
                return group.members.size();
            }
        };
        rvFuturesList.setAdapter(recyclerAdapter);

        recyclerAdapter.setOnLongClickListener(new GroupRecyclerAdapter.OnLongClickListener() {
            @Override
            public void onLongItemClick(View itemView, int groupPosition, int childPosition) {
            }
        });

        if (from.equals(AppUtils.OKEX)) {
            recyclerAdapter.setOnChildClickListener(new GroupRecyclerAdapter.OnChildClickListener() {
                @Override
                @CheckLogin(value = AppUtils.OKEX)
                public void onChildClick(View itemView, int groupPosition, int childPosition, String time) {
                    ToTradeActivity(groupPosition, childPosition, time);
                }
            });
        } else {
            recyclerAdapter.setOnChildClickListener(new GroupRecyclerAdapter.OnChildClickListener() {
                @Override
                @CheckLogin(value = AppUtils.BITMEX)
                public void onChildClick(View itemView, int groupPosition, int childPosition, String time) {
                    ToTradeActivity(groupPosition, childPosition, time);
                }
            });
        }

        recyclerAdapter.setOnChildViewClickListener(new GroupRecyclerAdapter.OnChildViewClickListener() {
            @Override
            public void onChildViewClick(final View itemView, int groupPosition, int childPosition, String time) {
                Team team = recyclerAdapter.getGroup(groupPosition);
                FuturesInstrumentsTickerList item = team.members.get(childPosition);
                final CollectionItem collectionItem;
                if (from.equals(AppUtils.OKEX)) {
                    collectionItem = new CollectionItem(item.getInstrument_id(), time);
                } else {
                    collectionItem = new CollectionItem(item.getInstrument_id(), time, 1);
                }
                AppUtils.isExitAndDelOrAdd(collectionModel, item.getInstrument_id(),
                        (ImageView) itemView.findViewById(R.id.iv_click_collection), collectionItem);
            }
        });
    }

    private void ToTradeActivity(int groupPosition, int childPosition, String time) {
        Team team = recyclerAdapter.getGroup(groupPosition);
        EventBus.getDefault().postSticky(new Event.SendInstrument(time, team.members.get(childPosition).getInstrument_id(), from));
        EventBus.getDefault().post(new Event.HideInstrument());
        Intent intent = new Intent(getContext(), TradeActivity.class);
        intent.putExtra(FragmentConfig.INSTRUMENT_ID, team.members.get(childPosition).getInstrument_id());
        intent.putExtra(FragmentConfig.TYPE, time);
        intent.putExtra(FragmentConfig.FROM, from);
        startActivity(intent);
    }

    @Override
    protected void registerDagger() {
        DaggerTradeFuturesAllComponent.builder()
                .appComponent(AppApplication.getAppComponent())
                .tradeFuturesAllModule(new TradeFuturesAllModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) { //viewPage 切换fragment实时刷新
            if (from.equals(AppUtils.OKEX)) {
                mPresenter.getOkexFutures(from);    //获取okex所有的合约
            } else {
                mPresenter.getBitmexFutures(from);
            }
        }
    }

    @Override
    public void onGetOkexFutures(List<Team> teamList) {
        teams.clear();
        teams.addAll(teamList);
        recyclerAdapter.update(teams);
    }

    @Override
    public void onGetOkexFuturesError(String msg) {
        ToastUtil.showCenter(msg);
    }

    @Override
    public void onGetBitmexFutures(List<Team> teamList) {
        teams.clear();
        teams.addAll(teamList);
        recyclerAdapter.update(teams);
    }

    @Override
    public void onGetBitmexFuturesError(String msg) {
        ToastUtil.showCenter(msg);
    }

}

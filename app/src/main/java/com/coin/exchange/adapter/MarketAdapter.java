package com.coin.exchange.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.coin.exchange.R;
import com.coin.exchange.aop.CheckLogin;
import com.coin.exchange.config.FragmentConfig;
import com.coin.exchange.model.okex.vo.HotCoinItemVO;
import com.coin.exchange.model.okex.vo.MenuItemVO;
import com.coin.exchange.model.okex.vo.RankItemVO;
import com.coin.exchange.model.okex.vo.RankStateVO;
import com.coin.exchange.mvp.KLine.KLineActivity;
import com.coin.exchange.utils.AppUtils;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/13
 * @description
 */
public class MarketAdapter extends RecyclerView.Adapter implements RankTitleAdapter.ClickListener {

    private RankStateVO DEFAULT_RANK_STATE = new RankStateVO(RankStateVO.EMPTY, RankStateVO.EMPTY_MSG);

    // 图片
    private final static int BANNER = 0;
    // 热门币种
    private final static int HOT_COIN  = 1;
    // 分割线
    private final static int DIVIDER_LINE = 2;
    // 排行榜的类别
    private final static int RANK_TYPE = 3;
    // 排行榜的
    private final static int RANK_TITLE = 4;
    // 排行榜的内容
    private final static int RANK_ITEM = 5;
    // 排行榜的占位
    private final static int RANK_REPLACE = 6;

    public final static int HEADER_COUNT = RANK_ITEM;
    // 个数，顺序：banner、热门、分割线、排行榜头、排行榜、风格线
    private final static int ITEM_COUNT = HEADER_COUNT + 1;

    private final LayoutInflater mLayoutInflater;

    private final List<HotCoinItemVO> mHotCoinItemVOList = new ArrayList<>();
    private final HotCoinAdapter mHotCoinAdapter;

    private final RankTitleAdapter mRankTitleAdapter;
    private final Context mContext;

    private ClickListener mListener;

    private final List<RankItemVO> mRankItemVOList = new ArrayList<>();
    private RankStateVO mRankStateVO;

    private boolean isRunning;
    private final List<Integer> mImages;
    private final GlideImageLoader mImageLoader;

    private String curType;

    public MarketAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);

        mHotCoinAdapter = new HotCoinAdapter(context, mHotCoinItemVOList);
        mRankTitleAdapter = new RankTitleAdapter(context, this);

        mContext = context;

        isRunning = false;
        mImages = new ArrayList<>();
        mImages.add(R.mipmap.banner_one);
        mImages.add(R.mipmap.banner_two);
        mImages.add(R.mipmap.banner_three);
        mImageLoader = new GlideImageLoader();

    }

    /**
     * 设置热门币种信息列表
     *
     * @param hotCoinItemVOList
     */
    public void setHotCoinItemVOList(List<HotCoinItemVO> hotCoinItemVOList) {
        this.mHotCoinItemVOList.clear();
        this.mHotCoinItemVOList.addAll(hotCoinItemVOList);
    }

    public void setRankItemVOList(String type, List<RankItemVO> rankItemVOList) {
        this.mRankItemVOList.clear();
        this.mRankItemVOList.addAll(rankItemVOList);
        this.curType = type;
    }

    public void setRankState(RankStateVO rankState) {
        this.mRankStateVO = rankState;
    }

    public void setListener(ClickListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case BANNER:    // 图片
                return new BannerViewHolder(mLayoutInflater.inflate(R.layout.item_market_banner,
                        parent, false));
            case HOT_COIN: // 热门币种
                return new HotCoinViewHolder(mLayoutInflater.inflate(R.layout.item_hot_coin_rv,
                        parent, false));
            case DIVIDER_LINE: // 分割线
                return new DividerViewHolder(mLayoutInflater.inflate(R.layout.item_10_divider_line,
                        parent, false));
            case RANK_TYPE:   // 币种类别
                return new RankTypeViewHolder(mLayoutInflater.inflate(R.layout.item_rank_type_rv,
                        parent, false));
            case RANK_REPLACE:  // 排行榜占位
                return new RankStateViewHolder(mLayoutInflater.inflate(R.layout.item_rank_state,
                        parent, false));
            case RANK_ITEM:     // 排行榜内容
                return new RankItemViewHolder(mLayoutInflater.inflate(R.layout.item_rank,
                        parent, false));
            case RANK_TITLE:     // 排行榜类别
                return new RankTitleViewHolder(mLayoutInflater.inflate(R.layout.item_rank_title,
                        parent, false));
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HotCoinViewHolder) {    // 热门
            HotCoinViewHolder hotCoinViewHolder = (HotCoinViewHolder) holder;

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            hotCoinViewHolder.rvHotCoin.setLayoutManager(linearLayoutManager);
            hotCoinViewHolder.rvHotCoin.setAdapter(mHotCoinAdapter);

        } else if (holder instanceof RankTypeViewHolder) { // 排行榜标题
            RankTypeViewHolder rankTypeViewHolder = (RankTypeViewHolder) holder;

            if (rankTypeViewHolder.rvRankTitle.getAdapter() == null) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                rankTypeViewHolder.rvRankTitle.setLayoutManager(linearLayoutManager);
                rankTypeViewHolder.rvRankTitle.setAdapter(mRankTitleAdapter);
            }
        } else if (holder instanceof RankStateViewHolder) {

            RankStateVO state;
            if (mRankStateVO == null) {
                state = DEFAULT_RANK_STATE;
            } else {
                state = mRankStateVO;
            }

            RankStateViewHolder rankStateViewHolder = (RankStateViewHolder) holder;
            rankStateViewHolder.tvTip.setText(state.getStateMsg());

        } else if (holder instanceof RankItemViewHolder) {

            RankItemViewHolder rankItemViewHolder = (RankItemViewHolder) holder;
            final RankItemVO item = mRankItemVOList.get(position - HEADER_COUNT);

            int drawable;
            switch (item.getNum()) {
                case 1:
                    drawable = R.drawable.shape_rank_one_bg;
                    break;
                case 2:
                    drawable = R.drawable.shape_rank_two_bg;
                    break;
                case 3:
                    drawable = R.drawable.shape_rank_three_bg;
                    break;
                default:
                    drawable = R.drawable.shape_rank_other_bg;
                    break;
            }
            rankItemViewHolder.tvRankNum
                    .setBackground(ContextCompat.getDrawable(mContext, drawable));
            rankItemViewHolder.tvRankNum.setText(item.getNum() + "");

            rankItemViewHolder.tvName.setText(item.getName());
            rankItemViewHolder.tvNameDes.setText(item.getNameDes());
            rankItemViewHolder.tvVolume.setText(item.getVolume());
            rankItemViewHolder.tvValue.setText(item.getValue());
            rankItemViewHolder.tvRange.setText(item.getRangeString());

            if (curType.equals(AppUtils.BITMEX)) {
                rankItemViewHolder.tvIndex.setVisibility(View.VISIBLE);
                rankItemViewHolder.tvIndex.setText(item.getIndex());
            } else if (curType.equals(AppUtils.OKEX)) {
                rankItemViewHolder.tvIndex.setVisibility(View.GONE);

            }

            if (item.isIncrease()) {
                rankItemViewHolder.tvRange.setBackground(AppUtils.getIncreaseBg());
            } else {
                rankItemViewHolder.tvRange.setBackground(AppUtils.getDecreaseBg());
            }

            Integer iconRes = AppUtils.ICON_MAP.get(item.getName());
            if (iconRes != null) {
                rankItemViewHolder.ivIcon.setImageDrawable(
                        ContextCompat.getDrawable(mContext, iconRes));
            }

            if (curType.equals(AppUtils.BITMEX)) {
                rankItemViewHolder.llItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    @CheckLogin(value = AppUtils.BITMEX)
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra(FragmentConfig.INSTRUMENT_ID, item.getInsId()); //KLineActivity 一样存在要一起改
                        intent.putExtra(FragmentConfig.TYPE, item.getNameDes());
                        intent.putExtra(FragmentConfig.FROM, AppUtils.BITMEX);
                        intent.setClass(mContext, KLineActivity.class);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                rankItemViewHolder.llItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    @CheckLogin(value = AppUtils.OKEX)
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra(FragmentConfig.INSTRUMENT_ID, item.getInsId()); //KLineActivity 一样存在要一起改
                        intent.putExtra(FragmentConfig.TYPE, item.getNameDes());
                        intent.putExtra(FragmentConfig.FROM, AppUtils.OKEX);
                        intent.setClass(mContext, KLineActivity.class);
                        mContext.startActivity(intent);
                    }
                });
            }

        } else if (holder instanceof BannerViewHolder) {

            if (!isRunning) {
                isRunning = true;
                BannerViewHolder bannerViewHolder = (BannerViewHolder) holder;
                bannerViewHolder.banner
                        .setImages(mImages)
                        .setImageLoader(mImageLoader)
                        .start();
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < HEADER_COUNT) {  // 为头部类型
            return position;
        } else if (position == getItemCount() - 1) {   // 最后一个为分割线
            return DIVIDER_LINE;
        } else if (mRankItemVOList != null && mRankItemVOList.size() > 0) { // 内容
            return RANK_ITEM;
        } else {    // 占位
            return RANK_REPLACE;
        }
    }

    @Override
    public int getItemCount() {
        if (mRankItemVOList != null && mRankItemVOList.size() > 0) {
            return ITEM_COUNT + mRankItemVOList.size();
        } else {
            return ITEM_COUNT + 1;
        }
    }

    /**
     * 点击排行榜标题回调
     *
     * @param position 选中下标
     */
    @Override
    public void onClick(int position) {

        boolean isChange = false;

        for (int i = 0; i < FragmentConfig.getRankNav().size(); ++i) {
            MenuItemVO item = FragmentConfig.getRankNav().get(i);

            // 选中的项
            if (item.isSelect()) {
                // 选择的和当前的是同一个，直接中断
                if (i == position) {
                    break;
                }

                item.setSelect(false);
                isChange = true;

            } else if (i == position) {
                item.setSelect(true);
                isChange = true;
            }
        }

        if (isChange) {
            mRankTitleAdapter.notifyDataSetChanged();
            if (mListener != null) {
                mListener.onClick();
            }
        }

    }

    /**
     * 顶部图片
     */
    static class BannerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.banner)
        Banner banner;

        BannerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    /**
     * 热门币种信息
     */
    static class HotCoinViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rv_hot_coin)
        RecyclerView rvHotCoin;

        HotCoinViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    /**
     * 排行榜题
     */
    static class RankTypeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rv_rank_title)
        RecyclerView rvRankTitle;

        RankTypeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    static class RankStateViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_tip)
        TextView tvTip;

        RankStateViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class RankItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_rank_num)
        TextView tvRankNum;
        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_name_des)
        TextView tvNameDes;
        @BindView(R.id.tv_volume)
        TextView tvVolume;
        @BindView(R.id.tv_value)
        TextView tvValue;
        @BindView(R.id.tv_range)
        TextView tvRange;
        @BindView(R.id.ll_item)
        LinearLayout llItem;
        @BindView(R.id.tv_index)
        TextView tvIndex;

        RankItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 分割线
     */
    static class DividerViewHolder extends RecyclerView.ViewHolder {
        DividerViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * 排行榜标题栏
     */
    static class RankTitleViewHolder extends RecyclerView.ViewHolder {
        RankTitleViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface ClickListener {
        void onClick();
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {

            //Glide 加载图片简单用法
            Glide.with(context)
                    .load(path)
                    .into(imageView);

        }

    }

}

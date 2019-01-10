package com.coin.exchange.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coin.exchange.R;
import com.coin.exchange.model.okex.vo.RankItemVO;
import com.coin.exchange.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/13
 * @description
 */
public class RankItemAdapter extends RecyclerView.Adapter {

    // 标题
    private static final int TITLE = 0x001;
    // 分割线
    private static final int DIVIDER = 0x002;
    // 数据
    private static final int DATA = 0x003;

    // 其他数据项
    private static final int OTHER_ITEM_SIZE = 2;

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;

    private final List<RankItemVO> mRankItemVOList;

    public RankItemAdapter(Context context) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);

        this.mRankItemVOList = new ArrayList<>();
    }

    public void setRankItemVOList(List<RankItemVO> rankItemVOList) {
        mRankItemVOList.clear();
        mRankItemVOList.addAll(rankItemVOList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TITLE:
                return new TitleViewHolder(mLayoutInflater.inflate(R.layout.item_rank_title, parent, false));
            case DIVIDER:
                return new DividerViewHolder(mLayoutInflater.inflate(R.layout.item_0_5_divider_line, parent, false));
            default:
                return new RankItemViewHolder(mLayoutInflater.inflate(R.layout.item_rank, parent, false));

        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (!(holder instanceof RankItemViewHolder)) {
            return;
        }

        RankItemViewHolder viewHolder = (RankItemViewHolder) holder;
        RankItemVO item = mRankItemVOList.get(position - OTHER_ITEM_SIZE);

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
        viewHolder.tvRankNum
                .setBackground(ContextCompat.getDrawable(mContext, drawable));
        viewHolder.tvRankNum.setText(item.getNum() + "");

        viewHolder.tvName.setText(item.getName());
        viewHolder.tvNameDes.setText(item.getNameDes());
        viewHolder.tvVolume.setText(item.getVolume());
        viewHolder.tvValue.setText(item.getValue());
        viewHolder.tvRange.setText(item.getRangeString());

        if (item.isIncrease()) {
            viewHolder.tvRange.setBackground(AppUtils.getIncreaseBg());
        } else {
            viewHolder.tvRange.setBackground(AppUtils.getDecreaseBg());
        }

    }

    @Override
    public int getItemCount() {
        return mRankItemVOList.size() + OTHER_ITEM_SIZE;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TITLE;
            case 1:
                return DIVIDER;
            default:
                return DATA;
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

        RankItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class TitleViewHolder extends RecyclerView.ViewHolder {

        TitleViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class DividerViewHolder extends RecyclerView.ViewHolder {

        DividerViewHolder(View itemView) {
            super(itemView);
        }
    }

}

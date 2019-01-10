package com.coin.exchange.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coin.exchange.R;
import com.coin.exchange.config.FragmentConfig;
import com.coin.exchange.model.okex.vo.MenuItemVO;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/13
 * @description 行情 —— 排行榜标题
 */
public class RankTitleAdapter extends RecyclerView.Adapter<RankTitleAdapter.ViewHolder> {

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final ClickListener mListener;

    public RankTitleAdapter(Context context, ClickListener listener) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mListener = listener;
    }

    @NonNull
    @Override
    public RankTitleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_rank_type, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RankTitleAdapter.ViewHolder holder, int position) {
        final int curItemIndex = position;
        MenuItemVO item = FragmentConfig.getRankNav().get(curItemIndex);
        holder.tvTitle.setText(item.getName());
        holder.ivIcon.setImageDrawable(ContextCompat.getDrawable(mContext, item.getResId()));

        if (item.isSelect()) {
            holder.vSelectLine.setVisibility(View.VISIBLE);
            holder.tvTitle.setTextColor(mContext.getResources().getColor(R.color.blue));
        } else {
            holder.vSelectLine.setVisibility(View.INVISIBLE);
            holder.tvTitle.setTextColor(mContext.getResources().getColor(R.color.light_blue));
        }

        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(curItemIndex);
            }
        });

    }

    @Override
    public int getItemCount() {
        return FragmentConfig.getRankNav().size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.v_select_line)
        View vSelectLine;
        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.ll_item)
        LinearLayout llItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ClickListener {
        void onClick(int position);
    }

}

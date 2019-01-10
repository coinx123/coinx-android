package com.coin.exchange.adapter.groundrecycleradapter;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.coin.exchange.R;
import com.coin.exchange.utils.AppUtils;


public class TeamViewHolder extends RecyclerView.ViewHolder {
    private final TextView mTitleView;
    private final TextView mTitleViewLine;
    private Activity context;

    public TeamViewHolder(View itemView, Activity activity) {
        super(itemView);
        mTitleView = itemView.findViewById(R.id.title);
        mTitleViewLine = itemView.findViewById(R.id.title_line);
        this.context = activity;
    }

    public void update(Team team, int isGone) {
        mTitleViewLine.setVisibility(isGone);
        mTitleView.setText("\t" + team.title);
        mTitleView.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, AppUtils.ICON_MAP.get(team.title)),
                null, null, null);
    }
}

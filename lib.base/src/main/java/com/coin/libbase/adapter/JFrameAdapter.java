package com.coin.libbase.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zinc.jrecycleview.adapter.JRefreshAndLoadMoreAdapter;
import com.coin.libbase.callback.IStateListener;
import com.coin.libbase.config.JFrameManager;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/21
 * @description
 */

public class JFrameAdapter extends JRefreshAndLoadMoreAdapter {

    private final static int RETRY_TYPE = 0xABC201;
    private final static int LOADING_TYPE = 0xABC202;
    private final static int EMPTY_TYPE = 0xABC203;
    private final static int SUCCESS_TYPE = 0xABC204;

    private int mCurrentType = LOADING_TYPE;
    private LayoutInflater mLayoutInflater;

    private IStateListener mStateListener;

    public JFrameAdapter(Context context, RecyclerView.Adapter adapter) {
        super(context, adapter);
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case RETRY_TYPE:
                return new RetryViewHolder(mLayoutInflater.inflate(JFrameManager.getInstance().getRetryViewLayout(), parent, false));
            case LOADING_TYPE:
                return new LoadingViewHolder(mLayoutInflater.inflate(JFrameManager.getInstance().getLoadingViewLayout(), parent, false));
            case EMPTY_TYPE:
                return new EmptyViewHolder(mLayoutInflater.inflate(JFrameManager.getInstance().getEmptyViewLayout(), parent, false));
        }

        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (this.mStateListener == null) {
            super.onBindViewHolder(holder, position);
            return;
        }

        if (holder instanceof RetryViewHolder) {

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JFrameAdapter.this.mCurrentType = LOADING_TYPE;
                    JFrameAdapter.super.notifyDataSetChanged();
                    JFrameAdapter.this.mStateListener.onRetry();
                }
            });

        } else if (holder instanceof LoadingViewHolder) {

            this.mStateListener.onLoading();

        } else if (holder instanceof EmptyViewHolder) {

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JFrameAdapter.this.mCurrentType = LOADING_TYPE;
                    JFrameAdapter.super.notifyDataSetChanged();
                    JFrameAdapter.this.mStateListener.onEmpty();
                }
            });

        } else {

            super.onBindViewHolder(holder, position);

        }
    }

    @Override
    public int getItemCount() {
        if (isState()) {
            return 1;
        }
        return super.getItemCount();
    }

    public void setStateListener(IStateListener mStateListener) {
        this.mStateListener = mStateListener;
    }

    @Override
    public int getItemViewType(int position) {

        if (this.mCurrentType == SUCCESS_TYPE) {
            return super.getItemViewType(position);
        } else {
            return this.mCurrentType;
        }

    }

    //是否处在loading、retry、empty状态
    public boolean isState() {
        return this.mCurrentType != SUCCESS_TYPE;
    }

    public void onLoading() {
        this.mCurrentType = LOADING_TYPE;
    }

    public void onSuccess() {
        super.setRefreshComplete();
        this.mCurrentType = SUCCESS_TYPE;
        this.notifyDataSetChanged();
    }

    public void onError() {
        this.mCurrentType = RETRY_TYPE;
        super.notifyDataSetChanged();
    }

    public void onEmpty() {
        this.mCurrentType = EMPTY_TYPE;
        super.notifyDataSetChanged();
    }

    class RetryViewHolder extends RecyclerView.ViewHolder {

        public RetryViewHolder(View itemView) {
            super(itemView);
        }

    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {

        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }

    class EmptyViewHolder extends RecyclerView.ViewHolder {

        public EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

}

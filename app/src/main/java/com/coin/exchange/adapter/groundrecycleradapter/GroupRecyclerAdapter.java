/*
 * Copyright (c) 2017. Xi'an iRain IOT Technology service CO., Ltd (ShenZhen). All Rights Reserved.
 */
package com.coin.exchange.adapter.groundrecycleradapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coin.exchange.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 分组的RecyclerViewAdapter
 *
 * @param <G>   Group类型
 * @param <GVH> ViewHolder of the group
 * @param <CVH> ViewHolder of the child
 * @author dean
 */
public abstract class GroupRecyclerAdapter<G, GVH extends RecyclerView.ViewHolder, CVH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter {

    public static final int INVALID_POSITION = -1;

    private static final int TYPE_GROUP = 1;
    private static final int TYPE_CHILD = 2;

    private List<G> mGroups;
    private int mItemCount;

    private OnLongClickListener mOnLongClickListener;
    private OnChildClickListener mOnChildClickListener;
    private OnChildViewClickListener mOnChildViewClickListener;

    public GroupRecyclerAdapter(List<G> groups) {
        mGroups = groups == null ? new ArrayList<G>() : groups;
        updateItemCount();
    }

    public OnLongClickListener getOnLongClickListener() {
        return mOnLongClickListener;
    }

    public void setOnLongClickListener(OnLongClickListener onGroupClickListener) {
        mOnLongClickListener = onGroupClickListener;
    }

    public OnChildClickListener getOnChildClickListener() {
        return mOnChildClickListener;
    }

    public void setOnChildClickListener(OnChildClickListener onChildClickListener) {
        mOnChildClickListener = onChildClickListener;
    }

    public void setOnChildViewClickListener(OnChildViewClickListener onChildViewClickListener) {
        mOnChildViewClickListener = onChildViewClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_GROUP) {
            final GVH viewHolder = onCreateGroupViewHolder(parent);
//            if (mOnLongClickListener != null) {
////                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
////                    @Override
////                    public boolean onLongClick(View v) {
////                        if (mOnLongClickListener != null) {
////                            final int itemPosition = viewHolder.getAdapterPosition();
////                            final Position position = getGroupChildPosition(itemPosition);
////                            mOnLongClickListener.onLongItemClick(v, position.group, position.child);
////                        }
////                        return true;
////                    }
////                });
////            }
            return viewHolder;
        } else {
            final CVH viewHolder = onCreateChildViewHolder(parent);
            if (mOnChildClickListener != null) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnChildClickListener != null) {
                            final int itemPosition = viewHolder.getAdapterPosition();
                            final Position position = getGroupChildPosition(itemPosition);
                            TextView icon = viewHolder.itemView.findViewById(R.id.tv_name);
                            TextView time = viewHolder.itemView.findViewById(R.id.tv_show_time);
                            String showTime = icon.getText().toString() + time.getText().toString();
                            mOnChildClickListener.onChildClick(v, position.group, position.child, showTime);
                        }
                    }
                });
            }

            if (mOnLongClickListener != null) {
                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (mOnLongClickListener != null) {
                            final int itemPosition = viewHolder.getAdapterPosition();
                            final Position position = getGroupChildPosition(itemPosition);
                            mOnLongClickListener.onLongItemClick(v, position.group, position.child);
                        }
                        return true;
                    }
                });
            }

            if (mOnChildViewClickListener != null) {
                viewHolder.itemView.findViewById(R.id.ll_click_collection).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int itemPosition = viewHolder.getAdapterPosition();
                        final Position position = getGroupChildPosition(itemPosition);
                        TextView icon = viewHolder.itemView.findViewById(R.id.tv_name);
                        TextView time = viewHolder.itemView.findViewById(R.id.tv_show_time);
                        String showTime = icon.getText().toString() + time.getText().toString();
                        mOnChildViewClickListener.onChildViewClick(v, position.group, position.child, showTime);
                    }
                });
            }
            return viewHolder;
        }
    }

    protected abstract GVH onCreateGroupViewHolder(ViewGroup parent);

    protected abstract CVH onCreateChildViewHolder(ViewGroup parent);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int itemPosition) {
        Position position = getGroupChildPosition(itemPosition);
        if (position.child == -1) {
            onBindGroupViewHolder((GVH) holder, position.group);
        } else {
            onBindChildViewHolder((CVH) holder, position.group, position.child);
        }
    }

    public Position getGroupChildPosition(int itemPosition) {
        int itemCount = 0;
        int childCount;
        final Position position = new Position();
        for (G g : mGroups) {
            if (itemPosition == itemCount) {
                position.child = INVALID_POSITION;
                return position;
            }
            itemCount++;
            position.child = itemPosition - itemCount;
            childCount = getChildCount(g);
            if (position.child < childCount) {
                return position;
            }
            itemCount += childCount;
            position.group++;
        }
        return position;
    }


    protected abstract void onBindGroupViewHolder(GVH holder, int groupPosition);

    protected abstract void onBindChildViewHolder(CVH holder, int groupPosition, int childPosition);

    @Override
    public int getItemCount() {
        return mItemCount;
    }

    @Override
    public int getItemViewType(int position) {
        return getItemType(position) == ItemType.GROUP_TITLE ? TYPE_GROUP : TYPE_CHILD;
    }

    private void updateItemCount() {
        int count = 0;
        for (G group : mGroups) {
            count += getChildCount(group) + 1;
        }
        mItemCount = count;
    }

    public int getGroupCount() {
        return mGroups.size();
    }

    protected abstract int getChildCount(G group);

    public void add(List<G> groups) {
        int lastCount = getItemCount();
        addGroups(groups);
        updateItemCount();
        notifyItemRangeInserted(lastCount, mItemCount - lastCount);
    }

    public void update(List<G> groups) {
        mGroups.clear();
        addGroups(groups);
        updateItemCount();
        notifyDataSetChanged();
    }

    private void addGroups(List<G> groups) {
        if (groups != null) {
            mGroups.addAll(groups);
        }
    }

    public G getGroup(int groupPosition) {
        return mGroups.get(groupPosition);
    }

    public ItemType getItemType(final int itemPosition) {
        int count = 0;
        for (G g : mGroups) {
            if (itemPosition == count) {
                return ItemType.GROUP_TITLE;
            }
            count += 1;
            if (itemPosition == count) {
                return ItemType.FIRST_CHILD;
            }
            count += getChildCount(g);
            if (itemPosition < count) {
                return ItemType.NOT_FIRST_CHILD;
            }
        }
        throw new IllegalStateException("Could not find item type for item position " + itemPosition);
    }

    public enum ItemType {
        GROUP_TITLE,
        FIRST_CHILD,
        NOT_FIRST_CHILD
    }

    public static class Position {
        public int group;
        public int child = INVALID_POSITION;
    }

    /**
     * 长按的的回调事件
     *
     * @author dean
     * @since 2018-09-04
     */
    public interface OnLongClickListener {
        /**
         * Callback when the group item was clicked.
         *
         * @param itemView      the itemView of the group item.
         * @param groupPosition the position of the group.
         */
        void onLongItemClick(View itemView, int groupPosition, int childPosition);
    }

    /**
     * Child 被点击的回调事件。
     *
     * @author dean
     * @since 2018-09-04
     */
    public interface OnChildClickListener {
        /**
         * Callback when the child item was clicked.
         *
         * @param itemView      the itemView of the child item.
         * @param groupPosition the position of the group that the child item was clicked.
         * @param childPosition the position of the child in group.
         */
        void onChildClick(View itemView, int groupPosition, int childPosition, String time);
    }

    public interface OnChildViewClickListener {
        /**
         * Callback when the child item was clicked.
         *
         * @param itemView      the itemView of the child item.
         * @param groupPosition the position of the group that the child item was clicked.
         * @param childPosition the position of the child in group.
         */
        void onChildViewClick(View itemView, int groupPosition, int childPosition, String time);
    }
}

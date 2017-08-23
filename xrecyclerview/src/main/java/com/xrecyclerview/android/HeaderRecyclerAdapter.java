/*
 * Copyright (c) 2016. wuzhen All rights reserved.
 */

package com.xrecyclerview.android;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * 支持 Header 和 Footer 的 {@link RecyclerView.Adapter}。
 *
 * @author wuzhen
 * @version 2017-08-23
 */
public class HeaderRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int mSpanCount;
    private boolean mStaggeredGrid;

    private RecyclerView.Adapter mWrapAdapter;

    private ArrayList<View> mHeaderViews;
    private ArrayList<View> mFooterViews;

    public HeaderRecyclerAdapter(ArrayList<View> headerViews, ArrayList<View> footerViews,
                                 RecyclerView.Adapter adapter, int spanCount, boolean isStaggered) {
        this.mSpanCount = spanCount;
        this.mStaggeredGrid = isStaggered;
        this.mWrapAdapter = adapter;
        this.mHeaderViews = (headerViews == null ? new ArrayList<View>() : headerViews);
        this.mFooterViews = (footerViews == null ? new ArrayList<View>() : footerViews);
    }

    /**
     * 判断指定位置的 Item 是否为 HeaderView。
     *
     * @param position 指定的位置
     * @return 是否为 HeaderView
     */
    public boolean isHeaderView(int position) {
        return position >= 0 && position < mHeaderViews.size();
    }

    /**
     * 判断指定位置的 Item 是否为 FooterView。
     *
     * @param position 指定的位置
     * @return 是否为 FooterView
     */
    public boolean isFooterView(int position) {
        int itemCount = getItemCount();
        return position < itemCount && position >= itemCount - mFooterViews.size();
    }

    /**
     * 获取 HeaderView 的数量。
     *
     * @return HeaderView 的数量
     */
    public int getHeaderViewCount() {
        return mHeaderViews.size();
    }

    /**
     * 获取 FooterView 的数量。
     *
     * @return FooterView 的数量
     */
    public int getFooterViewCount() {
        return mFooterViews.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final int headerRangeMin = RecyclerView.INVALID_TYPE - getHeaderViewCount();
        final int footerRangeMin = headerRangeMin - getFooterViewCount();

        if (viewType <= RecyclerView.INVALID_TYPE && viewType > headerRangeMin) { // header
            int headerIndex = RecyclerView.INVALID_TYPE - viewType;
            return new HeaderViewHolder(mHeaderViews.get(headerIndex));
        } else if (viewType <= headerRangeMin && viewType > footerRangeMin) { // footer
            int footerIndex = headerRangeMin - viewType;
            return new HeaderViewHolder(mFooterViews.get(footerIndex));
        }
        return mWrapAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int numHeaders = getHeaderViewCount();
        if (position < numHeaders) {
            return;
        }

        int adjPosition = position - numHeaders;
        int adapterCount;
        if (mWrapAdapter != null) {
            adapterCount = mWrapAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                mWrapAdapter.onBindViewHolder(holder, adjPosition);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mWrapAdapter != null) {
            return getHeaderViewCount() + getFooterViewCount() + mWrapAdapter.getItemCount();
        } else {
            return getHeaderViewCount() + getFooterViewCount();
        }
    }

    @Override
    public int getItemViewType(int position) {
        int numHeaders = getHeaderViewCount();
        if (position < numHeaders) { // header
            return RecyclerView.INVALID_TYPE - position;
        }
        int adjPosition = position - numHeaders;
        int adapterCount = 0;
        if (mWrapAdapter != null) {
            adapterCount = mWrapAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                return mWrapAdapter.getItemViewType(adjPosition);
            }
        }
        return RecyclerView.INVALID_TYPE - numHeaders - (position - numHeaders - adapterCount);
    }

    @Override
    public long getItemId(int position) {
        int numHeaders = getHeaderViewCount();
        if (mWrapAdapter != null && position >= numHeaders) {
            int adjPosition = position - numHeaders;
            int adapterCount = mWrapAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                return mWrapAdapter.getItemId(adjPosition);
            }
        }
        return -1;
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {

        HeaderViewHolder(View itemView) {
            super(itemView);

            if (mSpanCount > 1) {
                RecyclerView.LayoutParams rvParams;
                ViewGroup.LayoutParams itemParams = itemView.getLayoutParams();
                if (itemParams == null) {
                    rvParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                            RecyclerView.LayoutParams.WRAP_CONTENT);
                } else if (itemParams instanceof RecyclerView.LayoutParams) {
                    rvParams = (RecyclerView.LayoutParams) itemParams;
                } else {
                    rvParams = new RecyclerView.LayoutParams(itemParams);
                }

                if (mStaggeredGrid) {
                    StaggeredGridLayoutManager.LayoutParams lp = new StaggeredGridLayoutManager.LayoutParams(
                            rvParams);
                    lp.setFullSpan(true);
                    itemView.setLayoutParams(lp);
                } else {
                    GridLayoutManager.LayoutParams lp = new GridLayoutManager.LayoutParams(
                            rvParams);
                    itemView.setLayoutParams(lp);
                }
            }
        }
    }
}
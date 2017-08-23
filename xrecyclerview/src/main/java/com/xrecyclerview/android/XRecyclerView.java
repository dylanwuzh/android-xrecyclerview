/*
 * Copyright (c) 2016. wuzhen All rights reserved.
 */

package com.xrecyclerview.android;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * 支持 Header 和 Footer 的 {@link RecyclerView}。
 *
 * @author wuzhen
 * @version 2017-08-23
 */
public class XRecyclerView extends RecyclerView implements RecyclerView.OnItemTouchListener {

    private ArrayList<View> mHeaderViews = new ArrayList<>();
    private ArrayList<View> mFooterViews = new ArrayList<>();

    private View emptyView;

    private Adapter<?> mAdapter;
    private Adapter mActualAdapter;

    private AdapterDataObserver mAdapterObserver;
    private GestureDetector mGestureDetector;

    private int mSpanCount;
    private boolean mStaggeredGrid;

    private OnItemClickListener mOnItemClickListener;

    public XRecyclerView(Context context) {
        super(context);

        init(context, null, 0);
    }

    public XRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs, 0);
    }

    public XRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.XRecyclerView, defStyle, 0);
        mSpanCount = a.getInteger(R.styleable.XRecyclerView_xrv_spanCount, 1);
        mStaggeredGrid = a.getBoolean(R.styleable.XRecyclerView_xrv_staggered, false);
        a.recycle();

        if (mSpanCount <= 1) {
            LinearLayoutManager llm = new LinearLayoutManager(context);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            setLayoutManager(llm);
        } else {
            if (mStaggeredGrid) {
                StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,
                        StaggeredGridLayoutManager.VERTICAL);
                setLayoutManager(layoutManager);
            } else {
                GridLayoutManager layoutManager = new GridLayoutManager(context, 2,
                        GridLayoutManager.VERTICAL, false);
                setLayoutManager(layoutManager);
            }
        }

        addOnItemTouchListener(this);

        mGestureDetector = new GestureDetector(context,
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        return true;
                    }
                });
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        settingSpanLayoutManager();
    }

    private void settingSpanLayoutManager() {
        LayoutManager layout = getLayoutManager();
        if (layout instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager lm = (StaggeredGridLayoutManager) layout;
            mSpanCount = lm.getSpanCount();
            mStaggeredGrid = true;
        } else if (layout instanceof GridLayoutManager) {
            GridLayoutManager lm = (GridLayoutManager) layout;
            lm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

                @Override
                public int getSpanSize(int position) {
                    if (getAdapter() instanceof HeaderRecyclerAdapter) {
                        HeaderRecyclerAdapter adapter = (HeaderRecyclerAdapter) getAdapter();
                        if (adapter.isHeaderView(position) || adapter.isFooterView(position)) {
                            return mSpanCount;
                        }
                    }
                    return 1;
                }
            });
            mSpanCount = lm.getSpanCount();
            mStaggeredGrid = false;
        } else {
            mSpanCount = 1;
            mStaggeredGrid = false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        if (mOnItemClickListener != null) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && mGestureDetector.onTouchEvent(e)) {
                mOnItemClickListener.onRecyclerItemClick(this, child, rv.getChildAdapterPosition(child));
            }
        }
        return false;
    }

    /**
     * 添加头部视图。
     */
    public void addHeaderView(View view) {
        if (view.getLayoutParams() == null) {
            view.setLayoutParams(
                    new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        }
        mHeaderViews.add(view);
        if (mAdapter != null) {
            if (!(mAdapter instanceof HeaderRecyclerAdapter)) {
                mAdapter = new HeaderRecyclerAdapter(mHeaderViews, mFooterViews, mAdapter,
                        mSpanCount, mStaggeredGrid);
            }
        }
    }

    public int getHeaderViewsCount() {
        return (mHeaderViews == null ? 0 : mHeaderViews.size());
    }

    public View getHeaderViewAt(int index) {
        int headerViewCount = getHeaderViewsCount();
        if (index >= 0 && index < headerViewCount) {
            return mHeaderViews.get(index);
        }
        return null;
    }

    /**
     * 添加脚部视图。
     */
    public void addFooterView(final View view) {
        if (view.getLayoutParams() == null) {
            view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        }
        mFooterViews.add(view);
        if (mAdapter != null) {
            if (!(mAdapter instanceof HeaderRecyclerAdapter)) {
                mAdapter = new HeaderRecyclerAdapter(mHeaderViews, mFooterViews, mAdapter,
                        mSpanCount, mStaggeredGrid);
            }
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        unregisterWrapAdapterObserver();

        HeaderRecyclerAdapter headerRecyclerAdapter = new HeaderRecyclerAdapter(mHeaderViews,
                mFooterViews, adapter, mSpanCount, mStaggeredGrid);
        if (adapter != null) {
            headerRecyclerAdapter.setHasStableIds(adapter.hasStableIds());
        }
        super.setAdapter(headerRecyclerAdapter);

        mAdapter = headerRecyclerAdapter;
        mActualAdapter = adapter;

        registerWrapAdapterObserver();
        mAdapterObserver.onChanged();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        registerWrapAdapterObserver();
    }

    private void registerWrapAdapterObserver() {
        if (mActualAdapter != null && mAdapterObserver == null) {
            mAdapterObserver = new WrapAdapterObserver();
            mActualAdapter.registerAdapterDataObserver(mAdapterObserver);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        unregisterWrapAdapterObserver();
    }

    private void unregisterWrapAdapterObserver() {
        if (mActualAdapter != null && mAdapterObserver != null) {
            mActualAdapter.unregisterAdapterDataObserver(mAdapterObserver);
            mAdapterObserver = null;
        }
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    private class WrapAdapterObserver extends AdapterDataObserver {

        @Override
        public void onChanged() {
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }

            if (mActualAdapter != null && emptyView != null) {
                if (mActualAdapter.getItemCount() == 0) {
                    emptyView.setVisibility(View.VISIBLE);
                    setVisibility(View.GONE);
                } else {
                    emptyView.setVisibility(View.GONE);
                    setVisibility(View.VISIBLE);
                }
            }
        }
    }

    /**
     * 设置 Item 的单击监听事件。
     *
     * @param listener 监听事件
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    /**
     * Item 的单击监听事件。
     */
    public interface OnItemClickListener {

        void onRecyclerItemClick(RecyclerView recyclerView, View itemView, int position);
    }
}

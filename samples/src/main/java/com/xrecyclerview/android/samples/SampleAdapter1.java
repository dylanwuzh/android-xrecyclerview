package com.xrecyclerview.android.samples;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SampleAdapter1 extends RecyclerView.Adapter {

    private int mCount;

    public SampleAdapter1() {
        this.mCount = 10;
    }

    public SampleAdapter1(int count) {
        this.mCount = count;
    }

    public String getItem(int position) {
        if (position >= 0 && position < mCount) {
            return "Item" + position;
        }
        return "NOT ADAPTER ITEM";
    }

    @Override
    public int getItemCount() {
        return mCount;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(parent.getContext(), R.layout.adapter_sample_1, null);
        return new SampleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SampleViewHolder vh = (SampleViewHolder) holder;
        vh.tv.setText(String.valueOf(position));
    }

    private static class SampleViewHolder extends RecyclerView.ViewHolder {

        public TextView tv;

        public SampleViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv);
        }
    }
}

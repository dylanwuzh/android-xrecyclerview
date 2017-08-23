package com.xrecyclerview.android.samples;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.xrecyclerview.android.XRecyclerView;

public class XRecyclerViewDemo4 extends BaseSampleActivity
        implements XRecyclerView.OnItemClickListener {

    XRecyclerView rv;
    SampleAdapter2 adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv_3);

        adapter = new SampleAdapter2();

        rv = (XRecyclerView) findViewById(R.id.rv);
        rv.addHeaderView(View.inflate(this, R.layout.header_1, null));
        rv.addFooterView(View.inflate(this, R.layout.footer_1, null));
        rv.setAdapter(adapter);
        rv.setOnItemClickListener(this);
    }

    @Override
    public void onRecyclerItemClick(RecyclerView recyclerView, View itemView, int position) {
        String message = "clicked position: " + position + ", " + adapter.getItem(position - rv.getHeaderViewsCount());
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

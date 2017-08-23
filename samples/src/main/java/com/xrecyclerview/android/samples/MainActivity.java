package com.xrecyclerview.android.samples;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private static final Class<?>[] ACTIVITIES = {
            XRecyclerViewDemo1.class,
            XRecyclerViewDemo2.class,
            XRecyclerViewDemo3.class,
            XRecyclerViewDemo4.class,
    };

    private static final String[] NAMES = {
            "带有Header",
            "带有Header和Footer",
            "多列的RecyclerView",
            "瀑布流"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout layRoot = (LinearLayout) findViewById(R.id.lay_root);
        layRoot.removeAllViews();

        int count = ACTIVITIES.length;
        for (int i = 0; i < count; i++) {
            View child = createChildView(i);
            layRoot.addView(child);
        }
    }

    private View createChildView(final int index) {
        Button button = new Button(this);
        button.setText(NAMES[index]);
        button.setAllCaps(false);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                dp2px(48));
        lp.topMargin = dp2px(16);
        button.setLayoutParams(lp);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ACTIVITIES[index]));
            }
        });
        return button;
    }

    private int dp2px(float dpValue) {
        float value = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getResources().getDisplayMetrics());
        return (int) (value + 0.5f);
    }
}

package com.e.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

public class NoScrollExpandListView extends ExpandableListView {

    public NoScrollExpandListView(Context context) {
        super(context);
    }

    public NoScrollExpandListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollExpandListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}

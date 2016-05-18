package com.e.common.widget.overscroll.adapters;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.widget.ScrollView;

/**
 * An adapter that enables over-scrolling over a {@link ScrollView}.
 * <br/>Seeing that {@link ScrollView} only supports vertical scrolling, this adapter
 * over-scrolling, use {@link HorizontalScrollViewOverScrollDecorAdapter} in conjunction with
 * a {@link android.widget.HorizontalScrollView}.
 *
 * @author amit
 *
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class ScrollViewOverScrollDecorAdapter implements IOverScrollDecoratorAdapter {

    @Override
    public boolean isInAbsoluteStart() {
        return !mView.canScrollVertically(-1);
    }

    protected final ScrollView mView;

    public ScrollViewOverScrollDecorAdapter(ScrollView view) {
        mView = view;
    }

    @Override
    public View getView() {
        return mView;
    }

    @Override
    public boolean isInAbsoluteEnd() {
        return !mView.canScrollVertically(1);
    }
}

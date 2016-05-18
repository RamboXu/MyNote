package com.e.common.widget.overscroll.ext;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

import com.e.common.widget.overscroll.HorizontalOverScrollBounceEffectDecorator;
import com.e.common.widget.overscroll.adapters.HorizontalScrollViewOverScrollDecorAdapter;

/**
 * Created by Evan on 16/3/9.
 * <p/>
 * https://github.com/EverythingMe/OverScrollView
 */
public class OverHorizontalScrollView extends HorizontalScrollView {

    public OverHorizontalScrollView(Context context) {
        super(context);
        setUpScroll();
    }

    public OverHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUpScroll();
    }

    public OverHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setUpScroll();
    }

    void setUpScroll() {
        setUpOverScroll(this);
    }

    public void setUpOverScroll(HorizontalScrollView scrollView) {
        new HorizontalOverScrollBounceEffectDecorator(new HorizontalScrollViewOverScrollDecorAdapter(scrollView));
    }
}

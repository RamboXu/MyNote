package com.e.common.widget.overscroll.ext;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.e.common.widget.overscroll.OverScrollDecoratorHelper;

/**
 * Created by Evan on 16/3/9.
 *
 * https://github.com/EverythingMe/OverScrollView
 */
public class OverScrollView extends ScrollView {

    public OverScrollView(Context context) {
        super(context);
        setUpScroll();
    }

    public OverScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUpScroll();
    }

    public OverScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setUpScroll();
    }

    void setUpScroll() {
        OverScrollDecoratorHelper.setUpOverScroll(this);
    }

}

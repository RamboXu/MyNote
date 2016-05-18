package com.e.common.widget.overscroll.ext.viewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by Evan on 16/3/11.
 */
public class OverViewPager extends OverViewPagerContainer<ViewPager> {

    public OverViewPager(Context context) {
        this(context, null);
    }

    public OverViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OverViewPager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected boolean canOverScrollAtStart() {
        ViewPager viewPager = getOverScrollView();
        PagerAdapter adapter = viewPager.getAdapter();
        if (null != adapter) {
            if (viewPager.getCurrentItem() == 0) {
                return true;
            }
            return false;
        }

        return false;
    }

    @Override
    protected boolean canOverScrollAtEnd() {
        ViewPager viewPager = getOverScrollView();
        PagerAdapter adapter = viewPager.getAdapter();
        if (null != adapter && adapter.getCount() > 0) {
            if (viewPager.getCurrentItem() == adapter.getCount() - 1) {
                return true;
            }
            return false;
        }

        return false;
    }

    @Override
    protected OverViewPagerContainer.OverScrollDirection getOverScrollDirection() {
        return OverViewPagerContainer.OverScrollDirection.Horizontal;
    }

    @Override
    protected ViewPager createOverScrollView() {
        ViewPager viewPager = new ViewPager(getContext());
        return viewPager;
    }
}

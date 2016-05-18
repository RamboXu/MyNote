package com.e.common.widget.overscroll;

import android.view.View;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.ScrollView;

import com.e.common.widget.overscroll.adapters.AbsListViewOverScrollDecorAdapter;
import com.e.common.widget.overscroll.adapters.HorizontalScrollViewOverScrollDecorAdapter;
import com.e.common.widget.overscroll.adapters.ScrollViewOverScrollDecorAdapter;
import com.e.common.widget.overscroll.adapters.StaticOverScrollDecorAdapter;

/**
 * @author amit
 */
public class OverScrollDecoratorHelper {

    public static final int ORIENTATION_VERTICAL = 0;
    public static final int ORIENTATION_HORIZONTAL = 1;

    public static void setUpOverScroll(ListView listView) {
        new VerticalOverScrollBounceEffectDecorator(new AbsListViewOverScrollDecorAdapter(listView));
    }

    public static void setUpOverScroll(GridView gridView) {
        new VerticalOverScrollBounceEffectDecorator(new AbsListViewOverScrollDecorAdapter(gridView));
    }

    public static void setUpOverScroll(ScrollView scrollView) {
        new VerticalOverScrollBounceEffectDecorator(new ScrollViewOverScrollDecorAdapter(scrollView));
    }

    public static void setUpOverScroll(HorizontalScrollView scrollView) {
        new HorizontalOverScrollBounceEffectDecorator(new HorizontalScrollViewOverScrollDecorAdapter(scrollView));
    }

    /**
     * Set up the over-scroll over a generic view, assumed to always be over-scroll ready (e.g.
     * a plain text field, image view).
     *
     * @param view The view.
     * @param orientation One of {@link #ORIENTATION_HORIZONTAL} or {@link #ORIENTATION_VERTICAL}.
     */
    public static void setUpStaticOverScroll(View view, int orientation) {
        switch (orientation) {
            case ORIENTATION_HORIZONTAL:
                new HorizontalOverScrollBounceEffectDecorator(new StaticOverScrollDecorAdapter(view));
                break;

            case ORIENTATION_VERTICAL:
                new VerticalOverScrollBounceEffectDecorator(new StaticOverScrollDecorAdapter(view));
                break;

            default:
                new IllegalArgumentException("orientation");
        }
    }
}

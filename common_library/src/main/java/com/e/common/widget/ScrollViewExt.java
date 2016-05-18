package com.e.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.e.common.widget.overscroll.ext.OverScrollView;

/**
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @class ScrollViewExt Create on 15/10/25 18:18
 * @description
 */
public class ScrollViewExt extends OverScrollView {

    private ScrollViewListener scrollViewListener = null;

    private GestureDetector mGestureDetector;

    public ScrollViewExt(Context context, AttributeSet attrs,
                         int defStyle) {super(context, attrs, defStyle);

        mGestureDetector = new GestureDetector(context, new YScrollDetector());
    }

    public ScrollViewExt(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev)
                && mGestureDetector.onTouchEvent(ev);
    }

    public interface ScrollViewListener {
        void onScrollChanged(ScrollViewExt scrollView, int x, int y, int oldx, int oldy);
    }

    class YScrollDetector extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            //如果我们滚动更接近水平方向,返回false,让子视图来处理它
            return (Math.abs(distanceY) > Math.abs(distanceX));
        }
    }
}

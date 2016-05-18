package com.e.common.widget.overscroll.ext.viewpager;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;

/**
 * Created by Evan on 16/3/11.
 */
abstract public class OverViewPagerContainer<T extends View> extends RelativeLayout {

    private T mOverScrollView = null;

    private boolean mIsBeingDragged = false;

    private float mMotionBeginX = 0;

    private float mMotionBeginY = 0;

    private int mTouchSlop;

    public enum OverScrollDirection {
        Horizontal, Vertical,
    }

    abstract protected boolean canOverScrollAtStart();

    abstract protected boolean canOverScrollAtEnd();

    abstract protected OverScrollDirection getOverScrollDirection();

    abstract protected T createOverScrollView();

    public OverViewPagerContainer(Context context) {
        this(context, null);
    }

    public OverViewPagerContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OverViewPagerContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mOverScrollView = createOverScrollView();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        addView(mOverScrollView, layoutParams);

        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    public T getOverScrollView() {
        return mOverScrollView;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        int action = ev.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            mMotionBeginX = ev.getX();
            mMotionBeginY = ev.getY();
            mIsBeingDragged = false;
        } else if (action == MotionEvent.ACTION_MOVE) {
            if (mIsBeingDragged == false) {
                float scrollDirectionDiff = 0f;
                float anotherDirectionDiff = 0f;
                if (getOverScrollDirection() == OverScrollDirection.Horizontal) {
                    scrollDirectionDiff = ev.getX() - mMotionBeginX;
                    anotherDirectionDiff = ev.getY() - mMotionBeginY;
                } else if (getOverScrollDirection() == OverScrollDirection.Vertical) {
                    scrollDirectionDiff = ev.getY() - mMotionBeginY;
                    anotherDirectionDiff = ev.getX() - mMotionBeginX;
                }
                float absScrollDirectionDiff = Math.abs(scrollDirectionDiff);
                float absAnotherDirectionDiff = Math.abs(anotherDirectionDiff);
                if (absScrollDirectionDiff > mTouchSlop && absScrollDirectionDiff > absAnotherDirectionDiff) {
                    if (canOverScrollAtStart() && scrollDirectionDiff > 0f) {
                        mIsBeingDragged = true;
                    } else if (canOverScrollAtEnd() && scrollDirectionDiff < 0f) {
                        mIsBeingDragged = true;
                    }
                }
            }
        }
        return mIsBeingDragged;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float moveOffset = 0;
        if (getOverScrollDirection() == OverScrollDirection.Horizontal) {
            moveOffset = event.getX() - mMotionBeginX;
        } else if (getOverScrollDirection() == OverScrollDirection.Vertical) {
            moveOffset = event.getY() - mMotionBeginY;
        }
        moveOffset *= 0.5f;
        if (action == MotionEvent.ACTION_MOVE) {

            if (getOverScrollDirection() == OverScrollDirection.Horizontal) {
                moveOverScrollView(moveOffset, 0);
            } else if (getOverScrollDirection() == OverScrollDirection.Vertical) {
                moveOverScrollView(0, moveOffset);
            }
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            resetOverScrollViewWithAnimation(moveOffset, event.getY());
            mIsBeingDragged = false;
        }

        return true;
    }

    private void moveOverScrollView(float currentX, float currentY) {
        if (getOverScrollDirection() == OverScrollDirection.Horizontal) {
            scrollTo(-(int) currentX, 0);
        } else if (getOverScrollDirection() == OverScrollDirection.Vertical) {
            scrollTo(0, -(int) currentY);
        }
    }

    private void resetOverScrollViewWithAnimation(float currentX, float currentY) {
        Interpolator scrollAnimationInterpolator = new DecelerateInterpolator();
        SmoothScrollRunnable smoothScrollRunnable = new SmoothScrollRunnable((int) currentX, 0, 300, scrollAnimationInterpolator);
        post(smoothScrollRunnable);
    }

    final class SmoothScrollRunnable implements Runnable {
        private final Interpolator mInterpolator;
        private final int mScrollToPosition;
        private final int mScrollFromPosition;
        private final long mDuration;

        private boolean mContinueRunning = true;
        private long mStartTime = -1;
        private int mCurrentPosition = -1;

        public SmoothScrollRunnable(int fromPosition, int toPosition, long duration, Interpolator scrollAnimationInterpolator) {
            mScrollFromPosition = fromPosition;
            mScrollToPosition = toPosition;
            mInterpolator = scrollAnimationInterpolator;
            mDuration = duration;
        }

        @Override
        public void run() {

            /**
             * Only set mStartTime if this is the first time we're starting,
             * else actually calculate the Y delta
             */
            if (mStartTime == -1) {
                mStartTime = System.currentTimeMillis();
            } else {

                /**
                 * We do do all calculations in long to reduce software float
                 * calculations. We use 1000 as it gives us good accuracy and
                 * small rounding errors
                 */
                long normalizedTime = (1000 * (System.currentTimeMillis() - mStartTime)) / mDuration;
                normalizedTime = Math.max(Math.min(normalizedTime, 1000), 0);

                final int deltaY = Math.round((mScrollFromPosition - mScrollToPosition)
                        * mInterpolator.getInterpolation(normalizedTime / 1000f));
                mCurrentPosition = mScrollFromPosition - deltaY;

                if (getOverScrollDirection() == OverScrollDirection.Horizontal) {
                    moveOverScrollView(mCurrentPosition, 0);
                } else if (getOverScrollDirection() == OverScrollDirection.Vertical) {
                    moveOverScrollView(0, mCurrentPosition);
                }
            }

            if (mContinueRunning && mScrollToPosition != mCurrentPosition) {
                ViewCompat.postOnAnimation(OverViewPagerContainer.this, this);
            }
        }

        public void stop() {
            mContinueRunning = false;
            removeCallbacks(this);
        }
    }

}

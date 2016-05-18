package com.e.common.widget.overscroll.ext;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.e.common.utility.CommonUtility;

import java.util.List;

/**
 * Created by Evan on 16/3/9.
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public class OverListView extends ListView {

    public OverListView(Context context) {
        super(context);
        setUpScroll();
    }

    public OverListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUpScroll();
    }

    public OverListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setUpScroll();
    }

    void setUpScroll() {
//        OverScrollDecoratorHelper.setUpOverScroll(this);
    }

    private BaseAdapter mListAdapter;

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        if (adapter instanceof BaseAdapter) {
            mListAdapter = (BaseAdapter) adapter;
        }
    }

    public void deleteCellByObject(final View v, final Object obj, final List dataSource) {
        final int initialHeight = v.getHeight();
        Animation.AnimationListener al = new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                dataSource.remove(obj);
                if (!CommonUtility.Utility.isNull(mListAdapter)) {
                    v.getLayoutParams().height = initialHeight;
                    mListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
        };

        collapse(v, al);
    }

    public void deleteCellByPosition(final View v, final int index, final List dataSource) {
        final int initialHeight = v.getHeight();
        Animation.AnimationListener al = new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                dataSource.remove(index);
                if (!CommonUtility.Utility.isNull(mListAdapter)) {
                    v.getLayoutParams().height = initialHeight;
                    mListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
        };

        collapse(v, al);
    }

    private void collapse(final View v, Animation.AnimationListener al) {
        final int initialHeight = v.getHeight();

        Animation anim = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                    v.setAnimation(null);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        if (al != null) {
            anim.setAnimationListener(al);
        }
        anim.setDuration(300);
        v.startAnimation(anim);
    }

}

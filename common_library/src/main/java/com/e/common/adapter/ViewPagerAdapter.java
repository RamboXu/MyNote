package com.e.common.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


/**
 * Created by Evan on 15/2/4.
 */
public class ViewPagerAdapter extends PagerAdapter {

    private List<? extends View> mViews;

    public ViewPagerAdapter(List<? extends View> views) {
        this.mViews = views;
    }

    @Override
    public int getCount() {
        return mViews.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeAllViews();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViews.get(position)); // 把被点击的view放入缓存中
        return mViews.get(position); // 返回被点击图片对象

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}

package com.e.common.widget.viewpager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.e.common.R;
import com.e.common.widget.image.ImageLoaderView;
import com.e.common.widget.viewpager.page.CommonBanner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evan on 16/4/11.
 */
public class RecyleAdapter extends RecyclingPagerAdapter {

    private Context mContext;
    private List<CommonBanner> banners = new ArrayList<>();
    private boolean isLoop = true;

    public RecyleAdapter(Context context) {
        mContext = context;
    }

    /**
     * get really position
     *
     * @param position
     * @return
     */
    public int getPosition(int position) {
        return isLoop ? position % getRealCount() : position;
    }

    @Override
    public int getCount() {
        return isLoop ? getRealCount() * 100 : getRealCount();
    }

    public int getRealCount() {
        return banners.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup container) {

        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_banner, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        final CommonBanner page = banners.get(getPosition(position));

        if (page.onPageClickListener != null) {
            holder.mBannerImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    page.onPageClickListener.onPageClick(getPosition(position), page);
                }
            });
        }
        holder.mBannerImageView.loadImage(page.url);
        return convertView;
    }

    private static class ViewHolder {
        final ImageLoaderView mBannerImageView;

        public ViewHolder(View view) {
            mBannerImageView = (ImageLoaderView) view.findViewById(R.id.slider_image);
        }
    }

    public void setBanners(List<CommonBanner> banners) {
        this.banners.clear();
        this.banners.addAll(banners);
    }

    /**
     * @return the is Loop
     */
    public boolean isLoop() {
        return isLoop;
    }

    /**
     * @param isLoop the is InfiniteLoop to set
     */
    public void setLoop(boolean isLoop) {
        this.isLoop = isLoop;
        notifyDataSetChanged();
    }

}
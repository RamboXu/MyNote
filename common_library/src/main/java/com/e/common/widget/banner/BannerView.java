package com.e.common.widget.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.e.common.R;
import com.e.common.utility.CommonUtility;
import com.e.common.widget.image.ImageLoaderView;
import com.e.common.widget.viewpager.InfiniteIndicator;
import com.e.common.widget.viewpager.page.CommonBanner;
import com.e.common.widget.viewpager.page.OnPageClickListener;

import org.androidannotations.annotations.EViewGroup;

import java.util.List;

/**
 * Created by mark on 16/3/8.
 */
@EViewGroup
public abstract class BannerView extends LinearLayout implements OnPageClickListener {

    public IBannerClickCallback mBannerClickCallback;

    public interface IBannerClickCallback {
        void getCallback(Object banner);

    }

    public void setBannerClickCallback(IBannerClickCallback mBannerClickCallback) {
        this.mBannerClickCallback = mBannerClickCallback;
    }

    View mLayoutBanner;

    ImageLoaderView mEmptyBannerView;

    LayoutParams mLayoutParams;

    private InfiniteIndicator mBannerIndicator;

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public void init(AttributeSet attributeSet) {

        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet,
                R.styleable.BannerView, 0, 0);

        CommonUtility.UIUtility.inflate(R.layout.view_banner, this);

        mLayoutBanner = findViewById(R.id.layout_banner);

        mEmptyBannerView = (ImageLoaderView) findViewById(R.id.iv_empty_banner);
        mBannerIndicator = (InfiniteIndicator) findViewById(R.id.bannerIndicator);

        //banner规格默认为1:4
        float aspectRatio = typedArray.getFloat(R.styleable.BannerView_bannerAspectRatio, 0.25f);

        final int width = CommonUtility.UIUtility.getScreenWidth(getContext());

        int height = (int) (width * aspectRatio);

        mLayoutParams = new LayoutParams(width, height);
//
//        // 每个圆点的默认颜色
//        int pageIndicatorDefaultColor = typedArray.getColor(R.styleable.BannerView_bannerPageIndicatorDefaultColor, 0x60ffffff);
//        mPageIndicator.setPageColor(pageIndicatorDefaultColor);
//
//        // 每个圆点的边框颜色
//        int pageIndicatorStrokeColor = typedArray.getColor(R.styleable.BannerView_bannerPageIndicatorStrokeColor, 0x00ffffff);
//        mPageIndicator.setStrokeColor(pageIndicatorStrokeColor);
//
//        int pageIndicatorSelectedColor = typedArray.getColor(R.styleable.BannerView_bannerPageIndicatorSelectedColor, 0xff0bd151);
//
//        mPageIndicator.setFillColor(pageIndicatorSelectedColor); // 滑动时的圆点颜色
//
//        mPageIndicator.setViewPager(mViewPager.getOverScrollView());

        int defaultEmptyDrawable = typedArray.getResourceId(R.styleable.BannerView_bannerEmptyDrawableResource, R.drawable.default_banner_empty);
        mEmptyBannerView.loadLocalDrawable(defaultEmptyDrawable);

        mLayoutBanner.setLayoutParams(mLayoutParams);
    }

    public void setBannerDataSource(List<CommonBanner> banners) {
        if (!CommonUtility.Utility.isNull(banners) && !banners.isEmpty()) {
            mBannerIndicator.addPages(banners);
            mBannerIndicator.setPosition(InfiniteIndicator.IndicatorPosition.Center_Bottom);
        } else {
            mEmptyBannerView.setVisibility(View.VISIBLE);
        }
    }
}

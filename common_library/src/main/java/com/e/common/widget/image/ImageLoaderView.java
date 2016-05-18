/**
 * @{#} ImageLoaderView.java Create on 2014年11月13日 上午10:18:44
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @description
 */
package com.e.common.widget.image;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.e.common.R;
import com.e.common.activity.ImageShowActivity;
import com.e.common.constant.Constants;
import com.e.common.manager.init.InitManager_;
import com.e.common.utility.CommonUtility;

public class ImageLoaderView extends ImageView {


    /**
     * 是否显示为圆角图片
     */
    private boolean mIsRoundCorner;

    /**
     * 是否显示为圆形图片
     */
    private boolean mIsRound;

    private float mImageLoaderWidthHeightRate;

    // 是否点击跳转到大图预览
    private boolean mImageLoaderClick2preview;

    private String mUrl;

    private Drawable mEmptyDrawable;

    private Drawable mLoadingDrawable;

    private Drawable mFailDrawable;

    private int mRoundCornerRate = 3;

    private int mDefaultSrc;

    private boolean isFadeIn; //图片是否执行淡入效果，默认为1000毫秒

    private boolean isInited;

    private String mImageSize; //取服务器指定宽度图片

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public ImageLoaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    /**
     * @param context
     * @param attrs
     */
    public ImageLoaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    /**
     * @param context
     */
    public ImageLoaderView(Context context) {
        super(context);
        init(null);
    }

    /**
     * @return the mIsRoundCorner
     */
    public boolean ismIsRoundCorner() {
        return mIsRoundCorner;
    }

    /**
     * @param mIsRoundCorner the mIsRoundCorner to set
     */
    public void setmIsRoundCorner(boolean mIsRoundCorner) {
        this.mIsRoundCorner = mIsRoundCorner;
    }

    public void setRoundCornerRate(int mRoundCornerRate) {
        this.mRoundCornerRate = mRoundCornerRate;
    }

    /**
     * @return the mIsRound
     */
    public boolean ismIsRound() {
        return mIsRound;
    }

    /**
     * @param mIsRound the mIsRound to set
     */
    public void setmIsRound(boolean mIsRound) {
        this.mIsRound = mIsRound;
    }

    /**
     * @return the mImageLoaderWidthHeightRate
     */
    public float getmImageLoaderWidthHeightRate() {
        return mImageLoaderWidthHeightRate;
    }

    /**
     * @param mImageLoaderWidthHeightRate the mImageLoaderWidthHeightRate to set
     */
    public void setmImageLoaderWidthHeightRate(float mImageLoaderWidthHeightRate) {
        this.mImageLoaderWidthHeightRate = mImageLoaderWidthHeightRate;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mImageLoaderWidthHeightRate > 0) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);
            height = (int) (width * mImageLoaderWidthHeightRate);
            setMeasuredDimension(width, height);
        }
    }

    /**
     * 初始化属性 method desc：
     *
     * @param attrs isRound isRoundCorner isRound为true时，isRoundCorner则无效
     */
    public void init(AttributeSet attrs) {
        if (!CommonUtility.Utility.isNull(attrs)) {
            TypedArray attributes = getContext().obtainStyledAttributes(attrs,
                    R.styleable.ImageLoaderAttr, 0, 0);
            mIsRound = attributes.getBoolean(
                    R.styleable.ImageLoaderAttr_imageLoader_round, false);
            mIsRoundCorner = attributes.getBoolean(
                    R.styleable.ImageLoaderAttr_imageLoader_roundCorner, false);
            mImageLoaderWidthHeightRate = attributes.getFloat(
                    R.styleable.ImageLoaderAttr_imageLoader_width_height_rate,
                    0);
            mImageLoaderClick2preview = attributes.getBoolean(
                    R.styleable.ImageLoaderAttr_imageLoader_click2preview,
                    false);
            mRoundCornerRate = attributes.getInteger(R.styleable.ImageLoaderAttr_imageLoader_roundCorner_size, 3);
            mEmptyDrawable = attributes.getDrawable(R.styleable.ImageLoaderAttr_imageLoader_empty);
            mLoadingDrawable = attributes.getDrawable(R.styleable.ImageLoaderAttr_imageLoader_loading);
            mFailDrawable = attributes.getDrawable(R.styleable.ImageLoaderAttr_imageLoader_fail);
            isFadeIn = attributes.getBoolean(R.styleable.ImageLoaderAttr_imageLoader_isFadeIn, false);
            mImageSize = attributes.getString(R.styleable.ImageLoaderAttr_imageLoader_size);

//            int drawable = attributes.getResourceId(com.android.internal.R.styleable.ImageView_src, 0);//无法获取，受限制
            mDefaultSrc = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", 0);
            if (mDefaultSrc != 0) {
                loadLocalDrawable(mDefaultSrc, true);
            }
            attributes.recycle();
        }

        if (mImageLoaderClick2preview) {
            setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (!CommonUtility.Utility.isNull(mUrl)) {
                        Intent intent = new Intent(getContext(), ImageShowActivity.class);
                        intent.putExtra(Constants.IDENTITY.IDENTITY_FILEPATH, mUrl);
                        getContext().startActivity(intent);
                    }
                }
            });
        }

        isInited = true;
    }

    /**
     * 根据uri地址加载图片，根据options进行压缩 method desc：
     *
     * @param url     图片源地址
     * @param options 图片压缩对象
     */
    public void loadImage(String url, DisplayImageOptions options) {
        load(url, options, null);
    }

    public void loadImage(String url, ImageLoadingListener listener) {
        load(url, buildOptions(false).build(), listener);
    }

    public void loadImage(String url, int failImage) {
        loadImage(url, failImage, failImage, failImage);
    }

    public void loadImage(String url, int loadImage, int loadingFail, int emptyUri) {
        DisplayImageOptions.Builder build = buildOptions(false);
        if (loadImage != 0) {
            build.showImageOnLoading(loadImage);
        }

        if (loadingFail != 0) {
            build.showImageOnFail(loadingFail);
        }

        if (emptyUri != 0) {
            build.showImageForEmptyUri(emptyUri);
        }

        loadImage(url, build.build());
    }

    /**
     * @param isInitLoad 是否为初始显示图片
     * @return
     */
    DisplayImageOptions.Builder buildOptions(boolean isInitLoad) {
        int roundSize;
        if (mIsRound) {
            roundSize = 300;
        } else if (mIsRoundCorner) {
            roundSize = mRoundCornerRate;
        } else {
            roundSize = 0;
        }
        DisplayImageOptions.Builder build;
        if (isInitLoad) {
            build = InitManager_.getInstance_(getContext()).getOptionsForInitLoad(roundSize);
        } else {
            build = InitManager_.getInstance_(getContext()).getOptionBuilder(roundSize, isFadeIn);
        }
        return build;
    }

    /**
     * 根据uri地址加载图片，无压缩 method desc：
     *
     * @param url 图片源地址
     */
    public void loadImage(String url) {
        if (!CommonUtility.Utility.isNull(url)) {
            url = CommonUtility.ImageUtility.formatUrl(url);
            if (!url.equals(mUrl)) {
                DisplayImageOptions.Builder build = buildOptions(false);
                if (mEmptyDrawable != null) {
                    build.showImageForEmptyUri(mEmptyDrawable);
                }

                if (mLoadingDrawable != null) {
                    build.showImageOnLoading(mLoadingDrawable);
                }

                if (mFailDrawable != null) {
                    build.showImageOnFail(mFailDrawable);
                }

                if (mDefaultSrc != 0 && !url.equals(mUrl)) {
                    build.showImageOnLoading(mDefaultSrc);
                }
                if (url.startsWith("http") && !CommonUtility.Utility.isNull(mImageSize)) {
                    url = CommonUtility.formatString(url, mImageSize);
                }
                load(url, build.build(), null);
            }
        } else {
            setImageResource(mDefaultSrc);
        }
    }

    private void load(String url, final DisplayImageOptions options, ImageLoadingListener listener) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        url = CommonUtility.ImageUtility.formatUrl(url);
        if (!CommonUtility.Utility.isNull(mImageSize)) {
            mUrl = url.replace(mImageSize, "");
        } else {
            mUrl = url;
        }
        /**
         * 如果取缩略图失败，则加载原图
         */
        if (CommonUtility.Utility.isNull(listener) && !CommonUtility.Utility.isNull(mImageSize)) {
            listener = new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    if (!CommonUtility.Utility.isNull(mImageSize) && imageUri.endsWith(mImageSize)) {
                        load(mUrl, options, this);
                    }
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            };
        }

        ImageLoader.getInstance().displayImage(url, this, options, listener);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        if (isInited) {
            super.setImageDrawable(drawable);
        }
    }

    @Override
    public void setImageResource(int resId) {
        loadLocalDrawable(resId);
    }

    /**
     * 加载本地drawable 资源图片
     *
     * @param drawableId
     */
    public void loadLocalDrawable(int drawableId, boolean isInited) {
        mDefaultSrc = drawableId;
        DisplayImageOptions.Builder build = buildOptions(isInited);
        String imageUri = new StringBuilder("drawable://").append(drawableId).toString();

        ImageLoader.getInstance().displayImage(imageUri, this, build.build());
    }

    public void loadLocalDrawable(int drawable) {
        loadLocalDrawable(drawable, true);
    }

    public void setImageLoaderClick2preview(boolean isPreview) {
        mImageLoaderClick2preview = isPreview;
    }

    public String getCurrentPath() {
        return mUrl;
    }

}
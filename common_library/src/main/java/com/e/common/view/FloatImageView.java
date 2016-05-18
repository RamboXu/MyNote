package com.e.common.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.e.common.R;
import com.e.common.utility.CommonUtility;
import com.e.common.widget.image.ImageLoaderView;

/**
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @class FloatImageView.java Create on 2015-12-29 下午4:14
 * @description
 */
public class FloatImageView extends RelativeLayout {

    ImageLoaderView mImageLoaderView;

    private MyCount mc;

    boolean isShow = true;

    private String mLinkUrl; //点击后要跳转的url

    private boolean isRandom; //是否随机位置显示

    public FloatImageView(Context context) {
        super(context);
        init();
    }

    public FloatImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    void init() {
        CommonUtility.UIUtility.inflate(R.layout.view_float, this);

        mImageLoaderView = (ImageLoaderView) findViewById(R.id.image_float);

        mImageLoaderView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShow) {
                    if (!CommonUtility.Utility.isNull(mListener)) {
                        mListener.onImageClick(mLinkUrl);
                        if (isRandom) {
                            setVisibility(GONE);
                        }
                    }
                } else {
                    AnimationSet animationSet = new AnimationSet(true);
                    RotateAnimation rotateAnimation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.8f, Animation.RELATIVE_TO_SELF, 0.5f);
                    rotateAnimation.setDuration(500);
                    animationSet.addAnimation(rotateAnimation);
                    animationSet.setFillAfter(true);
                    mImageLoaderView.startAnimation(animationSet);
                    isShow = true;
                }
                if (!CommonUtility.Utility.isNull(mc)) {
                    mc.cancel();
                    mc.start();
                }
            }
        });
    }

    /**
     * 计时器
     */
    class MyCount extends CountDownTimer {

        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            AnimationSet animationSet = new AnimationSet(true);
            RotateAnimation rotateAnimation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.8f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(500);
            animationSet.addAnimation(rotateAnimation);
            animationSet.setFillAfter(true);
            mImageLoaderView.startAnimation(animationSet);
            isShow = false;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (!isShow) {
                mc.cancel();
            }
        }
    }

    /**
     * 设置banner 图片
     *
     * @param picUrl
     * @param random  1 true  0 false
     * @param percent
     * @param linkUrl 点击要跳转的url
     */
    public void setBannerPic(String picUrl, final int random, final float percent, String linkUrl) {
        mLinkUrl = linkUrl;
        isRandom = random == 1;

        /**
         * 如果为随机，则不转动图片缩回
         */
        if (!isRandom) {
            mc = new MyCount(8000, 1000);
            mc.start();
        }
        mImageLoaderView.loadImage(picUrl, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                //初定宽度为屏幕宽度的20%
                float screenWidth = CommonUtility.UIUtility.getScreenWidth(getContext());
                float imageViewWidth = screenWidth * 0.2f;

                float rate = loadedImage.getWidth() * 1f / loadedImage.getHeight();
                float imageViewHeight = imageViewWidth / rate;

                int screenHeight = CommonUtility.UIUtility.getScreenHeight(getContext());

                RelativeLayout.LayoutParams params = (LayoutParams) mImageLoaderView.getLayoutParams();
                params.height = (int) imageViewHeight;
                params.width = (int) imageViewWidth;
                if (random == 1) {
                    params.alignWithParent = false;
                    int dp50 = getResources().getDimensionPixelSize(R.dimen.size_dp_50);
                    int randomWidth = CommonUtility.getRandom((int) imageViewWidth, (int) (screenWidth - imageViewWidth));
                    int randomHeight = CommonUtility.getRandom(dp50, (int) (screenHeight - (imageViewHeight + dp50 + dp50)));
                    params.rightMargin = (int) (screenWidth - randomWidth);
                    params.topMargin = randomHeight;
                } else {
                    float position = screenHeight * percent / 100;
                    params.topMargin = (int) position;
                }
                mImageLoaderView.setLayoutParams(params);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }

    private OnImageClickListener mListener;

    public void setOnImageClickListener(OnImageClickListener listener) {
        mListener = listener;
    }

    public interface OnImageClickListener {
        void onImageClick(String url);
    }
}

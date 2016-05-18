package com.e.common.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.e.common.R;
import com.e.common.utility.CommonUtility;
import com.e.common.widget.image.ImageLoaderView;

/**
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @class ActionBar.java Create on 2015-02-11 下午1:44
 * @description
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ActionBar extends RelativeLayout {

    private Context mContext;

    protected TextView mTextLeft, mTextNav, mTextRight, mTextBackup;

    protected ImageLoaderView mImageLeft, mImageRight, mImageCenter;

    protected LinearLayout mLayoutNavCenter;

    private View mLayoutNavLeft, mLayoutNavRight;

    private View mBottomLine;

    private View mLayoutRootView;

    Animation mScaleAnimation;

    public ActionBar(Context context) {
        super(context);
        init(null);
    }

    public ActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ActionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    void init(AttributeSet attrs) {
        mContext = getContext();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_actionbar, this);

        TypedArray types = mContext.obtainStyledAttributes(attrs,
                R.styleable.CustomeActionBar);

        mImageRight = (ImageLoaderView) findViewById(R.id.img_nav_right);
        mTextRight = (TextView) findViewById(R.id.text_nav_right);

        mImageLeft = (ImageLoaderView) findViewById(R.id.img_nav_left);
        mTextLeft = (TextView) findViewById(R.id.text_nav_left);

        mTextNav = (TextView) findViewById(R.id.text_nav);
        mTextBackup = (TextView) findViewById(R.id.text_backup);
        mImageCenter = (ImageLoaderView) findViewById(R.id.img_nav_center);
        mLayoutNavRight = findViewById(R.id.ll_nav_right);
        mLayoutNavLeft = findViewById(R.id.ll_nav_left);
        mBottomLine = findViewById(R.id.actionbar_bottom_line);
        mLayoutNavCenter = (LinearLayout) findViewById(R.id.ll_nav_center);
        mLayoutRootView = findViewById(R.id.rl_nav);

        int textColor = types.getColor(R.styleable.CustomeActionBar_actionbar_textColor, Color.TRANSPARENT);
        if (textColor != Color.TRANSPARENT) {
            mTextLeft.setTextColor(textColor);
            mTextRight.setTextColor(textColor);
            mTextNav.setTextColor(textColor);
            mTextBackup.setTextColor(textColor);
        }

        boolean needBottomLine = types.getBoolean(R.styleable.CustomeActionBar_actionbar_needBootomLine,true);
        if (!needBottomLine) {
            mBottomLine.setVisibility(View.GONE);
        }

        mScaleAnimation = AnimationUtils.loadAnimation(mContext, R.anim.pull_loading_scale_big_small);
        boolean needShowLoading = types.getBoolean(R.styleable.CustomeActionBar_actionbar_showLoading,false);
        if(needShowLoading){
            mImageCenter.setVisibility(View.VISIBLE);
            mImageCenter.loadLocalDrawable(R.drawable.qb_tenpay_loading_1);
            mImageCenter.startAnimation(mScaleAnimation);
        }

        types.recycle();
    }

    public void setImageNavLeftResource(int resId) {
        if (!CommonUtility.Utility.isNull(mImageLeft)) {
            mImageLeft.loadLocalDrawable(resId);
            mImageLeft.setVisibility(View.VISIBLE);
        }
    }

    public void setTextNavLeft(String text) {
        if (!CommonUtility.Utility.isNull(mTextLeft)) {
            mTextLeft.setText(text);
            mTextLeft.setVisibility(View.VISIBLE);
        }
    }

    public TextView getTextNavLeft(){
        return mTextLeft;
    }

    public void setImageNavRightResource(int resId) {
        if (!CommonUtility.Utility.isNull(mImageRight)) {
            mImageRight.loadLocalDrawable(resId);
            mImageRight.setVisibility(View.VISIBLE);
            mLayoutNavRight.setVisibility(View.VISIBLE);
        }
    }

    public void setTextNavRight(String text) {
        if (!CommonUtility.Utility.isNull(mTextRight)) {
            mTextRight.setText(text);
            mTextRight.setVisibility(View.VISIBLE);
            mLayoutNavRight.setVisibility(View.VISIBLE);
        }
    }

    public void setNavTitle(String text) {
        if (!CommonUtility.Utility.isNull(mTextNav)) {
            if(!CommonUtility.Utility.isNull(text) && text.length() > 10) {
                text = new StringBuilder(text.substring(0, 10)).append("...").toString();
            }
            mTextNav.setText(text);
            mTextNav.setVisibility(View.VISIBLE);
        }
    }

    public void setNavTitle(Spanned spanned) {
        if (!CommonUtility.Utility.isNull(mTextNav)) {
            mTextNav.setText(spanned);
            mTextNav.setVisibility(View.VISIBLE);
        }
    }

    public void setNavBackTitle(String text) {
        if (!CommonUtility.Utility.isNull(mTextBackup)) {
            mTextBackup.setText(text);
            mTextBackup.setVisibility(View.VISIBLE);
        }
    }

    public void setNavBackTitle(int text) {
        if (!CommonUtility.Utility.isNull(mTextBackup)) {
            mTextBackup.setText(text);
            mTextBackup.setVisibility(View.VISIBLE);
        }
    }


    public void hideNavLeft() {
        if (!CommonUtility.Utility.isNull(mLayoutNavLeft)) {
            mLayoutNavLeft.setVisibility(View.GONE);
        }
    }

    public void hideNavRight() {
        if (!CommonUtility.Utility.isNull(mLayoutNavRight)) {
            mLayoutNavRight.setVisibility(View.GONE);
        }
    }

    public void hideNavLeftImage() {
        if (!CommonUtility.Utility.isNull(mImageLeft)) {
            mImageLeft.setVisibility(View.GONE);
        }
    }

    public void showNavLeft() {
        if (!CommonUtility.Utility.isNull(mLayoutNavLeft)) {
            mLayoutNavLeft.setVisibility(View.VISIBLE);
        }
    }

    public void setImageNavCenterResource(int resId) {
        if (!CommonUtility.Utility.isNull(mImageCenter)) {
            mImageCenter.loadLocalDrawable(resId);
            mImageCenter.setVisibility(View.VISIBLE);
        }
    }

    public void hideBottomLine() {
        mBottomLine.setVisibility(GONE);
    }

    public ImageLoaderView getImageNavCenter() {
        return mImageCenter;
    }

    public ImageLoaderView getImageNavLeft(){
        return mImageLeft;
    }

    public ImageLoaderView getImageNavRight(){
        return mImageRight;
    }

    public LinearLayout getNavCenterLayout() {
        return mLayoutNavCenter;
    }

    public View getNavLeftLayout() {
        return mLayoutNavLeft;
    }

    public View getNavRightLayout() {
        return mLayoutNavRight;
    }


    public TextView getNavTextRight() {
        return mTextRight;
    }
    public TextView getNavTextCenter(){
        return mTextNav;
    }

    public TextView getNavBackupTextCenter(){
        return mTextBackup;
    }

    public void isShowProgressBar(boolean isShow) {
        if (isShow) {
            mImageCenter.setVisibility(View.VISIBLE);
            mImageCenter.loadLocalDrawable(R.drawable.qb_tenpay_loading_1);
            mImageCenter.startAnimation(mScaleAnimation);
        } else {
            mImageCenter.clearAnimation();
            mImageCenter.setVisibility(View.GONE);
        }
    }

    public void setActionBarBackground(int color) {
        setBackgroundColor(color);
        mLayoutRootView.setBackgroundColor(color);
        hideBottomLine();
    }
}

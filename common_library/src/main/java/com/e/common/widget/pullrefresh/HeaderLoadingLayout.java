package com.e.common.widget.pullrefresh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.e.common.R;
import com.e.common.widget.image.ImageLoaderView;

@SuppressLint("InflateParams")
public class HeaderLoadingLayout extends LoadingLayout {

    /**
     * Header的容器
     */
    private ViewGroup mHeaderContainer;
    /**
     * 图片
     * 前者为滑动时根据scale值进行计算
     * 后者为release后自动循环播放动画
     */
    private ImageLoaderView mFrameAnimationImageView;

    /**
     * 进度条
     */
    private ProgressBar mProgressBar;

    private Animation mScaleAnimation;
    /**
     * 状态提示TextView
     */
    private TextView mHintTextView;
    private RelativeLayout mHeadLoaderLinearLayout;

    /**
     * 构造方法
     *
     * @param context context
     */
    public HeaderLoadingLayout(Context context) {
        super(context);
        init();
    }

    /**
     * 构造方法
     *
     * @param context context
     * @param attrs   attrs
     */
    public HeaderLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mHeaderContainer = (RelativeLayout) findViewById(R.id.pull_to_refresh_header_content);
        mFrameAnimationImageView = (ImageLoaderView) findViewById(R.id.pull_to_refresh_header_drawable);
        mProgressBar = (ProgressBar) findViewById(R.id.pull_to_refresh_header_drawable_animation);
        mHintTextView = (TextView) findViewById(R.id.text_pull_to_refresh_header_hint);
        mHeadLoaderLinearLayout = (RelativeLayout) findViewById(R.id.ll_prepare_loading);

        mScaleAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.pull_loading_scale_big_small);
    }

    @Override
    public void setLastUpdatedLabel(CharSequence label) {
        // 如果最后更新的时间的文本是空的话，隐藏前面的标题
        // mHeaderTimeViewTitle.setVisibility(TextUtils.isEmpty(label) ?
        // View.INVISIBLE : View.VISIBLE);
        // mHeaderTimeView.setText(label);
    }

    @Override
    public int getContentSize() {
        if (null != mHeaderContainer) {
            return mHeaderContainer.getHeight();
        }

        return (int) (getResources().getDisplayMetrics().density * 60);
    }

    @Override
    protected View createLoadingView(Context context, AttributeSet attrs) {
        View container = LayoutInflater.from(context).inflate(
                R.layout.pull_to_refresh_header_e, null);
        return container;
    }

    @Override
    protected void onStateChanged(State curState, State oldState) {
        mHeadLoaderLinearLayout.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        super.onStateChanged(curState, oldState);
    }

    @Override
    protected void onReset() {
        mHintTextView.setText(R.string.pull_to_refresh_header_hint_normal);
    }

    @Override
    public void onPull(float scale) {
        mFrameAnimationImageView.loadLocalDrawable(R.drawable.loading);
    }

    @Override
    protected void onPullToRefresh() {
        if (State.RELEASE_TO_REFRESH == getPreState()) {
        }

        mHintTextView.setText(R.string.pull_to_refresh_header_hint_normal);
    }

    @Override
    protected void onReleaseToRefresh() {
        mHintTextView.setText(R.string.pull_to_refresh_header_hint_ready);
    }

    @Override
    protected void onRefreshing() {
        mHeadLoaderLinearLayout.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        mHintTextView.setText(R.string.pull_to_refresh_header_hint_loading);
    }
}

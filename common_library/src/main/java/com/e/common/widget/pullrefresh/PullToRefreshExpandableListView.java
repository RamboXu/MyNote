package com.e.common.widget.pullrefresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

import com.e.common.R;
import com.e.common.utility.CommonUtility;

/**
 * Created by mark on 15/7/20.
 */
public class PullToRefreshExpandableListView extends PullToRefreshBase<ExpandableListView>
        implements AbsListView.OnScrollListener {

    /** ListView */
    private ExpandableListView mExpandableListView;
    /** 用于滑到底部自动加载的Footer */
    private LoadingLayout mLoadMoreFooterLayout;
    /** 滚动的监听器 */
    private AbsListView.OnScrollListener mScrollListener;

    /**
     * 构造方法
     *
     * @param context
     *            context
     */
    public PullToRefreshExpandableListView(Context context) {
        this(context, null);
    }

    /**
     * 构造方法
     *
     * @param context
     *            context
     * @param attrs
     *            attrs
     */
    public PullToRefreshExpandableListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造方法
     *
     * @param context
     *            context
     * @param attrs
     *            attrs
     * @param defStyle
     *            defStyle
     */
    public PullToRefreshExpandableListView(Context context, AttributeSet attrs,int defStyle) {
        super(context, attrs);

        setPullLoadEnabled(false);
    }


    @Override
    protected ExpandableListView createRefreshableView(Context context, AttributeSet attrs) {

        if (!CommonUtility.Utility.isNull(attrs)) {
            TypedArray array = context.obtainStyledAttributes(attrs,
                    R.styleable.SectionListview);
            array.recycle();
        }

        mExpandableListView = (ExpandableListView) LayoutInflater.from(context).inflate(R.layout.view_expandablelistview, null);
        mExpandableListView.setOnScrollListener(this);

        return mExpandableListView;
    }

    /**
     * 设置是否有更多数据的标志
     *
     * @param hasMoreData
     *            true表示还有更多的数据，false表示没有更多数据了
     */
    public void setHasMoreData(boolean hasMoreData) {
        if (!hasMoreData) {
            if (null != mLoadMoreFooterLayout) {
                mLoadMoreFooterLayout.setState(ILoadingLayout.State.NO_MORE_DATA);
            }

            LoadingLayout footerLoadingLayout = getFooterLoadingLayout();
            if (null != footerLoadingLayout) {
                footerLoadingLayout.setState(ILoadingLayout.State.NO_MORE_DATA);
            }
        }
    }

    /**
     * 设置滑动的监听器
     *
     * @param l
     *            监听器
     */
    public void setOnScrollListener(AbsListView.OnScrollListener l) {
        mScrollListener = l;
    }

    @Override
    protected boolean isReadyForPullUp() {
        return isLastItemVisible();
    }

    @Override
    protected boolean isReadyForPullDown() {
        return isFirstItemVisible();
    }

    @Override
    protected void startLoading() {
        super.startLoading();

        if (null != mLoadMoreFooterLayout) {
            mLoadMoreFooterLayout.setState(ILoadingLayout.State.REFRESHING);
        }
    }

    @Override
    public void onPullUpRefreshComplete() {
        super.onPullUpRefreshComplete();

        if (null != mLoadMoreFooterLayout) {
            mLoadMoreFooterLayout.setState(ILoadingLayout.State.RESET);
        }
    }

    @Override
    public void setScrollLoadEnabled(boolean scrollLoadEnabled) {
        super.setScrollLoadEnabled(scrollLoadEnabled);

        if (scrollLoadEnabled) {
            // 设置Footer
            if (null == mLoadMoreFooterLayout) {
                mLoadMoreFooterLayout = new FooterLoadingLayout(getContext());
            }

            if (null == mLoadMoreFooterLayout.getParent()) {
                mExpandableListView.addFooterView(mLoadMoreFooterLayout, null, false);
            }
            mLoadMoreFooterLayout.show(true);
        } else {
            if (null != mLoadMoreFooterLayout) {
                mLoadMoreFooterLayout.show(false);
            }
        }
    }

    @Override
    public LoadingLayout getFooterLoadingLayout() {
        if (isScrollLoadEnabled()) {
            return mLoadMoreFooterLayout;
        }

        return super.getFooterLoadingLayout();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (isScrollLoadEnabled() && hasMoreData()) {
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                    || scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                if (isReadyForPullUp()) {
                    startLoading();
                }
            }
        }

        if (null != mScrollListener) {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (null != mScrollListener) {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
                    totalItemCount);
        }
    }

    /**
     * 表示是否还有更多数据
     *
     * @return true表示还有更多数据
     */
    public boolean hasMoreData() {
        if ((null != mLoadMoreFooterLayout)
                && (mLoadMoreFooterLayout.getState() == ILoadingLayout.State.NO_MORE_DATA)) {
            return false;
        }

        return true;
    }

    /**
     * 判断第一个child是否完全显示出来
     *
     * @return true完全显示出来，否则false
     */
    private boolean isFirstItemVisible() {
        final Adapter adapter = mExpandableListView.getAdapter();

        if (null == adapter || adapter.isEmpty()) {
            return true;
        }

        int mostTop = (mExpandableListView.getChildCount() > 0) ? mExpandableListView.getChildAt(0)
                .getTop() : 0;
        if (mostTop >= 0) {
            return true;
        }

        return false;
    }

    /**
     * 判断最后一个child是否完全显示出来
     *
     * @return true完全显示出来，否则false
     */
    private boolean isLastItemVisible() {
        final Adapter adapter = mExpandableListView.getAdapter();

        if (null == adapter || adapter.isEmpty()) {
            return true;
        }

        final int lastItemPosition = adapter.getCount() - 1;
        final int lastVisiblePosition = mExpandableListView.getLastVisiblePosition();

        /**
         * This check should really just be: lastVisiblePosition ==
         * lastItemPosition, but ListView internally uses a FooterView which
         * messes the positions up. For me we'll just subtract one to account
         * for it and rely on the inner condition which checks getBottom().
         */
        if (lastVisiblePosition >= lastItemPosition - 1) {
            final int childIndex = lastVisiblePosition
                    - mExpandableListView.getFirstVisiblePosition();
            final int childCount = mExpandableListView.getChildCount();
            final int index = Math.min(childIndex, childCount - 1);
            final View lastVisibleChild = mExpandableListView.getChildAt(index);
            if (lastVisibleChild != null) {
                return lastVisibleChild.getBottom() <= mExpandableListView.getBottom();
            }
        }

        return false;
    }

    public void setAdapter(BaseExpandableListAdapter adapter) {
        mExpandableListView.setAdapter(adapter);
    }

    public void hideDivider() {
        mExpandableListView.setDivider(null);
    }

    public void setOnChildClickListener(ExpandableListView.OnChildClickListener onChildClickListener){
        mExpandableListView.setOnChildClickListener(onChildClickListener);
    }

    public void setOnGroupClickListener(ExpandableListView.OnGroupClickListener onGroupClickListener){
        mExpandableListView.setOnGroupClickListener(onGroupClickListener);
    }



    public LoadingLayout getLoadingLayout() {
        return mLoadMoreFooterLayout;
    }

    public void addHeaderView(View view){
        mExpandableListView.addHeaderView(view);
    }


    public void setGroupIndicator(Drawable drawable){
        mExpandableListView.setGroupIndicator(drawable);
    }

    public void hideFooterView(){
        mLoadMoreFooterLayout.show(false);
    }

    public void showFooterView(){
        mLoadMoreFooterLayout.show(true);
    }
}

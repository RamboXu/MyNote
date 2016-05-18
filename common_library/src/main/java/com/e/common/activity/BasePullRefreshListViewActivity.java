package com.e.common.activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.e.common.R;
import com.e.common.adapter.TAdapter;
import com.e.common.event.EventTypeRequest;
import com.e.common.manager.net.NetUtility;
import com.e.common.manager.net.config.Params;
import com.e.common.utility.CommonUtility;
import com.e.common.widget.pullrefresh.PullToRefreshBase;
import com.e.common.widget.pullrefresh.PullToRefreshListView;

import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @class BasePullRefreshListViewActivity.java Create on 2015-03-18 下午1:00
 * @description
 */
public abstract class BasePullRefreshListViewActivity<T> extends BaseActivity implements PullToRefreshBase.OnRefreshListener<ListView> {

    public PullToRefreshListView mPullRefreshListView;
    public TAdapter<T> mAdapter;
    public List<T> mDataSource = new ArrayList<>();
    public JSONObject mData;//整个json数据，防止子类需要
    public Params mParams;
    public String mRespKey;
    //T的class
    Class<T> clazz;
    public int mCurrentPage;
    public String mUrl = "";
    private int mPageSize;
    private boolean isShowDialog = false;
    private View mHeaderView;
    private View mFootView;
    private String mFooterTip;
    private boolean isInitScrollLoad; //记录最开始是否需要上拉加载更多

    private boolean isCache;

    public void initData() {
        //获取T的类型
        Type type = CommonUtility.Utility.getType(getClass(), BasePullRefreshListViewActivity.class);
        CommonUtility.DebugLog.log(getClass().getSimpleName());
        Type[] params = ((ParameterizedType) type).getActualTypeArguments();
        clazz = (Class<T>) params[0];

        mUrl = initUrl();
        mRespKey = initRespKey();
        mPageSize = initPageSize();
        mParams = initParams();
        mParams.put("limit", mPageSize);

        isCache = mParams.isCache();

        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.base_pullListView);

        if (mPullRefreshListView.getRefreshableView().getHeaderViewsCount() == 0) {
            mHeaderView = initHeaderView();
            if (!CommonUtility.Utility.isNull(mHeaderView)) {
                mPullRefreshListView.getRefreshableView().addHeaderView(mHeaderView);
            }
        }
        if (CommonUtility.Utility.isNull(mFootView)) {
            mFootView = initFootView();
            if (!CommonUtility.Utility.isNull(mFootView) && mPullRefreshListView.getRefreshableView().getFooterViewsCount() == 0) {
                mPullRefreshListView.getRefreshableView().addFooterView(mFootView);
            }
        }

        mAdapter = initAdapter();
        mAdapter.updateData(mDataSource);
        mPullRefreshListView.setAdapter(mAdapter);
        mPullRefreshListView.setOnRefreshListener(this);


        mFooterTip = initNoDataTip();

        initListViewOperate();

        if (isAutoLoading()) {
            mPullRefreshListView.doPullRefreshing(true);
        }
        mPullRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                T t = (T) CommonUtility.UIUtility.getObjFromView(view);
                if (!CommonUtility.Utility.isNull(t)) {
                    onListViewItemClick(t);
                    onListViewItemClick(t, position);
                }
            }
        });
    }

    public void getData() {
        if (CommonUtility.Utility.isNull(mParams)) {
            throw new NullPointerException("请求params为空了");
        }
        mParams.put("start", mCurrentPage * mPageSize);
        if (CommonUtility.Utility.isNull(mUrl)) {
            throw new NullPointerException("请求url为空了");
        }
        mRequestInterceptor.request(activity, mUrl, mParams, this, R.id.request_5, isShowDialog);
    }

    public List<T> parseData(JSONObject object) {
        return CommonUtility.JSONObjectUtility.convertJSONArray2Array(object.optJSONArray(mRespKey), clazz);
    }

    @Override
    public void onSuccess(EventTypeRequest event) {
        super.onSuccess(event);
        if (event.getTag() == R.id.request_5) {
            mPullRefreshListView.doComplete();
            mData = event.getData();
            List<T> tempList = parseData(event.getData());
            if (tempList.size() < mPageSize || mPageSize == 0) {
                if (isHideFooterView()) {
                    mPullRefreshListView.setScrollLoadEnabled(false);
                } else {
                    mPullRefreshListView.getFooterLoadingLayout().setHintText(mFooterTip);
                    mPullRefreshListView.setHasMoreData(false);
                }
            } else {
                if (isHideFooterView()) {
                    mPullRefreshListView.setScrollLoadEnabled(true);
                }
                mPullRefreshListView.setHasMoreData(true);
            }
            if (mCurrentPage == 0) {
                mDataSource.clear();
            }
            mDataSource.addAll(tempList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRequestNetBad(EventTypeRequest event) {
        super.onRequestNetBad(event);
        mPullRefreshListView.doComplete();
    }

    @Override
    public void onFail(EventTypeRequest event) {
        super.onFail(event);
        mPullRefreshListView.doComplete();
    }

    /**
     * 初始化请求参数
     *
     * @return
     */
    public abstract Params initParams();

    /**
     * 请求接口url
     *
     * @return
     */
    public abstract String initUrl();

    /**
     * 初始化适配器
     *
     * @return
     */
    public abstract TAdapter<T> initAdapter();

    public void onListViewItemClick(T entity) {
    }

    public void onListViewItemClick(T entity, int position) {
    }

    public int initPageSize() {
        return 20;
    }

    public void initListViewOperate() {
        mPullRefreshListView.setScrollLoadEnabled(true);
        isInitScrollLoad = true;
    }

    public String initRespKey() {
        return "itemList";
    }

    /**
     * 设置没有数据时footer view上显示的提示
     *
     * @return
     */
    public String initNoDataTip() {
        return getResources().getString(R.string.pushmsg_center_no_more_msg);
    }

    /**
     * 设置是否进入界面就请求接口，默认为true
     *
     * @return
     */
    public boolean isAutoLoading() {
        return true;
    }

    /**
     * 初始化header view
     *
     * @return
     */
    public View initHeaderView() {
        return null;
    }

    /**
     * 初始化footer view
     *
     * @return
     */
    public View initFootView() {
        return null;
    }

    /**
     * 是否隐藏footer view提示
     *
     * @return
     */
    public boolean isHideFooterView() {
        return false;
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        if (isInitScrollLoad) {
            mPullRefreshListView.showFooterView();
        }
        //如果需要缓存，则会在取最新数据时将标识告诉网络请求
        if (isCache) {
            NetUtility.addCacheFromMap(mParams, activity);
        }
        mCurrentPage = 0;
        getData();
    }

    @Override
    public void onPullUpToLoadMore(PullToRefreshBase<ListView> refreshView) {
        //如果需要缓存，则会再取最新数据时将标识从网络请求去除，不缓存第一页以上的数据
        if (isCache) {
            NetUtility.removeCacheFromMap(mParams);
        }
        mCurrentPage++;
        getData();
    }

    /**
     * 通知adapter刷新
     */
    public void notifyAdapterDataChange() {
        if (!CommonUtility.Utility.isNull(mAdapter)) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void setFootViewOnClickListener(View.OnClickListener listener) {
        mPullRefreshListView.getFooterLoadingLayout().setOnClickListener(listener);
    }
}
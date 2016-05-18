/**
 * @{#} IActivity.java Create on 2014年10月28日 上午10:37:04
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @description
 */
package com.e.common.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.e.common.R;
import com.e.common.application.BaseApplication;
import com.e.common.event.EventTypeLoginOrLogout;
import com.e.common.event.EventTypeRequest;
import com.e.common.manager.net.NetManager;
import com.e.common.task.net.RequestInterceptor;
import com.e.common.utility.CommonUtility;
import com.e.common.utility.reflect.ReflectHelper;
import com.e.common.utility.reflect.ReflectHelper_;
import com.e.common.utility.reflect.ReflectInterface;
import com.e.common.widget.ActionBar;
import com.e.common.widget.LoadingView;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;

import de.greenrobot.event.EventBus;

@EFragment
public class BaseFragment extends Fragment implements View.OnClickListener {

    public Activity activity;

    public Fragment fragment;

    public ActionBar mActionBar;

    @Bean
    public RequestInterceptor mRequestInterceptor;

    @Bean
    public NetManager mNetManager;

    public BaseApplication mApplication;

    public boolean isPatientClient;

    @Bean
    public ReflectHelper mReflectHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragment = this;
        activity = getActivity();
        EventBus.getDefault().register(this);

        mApplication = (BaseApplication) getActivity().getApplication();
        isPatientClient = CommonUtility.isPatientClient(activity);

        mReflectHelper = ReflectHelper_.getInstance_(activity);
    }

    public void onEventMainThread(EventTypeRequest event) {
        if (event.getTarget() == this) {
            if (event instanceof EventTypeRequest) {
                EventTypeRequest e = event;
                int resultCode = e.getResultCode();
                if (resultCode == EventTypeRequest.RESULT_CODE_EXCEPTION_SERVER) {
                    if (event.isNeedShowErrorTip()) {
                        CommonUtility.UIUtility.toast(activity, R.string.net_tip_server_wrong);
                    }
                    onRequestNetBad(e);
                } else if (resultCode == EventTypeRequest.RESULT_CODE_EXCEPTION_DEVICE) {
                    if (event.isNeedShowErrorTip()) {
                        CommonUtility.UIUtility.toast(activity, R.string.net_tip_no_net);
                    }
                    onRequestNetBad(e);
                } else if (resultCode == EventTypeRequest.RESULT_CODE_EXCEPTION_NET_NOT_GOOD) {
                    if (event.isNeedShowErrorTip()) {
                        CommonUtility.UIUtility.toast(activity, R.string.net_tip_not_good);
                    }
                    onRequestNetBad(event);
                } else {
                    onRequestFinish(e);
                }
            }
        }
    }

    public void onEventMainThread(EventTypeLoginOrLogout event) {
        onLoginOrLogout(event);
    }

    public void onLoginOrLogout(EventTypeLoginOrLogout event) {

    }

    /**
     * method desc：网络请求成功
     *
     * @param event
     */

    public void onRequestFinish(EventTypeRequest event) {
        LoadingView.hide(activity);
        if (mApplication.isRespSuccess(event.getData())) {
            onSuccess(event);
        } else {
            onFail(event);
        }
    }

    public void onSuccess(EventTypeRequest event) {

    }

    public void onFail(EventTypeRequest event) {
    }

    /**
     * method desc：网络有问题
     *
     * @param event
     */
    public void onRequestNetBad(EventTypeRequest event) {
        LoadingView.hide(activity);
        onFail(event);
    }

    protected void initActionBar() {
        mActionBar = (ActionBar) getView().findViewById(R.id.action_bar);
        if (mActionBar != null) {
            //处理actionbar的左右点击事件
            mActionBar.getNavLeftLayout().setOnClickListener(this);
            mActionBar.getNavRightLayout().setOnClickListener(this);
            mActionBar.getNavCenterLayout().setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onResume() {
        super.onResume();
        ReflectInterface i = mReflectHelper.getActivityOnResumeInterface();
        if (!CommonUtility.Utility.isNull(i)) {
            i.invoke(fragment);
        }
    }

    public void onPause() {
        super.onPause();
        ReflectInterface i = mReflectHelper.getActivityOnPauseInterface();
        if (!CommonUtility.Utility.isNull(i)) {
            i.invoke(fragment);
        }
    }
}

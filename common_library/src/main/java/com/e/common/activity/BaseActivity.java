/**
 * @{#} BaseActivity.java Create on 2014年10月28日 上午10:37:04
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @description
 */
package com.e.common.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.e.common.R;
import com.e.common.application.BaseApplication;
import com.e.common.event.EventTypeLoginOrLogout;
import com.e.common.event.EventTypeNetWorkChange;
import com.e.common.event.EventTypeRequest;
import com.e.common.i.IType;
import com.e.common.manager.net.NetManager;
import com.e.common.secret.UserSecret;
import com.e.common.task.net.RequestInterceptor;
import com.e.common.utility.CommonUtility;
import com.e.common.utility.reflect.ReflectHelper;
import com.e.common.utility.reflect.ReflectHelper_;
import com.e.common.utility.reflect.ReflectInterface;
import com.e.common.widget.ActionBar;
import com.e.common.widget.LoadingView;
import com.e.common.widget.wheel.utility.WheelViewUtility;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

@EActivity
public class BaseActivity extends FragmentActivity implements IType, View.OnClickListener {

    @Bean
    public RequestInterceptor mRequestInterceptor;

    @Bean
    public UserSecret mUserSecret;

    @Bean
    public WheelViewUtility mWheelViewUtility;

    @Bean
    public NetManager mNetManager;

    @Bean
    public ReflectHelper mReflectHelper;

    public BaseApplication mApplication;

    public Activity activity;

    public boolean isPatientClient;//是否是患者端

    @Extra
    public boolean isBackHome;//返回的时候，是否需要返回到主界面，用于通知点击打开界面，再点返回就关闭Activity的情况

    // 缓存当前activity图片资源，finish后release
    private ArrayList<Bitmap> bitmaps = new ArrayList<>();

    public boolean isActive = true; //标记当前是否进入运行在前台

    public ActionBar mActionBar;

    public void onEventMainThread(EventTypeRequest event) {
        if (event.getTarget() == activity) {
            if (event instanceof EventTypeRequest) {
                int resultCode = event.getResultCode();
                if (resultCode == EventTypeRequest.RESULT_CODE_EXCEPTION_SERVER) {
                    if (event.isNeedShowErrorTip()) {
                        CommonUtility.UIUtility.toast(activity, R.string.net_tip_server_wrong);
                    }
                    onRequestNetBad(event);
                } else if (resultCode == EventTypeRequest.RESULT_CODE_EXCEPTION_DEVICE) {
                    if (event.isNeedShowErrorTip()) {
                        CommonUtility.UIUtility.toast(activity, R.string.net_tip_no_net);
                    }
                    onRequestNetBad(event);
                } else if (resultCode == EventTypeRequest.RESULT_CODE_EXCEPTION_NET_NOT_GOOD) {
                    if (event.isNeedShowErrorTip()) {
                        CommonUtility.UIUtility.toast(activity, R.string.net_tip_not_good);
                    }
                    onRequestNetBad(event);
                } else {
                    onRequestFinish(event);
                }
            }
        }
    }

    public void onEventMainThread(EventTypeLoginOrLogout event) {
        onLoginOrLogout(event);
    }

    /**
     * method desc：网络请求成功
     *
     * @param event
     */

    public void onRequestFinish(EventTypeRequest event) {
        LoadingView.hide(this);
        if (event.isOtherRedirectUrl() || mApplication.isRespSuccess(event.getData())) {
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
        LoadingView.hide(this);
        onFail(event);
    }

    public void onLoginOrLogout(EventTypeLoginOrLogout event) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);
        activity = this;
        EventBus.getDefault().register(this);

        mApplication = (BaseApplication) getApplication();
        mApplication.putActivity(activity);
        isPatientClient = CommonUtility.isPatientClient(activity);

        setDefaultFontSize();

        mReflectHelper = ReflectHelper_.getInstance_(activity);
    }

    public void onEventMainThread(EventTypeNetWorkChange event) {
        CommonUtility.DebugLog.e("mark", "isConnect:" + event.isConnect + ";type:" + event.netType);
        onNetChanged(event.isConnect, event.netType);
    }

    //子类可以重写该方法,获取对网络改变的通知
    public void onNetChanged(boolean isConnect, int type) {

    }

    @Override
    public void addBitmap(Bitmap bitmap) {
        CommonUtility.BitmapOperateUtility.addBitmap(bitmap, bitmaps);
    }

    @Override
    public void destroyBitmaps() {
        CommonUtility.BitmapOperateUtility.destroyBitmaps(bitmaps);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CommonUtility.UIUtility.hideKeyboard(activity);
        CommonUtility.BitmapOperateUtility.destroyBitmaps(bitmaps);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        if (!CommonUtility.Utility.isNull(mWheelViewUtility) && mWheelViewUtility.isShown()) {
            try {
                mWheelViewUtility.removeWheelView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (isBackHome) {
                mApplication.forwardMainPage(activity);
            }
            super.onBackPressed();
        }
    }

    public void initActionBar() {
        mActionBar = (ActionBar) findViewById(R.id.action_bar);
        //处理actionbar的左右点击事件
        mActionBar.getNavLeftLayout().setOnClickListener(this);
        mActionBar.getNavRightLayout().setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ll_nav_left) {
            CommonUtility.UIUtility.hideKeyboard(activity);
            if (isBackHome) {
                mApplication.forwardMainPage(activity);
            }
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        ReflectInterface i = mReflectHelper.getActivityOnResumeInterface();
        if (!CommonUtility.Utility.isNull(i)) {
            i.invoke(this);
        }

        if (!isActive) {
            onAppActive();
            isActive = true;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setDefaultFontSize();
    }

    @Override
    protected void onPause() {
        super.onPause();

        ReflectInterface i = mReflectHelper.getActivityOnPauseInterface();
        if (!CommonUtility.Utility.isNull(i)) {
            i.invoke(this);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        ReflectInterface i = mReflectHelper.getReflectInterface("dispatchTouchEvent");
        if (!CommonUtility.Utility.isNull(i)) {
            i.invoke(this, event);
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!CommonUtility.UIUtility.isAppOnForeground(activity)) {
            isActive = false;
        }
    }

    /**
     * 当app重新回到前端时调用
     */
    public void onAppActive() {
    }

    /**
     * 设置应用字体不受系统字体变化而变化
     */
    void setDefaultFontSize() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        displayMetrics.scaledDensity = displayMetrics.density;
    }
}

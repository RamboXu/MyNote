package com.e.common.application;

import android.app.Activity;
import android.app.Application;

import com.e.common.i.ILoginFilter;
import com.e.common.manager.init.InitManager_;
import com.e.common.manager.net.INet;
import com.e.common.utility.CommonUtility;

import java.util.concurrent.ConcurrentHashMap;

import de.greenrobot.dao.AbstractDao;

/**
 * @author <a href="mailto:zjc198805@qq.com">mark</a>
 * @version 1.0
 * @class BaseApplication.java Create on 2015-04-07 上午1:32
 * @description
 */
public abstract class BaseApplication extends Application implements INet, ILoginFilter {

    private ConcurrentHashMap<String, Activity> mActivityLists = new ConcurrentHashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();

        /**
         * 初始化图片配置
         */
        InitManager_.getInstance_(this).initImageLoaderConfig(this);

        /**
         * 只允许本应用走一次onCreate
         */
        String processName = CommonUtility.SystemOperateUtility.getProcessName(this, android.os.Process.myPid());
        if (!CommonUtility.Utility.isNull(processName) && processName.equals(getPackageName())) {
            initOnce();
        }
    }

    /**
     * 应用程序初始化一次
     */
    public abstract void initOnce();


    /**
     * method desc：模拟activity栈
     *
     * @param activity
     */
    public void putActivity(Activity activity) {
        mActivityLists.put(activity.getClass().getSimpleName(), activity);
    }

    public Activity getActivityByKey(String key) {
        return mActivityLists.get(key);
    }

    public void forwardMainPage(Activity activity) {

    }

    /**
     * method desc：结束指定类型的activity
     *
     * @param clazz
     */
    public void finishActivity(Class<?> clazz) {
        finishActivity(clazz.getSimpleName());
    }

    public void finishActivity(String clazzName){
        Activity activity = mActivityLists.get(clazzName);
        if (!CommonUtility.Utility.isNull(activity)) {
            activity.finish();
        }
        mActivityLists.remove(clazzName);
    }

    public void finishAll() {
        for (Activity activity : mActivityLists.values()) {
            finishActivity(activity.getClass());
        }
    }

    /**
     * 移除非指定的所有activity
     *
     * @param withoutActivity
     */
    public void finishAllWithout(Class withoutActivity) {
        for (Activity activity : mActivityLists.values()) {
            if (!activity.getClass().getSimpleName().equals(withoutActivity.getSimpleName())) {
                finishActivity(activity.getClass());
            }
        }
    }

    public AbstractDao getCacheDao() {
        return null;
    }

}

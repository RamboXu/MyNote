package com.e.common.utility.reflect;

import org.androidannotations.annotations.EBean;

import java.util.HashMap;

/**
 * Created by Evan on 16/4/2.
 */
@EBean(scope = EBean.Scope.Singleton)
public class ReflectHelper {

    private HashMap<String, ReflectInterface> mReflectHashMap = new HashMap<>();

    public void addReflectClass(String key, ReflectInterface value) {
        mReflectHashMap.put(key, value);
    }

    public ReflectInterface getActivityOnResumeInterface() {
        return mReflectHashMap.get("onResume");
    }

    public void setActivityOnResumeInterface(ReflectInterface i) {
        addReflectClass("onResume", i);
    }

    public ReflectInterface getActivityOnPauseInterface() {
        return mReflectHashMap.get("onPause");
    }

    public void setActivityOnPauseInterface(ReflectInterface i) {
        addReflectClass("onPause", i);
    }

    public void setReflectInterface(String key, ReflectInterface value) {
        mReflectHashMap.put(key, value);
    }

    public ReflectInterface getReflectInterface(String key) {
        return mReflectHashMap.get(key);
    }
}

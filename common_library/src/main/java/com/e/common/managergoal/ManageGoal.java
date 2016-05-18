package com.e.common.managergoal;

import android.content.Context;

import com.e.common.utility.CommonUtility;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.json.JSONObject;

/**
 * Created by e on 15/3/6.
 * 管理目标类
 */
@EBean(scope = EBean.Scope.Singleton)
public class ManageGoal {

    @RootContext
    protected Context mContext;
    private final String KEY_MANAGERGOAL = "ManageGoal";


    /**
     * 获取管理目标
     *
     * @return
     */
    public String getManagerGoal() {
        String content = CommonUtility.SharedPreferencesUtility.getSharedPreferences(
                mContext).getString(KEY_MANAGERGOAL, "");
        return content;
    }

    /**
     * 保存管理目标
     *
     * @param jsonObject
     */
    public void saveManagerGoal(Object jsonObject) {
        if (!CommonUtility.Utility.isNull(jsonObject)) {
            if (jsonObject instanceof JSONObject || jsonObject instanceof String) {
                CommonUtility.SharedPreferencesUtility.put(mContext,
                        KEY_MANAGERGOAL, jsonObject.toString());
            } else {
                CommonUtility.SharedPreferencesUtility.put(mContext,
                        KEY_MANAGERGOAL, CommonUtility.JSONObjectUtility.GSON.toJson(jsonObject));
            }
        }
    }

}

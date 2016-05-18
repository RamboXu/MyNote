package com.e.common.secret;

import android.content.Context;

import com.e.common.utility.CommonUtility;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;
import org.androidannotations.annotations.RootContext;
import org.json.JSONObject;


/**
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @{# userSecret.java Create on 2015年1月21日 下午9:15:24
 * @description
 */
@EBean(scope = Scope.Singleton)
public class UserSecret {

    @RootContext
    protected Context mContext;

    public static final String KEY_USER = "user";

    public String getUserSecret() {
        return CommonUtility.SharedPreferencesUtility.getSharedPreferences(mContext).getString(KEY_USER, "");
    }

    public void saveUserSecret(Object object) {

        if (object instanceof JSONObject || object instanceof String) {
            CommonUtility.SharedPreferencesUtility.put(mContext,
                    KEY_USER, object.toString());
        } else {
            CommonUtility.SharedPreferencesUtility.put(mContext,
                    KEY_USER, CommonUtility.JSONObjectUtility.GSON.toJson(object));
        }
    }
}

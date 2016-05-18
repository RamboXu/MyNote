package com.e.common.cookie;

import android.content.Context;
import android.content.SharedPreferences;

import com.e.common.utility.CommonUtility;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;

/**
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @class CookieStoreManager.java Create on 2015-03-18 下午10:26
 * @description
 */
@EBean(scope = EBean.Scope.Singleton)
public class CookieStoreManager {

    public final String PREF_DOMAIN = "domain",
            PREF_SESSION_ID = "value",
            PREF_COOKIE_NAME = "name",
            PREF_COOKIE_PATH = "path",
            PREF_URL_SCHEME = "url_scheme",
            PREF_ACCESS_ID = "access_id",
            PREF_SECRET_KEY = "secret_key";
    private final String COOKIE_PREF = "com.byb.patient_preferences";
    @RootContext
    Context mContext;
    SharedPreferences mSharedPreferences;

    @AfterInject
    void initData() {
        mSharedPreferences = mContext.getSharedPreferences(COOKIE_PREF, 0);
    }

    /**
     * 获取当前cookie store
     */
    public CookieManager getCurrentCookieStore() {
        CookieManager manager = new CookieManager();
        manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        String domain = mSharedPreferences.getString(PREF_DOMAIN, null);
        String sessionId = mSharedPreferences.getString(PREF_SESSION_ID, null);
        String cookieName = mSharedPreferences.getString(PREF_COOKIE_NAME, null);
        String path = mSharedPreferences.getString(PREF_COOKIE_PATH, null);
        String scheme = mSharedPreferences.getString(PREF_URL_SCHEME, "http://");

        CommonUtility.DebugLog.log(domain);
        CommonUtility.DebugLog.log(sessionId);
        CommonUtility.DebugLog.log(cookieName);
        CommonUtility.DebugLog.log(path);
        if(!CommonUtility.Utility.isNull(domain) && !CommonUtility.Utility.isNull(sessionId) &&
                !CommonUtility.Utility.isNull(cookieName) && !CommonUtility.Utility.isNull(path)) {
            URI uri = URI.create(scheme + domain);
            HttpCookie cookie = new HttpCookie(cookieName, sessionId);
            cookie.setPath(path);
            cookie.setDomain(domain);
            manager.getCookieStore().add(uri, cookie);
        }
        return manager;
    }

    public String getCookieName() {
        String cookieName = mSharedPreferences.getString(PREF_COOKIE_NAME, "");
        return cookieName;
    }

    public String getCookieValue() {
        String sessionId = mSharedPreferences.getString(PREF_SESSION_ID, "");
        return sessionId;
    }

    public void setCookieValue(String cookieValue) {
        mSharedPreferences.edit().putString(PREF_SESSION_ID, cookieValue).commit();
    }

    public void initNewApiCookie(String domain) {
        mSharedPreferences.edit().putString(PREF_DOMAIN, domain.substring(0, domain.indexOf("/"))).
                putString(PREF_COOKIE_PATH, "/").
                putString(PREF_COOKIE_NAME, "sessionid").
                putString(PREF_URL_SCHEME, "http://").commit();
    }

    /**
     * 将新的cookie store持久化到本地
     *
     * @param cookieStore
     */
    public void setCookieStore(CookieStore cookieStore) {
        if (!CommonUtility.Utility.isNull(cookieStore)) {
            List<URI> uris = cookieStore.getURIs();
            if (uris.size() > 0) {
                URI uri = uris.get(0);

                HttpCookie cookie = cookieStore.get(uri).get(0);
                String domain = cookie.getDomain();
                String sessionId = cookie.getValue();
                String cookieName = cookie.getName();
                String path = cookie.getPath();
                mSharedPreferences.edit().putString(PREF_COOKIE_NAME, cookieName).
                        putString(PREF_DOMAIN, domain).
                        putString(PREF_SESSION_ID, sessionId).
                        putString(PREF_COOKIE_PATH, path).
                        putString(PREF_URL_SCHEME, uri.getScheme()).commit();
            }
        }
    }

    public boolean isInitCookieStore() {
        if (!CommonUtility.Utility.isNull(mSharedPreferences.getString(PREF_DOMAIN, null))
                && !CommonUtility.Utility.isNull(mSharedPreferences.getString(PREF_SESSION_ID, null))
                && !CommonUtility.Utility.isNull(mSharedPreferences.getString(PREF_COOKIE_PATH, null))
                && !CommonUtility.Utility.isNull(mSharedPreferences.getString(PREF_COOKIE_NAME, null))) {
            return true;
        }
        return false;
    }

    public void clearAll() {
        mSharedPreferences.edit().putString(PREF_DOMAIN, null).
                putString(PREF_SESSION_ID, null).
                putString(PREF_COOKIE_NAME, null).
                putString(PREF_COOKIE_PATH, null).
                putString(PREF_ACCESS_ID, null).
                putString(PREF_SECRET_KEY, null).commit();
    }

    public void setAccessIdSecretKey(String accessId, String secretKey) {
        mSharedPreferences.edit().putString(PREF_ACCESS_ID, accessId).
                putString(PREF_SECRET_KEY, secretKey).commit();
    }

    public String getAccessId() {
        return mSharedPreferences.getString(PREF_ACCESS_ID, "");
    }

    public String getSecretKey() {
        return mSharedPreferences.getString(PREF_SECRET_KEY, "");
    }
}

package com.e.common.manager.net;

import com.e.common.manager.net.config.Params;

import java.util.HashMap;

/**
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @class NetUtility.java Create on 2015-12-30 下午1:23
 * @description
 */
public class NetUtility {

    public static Params getPostMap() {
        Params params = new Params().post();
        return params;
    }

    public static Params getGetMap() {
        Params params = new Params().get();
        return params;
    }

    public static Params getPutMap() {
        Params params = new Params().put();
        return params;
    }

    public static Params getDeleteMap() {
        Params params = new Params().delete();
        return params;
    }

    public static Params getJSONPostMap() {
        Params params = getPostMap().isNewApi(true);
        return params;
    }

    public static Params getJSONPutMap() {
        Params params = getPutMap().isNewApi(true);
        return params;
    }

    public static Params getJSONGetMap() {
        Params params = getNoCacheGetMap().isNewApi(true);
        return params;
    }

    public static Params getJSONCacheGetMap(Object object) {
        Params params = getNoCacheGetMap()
                .isNewApi(true)
                .isCache(true)
                .cacheKey(object.getClass().getSimpleName());
        return params;
    }

    public static Params getJSONCacheGetMapNoErrorTip(Object object) {
        Params params = getJSONCacheGetMap(object).isShowError(false);
        return params;
    }

    public static Params getJSONGetMapNoErrorTip() {
        Params params = getJSONGetMap().isShowError(false);
        return params;
    }

    public static void removeCacheFromMap(HashMap<String, Object> params) {
        if (params instanceof Params) {
            ((Params) params).cacheKey("");
        }
    }

    public static void addCacheFromMap(HashMap<String, Object> params, Object object) {
        if (params instanceof Params) {
            ((Params) params).cacheKey(object.getClass().getSimpleName());
        }
    }

    public static Params getJSONDeleteMap() {
        Params params = getDeleteMap().isNewApi(true);
        return params;
    }

    public static Params getJSONGetMapWithoutEncrypt(Object object) {
        Params params = getJSONCacheGetMap(object).isEncrypt(false);
        return params;
    }

    public static Params getJSONGetMapWithoutEncryptAndCache() {
        Params params = getJSONGetMap().isEncrypt(false);
        return params;
    }

    public static Params getJSONGetMapNoErrorTipWithoutEncrypt(Object object) {
        Params params = getJSONGetMapWithoutEncrypt(object).isShowError(false);
        return params;
    }

    public static Params getNoCacheGetMap() {
        return getGetMap();
    }

    public static void addReportUrl(HashMap<String, Object> params, String url) {
        if (params instanceof Params) {
            ((Params) params).reportUrl(url);
        }
    }
}
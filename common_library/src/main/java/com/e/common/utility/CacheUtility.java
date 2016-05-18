package com.e.common.utility;

import com.e.common.db.entity.Caches;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import de.greenrobot.dao.AbstractDao;

/**
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @class CacheUtility.java Create on 2015-08-24 下午2:46
 * @description
 */
public class CacheUtility {

    /**
     * 根据标识获取缓存内容
     * @param clazz
     * @param params
     * @param dao
     * @return
     */
    public static JSONObject getCache(Class clazz, HashMap<String, Object> params, AbstractDao dao) {
        String mCacheKey = CommonUtility.formatString(clazz.getSimpleName(), CommonUtility.JSONObjectUtility.GSON.toJson(params));

        List cacheList = dao.queryRaw("where type = ?", mCacheKey);
        if (!CommonUtility.Utility.isNull(cacheList) && !cacheList.isEmpty()) {
            Caches dataCache = (Caches) cacheList.get(0);
            try {
                return new JSONObject(dataCache.getContent());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return new JSONObject();
    }

    /**
     * 根据标识缓存内容
     * @param clazz
     * @param params
     * @param dao
     * @param cacheData
     */
    public static void saveCache(Class clazz, HashMap<String, Object> params, AbstractDao dao, Object cacheData) {
        String mCacheKey = CommonUtility.formatString(clazz.getSimpleName(), CommonUtility.JSONObjectUtility.GSON.toJson(params));
        List cacheList = dao.queryRaw("where type = ?", mCacheKey);
        if(!cacheList.isEmpty()) {
            dao.deleteInTx(cacheList);
        }
        Caches caches = new Caches();
        caches.setType(mCacheKey);
        caches.setContent(cacheData.toString());
        dao.insertOrReplace(caches);
    }

}

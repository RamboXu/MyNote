package com.e.common.manager.net.config;

import com.e.common.utility.CommonUtility;

import java.util.HashMap;

/**
 * Created by Evan on 16/3/16.
 */
public class Params extends HashMap<String, Object> {

//    public static final int REQUEST_TYPE_GET = 0, REQUEST_TYPE_POST = 1,
//            REQUEST_TYPE_PUT = 2, REQUEST_TYPE_DELETE = 3;

    public enum Method {
        GET(0, "GET"),
        POST(1, "POST"),
        PUT(2, "PUT"),
        DELETE(3, "DELETE");

        private int method;
        private String methodName;

        Method(int method, String methodName) {
            this.method = method;
            this.methodName = methodName;
        }

        public int method() {
            return method;
        }

        public String getMethodName() {
            return methodName;
        }

    }

    private Method method;
    private String url;
    private boolean isEncrypt;
    private boolean isCache;
    private String reportUrl;
    private boolean isShowError = true; //请求失败是否需要显示error
    private long afterRefresh = 10 * 1000; //同一个接口请求间隔
    private boolean isNewApi;
    private String cacheKey;
    private String encryptParam; //加密需要的额外参数
    private boolean isMultipart; //是否为文件上传
    private boolean isOtherRequestUrl; //是否是请求第三方的接口，此接口需要额外特殊处理

    //以下两个要配合使用，当isJustJson为true时，才会用json
    private boolean isJustJson;
    private String json;

    public Params get() {
        method = Method.GET;
        return this;
    }

    public Params post() {
        method = Method.POST;
        return this;
    }

    public Params put() {
        method = Method.PUT;
        return this;
    }

    public Params delete() {
        method = Method.DELETE;
        return this;
    }

    public Params url(String url) {
        this.url = url;
        if (CommonUtility.Utility.isNull(reportUrl)) {
            this.reportUrl(reportUrl);
        }
        return this;
    }

    public Params isEncrypt(boolean isEncrypt) {
        this.isEncrypt = isEncrypt;
        return this;
    }

    public Params isCache(boolean isCache) {
        this.isCache = isCache;
        return this;
    }

    public Params reportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
        return this;
    }

    public Params isShowError(boolean isShowError) {
        this.isShowError = isShowError;
        return this;
    }

    public Params afterRefresh(long timestamp) {
        this.afterRefresh = timestamp;
        return this;
    }

    public Params encryptParam(String encryptParam) {
        this.encryptParam = encryptParam;
        return this;
    }

    public Params isMultipart(boolean isMultipart) {
        this.isMultipart = isMultipart;
        return this;
    }

    public Params isNewApi(boolean isNewApi) {
        this.isNewApi = isNewApi;
        return this;
    }

    public Params params(HashMap<String, Object> params) {
        this.putAll(params);
        return this;
    }

    public Params params(String key, Object object) {
        this.put(key, object);
        return this;
    }

    public Params cacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
        return this;
    }

    public Params isJustJson(boolean isJustJson) {
        this.isJustJson = isJustJson;
        return this;
    }

    public Params json(String json) {
        this.json = json;
        return this;
    }

    public Params isOtherRequestUrl(boolean isOtherRequestUrl) {
        this.isOtherRequestUrl = isOtherRequestUrl;
        return this;
    }

    public boolean isOtherRequestUrl() {
        return isOtherRequestUrl;
    }

    public Method getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public boolean isEncrypt() {
        return isEncrypt;
    }

    public boolean isCache() {
        return isCache;
    }

    public String getReportUrl() {
        return reportUrl;
    }

    public boolean isShowError() {
        return isShowError;
    }

    public long getAfterRefresh() {
        return afterRefresh;
    }

    public boolean isNewApi() {
        return isNewApi;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public boolean isJustJson() {
        return isJustJson;
    }

    public String json() {
        return json;
    }

    //是否超过缓存时期重新请求接口
    public boolean isNeedRefresh(Long lastRequestTime) {
        if (CommonUtility.Utility.isNull(lastRequestTime)) {
            lastRequestTime = 0l;
        }
        if (System.currentTimeMillis() - lastRequestTime > afterRefresh) {
            return true;
        }
        return false;
    }
}

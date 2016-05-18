package com.e.common.event;

import org.json.JSONObject;

/**
 * @{# EventType.java Create on 2015年1月20日 下午5:30:22
 *
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @description 处理网络请求后返回的实体
 */
public final class EventTypeRequest extends EventType {

    public static final int RESULT_CODE_OK = 200, RESULT_CODE_EXCEPTION_SERVER = 400,
            RESULT_CODE_EXCEPTION_DEVICE = 600, RESULT_CODE_EXCEPTION_NET_NOT_GOOD = 800;

    private String requestUrl;

    private Object target; // 特定的接收类

    private JSONObject data;
    private int resultCode = RESULT_CODE_OK;
    private boolean isCache;

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    private boolean isOtherRedirectUrl; //此次请求是否请求第三方

    public boolean isNeedShowErrorTip() {
        return isNeedShowErrorTip;
    }

    public void setIsNeedShowErrorTip(boolean isNeedShowErrorTip) {
        this.isNeedShowErrorTip = isNeedShowErrorTip;
    }

    private boolean isNeedShowErrorTip;
    /**
     * @return the target
     */
    public Object getTarget() {
        return target;
    }

    /**
     * @param target
     *            the target to set
     */
    public void setTarget(Object target) {
        this.target = target;
    }
    /**
     * @return the data
     */
    public JSONObject getData() {
        return data;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(JSONObject data) {
        this.data = data;
    }

    /**
     * @return the resultCode
     */
    public int getResultCode() {
        return resultCode;
    }

    /**
     * @param resultCode
     *            the resultCode to set
     */
    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public boolean isCache() {
        return isCache;
    }

    public void setIsCache(boolean isCache) {
        this.isCache = isCache;
    }

    public boolean isOtherRedirectUrl() {
        return isOtherRedirectUrl;
    }

    public void setOtherRedirectUrl(boolean otherRedirectUrl) {
        isOtherRedirectUrl = otherRedirectUrl;
    }
}

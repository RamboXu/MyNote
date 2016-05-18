package com.e.common.task.net;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.e.common.application.BaseApplication;
import com.e.common.db.entity.Caches;
import com.e.common.event.EventTypeRequest;
import com.e.common.handler.RequestHandler;
import com.e.common.manager.net.NetManager;
import com.e.common.manager.net.config.Params;
import com.e.common.utility.CommonUtility;
import com.e.common.widget.LoadingView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;
import org.androidannotations.annotations.RootContext;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.event.EventBus;

/**
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @{# RequestInterceptor.java Create on 2015年1月20日 下午4:00:00
 * @description
 */
@EBean(scope = Scope.Singleton)
public class RequestInterceptor {

    public static final int NO_TAG = 0x0001;
    public static final boolean MULTIPART = true;
    public static final boolean SHOW_DIALOG = true;
    public static final String CACHE_KEY = "isCache";
    @Bean
    NetManager mNetManager;

    @RootContext
    Context mContext;

    BaseApplication mApplication;

    @AfterInject
    void initData() {
        mApplication = (BaseApplication) mContext.getApplicationContext();
    }

    /**
     * method desc：发出一个默认显示loading框的请求，并且无标识，适用于当前界面只有一个请求
     *
     * @param url
     * @param params
     */
    public void request(Context context, String url,
                        HashMap<String, Object> params, Object target) {
        request(context, url, params, target, NO_TAG, SHOW_DIALOG, !MULTIPART);
    }

    /**
     * method desc：发出一个带有tag，并且标记是否显示loading框
     *
     * @param url
     * @param params
     * @param isShowDialog
     */
    public void request(Context context, String url,
                        HashMap<String, Object> params, Object target, boolean isShowDialog) {
        request(context, url, params, target, NO_TAG, isShowDialog, !MULTIPART);
    }

    /**
     * method desc：发出一个带有tag的请求，主要用于一个界面有多个请求时区分
     *
     * @param url
     * @param params
     * @param tag
     */
    public void request(Context context, String url,
                        HashMap<String, Object> params, Object target, int tag) {
        request(context, url, params, target, tag, SHOW_DIALOG, !MULTIPART);
    }

    /**
     * method desc：发出一个带有tag，并且标记是否显示loading框，主要用于一个界面有多个请求时区分
     *
     * @param url
     * @param params
     * @param tag
     * @param isShowDialog
     */
    public void request(Context context, String url,
                        HashMap<String, Object> params, Object target, int tag,
                        boolean isShowDialog) {
        request(context, url, params, target, tag, isShowDialog, !MULTIPART);
    }

    /**
     * method desc：发出一个带有tag，并且标记是否显示loading框，主要用于一个界面有多个请求时区分
     *
     * @param url
     * @param params
     * @param tag
     * @param isShowDialog
     * @param isMultipart
     */
    public void request(Context context, String url,
                        HashMap<String, Object> params, Object target, int tag,
                        boolean isShowDialog, boolean isMultipart) {
        if (isShowDialog) {
            LoadingView.show(context);
        }
        requestInvoke(url, params, target, tag, isMultipart);
    }

    /**
     * method desc：发出一个带有tag，并且标记是否显示loading框，主要用于一个界面有多个请求时区分
     *
     * @param object       //附带的对象
     * @param url
     * @param params
     * @param tag
     * @param isShowDialog
     * @param isMultipart
     */
    public void request(Context context, Object object, String url,
                        HashMap<String, Object> params, Object target, int tag,
                        boolean isShowDialog, boolean isMultipart) {
        if (isShowDialog) {
            LoadingView.show(context);
        }
        requestInvoke(object, url, params, target, tag, isMultipart, null);
    }

    /**
     * method desc：后台请求
     *
     * @param url
     * @param params
     * @param tag
     */
    @Background
    void requestInvoke(String url,
                       HashMap<String, Object> params, Object target, int tag, boolean isMultipart) {
        requestInvoke(null, url, params, target, tag, isMultipart, null);
    }

    /**
     * method desc：后台请求
     *
     * @param url
     * @param params
     */
    public void requestByHandler(String url,
                                 HashMap<String, Object> params, boolean isMultipart, Handler handler) {
        requestByHandler(url, params, isMultipart, false, handler);
    }

    /**
     * method desc：后台请求
     *
     * @param url
     * @param params
     */
    public void requestByHandler(String url,
                                 HashMap<String, Object> params, Handler handler) {
        requestByHandler(url, params, false, handler);
    }

    /**
     * method desc：后台请求
     *
     * @param url
     * @param params
     */
    public void requestByHandler(String url,
                                 HashMap<String, Object> params, boolean isMultipart, boolean isShowDialog, Handler handler) {
        if (isShowDialog && handler instanceof RequestHandler) {
            LoadingView.show(((RequestHandler) handler).getContext());
        }
        requestInvoke(null, url, params, null, -1, isMultipart, handler);
    }

    /**
     * method desc：后台请求
     *
     * @param url
     * @param par
     * @param tag
     */
    @Background
    void requestInvoke(Object object, String url,
                       HashMap<String, Object> par, Object target, int tag, boolean isMultipart, Handler handler) {

        Params params = null;
        if (par instanceof Params) {
            params = (Params) par;
        } else {
            try {
                throw new Exception("参数类型必须为com.e.common.manager.net.config.Params");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        String data = null;
        Object cacheKey = null;
        boolean isOtherRedirectUrl = false;

        //如果当前数据是从缓存获取，那么出错后不提示
        boolean isNeedShowErrorTip = params.isShowError();
        /**
         * 先从缓存获取
         */
        Caches caches = null;
        if (!CommonUtility.Utility.isNull(params)) {
            cacheKey = params.getCacheKey();
            boolean isCache = params.isCache();
            isOtherRedirectUrl = params.isOtherRequestUrl();

            if (isCache) {
                cacheKey = CommonUtility.formatString(cacheKey.toString(), url, CommonUtility.JSONObjectUtility.map2JSONObject(params));
                AbstractDao dao = mApplication.getCacheDao();
                if (!CommonUtility.Utility.isNull(dao)) {
                    List cacheList = dao.queryRaw("where type = ?", cacheKey.toString());
                    if (!CommonUtility.Utility.isNull(cacheList) && !cacheList.isEmpty()) {
                        caches = (Caches) cacheList.get(0);
                        data = caches.getContent();
                        if (!CommonUtility.Utility.isNull(data)) {
                            isNeedShowErrorTip = false;
                            sendEventTypeRequest(tag, object, target, data, handler, cacheKey, true, isNeedShowErrorTip, isOtherRedirectUrl, url);
                        }
                    }
                    data = null;
                }
            }
        }

        //当请求接口距离上次请求时间超出设定时间时则重新请求接口
        if (CommonUtility.Utility.isNull(caches) || params.isNeedRefresh(caches.getLastRefreshTimestamp())) {
            data = requestInvokeDependOnCurrentThread(url, params, isMultipart);

            sendEventTypeRequest(tag, object, target, data, handler, cacheKey, false, isNeedShowErrorTip, isOtherRedirectUrl, url);
        }
    }

    /**
     * 请求接口，依赖于当前调用的线程
     */
    public String requestInvokeDependOnCurrentThread(String url,
                                                     HashMap<String, Object> par, boolean isMultipart) {
        Params params = null;
        if (par instanceof Params) {
            params = (Params) par;
        } else {
            try {
                throw new Exception("参数类型必须为com.e.common.manager.net.config.Params");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        //兼容之前版本url
        params.url(url);

        String data = null;

        String tempUrl = params.getReportUrl();
        String reqOriginUrl = CommonUtility.Utility.isNull(tempUrl) ? url : tempUrl;
        String reqMethod = "ERROR";
        try {
            reqMethod = params.getMethod().getMethodName();
        } catch (Exception e) {

        }

        long reqTime = System.currentTimeMillis();

        if (isMultipart) {
            data = mNetManager.sendRequestFromHttpClientByOkHttpMultipart(params);
        } else {
            if (params.isNewApi()) {
                data = mNetManager.sendRequestFromHttpClientByOkHttpJSON(params);
            } else {
                data = mNetManager.sendRequestFromHttpClientByOkHttpForm(params);
            }
        }

        if (!CommonUtility.Utility.isNull(data)) {
            try {
                if (!data.equals(NetManager.NET_EXCEPTION_BY_SERVER)
                        && !data.equals(NetManager.NET_EXCEPTION_BY_DEVICE)
                        && !data.equals(NetManager.NET_EXCEPTION_NOT_GOOD)) {
                    JSONObject result = new JSONObject(data);
                    //从返回的数据中将数据和respCode解析出来
                    data = result.optString(NetManager.KEY_RESPONSE_DATA);

                    //默认返回值为-1001，如果最后依然是-1001,则不上报
                    String respCode = "0";

                    if (!CommonUtility.Utility.isNull(data)) {

                        //获取接口请求的respCode
                        respCode = result.optString(NetManager.KEY_RESPONSE_CODE);
                    }
                    /**
                     * 这里做api上报
                     * 完全请求网络并且response
                     */
                    if (!CommonUtility.Utility.isNull(reqOriginUrl) && !reqOriginUrl.startsWith("http")) {
                        /**
                         * 将/加再接口最前面
                         */
                        reqOriginUrl = reqOriginUrl.replace("/weitang/", "");
                        if (!reqOriginUrl.startsWith("/")) {
                            reqOriginUrl = CommonUtility.formatString("/", reqOriginUrl);
                        }
                        JSONObject resultData = new JSONObject(data);
//                        mApplication.reportApi(reqOriginUrl, reqMethod, CommonUtility.formatString(reqTime), respCode, mApplication.getRespCode(resultData), CommonUtility.formatString(System.currentTimeMillis() - reqTime));
                    }
                } else {
                    /**
                     * 这里做api上报
                     * 完全请求网络并且response
                     */
                    if (!CommonUtility.Utility.isNull(reqOriginUrl) && !reqOriginUrl.startsWith("http")) {
                        /**
                         * 将/加再接口最前面
                         */
                        reqOriginUrl = reqOriginUrl.replace("/weitang/", "");
                        if (!reqOriginUrl.startsWith("/")) {
                            reqOriginUrl = CommonUtility.formatString("/", reqOriginUrl);
                        }
                        String respCode = "0", errorCode = "-1002";
                        if (data.equals(NetManager.NET_EXCEPTION_BY_DEVICE)) {
                            errorCode = "-1004";
                        } else if (data.equals(NetManager.NET_EXCEPTION_BY_SERVER)) {
                            errorCode = "-1006";
                        } else if (data.equals(NetManager.NET_EXCEPTION_NOT_GOOD)) {
                            errorCode = "-1008";
                        }
//                        mApplication.reportApi(reqOriginUrl, reqMethod, CommonUtility.formatString(reqTime), respCode, errorCode, CommonUtility.formatString(System.currentTimeMillis() - reqTime));
                    }
                }
            } catch (Exception e) {

            }
        }

        CommonUtility.DebugLog.log(CommonUtility.formatString("request_result: ", url, "---->", data));

        return data;
    }

    /**
     * @param tag
     * @param object
     * @param target
     * @param data
     * @param handler
     * @param cacheKey
     * @param isCacheTag         callback到请求处，告诉其当前数据是否是缓存数据
     * @param isNeedShowErrorTip 如果从缓存中获取到数据，则网络出错后不提示
     * @param isOtherRedirectUrl 是否是请求第三方接口
     * @param requestUrl         请求的接口
     */
    private void sendEventTypeRequest(int tag, Object object, Object target, String data, Handler handler, Object cacheKey, boolean isCacheTag, boolean isNeedShowErrorTip, boolean isOtherRedirectUrl, String requestUrl) {
        if (!CommonUtility.Utility.isNull(data)) {
            EventTypeRequest eventType = new EventTypeRequest();
            eventType.setTag(tag);
            eventType.setObject(object);
            eventType.setTarget(target);
            eventType.setIsCache(isCacheTag);
            eventType.setOtherRedirectUrl(isOtherRedirectUrl);
            eventType.setIsNeedShowErrorTip(isNeedShowErrorTip);
            eventType.setRequestUrl(requestUrl);

            if (data.equals(NetManager.NET_EXCEPTION_BY_SERVER)) {
                eventType.setResultCode(EventTypeRequest.RESULT_CODE_EXCEPTION_SERVER);
            } else if (data.equals(NetManager.NET_EXCEPTION_BY_DEVICE)) {
                eventType.setResultCode(EventTypeRequest.RESULT_CODE_EXCEPTION_DEVICE);
            } else if (data.equals(NetManager.NET_EXCEPTION_NOT_GOOD)) {
                eventType.setResultCode(EventTypeRequest.RESULT_CODE_EXCEPTION_NET_NOT_GOOD);
            } else {
                try {
                    JSONObject responseData = new JSONObject(data);
                    responseData.put(CACHE_KEY, isCacheTag);
                    eventType.setData(responseData);

                    //保存至缓存
                    boolean isCache = !CommonUtility.Utility.isNull(cacheKey);

                    if (isCache && !isCacheTag) {
                        AbstractDao dao = mApplication.getCacheDao();
                        List cacheList = dao.queryRaw("where type = ?", cacheKey.toString());
                        if (!cacheList.isEmpty()) {
                            dao.deleteInTx(cacheList);
                        }
                        Caches caches = new Caches();
                        caches.setType(cacheKey.toString());
                        caches.setContent(data);
                        caches.setLastRefreshTimestamp(System.currentTimeMillis());
                        dao.insertOrReplace(caches);
                    }
                } catch (Exception e) {
                }
            }
            if (!CommonUtility.Utility.isNull(handler)) {
                Message message = Message.obtain();
                message.obj = eventType;
                handler.sendMessage(message);
            } else {
                EventBus.getDefault().post(eventType);
            }
        }
    }
}

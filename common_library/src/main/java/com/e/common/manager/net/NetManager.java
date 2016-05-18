package com.e.common.manager.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.e.common.R;
import com.e.common.manager.net.config.NetBuilder;
import com.e.common.manager.net.config.Params;
import com.e.common.manager.net.intercept.RequestIntercept;
import com.e.common.utility.CommonUtility;
import com.squareup.okhttp.CacheControl;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okio.Buffer;
import okio.BufferedSink;
import okio.ByteString;

/**
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @{# NetManager.java Create on 2014年11月20日 下午2:32:50
 * @description 网络处理管理器
 */
@EBean(scope = EBean.Scope.Singleton)
public class NetManager {

    public static final String NET_EXCEPTION_BY_DEVICE = "NET_EXCEPTION_BY_DEVICE"; //网络未设置
    public static final String NET_EXCEPTION_NOT_GOOD = "NET_EXCEPTION_NOT_GOOD"; //网络不给力
    public static final String NET_EXCEPTION_BY_SERVER = "NET_EXCEPTION_BY_SERVER"; //服务器相应出错

    public static final String KEY_RESPONSE_DATA = "KEY_RESPONSE_DATA";
    public static final String KEY_RESPONSE_CODE = "KEY_RESPONSE_CODE";

    public final String CHARSET_PREFIX = "\ufeff";

    OkHttpClient mOKClient;
    @RootContext
    Context mContext;

    INet mINet;
    private HashMap<String, String> mHeader;

    private RequestIntercept mRequestIntercept; //请求拦截器

    private NetBuilder mNetBuilder;

    private String mFormMultipartBoundary = UUID.randomUUID().toString();
    //    private String mMultipartContentType = new StringBuilder("multipart/form-data; boundary=").append(ByteString.encodeUtf8(mFormMultipartBoundary).utf8()).toString();
    public static String mJSONContentType = "application/json; charset=UTF-8";

    /**
     * 初始化网络模块参数
     */
    public void setNetBuilder(NetBuilder builder) {
        mHeader = builder.getHeader();
        mOKClient.setCookieHandler(builder.getCookieManager());
        mOKClient.setConnectTimeout(builder.getConnectTimeout(), TimeUnit.SECONDS);
        mOKClient.setReadTimeout(builder.getReadTimeout(), TimeUnit.SECONDS);
        mOKClient.setWriteTimeout(builder.getWriteTimeout(), TimeUnit.SECONDS);
        mRequestIntercept = builder.getIntercept();
    }

    public NetBuilder getNetBuilder() {
        if (CommonUtility.Utility.isNull(mNetBuilder)) {
            mNetBuilder = NetBuilder.build();
        }
        return mNetBuilder;
    }


    @AfterInject
    void initData() {

        mINet = ((INet) mContext.getApplicationContext());
        mOKClient = new OkHttpClient();
    }

    /**
     * 检查网络是否可用
     *
     * @return
     */
    public boolean checkNetwork() {
        try {
            ConnectivityManager manager = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkinfo = manager.getActiveNetworkInfo();
            if (networkinfo == null || !networkinfo.isAvailable()) {
                // 当前网络不可用
                return false;
            } else {
                // 当前网络可用
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 检查网络是否可用，不可用时会有提示
     *
     * @return
     */
    public boolean checkNetworkAndShowWhenUnAvailable() {
        boolean isAvailable = checkNetwork();
        if (!isAvailable) {
            CommonUtility.UIUtility.toast(mContext, R.string.net_tip_no_net);
        }
        return isAvailable;
    }

    /**
     * 发出文件上传表单提交
     *
     * @param params
     * @return
     */
    public String sendRequestFromHttpClientByOkHttpMultipart(Params params) {

        StringBuilder pars = new StringBuilder();
        Params.Method method = params.getMethod();

        boolean isPostOrPut = method == Params.Method.POST
                || method == Params.Method.PUT;
        MultipartBuilder multipartBuilder = null;
        if (isPostOrPut) {
            multipartBuilder = new MultipartBuilder(mFormMultipartBoundary).type(MultipartBuilder.FORM);
            if (params.isEmpty()) { // 一定要保证Multipart
                // body有至少一组键值，不然报错
                // java.lang.IllegalStateException:
                // Multipart body must have at
                // least one
                // part.
                multipartBuilder.addFormDataPart("a", "b");
            }
        }

        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String entryObjKey = entry.getKey();
                Object entryObjValue = entry.getValue();
                if (isPostOrPut) {
                    if (entryObjValue instanceof ArrayList) {
                        ArrayList<Object> list = (ArrayList<Object>) entryObjValue;
                        for (Object object : list) {
                            if (object instanceof File) {
                                multipartBuilder.addFormDataPart(entryObjKey,
                                        ((File) object).getName(), RequestBody
                                                .create(MultipartBuilder.FORM,
                                                        (File) object));
                            } else {
                                multipartBuilder.addFormDataPart(entryObjKey,
                                        String.valueOf(object));
                            }
                        }
                    } else {
                        if (entryObjValue instanceof File) {
                            multipartBuilder.addFormDataPart(entryObjKey,
                                    ((File) entryObjValue).getName(),
                                    RequestBody.create(MultipartBuilder.FORM,
                                            (File) entryObjValue));
                        } else {
                            multipartBuilder.addFormDataPart(entryObjKey, String.valueOf(entryObjValue));
                        }
                    }
                }
                pars.append(entryObjKey).append("=").append(entryObjValue)
                        .append("&");
            }
        }
        RequestBody requestBody = null;
        try {
            if (!CommonUtility.Utility.isNull(multipartBuilder)) {
                requestBody = multipartBuilder
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return execute(isPostOrPut, pars, requestBody, params);
    }


    /**
     * 发出普通表单提交
     *
     * @param params
     * @return
     */
    public String sendRequestFromHttpClientByOkHttpForm(Params params) {

        StringBuilder pars = new StringBuilder();
        Params.Method method = params.getMethod();

        boolean isPostOrPut = method == Params.Method.POST
                || method == Params.Method.PUT;
        FormEncodingBuilder formEncodingBuilder = null;
        if (isPostOrPut) {
            formEncodingBuilder = new FormEncodingBuilder();
            if (params.isEmpty()) { // 一定要保证form
                // body有至少一组键值，不然报错
                // Form encoded body must have at least one part.
                formEncodingBuilder.add("a", "b");
            }
        }

        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String entryObjKey = entry.getKey();
                Object entryObjValue = entry.getValue();
                if (isPostOrPut) {
                    if (entryObjValue instanceof ArrayList) {
                        ArrayList<Object> list = (ArrayList<Object>) entryObjValue;
                        for (Object object : list) {
                            if (object instanceof String) {
                                formEncodingBuilder.add(entryObjKey,
                                        String.valueOf(object));
                            } else {
                                throw new IllegalArgumentException("form 表单提交请注意对象为String");
                            }
                        }
                    } else {
                        formEncodingBuilder.add(entryObjKey,
                                String.valueOf(entryObjValue));
                    }
                }
                pars.append(entryObjKey).append("=").append(entryObjValue)
                        .append("&");
            }
        }
        RequestBody requestBody = null;
        try {
            if (!CommonUtility.Utility.isNull(formEncodingBuilder)) {
                requestBody = requestBodyWithContentLength(formEncodingBuilder
                        .build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return execute(isPostOrPut, pars, requestBody, params);
    }

    /**
     * json类型接口请求
     *
     * @param params
     * @return
     */
    public /*synchronized*/ String sendRequestFromHttpClientByOkHttpJSON(Params params) {
        /**
         * 先加密
         */
        HashMap<String, String> encryptHeader = mRequestIntercept.encrypt(params);

        Params.Method method = params.getMethod();

        mRequestIntercept.progress(params);

        StringBuilder pars = new StringBuilder();
        String json = "";
        if (params != null) {
            if (method == Params.Method.GET) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    String entryObjKey = entry.getKey();
                    Object entryObjValue = entry.getValue();
                    try {
                        if (!params.isJustJson()) {
                            pars.append(entryObjKey).append("=").append(entryObjValue.toString())
                                    .append("&");
                        } else {
                            pars.append(params.json());
                        }
                    } catch (Exception e) {
                    }
                }
            } else {
                if (params.isJustJson()) {
                    json = params.json();
                } else {
                    json = CommonUtility.JSONObjectUtility.GSON.toJson(params);
                }
            }
        }
        RequestBody body = RequestBody.create(MediaType.parse(mJSONContentType), json);
        Builder builder = new Builder().url(params.getUrl());
        String url = params.getUrl();
        if (method == Params.Method.POST) {
            builder.post(body);
        } else if (method == Params.Method.PUT) {
            builder.put(body);
        } else if (method == Params.Method.GET) {
            url = CommonUtility.formatString(url, "?", pars);
            builder.get();
        } else if (method == Params.Method.DELETE) {
            builder.delete(body);
        }
        builder.url(url);
        StringBuilder urlBuilder = new StringBuilder().append(params.getUrl());
        if (!urlBuilder.toString().contains("?")) {
            urlBuilder.append("?").append(pars);
        }
        CommonUtility.DebugLog.log(urlBuilder);
        CommonUtility.DebugLog.log(CommonUtility.formatString("json:", json));

        for (Map.Entry<String, String> entry : mHeader.entrySet()) {
            builder.addHeader(entry.getKey(), entry.getValue());
        }

        if (!CommonUtility.Utility.isNull(encryptHeader)) {
            for (Map.Entry<String, String> entry : encryptHeader.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        return executeResponse(params.isEncrypt(), builder);
    }

    /**
     * 处理接口请求返回
     *
     * @param isWithoutEncrypt
     * @param builder
     * @return
     */
    String executeResponse(boolean isWithoutEncrypt, Builder builder) {
        try {
            if (!isWithoutEncrypt && !checkNetwork()) {
                return NET_EXCEPTION_BY_DEVICE;
            }

            if (checkNetwork()) {
                builder.cacheControl(CacheControl.FORCE_NETWORK);
                Request request = builder.build();
                Response response = mOKClient.newCall(request).execute();
                String res = removeFirstPrefix(response.body().string());
                response.body().close();
                if (res.contains("<html")) {
                    CommonUtility.DebugLog.log(CommonUtility.formatString("服务器异常返回内容为：", res));
                    return NET_EXCEPTION_BY_SERVER;
                }
                JSONObject result = new JSONObject();
                result.put(KEY_RESPONSE_DATA, res);
                result.put(KEY_RESPONSE_CODE, response.code());
                return result.toString();
            } else {
                return NET_EXCEPTION_BY_DEVICE;
            }
        } catch (ConnectTimeoutException connectionTimeoutEx) {
            return NET_EXCEPTION_NOT_GOOD;
        } catch (SocketTimeoutException socketTimeoutEx) {
            return NET_EXCEPTION_NOT_GOOD;
        } catch (Exception e) {
            e.printStackTrace();
            if (checkNetwork()) {
                return NET_EXCEPTION_BY_SERVER;
            } else {
                return NET_EXCEPTION_BY_DEVICE;
            }
        }
    }

    /**
     * * 处理接口请求返回
     *
     * @param isPostOrPut
     * @param pars
     * @param requestBody
     * @param params
     * @return
     */
    private String execute(boolean isPostOrPut, StringBuilder pars,
                           RequestBody requestBody, Params params) {

        /**
         * 加密
         */
        HashMap<String, String> encryptHeader = mRequestIntercept.encrypt(params);

        mRequestIntercept.progress(params);

        mHeader = mINet.getHeaderInfo();
        Builder builder = new Builder();
        builder.url(params.getUrl());

        if (!isPostOrPut) {
            if (params.getMethod() == Params.Method.GET) {
                builder.get();
            } else {
                builder.delete();
            }
        } else {// post || put

            if (params.getMethod() == Params.Method.POST) {
                builder.post(requestBody);
            } else {
                builder.put(requestBody);
            }
        }

        StringBuilder url = new StringBuilder();

        if (!url.toString().contains("?")) {
            url.append("?").append(pars);
        }
        CommonUtility.DebugLog.log(url);

        // 添加请求头部信息
        if (!CommonUtility.Utility.isNull(mHeader)) {
            for (Map.Entry<String, String> entry : mHeader.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        if (!CommonUtility.Utility.isNull(encryptHeader)) {
            for (Map.Entry<String, String> entry : encryptHeader.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        try {
            if (checkNetwork()) {
                builder.cacheControl(CacheControl.FORCE_NETWORK);
                Request request = builder.build();
                Response response = mOKClient.newCall(request).execute();
                String res = removeFirstPrefix(response.body().string());
                response.body().close();
                if (res.contains("<html")) {
                    CommonUtility.DebugLog.log(CommonUtility.formatString("服务器异常返回内容为：", res));
                    return NET_EXCEPTION_BY_SERVER;
                }
                JSONObject result = new JSONObject();
                result.put(KEY_RESPONSE_DATA, res);
                result.put(KEY_RESPONSE_CODE, response.code());
                return result.toString();
            } else {
                return NET_EXCEPTION_BY_DEVICE;
            }
        } catch (ConnectTimeoutException connectionTimeoutEx) {
            connectionTimeoutEx.printStackTrace();
            return NET_EXCEPTION_NOT_GOOD;
        } catch (SocketTimeoutException socketTimeoutEx) {
            socketTimeoutEx.printStackTrace();
            return NET_EXCEPTION_NOT_GOOD;
        } catch (Exception e) {
            e.printStackTrace();
            if (checkNetwork()) {
                return NET_EXCEPTION_BY_SERVER;
            } else {
                return NET_EXCEPTION_BY_DEVICE;
            }
        }
    }

    /**
     * 处理请求体，主要兼容nginx老版本问题
     *
     * @param requestBody
     * @return
     * @throws IOException
     */
    private RequestBody requestBodyWithContentLength(final RequestBody requestBody) throws IOException {
        final Buffer buffer = new Buffer();
        try {
            requestBody.writeTo(buffer);
        } catch (IOException e) {
            throw new IOException("Unable to copy RequestBody");
        }
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return requestBody.contentType();
            }

            @Override
            public long contentLength() {
                return buffer.size();
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                sink.write(ByteString.read(buffer.inputStream(), (int) buffer.size()));
            }
        };
    }

    /**
     * method desc： 过滤掉服务器返回数据的特殊字符，这会影响到数据解析
     *
     * @param json
     * @return
     */
    private String removeFirstPrefix(String json) {
        if (!CommonUtility.Utility.isNull(json)) {
            if (json.startsWith(CHARSET_PREFIX)) {
                json = json.substring(1);
            }
        }
        return json;
    }
}

package com.e.common.manager.net.config;

import com.e.common.manager.net.intercept.RequestIntercept;

import java.net.CookieManager;
import java.util.HashMap;

/**
 * Created by Evan on 16/3/16.
 */
public class NetBuilder {

    // 网络请求超时时间
    public static final int CONNECTION_TIMEOUT = 20, READ_TIMEOUT = CONNECTION_TIMEOUT + 20, WRITE_TIMEOUT = READ_TIMEOUT * 3;

    private HashMap<String, String> header = new HashMap<>();
    private long connectTimeout; //网络连接时间
    private long readTimeout; //拉去接口数据超时时间
    private long writeTimeout; //往服务器写数据超时时间
    private CookieManager cookieManager;
    private RequestIntercept intercept;

    /**
     * newInstance
     *
     * @return
     */
    public static NetBuilder build() {
        NetBuilder builder = new NetBuilder();
        builder.connectTimeout(CONNECTION_TIMEOUT);
        builder.readTimeout(READ_TIMEOUT);
        builder.writeTimeout(WRITE_TIMEOUT);
        return builder;
    }

    /**
     * add a list header info
     *
     * @param header
     * @return
     */
    public NetBuilder header(HashMap<String, String> header) {
        this.header.putAll(header);
        return this;
    }

    /**
     * add a header info
     *
     * @param key
     * @param value
     * @return
     */
    public NetBuilder header(String key, String value) {
        header.put(key, value);
        return this;
    }

    /**
     * set connect timeout
     *
     * @param connectTimeout
     * @return
     */
    public NetBuilder connectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    /**
     * set read data timeout
     *
     * @param readTimeout
     * @return
     */
    public NetBuilder readTimeout(long readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    /**
     * set write data timeout
     *
     * @param writeTimeout
     * @return
     */
    public NetBuilder writeTimeout(long writeTimeout) {
        this.writeTimeout = writeTimeout;
        return this;
    }

    public long getConnectTimeout() {
        return connectTimeout;
    }

    public long getReadTimeout() {
        return readTimeout;
    }

    public long getWriteTimeout() {
        return writeTimeout;
    }

    public CookieManager getCookieManager() {
        return cookieManager;
    }

    public NetBuilder cookieManager(CookieManager cookieManager) {
        this.cookieManager = cookieManager;
        return this;
    }

    /**
     * 网络请求前的拦截，处理请求前的业务逻辑
     *
     * @param intercept
     * @return
     */
    public NetBuilder requestIntercept(RequestIntercept intercept) {
        this.intercept = intercept;
        return this;
    }

    public RequestIntercept getIntercept() {
        return intercept;
    }

    public HashMap<String, String> getHeader() {
        return header;
    }
}

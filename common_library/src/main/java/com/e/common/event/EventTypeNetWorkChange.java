package com.e.common.event;

/**
 * Created by mark on 15/3/26.
 * 网路发生变化的广播
 * 如果isConnect为false，则网络类型为-1
 */
public class EventTypeNetWorkChange {
    public boolean isConnect;//是否联网

    //Type有以下几种类型
    //http://developer.android.com/reference/android/net/NetworkInfo.html#getType()
    // TYPE_MOBILE, TYPE_WIFI, TYPE_WIMAX, TYPE_ETHERNET, TYPE_BLUETOOTH
    public int netType;//网络连接类型

    public EventTypeNetWorkChange(boolean isConnect, int netType){
        this.isConnect = isConnect;
        this.netType = netType;
    }

}

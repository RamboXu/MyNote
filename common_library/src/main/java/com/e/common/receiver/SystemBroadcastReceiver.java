package com.e.common.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.e.common.constant.Constants;
import com.e.common.event.EventTypeNetWorkChange;
import com.e.common.utility.CommonUtility;

import de.greenrobot.event.EventBus;

/**
 * Created by mark on 15/3/26.
 */
public class SystemBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        //网络发生改变监听
        if (TextUtils.equals(action, ConnectivityManager.CONNECTIVITY_ACTION)) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo netWorkInfo = connectivityManager.getActiveNetworkInfo();
                if(netWorkInfo != null){
                    if (NetworkInfo.State.CONNECTED == netWorkInfo.getState()) {
                        //TODO 这边触发了两次
                        if (netWorkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                            if (!Constants.isWifi) {
                                CommonUtility.DebugLog.e("mark", "SystemBroadcastReceiver........................");
                                EventBus.getDefault().post(new EventTypeNetWorkChange(true, netWorkInfo.getType()));
                                Constants.isWifi = true;
                            }
                            return;
                        } else {
                            Constants.isWifi = false;
                        }
                    }
                }
            }
            //没有执行return,则说明当前无网络连接
//            EventBus.getDefault().post(new EventTypeNetWorkChange(false, -1));
        } else if (TextUtils.equals(action, Intent.ACTION_BOOT_COMPLETED)) {//开机广播监听

        } else if (TextUtils.equals(action, Intent.ACTION_SHUTDOWN)) {//关机广播

        }


    }
}

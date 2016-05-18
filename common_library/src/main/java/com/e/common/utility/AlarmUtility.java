package com.e.common.utility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

/**
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @class AlarmUtility.java Create on 2015-04-10 上午9:54
 * @description
 */
@EBean(scope = EBean.Scope.Singleton)
public class AlarmUtility {

    @RootContext
    Context mContext;

    AlarmManager mAlarmManager;

    @AfterInject
    void initData() {
        mAlarmManager = (AlarmManager) mContext
                .getSystemService(Context.ALARM_SERVICE);
    }

    /**
     * 设置重复提醒
     *
     * @param action
     * @param triggerAtMillis 触发时间
     * @param intervalMillis  重复频率
     * @param requestCode
     */
    public void setRepeatAlarm(String action,
                               long triggerAtMillis, long intervalMillis, int requestCode) {
        Intent intent = new Intent(action);
        PendingIntent sender = PendingIntent.getBroadcast(mContext, requestCode,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (triggerAtMillis < System.currentTimeMillis()) {
            triggerAtMillis += intervalMillis;
        }
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtMillis,
                intervalMillis, sender);
    }

    /**
     * 设置单次提醒
     *
     * @param triggerAtMillis
     * @param requestCode
     */
    public void setAlarmOnce(Intent intent,
                             long triggerAtMillis, int requestCode) {
        PendingIntent sender = PendingIntent.getBroadcast(mContext, requestCode,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) mContext
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, sender);
    }

    /**
     * 取消设置的指定的提醒
     *
     * @param action
     * @param requestCode
     */
    public void cancelAlarm(String action, int requestCode) {
        PendingIntent sender = PendingIntent.getBroadcast(mContext, requestCode, new Intent(action), PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarmManager.cancel(sender);
    }
}

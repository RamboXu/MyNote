package com.e.common.utility;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.e.common.R;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

/**
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @class NotificationManagementUtility.java Create on 2015-04-10 下午1:36
 * @description
 */
@EBean(scope = EBean.Scope.Singleton)
public class NotificationManagementUtility {

    @RootContext
    Context mContext;

    private NotificationManager mNotificationManager;

    @AfterInject
    void initData() {
        mNotificationManager = (NotificationManager) mContext
                .getSystemService(Activity.NOTIFICATION_SERVICE);
    }

    /**
     * Show a notification while this service is running.
     */
    public void showNotificationWithForward(Intent intent, String content,
                                            int notifyId) {
        showNotificationWithForward(intent, null, content, notifyId);
    }

    public void showNotificationWithForward(Intent intent, String title, String content,
                                            int notifyId) {
        NotificationCompat.Builder mBuilder = getBuilder(title, true, true);
        if (!CommonUtility.Utility.isNull(content)) {
            mBuilder.setContentText(content);
        }
        PendingIntent contentIntent = PendingIntent.getActivity(mContext,
                notifyId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);

        mNotificationManager.notify(notifyId, mBuilder.build());
    }

    public void showNotificationWithForward(Intent intent, String title, String content,
                                            int notifyId, boolean isOpenSpeaker, boolean isOpenBee) {
        NotificationCompat.Builder mBuilder = getBuilder(title, isOpenSpeaker, isOpenBee);
        if (!CommonUtility.Utility.isNull(content)) {
            mBuilder.setContentText(content);
        }
        PendingIntent contentIntent = PendingIntent.getActivity(mContext,
                notifyId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);
        //设置清除
        mBuilder.setDeleteIntent(PendingIntent.getBroadcast(mContext, R.id.notification_delete_request_code, (new Intent("com.byb.broadcast.chat.clear")), 0));
        mNotificationManager.notify(notifyId, mBuilder.build());
    }


    public NotificationCompat.Builder getBuilder(String title, boolean isOpenSpeaker, boolean isOpenBee) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                mContext);
        mBuilder.setWhen(System.currentTimeMillis())
                // 通知产生的时间，会在通知信息里显示
                .setContentTitle(CommonUtility.Utility.isNull(title) ? mContext.getText(R.string .app_name) : title)
                .setContentIntent(
                        getDefaultIntent(PendingIntent.FLAG_UPDATE_CURRENT))
                        // .setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
                        // 最低sdk需要 >= 16
                .setTicker("您有一条新消息")
                .setOngoing(false)// ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setAutoCancel(true)
                .setSmallIcon(getNotificationIcon());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBuilder.setLargeIcon(((BitmapDrawable) mContext.getResources().getDrawable(R.drawable.ic_launcher)).getBitmap());
        }
        if (isOpenSpeaker && isOpenBee) {
            mBuilder.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND);
        } else {
            if (isOpenSpeaker) {
                mBuilder.setDefaults(Notification.DEFAULT_SOUND);
            } else {
                mBuilder.setSound(null);
            }

            if (isOpenBee) {
                mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
            } else {
                mBuilder.setVibrate(null);
            }
        }

        return mBuilder;
    }

    private int getNotificationIcon() {
        boolean whiteIcon = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
        return whiteIcon ? R.drawable.ic_launcher_5 : R.drawable.ic_launcher;
    }

    /**
     * method desc： 获取默认PendingIntent
     *
     * @param flags 在顶部常驻：Notification.FLAG_ONGOING_EVENT
     *              点击去除：Notification.FLAG_AUTO_CANCEL
     * @return
     */
    public PendingIntent getDefaultIntent(int flags) {
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 1,
                new Intent(), flags);
        return pendingIntent;
    }

    /**
     * method desc： 清除指定notifyId的通知
     *
     * @param notifyId
     */
    public void cancelNotification(int notifyId) {
        mNotificationManager.cancel(notifyId);
    }

    /**
     * method desc： 清除当前应用所有通知
     */
    public void cancelAllNotification() {
        mNotificationManager.cancelAll();
    }

}

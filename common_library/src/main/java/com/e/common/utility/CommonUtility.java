/**
 * @{#} Utility.java Create on 2013-10-18 上午11:16:24
 * @author Evan
 * @email evan0502@qq.com
 * @version 1.0
 */
package com.e.common.utility;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.e.common.R;
import com.e.common.constant.Constants;
import com.e.common.secret.MD5Utility;
import com.e.common.widget.DialogExt;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @{# Utility.java Create on 2014年11月30日 上午10:47:39
 * @description
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public abstract class CommonUtility {

    /**
     * 获取app渠道id
     *
     * @param context
     * @return
     */
    public static String getChannelId(Context context) {
        ApplicationInfo info;
        try {
            info = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            String channelId = info.metaData.get("UMENG_CHANNEL") + "";
            return channelId;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取指定范围内的随机数
     *
     * @param minNum
     * @param maxNum
     * @return
     */
    public static int getRandom(int minNum, int maxNum) {
        int s = (int) (minNum + Math.random() * (maxNum - minNum + 1));
        return s;
    }

    /**
     * 将多个对象拼接成字符串
     *
     * @param object
     * @return
     */
    public static String formatString(Object... object) {
        StringBuilder builder = new StringBuilder();
        for (Object o : object) {
            if (o != null) {
                builder.append(o);
            }
        }
        return builder.toString();
    }

    /**
     * 获取系统版本
     *
     * @return
     */
    public static String getOSVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 普通操作工具类
     */
    public static final class Utility {


        /**
         * method desc：判断参数值是否为空 null，空字符串，或者全部空格字符串或者"null"字符串都视为空
         *
         * @param o
         * @return
         */
        public static boolean isNull(Object o) {
            try {
                return null == o || "".equals(o.toString().replaceAll(" ", ""))
                        || "null".equals(o.toString());
            } catch (Exception e) {
            }
            return true;
        }

        /**
         * 验证邮箱地址是否正确
         *
         * @param email
         * @return
         */
        public static boolean checkEmail(String email) {
            boolean flag = false;
            try {
                String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
                Pattern regex = Pattern.compile(check);
                Matcher matcher = regex.matcher(email);
                flag = matcher.matches();
            } catch (Exception e) {
                flag = false;
            }

            return flag;
        }

        /**
         * 验证手机号码
         *
         * @param mobiles
         * @return [0-9]{5,9}
         */
        public static boolean isMobileNO(String mobiles) {
            boolean flag = false;
            try {
                Pattern p = Pattern
                        .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
                Matcher m = p.matcher(mobiles);
                flag = m.matches();
            } catch (Exception e) {
                flag = false;
            }
            return flag;
        }

        /**
         * 判断是否是手机号码格式（只判断是否是11位纯数字）
         *
         * @param mobiles
         * @return
         */
        public static boolean isMobile(String mobiles) {

            Pattern p = Pattern.compile("^(([1-9][0-9][0-9]))\\d{8}$");

            Matcher m = p.matcher(mobiles);

            System.out.println(m.matches() + "---");

            return m.matches();

        }

        /**
         * method desc：验证是否为数字
         *
         * @param number
         * @return
         */
        public static boolean isNum(String number) {
            boolean flag = false;
            try {
                Pattern p = Pattern.compile("^[0-9]{5}$");
                Matcher m = p.matcher(number);
                flag = m.matches();
            } catch (Exception e) {
                flag = false;
            }
            return flag;
        }

        /**
         * method desc： 判断密码是否有至少一位数字和字母
         *
         * @param password
         * @return
         */
        public static boolean isAlphanumerics(String password) {
            boolean flag = false;
            try {
                Pattern p = Pattern
                        .compile(".*[A-Za-z].*[0-9]|.*[0-9].*[A-Za-z]");
                Matcher m = p.matcher(password);
                flag = m.matches();
            } catch (Exception e) {
                flag = false;
            }
            return flag;
        }

        /**
         * 对double进行处理
         *
         * @param f      需要处理的数据
         * @param length 保留的小数位数
         * @return
         */
        public static double formatDouble(double f, int length) {
            BigDecimal bigDecimal = new BigDecimal(f);
            return bigDecimal.setScale(length, BigDecimal.ROUND_HALF_UP)
                    .doubleValue();
        }

        public static double formatStr2Double(String str, int length) {
            return formatDouble(Double.parseDouble(str), length);
        }

        public static String formatStr2Num(String str, int length) {
            if (!Utility.isNull(str)) {
                return formatDouble(Double.parseDouble(str), length) + "";
            } else {
                return null;
            }
        }

        public static String formatDouble2String(double f, int length) {
            return formatDouble(f, length) + "";
        }

        public static String formatDouble2String(float f, int length) {
            return formatDouble((double) f, length) + "";
        }

        public static String formatDouble2String(double f) {
            if (f % 1.00 == 0) {
                return CommonUtility.formatString((int) f);
            }
            if (f % 0.1 == 0) {
                return formatDouble2String(f, 1);
            }
            return formatDouble2String(f, 2);
        }


        public static int getRandomNum() {
            return (int) (Math.random() * (10000 - 100) + 1);
        }


        public static String encode(String param) {
            try {
                return URLEncoder.encode(param, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        public static String decode(String param) {
            try {
                return URLEncoder.encode(param, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        public static Type getType(Class clazz, Class targetClass) {
            Class superClass = clazz.getSuperclass();
            if (superClass.getSimpleName().equals(targetClass.getSimpleName())) {
                return clazz.getGenericSuperclass();
            }
            return getType(superClass, targetClass);
        }

        public static Field[] getAllField(Class<?> clazz) {
            List<Field> fieldList = new ArrayList<>();
            Field[] dFields = clazz.getDeclaredFields();
            if (null != dFields && dFields.length > 0) {
                fieldList.addAll(Arrays.asList(dFields));
            }

            Class<?> superClass = clazz.getSuperclass();
            if (superClass != Object.class) {
                Field[] superFields = getAllField(superClass);
                if (null != superFields && superFields.length > 0) {
                    for (Field field : superFields) {
                        if (!isContain(fieldList, field)) {
                            fieldList.add(field);
                        }
                    }
                }
            }
            Field[] result = new Field[fieldList.size()];
            fieldList.toArray(result);
            return result;
        }

        /**
         * 检测Field List中是否已经包含了目标field
         *
         * @param fieldList
         * @param field     带检测field
         * @return
         */
        public static boolean isContain(List<Field> fieldList, Field field) {
            for (Field temp : fieldList) {
                if (temp.getName().equals(field.getName())) {
                    return true;
                }
            }
            return false;
        }
    }

    public static class DebugLog {

        private static final String TAG_LOG = "e";

        private static final boolean DEBUG = true;

        static String className;
        static String methodName;
        static int lineNumber;

        public static void log(Object message) {
            if (!isDebuggable())
                return;

            // Throwable instance must be created before any methods
            getMethodNames(new Throwable().getStackTrace());
            Log.e(TAG_LOG, createLog(message));
        }

        public static boolean isDebuggable() {
            return DEBUG;
        }

        private static String createLog(Object log) {
            return CommonUtility.formatString(className, ":", "[", methodName, ":", lineNumber, "]", log);
        }

        private static String createLogWithClassName(Object log) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("[");
            buffer.append(className);
            buffer.append(":");
            buffer.append(methodName);
            buffer.append(":");
            buffer.append(lineNumber);
            buffer.append("]");
            buffer.append(log);

            return buffer.toString();
        }

        private static void getMethodNames(StackTraceElement[] sElements) {
            className = sElements[1].getFileName();
            methodName = sElements[1].getMethodName();
            lineNumber = sElements[1].getLineNumber();
        }

        public static void e(Object message) {
            if (!isDebuggable())
                return;

            // Throwable instance must be created before any methods
            getMethodNames(new Throwable().getStackTrace());
            Log.e(className, createLog(message));
        }

        public static void e(String key, Object message) {
            if (Utility.isNull(message)) {
                message = "null";
            }
            if (!isDebuggable())
                return;

            // Throwable instance must be created before any methods
            getMethodNames(new Throwable().getStackTrace());
            Log.e(key, createLogWithClassName(message));
        }


        public static void i(Object message) {
            if (!isDebuggable())
                return;

            getMethodNames(new Throwable().getStackTrace());
            Log.i(className, createLog(message));
        }

        public static void d(Object message) {
            if (!isDebuggable())
                return;

            getMethodNames(new Throwable().getStackTrace());
            Log.d(className, createLog(message));
        }

        public static void v(Object message) {
            if (!isDebuggable())
                return;

            getMethodNames(new Throwable().getStackTrace());
            Log.v(className, createLog(message));
        }

        public static void w(Object message) {
            if (!isDebuggable())
                return;

            getMethodNames(new Throwable().getStackTrace());
            Log.w(className, createLog(message));
        }

        public static void wtf(Object message) {
            if (!isDebuggable())
                return;

            getMethodNames(new Throwable().getStackTrace());
            Log.wtf(className, createLog(message));
        }

    }

    /**
     * 跟用户界面相关的操作工具类
     */
    public static final class UIUtility {

        /**
         * 用于判断点击屏幕外面区域,是否隐藏键盘
         *
         * @param v
         * @param event
         * @return true 执行隐藏键盘 ,false不隐藏
         */
        public static boolean isShouldHideKeyboard(View v, MotionEvent event) {
            if (v != null && (v instanceof EditText)) {
                int[] l = {0, 0};
                v.getLocationInWindow(l);
                int left = l[0],
                        top = l[1],
                        bottom = top + v.getHeight(),
                        right = left + v.getWidth();
                if (event.getX() > left && event.getX() < right
                        && event.getY() > top && event.getY() < bottom) {
                    // 点击EditText的事件，忽略它。
                    return false;
                } else {
                    v.clearFocus();
                    return true;
                }
            }
            // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
            return false;
        }

        static View.OnClickListener mCoverViewListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClick) {
                    ViewGroup parent = (ViewGroup) v.getParent();
                    if (!Utility.isNull(parent)) {
                        View view = (View) v.getTag(); //获取添加的view
                        removeView(parent, v);
                        removeView(parent, view);
                    }
                }
            }
        };

        public static void inflate(int layout, ViewGroup group) {
            ((LayoutInflater) group.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layout, group);
        }

        public static View inflate(Context context, int layout) {
            return LayoutInflater.from(context).inflate(layout, null);
        }

        private static Ringtone mRing;
        //遮罩和临时view变量
        private static View mCoverView = null, mTargetView = null;
        //是否点击cover后dismiss
        private static boolean isClick = false;

        /**
         * method desc：将dipValue换算成px
         *
         * @param context
         * @param dipValue
         * @return
         */
        public static int dip2px(Context context, float dipValue) {
            float m = context.getResources().getDisplayMetrics().density;
            return (int) (dipValue * m + 0.5f);
        }

        public static float getDimensionPixelSize(Context context, int dimen) {
            return context.getResources().getDimensionPixelSize(dimen);
        }

        /**
         * 获取view的坐标
         *
         * @param view
         * @return
         */
        public static int[] getLocation(View view) {
            int[] location = new int[2];
            view.getLocationInWindow(location);
            return location;
        }

        public static int[] getLocationAlignParent(View view) {
            int[] location = new int[2];
            location[0] = view.getLeft();
            location[1] = view.getTop();
            return location;
        }

        /**
         * method desc：显示提示
         *
         * @param context
         * @param str
         */
        public static void toast(Context context, String str) {
            if (Utility.isNull(str)) {
                return;
            }
            try {
                Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
            }
        }

        public static void toast(Context context, int strRes) {
            toast(context, context.getString(strRes));
        }

        public static void toastLong(Context context, String str) {
            if (Utility.isNull(str)) {
                return;
            }
            try {
                Toast.makeText(context, str, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
            }
        }

        public static void toastLong(Context context, int strRes) {
            toastLong(context, context.getString(strRes));
        }

        /**
         * method desc：获取对应应用的service是否运行
         *
         * @param context
         * @param serviceClass
         * @return
         */
        public static boolean serviceIsRunning(Context context,
                                               Class serviceClass) {
            ActivityManager mActivityManager = (ActivityManager) context
                    .getSystemService(Activity.ACTIVITY_SERVICE);
            List<RunningServiceInfo> mServiceList = mActivityManager
                    .getRunningServices(100);
            for (RunningServiceInfo service : mServiceList) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
            return false;
        }

        /**
         * 限制数字为小数点#{position}位
         *
         * @param text
         * @param position
         */
        public static void numLimit(Editable text, int position) {
            int d = text.toString().indexOf(".");
            if (d < 0) return;
            if (text.length() - 1 - d > position) {
                text.delete(d + position + 1, d + position + 2);
            } else if (d == 0) {
                text.delete(d, d + 1);
            }
        }

        /**
         * 判断当前应用是否在前台活动
         *
         * @param ctx
         * @return
         */
        public static boolean isAppOnForeground(Context ctx) {
            List<ActivityManager.RunningTaskInfo> tasksInfo = ((ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(1);
            if (tasksInfo.size() > 0) {
                Log.i("mark", "top Activity = " + tasksInfo.get(0).topActivity.getPackageName());
                // 应用程序位于堆栈的顶层
                if (ctx.getPackageName().equals(tasksInfo.get(0).topActivity.getPackageName())) {
                    return true;
                }
            }
            return false;
        }

        /**
         * 判断某个activity当前是否在在栈顶
         *
         * @param mContext
         * @param cls      需要判断的界面
         * @return
         */
        public static boolean isActivityOnTop(Context mContext,
                                              Class<? extends Activity> cls) {
            return isActivityOnTop(mContext, cls.getName());
        }

        private static boolean isActivityOnTop(Context context, String clsName) {
            String name = getTopActivityFullName(context);
            if (name.equals(clsName)) {
                return true;
            }
            return false;
        }

        /**
         * 获取当前活动的activity的名字，包含保命
         *
         * @param context
         * @return
         */
        public static String getTopActivityFullName(Context context) {
            ActivityManager manager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            String name = manager.getRunningTasks(1).get(0).topActivity
                    .getClassName();
            return name;
        }

        /**
         * 获取当前活动的activity的名字，不包含保命
         *
         * @param context
         * @return
         */
        public static String getTopActivityClassName(Context context) {
            ActivityManager manager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            ComponentName componentName = manager.getRunningTasks(1).get(0).topActivity;
            String name = componentName.getClassName().substring(componentName.getClassName().lastIndexOf(".") + 1);
            return name;
        }

        /**
         * 获取版本名称
         *
         * @return 当前应用的版本名称
         */
        public static String getVersionName(Context context) {
            try {
                PackageManager manager = context.getPackageManager();
                PackageInfo info = manager.getPackageInfo(
                        context.getPackageName(), 0);
                String version = info.versionName;
                return version;
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }

        /**
         * 获取版本号
         *
         * @return 当前应用的版本号
         */
        public static int getVersionCode(Context context) {
            try {
                PackageManager manager = context.getPackageManager();
                PackageInfo info = manager.getPackageInfo(
                        context.getPackageName(), 0);
                int version = info.versionCode;
                return version;
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }

        public static void addView(Object container, View view, int anim) {
            if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
                addView(container, view, anim, true, true);
            }
        }

        public static void addView(Object container, View view, int anim, boolean isShowCover, boolean click) {
            if (Thread.currentThread() == Looper.getMainLooper().getThread() && view.getParent() == null && view != null) {
                ViewGroup rootView = null;
                if (container instanceof Activity) {
                    rootView = (ViewGroup) ((Activity) container).getWindow()
                            .getDecorView().findViewById(android.R.id.content);
                } else if (container instanceof ViewGroup) {
                    rootView = (ViewGroup) container;
                } else {
                    throw new IllegalArgumentException("container should be a container.");
                }

                if (rootView == null) {
                    return;
                }

                if (!Utility.isNull(mCoverView)) {
                    ViewGroup tempRootView = (ViewGroup) mCoverView.getParent();
                    if (!Utility.isNull(mCoverView) && !Utility.isNull(tempRootView)) {
                        tempRootView.removeView(mCoverView);
                        if (!Utility.isNull(mTargetView)) {
                            tempRootView.removeView(mTargetView);
                        }
                    }
                }

                mTargetView = view;
                isClick = click;

                if (isShowCover) {
                    if (Utility.isNull(mCoverView)) {
                        mCoverView = LayoutInflater.from(rootView.getContext()).inflate(R.layout.view_cover, null);
                        mCoverView.setOnClickListener(mCoverViewListener);
                    }
                    if (Utility.isNull(mCoverView.getParent())) {
                        rootView.addView(mCoverView);
                        mCoverView.startAnimation(AnimationUtils
                                .loadAnimation(rootView.getContext(), R.anim.abc_fade_in));
                    }
                    mCoverView.setTag(mTargetView);
                }

                rootView.addView(mTargetView);
                if (anim != 0) {
                    mTargetView.startAnimation(AnimationUtils
                            .loadAnimation(rootView.getContext(), anim));
                }
            }
        }

        public static void removeView(final Object container, final View view,
                                      int anim) {
            if (!Utility.isNull(view) && Thread.currentThread() == Looper.getMainLooper().getThread()) {
                Animation animation = AnimationUtils.loadAnimation(view.getContext(),
                        anim);
                animation.setAnimationListener(new AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        ViewGroup rootView = null;
                        if (container instanceof Activity) {
                            rootView = (ViewGroup) ((Activity) container).getWindow()
                                    .getDecorView().findViewById(android.R.id.content);
                        } else if (container instanceof ViewGroup) {
                            rootView = (ViewGroup) container;
                        } else {
                            throw new IllegalArgumentException("container should be a view group.");
                        }
                        rootView.removeView(view);
                        rootView.removeView(mCoverView);
                    }
                });
                view.startAnimation(animation);
            }
        }

        public static void removeView(Object container, View view) {
            if (!Utility.isNull(view) && Thread.currentThread() == Looper.getMainLooper().getThread()) {
                try {
                    ViewGroup rootView = null;
                    if (container instanceof Activity) {
                        rootView = (ViewGroup) ((Activity) container).getWindow()
                                .getDecorView().findViewById(android.R.id.content);
                    } else if (container instanceof ViewGroup) {
                        rootView = (ViewGroup) container;
                    } else {
                        throw new IllegalArgumentException("container should be a container.");
                    }
                    rootView.removeView(view);
                    rootView.removeView(mCoverView);
                } catch (Exception e) {
                    /**
                     *
                     * 已经判断了是否在主线程了
                     * 可能出现 Only the original thread that created a view hierarchy can touch its views.
                     */
                }
            }
        }

        /**
         * 模拟遮罩的点击
         */
        public static void coverPerfomClick() {
            if (!Utility.isNull(mCoverView)) {
                mCoverView.performClick();
            }
        }

        public static boolean isShown(View view) {
            return view == mTargetView && UIUtility.isVisible(view);
        }


        /**
         * method desc：隐藏虚拟键盘
         *
         * @param view
         */
        public static void hideKeyboard(View view) {
            if (!Utility.isNull(view)) {
                InputMethodManager imm = (InputMethodManager) view.getContext()
                        .getApplicationContext().getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                // 显示或者隐藏输入法
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }

        /**
         * method desc：隐藏虚拟键盘
         *
         * @param activity
         */
        public static void hideKeyboard(Activity activity) {
            InputMethodManager imm = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (!Utility.isNull(activity.getCurrentFocus())) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }

        /**
         * method desc：在对应的view上显示虚拟键盘
         *
         * @param view
         */
        public static void showKeyboard(final View view) {
            Timer timer = new Timer();

            timer.schedule(new TimerTask() {
                public void run() {
                    InputMethodManager inputManager = (InputMethodManager) view
                            .getContext().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(view, 0);
                }
            }, 150);

        }

        public static void backgroundAlternate(View view, int position, int... resIds) {
            int index = position % resIds.length;
            view.setBackgroundResource(resIds[index]);
        }

        /**
         * method desc：屏幕截图，只能截当前应用
         *
         * @param activity
         * @param v        为空则获取activity根目录
         * @return
         */
        public static Bitmap takeScreenShot(Activity activity, View v) {
            View view = v;
            if (Utility.isNull(v)) {
                view = activity.getWindow().getDecorView();
            }

            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache();
            Bitmap b1 = view.getDrawingCache();

            Bitmap b = Bitmap.createBitmap(b1, 0, 0,
                    view.getWidth(), view.getHeight());
            view.destroyDrawingCache();
            return b;
        }

        /**
         * Get the screen height.
         *
         * @param context
         * @return the screen height
         */
        @SuppressLint("NewApi")
        public static int getScreenHeight(Context context) {
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            return dm.heightPixels;
        }

        /**
         * Get the screen width.
         *
         * @param context
         * @return the screen width
         */
        @SuppressLint("NewApi")
        public static int getScreenWidth(Context context) {
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            return dm.widthPixels;
        }

        public static int getBarHeight(Context context) {
            Class<?> c = null;
            Object obj = null;
            Field field = null;
            int x = 0, sbar = 38;//默认为38，貌似大部分是这样的
            try {
                c = Class.forName("com.android.internal.R$dimen");
                obj = c.newInstance();
                field = c.getField("status_bar_height");
                x = Integer.parseInt(field.get(obj).toString());
                sbar = context.getResources().getDimensionPixelSize(x);

            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return sbar;
        }

        /**
         * method desc：判断当前系统语言
         *
         * @param context
         * @return
         */
        public static boolean isZh(Context context) {
            Locale locale = context.getResources().getConfiguration().locale;
            String language = locale.getLanguage();
            if (language.endsWith("zh"))
                return true;
            else
                return false;
        }

        /**
         * method desc：获取TextView 内容
         *
         * @param textView
         * @return
         */
        public static String getText(TextView textView) {
            return textView.getText().toString();
        }

        /**
         * method desc：获取EditText内容
         *
         * @param editText
         * @return
         */
        public static String getText(EditText editText) {
            return editText.getText().toString().trim();
        }

        /**
         * 将光标移到最后一个
         *
         * @param editText
         */
        public static void setEditTextSection2End(EditText editText) {
            editText.setSelection(editText.getText().length());
        }

        /**
         * 将光标移到指定位置
         *
         * @param editText
         */
        public static void setEditTextSection2End(EditText editText, int index) {
            editText.setSelection(index);
        }

        /**
         * method desc： 判断是否有sdcard
         *
         * @return
         */
        public static boolean isExistSDCard() {
            if (Environment.getExternalStorageDirectory().exists()) {
                return true;
            }
            return false;
        }

        public static void setObj2View(View view, Object object) {
            if (!Utility.isNull(view)) {
                view.setTag(R.id.tag_obj, object);
            }
        }

        public static Object getObjFromView(View view) {
            return view.getTag(R.id.tag_obj);
        }

        /**
         * 获取view的Visibility
         *
         * @param view
         * @return
         */
        public static boolean isVisible(View view) {
            return view.getVisibility() == View.VISIBLE;
        }

        private synchronized static Ringtone getRingtone(Context context) {
            if (mRing == null) {
                // http://stackoverflow.com/questions/15578812/troubles-play-sound-in-silent-mode-on-android
                Uri uriRing = RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                if (uriRing == null)
                    uriRing = RingtoneManager
                            .getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                mRing = RingtoneManager.getRingtone(context,
                        uriRing);
                if (mRing == null
                        && !uriRing.toString().equals(
                        RingtoneManager.getDefaultUri(
                                RingtoneManager.TYPE_RINGTONE).toString())) {
                    mRing = RingtoneManager.getRingtone(context, RingtoneManager
                            .getDefaultUri(RingtoneManager.TYPE_RINGTONE));
                }
                if (mRing != null) {
                    mRing.setStreamType(AudioManager.STREAM_ALARM);
                }

            }
            return mRing;
        }

        public static void playNotifRing(Context context) {
            try {
                if (getRingtone(context) != null) {
                    mRing.play();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * @param text 要计算的字符串
         * @param Size 字提大小
         * @return
         */
        public static float getTextWidth(String text, float Size) {
            TextPaint FontPaint = new TextPaint();
            FontPaint.setTextSize(Size);
            return FontPaint.measureText(text);
        }

        /**
         * 设置text中划线
         *
         * @param textViews
         */
        public static void setTextViewStrikeThruTextFlag(TextView... textViews) {
            for (TextView textView : textViews) {
                textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
            }

        }
    }

    /**
     * 动画辅助类
     */
    public static class AnimationUtility {
        public static void show(final View view, int anim) {
            Animation animation = AnimationUtils.loadAnimation(view.getContext(), anim);
            view.startAnimation(animation);
            view.setVisibility(View.VISIBLE);
            view.setAlpha(1);
        }

        public static void hide(final View view, int anim) {
            Animation animation = AnimationUtils.loadAnimation(view.getContext(), anim);
            view.startAnimation(animation);
            animation.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

        public static void playAnimation(View view) {
            AnimationDrawable animationDrawable = (AnimationDrawable) view.getBackground();
            animationDrawable.start();
        }

        public static void stopAnimation(View view) {
            AnimationDrawable animationDrawable = (AnimationDrawable) view.getBackground();
            animationDrawable.stop();
            animationDrawable.selectDrawable(0);
        }
    }

    public static class SharedPreferencesUtility {

        private static SharedPreferences mSharedPreferences = null;

        public static SharedPreferences getSharedPreferences(Context context) {
            if (Utility.isNull(mSharedPreferences)) {
                if (isPatientClient(context)) {
                    mSharedPreferences = context.getSharedPreferences(Constants.SHARE_PREF, 0);
                } else {
                    mSharedPreferences = context.getSharedPreferences(Constants.SHARE_PREF_DOCTOR, 0);
                }
            }
            return mSharedPreferences;
        }

        public static SharedPreferences getSharedPreferences(Context context, String prefName) {
            return context.getSharedPreferences(prefName, 0);
        }

        /**
         * 通过PrefName放置相关的数据
         *
         * @param context
         * @param prefName
         * @param key
         * @param value
         */
        public static void put(Context context, String prefName, String key, Object value) {
            if (value != null) {
                if (value instanceof String) {
                    getSharedPreferences(context, prefName).edit().putString(key, value.toString())
                            .commit();
                } else if (value instanceof Integer) {
                    getSharedPreferences(context, prefName).edit()
                            .putInt(key, Integer.parseInt(value.toString()))
                            .commit();
                } else if (value instanceof Long) {
                    getSharedPreferences(context, prefName).edit()
                            .putLong(key, Long.parseLong(value.toString()))
                            .commit();
                } else if (value instanceof Boolean) {
                    getSharedPreferences(context, prefName).edit().putBoolean(key, Boolean.parseBoolean(value.toString())).commit();
                }
            } else {
                getSharedPreferences(context, prefName).edit().putString(key, null).commit();
            }
        }

        /**
         * @param key
         * @param value
         */
        public static void put(Context context, String key, Object value) {

            if (value != null) {
                if (value instanceof String) {
                    getSharedPreferences(context).edit().putString(key, value.toString())
                            .commit();
                } else if (value instanceof Integer) {
                    getSharedPreferences(context).edit()
                            .putInt(key, Integer.parseInt(value.toString()))
                            .commit();
                } else if (value instanceof Long) {
                    getSharedPreferences(context).edit()
                            .putLong(key, Long.parseLong(value.toString()))
                            .commit();
                } else if (value instanceof Boolean) {
                    getSharedPreferences(context).edit().putBoolean(key, Boolean.parseBoolean(value.toString())).commit();
                } else {
                    getSharedPreferences(context).edit().putString(key, value.toString())
                            .commit();
                }
            } else {
                getSharedPreferences(context).edit().putString(key, null).commit();
            }
        }

        public static String getString(Context context, String prefName, String key, String defaultValue) {
            return getSharedPreferences(context, prefName).getString(key, defaultValue);
        }

        public static int getInt(Context context, String prefName, String key, int defaultValue) {
            return getSharedPreferences(context, prefName).getInt(key, defaultValue);
        }

        public static long getLong(Context context, String prefName, String key, long defaultValue) {
            return getSharedPreferences(context, prefName).getLong(key, defaultValue);
        }

        public static boolean getBoolean(Context context, String prefName, String key, boolean defaultValue) {
            return getSharedPreferences(context, prefName).getBoolean(key, defaultValue);
        }

        public static boolean contains(Context context, String prefName, String key) {
            return getSharedPreferences(context, prefName).contains(key);
        }

        public static void remove(Context context, String prefName, String key) {
            getSharedPreferences(context, prefName).edit().remove(key).commit();
        }

        public static String getString(Context context, String key, String defaultValue) {
            return getSharedPreferences(context).getString(key, defaultValue);
        }

        public static int getInt(Context context, String key, int defaultValue) {
            return getSharedPreferences(context).getInt(key, defaultValue);
        }

        public static long getLong(Context context, String key, long defaultValue) {
            return getSharedPreferences(context).getLong(key, defaultValue);
        }

        public static boolean getBoolean(Context context, String key, boolean defaultValue) {

            return getSharedPreferences(context).getBoolean(key, defaultValue);
        }

        public static boolean contains(Context context, String key) {
            return getSharedPreferences(context).contains(key);
        }

        public static void remove(Context context, String key) {
            getSharedPreferences(context).edit().remove(key).commit();
        }

        public static void clear(Context context) {
            getSharedPreferences(context).edit().clear().commit();
        }

        public static void clear(Context context, String prefName) {
            getSharedPreferences(context, prefName).edit().clear().commit();
        }

        public SharedPreferences getSharedPreference() {
            return mSharedPreferences;
        }
    }

    /**
     * 身份证验证
     */
    public static final class IDCARD {
        static int[] WI = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

        static String[] VALIDATENUM = {"1", "0", "X", "9", "8", "7", "6", "5",
                "4", "3", "2"};

        public static boolean cardValidate(String cardNum) {
            if (!Utility.isNull(cardNum)) {
                if (cardNum.length() != 18) {
                    return false;
                }
                int count = 0;
                for (int i = 0; i < cardNum.length() - 1; i++) {
                    count += Integer.parseInt(cardNum.substring(i, i + 1))
                            * WI[i];
                }
                int mod = count % 11;
                String validateNum = VALIDATENUM[mod];
                if (!validateNum.equals(cardNum.substring(17))) {
                    return false;
                }
            }
            return true;
        }
    }

    public static final class FileUtility {

        public static String sd_card = Environment
                .getExternalStorageDirectory().getAbsolutePath();

        private static String TEMP_IMAGE_DIR_PATH;

        private static String TEMP_VOICE_DIR_PATH;

        private static String TEMP_DOCUMENT_DIR_PATH;

        public static void setTempImageDir(String path) {
            TEMP_IMAGE_DIR_PATH = path;
        }

        public static void setTempVoiceDir(String path) {
            TEMP_VOICE_DIR_PATH = path;
        }

        public static void setTempDocument(String path) {
            TEMP_DOCUMENT_DIR_PATH = path;
        }

        public static String getFilePathByContentResolver(Context context, Uri uri) {
            if (null == uri) {
                return null;
            }
            Cursor c = context.getContentResolver().query(uri, null, null, null,
                    null);
            String filePath = null;
            if (null == c) {
                throw new IllegalArgumentException("Query on " + uri
                        + " returns null result.");
            }
            try {
                if ((c.getCount() != 1) || !c.moveToFirst()) {
                } else {
                    filePath = c.getString(c
                            .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
                }
            } finally {
                c.close();
            }
            return filePath;
        }

        /**
         * method desc：获取一个随机的完整的临时图片路径
         *
         * @return
         */
        public static String getUUIDImgPath() {
            createDir(TEMP_IMAGE_DIR_PATH);
            return CommonUtility.formatString(sd_card, TEMP_IMAGE_DIR_PATH, UUID.randomUUID().toString()
                    , ".png");// ".png.cache";
        }

        /**
         * 获取一个随机的完整的语音路径
         *
         * @return
         */
        public static String getUUIDVoicePath() {
            createDir(TEMP_VOICE_DIR_PATH);
            return CommonUtility.formatString(sd_card, TEMP_VOICE_DIR_PATH, UUID.randomUUID().toString());
        }

        public static String getVoiceDirPath() {
            createDir(TEMP_VOICE_DIR_PATH);
            return CommonUtility.formatString(sd_card, TEMP_VOICE_DIR_PATH);
        }

        public static String getVoiceTempDirPath() {
            return TEMP_VOICE_DIR_PATH;
        }

        /**
         * 获取当前时间，并生成文件名字
         * 时间格式为 YYYY-MM-DD HH-MM
         *
         * @return
         */
        public static String getDateDocumentPath() {
            createDir(TEMP_DOCUMENT_DIR_PATH);
            return CommonUtility.formatString(sd_card, TEMP_DOCUMENT_DIR_PATH, DateTime.now().toString(CalendarUtility.PATIENT_YYYY_MM_DD_HH_MM_SS_MIDDLE_LINE)
                    , ".xls");
        }

        /**
         * 获取当前时间，并生成文件名字
         *
         * @return
         */
        public static String getDocumentPath(String fileName) {
            createDir(TEMP_DOCUMENT_DIR_PATH);
            return CommonUtility.formatString(sd_card, TEMP_DOCUMENT_DIR_PATH, fileName);
        }

        /**
         * 默认会创建一个fileName的文件/data/data/your_packages/files/fileName
         *
         * @param context
         * @param fileName
         * @return
         */
        public static File getDataPath(Context context, String fileName) {
            String filePath = context.getFilesDir().getAbsolutePath();
            DebugLog.e("mark", "filePath:" + filePath);
            File file = new File(filePath, fileName);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            DebugLog.e("mark", "file:" + file.getPath());
            return file;
        }

        /**
         * method desc：创建指定的路径的文件夹
         *
         * @param path
         */
        private static void createDir(String path) {
            if (!Utility.isNull(path)) {
                File file = new File(CommonUtility.formatString(sd_card, path));
                if (!file.exists()) {
                    file.mkdirs();
                }
            }
        }

        /**
         * 获取地址的文件名
         *
         * @param path
         * @return
         */
        public static String getFileNameString(String path) {
            String p = path.substring(path.lastIndexOf("/") + 1).toLowerCase(
                    Locale.getDefault());
            StringBuilder sb = new StringBuilder(p);
            sb.append("_").append(".cache");
            return sb.toString();
        }
    }

    /**
     * 网络类型
     */
    public static final class NetTypeUtility {

        /**
         * 获取网络类型
         *
         * @param context
         * @return
         */

        public static String getNetType(Context context) {
            ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {//判断网络是否是WiFi
                return "wifi";
            } else if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {//判断是否是手机网络
                switch (networkInfo.getSubtype()) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        return "2G";
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        return "3G";
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        return "4G";
                    default:
                        return "UN_NETWORK";
                }
            }
            return null;
        }

        /**
         * 获取网络服务商
         *
         * @param context
         * @return
         */
        public static String getCarrier(Context context) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String iNumeric = telephonyManager.getSimOperator();
            if (iNumeric.length() == 0) {
                return null;
            }
            if (iNumeric.equals("46000") || iNumeric.equals("46002")) {
                return "CMCC";
            } else if (iNumeric.equals("46001")) {
                return "CUCC";
            } else if (iNumeric.equals("46003")) {
                return "CTCC";
            }
            return null;
        }

        /**
         * 判断是否是WiFi
         *
         * @param context
         * @return
         */
        public static boolean isHasWiFi(Context context) {
            ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {//判断网络是否是WiFi
                return true;
            }
            return false;
        }
    }

    /**
     * 设备信息工具类
     */
    public static final class DeviceInfoUtility {
        /**
         * method desc：获取设备网卡地址
         *
         * @param context
         * @return
         */
        public static String getMac(Context context) {
            WifiManager wifi = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            return info.getMacAddress();
        }

        /**
         * 获取手机IMEI
         *
         * @param context
         * @return
         */
        public static String getIMEI(Context context) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = telephonyManager.getDeviceId();
            return imei;
        }


        /**
         * 获取Pseudo-Unique ID
         *
         * @return
         */
        public static String getPUID() {
            return formatString("35", Build.BOARD.length() % 10, Build.BRAND.length() % 10,
                    Build.CPU_ABI.length() % 10,
                    Build.DEVICE.length() % 10,
                    Build.DISPLAY.length() % 10,
                    Build.HOST.length() % 10,
                    Build.ID.length() % 10,
                    Build.MANUFACTURER.length() % 10,
                    Build.MODEL.length() % 10,
                    Build.PRODUCT.length() % 10,
                    Build.TAGS.length() % 10,
                    Build.TYPE.length() % 10,
                    Build.USER.length() % 10);
        }

        /**
         * 获取Android id
         *
         * @param context
         * @return
         */
        public static String getAndroidId(Context context) {
            return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }

        /**
         * 获取蓝牙ID
         *
         * @return
         */
        public static String getBTMac() {
            BluetoothAdapter m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();// Local Bluetooth adapter
            if (m_BluetoothAdapter != null) {
                return m_BluetoothAdapter.getAddress();
            }
            return "";
        }

        /**
         * 获取唯一ID
         *
         * @param context
         * @return
         */
        public static String getOnlyID(Context context) {
            String UUID = SharedPreferencesUtility.getString(context, "UUID", null);
            if (UUID != null) {
                return UUID;
            }
            String ONLYID = SharedPreferencesUtility.getString(context, "ONLYID", null);
            if (ONLYID == null) {
                ONLYID = formatString(getIMEI(context), getPUID(), getAndroidId(context), getMac(context), getBTMac());
                SharedPreferencesUtility.put(context, "ONLYID", ONLYID);
            }
            return MD5Utility.MD5(ONLYID);
        }

        /**
         * 获取手机型号
         *
         * @return
         */
        public static String getDeviceModel() {
            return Build.MODEL;
        }

        /**
         * 获取手机唯一UUID
         *
         * @param context
         * @return
         */
        public static String getUUID(Context context) {
            String UUID = SharedPreferencesUtility.getString(context, "UUID", null);
            if (UUID == null) {
                UUID = java.util.UUID.randomUUID().toString();
                SharedPreferencesUtility.put(context, "UUID", UUID);
            }
            return UUID;
        }

        public static String getDeviceId(Context context) {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getDeviceId();
        }

        /**
         * 判断计步器是否能使用4.4的计步器API
         *
         * @param ctx
         * @return
         */
        public static final boolean isCanUseStepSensor(Context ctx) {
//            int sdkInt = Build.VERSION.SDK_INT;
//            PackageManager packageManager = ctx.getPackageManager();
//            boolean isStepCounter = packageManager.hasSystemFeature("android.hardware.sensor.stepcounter");
//            boolean isStepDetector = packageManager.hasSystemFeature("android.hardware.sensor.stepdetector");
//            DebugLog.e("step", "isStepCounter:" + isStepCounter + ";isStepDetector:" + isStepDetector);
//            if (sdkInt >= 19 && isStepCounter && isStepDetector) {
//                return true;
//            }
            return false;
        }

        /**
         * 判断是否挂在sdcard
         *
         * @return
         */
        public static final boolean isMountSdcard() {
            return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        }
    }

    /**
     * 设备震动工具类
     */
    public static final class DeviceControllerUtility {
        public static void vibrate(Context context, long milliseconds) {
            Vibrator vib = (Vibrator) context
                    .getSystemService(Service.VIBRATOR_SERVICE);
            vib.vibrate(milliseconds);
        }

        public static void vibrate(Context context, long[] pattern,
                                   boolean isRepeat) {
            Vibrator vib = (Vibrator) context
                    .getSystemService(Service.VIBRATOR_SERVICE);
            vib.vibrate(pattern, isRepeat ? 1 : -1);
        }
    }

    /**
     * 系统操作工具类
     */
    public static final class SystemOperateUtility {

        /**
         * method desc: 发送短信
         *
         * @param smsBody
         */
        public static void sendSMS(Context context, String tel, String smsBody) {
            Uri smsToUri = Uri.parse("smsto:" + tel);
            Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
            intent.putExtra("sms_body", smsBody);
            context.startActivity(intent);
        }

        /**
         * 得到剪切板中的内容
         *
         * @param context
         * @return
         */
        public static String getClipboard(Context context) {
            ClipboardManager clipboard = (ClipboardManager) context
                    .getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard.hasText()) {
                return clipboard.getText().toString();
            }
            return null;
        }

        /**
         * method desc：将内容复制到剪贴板上
         *
         * @param context
         * @param str
         * @param tip
         */
        @SuppressWarnings("deprecation")
        public static void copy2Clipboard(Context context, String str,
                                          String tip) {
            ClipboardManager clipboard = (ClipboardManager) context
                    .getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(str);
            UIUtility
                    .toast(context, Utility.isNull(tip) ? "内容已复制到剪贴板中。" : tip);
        }

        /**
         * method desc：将内容复制到剪贴板上
         *
         * @param context
         * @param str
         */
        public static void copy2Clipboard(Context context, String str) {
            copy2Clipboard(context, str, null);
        }

        public static String getProcessName(Context context, int pid) {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
            if (runningApps == null) {
                return null;
            }
            for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
                if (procInfo.pid == pid) {
                    return procInfo.processName;
                }
            }
            return null;
        }
    }

    public static final class BitmapOperateUtility {
        /**
         * 将bitmap放入缓存中 method desc：
         *
         * @param bitmap
         * @param bitmaps
         */
        public static void addBitmap(Bitmap bitmap, ArrayList<Bitmap> bitmaps) {
            if (!Utility.isNull(bitmaps)) {
                bitmaps.add(bitmap);
            } else {
                bitmaps = new ArrayList<>();
                bitmaps.add(bitmap);
            }
        }

        /**
         * 销毁指定集合的bitmap，释放内存 method desc：
         *
         * @param bitmaps
         */
        public static void destroyBitmaps(ArrayList<Bitmap> bitmaps) {
            if (!Utility.isNull(bitmaps)) {
                for (Bitmap bitmap : bitmaps) {
                    if (!Utility.isNull(bitmap) && !bitmap.isRecycled()) {
                        bitmap.recycle();
                    }
                    bitmap = null;
                }
                bitmaps.clear();
                bitmaps = null;
            }
        }

        /**
         * 销毁bitmap，释放内存 method desc：
         *
         * @param bitmap
         */
        public static void destroyBitmap(Bitmap bitmap) {
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
            bitmap = null;
        }

        public static Bitmap drawable2Bitmap(Drawable drawable) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        }

        /**
         * method desc：将图片按照指定角度旋转
         *
         * @param bitmap
         * @param degree
         * @return
         */
        public static Bitmap rotate(Bitmap bitmap, int degree) {
            if (bitmap.getHeight() < bitmap.getWidth()) {
                Matrix matrix = new Matrix();
                matrix.postRotate(degree);

                Bitmap b = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                        bitmap.getHeight(), matrix, true);
                destroyBitmap(bitmap);
                return b;
            } else {
                return bitmap;
            }
        }

        /**
         * 将彩色图转换为黑白图
         *
         * @param bmp
         * @return 返回转换好的位图
         */
        public static Bitmap convertToBlackWhite(Bitmap bmp) {
            int width = bmp.getWidth(); // 获取位图的宽
            int height = bmp.getHeight(); // 获取位图的高

            int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组

            bmp.getPixels(pixels, 0, width, 0, 0, width, height);
            int alpha = 0xFF << 24;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int grey = pixels[width * i + j];

                    int red = ((grey & 0x00FF0000) >> 16);
                    int green = ((grey & 0x0000FF00) >> 8);
                    int blue = (grey & 0x000000FF);

                    //当原像素为透明时不处理
                    if (red != 0 || green != 0 || blue != 0) {
                        grey = (int) (red * 0.3 + green * 0.59 + blue * 0.11);
                        grey = alpha | (grey << 16) | (grey << 8) | grey;
                        pixels[width * i + j] = grey;
                    }
                }
            }
            Bitmap newBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            newBmp.setPixels(pixels, 0, width, 0, 0, width, height);
            return newBmp;
        }

        /**
         * 转换图片成圆形
         *
         * @param bitmap 传入Bitmap对象
         * @return
         */
        public static Bitmap toRoundBitmap(Bitmap bitmap) {
            if (!Utility.isNull(bitmap)) {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                float roundPx;
                float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
                if (width <= height) {
                    roundPx = width / 2;

                    left = 0;
                    top = 0;
                    right = width;
                    bottom = width;

                    height = width;

                    dst_left = 0;
                    dst_top = 0;
                    dst_right = width;
                    dst_bottom = width;
                } else {
                    roundPx = height / 2;

                    float clip = (width - height) / 2;

                    left = clip;
                    right = width - clip;
                    top = 0;
                    bottom = height;
                    width = height;

                    dst_left = 0;
                    dst_top = 0;
                    dst_right = height;
                    dst_bottom = height;
                }

                Bitmap output = Bitmap
                        .createBitmap(width, height, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(output);

                final Paint paint = new Paint();
                final Rect src = new Rect((int) left, (int) top, (int) right,
                        (int) bottom);
                final Rect dst = new Rect((int) dst_left, (int) dst_top,
                        (int) dst_right, (int) dst_bottom);
                final RectF rectF = new RectF(dst);

                paint.setAntiAlias(true);// 设置画笔无锯齿

                canvas.drawARGB(0, 0, 0, 0); // 填充整个Canvas

                // 以下有两种方法画圆,drawRounRect和drawCircle
                canvas.drawRoundRect(rectF, roundPx, roundPx, paint);// 画圆角矩形，第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径。
                // canvas.drawCircle(roundPx, roundPx, roundPx, paint);

                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));// 设置两张图片相交时的模式,参考http://trylovecatch.iteye.com/blog/1189452
                canvas.drawBitmap(bitmap, src, dst, paint); // 以Mode.SRC_IN模式合并bitmap和已经draw了的Circle

                return output;
            }
            return bitmap;
        }
    }

    /**
     * 图片操作工具类
     */
    public static final class ImageUtility {

        private static final String TAG = "library_image_utility";

        /**
         * Stores an image on the storage
         *
         * @param bitmap      the image to store.
         * @param pictureFile the file in which it must be stored
         */
        public static File storeImage(File pictureFile, Bitmap bitmap) {
            if (pictureFile == null) {
                Log.d(TAG,
                        "Error creating media file, check storage permissions: ");
                return null;
            }
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(pictureFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
                pictureFile = null;
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
                pictureFile = null;
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                pictureFile = null;
            } finally {
                try {
                    fos.close();
                    fos = null;
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (!Utility.isNull(bitmap) && !bitmap.isRecycled()) {
                    bitmap.recycle();
                    bitmap = null;
                }
            }
            return pictureFile;
        }

        /**
         * method desc：
         *
         * @param path   absolute file path
         * @param bitmap
         */
        public static File storeImage(String path, Bitmap bitmap) {
            // String name = MyHash.mixHashStr(AdName);
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            storeImage(file, bitmap);
            return file;
        }

        public static byte[] bmpToByteArray(Bitmap bitmap) {
            ByteArrayOutputStream baos = null;
            try {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                return baos.toByteArray();
            } catch (Exception e) {
                // TODO: handle exception
            } finally {
                bitmap.recycle();
            }
            return null;
        }


        /**
         * 高斯模糊
         *
         * @param sentBitmap
         * @param radius     值越小图片会越亮，越大则越暗
         * @return
         */
        public static Bitmap blurBitmap(Bitmap sentBitmap, int radius) {

            Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

            if (radius < 1) {
                return (null);
            }

            int w = bitmap.getWidth();
            int h = bitmap.getHeight();

            int[] pix = new int[w * h];
            bitmap.getPixels(pix, 0, w, 0, 0, w, h);

            int wm = w - 1;
            int hm = h - 1;
            int wh = w * h;
            int div = radius + radius + 1;

            int r[] = new int[wh];
            int g[] = new int[wh];
            int b[] = new int[wh];
            int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
            int vmin[] = new int[Math.max(w, h)];

            int divsum = (div + 1) >> 1;
            divsum *= divsum;
            int dv[] = new int[256 * divsum];
            for (i = 0; i < 256 * divsum; i++) {
                dv[i] = (i / divsum);
            }

            yw = yi = 0;

            int[][] stack = new int[div][3];
            int stackpointer;
            int stackstart;
            int[] sir;
            int rbs;
            int r1 = radius + 1;
            int routsum, goutsum, boutsum;
            int rinsum, ginsum, binsum;

            for (y = 0; y < h; y++) {
                rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
                for (i = -radius; i <= radius; i++) {
                    p = pix[yi + Math.min(wm, Math.max(i, 0))];
                    sir = stack[i + radius];
                    sir[0] = (p & 0xff0000) >> 16;
                    sir[1] = (p & 0x00ff00) >> 8;
                    sir[2] = (p & 0x0000ff);
                    rbs = r1 - Math.abs(i);
                    rsum += sir[0] * rbs;
                    gsum += sir[1] * rbs;
                    bsum += sir[2] * rbs;
                    if (i > 0) {
                        rinsum += sir[0];
                        ginsum += sir[1];
                        binsum += sir[2];
                    } else {
                        routsum += sir[0];
                        goutsum += sir[1];
                        boutsum += sir[2];
                    }
                }
                stackpointer = radius;

                for (x = 0; x < w; x++) {

                    r[yi] = dv[rsum];
                    g[yi] = dv[gsum];
                    b[yi] = dv[bsum];

                    rsum -= routsum;
                    gsum -= goutsum;
                    bsum -= boutsum;

                    stackstart = stackpointer - radius + div;
                    sir = stack[stackstart % div];

                    routsum -= sir[0];
                    goutsum -= sir[1];
                    boutsum -= sir[2];

                    if (y == 0) {
                        vmin[x] = Math.min(x + radius + 1, wm);
                    }
                    p = pix[yw + vmin[x]];

                    sir[0] = (p & 0xff0000) >> 16;
                    sir[1] = (p & 0x00ff00) >> 8;
                    sir[2] = (p & 0x0000ff);

                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];

                    rsum += rinsum;
                    gsum += ginsum;
                    bsum += binsum;

                    stackpointer = (stackpointer + 1) % div;
                    sir = stack[(stackpointer) % div];

                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];

                    rinsum -= sir[0];
                    ginsum -= sir[1];
                    binsum -= sir[2];

                    yi++;
                }
                yw += w;
            }
            for (x = 0; x < w; x++) {
                rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
                yp = -radius * w;
                for (i = -radius; i <= radius; i++) {
                    yi = Math.max(0, yp) + x;

                    sir = stack[i + radius];

                    sir[0] = r[yi];
                    sir[1] = g[yi];
                    sir[2] = b[yi];

                    rbs = r1 - Math.abs(i);

                    rsum += r[yi] * rbs;
                    gsum += g[yi] * rbs;
                    bsum += b[yi] * rbs;

                    if (i > 0) {
                        rinsum += sir[0];
                        ginsum += sir[1];
                        binsum += sir[2];
                    } else {
                        routsum += sir[0];
                        goutsum += sir[1];
                        boutsum += sir[2];
                    }

                    if (i < hm) {
                        yp += w;
                    }
                }
                yi = x;
                stackpointer = radius;
                for (y = 0; y < h; y++) {
                    // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                    pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
                            | (dv[gsum] << 8) | dv[bsum];

                    rsum -= routsum;
                    gsum -= goutsum;
                    bsum -= boutsum;

                    stackstart = stackpointer - radius + div;
                    sir = stack[stackstart % div];

                    routsum -= sir[0];
                    goutsum -= sir[1];
                    boutsum -= sir[2];

                    if (x == 0) {
                        vmin[y] = Math.min(y + r1, hm) * w;
                    }
                    p = x + vmin[y];

                    sir[0] = r[p];
                    sir[1] = g[p];
                    sir[2] = b[p];

                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];

                    rsum += rinsum;
                    gsum += ginsum;
                    bsum += binsum;

                    stackpointer = (stackpointer + 1) % div;
                    sir = stack[stackpointer];

                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];

                    rinsum -= sir[0];
                    ginsum -= sir[1];
                    binsum -= sir[2];

                    yi += w;
                }
            }
            bitmap.setPixels(pix, 0, w, 0, 0, w, h);
            return bitmap;
        }

        /**
         * @param filePath
         * @return
         */
        public static Bitmap getBitmap(String filePath) {
            return ImageLoader.getInstance().loadImageSync(
                    formatUrl(filePath));
        }

        public static Bitmap getBitmap(int drawableId) {
            return ImageLoader.getInstance().loadImageSync(new StringBuilder("drawable://").append(drawableId).toString());
        }

        /**
         * method desc：将图片地址format，适用于imageloader
         *
         * @param path
         * @return
         */
        public static String formatUrl(String path) {
            if (!Utility.isNull(path) && !path.startsWith("http") && !path.startsWith("file://")) {
                path = "file://" + path;
            }
            return path;
        }
    }

    /**
     * 对话框工具类
     */
    public static final class DialogUtility {

        /**
         * method desc：确认对话框，显示确认和取消按钮，可以给确认按钮添加点击事件
         *
         * @param context
         * @param strRes
         * @return
         * @see {@link #confirm(android.content.Context, String)}
         */
        public static DialogExt confirm(Context context, int strRes) {
            DialogExt dialog = confirm(context, context.getString(strRes));
            return dialog;
        }

        /**
         * method desc：确认对话框，显示确认和取消按钮，可以给确认按钮添加点击事件
         *
         * @param context
         * @param str
         * @return
         * @see {@link #confirm(android.content.Context, int)}
         */
        public static DialogExt confirm(Context context, String str) {
            DialogExt dialog = DialogExt.createDialog(context);
            dialog.setMessage(str);
            dialog.setTitle(R.string.s_dialog_title_tip);
            dialog.show();
            return dialog;
        }

        /**
         * method desc：确认对话框，显示确认和取消按钮，可以给确认按钮添加点击事件
         *
         * @param context
         * @param title
         * @param msg
         * @return
         * @see {@link #confirm(android.content.Context, int)}
         */
        public static DialogExt confirm(Context context, String title, String msg) {
            DialogExt dialog = DialogExt.createDialog(context);
            dialog.setMessage(msg);
            dialog.setTitle(title);
            dialog.show();
            return dialog;
        }

        /**
         * method desc：提示对话框，显示取消按钮
         *
         * @param context
         * @param strRes
         * @return
         * @see {@link #tip(android.content.Context, String)}
         */
        public static DialogExt tip(Context context, int strRes) {
            return tip(context, context.getString(strRes));
        }

        /**
         * method desc：提示对话框，显示取消按钮
         *
         * @param context
         * @param str
         * @return
         */
        public static DialogExt tip(Context context, String str) {
            DialogExt dialog = DialogExt.createDialog(context);
            try {
                dialog.setSingleBtn(DialogExt.OK);
                dialog.setMessage(str);
                dialog.setTitle(R.string.s_dialog_title_tip);
                dialog.show();
            } catch (Exception e) {

            }
            return dialog;
        }

        /**
         * method desc：自定义view的对话框
         *
         * @param context
         * @param view
         * @return
         */
        public static DialogExt customerDialog(Context context, View view) {
            DialogExt dialog = DialogExt.createDialog(context);
            dialog.setView(view);
            dialog.show();
            return dialog;
        }

        /**
         * method desc：自定义view的对话框
         *
         * @param context
         * @param layoutRes
         * @return
         */
        public static DialogExt customerDialog(Context context, int layoutRes) {
            DialogExt dialog = DialogExt.createDialog(context);
            dialog.setView(LayoutInflater.from(context)
                    .inflate(layoutRes, null));
            dialog.show();
            return dialog;
        }
    }

    public static final class CalendarUtility {

        public static final String PATTERN_YYYY_MM_DD = "yyyy-MM-dd";
        public static final String PATTERN_YY_MM_DD = "yy-MM-dd";
        //        public static final String PATTERN_YY_MM = "yy-MM";
        public static final String PATTERN_YYYY_MM = "yyyy-MM";
        public static final String PATTERN_YY_MM_DD_SLASH = "yy/M/d";
        public static final String PATTERN_YYYY_MM_DD_POINT = "yyyy.MM.dd";
        public static final String PATTERN_YYYY_C_MM_C_DD_C = "yyyy年MM月dd日";
        public static final String PATTERN_YY_C_MM_C_DD_C = "yy年M月d日";
        public static final String PATTERN_MM_C_DD_C = "m月d日";
        public static final String PATTERN_HH_MM = "HH:mm";
        public static final String PATTERN_MM_DD = "MM-dd";
        public static final String PATTERN_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
        public static final String PATTERN_YYYY_MM_DD_HH_MM_CN = "yyyy年MM月dd日 HH:mm";
        public static final String PATIENT_MM_DD_HH_MM = "MM/dd HH:mm";
        public static final String PATIENT_YYYY_MM_DD_HH_MM_SS_MIDDLE_LINE = "yyyy-MM-dd_HH_mm_ss";
        public static final String PATIENT_YYYY_MM_DD_HH_MM_SS = "yyyy/MM/dd HH:mm:ss";
        public static final String PATIENT_GMT = "EEE, dd MMM yyyy HH:mm:ss z";
        public static final String PATIENT_YYYY_MM_DD_HH_MM_SS_UNDERLINE = "yyyy-MM-dd HH:mm:ss";


        public static String getWeek(int dayOfWeek) {
            switch (dayOfWeek) {
                case 1:
                    return "周一";
                case 2:
                    return "周二";
                case 3:
                    return "周三";
                case 4:
                    return "周四";
                case 5:
                    return "周五";
                case 6:
                    return "周六";
                case 7:
                    return "周日";
                default:
                    return "";
            }
        }

        /**
         * @param prefName
         * @param key
         * @param expireTime
         * @return
         */
        public static boolean checkTimeout(Context context, String prefName, String key, long expireTime, long defaultTime) {
            long times = CommonUtility.SharedPreferencesUtility.getLong(context, prefName, key, defaultTime);
            return doCheckTime(times, expireTime);
        }

        public static boolean checkTimeout(Context context, String key, long expireTime, long defaultTime) {
            long times = CommonUtility.SharedPreferencesUtility.getLong(context, key, defaultTime);
            return doCheckTime(times, expireTime);
        }

        private static boolean doCheckTime(long times, long expireTime) {
            long currentTime = System.currentTimeMillis();
            return (times == 0L || currentTime - times > expireTime) ? true : false;
        }

        /**
         * method desc： 获取当前时间
         *
         * @return 2012-01-01
         */
        public static String getCurrentDate() {
            return new Timestamp(System.currentTimeMillis()).toString()
                    .substring(0, 10);
        }

        /**
         * 仅处理 2010-10-10 这样的日期
         *
         * @param date
         */
        public static long formatDate2Millis(String date) {
            try {
                String[] dates = date.split("-");
                return new DateTime(Integer.parseInt(dates[0]), Integer.parseInt(dates[1]), Integer.parseInt(dates[2]), 0, 0).getMillis();
            } catch (Exception e) {
                return DateTime.now().getMillis();
            }
        }

        public static long getLongMillis(String date, String formate) {
            SimpleDateFormat sdft = new SimpleDateFormat(formate);
            try {
                return sdft.parse(date).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        }


        /**
         * method desc：获取当前时间，并根据起始索引和结束索引截取字符串
         *
         * @return 2012-01-01
         */
        public static String getCurrentDate(int start, int end) {
            return new Timestamp(System.currentTimeMillis()).toString()
                    .substring(start, end);
        }

        public static String getCurrentWithoutMill() {
            return (System.currentTimeMillis() / 1000 * 1000) + "";
        }

        public static final long getLongTimeMillis(String date) {
            SimpleDateFormat sdft = new SimpleDateFormat(PATTERN_YYYY_MM_DD_HH_MM);
            try {
                return sdft.parse(date).getTime();
            } catch (Exception e) {
                return (new Date()).getTime();
            }
        }

        /**
         * method desc：计算两个日期相差的天数
         *
         * @param startTime
         * @param endTime
         * @param format
         */
        @SuppressLint("SimpleDateFormat")
        public static long dateDiff(String startTime, String endTime,
                                    String format) {
            // 按照传入的格式生成一个simpledateformate对象
            SimpleDateFormat sd = new SimpleDateFormat(format);
            long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
            long diff;
            try {
                // 获得两个时间的毫秒时间差异
                diff = sd.parse(endTime).getTime()
                        - sd.parse(startTime).getTime();
                long day = diff / nd;// 计算差多少天
                // 输出结果
                return day;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0;
        }

        /**
         * method desc：获取当前格式化的时间 ps: "yyyy-M-d HH:mm"
         *
         * @return 2012-01-01 10:10
         */
        public static String getCurrentDateFormat(String format) {
            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat(format,
                    Locale.getDefault());
            return df.format(date);
        }

        /**
         * method desc： 根据年月获取当月最大天数
         *
         * @param year
         * @param month
         * @return
         */
        public static int getMaxDay(int year, int month) {
            Calendar c = Calendar.getInstance();
            c.set(year, month, 1);
            c.add(Calendar.DAY_OF_YEAR, -1);
            return c.get(Calendar.DAY_OF_MONTH);
        }

        /**
         * method desc: 根据时间返回
         * 刚刚，1分钟前-59分钟前，1小时前-23小时前，昨天，前天，4天前-90天前，3个月前，04-20。 method desc：
         *
         * @return
         */
        public static String getTimeDifferent(long mills) {
            DateTime date = new DateTime(mills);
            long diff = System.currentTimeMillis() - date.getMillis();

            String str = "";
            if (diff > 2678400000L) {
                str = date.toString(PATTERN_YYYY_MM_DD_HH_MM);
            } else if (diff > 2592000000L) {// 30 * 24 * 60 * 60 *
                // 1000=2592000000
                // 毫秒
                str = "1个月前";
            } else if (diff > 1814400000) {// 21 * 24 * 60 * 60 *
                // 1000=1814400000 毫秒
                str = "3周前";
            } else if (diff > 1209600000) {// 14 * 24 * 60 * 60 *
                // 1000=1209600000 毫秒
                str = "2周前";
            } else if (diff > 604800000) {// 7 * 24 * 60 * 60 * 1000=604800000
                // 毫秒
                str = "1周前";
            } else if (diff > 86400000) { // 24 * 60 * 60 * 1000=86400000 毫秒
                int day = (int) Math.floor(diff / 86400000f);
                if (day == 1) {
                    str = "昨天";
                } else if (day == 2) {
                    str = "前天";
                } else {
                    str = day + "天前";
                }
            } else if (diff > 3600000) {// 60 * 60 * 1000=18000000 毫秒
                str = (int) Math.floor(diff / 3600000f) + "小时前";
            } else if (diff > 60000) {// 1 * 60 * 1000=60000 毫秒
                str = (int) Math.floor(diff / 60000) + "分钟前";
            } else {
                str = "刚刚";
            }
            return str;
        }

        public static String getTimeDiff(long date) {
            DateFormat format = new SimpleDateFormat("MM-dd",
                    Locale.getDefault());

            long diff = System.currentTimeMillis() - date;

            String str = "";
            if (diff > 2678400000L) {
                DateTime dateTime = new DateTime(date);
                String monthUnit = "";
                if (dateTime.getMonthOfYear() < 10) {
                    monthUnit = "M";
                } else {
                    monthUnit = "MM";
                }
                String dayUnit = "";
                if (dateTime.getDayOfMonth() < 10) {
                    dayUnit = "d";
                } else {
                    dayUnit = "dd";
                }
                str = dateTime.toString("yy年" + monthUnit + "月" + dayUnit + "日");
            } else if (diff > 2592000000L) {// 30 * 24 * 60 * 60 * 1000=2592000000
                // 毫秒
                str = "1个月前";
            } else if (diff > 1814400000) {// 21 * 24 * 60 * 60 * 1000=1814400000 毫秒
                str = "3周前";
            } else if (diff > 1209600000) {// 14 * 24 * 60 * 60 * 1000=1209600000 毫秒
                str = "2周前";
            } else if (diff > 604800000) {// 7 * 24 * 60 * 60 * 1000=604800000 毫秒
                str = "1周前";
            } else if (diff > 86400000) { // 24 * 60 * 60 * 1000=86400000 毫秒
                int day = (int) Math.floor(diff / 86400000f);
                if (day == 1) {
                    str = "昨天";
                } else if (day == 2) {
                    str = "前天";
                } else {
                    str = day + "天前";
                }
            } else if (diff > 3600000) {// 60 * 60 * 1000=18000000 毫秒
                str = (int) Math.floor(diff / 3600000f) + "小时前";
            } else {
                //甜甜圈的文章需求小于1小时，显示1小时前
                str = "1小时前";
            }
            return str;
        }

        /**
         * method desc: 根据时间返回
         * 刚刚，1分钟前-59分钟前，1小时前-23小时前，昨天，前天，4天前-90天前，3个月前，04-20。 method desc：
         *
         * @return
         */
        public static String formatMessageDate(long mills) {
            DateTime date = new DateTime(mills);
            String str = null;
            if (CalendarUtility.isToday(date)) {
                str = date.toString(PATTERN_HH_MM);
            } else {
                str = date.toString(PATTERN_YYYY_MM_DD_HH_MM_CN);
            }
            return str;
        }

        public static String getFormatDate(long timestamp, String format) {
            return new DateTime(timestamp).toString(format);
        }

        public static String getBirthdayByUnixTime(long unixTimestamp) {
            long timestamp = unixTimestamp * 1000;
            return new SimpleDateFormat("yyyy-MM-dd")
                    .format(new Date(timestamp));
        }

        public static int getAge(long mills) {
            int year = (int) (getBornDay(mills) / 365);
            return year;
        }

        /**
         * 获取出生后的天数
         *
         * @param mills
         * @return
         */
        public static long getBornDay(long mills) {
            DateTime start = new DateTime(mills);
            //最后一个参数如果不写的话，下面的返回值将会是错误的。
            Period p = new Period(start, DateTime.now(), PeriodType.days());
            return p.getDays();
        }


        /**
         * 换算糖尿病确证时间
         *
         * @param mills
         * @return
         */
        public static String getDiabeteAge(long mills) {
            long day = getBornDay(mills);
            int year = (int) (day / 365);
            int month = (int) ((day % 365) / 30);
            return new StringBuilder().append(year).append("年").append(month).append("个月").toString();
        }

        /**
         * @param mills
         * @return
         */
        public static String getDiabetesDay(long mills) {
            StringBuilder stringBuilder = new StringBuilder();
            long bornDay = CommonUtility.CalendarUtility.getBornDay(mills);
            CommonUtility.DebugLog.log("day" + bornDay);
            if (bornDay < 30) {
                stringBuilder.append(bornDay).append("天");
            } else if (bornDay >= 30 && bornDay < 365) {
                stringBuilder.append(bornDay / 30).append("个月");
            } else {
                long month = bornDay % 365 / 30;
                stringBuilder.append(bornDay / 365).append("年");
                if (month>0) {
                    stringBuilder.append(month).append("个月");
                }
            }
            return stringBuilder.toString();
        }

        /**
         * 自动补0
         *
         * @param num
         * @return
         */
        public static String autoPlus0(int num) {
            if (num < 10) {
                return "0" + num;
            }
            return num + "";
        }

        /**
         * 获取date为当前月的第几周
         *
         * @param date
         * @return
         */
        public static int getWeekOfMonth(Date date) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.get(Calendar.WEEK_OF_MONTH);
        }

        public static String toFormat(long times, String format) {
            return new DateTime(times).toString(format);
        }

        public static boolean isToday(DateTime date) {
            DateTime now = DateTime.now();
            return date.getYear() == now.getYear()
                    && date.getMonthOfYear() == now.getMonthOfYear()
                    && date.getDayOfMonth() == now.getDayOfMonth();
        }


        public static boolean isToday(long mills) {
            return isToday(new DateTime(mills));
        }

        public static long getToday() {
            return DateTime.now().withTime(0, 0, 0, 0).getMillis();
        }

        public static String getCurrentYearAndMonthByBirthDate(Long birthdayStr) {
            if (birthdayStr != null) {
                Calendar birthday = Calendar.getInstance();// 2010年10月12日，month从0开始
                birthday.setTimeInMillis(birthdayStr);
                Calendar now = Calendar.getInstance();
                int day = now.get(Calendar.DAY_OF_MONTH)
                        - birthday.get(Calendar.DAY_OF_MONTH);
                int month = now.get(Calendar.MONTH) - birthday.get(Calendar.MONTH);
                int year = now.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);
                // 按照减法原理，先day相减，不够向month借；然后month相减，不够向year借；最后year相减。
                if (day < 0) {
                    month -= 1;
                    now.add(Calendar.MONTH, -1);// 得到上一个月，用来得到上个月的天数。
                    day = day + now.getActualMaximum(Calendar.DAY_OF_MONTH);
                }
                if (month < 0) {
                    month = (month + 12) % 12;
                    year--;
                }
                return year + "年" + month + "月";
            } else
                return null;
        }


        /**
         * 将秒数格式化成时分秒
         *
         * @param seconds
         * @return
         */
        public static String formatSecond(int seconds) {
            StringBuilder formatStr = new StringBuilder();
            if (seconds > 60) {
                formatStr.append(seconds / 60).append("'").append(seconds % 60).append("\"");
            } else {
                formatStr.append(seconds).append("\"");
            }
            return formatStr.toString();
        }
    }

    /**
     * @author <a href="mailto:evan0502@qq.com">Evan</a>
     * @version 1.0
     * @{# CommonUtility.java Create on 2015年1月17日 下午5:47:08
     * @description json 取值的简单封装，主要是封装每个key的异常处理
     */
    public static final class JSONObjectUtility {

        public static final Gson GSON = new Gson();

        public static String optString(JSONObject object, String key) {
            try {
                String text = object.optString(key);
                return Utility.isNull(text) ? null : text;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public static <T> ArrayList<T> convertJSONArray2Array(JSONArray jsonArray, Class<T> c) {
            ArrayList<T> objects = new ArrayList<>();
            if (!CommonUtility.Utility.isNull(jsonArray)) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    objects.add(convertJSONObject2Obj(jsonArray.optJSONObject(i), c));
                }
            }
            return objects;
        }

        public static <T> ArrayList<T> convertJSONArray2Array(JSONArray jsonArray, Class<T> c, String addKey, String addValueKey) {
            ArrayList<T> objects = new ArrayList<>();
            if (!CommonUtility.Utility.isNull(jsonArray)) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    objects.add(convertJSONObject2Obj(jsonArray.optJSONObject(i), c, addKey, addValueKey));
                }
            }
            return objects;
        }

        /**
         * @param jsonArray
         * @param c
         * @param <T>
         * @return
         */
        public static <T> ArrayList<T> convertJSONArray2ArrayReflect(JSONArray jsonArray, Class<T> c) {
            ArrayList<T> objects = new ArrayList<>();
            if (!CommonUtility.Utility.isNull(jsonArray)) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    objects.add(convertJSONObject2ObjReflect(jsonArray.optJSONObject(i), c));
                }
            }
            return objects;
        }

        public static <T> T convertJSONObject2Obj(JSONObject jsonObject, Class<T> c) {
            return GSON.fromJson(jsonObject.toString(), c);
        }

        public static <T> T convertJSONObject2Obj(String jsonStr, Class<T> c) {
            return GSON.fromJson(jsonStr, c);
        }

        public static <T> T convertJSONObject2Obj(JSONObject jsonObject, Class<T> c, String addKey, String addValueKey) {
            if (!Utility.isNull(addKey) && !Utility.isNull(addValueKey)) {
                try {
                    jsonObject.put(addKey, jsonObject.optString(addValueKey));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return GSON.fromJson(jsonObject.toString(), c);
        }

        public static <T> T convertJSONObject2ObjReflect(JSONObject jsonObject, Class<T> c) {

            T o = null;
            try {
                o = c.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!Utility.isNull(jsonObject)) {
                Field[] fields = c.getDeclaredFields();
                for (Field field : fields) {
                    String fieldName = field.getName();
                    if (!fieldName.equals("serialVersionUID") && !fieldName.equals("_id")) {
                        field.setAccessible(true);
                        String fieldValue = jsonObject.optString(fieldName);
                        try {
                            field.set(o, fieldValue);
                        } catch (Exception e) {
                            DebugLog.log(fieldName + "==========error");
                        }
                    }
                }
            }
            return o;
        }

        public static String map2JSONObject(HashMap<String, Object> params) {
            StringBuilder builder = new StringBuilder();
            for (Iterator it = params.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
                if (!Utility.isNull(entry.getValue())) {
                    builder.append(entry.getKey()).append(entry.getValue());
                }
            }
            return builder.toString();
        }
    }

    /**
     * @param
     * @param
     * @return
     * @note 判断密码输入是否符合格式
     */
    public static boolean regexPassword(String pwd) {
        boolean flag = true;

        String regExp = "^[A-Za-z0-9]+$";

        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(pwd.subSequence(0, 1));

        flag = matcher.matches();
        if (flag) {
            String[] pwds = pwd.split("");
            int length = pwds.length;
            for (int i = 0; i < length; i++) {
                if (pwds[i].equals(" ")) {
                    flag = false;
                    break;
                }
            }
        }

        return flag;
    }


    /**
     * 判断是否是患者端
     *
     * @param context
     * @return
     */
    public static boolean isPatientClient(Context context) {
        if (context.getPackageName().equals("com.byb.patient")) {
            return true;
        }
        return false;
    }

}

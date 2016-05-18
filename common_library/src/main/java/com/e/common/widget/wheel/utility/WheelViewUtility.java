package com.e.common.widget.wheel.utility;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import com.e.common.utility.CommonUtility;
import com.e.common.widget.wheel.bean.WheelItemEntity;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.joda.time.DateTime;

/**
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @class WheelViewUtility.java Create on 2015-03-15 上午9:08
 * @description
 */
@EBean(scope = EBean.Scope.Singleton)
public class WheelViewUtility {

    private ThreeWheelView mWheelView;

    public static final String NO_SEPARATOR = "ABCDEFG";

    public WheelItemEntity itemEntityHour = new WheelItemEntity(), itemEntityMinute = new WheelItemEntity(),
            itemEntityYear = new WheelItemEntity(), itemEntityMonth = new WheelItemEntity(), itemEntityDay = new WheelItemEntity(),
            mItemEntityHeight = new WheelItemEntity(), mItemEntityWeight = new WheelItemEntity(), mItemEntityHemoglobin = new WheelItemEntity(),
            mItemSmokingForLong = new WheelItemEntity(), mItemSmokingFrequence = new WheelItemEntity(), mItemDdrinkingFrequence = new WheelItemEntity(),
            mItemExerciseFrequence = new WheelItemEntity(), mItemExerciseMovementTime = new WheelItemEntity();

    //日期数据集合[2010-10-10, 2010-10-11......]
    public WheelItemEntity mItemEntityDate;

    @RootContext
    Context mContext;

    @AfterInject
    void initViews() {
        mWheelView = new ThreeWheelView(mContext);

        itemEntityHour.setItemType(itemEntityHour.ITEM_TYPE_NUMBER);
        itemEntityHour.setMinValue(0);
        itemEntityHour.setMaxValue(23);
        itemEntityHour.setAutoPlus0(true);

        itemEntityMinute.setItemType(itemEntityMinute.ITEM_TYPE_NUMBER);
        itemEntityMinute.setMinValue(0);
        itemEntityMinute.setMaxValue(59);
        itemEntityMinute.setAutoPlus0(true);

        itemEntityYear.setItemType(itemEntityYear.ITEM_TYPE_NUMBER);
        itemEntityYear.setAutoPlus0(true);

        itemEntityMonth.setItemType(itemEntityMonth.ITEM_TYPE_NUMBER);
        itemEntityMonth.setMinValue(1);
        itemEntityMonth.setMaxValue(12);
        itemEntityMonth.setAutoPlus0(true);

        itemEntityDay.setItemType(itemEntityDay.ITEM_TYPE_NUMBER);
        itemEntityDay.setMinValue(1);
        itemEntityDay.setMaxValue(31);
        itemEntityDay.setAutoPlus0(true);

        mItemEntityHeight.setItemType(itemEntityHour.ITEM_TYPE_NUMBER);
        mItemEntityHeight.setMinValue(30);
        mItemEntityHeight.setMaxValue(250);

        mItemEntityWeight.setItemType(itemEntityHour.ITEM_TYPE_NUMBER);
        mItemEntityWeight.setMinValue(2);
        mItemEntityWeight.setMaxValue(250);

        mItemEntityHemoglobin.setItemType(itemEntityHour.ITEM_TYPE_NUMBER);
        mItemEntityHemoglobin.setMinValue(1);
        mItemEntityHemoglobin.setMaxValue(20);

        mItemSmokingForLong.setItemType(itemEntityHour.ITEM_TYPE_NUMBER);
        mItemSmokingForLong.setMinValue(0);
        mItemSmokingForLong.setMaxValue(100);

        mItemSmokingFrequence.setItemType(itemEntityHour.ITEM_TYPE_NUMBER);
        mItemSmokingFrequence.setMinValue(0);
        mItemSmokingFrequence.setMaxValue(100);

        mItemDdrinkingFrequence.setItemType(itemEntityHour.ITEM_TYPE_NUMBER);
        mItemDdrinkingFrequence.setMinValue(0);
        mItemDdrinkingFrequence.setMaxValue(50);

        mItemExerciseFrequence.setItemType(itemEntityHour.ITEM_TYPE_NUMBER);
        mItemExerciseFrequence.setMinValue(0);
        mItemExerciseFrequence.setMaxValue(1000);

        mItemExerciseMovementTime.setItemType(itemEntityHour.ITEM_TYPE_NUMBER);
        mItemExerciseMovementTime.setMinValue(0);
        mItemExerciseMovementTime.setMaxValue(1000);
    }

    public void setYear(int start, int end) {
        itemEntityYear.setMinValue(start);
        itemEntityYear.setMaxValue(end);
    }

    /**
     * @param activity
     * @param title          wheel view 显示的中间的文本
     * @param targetTextView
     * @param separator
     * @param listener
     * @param items
     */
    public void showWheelView(Activity activity, String title, TextView targetTextView, String separator, ThreeWheelView.OnWheelValueSelectedListener listener, WheelItemEntity... items) {
        CommonUtility.UIUtility.hideKeyboard(activity);
        mWheelView.updateItems(separator, items);
        mWheelView.setTitle(title);
        mWheelView.setOnValueSelectedListener(listener);
        mWheelView.setTargetView(targetTextView);
        mWheelView.showWheelView(activity);
    }

    public void showTime(Activity activity, String title, TextView targetTextView, String separator, ThreeWheelView.OnWheelValueSelectedListener listener) {
        showWheelView(activity, title, targetTextView, separator, listener, itemEntityHour, itemEntityMinute);
    }

    public void showTimeNotAllowFuture(Activity activity, String title, TextView targetTextView, String separator, DateTime dateTime, ThreeWheelView.OnWheelValueSelectedListener listener) {
        mWheelView.isTypeHour(true);
        mWheelView.setDateTime(dateTime);
        showWheelView(activity, title, targetTextView, separator, listener, itemEntityHour, itemEntityMinute);
    }

    /**
     * 显示年月日选择器
     *
     * @param activity
     * @param title
     * @param targetTextView
     * @param separator
     * @param dateTime
     * @param listener
     */
    public void showDateNotAllowFuture(Activity activity, String title, TextView targetTextView, String separator, DateTime dateTime, ThreeWheelView.OnWheelValueSelectedListener listener) {
        mWheelView.isTypeDate(true, false);
        initDate(activity, title, targetTextView, separator, dateTime, listener);
    }

    public void showDateAllowFuture(Activity activity, String title, TextView targetTextView, String separator, DateTime dateTime, ThreeWheelView.OnWheelValueSelectedListener listener) {
        mWheelView.isTypeDate(true, true);
        initDate(activity, title, targetTextView, separator, dateTime, listener);
    }

    private void initDate(Activity activity, String title, TextView targetTextView, String separator, DateTime dateTime, ThreeWheelView.OnWheelValueSelectedListener listener) {
        mWheelView.setDateTime(dateTime);
        if (CommonUtility.Utility.isNull(mItemEntityDate)) {
            mItemEntityDate = new WheelItemEntity();
            mItemEntityDate.setItemType(mItemEntityDate.ITEM_TYPE_STRING);
            DateTime now = DateTime.now();
            String[] entities = new String[365];
            for (int i = 364; i >= 0; i--) {
                entities[364 - i] = now.minusDays(i).toString(CommonUtility.CalendarUtility.PATTERN_YYYY_MM_DD);
            }
            mItemEntityDate.setWheelItems(entities);
        }
        showWheelView(activity, title, targetTextView, separator, listener, mItemEntityDate, itemEntityHour, itemEntityMinute);
    }

    public ThreeWheelView getWheelView() {
        return mWheelView;
    }

    public boolean isShown() {
        return mWheelView.isShown();
    }

    public void removeWheelView() {
        mWheelView.removeViews();
    }
}

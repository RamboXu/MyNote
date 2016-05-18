package com.e.common.widget.wheel.utility;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.e.common.R;
import com.e.common.utility.CommonUtility;
import com.e.common.widget.wheel.OnWheelScrollListener;
import com.e.common.widget.wheel.WheelView;
import com.e.common.widget.wheel.adapter.AbstractWheelTextAdapter;
import com.e.common.widget.wheel.adapter.ArrayWheelAdapter;
import com.e.common.widget.wheel.bean.WheelItemEntity;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @class ThreeWheelView.java Create on 2015-03-14 下午8:01
 * @description
 */
public class ThreeWheelView extends LinearLayout implements View.OnClickListener, OnWheelScrollListener {

    private Context mContext;

    private WheelView mWheelView1, mWheelView2, mWheelView3;

    private AbstractWheelTextAdapter mAdapter1, mAdapter2, mAdapter3;

    private View mCancel;

    private View mOk;

    private View mLayoutButtons;

    private TextView mTextTitle;

    //触发显示wheel view的target textview
    private TextView mTargetTextView;

    //当前显示wheel view的activity
    private Activity mActivity;

    //值的分割符，用于各个wheel view的值初始化
    private String mSeparator;

    //是否滚动实时通知
    private boolean isScrollNotify, isTypeTime, isAllowFutureDate = true, isTypeHour, isTypeDate;

    private DateTime mDateTime; //用于isTypeHour;

    private WheelView[] mWheelViews = new WheelView[3];

    private OnWheelValueSelectedListener mOnValueSelectedListener;

    public ThreeWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ThreeWheelView(Context context) {
        super(context);
        init();
    }

    public void setOnValueSelectedListener(OnWheelValueSelectedListener listener) {
        mOnValueSelectedListener = listener;
    }

    /**
     * 初始化ui
     */
    void init() {
        mContext = getContext();

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_three_wheel, this);

        mWheelView1 = (WheelView) findViewById(R.id.wheelView_1);
        mWheelView2 = (WheelView) findViewById(R.id.wheelView_2);
        mWheelView3 = (WheelView) findViewById(R.id.wheelView_3);

        mAdapter1 = new ArrayWheelAdapter(mContext);
        mAdapter2 = new ArrayWheelAdapter(mContext);
        mAdapter3 = new ArrayWheelAdapter(mContext);

        mWheelView1.setViewAdapter(mAdapter1);
        mWheelView2.setViewAdapter(mAdapter2);
        mWheelView3.setViewAdapter(mAdapter3);

        mWheelViews[0] = mWheelView1;
        mWheelViews[1] = mWheelView2;
        mWheelViews[2] = mWheelView3;

        mWheelView1.addScrollingListener(this);
        mWheelView2.addScrollingListener(this);
        mWheelView3.addScrollingListener(this);

        mCancel = findViewById(R.id.effectBtn_cancel);
        mOk = findViewById(R.id.effectBtn_ok);
        mTextTitle = (TextView) findViewById(R.id.text_title);
        mLayoutButtons = findViewById(R.id.ll_wheelView);

        mCancel.setOnClickListener(this);
        mOk.setOnClickListener(this);
    }

    //更新数据源，根据数据源的数量来决定显示几个wheel view
    public void updateItems(String separator, WheelItemEntity... items) {
        isScrollNotify = false;
        mSeparator = separator;
        for (WheelView wheelView : mWheelViews) {
            wheelView.setVisibility(View.GONE);
        }
        for (int i = 0; i < items.length; i++) {
            WheelItemEntity itemEntity = items[i];
            //当数据源类型为int时，动态转成String数组
            boolean isAutoPlus0 = itemEntity.isAutoPlus0();
            if (itemEntity.getItemType() == itemEntity.ITEM_TYPE_NUMBER) {
                List<String> list = new ArrayList<>();
                int count = (itemEntity.getMaxValue() - itemEntity.getMinValue()) / itemEntity.getStep();
                for (int j = 0; j <= count; j++) {
                    if (isAutoPlus0) {
                        list.add(CommonUtility.CalendarUtility.autoPlus0(j * itemEntity.getStep() + itemEntity.getMinValue()));
                    } else {
                        list.add((j * itemEntity.getStep() + itemEntity.getMinValue()) + "");
                    }
                }
                mWheelViews[i].getViewAdapter().updateItems(list.toArray());
            } else if (itemEntity.getItemType() == itemEntity.ITEM_TYPE_STRING) {
                mWheelViews[i].getViewAdapter().updateItems(itemEntity.getWheelItems());
            } else if (itemEntity.getItemType() == itemEntity.ITEM_TYPE_WHEEL_ENTITY) {
                mWheelViews[i].getViewAdapter().updateItems(itemEntity.getWheelEntities());
            }
            mWheelViews[i].setVisibility(View.VISIBLE);
        }
    }

    public Object getCurrentValue(WheelView wheelView, AbstractWheelTextAdapter adapter) {
        Object o = adapter.getItemEntity(wheelView.getCurrentItem());
        return CommonUtility.Utility.isNull(o) ? "0" : o;
    }

    public void setTargetView(TextView targetTextView) {
        mTargetTextView = targetTextView;
    }

    public void setTitle(String title) {
        mTextTitle.setText(title);
    }

    public void showWheelView(Activity activity) {
        mActivity = activity;

        separatorDate();

        removeViews();
        addViews();
    }

    public void separatorDate() {
        if (!CommonUtility.Utility.isNull(mTargetTextView)) {
            try {
                String text = CommonUtility.UIUtility.getText(mTargetTextView);
                //显示类型为 2010-10-10 10:10
                if (isTypeDate) {
                    String[] date = mDateTime.toString(CommonUtility.CalendarUtility.PATTERN_YYYY_MM_DD_HH_MM).split(" ");
                    String[] time = date[1].split(mSeparator);
                    mWheelView1.setCurrentItemStr(date[0]);
                    mWheelView2.setCurrentItemStr(time[0]);
                    mWheelView3.setCurrentItemStr(time[1]);
                } else {
                    String[] splitText = text.split(mSeparator);
                    for (int i = 0; i < splitText.length; i++) {
                        mWheelViews[i].setCurrentItem(0);
                        mWheelViews[i].setCurrentItemStr(splitText[i]);
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.effectBtn_ok) {
            Object entity1 = getCurrentValue(mWheelView1, mAdapter1);
            Object entity2 = getCurrentValue(mWheelView2, mAdapter2);
            Object entity3 = getCurrentValue(mWheelView3, mAdapter3);
            if (!CommonUtility.Utility.isNull(mOnValueSelectedListener)) {
                mOnValueSelectedListener.onValueSelected(mTargetTextView, entity1, entity2, entity3);
            }
            isTypeTime = false;
            isAllowFutureDate = true;
            mDateTime = null;
            isTypeHour = false;
        }
        removeViews();
    }

    private void addViews() {
        CommonUtility.UIUtility.addView(mActivity, this, R.anim.push_bottom_in);
    }

    public void removeViews() {
        CommonUtility.UIUtility.removeView(mActivity, this);
    }

    public void isScrollNotify(boolean isScrollNotify) {
        this.isScrollNotify = isScrollNotify;
    }

    public void isTypeTime(boolean isTypeTime) {
        this.isTypeTime = isTypeTime;
    }

    public void isAllowFutureDate(boolean isAllowFutureDate) {
        this.isAllowFutureDate = isAllowFutureDate;
        this.isTypeTime = true;
    }

    /**
     * 配合时分的未来时间选择
     *
     * @param dateTime
     */
    public void setDateTime(DateTime dateTime) {
        this.mDateTime = dateTime;
    }

    public void isTypeHour(boolean isTypeHour) {
        this.isTypeHour = isTypeHour;
        this.isAllowFutureDate = false;
    }

    public void isTypeDate(boolean isTypeDate, boolean isAllowFutureDate) {
        this.isTypeDate = isTypeDate;
        this.isAllowFutureDate = isAllowFutureDate;
        LayoutParams params = (LayoutParams) mWheelView1.getLayoutParams();
        params.weight = 1.3f;
        mWheelView1.setLayoutParams(params);
    }

    public void hideButtons() {
        mLayoutButtons.setVisibility(View.GONE);
    }

    @Override
    public void onScrollingStarted(WheelView wheel) {

    }

    @Override
    public void onScrollingFinished(WheelView wheel) {
        try {
            if (isTypeDate) {
                String date = getCurrentValue(mWheelView1, mAdapter1).toString();
                String hour = getCurrentValue(mWheelView2, mAdapter2).toString();
                String minute = getCurrentValue(mWheelView3, mAdapter3).toString();

                DateTime dateTime = new DateTime(CommonUtility.CalendarUtility.getLongTimeMillis(CommonUtility.formatString(date, " ", hour, ":", minute)));
                if (dateTime.isAfterNow()) {
                    DateTime now = DateTime.now();
                    mWheelView1.setCurrentItemStr(now.toString(CommonUtility.CalendarUtility.PATTERN_YYYY_MM_DD));
                    mWheelView2.setCurrentItemStr(now.getHourOfDay() + "");
                    mWheelView3.setCurrentItemStr(now.getMinuteOfHour() + "");
                }
            } else {
                int entity1 = Integer.parseInt(getCurrentValue(mWheelView1, mAdapter1).toString());
                int entity2 = Integer.parseInt(getCurrentValue(mWheelView2, mAdapter2).toString());
                int entity3 = Integer.parseInt(getCurrentValue(mWheelView3, mAdapter3).toString());

                if (isTypeTime) {
                    int day = new DateTime(entity1, entity2, 1, 0, 0).plusMonths(1).minusDays(1).getDayOfMonth();
                    if (entity3 > day) {
                        mWheelView3.setCurrentItem(day - 1);
                        entity3 = day;
                    }
                }

                if (!isAllowFutureDate) {
                    DateTime now = DateTime.now();
                    if (isTypeHour) {
                        DateTime dateTime = mDateTime.withTime(entity1, entity2, 0, 0);
                        if (dateTime.getMillis() > now.getMillis()) {
                            if (wheel == mWheelView1) {
                                mWheelView1.setCurrentItemStr(CommonUtility.CalendarUtility.autoPlus0(now.getHourOfDay()));
                                if (entity2 > now.getMinuteOfHour()) {
                                    mWheelView2.setCurrentItemStr(CommonUtility.CalendarUtility.autoPlus0(now.getMinuteOfHour()));
                                }
                            } else if (wheel == mWheelView2) {
                                mWheelView2.setCurrentItemStr(CommonUtility.CalendarUtility.autoPlus0(now.getMinuteOfHour()));
                            }
                        }
                    } else {
                        DateTime date = new DateTime(entity1, entity2, entity3, 0, 0);
                        if (date.getMillis() > now.getMillis()) {
                            if (wheel == mWheelView2) {
                                mWheelView2.setCurrentItemStr(CommonUtility.CalendarUtility.autoPlus0(now.getMonthOfYear()));
                                if (entity3 > now.getDayOfMonth()) {
                                    mWheelView3.setCurrentItemStr(CommonUtility.CalendarUtility.autoPlus0(now.getDayOfMonth()));
                                }
                            } else if (wheel == mWheelView3) {
                                mWheelView3.setCurrentItemStr(CommonUtility.CalendarUtility.autoPlus0(now.getDayOfMonth()));
                            } else if (wheel == mWheelView1) {
                                mWheelView2.setCurrentItemStr(CommonUtility.CalendarUtility.autoPlus0(now.getMonthOfYear()));
                                mWheelView3.setCurrentItemStr(CommonUtility.CalendarUtility.autoPlus0(now.getDayOfMonth()));
                            }
                        }
                    }
                }

                if (isScrollNotify && !CommonUtility.Utility.isNull(mOnValueSelectedListener)) {
                    mOnValueSelectedListener.onValueSelected(mTargetTextView, entity1, entity2, entity3);
                }
            }
        } catch (Exception e) {

        }
    }

    public interface OnWheelValueSelectedListener {
        void onValueSelected(TextView targetTextView, Object... value);
    }

}

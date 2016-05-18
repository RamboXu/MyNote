package com.e.common.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import com.e.common.widget.effect.button.EffectColorButton;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EView;
import org.androidannotations.annotations.UiThread;

/**
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @class CountdownTextView.java Create on 2015-04-22 上午10:15
 * @description
 */
@EView
public class CountdownTextView extends EffectColorButton {

    private int mSecond = 60;

    private String mOldText;

    private int mOldColor;

    private boolean mRepeatFlag;

    public CountdownTextView(Context context) {
        super(context);
        initData();
    }

    public CountdownTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData();
    }

    public void initData() {
        mOldText = getText().toString();
        mOldColor = getTextColors().getDefaultColor();
    }


    /**
     * 设置超时时间
     * @param second
     */
    public void setMaxSecond(int second) {
        this.mSecond = second;
    }


    public void startCount() {
        mRepeatFlag = true;
        setEnabled(false);
        setTextColor(Color.WHITE);
        count();
    }

    //验证码计数器
    @Background
    void count() {
        while (mRepeatFlag) {
            updateCount();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mSecond--;
        }
    }

    @UiThread
    void updateCount() {
        if (mSecond == 0) {
            reset();
        } else {
            setText(mSecond + "秒");
        }
    }

    public void reset() {
        setText(mOldText);
        setEnabled(true);
        mSecond = 60;
        setTextColor(mOldColor);
        mRepeatFlag = false;
    }
}

package com.e.common.widget.effect.layout;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.e.common.R;
import com.e.common.utility.CommonUtility;


/**
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @{# EffectColorLinearLayout.java Create on 2014年11月21日 下午1:00:07
 * @description
 */

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class EffectColorLinearLayout extends LinearLayout {

    private Context mContext;

    private float[] radiusArr = {0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f};

    private boolean isBorder = false, isClickable, isPressedStatus;

    private boolean isBorderLeft, isBorderRight, isBorderTop, isBorderBottom, isJustBorder;

    private float mBorderStroke = 0.5f;

    private int mBgNormalColor, mBgPressedColor;

    public EffectColorLinearLayout(Context context) {
        super(context);
        mContext = context;
    }

    public EffectColorLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    public EffectColorLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {

        // 读取配置
        TypedArray array = mContext.obtainStyledAttributes(attrs,
                R.styleable.EffectColorBtn, 0, 0);

        isClickable = array.getBoolean(
                R.styleable.EffectColorBtn_effectColor_clickable, false);

        setClickable(isClickable);

        // 默认背景颜色
        mBgNormalColor = array.getColor(
                R.styleable.EffectColorBtn_effectColor_bgNormalColor,
                0x00000000);
        // 按下背景颜色
        mBgPressedColor = array.getColor(
                R.styleable.EffectColorBtn_effectColor_bgPressedColor,
                getResources().getColor(R.color.item_press));
        // 导角
        float radius = array.getDimension(
                R.styleable.EffectColorBtn_effectColor_radius, 0);

        boolean radiusSpecLeftTop = array.getBoolean(R.styleable.EffectColorBtn_effectColor_radius_spec_left_top, false);
        boolean radiusSpecLeftBottom = array.getBoolean(R.styleable.EffectColorBtn_effectColor_radius_spec_left_bottom, false);
        boolean radiusSpecRightTop = array.getBoolean(R.styleable.EffectColorBtn_effectColor_radius_spec_right_top, false);
        boolean radiusSpecRightBottom = array.getBoolean(R.styleable.EffectColorBtn_effectColor_radius_spec_right_bottom, false);

        isBorder = array.getBoolean(R.styleable.EffectColorBtn_effectColor_is_border, false);
        isBorderLeft = array.getBoolean(R.styleable.EffectColorBtn_effectColor_is_border_left, false);
        isBorderRight = array.getBoolean(R.styleable.EffectColorBtn_effectColor_is_border_right, false);
        isBorderTop = array.getBoolean(R.styleable.EffectColorBtn_effectColor_is_border_top, false);
        isBorderBottom = array.getBoolean(R.styleable.EffectColorBtn_effectColor_is_border_bottom, false);
        isJustBorder = array.getBoolean(R.styleable.EffectColorBtn_effectColor_is_just_border, false);

        mBorderStroke = array.getDimensionPixelSize(R.styleable.EffectColorBtn_effectColor_border_stroke, 1);
        isPressedStatus = array.getBoolean(R.styleable.EffectColorBtn_effectColor_is_press_status, true);

        // 初始化导角
        if (!radiusSpecLeftTop && !radiusSpecLeftBottom && !radiusSpecRightTop && !radiusSpecRightBottom) {
            radiusArr = new float[]{radius, radius, radius, radius, radius,
                    radius, radius, radius};
        } else {
            if (radiusSpecLeftTop) {
                radiusArr[0] = radius;
                radiusArr[1] = radius;
            }
            if (radiusSpecRightTop) {
                radiusArr[2] = radius;
                radiusArr[3] = radius;
            }
            if (radiusSpecLeftBottom) {
                radiusArr[6] = radius;
                radiusArr[7] = radius;
            }
            if (radiusSpecRightBottom) {
                radiusArr[4] = radius;
                radiusArr[5] = radius;
            }
        }

        setBgColor();

        array.recycle();
    }

    public void setBgColor(int[] colors) {
        mBgNormalColor = colors[0];
        mBgPressedColor = colors[1];
        setBgColor();
    }

    public void setBgColor() {
        // 默认状态
        ShapeDrawable shapeNormal;
        //如果只需要边框
        if (isJustBorder) {
            shapeNormal = new ShapeDrawable(new RoundRectShape(
                    radiusArr, new RectF(mBorderStroke, mBorderStroke, mBorderStroke, mBorderStroke), radiusArr));
        } else {
            shapeNormal = new ShapeDrawable(new RoundRectShape(
                    radiusArr, null, radiusArr));
        }
        shapeNormal.getPaint().setColor(mBgNormalColor);

        Drawable[] drawableNormal;
        RectF rectF = null;
        if (isBorder) {
            rectF = new RectF(mBorderStroke, mBorderStroke, mBorderStroke, mBorderStroke);
        } else if (isBorderLeft || isBorderRight || isBorderTop || isBorderBottom) {
            rectF = new RectF(0.1f, 0.1f, 0.1f, 0.1f);
            if (isBorderLeft) {
                rectF.left = mBorderStroke;
            }
            if (isBorderRight) {
                rectF.right = mBorderStroke;
            }
            if (isBorderTop) {
                rectF.top = mBorderStroke;
            }
            if (isBorderBottom) {
                rectF.bottom = mBorderStroke;
            }
        }

        if (!CommonUtility.Utility.isNull(rectF)) {
            // border
            ShapeDrawable border = new ShapeDrawable(new RoundRectShape(
                    radiusArr, rectF, radiusArr));
            border.getPaint().setColor(mBgPressedColor);
            drawableNormal = new Drawable[2];
            drawableNormal[1] = border;
            drawableNormal[0] = shapeNormal;
        } else {
            drawableNormal = new Drawable[1];
            drawableNormal[0] = shapeNormal;
        }


        LayerDrawable normal = new LayerDrawable(drawableNormal);

        // 点击状态
        ShapeDrawable shapePressed;
        if ((!isClickable && isJustBorder) || !isPressedStatus) {
            shapePressed = shapeNormal;
        } else {
            shapePressed = new ShapeDrawable(new RoundRectShape(
                    radiusArr, null, radiusArr));
            shapePressed.getPaint().setColor(mBgPressedColor);
        }

        Drawable[] drawablePressed = {shapePressed};
        LayerDrawable pressed = new LayerDrawable(drawablePressed);

        // 不可点击状态
        ShapeDrawable shapeDisable = new ShapeDrawable(new RoundRectShape(
                radiusArr, null, null));
        shapeDisable.getPaint().setColor(Color.argb(150, Color.red(mBgPressedColor), Color.green(mBgPressedColor), Color.blue(mBgPressedColor)));

        Drawable[] drawableDisable = {shapeDisable};
        LayerDrawable disabled = new LayerDrawable(drawableDisable);

        StateListDrawable bgStates = new StateListDrawable();

        bgStates.addState(new int[]{android.R.attr.state_pressed,
                android.R.attr.state_enabled}, pressed);
        bgStates.addState(new int[]{android.R.attr.state_focused,
                android.R.attr.state_enabled}, pressed);

        bgStates.addState(new int[]{android.R.attr.state_enabled}, normal);
        bgStates.addState(new int[]{-android.R.attr.state_enabled}, disabled);

        int bottom = getPaddingBottom();
        int top = getPaddingTop();
        int right = getPaddingRight();
        int left = getPaddingLeft();

        // set Background
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
            setBackgroundDrawable(bgStates);
        else
            setBackground(bgStates);

        setPadding(left, top, right, bottom);
    }

    public void setNormalColor(int color) {
        mBgNormalColor = color;
    }

    public int getNormalColor() {
        return mBgNormalColor;
    }

    public void setPressedColor(int color) {
        mBgPressedColor = color;
    }

    public int getPressedColor() {
        return mBgPressedColor;
    }

    public void setBorderLeft() {
        this.isBorderLeft = true;
    }

    public void setBorderRight() {
        this.isBorderRight = true;
    }

    public void setBorderBottom() {
        this.isBorderBottom = true;
    }

    public void setBorderTop() {
        this.isBorderTop = true;
    }

    public void disableClickable() {
        isClickable = false;
    }

    public void setIsJustBorder(boolean isJustBorder) {
        this.isJustBorder = isJustBorder;
    }

    public void setIsBorder(boolean isBorder) {
        this.isBorder = isBorder;
    }

}

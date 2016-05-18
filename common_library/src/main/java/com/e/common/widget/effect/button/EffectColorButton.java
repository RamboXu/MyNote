package com.e.common.widget.effect.button;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
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
import android.widget.TextView;

import com.e.common.R;
import com.e.common.utility.CommonUtility;


/**
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @{# EffectColorButton.java Create on 2014年11月21日 下午1:00:07
 * @description
 */

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class EffectColorButton extends TextView {

    private Context mContext;

    private float[] radiusArr = {0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f};

    private boolean isBorder = false, isClickable, isPressedStatus;

    private boolean isBorderLeft, isBorderRight, isBorderTop, isBorderBottom, isJustBorder;

    private boolean isRadiusSpecLeftTop, isRadiusSpecLeftBottom, isRadiusSpecRightTop, isRadiusSpecRightBottom;

    private float mBorderStroke = 0.5f;

    private int mBgNormalColor, mBgPressedColor;

    private int mTextNormalColor, mTextPressedColor;

    private boolean isChecked; //是否被选中
    private boolean isJustBorderInCheckedState; //当应用了check时起作用

    public EffectColorButton(Context context) {
        super(context);
        mContext = context;
    }

    public EffectColorButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {

        // 读取配置
        TypedArray array = mContext.obtainStyledAttributes(attrs,
                R.styleable.EffectColorBtn, 0, 0);

        isClickable = array.getBoolean(
                R.styleable.EffectColorBtn_effectColor_clickable, true);

        setClickable(isClickable);

        // 默认背景颜色
        mBgNormalColor = array.getColor(
                R.styleable.EffectColorBtn_effectColor_bgNormalColor,
                0xffffffff);
        // 按下背景颜色
        mBgPressedColor = array.getColor(
                R.styleable.EffectColorBtn_effectColor_bgPressedColor,
                getResources().getColor(R.color.item_press));
        // 导角
        float radius = array.getDimension(
                R.styleable.EffectColorBtn_effectColor_radius, 0);

        // 默认字体颜色
        mTextNormalColor = array.getColor(
                R.styleable.EffectColorBtn_effectColor_textNormalColor,
                getTextColors().getDefaultColor());
        // 按下字体颜色
        mTextPressedColor = array.getColor(
                R.styleable.EffectColorBtn_effectColor_textPressedColor,
                mTextNormalColor);

        isRadiusSpecLeftTop = array.getBoolean(R.styleable.EffectColorBtn_effectColor_radius_spec_left_top, false);
        isRadiusSpecLeftBottom = array.getBoolean(R.styleable.EffectColorBtn_effectColor_radius_spec_left_bottom, false);
        isRadiusSpecRightTop = array.getBoolean(R.styleable.EffectColorBtn_effectColor_radius_spec_right_top, false);
        isRadiusSpecRightBottom = array.getBoolean(R.styleable.EffectColorBtn_effectColor_radius_spec_right_bottom, false);

        //点击后内容填充成边框颜色
        isBorder = array.getBoolean(R.styleable.EffectColorBtn_effectColor_is_border, false);
        isBorderLeft = array.getBoolean(R.styleable.EffectColorBtn_effectColor_is_border_left, false);
        isBorderRight = array.getBoolean(R.styleable.EffectColorBtn_effectColor_is_border_right, false);
        isBorderTop = array.getBoolean(R.styleable.EffectColorBtn_effectColor_is_border_top, false);
        isBorderBottom = array.getBoolean(R.styleable.EffectColorBtn_effectColor_is_border_bottom, false);
        //点击无效果，只有边框
        isJustBorder = array.getBoolean(R.styleable.EffectColorBtn_effectColor_is_just_border, false);

        mBorderStroke = array.getDimensionPixelSize(R.styleable.EffectColorBtn_effectColor_border_stroke, 1);
        isPressedStatus = array.getBoolean(R.styleable.EffectColorBtn_effectColor_is_press_status, true);
        isChecked = array.getBoolean(R.styleable.EffectColorBtn_effectColor_checked, false);

        isJustBorderInCheckedState = isJustBorder;

        setRadiusArr(radius);

        setCustomTextColor(new int[]{mTextPressedColor, mTextNormalColor, mTextNormalColor});

        if (isChecked) {
            setChecked(isChecked);
        } else {
            setBgColor();
        }

        array.recycle();
    }

    public void setRadiusArr(float radius) {
        // 初始化导角
        if (!isRadiusSpecLeftTop && !isRadiusSpecLeftBottom && !isRadiusSpecRightTop && !isRadiusSpecRightBottom) {
            radiusArr = new float[]{radius, radius, radius, radius, radius,
                    radius, radius, radius};
        } else {
            if (isRadiusSpecLeftTop) {
                radiusArr[0] = radius;
                radiusArr[1] = radius;
            }
            if (isRadiusSpecRightTop) {
                radiusArr[2] = radius;
                radiusArr[3] = radius;
            }
            if (isRadiusSpecLeftBottom) {
                radiusArr[6] = radius;
                radiusArr[7] = radius;
            }
            if (isRadiusSpecRightBottom) {
                radiusArr[4] = radius;
                radiusArr[5] = radius;
            }
        }
    }

    /**
     * 动态设置字体颜色
     *
     * @param colors
     */
    public void setCustomTextColor(int[] colors) {
        int[][] textColorStates = new int[3][];
        textColorStates[0] = new int[]{android.R.attr.state_pressed,
                android.R.attr.state_enabled};
        textColorStates[1] = new int[]{android.R.attr.state_enabled};
        textColorStates[2] = new int[]{-android.R.attr.state_enabled};
        ColorStateList textColorList = new ColorStateList(textColorStates,
                colors);
        setTextColor(textColorList);
    }

    public void setPressedStatus(boolean pressedStatus) {
        isPressedStatus = pressedStatus;
        setBgColor();

    }

    public void isBorder(boolean isBorder) {
        this.isBorder = isBorder;
        setBgColor();
    }

    public void isJustBorder(boolean isJustBorder) {
        this.isJustBorder = isJustBorder;
        setBgColor();
    }

    public void setBgColor(int[] colors) {
        mBgNormalColor = colors[0];
        mBgPressedColor = colors[1];
        setBgColor();
    }

    public void setBorderStroke(int stroke) {
        this.mBorderStroke = CommonUtility.UIUtility.dip2px(getContext(), stroke);
    }

    @Override
    public void setClickable(boolean clickable) {
        super.setClickable(clickable);
        isClickable = clickable;
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

    //参照radioButton写的
    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        int normalColor = mBgNormalColor, pressedColor = mBgPressedColor;
        int normalTextColor = mTextNormalColor, pressedTextColor = mTextPressedColor;
        if (isChecked) {
            normalColor = mBgPressedColor;
            pressedColor = mBgNormalColor;

            normalTextColor = mTextPressedColor;
            pressedTextColor = mTextNormalColor;
            isJustBorder = false;
        } else {
            isJustBorder = isJustBorderInCheckedState;
        }
        setBgColor(new int[]{normalColor, pressedColor});
        setCustomTextColor(new int[]{pressedTextColor, normalTextColor, normalTextColor});
        this.isChecked = isChecked;
    }
}

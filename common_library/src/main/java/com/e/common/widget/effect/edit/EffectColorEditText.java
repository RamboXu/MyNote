package com.e.common.widget.effect.edit;

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
import android.widget.EditText;

import com.e.common.R;
import com.e.common.utility.CommonUtility;

/**
 * Created by mark on 15/6/11.
 */
public class EffectColorEditText extends EditText{

    private Context mContext;

    private float[] radiusArr = {0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f};

    private boolean isBorder = false, isClickable, isPressedStatus;

    private boolean isBorderLeft, isBorderRight, isBorderTop, isBorderBottom, isJustBorder;

    private float mBorderStroke = 0.5f;

    public EffectColorEditText(Context context) {
        super(context);
        mContext = context;
    }

    public EffectColorEditText(Context context, AttributeSet attrs) {
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
        int bgNormalColor = array.getColor(
                R.styleable.EffectColorBtn_effectColor_bgNormalColor,
                0xffffffff);
        // 按下背景颜色
        int bgPressedColor = array.getColor(
                R.styleable.EffectColorBtn_effectColor_bgPressedColor,
                0xffcccccc);
        // 导角
        float radius = array.getDimension(
                R.styleable.EffectColorBtn_effectColor_radius, 0);

        // 默认字体颜色
        int textNormalColor = array.getColor(
                R.styleable.EffectColorBtn_effectColor_textNormalColor,
                getTextColors().getDefaultColor());
        // 按下字体颜色
        int textPressedColor = array.getColor(
                R.styleable.EffectColorBtn_effectColor_textPressedColor,
                textNormalColor);

        boolean radiusSpecLeftTop = array.getBoolean(R.styleable.EffectColorBtn_effectColor_radius_spec_left_top, false);
        boolean radiusSpecLeftBottom = array.getBoolean(R.styleable.EffectColorBtn_effectColor_radius_spec_left_bottom, false);
        boolean radiusSpecRightTop = array.getBoolean(R.styleable.EffectColorBtn_effectColor_radius_spec_right_top, false);
        boolean radiusSpecRightBottom = array.getBoolean(R.styleable.EffectColorBtn_effectColor_radius_spec_right_bottom, false);

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

        setBgColor(new int[]{bgNormalColor, bgPressedColor});

        setTextColor(new int[]{textPressedColor, textNormalColor, textNormalColor});

        array.recycle();
    }

    public void setTextColor(int[] colors) {
        int[][] textColorStates = new int[3][];
        textColorStates[0] = new int[]{android.R.attr.state_pressed,
                android.R.attr.state_enabled};
        textColorStates[1] = new int[]{android.R.attr.state_enabled};
        textColorStates[2] = new int[]{-android.R.attr.state_enabled};
        ColorStateList textColorList = new ColorStateList(textColorStates,
                colors);
        setTextColor(textColorList);
    }

    public void setBgColor(int[] colors) {

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
        shapeNormal.getPaint().setColor(colors[0]);

        Drawable[] drawableNormal;
        RectF rectF = null;
        if (isBorder) {
            rectF = new RectF(mBorderStroke, mBorderStroke, mBorderStroke, mBorderStroke);
        } else if (isBorderLeft || isBorderRight || isBorderTop || isBorderBottom) {
            rectF = new RectF();
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
            border.getPaint().setColor(colors[1]);
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
        if((!isClickable && isJustBorder) || !isPressedStatus) {
            shapePressed = shapeNormal;
        } else {
            shapePressed = new ShapeDrawable(new RoundRectShape(
                    radiusArr, null, radiusArr));
            shapePressed.getPaint().setColor(colors[1]);
        }

        Drawable[] drawablePressed = {shapePressed};
        LayerDrawable pressed = new LayerDrawable(drawablePressed);

        // 不可点击状态
        ShapeDrawable shapeDisable = new ShapeDrawable(new RoundRectShape(
                radiusArr, null, null));
        shapeDisable.getPaint().setColor(Color.argb(150, Color.red(colors[1]), Color.green(colors[1]), Color.blue(colors[1])));

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
}

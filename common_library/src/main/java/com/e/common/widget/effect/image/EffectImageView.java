package com.e.common.widget.effect.image;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.e.common.R;
import com.e.common.utility.CommonUtility;
import com.e.common.widget.image.ImageLoaderView;

/**
 * Created by Evan on 16/3/4.
 */
public class EffectImageView extends ImageLoaderView {

    boolean isHasPressedState; //是否有按下状态

    boolean isUserTouch;

    private float mRadius; //点击效果阴影的圆角

    private Drawable mNormalDrawable, mPressedDrawable;

    Paint mPressedStatePaint = new Paint() {{
        setColor(0x33000000);
        setAntiAlias(true);
    }};

    private RectF mRect;

    public EffectImageView(Context context) {
        super(context);
        initAttributes(null);
    }

    public EffectImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttributes(attrs);
    }

    public void initAttributes(AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        // 读取配置
        TypedArray array = getContext().obtainStyledAttributes(attrs,
                R.styleable.EffectColorBtn, 0, 0);

        mNormalDrawable = array.getDrawable(
                R.styleable.EffectColorBtn_effectImage_normal);

        mPressedDrawable = array.getDrawable(
                R.styleable.EffectColorBtn_effectImage_pressed);

        if (!CommonUtility.Utility.isNull(mPressedDrawable)) {
            isHasPressedState = true;
        } else {
            setImageDrawable(mNormalDrawable);
            mRadius = array.getDimensionPixelSize(R.styleable.EffectColorBtn_effectImage_shadow_radius, 0);

            //不设置的话，onTouchEvent里面up和cancel不会触发
            setClickable(true);
        }
        array.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isHasPressedState && isUserTouch) {
            if (getWidth() > 0 && mRect == null) {
                mRect = new RectF(0, 0, getWidth(), getHeight());
            }
            canvas.drawRoundRect(mRect, mRadius, mRadius, mPressedStatePaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isHasPressedState) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    isUserTouch = true;
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    isUserTouch = false;
                    break;
            }
            if (action == MotionEvent.ACTION_DOWN
                    || action == MotionEvent.ACTION_CANCEL
                    || action == MotionEvent.ACTION_UP)
                invalidate();
        } else {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    setImageDrawable(mPressedDrawable);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    setImageDrawable(mNormalDrawable);
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!CommonUtility.Utility.isNull(mPressedDrawable) && getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            getLayoutParams().height = mPressedDrawable.getIntrinsicHeight();
        }
    }
}

package com.e.common.widget.effect.button;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageButton;

import com.e.common.R;
import com.e.common.i.IType;
import com.e.common.utility.CommonUtility.Utility;

/**
 * @{# EffectImgButton.java Create on 2014年11月21日 下午1:00:07
 * 
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @description
 *
 */
@SuppressLint("ClickableViewAccessibility")
public class EffectImgButton extends ImageButton {

	// 重新绘制的bitmap
	private Bitmap mCoverBitmap;
	// 原始背景图
	private Drawable mOldDrawable;
	// 临时bitmap对象
	private Bitmap mTempBitmap;
	// 创建心的bitmap转换成的新的drawable
	private BitmapDrawable mNewDrawable;

	// 标识效果是处理在背景图还是源图
	private boolean mEffectOnBackground = false;

	// 全局Activity类型
	private IType mType;

	private final int DEFAULT_PRESSED_COLOR = 0xffaaaaaa;
	private int mSpecifyColor = DEFAULT_PRESSED_COLOR;

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public EffectImgButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public EffectImgButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	/**
	 * @param context
	 */
	public EffectImgButton(Context context) {
		super(context);
		init(context, null);
	}

	/**
	 * method desc： if src getDrawable() -->
	 * android.graphics.drawable.BitmapDrawable@41d7f778 getBackground() -->
	 * android.graphics.drawable.StateListDrawable@41d6a280 else background
	 * getDrawable() --> null getBackground() -->
	 * android.graphics.drawable.BitmapDrawable@420b3950
	 * 
	 * @param context
	 */

	private void init(Context context, AttributeSet attrs) {

		mType = (IType) context;

		if (!Utility.isNull(attrs)) {
			TypedArray array = context.obtainStyledAttributes(attrs,
					R.styleable.EffectImageBtn, 0, 0);
			mSpecifyColor = array.getColor(
					R.styleable.EffectImageBtn_effectImage_pressedColor,
					DEFAULT_PRESSED_COLOR);
			array.recycle();
		}
	}

    public void setBackgroundResource(int resId) {
        super.setBackgroundResource(resId);
        if(!Utility.isNull(mCoverBitmap)) {
            mCoverBitmap.recycle();
            mOldDrawable = null;
            mCoverBitmap = null;
        }
    }

	public Bitmap getAlphaBitmap(Bitmap mBitmap, int mColor) {
		Bitmap mAlphaBitmap = Bitmap.createBitmap(mBitmap.getWidth(),
				mBitmap.getHeight(), Config.ARGB_8888);

		Canvas mCanvas = new Canvas(mAlphaBitmap);
		Paint mPaint = new Paint();

		mPaint.setColor(mColor);
		// 从原位图中提取只包含alpha的位图
		Bitmap alphaBitmap = mBitmap.extractAlpha();
		// 在画布上（mAlphaBitmap）绘制alpha位图
		mCanvas.drawBitmap(alphaBitmap, 0, 0, mPaint);

		mType.addBitmap(alphaBitmap);

		return mAlphaBitmap;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		try {
			super.onDraw(canvas);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:

			//第一次初始化按下的状态
			if (Utility.isNull(mCoverBitmap)) {
				mOldDrawable = getDrawable();
				if (Utility.isNull(mOldDrawable)) {
					mEffectOnBackground = true;
					mOldDrawable = getBackground();
				}

				BitmapDrawable bd = (BitmapDrawable) mOldDrawable;
				mTempBitmap = bd.getBitmap();

				mCoverBitmap = getAlphaBitmap(mTempBitmap, mSpecifyColor);
				mNewDrawable = new BitmapDrawable(mCoverBitmap);
				// 将bitmap放入list，生命周期与activity绑定
				mType.addBitmap(mCoverBitmap);
			}

			if (mEffectOnBackground) {
				setBackgroundDrawable(mNewDrawable);
			} else {
				setImageBitmap(mCoverBitmap);
			}
			break;
		case MotionEvent.ACTION_CANCEL:
			if (mEffectOnBackground) {
				setBackgroundDrawable(mOldDrawable);
			} else {
				setImageDrawable(mOldDrawable);
			}
			break;
		case MotionEvent.ACTION_UP:
			if (mEffectOnBackground) {
				setBackgroundDrawable(mOldDrawable);
			} else {
				setImageDrawable(mOldDrawable);
			}
			break;
		}

		return super.onTouchEvent(event);
	}
}

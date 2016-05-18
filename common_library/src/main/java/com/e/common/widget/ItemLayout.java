package com.e.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.e.common.R;
import com.e.common.utility.CommonUtility;
import com.e.common.widget.effect.layout.EffectColorLinearLayout;
import com.e.common.widget.image.ImageLoaderView;

/**
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @{#} ItemView.java Create on 2014-10-9 下午10:04:16
 */

public class ItemLayout extends EffectColorLinearLayout {

    TextView mTextLeft1, mTextLeft2, mTextLeft3, mTextRight1, mTextRight2;
    ImageLoaderView mImageRight;
    ImageLoaderView mImageLeft;
    Paint mLinePaint;
    private Context mContext;
    private float mBottomLineMargin, mTopLineMargin;
    private float mBottomLineMarginRight;
    private boolean mTopLine, mBottomLine, mBothLine;
    private int mLineHeight = 2;
    private int mLineColor = 0xffeeeeee;
    // 右侧箭头
    private ImageView mImageViewForward;

    /**
     * @param context
     */
    public ItemLayout(Context context) {
        super(context);
        this.mContext = context;
        init(null);
    }

    public ItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        inflateView();
        TypedArray types = mContext.obtainStyledAttributes(attrs,
                R.styleable.ItemLayout, 0, 0);

        mTextLeft1 = (TextView) findViewById(R.id.itemLayout_text_left_1);
        mTextLeft2 = (TextView) findViewById(R.id.itemLayout_text_left_2);
        mTextRight1 = (TextView) findViewById(R.id.itemLayout_text_right_1);
        mTextRight2 = (TextView) findViewById(R.id.itemLayout_text_right_2);
        mTextLeft3 = (TextView) findViewById(R.id.itemLayout_text_left_3);

        mImageLeft = (ImageLoaderView) findViewById(R.id.itemLayout_image_left);
        mImageRight = (ImageLoaderView) findViewById(R.id.itemLayout_image_right);

        int colorLeft1 = types.getColor(R.styleable.ItemLayout_itemLayout_left1_textColor, Color.BLACK);
        int colorLeft2 = types.getColor(R.styleable.ItemLayout_itemLayout_left2_textColor, Color.BLACK);
        int colorLeft3 = types.getColor(R.styleable.ItemLayout_itemLayout_left3_textColor, Color.BLACK);
        int colorRight1 = types.getColor(R.styleable.ItemLayout_itemLayout_right1_textColor, Color.BLACK);
        int colorRight2 = types.getColor(R.styleable.ItemLayout_itemLayout_right2_textColor, Color.BLACK);

        mTextLeft1.setTextColor(colorLeft1);
        mTextLeft2.setTextColor(colorLeft2);
        mTextLeft3.setTextColor(colorLeft3);
        mTextRight1.setTextColor(colorRight1);
        mTextRight2.setTextColor(colorRight2);

        int normalTextSize = getResources().getDimensionPixelSize(R.dimen.size_sp_17);
        int textSizeLeft1 = types.getDimensionPixelSize(R.styleable.ItemLayout_itemLayout_left1_textSize, normalTextSize);
        int textSizeLeft2 = types.getDimensionPixelSize(R.styleable.ItemLayout_itemLayout_left2_textSize, normalTextSize);
        int textSizeLeft3 = types.getDimensionPixelSize(R.styleable.ItemLayout_itemLayout_left3_textSize, normalTextSize);
        int textSizeRight1 = types.getDimensionPixelSize(R.styleable.ItemLayout_itemLayout_right1_textSize, normalTextSize);
        int textSizeRight2 = types.getDimensionPixelSize(R.styleable.ItemLayout_itemLayout_right2_textSize, normalTextSize);

        mTextLeft1.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeLeft1);
        mTextLeft2.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeLeft2);
        mTextLeft3.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeLeft3);
        mTextRight1.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeRight1);
        mTextRight2.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeRight2);

        String strHintLeft1 = types.getString(R.styleable.ItemLayout_itemLayout_left1_textHint);
        String strHintLeft2 = types.getString(R.styleable.ItemLayout_itemLayout_left2_textHint);
        String strHintLeft3 = types.getString(R.styleable.ItemLayout_itemLayout_left3_textHint);
        String strHintRight1 = types.getString(R.styleable.ItemLayout_itemLayout_right1_textHint);
        String strHintRight2 = types.getString(R.styleable.ItemLayout_itemLayout_right2_textHint);

        mTextLeft1.setHint(strHintLeft1);
        mTextLeft2.setHint(strHintLeft2);
        mTextLeft3.setHint(strHintLeft3);
        mTextRight1.setHint(strHintRight1);
        mTextRight2.setHint(strHintRight2);

        String strLeft1 = types.getString(R.styleable.ItemLayout_itemLayout_left1_text);
        String strLeft2 = types.getString(R.styleable.ItemLayout_itemLayout_left2_text);
        String strLeft3 = types.getString(R.styleable.ItemLayout_itemLayout_left3_text);
        String strRight1 = types.getString(R.styleable.ItemLayout_itemLayout_right1_text);
        String strRight2 = types.getString(R.styleable.ItemLayout_itemLayout_right2_text);

        mTextLeft1.setText(strLeft1);
        mTextLeft2.setText(strLeft2);
        mTextLeft3.setText(strLeft3);
        mTextRight1.setText(strRight1);
        mTextRight2.setText(strRight2);

        /**
         * 避免出现布局问题
         */
        if (strLeft3 == null || "".equals(strLeft3)) {
            mTextLeft3.setVisibility(GONE);
        }

        Drawable imageLeftDrawable = types.getDrawable(R.styleable.ItemLayout_itemLayout_image_left_src);
        if (!CommonUtility.Utility.isNull(imageLeftDrawable)) {
            int leftRoundCorner = types.getDimensionPixelSize(R.styleable.ItemLayout_itemLayout_image_left_roundCorner, 0);
            if (leftRoundCorner > 0) {
                mImageLeft.setmIsRoundCorner(true);
                mImageLeft.setRoundCornerRate(leftRoundCorner);
            }
            mImageLeft.setImageDrawable(imageLeftDrawable);
            int imageWidth = types.getDimensionPixelSize(R.styleable.ItemLayout_itemLayout_image_left_width, 0);
            int imageHeight = types.getDimensionPixelSize(R.styleable.ItemLayout_itemLayout_image_left_height, 0);
            if (imageWidth > 0 && imageHeight > 0) {
                ViewGroup.LayoutParams params = mImageLeft.getLayoutParams();
                params.width = imageWidth;
                params.height = imageHeight;
                mImageLeft.setLayoutParams(params);
            }

            mImageLeft.setVisibility(View.VISIBLE);
        } else {
            mImageLeft.setVisibility(View.GONE);
        }

        Drawable imageRightDrawable = types.getDrawable(R.styleable.ItemLayout_itemLayout_image_right_src);
        if (!CommonUtility.Utility.isNull(imageRightDrawable)) {
            int rightRoundCorner = types.getDimensionPixelSize(R.styleable.ItemLayout_itemLayout_image_right_roundCorner, 0);
            if (rightRoundCorner > 0) {
                mImageRight.setmIsRoundCorner(true);
                mImageRight.setRoundCornerRate(rightRoundCorner);
            }
            mImageRight.setImageDrawable(imageRightDrawable);
            int imageWidth = types.getDimensionPixelSize(R.styleable.ItemLayout_itemLayout_image_right_width, 0);
            int imageHeight = types.getDimensionPixelSize(R.styleable.ItemLayout_itemLayout_image_right_height, 0);
            if (imageWidth > 0 && imageHeight > 0) {
                ViewGroup.LayoutParams params = mImageRight.getLayoutParams();
                params.width = imageWidth;
                params.height = imageHeight;
                mImageRight.setLayoutParams(params);
            }
            mImageRight.setVisibility(View.VISIBLE);
        } else {
            mImageRight.setVisibility(View.GONE);
        }

        mBottomLineMargin = types.getDimensionPixelSize(R.styleable.ItemLayout_itemLayout_bottom_line_margin, 0);
        mTopLineMargin = types.getDimensionPixelSize(R.styleable.ItemLayout_itemLayout_top_line_margin, 0);
        mBottomLineMarginRight = types.getDimensionPixelSize(R.styleable.ItemLayout_itemLayout_bottom_line_margin_right, 0);

        boolean needArrow = types.getBoolean(R.styleable.ItemLayout_itemLayout_need_arrow,
                true);

        mImageViewForward = (ImageView) findViewById(R.id.include_arrow_right);

        if (!needArrow) {
            mImageViewForward.setVisibility(View.GONE);
        }

        mTopLine = types.getBoolean(R.styleable.ItemLayout_itemLayout_line_top, false);
        mBottomLine = types.getBoolean(R.styleable.ItemLayout_itemLayout_line_bottom, false);
        mBothLine = types.getBoolean(R.styleable.ItemLayout_itemLayout_line_both, false);
        mLineHeight = types.getDimensionPixelSize(R.styleable.ItemLayout_itemLayout_line_height, mLineHeight);
        mLineColor = types.getColor(R.styleable.ItemLayout_itemLayout_line_color, mLineColor);

        if (mTopLine || mBothLine || mBottomLine) {
            mLinePaint = new Paint();
            mLinePaint.setAntiAlias(true);
            mLinePaint.setStrokeWidth(mLineHeight);
            mLinePaint.setColor(mLineColor);
        }
        types.recycle();
    }

    public void inflateView() {
        CommonUtility.UIUtility.inflate(R.layout.layout_item_view, this);
    }

    public void setArrowVisible(boolean visible) {
        if (visible) {
            mImageViewForward.setVisibility(View.VISIBLE);
        } else {
            mImageViewForward.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!CommonUtility.Utility.isNull(mLinePaint)) {
            int width = getWidth();
            int height = getHeight();
            if (mBothLine) {
                mTopLine = true;
                mBottomLine = true;
            }
            if (mTopLine) {
                canvas.drawLine(mTopLineMargin, 0, width, 0, mLinePaint);
            }
            if (mBottomLine) {
                canvas.drawLine(mBottomLineMargin, height, width - mBottomLineMarginRight, height, mLinePaint);
            }
        }
    }

    public void setBottomLineMargin(int margin) {
        this.mBottomLineMargin = margin;
        invalidate();
    }

    public void setBottomLineMarginRight(int margin) {
        this.mBottomLineMarginRight = margin;
        invalidate();
    }

    public TextView getTextLeft1() {
        return mTextLeft1;
    }

    public TextView getTextLeft2() {
        return mTextLeft2;
    }

    public TextView getTextLeft3() {
        return mTextLeft3;
    }

    public TextView getTextRight1() {
        return mTextRight1;
    }

    public TextView getTextRight2() {
        return mTextRight2;
    }

    public ImageLoaderView getImageLeft() {
        return mImageLeft;
    }

    public ImageLoaderView getImageRight() {
        return mImageRight;
    }
}

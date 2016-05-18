package com.xuchunyu.mynote;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by xuchunyu on 16/5/16.
 */
public class PyramidTableView extends View {

    float mHight = 300, mWidth = 300;


    public PyramidTableView(Context context) {
        super(context);
    }

    public PyramidTableView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PyramidTableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setPercent(30, 25, 45, canvas);
    }

    //ay+bx-ab=0
    //mWidth/2*y+mHight*x-mWidth/2*mHight=0
    //a*y/2-b*x+a*b/2=0
    //2*mHight*y-2*mHight*mHight = mWidth*x-mWidth*mWidth
    //top(mWidth/2,0)
    //leftbottom(0,mHight)
    //rightbottom(mWidth,mHight)
    public void setPercent(float top, float middle, float bottom, Canvas canvas) {


        float topX = mWidth / 2;
        float topY = 0;
        float topY1 = (top / 100) * (mHight - 10);
        float topX1 = getLeftDotX(topY1);
        float topX2 = getRightDotX(topY1);

        float middleY = topY1 + 5;
        float middleX = getLeftDotX(middleY);
        float middleY2 = ((top + middle) / 100) * (mHight - 10) + 5;
        float middleX2 = getLeftDotX(middleY2);
        float middleY3 = middleY2;
        float middleX3 = getRightDotX(middleY3);
        float middleY4 = middleY;
        float middleX4 = getRightDotX(middleY4);


        float bottomY = middleY2 + 5;
        float bottomX = getLeftDotX(bottomY);
        float bottomY2 = mHight;
        float bottomX2 = 0;
        float bottomY3 = bottomY2;
        float bottomX3 = mWidth;
        float bottomY4 = bottomY;
        float bottomX4 = getRightDotX(bottomY4);

        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(4);
        paint.setStyle(Paint.Style.FILL);

        Path path = new Path();
        path.moveTo(topX, topY);
        path.lineTo(topX1, topY1);
        path.lineTo(topX2, topY1);
        path.close();
        canvas.drawPath(path, paint);

        Path path1 = new Path();
        paint.setColor(Color.BLUE);
        path1.moveTo(middleX, middleY);
        path1.lineTo(middleX2, middleY2);
        path1.lineTo(middleX3, middleY3);
        path1.lineTo(middleX4, middleY4);
        path1.close();
        canvas.drawPath(path1, paint);

        Path path2 = new Path();
        paint.setColor(Color.RED);
        path2.reset();
        path2.moveTo(bottomX, bottomY);
        path2.lineTo(bottomX4, bottomY4);
        path2.lineTo(bottomX3, bottomY3);
        path2.lineTo(bottomX2, bottomY2);
        path2.lineTo(bottomX, bottomY);
        path2.close();
        canvas.drawPath(path2, paint);

        System.out.println("x==" + bottomX + " " + bottomX2 + " " + bottomX3 + " " + bottomX4);
        System.out.println("Y==" + bottomY + " " + bottomY2 + " " + bottomY3 + " " + bottomY4);
    }

    public float getRightDotX(float y) {
        float x = mWidth / 2 + (mWidth * y) / (2 * mHight);
        return x;
    }

    public float getLeftDotX(float y) {
        float x = (mWidth * mHight - mWidth * y) / (2 * mHight);
        return x;
    }
}

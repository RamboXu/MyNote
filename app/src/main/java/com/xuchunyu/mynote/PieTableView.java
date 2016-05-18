package com.xuchunyu.mynote;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuchunyu on 16/5/17.
 */
public class PieTableView extends View {


    public PieTableView(Context context) {
        super(context);
    }

    public PieTableView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PieTableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        List<Double> degree = new ArrayList<>();
        degree.add(22.67);
        degree.add(6.00);
        degree.add(25.33);
        degree.add(6.33);
        degree.add(27.67);
        degree.add(12.00);
        setPercent(canvas, degree);
    }

    public void setPercent(Canvas canvas, List<Double> degree) {
        float allDegree;
        double allLineDegree = 0;
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        paint.setStrokeWidth(5);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        RectF oval1 = new RectF(0, 0, 500, 500);
        paint.setColor(Color.RED);
        canvas.drawArc(oval1, 0, (float) (degree.get(0) / 100 * 360), true, paint);
        allDegree = degree.get(0).floatValue();

        paint.setColor(Color.BLACK);
        canvas.drawArc(oval1, allDegree / 100 * 360, (float) (degree.get(1) / 100 * 360), true, paint);
        allDegree = allDegree + degree.get(1).floatValue();

        paint.setColor(Color.GREEN);
        canvas.drawArc(oval1, allDegree / 100 * 360, (float) (degree.get(2) / 100 * 360), true, paint);
        allDegree = allDegree + degree.get(2).floatValue();

        paint.setColor(Color.BLUE);
        canvas.drawArc(oval1, allDegree / 100 * 360, (float) (degree.get(3) / 100 * 360), true, paint);
        allDegree = allDegree + degree.get(3).floatValue();

        paint.setColor(Color.YELLOW);
        canvas.drawArc(oval1, allDegree / 100 * 360, (float) (degree.get(4) / 100 * 360), true, paint);
        allDegree = allDegree + degree.get(4).floatValue();

        paint.setColor(Color.GRAY);
        canvas.drawArc(oval1, allDegree / 100 * 360, (float) (degree.get(5) / 100 * 360), true, paint);


        paint.setColor(Color.WHITE);
        for (double d : degree) {
            allLineDegree = d + allLineDegree;
            canvas.drawLine(250, 250, getX(allLineDegree / 100 * 360), getY(allLineDegree / 100 * 360), paint);
        }
    }

    public float getX(double degree) {
        float x = (float) (250 + 250 * Math.cos(degree * Math.PI / 180));
        return x;
    }

    public float getY(double degree) {
        float y = (float) (250 + 250 * Math.sin(degree * Math.PI / 180));
        return y;
    }
}

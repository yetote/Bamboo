package com.example.bamboo.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @author ether QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.myview
 * @class describe
 * @time 2018/11/28 18:28
 * @change
 * @chang time
 * @class describe
 */
public class TimeButton extends View {
    private Paint textPaint, circlePaint;
    private int width, height;

    public TimeButton(Context context) {
        super(context);
        init();
    }

    private void init() {
        textPaint = new Paint();
        circlePaint = new Paint();
        textPaint.setColor(Color.GRAY);
        circlePaint.setColor(Color.GRAY);
        circlePaint.setStrokeWidth(10);
        circlePaint.setStyle(Paint.Style.STROKE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(50);
        circlePaint.setAntiAlias(true);
    }

    public TimeButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TimeButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        if (width != height) {
            if (width > height) {
                width = height;
            } else {
                height = width;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle((getRight() - getLeft()) / 2, (getBottom() - getTop()) / 2, width / 2 - 20, circlePaint);
//        canvas.drawLine(0, height/2 , width, height/2 , circlePaint);
//        canvas.drawLine(width/2, 0, width/2, height, circlePaint);
        canvas.drawText("3", (getRight() - getLeft()) / 2, (getBottom() - getTop()) / 2+textPaint.getTextSize()/3, textPaint);
    }
}

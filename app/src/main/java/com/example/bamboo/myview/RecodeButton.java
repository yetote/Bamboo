package com.example.bamboo.myview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.example.bamboo.R;

import androidx.annotation.Nullable;

/**
 * @author ether QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.myview
 * @class 录制按钮
 * @time 2018/12/24 14:09
 * @change
 * @chang time
 * @class describe
 */
public class RecodeButton extends View {
    private Context context;
    private Paint smallPaint, largePaint, progressPaint;
    private int width, height;
    private int radius;
    private int angle;
    private boolean isStart = false;
    private static final String TAG = "RecodeButton";

    public RecodeButton(Context context) {
        super(context);
        this.context = context;
//        init();
    }

    public void setAngle(int angle) {
        this.angle = angle;
        invalidate();
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    public RecodeButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    public RecodeButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RecodeButton);
        radius = ta.getInteger(R.styleable.RecodeButton_radius, 0);
        ta.recycle();
        smallPaint = new Paint();
        smallPaint.setColor(Color.WHITE);
        largePaint = new Paint();
        largePaint.setColor(context.getResources().getColor(R.color.floralwhite));
        progressPaint = new Paint();
        progressPaint.setColor(context.getResources().getColor(R.color.skyblue));
        progressPaint.setStrokeWidth(10);
        progressPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isStart) {
            canvas.drawCircle(width / 2, height / 2, radius + 10, smallPaint);
            canvas.drawArc(width / 2 - radius - 9, height / 2 - radius - 9, (width / 2 + radius + 11), (height / 2 + radius + 11), -90, angle, false, progressPaint);
            Log.e(TAG, "onDraw: " + angle);
        } else {
            canvas.drawCircle(width / 2, height / 2, radius, largePaint);
            canvas.drawCircle(width / 2, height / 2, radius - 20, smallPaint);
        }
    }

    public void showAnimation() {
        if (isStart) {
            ObjectAnimator oa = ObjectAnimator.ofInt(this, "angle", 0, 360);
            oa.setInterpolator(new LinearInterpolator());
            oa.setDuration(10000);
            oa.start();
        }
    }
}

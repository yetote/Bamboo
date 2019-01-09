package com.example.bamboo.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.example.bamboo.R;

import androidx.annotation.Nullable;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.myview
 * @class describe
 * @time 2019/1/9 9:31
 * @change
 * @chang time
 * @class describe
 */
public abstract class AnimationButton extends View {
    private Drawable drawable;
    private Paint textPaint;
    private Context context;
    private String text;
    private int w, h;

    public AnimationButton(Context context) {
        super(context);
    }

    public AnimationButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    public AnimationButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    /**
     * 点击事件
     */
    public abstract void onClick();

    public void init(AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AnimationButton);
        text = ta.getString(R.styleable.AnimationButton_text);
        ta.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        w = MeasureSpec.getMode(widthMeasureSpec);
        h = MeasureSpec.getMode(heightMeasureSpec);

    }
}

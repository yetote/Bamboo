package com.example.bamboo.myview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.myview
 * @class describe
 * @time 2019/1/30 11:02
 * @change
 * @chang time
 * @class describe
 */
public class RectImage extends AppCompatImageView {
    public RectImage(Context context) {
        super(context);
    }

    public RectImage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RectImage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, width);
    }
}

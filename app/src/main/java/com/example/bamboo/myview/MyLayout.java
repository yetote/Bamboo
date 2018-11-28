package com.example.bamboo.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.util
 * @class 流式标签布局
 * @time 2018/11/11 15:16
 * @change
 * @chang time
 * @class describe
 */
public class MyLayout extends ViewGroup {
    public MyLayout(Context context) {
        super(context);
    }

    public MyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int childWidthSpec, childHeightSpec;
        int usedWidthSize = 0, usedHeightSize = 0;

        int selfWidthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int selfWidthSpecSize = MeasureSpec.getSize(widthMeasureSpec);

        int selfHeightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int selfHeightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            LayoutParams lp = v.getLayoutParams();
            switch (lp.width) {
                case MATCH_PARENT:
                    if (selfWidthSpecMode == MeasureSpec.EXACTLY || selfWidthSpecMode == MeasureSpec.AT_MOST) {
                        childWidthSpec = MeasureSpec.makeMeasureSpec(selfWidthSpecSize - usedWidthSize, MeasureSpec.EXACTLY);
                    } else {
                        childWidthSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                    }
                    break;
                case WRAP_CONTENT:
                    if (selfWidthSpecMode == MeasureSpec.EXACTLY || selfWidthSpecMode == MeasureSpec.AT_MOST) {
                        childWidthSpec = MeasureSpec.makeMeasureSpec(selfWidthSpecSize - usedWidthSize, MeasureSpec.AT_MOST);
                    } else {
                        childWidthSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                    }
                    break;
                default:
                    childWidthSpec = MeasureSpec.makeMeasureSpec(lp.width, MeasureSpec.EXACTLY);
                    break;
            }
            usedWidthSize += childWidthSpec;
            // TODO: 2018/11/11 当子view的宽度大于可用宽度的时候，未作处理
            switch (lp.height) {
                case MATCH_PARENT:
                    if (selfHeightSpecMode == MeasureSpec.EXACTLY || selfHeightSpecMode == MeasureSpec.AT_MOST) {
                        childHeightSpec = MeasureSpec.makeMeasureSpec(selfHeightSpecSize - usedWidthSize, MeasureSpec.EXACTLY);
                    } else {
                        childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                    }
                    break;
                case WRAP_CONTENT:
                    if (selfHeightSpecMode == MeasureSpec.EXACTLY || selfHeightSpecMode == MeasureSpec.AT_MOST) {
                        childHeightSpec = MeasureSpec.makeMeasureSpec(selfHeightSpecSize - usedHeightSize, MeasureSpec.AT_MOST);
                    } else {
                        childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                    }
                    break;
                default:
                    childHeightSpec = MeasureSpec.makeMeasureSpec(lp.height, MeasureSpec.EXACTLY);
                    break;
            }
            if (usedWidthSize == selfWidthSpecSize) {
                usedHeightSize += childHeightSpec;
            }
            setMeasuredDimension(childWidthSpec, childHeightSpec);
        }
        // TODO: 2018/11/11 标签不等高未完成
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}

package com.example.bamboo.myview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.bamboo.R;

import androidx.annotation.Nullable;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.myview
 * @class describe
 * @time 2019/1/9 9:47
 * @change
 * @chang time
 * @class describe
 */
public class TestView extends View {
    private Drawable drawable;
    private Context context;
    private Paint paint;
    int w, h;
    private static final String TAG = "TestView";

    public TestView(Context context) {
        super(context);
        this.context = context;
        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.mediumseagreen));
    }

    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.mediumseagreen));
    }

    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.mediumseagreen));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        w = MeasureSpec.getSize(widthMeasureSpec);
        h = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.boss);
//        bitmap.setWidth(w);
//        bitmap.setHeight(h);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Log.e(TAG, "onDraw: w:" + w + "\n" +
                "onDraw: h:" + h + "\n" +
                "onDraw: width :" + width + "\n" +
                "onDraw: height:" + height + "\n");
        canvas.save();
        canvas.scale(w / width, h / height);
        canvas.drawBitmap(bitmap, 0, 0, paint);
//        canvas.scale(width/w, height/h);
        canvas.restore();

        bitmap.recycle();
    }
}

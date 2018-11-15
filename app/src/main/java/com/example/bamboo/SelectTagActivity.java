package com.example.bamboo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.example.bamboo.opengl.SelectTagRenderer;

/**
 * @author yetote
 * @decription 选择标签
 */
public class SelectTagActivity extends AppCompatActivity {
    private GLSurfaceView glSurfaceView;
    private SelectTagRenderer renderer;
    private static final String TAG = "SelectTagActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tag);
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        // 屏幕宽度（像素）
        int width = dm.widthPixels;
        // 屏幕高度（像素）
        int height = dm.heightPixels;

        init(width, height);
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(renderer);
        glSurfaceView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                glSurfaceView.queueEvent(() -> renderer.tagClick(event.getX(), event.getY()));
            }
            return false;
        });
    }

    private void init(int width, int height) {
        glSurfaceView = findViewById(R.id.select_tag_glSurfaceView);
        Log.e(TAG, "init: " + glSurfaceView.getWidth());
        renderer = new SelectTagRenderer(this, width, height);
    }

}

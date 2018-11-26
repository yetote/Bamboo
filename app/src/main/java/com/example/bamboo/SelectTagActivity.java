package com.example.bamboo;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.bamboo.opengl.SelectTagRenderer;
import com.example.bamboo.util.CoordinateTransformation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**
 * @author yetote
 * @decription 选择标签
 */
public class SelectTagActivity extends AppCompatActivity {
    private GLSurfaceView glSurfaceView;
    private SelectTagRenderer renderer;
    private static final String TAG = "SelectTagActivity";
    private Toolbar toolbar;
    View statusBar;
    private int statusHeight;
    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tag);
        getWindow().getDecorView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (statusBar == null) {
                    int identifier = getResources().getIdentifier("statusBarBackground", "id", "android");
                    statusBar = getWindow().findViewById(identifier);
                }
                statusBar.setBackgroundResource(R.drawable.toolbar_gradient_background);
                statusHeight = statusBar.getHeight();
                getWindow().getDecorView().removeOnLayoutChangeListener(this);
            }
        });
        init();
        glSurfaceView.setEGLContextClientVersion(2);

        glSurfaceView.setRenderer(renderer);

        glSurfaceView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    glSurfaceView.queueEvent(() -> renderer.tagClick(event.getX(), event.getY()));
                    break;
                default:
                    break;
            }
            return false;
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && isFirst) {
            Log.e(TAG, "onWindowFocusChanged: height " + glSurfaceView.getHeight());
            Log.e(TAG, "onWindowFocusChanged: width " + glSurfaceView.getWidth());
            CoordinateTransformation.initDisplay(glSurfaceView.getWidth(), glSurfaceView.getHeight(), glSurfaceView.getWidth(), toolbar.getHeight());
            isFirst = false;

        }
    }

    private void init() {
        toolbar = findViewById(R.id.selectTag_toolbar);
        glSurfaceView = findViewById(R.id.select_tag_glSurfaceView);
        renderer = new SelectTagRenderer(this);
    }

}

package com.example.bamboo;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
public class SelectTagActivity extends AppCompatActivity implements SensorEventListener {
    private GLSurfaceView glSurfaceView;
    private SelectTagRenderer renderer;
    private static final String TAG = "SelectTagActivity";
    private Toolbar toolbar;
    View statusBar;
    private boolean isFirst = true;
    private SensorManager sensorManager;
    private Sensor sensor;

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
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onStart() {
        super.onStart();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0]*10f;
            float y = event.values[1] * 10f;
            renderer.sensorChanged(-x, y);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

package com.example.bamboo;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.bamboo.application.MyApplication;
import com.example.bamboo.fragment.RecommendFragment;
import com.example.bamboo.myinterface.MattersInterface;
import com.example.bamboo.opengl.SelectTagRenderer;
import com.example.bamboo.util.CoordinateTransformation;
import com.example.bamboo.util.StatusBarUtils;

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
    private int[] selectArr = new int[15];
    private MattersInterface mattersInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tag);

        StatusBarUtils.changedStatusBar(this);

        init();

//        Toast.makeText(this, MyApplication.uId, Toast.LENGTH_SHORT).show();

        toolbar.inflateMenu(R.menu.select_tag_menu);

        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.select_tag_toolbar_sure:
                    int count = checkSelect(selectArr);
                    RecommendFragment fragment = new RecommendFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("count", count);
                    bundle.putIntArray("select_arr", selectArr);
                    mattersInterface = fragment;
                    mattersInterface.selectedTag(bundle);
                    finish();
                    break;
                default:
                    break;
            }
            return false;
        });

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

    /**
     * 检查选择了那些标签
     *
     * @param selectIdArr 标签选择数组
     * @return 选择了多少个标签
     */
    private int checkSelect(int[] selectIdArr) {
        int j = 0;
        for (int i = 0; i < renderer.getSelectTag().length; i++) {
            if (renderer.getSelectTag()[i].getIsSelect()) {
                selectIdArr[j++] = i;
            }
        }
        return j;
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
            float x = event.values[0] * 10f;
            float y = event.values[1] * 10f;
            renderer.sensorChanged(-x, y);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
